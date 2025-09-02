FROM openjdk:17
COPY target/storefront-bff-0.0.1-SNAPSHOT.jar storefront-bff.jar
ENTRYPOINT ["java", "-jar", "/storefront-bff.jar"]