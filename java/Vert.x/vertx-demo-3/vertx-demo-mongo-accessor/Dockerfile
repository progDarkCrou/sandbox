FROM darkcrou/java:oracle-java8
MAINTAINER Andriy Vorona
WORKDIR /root/
EXPOSE 8778
COPY target/vertx-demo-mongo-accessor-1.0-SNAPSHOT-jar-with-dependencies.jar ./app.jar
COPY jolokia-jvm-1.3.3-agent.jar ./jolokia.jar
ENTRYPOINT ["java", "-javaagent:jolokia.jar=host=0.0.0.0", "-jar", "app.jar"]