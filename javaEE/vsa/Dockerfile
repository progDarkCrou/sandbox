FROM java:oracle-java8
COPY ["target/vsa-1.0.jar", "/home/"]
COPY ["jssecacerts", "/usr/lib/jvm/java-8-oracle/jre/lib/security/"]
EXPOSE 8080
ENTRYPOINT ["java", "-Dserver.port=8080", "-jar", "/home/vsa-1.0.jar", "--mail.sender.login=dark.crou@gmail.com", "--mail.sender.password=ChangePass!@"]