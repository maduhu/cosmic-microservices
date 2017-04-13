# Cosmic Config Server

Cosmic Config Server is a Spring Cloud Config Server that holds the configuration of all Cosmic microservices.

## Profiles

- **`local:`** Runs locally or in a container without external dependencies.
    Apps config are loaded from [`src/main/resources/local`](src/main/resources/local).
- **`development:`** Runs in a container alongside a Vault container where all apps config are stored.
    Apps config are loaded from [`src/main/resources/vault`](src/main/resources/vault).
- **`production:`** Same as development, but production-ready.

## Building from Source

Requirements:
- Java 8
- Git
- Maven
- Docker

In order to build and run the Cosmic Config Server, please follow the following steps:

    git clone git@github.com:MissionCriticalCloud/cosmic-microservices.git
    cd cosmic-microservices/cosmic-config-server

Build a Docker image for the Cosmic Config Server:

    mvn clean package \
        -P local -DskipTests

Start a Docker container running the Cosmic Config Server:

    docker run -it --rm \
        --network cosmic-network \
        --name cosmic-config-server \
        missioncriticalcloud/cosmic-config-server

Alternatively, if you want to access the container locally:

    docker run -it --rm -p 7001:8080 -p 8001:8000 \
        --network cosmic-network \
        --name cosmic-config-server \
        missioncriticalcloud/cosmic-config-server

In that case, the service should be available at: [http://localhost:7001/](http://localhost:7001/)

One-liner:

    mvn clean package \
        -P local -DskipTests && \
    docker run -it --rm -p 7001:8080 -p 8001:8000 \
        --network cosmic-network \
        --name cosmic-config-server \
        missioncriticalcloud/cosmic-config-server
