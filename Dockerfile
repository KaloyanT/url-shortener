FROM maven:3.9.5-amazoncorretto-21 as build
WORKDIR /app
COPY . .
RUN mvn clean install

FROM azul/zulu-openjdk-alpine:21-jre-headless
COPY --from=build /app/target/url-shortener*.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]