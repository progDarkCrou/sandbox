FROM darkcrou/java:oracle-java8
MAINTAINER Andriy Vorona
WORKDIR /root/
COPY target/vertx-demo-server-with-hawkular-1.0-SNAPSHOT-jar-with-dependencies.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dcluster.join=${CLUSTER_MASTER}", "-Dhawkular.host=$HAWKULAR_HOST", "-Dhawkular.port=$HAWKULAR_PORT", "app.jar"]