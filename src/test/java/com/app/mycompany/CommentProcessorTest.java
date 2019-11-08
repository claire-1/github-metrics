package com.app.mycompany;

import org.junit.Test;

public class CommentProcessorTest {


// Given a comment, put it in the database
    @Test
    public void givenDatabaseConnectionShouldConnectWithoutErrors() {
        // TODO not the greatest test either b/c really should mock/assume connection works so don't need to test it in the end
        // TODO need to connect to dockerized database!!!!! (see docker-compose.yml)
        CommentProcessor processor = new CommentProcessor("mysqlhost:3306", "storage");
        
    }

}