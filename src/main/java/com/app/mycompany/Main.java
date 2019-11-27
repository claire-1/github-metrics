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

import weka.core.Instance;
import weka.core.Instances;

/**
 * Main: Gets issues from readme, classifies them, writes the result to a JSON
 * file to be displayed later.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        String repo = "claire-1/github-metrics";
        // String repo = "tootsuite/mastodon";
        //String repo = "liyasthomas/postwoman";
        GithubAccess github = new GithubAccess(repo);

        MySqlConnection processorDB = new MySqlConnection("comments-sql-db:3306", "storage", "root");
        // TODO change this back for issue with only getting some of the issues from
        // github but not all if there are a lot
        // TODO this is the thing to change GithubAccess github = new
        // GithubAccess("tootsuite/mastodon");
        List<GHIssue> issues = github.getClosedIssues();
        // Process data to get classification
        // Instances trainingData =
        // CommentProcessor.getDataSetFromFile("trainingData.arff");
        // trainingData.setClassIndex(0); // data formatted resolved, 'some string' TODO
        // System.out.println("TRAINING DATA " + trainingData.toString());

        FilteredClassifierTrainer trainer = new FilteredClassifierTrainer();
        Instances trainingData = trainer.loadDataset("trainingData.arff");
        // Evaluation mus be done before training
        // More info in: http://weka.wikispaces.com/Use+WEKA+in+your+Java+code
        trainer.evaluate();
        trainer.learn();
        trainer.saveModel("trainingDataModel.arff");
        // Get the last comment for each issue
        for (int i = 0; i < issues.size(); i++) {
            GHIssue currentIssue = issues.get(i);

            // TODO HERE ---> freezes when getting lots of issues, might be due
            // to rate limitting on getComments, would be great if there was a way
            // just to get the last comment
            System.out.println("getting comments for issue number (starting at 0) " + i);
            List<GHIssueComment> currentIssueComments = IssueUtils.getComments(currentIssue);
            System.out.println("COMMENTS " + currentIssueComments.size());
            System.out.println(currentIssueComments);
            String lastestComment;
            if (currentIssueComments.size() > 1) {
                lastestComment = currentIssueComments.get(currentIssueComments.size() - 1).getBody();
                // currentIssueComments.get(currentIssueComments.size() - 1));
            } else {
                // There are no comments other than the issue body
                lastestComment = currentIssue.getBody();
            }

            SimpleFilteredClassifier classifier = new SimpleFilteredClassifier();
            classifier.loadModel("trainingDataModel.arff");
            Instance dataToClassify = classifier.makeInstance(lastestComment, "resolved", "unresolved");
            // There is just one instance because there is one comment processed at at time

            // instance(0) TODO
            String classification = classifier.classify(); 

            processorDB.putClassificationInDB(repo, currentIssue.getNumber(), IssueUtils.getSqlDate(currentIssue),
                    classification);
            // TODO delete these two following lines once have more data to get classifer to
            // work correctly
            // processorDB.putClassificationInDB(currentIssue.getUrl(),
            // IssueUtils.getSqlDate(currentIssue), "resolved");
            // processorDB.putClassificationInDB(currentIssue.getUrl(),
            // IssueUtils.getSqlDate(currentIssue), "unresolved");

        }

        ResultSet rsFromDB = processorDB.getDataFromDatabaseAsResultSet(
                " select DATE_FORMAT(dateIssueClosed, '%Y-%m') AS dateIssueClosed, SUM(CASE WHEN classifiedIssueStatus=\'unresolved\' THEN 1 ELSE 0 END) as numberIssuesUnresolved, SUM(CASE WHEN classifiedIssueStatus=\'resolved\' THEN 1 ELSE 0 END) as numberIssuesResolved from classifierResults GROUP BY DATE_FORMAT(dateIssueClosed, '%Y-%m') ORDER BY dateIssueClosed");

        JSONArray jsonOfDB = ResultSetUtils.convertToJSON(rsFromDB);
        System.out.println("JSON " + jsonOfDB.toString());

        String filePath = System.getProperty("user.dir") + "/display/php/classifications.json";
        System.out.println("FILE PATH " + filePath);
        File classificationToDisplayFile = new File(filePath);

        FileWriter writer = new FileWriter(classificationToDisplayFile);
        // jsonOfDB.writeJSONString(jsonOfDB, classificationToDisplayFile);
        JSONObject wrapJsonOfDB = new JSONObject();
        wrapJsonOfDB.put("issuesArray", jsonOfDB);
        writer.write(wrapJsonOfDB.toString());
        writer.close();

        // TRY WRITING TO CSV
        ResultSet csvRSFromDB = processorDB.getDataFromDatabaseAsResultSet(
                " select relatedIssueUrl, classifiedIssueStatus, DATE_FORMAT(dateIssueClosed, '%Y-%m') AS dateIssueClosed from classifierResults");
        // TODO source:
        // http://www.codecodex.com/wiki/Write_a_SQL_result_set_to_a_comma_seperated_value_(CSV)_file
        String csvFilePath = System.getProperty("user.dir") + "/display/php/classifications.csv";
        CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFilePath), '\t', CSVWriter.DEFAULT_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);

        boolean includeHeaders = true;
        csvWriter.writeAll(csvRSFromDB, includeHeaders);
        csvWriter.close();

        processorDB.close();
    }
}
