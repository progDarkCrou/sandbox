FROM ubuntu:latest
MAINTAINER Andriy Vorona
RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections
RUN apt update
RUN apt-get install -y software-properties-common
RUN apt-add-repository -y ppa:webupd8team/java
RUN apt update
RUN apt install -y oracle-java8-installer
RUN apt-get autoclean
RUN apt-get autoremove -y

ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

CMD ["bash"]