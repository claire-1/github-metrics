package com.app.mycompany;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

/**
 * GithubAccess: Gets info from github
 */
public class GithubAccess {

    private GHRepository repo;

    public GithubAccess(String repo) {

        try {
            GitHub github = GitHub.connect();
            this.repo = github.getRepository(repo);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException("GithubAccess|could not get repo", e);
        }
    }

    public GHRepository getRepo() {
        return repo;
    }

    public List<GHIssue> getClosedIssues() {
        List<GHIssue> allIssues = new LinkedList<>();
        try {
            return repo.getIssues(GHIssueState.CLOSED);
        } catch (IOException e) {
            throw new RuntimeException("GithubAccess|could not get issues", e);
        }
        // return allIssues;
    }

    // TODO get all comments for an issue in a repo
    // https://github.com/github-api/github-api/blob/master/src/main/java/org/kohsuke/github/GHIssue.java#L287
    // TODO then need to classify them as closed or resolved using spam
    // classification algorithm

    public static void main(String[] args) throws Exception {
        // // GithubAccess github = new GithubAccess("claire-1/github-metrics");
        // // GithubAccess github = new GithubAccess("tootsuite/mastodon");
        // GithubAccess github = new GithubAccess("liyasthomas/postwoman");
        // MySqlConnection processorDB = new MySqlConnection("comments-sql-db:3306", "storage", "root");
        // // TODO change this back for issue with only getting some of the issues from
        // // github but not all if there are a lot
        // // TODO this is the thing to change GithubAccess github = new
        // // GithubAccess("tootsuite/mastodon");
        // List<GHIssue> issues = github.getClosedIssues();
        // // Process data to get classification
        // Instances trainingData = CommentProcessor.getDataSetFromFile("trainingData.arff");
        // trainingData.setClassIndex(0); // data formatted resolved, 'some string' TODO
        // System.out.println("TRAINING DATA " + trainingData.toString());

        // // System.out.println(issues.toString());
        // System.out.println("NUMBER OF ISSUES " + issues.size());

        // // // Put the comments in the database
        // for (int i = 0; i < issues.size(); i++) {
        //     GHIssue currentIssue = issues.get(i);

        //     // TODO HERE ---> freezes when getting lots of issues, might be due
        //     // to rate limitting on getComments, would be great if there was a way
        //     // just to get the last comment
        //     System.out.println("getting comments for issue number (starting at 0) " + i);
        //     List<GHIssueComment> currentIssueComments = IssueUtils.getComments(currentIssue);
        //     System.out.println("COMMENTS " + currentIssueComments.size());
        //     System.out.println(currentIssueComments);
        //     String lastestComment;
        //     if (currentIssueComments.size() > 1) {
        //         lastestComment = currentIssueComments.get(currentIssueComments.size() - 1).getBody();
        //         // currentIssueComments.get(currentIssueComments.size() - 1));
        //     } else {
        //         // There are no comments other than the issue body
        //         lastestComment = currentIssue.getBody();
        //     }
        //     // IssueUtils.getSqlDate(currentIssue), lastestComment);

        //     CommentProcessor commentProcessor = new CommentProcessor();
        //     String classification = commentProcessor.classifyData(trainingData, lastestComment);
        //     // System.out.println("classification " + classification);

        //     processorDB.putClassificationInDB(currentIssue.getId(), IssueUtils.getSqlDate(currentIssue),
        //             classification);
        //     // TODO delete these two following lines once have more data to get classifer to
        //     // work correctly
        //     ///// processorDB.putClassificationInDB(currentIssue.getId(),
        //     // IssueUtils.getSqlDate(currentIssue), "resolved");
        //     ////// processorDB.putClassificationInDB(currentIssue.getId(),
        //     // IssueUtils.getSqlDate(currentIssue), "unresolved");
        //     // processorDB.manipulateData(" delete from comments"); // execute a query to
        //     // clear the database of comments so
        //     // // can be empty for next issue's comments
        // }

        // ResultSet rsFromDB = processorDB.getDataFromDatabaseAsResultSet(
        //         " select DATE_FORMAT(dateIssueClosed, '%Y-%m') AS dateIssueClosed, SUM(CASE WHEN classifiedIssueStatus=\'unresolved\' THEN 1 ELSE 0 END) as numberIssuesUnresolved, SUM(CASE WHEN classifiedIssueStatus=\'resolved\' THEN 1 ELSE 0 END) as numberIssuesResolved from classifierResults GROUP BY DATE_FORMAT(dateIssueClosed, '%Y-%m') ORDER BY dateIssueClosed");
        // System.out.println("RESULT SET " + rsFromDB.toString());

        // JSONArray jsonOfDB = CommentProcessor.convertToJSON(rsFromDB);
        // System.out.println("JSON " + jsonOfDB.toString());

        // String filePath = System.getProperty("user.dir") + "/display/php/classifications.json";
        // System.out.println("FILE PATH " + filePath);
        // File classificationToDisplayFile = new File(filePath);

        // FileWriter writer = new FileWriter(classificationToDisplayFile);
        // // jsonOfDB.writeJSONString(jsonOfDB, classificationToDisplayFile);
        // JSONObject wrapJsonOfDB = new JSONObject();
        // wrapJsonOfDB.put("issuesArray", jsonOfDB);
        // writer.write(wrapJsonOfDB.toString());
        // writer.close();

        // // TODO need to make a way to close the connection
    }
}
