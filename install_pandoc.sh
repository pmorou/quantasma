#!/bin/bash

set -o errexit

main() {
  echo "Setting up dependencies."

  install_pandoc

  echo "Done! Finished setting up dependencies."
}

install_pandoc() {
  echo "Installing pandoc..."

  TEMP_DEB="pandoc_deb_temp"
  wget -O "$TEMP_DEB" "https://github.com/jgm/pandoc/releases/download/2.4/pandoc-2.4-1-amd64.deb"
  sudo dpkg -i "$TEMP_DEB"
  rm -f "$TEMP_DEB"

  echo "Pandoc installed."
}

main
