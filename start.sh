#/bin/bash
mvn clean package 
mvn exec:java -Dexec.mainClass=com.app.mycompany.GithubAccess #-Djava.awt.headless=true
http-server ./display -p 8080 -c-1