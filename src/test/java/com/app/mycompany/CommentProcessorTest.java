package com.app.mycompany;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import weka.core.Instances;

// import static org.junit.Assert.assertEquals;

// import java.sql.Date;
// import java.sql.SQLException;
// import java.util.List;

// import org.junit.Test;
// import org.kohsuke.github.GHIssue;
// import org.kohsuke.github.GHIssueComment;

// import weka.core.Instances;

public class CommentProcessorTest {

    @Test
    public void givenTrainedClassifierShouldClassifyString() throws Exception {
        CommentProcessor processor = new CommentProcessor();
        Instances trainingData = CommentProcessor.getDataSetFromFile(CommentProcessorTest.class.getResource("trainingDataTest.arff").getFile());
        trainingData.setClassIndex(0); // data formatted resolved, 'some string'
        
        String classification = processor.classifyData(trainingData, "u have won the 1 lakh prize");
        assertEquals("spam", classification);
    }

    // // Given a comment, put it in the database
    // @Test
    // public void givenDatabaseConnectionShouldConnectWithoutErrors() throws
    // SQLException {
    // // TODO not the greatest test either b/c really should mock/assume connection
    // // works so don't need to test it in the end
    // CommentProcessor processorDB = new CommentProcessor("comments-sql-db:3306",
    // "storage","root");
    // GithubAccess access = new GithubAccess("claire-1/github-metrics");
    // List<GHIssue> issues = access.getClosedIssues();
    // List<GHIssueComment> commentsForFirstIssue =
    // IssueUtils.getComments(issues.get(0));
    // Date sqlDate = IssueUtils.getSqlDate(issues.get(0));
    // processorDB.putCommentsInDB(issues.get(0).getId(), sqlDate,
    // commentsForFirstIssue);

    // // assertEquals("another comment!", processorDB.getComments());
    // // TODO above breaks when writing more tests with putting things in the
    // database
    // // TODO b/c needs to be mocked, which isn't happening right now so just
    // commenting out
    // }

    // @Test
    // public void givenDatabaseShouldBeAbleToRunClassifier() throws Exception {
    // CommentProcessor processorDB = new CommentProcessor("comments-sql-db:3306",
    // "storage", "root");
    // GithubAccess access = new GithubAccess("claire-1/github-metrics");
    // List<GHIssue> issues = access.getClosedIssues();
    // GHIssue secondIssue = issues.get(issues.size()-2);
    // List<GHIssueComment> commentsForFirstIssue =
    // IssueUtils.getComments(secondIssue);
    // Date sqlDate = IssueUtils.getSqlDate(secondIssue);
    // processorDB.putCommentsInDB(secondIssue.getId(), sqlDate,
    // commentsForFirstIssue);

    // // new
    // Instances trainingData = processorDB.getDataSetFromFile("trainingData.arff");
    // System.out.println("TRAINING DATA " + trainingData.toString());
    // Instances dataToBeClassified = processorDB.getAsDataSetFromSql(" select
    // content from comments");
    // System.out.println("data set " + dataToBeClassified.toString());
    // String classification = processorDB.classifyData(trainingData,
    // dataToBeClassified);

    // // Put in database --> TODO should really be own test but the issue with
    // adding to the database in different tests
    // processorDB.putClassificationInDB(secondIssue.getId(), sqlDate,
    // classification);
    // assertEquals("resolved", classification);
    // assertEquals("resolved", processorDB.getDataFromDatabase(" select * from
    // classifierResults"));
    // }

}