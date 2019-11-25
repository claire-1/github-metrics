package com.app.mycompany;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.kohsuke.github.GHIssueComment;

import weka.core.Instances;
import weka.experiment.InstanceQuery;

public class MySqlConnection {

    private Connection conn;
    // private String connectionUrl;
    // private String userName;

    public MySqlConnection(String hostnameAndPort, String databaseName, String userName) {
        // TODO source:
        // https://javarevisited.blogspot.com/2016/09/javasqlsqlexception-no-suitable-driver-mysql-jdbc-localhost.html#ixzz64gKoyLbl

        try {
            String connectionUrl = "jdbc:mysql://" + hostnameAndPort + "/" + databaseName;
            Properties info = new Properties();
            info.put("user", userName);
            info.put("password", "root"); // TODO make this a parameter if works with PHP

            conn = DriverManager.getConnection(connectionUrl, info);
        } catch (SQLException e) {
            throw new RuntimeException("CommentProcessor|cannot connect to mysql database", e);
        }

    }

    public void putCommentInDB(long issueId, Date issueClosedDate, String comment) throws SQLException {
        String query = " insert into comments (relatedIssueId, dateIssueClosed, content)" + " values (?, ?, ?)";

        // Source TODO
        // https://alvinalexander.com/java/java-mysql-insert-example-preparedstatement
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setLong(1, issueId);
        preparedStmt.setDate(2, issueClosedDate);
        preparedStmt.setString(3, comment);
        // execute the query
        preparedStmt.execute();
    }

    public void putCommentsInDB(long issueId, Date issueClosedDate, List<GHIssueComment> comments) throws SQLException {
        for (GHIssueComment comment : comments) {
            // TODO how to get the timestamp from the comments?
            putCommentInDB(issueId, issueClosedDate, comment.getBody());
        }
    }

   

    // TODO delete this probably
    public String getDataFromDatabase(String query) throws SQLException { // TODO this is just for testing for now
        // Source for dealing with ResultSets:
        // https://www.javatpoint.com/example-to-connect-to-the-mysql-database TODO
        System.out.println("getting data");
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        ResultSet rs = preparedStmt.executeQuery();
        String dataString = "";
        while (rs.next()) {
            System.out.println("COMMENTS " + rs.getString(1));
            // for (int i = 0; i < rs.getFetchSize(); i++) {
            // dataString += rs.getString(i);
            // }
            dataString += rs.getString(1);
            // comments += rs.getString(2);
        }

        return dataString;
    }

    public ResultSet getDataFromDatabaseAsResultSet(String query) throws SQLException { // TODO this is just for testing
                                                                                        // for now
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        ResultSet rs = preparedStmt.executeQuery();
        return rs;
    }

    public Instances getAsDataSetFromSql(String query2) {
        try {
            InstanceQuery query = new InstanceQuery();
            query.setUsername("root");
            query.setPassword("root");
            query.setQuery("select content from comments");
            Instances data = query.retrieveInstances();
            data.setClassIndex(data.numAttributes() - 1);
            System.out.println("DATA HERE " + data.toString());
            return data;
        } catch (Exception e) {
            throw new RuntimeException("getAsDataSet|problem connecting with database loader", e);
        }
    }

    public void manipulateData(String query) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.execute();
    }

    public void putClassificationInDB(URL issueUrl, Date issueClosedDate, String classification) throws SQLException {
        String query = " insert into classifierResults (relatedIssueUrl, dateIssueClosed, classifiedIssueStatus)"
                + " values (?, ?, ?)";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setURL(1, issueUrl);
        preparedStmt.setDate(2, issueClosedDate);
        preparedStmt.setString(3, classification);
        // execute the query
        preparedStmt.execute();
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}