FROM darkcrou/java:oracle-java8
MAINTAINER Andriy Vorona
WORKDIR /root/
COPY target/vertx-demo-server-1.0-SNAPSHOT-jar-with-dependencies.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dcluster.join=$CLUSTER_MASTER", "app.jar"]