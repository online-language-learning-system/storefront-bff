# Sử dụng Temurin JDK 17
FROM eclipse-temurin:17-jdk

# Copy file JAR vào container
COPY target/storefront-bff*.jar /storefront-bff.jar

# Expose port nếu cần
EXPOSE 8000

# Chạy ứng dụng khi container start
ENTRYPOINT ["java", "-jar", "/storefront-bff.jar"]
