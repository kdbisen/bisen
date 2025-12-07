#!/bin/bash

# macOS Application Launcher
# This file can be double-clicked in Finder to start the application

# Get the directory where this script is located
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$SCRIPT_DIR"

# Open Terminal and run the startup script
osascript -e "tell application \"Terminal\" to do script \"cd '$SCRIPT_DIR' && ./start-mac.sh\""

