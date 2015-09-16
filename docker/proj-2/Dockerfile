FROM mysql
ENV MYSQL_ROOT_PASSWORD root
RUN ["service", "mysql", "start"]
RUN ["mysql", "-u", "root", "-proot", "-h", "localhost"]
RUN ["create", "database", "hibernate"]