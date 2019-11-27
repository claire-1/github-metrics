package com.app.mycompany;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class SqlConnection {

    private Connection conn;

    public SqlConnection(String hostnameAndPort, String databaseName, String userName, String password) {
        try {
            String connectionUrl = "jdbc:mysql://" + hostnameAndPort + "/" + databaseName;
            Properties info = new Properties();
            info.put("user", userName);
            info.put("password", password);
            conn = DriverManager.getConnection(connectionUrl, info);
        } catch (SQLException e) {
            throw new RuntimeException("CommentProcessor|cannot connect to mysql database", e);
        }

    }

    public void putCommentInDB(long issueId, Date issueClosedDate, String comment) throws SQLException {
        String query = " insert into comments (relatedIssueId, dateIssueClosed, content)" + " values (?, ?, ?)";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setLong(1, issueId);
        preparedStmt.setDate(2, issueClosedDate);
        preparedStmt.setString(3, comment);
        // execute the query
        preparedStmt.execute();
    }

    public ResultSet getDataFromDatabaseAsResultSet(String query) throws SQLException {
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        ResultSet rs = preparedStmt.executeQuery();
        return rs;
    }

    public void putClassificationInDB(String repo, int issueNumber, Date issueClosedDate, String classification)
            throws SQLException {
        String urlToGithubIssue = "https://github.com/" + repo + "/issues/" + issueNumber;
        String query = " insert into classifierResults (relatedIssueUrl, dateIssueClosed, classifiedIssueStatus)"
                + " values (?, ?, ?)";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, urlToGithubIssue);
        preparedStmt.setDate(2, issueClosedDate);
        preparedStmt.setString(3, classification);
        // execute the query
        preparedStmt.execute();
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}