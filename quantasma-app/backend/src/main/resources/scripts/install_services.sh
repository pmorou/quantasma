#!/bin/bash

docker run -d \
  --name historical-mongo-db \
  -v historical-mongo-db-vol:/app \
  mongo