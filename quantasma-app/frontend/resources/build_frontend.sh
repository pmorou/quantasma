#!/bin/bash

if [ -f package.json ]
then
    echo 'Building frontend distribution...'
    npm run build
fi
