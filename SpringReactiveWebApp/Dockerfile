
FROM adoptopenjdk/openjdk11:jdk-11.0.9.1_1
VOLUME ["/tmp"]
COPY target/SpringReactiveWebApp-*.jar SpringReactiveWebApp.jar
COPY src/ src
COPY pom.xml pom.xml
COPY Dockerfile Dockerfile
ENTRYPOINT ["java","-jar","SpringReactiveWebApp.jar"]