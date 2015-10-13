FROM java:oracle-java8
COPY ["./target/classes/utils/InstallCert*.class", "/home/utils/"]
WORKDIR /home/
RUN ["java", "utils.InstallCert", "polandonline.vfsglobal.com"]
RUN ["mv", "/home/jssecacerts", "/usr/lib/jvm/java-8-oracle/jre/lib/security/jssecacerts"]
RUN ["rm", "-rf", "/home/utils"]
COPY ["target/vsa-1.0.jar", "/home/"]
EXPOSE 8080
ENTRYPOINT ["java", "-Dserver.port=8080", "-jar",\
 "/home/vsa-1.0.jar",\
 "--mail.sender.login=dark.crou@gmail.com",\
 "--mail.sender.password=ChangePass!@",\
 "--default.email=dark.crou@gmail.com",\
 "--default.name=Andriy Vorona"]