# Quantasma application

## Architecture

Currently application consists of three separate services:

-   frontend - nginx running angular 6

-   backend - Spring Boot 2 publishing API endpoints

-   database - MongoDB storing historical data for backtests

**NOTE: It is not a final setup, more services coming soon.**

## Compilation

Follow parent [README]({../README.md}).

## Start application

After the whole project was compiled all distribution files including frontend and backend should be generated.

Docker compose is being used to manage the application services, simply run command below to start up everything.

    docker-compose up

After some time an app should be now available at `http://localhost/`.
