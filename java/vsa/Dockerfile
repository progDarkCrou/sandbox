FROM darkcrou/java:oracle-java8
COPY ["target/vsa-1.0.jar", "/home/"]
EXPOSE 8080
ENTRYPOINT ["java", "-Dserver.port=8080", "-jar",\
 "/home/vsa-1.0.jar",\
 "--mail.sender.login=dark.crou@gmail.com",\
 "--mail.sender.password=ChangePass!@",\
 "--default.email=dark.crou@gmail.com",\
 "--default.name=Andriy Vorona"]
