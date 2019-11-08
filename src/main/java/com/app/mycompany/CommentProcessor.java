package com.app.mycompany;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class CommentProcessor {

    private Connection mysql;

    public CommentProcessor(String hostnameAndPort, String databaseName) {

        // source:
        // https://javarevisited.blogspot.com/2016/09/javasqlsqlexception-no-suitable-driver-mysql-jdbc-localhost.html#ixzz64gKoyLbl

        try {
           // String connectionUrl = "jdbc:mysql://" + hostnameAndPort + "/" + databaseName;
            String connectionUrl = "jdbc:mysql://comments-sql-db:3306/storage";
            Properties info = new Properties();
            info.put("user", "root");
            //info.put("password", "root");

            mysql = DriverManager.getConnection(connectionUrl, info);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException("CommentProcessor|cannot connect to mysql database", e);
        }
    }

}