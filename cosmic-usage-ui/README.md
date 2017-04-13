# Cosmic Usage UI

Cosmic Usage UI is an interface for generating billing reports based on the Usage API.

## Building from Source

Requirements:
- Git
- Node
- NPM
- Bower
- Docker

In order to build and run the Cosmic Usage UI, please follow the following steps:

    git clone git@github.com:MissionCriticalCloud/cosmic-microservices.git
    cd cosmic-microservices/cosmic-usage-ui

Build a Docker image for the Cosmic Usage UI:

    mvn clean package -DskipTests

Start a Docker container running the Cosmic Usage UI:

    docker run -it --rm -p 7004:8080 \
        -e COSMIC_USAGE_API_BASE_URL="http://localhost:7003/" \
        --network cosmic-network \
        --name cosmic-usage-ui \
        missioncriticalcloud/cosmic-usage-ui

The service should be available at: [http://localhost:7004/](http://localhost:7004/)

One-liner:

    mvn clean package \
        -DskipTests && \
    docker run -it --rm -p 7004:8080 \
        -e COSMIC_USAGE_API_BASE_URL="http://localhost:7003/" \
        --network cosmic-network \
        --name cosmic-usage-ui \
        missioncriticalcloud/cosmic-usage-ui
