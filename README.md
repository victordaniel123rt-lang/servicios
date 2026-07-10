# Servicios — Plataforma eCommerce con microservicios (Java / Spring Boot)

Resumen
-------
Plataforma de ejemplo de eCommerce construida como un conjunto de microservicios en Java 17 usando Spring Boot y Spring Cloud. Cada servicio mantiene su propia persistencia en MariaDB y se comunican de forma asíncrona a través de Kafka. Existe un servidor de descubrimiento (Eureka) y un API Gateway (declarado en el POM padre).

Estado actual
- Proyecto multi-módulo Maven (pom.xml) con los módulos:
  - product-service
  - discovery-service (Eureka)
  - orders-service
  - inventory-service
  - notification-service
  - api-gateway (declarado en el POM padre; confirmar si la carpeta existe)
- Contenerización/infra mínima: `docker-compose.yml` incluye configuración para Kafka (snippet presente). No se incluyen en el compose MariaDB ni Zipkin/Prometheus por defecto — ver sección "Docker / Orquestación".

Stack
-----
- Lenguaje: Java 17
- Frameworks: Spring Boot 3.x, Spring Cloud (2025.0.0)
- Messaging: Apache Kafka
- Persistencia: MariaDB (driver presente en varios módulos)
- Observabilidad / Resiliencia: Micrometer, Zipkin reporter, Resilience4j (en orders-service)
- Build: Maven (multi-module)

Estructura principal (top-level)
```
pom.xml                   # POM padre (módulos y properties)
product-service/          # Catálogo de productos (JPA, web)
discovery-service/        # Eureka server
orders-service/           # Gestión de pedidos (JPA, Kafka, tracing)
inventory-service/       # Inventario (JPA, Kafka)
notification-service/     # Notificaciones (Kafka)
api-gateway/              # (declarado en pom) Spring Cloud Gateway (verificar existencia)
docker-compose.yml        # Compose parcial: contiene configuración para Kafka
```

Cómo encaja todo
-----------------
- discovery-service funciona como servidor Eureka; los demás servicios se registran en él.
- api-gateway (Spring Cloud Gateway) actúa como entrada HTTP y enruta a servicios descubiertos por Eureka.
- Kafka se usa para eventos asíncronos (p. ej. OrderCreated → StockReserved → PaymentProcessed → Notification).
- Cada servicio con JPA guarda sus datos en su propia instancia/DB MariaDB (recomendado: una base por servicio o esquemas separados).
- Observabilidad: orders-service incluye dependencias para métricas y trazas; puedes desplegar Zipkin/Prometheus para agregarlas.

Requisitos
----------
- Java 17+
- Maven 3.6+
- Docker & Docker Compose (para entornos locales reproducibles)

Quick start (local)
-------------------
1) Build multi-módulo:
```bash
# desde la raíz del repo
./mvnw -T 1C clean install
```

2) Levantar Kafka (compose incluido):
```bash
docker-compose up -d kafka
docker-compose logs -f kafka
```

3) (Recomendado) Añadir MariaDB al docker-compose local antes de arrancar los servicios. Ejemplo mínimo para MariaDB (añadir a docker-compose.yml):
```yaml
mariadb:
  image: mariadb:10.11
  environment:
    MYSQL_ROOT_PASSWORD: example
    MYSQL_DATABASE: servicios_db
    MYSQL_USER: svc
    MYSQL_PASSWORD: svcpass
  ports:
    - "3306:3306"
```

4) Ejecutar discovery (Eureka) primero:
```bash
# arranque con maven desde raíz, usando el módulo discovery-service
./mvnw -pl discovery-service spring-boot:run
```

5) Ejecutar servicios (cada uno en terminal diferente o con contenedores):
```bash
./mvnw -pl product-service spring-boot:run
./mvnw -pl inventory-service spring-boot:run
./mvnw -pl orders-service spring-boot:run
./mvnw -pl notification-service spring-boot:run
```

6) Ejecutar API Gateway (si existe la carpeta/module):
```bash
./mvnw -pl api-gateway spring-boot:run
```

Variables de entorno / configuración (ejemplos)
- Para cada servicio (application.yml/properties):
  - spring.datasource.url=jdbc:mariadb://mariadb:3306/<db>
  - spring.datasource.username=<user>
  - spring.datasource.password=<pass>
  - spring.kafka.bootstrap-servers=kafka:29092
  - eureka.client.service-url.defaultZone=http://discovery-service:8761/eureka/

Migraciones
----------
- Recomendado: agregar Flyway o Liquibase en cada servicio que use JPA para aplicar migraciones automáticas.
- Mantener scripts SQL en `src/main/resources/db/migration` (o la convención que uses).

Pruebas
-------
- Unit tests: `./mvnw test`
- Integration tests para Kafka: usar `spring-kafka-test` (dependencia ya presente en algunos módulos).
- E2E: levantar Kafka + MariaDB + discovery y ejecutar pruebas de integración que realicen flujos completos.

Contenerización y despliegue
----------------------------
- Añadir un Dockerfile por servicio (si no existen) y ampliar docker-compose.yml para incluir:
  - MariaDB
  - Zipkin (opcional) para trazas
  - Prometheus/Grafana (opcional) para métricas
- Pipeline sugerido: GitHub Actions -> construir imágenes -> push a registry -> desplegar en Kubernetes (manifests/Helm) o Docker Swarm.

Cositas a revisar / notas
-------------------------
- El POM padre declara `api-gateway` como módulo; en el repo actual no se encontró la carpeta `api-gateway` (por favor confirma si falta o si se debe renombrar).
- docker-compose.yml en el repo actualmente incluye configuración de Kafka (KRaft). Debes añadir MariaDB y otros servicios al compose para un entorno local completo.
- Rutas y puertos concretos se definen en `application.yml`/`application.properties` de cada módulo; compártelos si quieres que inserte ejemplos exactos.

Contribuir
----------
1. Fork → feature/<nombre> → PR con descripción y tests.
2. Ejecutar `./mvnw -T 1C clean verify` antes de enviar PR.
3. Añade pruebas y documentación de cualquier cambio de contrato entre servicios (eventos Kafka, APIs REST).

Licencia
--------
Añade un archivo LICENSE (por ejemplo MIT o Apache-2.0) si quieres publicar con licencia abierta.

Preguntas que pueden ayudar a completar el README
------------------------------------------------
- ¿Confirmas que `api-gateway` está ausente y quieres que lo agregue al repo, o simplemente renombrarlo?
- ¿Quieres que añada un docker-compose.yml completo (Kafka + MariaDB + Zipkin + todos los servicios) para desarrollo local?
- ¿Prefieres Flyway o Liquibase para migraciones DB en cada servicio?
