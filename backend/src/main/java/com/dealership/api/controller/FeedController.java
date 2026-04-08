package com.dealership.api.controller;

import com.dealership.api.service.FacebookFeedGenerator;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Public feed endpoints.
 *
 * GET /api/feeds/facebook-marketplace        — Facebook / Meta Catalog XML feed
 * GET /api/feeds/facebook-marketplace.xml    — same as above (with .xml extension)
 * GET /api/feeds/products.xml                — alias used by some Meta integrations
 *
 * All endpoints return XML with Content-Type: application/xml
 * Cache duration: 1 hour (configurable)
 *
 * Feed includes:
 * - All cars with status="available"
 * - Car details: make, model, year, price, mileage, VIN, colors, etc.
 * - Images (first image as primary, others as additional)
 * - Dealership contact info from config
 */
@RestController
@RequestMapping("/api/feeds")
public class FeedController {

    private final FacebookFeedGenerator facebookFeedGenerator;

    public FeedController(FacebookFeedGenerator facebookFeedGenerator) {
        this.facebookFeedGenerator = facebookFeedGenerator;
    }

    @GetMapping(path = "/facebook-marketplace", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> facebookMarketplaceFeed() {
        return buildXmlResponse();
    }

    @GetMapping(path = "/facebook-marketplace.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> facebookMarketplaceFeedXml() {
        return buildXmlResponse();
    }

    /** Alias: GET /api/feeds/products.xml */
    @GetMapping(path = "/products.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> productsFeed() {
        return buildXmlResponse();
    }

    private ResponseEntity<String> buildXmlResponse() {
        String xml = facebookFeedGenerator.generateFeed();
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_XML)
            .header("Cache-Control", "public, max-age=3600")
            .body(xml);
    }
}
