FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -Dmaven.test.skip=true


FROM eclipse-temurin:21-jdk

WORKDIR /app

RUN apt-get update \
    && apt-get install -y mysql-server \
    && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/*.jar app.jar
COPY entrypoint.sh /entrypoint.sh

RUN chmod +x /entrypoint.sh

EXPOSE 8080
EXPOSE 3306

ENTRYPOINT ["/entrypoint.sh"]