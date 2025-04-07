FROM eclipse-temurin:17-jdk-focal AS builder
 
WORKDIR /app
 
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
 
COPY src ./src
EXPOSE 8080
 
CMD ["./mvnw", "spring-boot:run"]