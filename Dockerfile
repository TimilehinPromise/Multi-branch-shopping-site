FROM eclipse-temurin:17-jdk-alpine
EXPOSE 9010
VOLUME /tmp
# Add the application's jar to the container
ADD target/shop-0.0.1-SNAPSHOT.jar app.jar
# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
