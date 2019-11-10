FROM maven:3.5-jdk-8-alpine

COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
#CMD ["mvn", "test"]
#CMD [ "mvn", "clean package" ]
#RUN "mvn clean package"
#ENTRYPOINT [ "mvn exec:java -Dexec.mainClass=com.app.mycompany.GithubAccess" ]
CMD ["/bin/bash", "./start.sh"]
#ENTRYPOINT [ "mvn", "exec:java" ]