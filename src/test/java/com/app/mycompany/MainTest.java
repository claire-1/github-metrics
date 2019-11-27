package com.app.mycompany;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import weka.core.Instances;

public class MainTest {

    // Classifier tests
    @Test
    public void givenTrainedClassifierShouldClassifyStringAsSpam() throws Exception {
        FilteredClassifierTrainer trainer = new FilteredClassifierTrainer();
        Instances trainingData = trainer.loadDataset(MainTest.class.getResource("/trainingDataTest.arff").getFile());
        // Evaluation mus be done before training
        // More info in: http://weka.wikispaces.com/Use+WEKA+in+your+Java+code
        trainer.evaluate(trainingData);
        trainer.trainClassifier(trainingData);
        trainer.saveModel(MainTest.class.getResource("/trainingDataModelTest.arff").getFile());
        String modelFile = MainTest.class.getResource("/trainingDataModelTest.arff").getFile();
        SimpleFilteredClassifier classifier = new SimpleFilteredClassifier(modelFile);
        Instances unlabeledData = classifier.makeInstanceInInstances("u have won the 1 lakh prize", "spam", "ham");
        String classification = classifier.classify(unlabeledData);
        assertEquals("spam", classification);
    }

    @Test
    public void givenTrainedClassifierShouldClassifyStringAsHam() throws Exception {
        FilteredClassifierTrainer trainer = new FilteredClassifierTrainer();
        Instances trainingData = trainer.loadDataset(MainTest.class.getResource("/trainingDataTest.arff").getFile());
        trainer.evaluate(trainingData);
        trainer.trainClassifier(trainingData);
        trainer.saveModel(MainTest.class.getResource("/trainingDataModelTest.arff").getFile());
        String modelFile = MainTest.class.getResource("/trainingDataModelTest.arff").getFile();
        SimpleFilteredClassifier classifier = new SimpleFilteredClassifier(modelFile);
        Instances unlabeledData = classifier.makeInstanceInInstances("how are you ?", "spam", "ham");
        String classification = classifier.classify(unlabeledData);
        assertEquals("ham", classification);
    }
    // End of classifier tests

    // // Given a comment, put it in the database
    // @Test
    // public void givenDatabaseConnectionShouldConnectWithoutErrors() throws
    // SQLException {
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

    // processorDB.putClassificationInDB(secondIssue.getId(), sqlDate,
    // classification);
    // assertEquals("resolved", classification);
    // assertEquals("resolved", processorDB.getDataFromDatabase(" select * from
    // classifierResults"));
    // }

}