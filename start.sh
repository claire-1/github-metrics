#/bin/bash
mvn clean package 
mvn exec:java -Dexec.mainClass=com.app.mycompany.Main -Dexec.cleanupDaemonThreads=false #-Djava.awt.headless=true

ret=$?
if [ $ret -ne 0 ]; then
        echo "There was an error. Not starting webserver."
else
        http-server ./display -p 8080 -c-1
fi