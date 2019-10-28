FROM maven:3.3-jdk-8

COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
CMD ["mvn", "test"]