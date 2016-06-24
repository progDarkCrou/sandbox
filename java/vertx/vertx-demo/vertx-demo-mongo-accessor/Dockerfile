FROM darkcrou/java:oracle-java8
MAINTAINER Andriy Vorona
WORKDIR /root/
COPY target/vertx-demo-mongo-accessor-1.0-SNAPSHOT-jar-with-dependencies.jar ./app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]