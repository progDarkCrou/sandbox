FROM darkcrou/java:oracle-java8
MAINTAINER Andriy Vorona
WORKDIR /root/
COPY target/vertx-demo-calculator-1.0-SNAPSHOT-jar-with-dependencies.jar ./app.jar
ENTRYPOINT ["java", "-jar", "-Dhazelcast.socket.bind.any=false", "-Dcluster.join=$CLUSTER_MASTER", "app.jar"]