# Quantasma application

## Before first run

Application requires MongoDB service.

    ./src/main/resources/scripts/install_service.sh

## First run

Run services.

    ./src/main/resources/scripts/start_service.sh

All dependencies are in the place. Next step is application itself. Pick right spring profile, eg.

    java -jar -Dspring.profiles.active=dukascopy quantasma-app-0.0.1-SNAPSHOT.jar
