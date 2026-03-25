
# SGDEA Enterprise Demo

Arquitectura:

Client
  │
  ▼
API Gateway (JWT Validation)
  │
  ├── Identity Service
  ├── Document Service → Kafka → Metadata Service
  └── Metadata Service

Infraestructura:

Kafka
PostgreSQL
MinIO

Puertos:

Gateway           8080
Identity          8081
Document          8082
Metadata          8083
Kafka             9092
MinIO             9000
PostgreSQL        5432

## Ejecutar

docker compose up --build
