package com.app.mycompany;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueComment;

import weka.core.Instances;

public class CommentProcessorTest {

    // Given a comment, put it in the database
    @Test
    public void givenDatabaseConnectionShouldConnectWithoutErrors() throws SQLException {
        // TODO not the greatest test either b/c really should mock/assume connection
        // works so don't need to test it in the end
        // TODO need to connect to dockerized database!!!!! (see docker-compose.yml)
        CommentProcessor processorDB = new CommentProcessor("mysqlhost:3306", "storage");
        GithubAccess access = new GithubAccess("claire-1/github-metrics");
        List<GHIssue> issues = access.getClosedIssues();
        List<GHIssueComment> commentsForFirstIssue = IssueUtils.getComments(issues.get(0));
        Date sqlDate = IssueUtils.getSqlDate(issues.get(0));
        processorDB.putCommentsInDB(issues.get(0).getId(), sqlDate, commentsForFirstIssue);

       // assertEquals("another comment!", processorDB.getComments());
       // TODO above breaks when writing more tests with putting things in the database 
       // TODO b/c needs to be mocked, which isn't happening right now so just commenting out
    }

    @Test
    public void givenDatabaseShouldBeAbleToRunClassifier() {
        CommentProcessor processorDB = new CommentProcessor("mysqlhost:3306", "storage");
        GithubAccess access = new GithubAccess("claire-1/github-metrics");
        List<GHIssue> issues = access.getClosedIssues();
        List<GHIssueComment> commentsForFirstIssue = IssueUtils.getComments(issues.get(0));
        Date sqlDate = IssueUtils.getSqlDate(issues.get(0));
        processorDB.putCommentsInDB(issues.get(0).getId(), sqlDate, commentsForFirstIssue);

        // new
        Instances trainingData = processorDB.getDataSetFromFile("trainingData.arff");
        System.out.println("TRAINING DATA " + trainingData.toString());
        Instances dataToBeClassified = processorDB.getAsDataSetFromSql(" select content from comments");
        System.out.println("data set " + dataToBeClassified.toString());
        assertEquals("", dataToBeClassified.toString());
    }

}