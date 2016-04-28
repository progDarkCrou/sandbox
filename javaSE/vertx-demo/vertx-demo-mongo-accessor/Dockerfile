FROM darkcrou/java:oracle-java8
MAINTAINER Andriy Vorona
WORKDIR /root/
COPY target/vertx-demo-mongo-accessor-1.0-SNAPSHOT-jar-with-dependencies.jar ./app.jar
ENTRYPOINT ["java", "-jar", "-Dcluster.join=$CLUSTER_MASTER", "-Dmongo.addr=\"$MONGO_ADDR\"", "app.jar"]