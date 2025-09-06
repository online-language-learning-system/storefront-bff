FROM openjdk:17
COPY target/storefront-bff*.jar storefront-bff.jar
ENTRYPOINT ["java", "-jar", "/storefront-bff.jar"]