#!/bin/bash
set -e

echo "Building dealership template..."

# Build backend
echo "Building Spring Boot backend..."
cd backend
mvn clean package -DskipTests
cd ..

# Build frontend
echo "Building Vue frontend..."
cd frontend
npm install
npm run build
cd ..

echo "Build complete!"
