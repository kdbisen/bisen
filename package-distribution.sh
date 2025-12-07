#!/bin/bash

# Package BISEN API Tester for Distribution

echo "========================================"
echo "  BISEN - Packaging"
echo "========================================"
echo ""

# Build the JAR file first
echo "Building application..."
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "ERROR: Build failed!"
    exit 1
fi

# Create distribution directory
DIST_DIR="bisen-api-tester-dist"
rm -rf "$DIST_DIR"
mkdir -p "$DIST_DIR"

# Copy files
echo "Copying files..."
cp target/rest-api-tester-1.0.0.jar "$DIST_DIR/"
cp start-windows.bat "$DIST_DIR/"
cp start-mac.sh "$DIST_DIR/"
cp start-mac.command "$DIST_DIR/" 2>/dev/null || true
cp DISTRIBUTION_README.md "$DIST_DIR/README.md"
cp create-shortcut-windows.vbs "$DIST_DIR/" 2>/dev/null || true

# Make scripts executable
chmod +x "$DIST_DIR/start-mac.sh"
chmod +x "$DIST_DIR/start-mac.command" 2>/dev/null || true

# Create ZIP file
ZIP_NAME="bisen-api-tester-v1.0.0.zip"
echo "Creating ZIP archive: $ZIP_NAME"
cd "$DIST_DIR"
zip -r "../$ZIP_NAME" . -q
cd ..

echo ""
echo "========================================"
echo "  Packaging Complete!"
echo "========================================"
echo ""
echo "Distribution package: $ZIP_NAME"
echo "Size: $(du -h $ZIP_NAME | cut -f1)"
echo ""
echo "Contents:"
echo "  - rest-api-tester-1.0.0.jar"
echo "  - start-windows.bat (Windows)"
echo "  - start-mac.sh (macOS/Linux)"
echo "  - README.md"
echo ""
echo "Users can extract this ZIP and run the appropriate script!"
echo ""

