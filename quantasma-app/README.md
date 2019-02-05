# Quantasma application

## Architecture

Currently application consists of three separate services:

-   frontend - nginx running angular 6

-   backend - Spring Boot 2 publishing API endpoints and handling 3rd party communications

-   database - MongoDB storing historical data for backtests

**NOTE: It is not a final setup, more services coming soon.**

## Compilation

Follow parent [README]({../README.md}).

## Start application

After the whole project was compiled all distribution files including frontend and backend should be generated.

Docker compose is being used to manage the application services, simply run command below to start up everything.

    docker-compose up

After some time an app should be now available at `http://localhost/`.

## Developing

In order to see changes done within angular project a watch mode has to be enabled.

    cd frontend/resources && sudo ng build --watch

Live reload for spring boot service can be triggered through remote connection. Run project with the following configuration:

-   **Main class:** org.springframework.boot.devtools.RemoteSpringApplication

-   **Program arguments:** <http://localhost:8080>

-   **Use classpath of module:** quantasma-app-backend

Run containers with additional dev enhancements like directory sharing and port publishing.

    docker-compose -f docker-compose.yml -f docker-compose-dev.yml up

If you have made changes to the immutable part of the images you can put them down and remove in order to rebuild them from the ground up.

    docker-compose down --rmi local
