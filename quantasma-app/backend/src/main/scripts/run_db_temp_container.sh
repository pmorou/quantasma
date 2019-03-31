#!/bin/bash

echo 'Running docker container...'

sudo docker run --name jooq-gen-container -p 5432:5432 -v "$PWD"/quantasma-app/backend/src/main/resources/sql:/docker-entrypoint-initdb.d -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -d postgres:11.2
