#!/bin/bash

echo 'Converting README-src.adoc to README.md in progress...'
asciidoctor -b docbook -a leveloffset=+1 -o - README-src.adoc | pandoc --atx-headers --wrap=preserve -t markdown_github -f docbook - > README.md