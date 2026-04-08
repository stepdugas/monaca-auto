package com.dealership.api.service;

import com.dealership.api.model.Car;
import com.dealership.api.model.CarImage;
import com.dealership.api.repository.CarRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service for generating Facebook Marketplace catalog XML feed.
 * Follows Meta's catalog XML specification for vehicle ads.
 *
 * Reference: https://www.facebook.com/business/help/
 */
@Service
public class FacebookFeedGenerator {

    private final CarRepository carRepository;

    @Value("${facebook.feed.base-url:http://localhost:8080}")
    private String baseUrl;

    @Value("${facebook.feed.image-base-url:http://localhost:8080}")
    private String imageBaseUrl;

    public FacebookFeedGenerator(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    /**
     * Generate Facebook Marketplace catalog XML feed as a string.
     * Only includes cars with status="available".
     *
     * @return XML string conforming to Facebook catalog schema
     */
    public String generateFeed() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // Root element: rss
            Element rss = doc.createElement("rss");
            rss.setAttribute("version", "2.0");
            rss.setAttribute("xmlns:g", "http://base.google.com/ns/1.0");
            doc.appendChild(rss);

            // Channel element
            Element channel = doc.createElement("channel");
            rss.appendChild(channel);

            // Channel metadata
            addElement(channel, "title", "Dealership Vehicles");
            addElement(channel, "link", baseUrl);
            addElement(channel, "description", "Latest available vehicles from our dealership");
            addElement(channel, "lastBuildDate", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

            // Fetch all available cars
            List<Car> cars = carRepository.findAllByStatus("available");

            // Add each car as an item (product)
            for (Car car : cars) {
                Element item = doc.createElement("item");
                channel.appendChild(item);

                addCarItem(doc, item, car);
            }

            // Convert document to string
            return documentToString(doc);

        } catch (Exception e) {
            throw new RuntimeException("Error generating Facebook feed: " + e.getMessage(), e);
        }
    }

    /**
     * Add a car listing as an XML item element.
     *
     * @param doc The XML document
     * @param item The item element to populate
     * @param car The car entity to convert
     */
    private void addCarItem(Document doc, Element item, Car car) {
        // Basic fields
        addElement(item, "title", buildTitle(car));
        addElement(item, "description", car.getDescription() != null ? car.getDescription() : "");
        addElement(item, "link", baseUrl + "/inventory/" + car.getId());

        // Price (required for Facebook Catalog)
        if (car.getPrice() != null) {
            addElement(item, "g:price", car.getPrice().toPlainString() + " USD");
        }

        // Vehicle-specific fields (using Google's namespace)
        addElement(item, "g:vehicle_make", car.getMake());
        addElement(item, "g:vehicle_model", car.getModel());
        addElement(item, "g:vehicle_year", car.getYear() != null ? car.getYear().toString() : "");

        // Condition (New, Used, Certified)
        if (car.getCondition() != null) {
            addElement(item, "g:vehicle_condition", car.getCondition());
        }

        // Mileage
        if (car.getMileage() != null) {
            addElement(item, "g:vehicle_mileage", car.getMileage().toString());
        }

        // VIN (optional but useful)
        if (car.getVin() != null) {
            addElement(item, "g:vehicle_vin", car.getVin());
        }

        // Transmission
        if (car.getTransmission() != null) {
            addElement(item, "g:vehicle_transmission", car.getTransmission());
        }

        // Engine
        if (car.getEngine() != null) {
            addElement(item, "g:vehicle_engine", car.getEngine());
        }

        // Drive train
        if (car.getDriveTrain() != null) {
            addElement(item, "g:vehicle_drive_train", car.getDriveTrain());
        }

        // Exterior color
        if (car.getExteriorColor() != null) {
            addElement(item, "g:vehicle_exterior_color", car.getExteriorColor());
        }

        // Interior color
        if (car.getInteriorColor() != null) {
            addElement(item, "g:vehicle_interior_color", car.getInteriorColor());
        }

        // Trim
        if (car.getTrim() != null) {
            addElement(item, "g:vehicle_trim", car.getTrim());
        }

        // KBB Value (if available)
        if (car.getKbbValue() != null) {
            addElement(item, "g:vehicle_kbb_value", car.getKbbValue().toPlainString());
        }

        // Primary image (first image in list)
        if (car.getImages() != null && !car.getImages().isEmpty()) {
            CarImage primaryImage = car.getImages().get(0);
            if (primaryImage.getImageUrl() != null) {
                addElement(item, "g:image_link", primaryImage.getImageUrl());
            }
        }

        // Additional images
        if (car.getImages() != null && car.getImages().size() > 1) {
            for (int i = 1; i < car.getImages().size(); i++) {
                CarImage image = car.getImages().get(i);
                if (image.getImageUrl() != null) {
                    addElement(item, "g:additional_image_link", image.getImageUrl());
                }
            }
        }

        // Availability
        addElement(item, "g:availability", "in_stock");

        // ID (must be unique)
        addElement(item, "g:id", "car-" + car.getId());
    }

    /**
     * Build a title from car make, model, year, and trim.
     */
    private String buildTitle(Car car) {
        StringBuilder title = new StringBuilder();
        if (car.getYear() != null) title.append(car.getYear()).append(" ");
        if (car.getMake() != null) title.append(car.getMake()).append(" ");
        if (car.getModel() != null) title.append(car.getModel());
        if (car.getTrim() != null) title.append(" ").append(car.getTrim());
        return title.toString().trim();
    }

    /**
     * Helper to add a simple text element.
     */
    private void addElement(Element parent, String tagName, String value) {
        Document doc = parent.getOwnerDocument();
        Element element = doc.createElement(tagName);
        if (value != null && !value.isEmpty()) {
            element.setTextContent(value);
        }
        parent.appendChild(element);
    }

    /**
     * Convert XML Document to string with proper formatting.
     */
    private String documentToString(Document doc) throws Exception {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.VERSION, "1.0");

        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        return writer.toString();
    }
}
