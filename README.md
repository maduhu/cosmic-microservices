# Cosmic Microservices

- [Cosmic Config Server](cosmic-config-server) (powered by Spring Cloud Config Server)
- [Cosmic Metrics Collector](cosmic-metrics-collector) (powered by Spring Boot)
- [Cosmic Usage API](cosmic-usage-api) (powered by Spring Boot)

## [Cosmic Config Server](cosmic-config-server)

Locally it runs by default at [http://localhost:7001](http://localhost:7001).
Local debugging port is `8001`.

Depends on:
- Cosmic Vault (powered by Hashicorp Vault)

## [Cosmic Metrics Collector](cosmic-metrics-collector)

Local debugging port is `8002`.

Depends on:
- [Cosmic Config Server](cosmic-config-server)
- Cosmic Database (powered by MariaDB)

## [Cosmic Usage API](cosmic-usage-api)

Locally it runs by default at [http://localhost:7003](http://localhost:7003).
Local debugging port is `8003`.

Depends on:
- [Cosmic Config Server](cosmic-config-server)
- Cosmic Metrics Index (powered by Elasticsearch)
