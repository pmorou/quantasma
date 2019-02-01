#!/bin/bash

set -o errexit

main() {
  echo "Setting up dependencies."

  update_packages_list
  install_asciidoctor
  install_pandoc
  install_node_modules

  echo "Done! Finished setting up dependencies."
}

update_packages_list() {
  echo "Updating packages list..."

  sudo apt-get update

  echo "Packages list update."
}

install_asciidoctor() {
  echo "Installing AsciiDoctor..."

  sudo apt-get install asciidoctor

  echo "AsciiDoctor installed."
}

install_pandoc() {
  echo "Installing pandoc..."

  TEMP_DEB="pandoc_deb_temp"
  wget -O "$TEMP_DEB" "https://github.com/jgm/pandoc/releases/download/2.4/pandoc-2.4-1-amd64.deb"
  sudo dpkg -i "$TEMP_DEB"
  rm -f "$TEMP_DEB"

  echo "Pandoc installed."
}

install_node_modules() {
  echo "Installing node modules..."

  npm --prefix ./quantasma-app/frontend/resources install

  echo "Node modules installed."
}

main
