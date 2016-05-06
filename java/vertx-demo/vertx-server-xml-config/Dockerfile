FROM darkcrou/java:oracle-java8
MAINTAINER Andriy Vorona
WORKDIR /root/
COPY target/vertx-demo-server-xml-config-1.0-SNAPSHOT-jar-with-dependencies.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]