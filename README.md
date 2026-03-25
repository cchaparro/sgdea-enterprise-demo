# SGDEA Enterprise Demo

Demo de gestor documental con arquitectura de microservicios.

## Arquitectura

```text
Client
  |
  v
API Gateway
  |
  +-- Identity Service
  +-- Document Service -> Kafka -> Metadata Service
  +-- Audit Service
  +-- Search Service
```

## Componentes

- `gateway`: entrada principal de la solucion
- `identity-service`: autenticacion demo
- `document-service`: carga de documentos y almacenamiento en MinIO
- `metadata-service`: metadatos, expedientes y ACL basica
- `audit-service`: consumo de eventos de auditoria
- `search-service`: indexacion y busqueda en Elasticsearch

## Infraestructura

- Kafka
- PostgreSQL
- MinIO
- Elasticsearch
- Kibana
- Jaeger
- Kafka UI

## Puertos

### Microservicios

- Gateway: `8080`
- Identity: `8081`
- Document: `8082`
- Metadata Docker: `8083`
- Metadata local: `8183`
- Audit: `8084`
- Search: `8086`

### Infraestructura

- PostgreSQL: `5432`
- Kafka externo: `9092`
- MinIO API: `9000`
- MinIO Console: `9001`
- Elasticsearch: `9200`
- Kibana: `5601`
- Kafka UI: `8085`
- Jaeger: `16686`

## Ejecucion completa en Docker

```powershell
docker compose up --build
```

## Desarrollo local

El repo quedo preparado para correr la infraestructura en Docker y los microservicios en local con perfil `local`.

### Infraestructura

```powershell
docker compose down -v --remove-orphans
docker compose up -d postgres zookeeper kafka minio elasticsearch kibana jaeger otel-collector kafka-ui
```

### Perfil local

Los servicios usan `application-local.yml` y se levantan con:

```text
SPRING_PROFILES_ACTIVE=local
```

## VS Code

Se dejo configuracion en:

- `.vscode/tasks.json`
- `.vscode/launch.json`

### Tareas principales

- `Reset Infra`
- `Run Local Services Except Document`

### Debug principal

- `Debug Document`

Flujo recomendado:

1. Ejecutar `Reset Infra`
2. Ejecutar `Run Local Services Except Document`
3. Ir a `Run and Debug`
4. Ejecutar `Debug Document`

## Flujo documental actual

1. `document-service` recibe `POST /documents/upload`
2. guarda el archivo en MinIO
3. publica evento `document.created`
4. `metadata-service` consume `metadata-events`
5. `audit-service` consume `audit-events`
6. `search-service` indexa documentos en Elasticsearch

## Endpoints documentales basicos

### Crear documento

```http
POST /documents/upload
```

### Crear nueva version del mismo documento

```http
POST /documents/{id}/versions
```

Parametros:

- `file`
- `user`
- `estado` opcional

Comportamiento:

- reutiliza el mismo `documentId`
- calcula la siguiente version automaticamente
- guarda el archivo en una ruta nueva en MinIO
- actualiza metadatos con la version mas reciente

### Consultar historial de versiones

```http
GET /metadata/{id}/versions
```

## Eventos de auditoria

La auditoria usa un evento distinto al de metadatos y esta orientada a:

- quien ejecuto la accion
- que accion ejecuto
- sobre que recurso

Campos del evento:

- `eventType`
- `action`
- `actor`
- `resourceType`
- `resourceId`
- `resourceName`
- `traceId`
- `timestamp`

### Evento al crear documento

```json
{
  "eventType": "audit.document.created",
  "action": "DOCUMENT_CREATED",
  "actor": "user1",
  "resourceType": "DOCUMENT",
  "resourceId": "8d5c1f9e-....",
  "resourceName": "contrato.pdf",
  "traceId": "....",
  "timestamp": "2026-03-25T18:00:00Z"
}
```

### Evento al consultar documento

```json
{
  "eventType": "audit.document.viewed",
  "action": "DOCUMENT_VIEWED",
  "actor": "user1",
  "resourceType": "DOCUMENT",
  "resourceId": "8d5c1f9e-....",
  "resourceName": "contrato.pdf",
  "traceId": "....",
  "timestamp": "2026-03-25T18:01:00Z"
}
```

### Evento al descargar documento

```json
{
  "eventType": "audit.document.download-url-generated",
  "action": "DOCUMENT_DOWNLOAD_URL_GENERATED",
  "actor": "user1",
  "resourceType": "DOCUMENT",
  "resourceId": "8d5c1f9e-....",
  "resourceName": "contrato.pdf",
  "traceId": "....",
  "timestamp": "2026-03-25T18:02:00Z"
}
```

### Evento al crear nueva version

```json
{
  "eventType": "audit.document.version.created",
  "action": "DOCUMENT_VERSION_CREATED",
  "actor": "user1",
  "resourceType": "DOCUMENT",
  "resourceId": "8d5c1f9e-....",
  "resourceName": "contrato-v2.pdf",
  "traceId": "....",
  "timestamp": "2026-03-25T18:04:00Z"
}
```

### Evento al otorgar acceso

```json
{
  "eventType": "audit.document.access-granted",
  "action": "DOCUMENT_ACCESS_GRANTED",
  "actor": "admin",
  "resourceType": "DOCUMENT",
  "resourceId": "8d5c1f9e-....",
  "resourceName": "contrato.pdf",
  "traceId": "....",
  "timestamp": "2026-03-25T18:03:00Z"
}
```

Estos eventos se pueden revisar en Kafka UI en el topic `audit-events`.

## Busqueda

### Busqueda libre

```http
GET /search?q=contrato
```

### Busqueda por propietario

```http
GET /search/owner/{owner}
```

Ejemplo:

```http
GET /search/owner/user1
```
