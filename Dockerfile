FROM maven:3.5-jdk-8-alpine

RUN apk add --update nodejs npm
#RUN npm init
COPY package.json /package.json
RUN npm install http-server -g
RUN echo "LOOK HERE "
RUN echo $PATH
RUN export PATH=$PATH:/usr/bin/http-server
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
#ENV DISPLAY=localhost:0
#RUN unset DISPLAY
#CMD ["export", "DISPLAY=:0.0"]
#CMD ["mvn", "test"]
#CMD [ "mvn", "clean package" ]
#RUN "mvn clean package"
#ENTRYPOINT [ "mvn exec:java -Dexec.mainClass=com.app.mycompany.GithubAccess" ]
CMD ["/bin/bash", "./start.sh"]
#ENTRYPOINT [ "mvn", "exec:java" ]
