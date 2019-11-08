package com.app.mycompany;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.kohsuke.github.GHIssueComment;

public class CommentProcessor {

    private Connection conn;

    public CommentProcessor(String hostnameAndPort, String databaseName) {
        // TODO source:
        // https://javarevisited.blogspot.com/2016/09/javasqlsqlexception-no-suitable-driver-mysql-jdbc-localhost.html#ixzz64gKoyLbl

        try {
            // String connectionUrl = "jdbc:mysql://" + hostnameAndPort + "/" +
            // databaseName;
            String connectionUrl = "jdbc:mysql://comments-sql-db:3306/storage";
            Properties info = new Properties();
            info.put("user", "root");
            // info.put("password", "root");

            conn = DriverManager.getConnection(connectionUrl, info);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException("CommentProcessor|cannot connect to mysql database", e);
        }
    }

    public void putCommentInDB(long issueId, Date timestamp, GHIssueComment comment) throws SQLException {
        // the mysql insert statement
        String query = " insert into comments (relatedIssueId, dateIssueClosed, content)" + " values (?, ?, ?)";

        // create the mysql insert preparedstatement
        // Source TODO
        // https://alvinalexander.com/java/java-mysql-insert-example-preparedstatement
        // try {
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setLong(1, issueId);
        preparedStmt.setDate(2, timestamp);
        preparedStmt.setString(3, comment.getBody());
        // execute the query
        preparedStmt.execute();
        // } catch (SQLException e) {
        // throw new SQLException("CommentProcessor|cannot add comment to database", e);
        // }
    }

    public void putCommentsInDB(long issueId, Date issueClosedDate, List<GHIssueComment> comments) {
        try {
            for (GHIssueComment comment : comments) {
                // TODO how to get the timestamp from the comments?
                putCommentInDB(issueId, issueClosedDate, comment);
            }
        } catch (SQLException e) {
            // TODO should this be a runtime execption? or should I pass the exception up to
            // main?
            // TODO passing main will probably be better in the long run. we'll see
            throw new RuntimeException("CommentProcessor|cannot add comment to database", e);
        }
    }

    public String getComments() throws SQLException { // TODO this is just for testing for now
        String query = " select * from comments";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        ResultSet rs = preparedStmt.executeQuery();
        String comments = "";
        while (rs.next()) {
            comments += rs.getString(3);
        }

        return comments;
    }

}