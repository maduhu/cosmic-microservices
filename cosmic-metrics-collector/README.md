# Cosmic Metrics Collector

Cosmic Metrics Collector is a Spring Boot application that collects metrics about Cosmic resources from Cosmic's database.

## Building from Source

Requirements:
- Java 8
- Git
- Maven
- Docker

In order to build and run the Cosmic Metrics Collector, please follow the following steps:

    git clone git@github.com:MissionCriticalCloud/cosmic-microservices.git
    cd cosmic-microservices/cosmic-metrics-collector

Build a Docker image for the Cosmic Metrics Collector:

    mvn clean package \
        -P local -DskipTests

Start a Docker container running the Cosmic Metrics Collector:

    docker run -it --rm \
        --network cosmic-network \
        --name cosmic-metrics-collector \
        missioncriticalcloud/cosmic-metrics-collector

Alternatively, if you want to access the container locally:

    docker run -it --rm -p 8002:8000 \
        --network cosmic-network \
        --name cosmic-metrics-collector \
        missioncriticalcloud/cosmic-metrics-collector

One-liner:

    mvn clean package \
        -P local -DskipTests && \
    docker run -it --rm -p 8002:8000 \
        --network cosmic-network \
        --name cosmic-metrics-collector \
        missioncriticalcloud/cosmic-metrics-collector
