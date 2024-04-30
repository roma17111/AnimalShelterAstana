FROM eclipse-temurin:17-jdk-alpine
COPY target/*.jar app.jar
COPY photos photos
ADD DejaVuSans.ttf DejaVuSans.ttf
ADD src/main/resources/images/dog/img.png src/main/resources/images/dog/img.png
ENTRYPOINT ["java","-jar","/app.jar"]