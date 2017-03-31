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

    docker build -t missioncriticalcloud/cosmic-usage-ui .

Start a Docker container running the Cosmic Usage UI:

    docker run --rm -p 7004:8080 \
        --network cosmic-network \
        --name cosmic-usage-ui \
        missioncriticalcloud/cosmic-usage-ui

The service should be available at: [http://localhost:7004/](http://localhost:7004/)

One-liner:

    docker build \
           -t missioncriticalcloud/cosmic-usage-ui . && \
    docker run --rm -p 7004:8080 \
        --network cosmic-network \
        --name cosmic-usage-ui \
        missioncriticalcloud/cosmic-usage-ui
