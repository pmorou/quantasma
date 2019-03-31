#!/bin/bash

echo 'Running db container for JOOQ class generation'
./quantasma-app/backend/src/main/scripts/run_db_temp_container.sh

echo 'Starting maven build process...'
if ! mvn clean package $@; then
    echo "Build failed"
else
    echo "Build successful"
fi

echo 'Removing db container for JOOQ class generation'
./quantasma-app/backend/src/main/scripts/remove_db_temp_container.sh
