package com.app.mycompany;

import java.io.File;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.util.List;

import com.opencsv.CSVWriter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueComment;

import weka.core.Instances;

/**
 * Main: Gets issues from readme, classifies them, writes the result to a JSON
 * file to be displayed later.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        // String repo = "claire-1/github-metrics";
        // String repo = "tootsuite/mastodon";
        String repo = "liyasthomas/postwoman";
        GithubAccess github = new GithubAccess(repo);

        SqlConnection processorDB = new SqlConnection("comments-sql-db:3306", "storage", "root", "root");

        // Get all the issues.
        List<GHIssue> issues = github.getClosedIssues();

        // Train classifier.
        FilteredClassifierTrainer trainer = new FilteredClassifierTrainer();
        String trainingDataFile = Main.class.getResource("/trainingData.arff").getFile();
        Instances trainingData = trainer.loadDataset(trainingDataFile);
        // Evaluation must be done before training
        // More info in: http://weka.wikispaces.com/Use+WEKA+in+your+Java+code
        trainer.evaluate(trainingData);
        trainer.trainClassifier(trainingData);
        trainer.saveModel("trainingDataModel.arff");
        // Get the last comment for each issue
        for (int i = 0; i < issues.size(); i++) {
            GHIssue currentIssue = issues.get(i);
            
            // Get all the comments since there isn't a method to just get one specific comment.
            List<GHIssueComment> currentIssueComments = IssueUtils.getComments(currentIssue);
            String lastestComment;
            if (currentIssueComments.size() > 1) {
                // Get the last comment
                lastestComment = currentIssueComments.get(currentIssueComments.size() - 1).getBody();
            } else {
                // There are no comments other than the issue body
                lastestComment = currentIssue.getBody();
            }

            SimpleFilteredClassifier classifier = new SimpleFilteredClassifier("trainingDataModel.arff");
            Instances dataToClassify = classifier.makeInstanceInInstances(lastestComment, "resolved", "unresolved");

            String classification = classifier.classify(dataToClassify);
            processorDB.putClassificationInDB(repo, currentIssue.getNumber(), IssueUtils.getSqlDate(currentIssue),
                    classification);
        }

        ResultSet rsFromDB = processorDB.getDataFromDatabaseAsResultSet(
                " select DATE_FORMAT(dateIssueClosed, '%Y-%m') AS dateIssueClosed, SUM(CASE WHEN classifiedIssueStatus=\'unresolved\' THEN 1 ELSE 0 END) as numberIssuesUnresolved, SUM(CASE WHEN classifiedIssueStatus=\'resolved\' THEN 1 ELSE 0 END) as numberIssuesResolved from classifierResults GROUP BY DATE_FORMAT(dateIssueClosed, '%Y-%m') ORDER BY dateIssueClosed");

        JSONArray jsonOfDB = ResultSetUtils.convertToJSON(rsFromDB);
        // System.out.println("Json results: " + jsonOfDB.toString());

        String filePath = System.getProperty("user.dir") + "/display/classificationOutput/classifications.json";
        File classificationToDisplayFile = new File(filePath);

        FileWriter writer = new FileWriter(classificationToDisplayFile);
        JSONObject wrapJsonOfDB = new JSONObject();
        wrapJsonOfDB.put("issuesArray", jsonOfDB);
        writer.write(wrapJsonOfDB.toString());
        writer.close();

        // Write to CSV
        ResultSet csvRSFromDB = processorDB.getDataFromDatabaseAsResultSet(
                " select relatedIssueUrl, classifiedIssueStatus, DATE_FORMAT(dateIssueClosed, '%Y-%m') AS dateIssueClosed from classifierResults");
        String csvFilePath = System.getProperty("user.dir") + "/display/classificationOutput/classifications.csv";
        CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFilePath), '\t', CSVWriter.DEFAULT_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);

        boolean includeHeaders = true;
        csvWriter.writeAll(csvRSFromDB, includeHeaders);
        csvWriter.close();

        processorDB.close();
    }
}
