
FROM maven:3.9.9-eclipse-temurin-17
WORKDIR /app
RUN mkdir -p /otel
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.32.1/opentelemetry-javaagent.jar /otel/opentelemetry-javaagent.jar
COPY . .
RUN mvn -q -DskipTests package
CMD ["sh","-c","java -jar target/*.jar"]
