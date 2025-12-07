#!/bin/bash

# Script to add author headers to all Java files
# Author: Kuldeep Bisen

HEADER="/**\n * BISEN - API Testing Tool\n * Simple Postman Alternative, Clean to Test APIs\n * \n * @author Kuldeep Bisen\n * @version 1.2 BETA\n */"

find src/main/java -name "*.java" -type f | while read file; do
    # Check if file already has the header
    if ! grep -q "@author Kuldeep Bisen" "$file"; then
        # Get the package declaration line
        PACKAGE_LINE=$(grep -n "^package " "$file" | head -1 | cut -d: -f1)
        
        if [ -n "$PACKAGE_LINE" ]; then
            # Insert header before package declaration
            sed -i '' "${PACKAGE_LINE}i\\
/**\\
 * BISEN - API Testing Tool\\
 * Simple Postman Alternative, Clean to Test APIs\\
 * \\
 * @author Kuldeep Bisen\\
 * @version 1.2 BETA\\
 */
" "$file"
        fi
    fi
done

echo "Author headers added to all Java files."

