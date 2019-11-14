package com.app.mycompany;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueComment;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import weka.core.Instances;

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
            // while () {
            // repo.listIssues(GHIssueState.CLOSED);
            // }

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
        System.out.println("HELLLOOOOOOO");
        GithubAccess github = new GithubAccess("claire-1/github-metrics");
        // TODO change this back for issue with only getting some of the issues from github but not all if there are a lot
        // TODO this is the thing to change GithubAccess github = new GithubAccess("tootsuite/mastodon");
        List<GHIssue> issues = github.getClosedIssues();

        // System.out.println(issues.toString());
        System.out.println("NUMBER OF ISSUES " + issues.size());

        // Put the comments in the database
        GHIssue currentIssue = issues.get(issues.size() - 1);// TODO just the first issue for now
        List<GHIssueComment> comments = IssueUtils.getComments(currentIssue);
        System.out.println("COMMENTS " + comments.size());
        System.out.println(comments);
        CommentProcessor processorDB = new CommentProcessor("comments-sql-db:3306", "storage", "root");
        processorDB.putCommentsInDB(currentIssue.getId(), IssueUtils.getSqlDate(issues.get(0)), comments);
        // TimeUnit.SECONDS.sleep(5); // Sleep so you can see the output from the
        // container before it finishes

        // Process data to get classification
        Instances trainingData = processorDB.getDataSetFromFile("trainingData.arff");
        System.out.println("TRAINING DATA " + trainingData.toString());
        Instances dataToBeClassified = processorDB.getAsDataSetFromSql(" select content from comments");
        System.out.println("data set " + dataToBeClassified.toString());
        String classification = processorDB.classifyData(trainingData, dataToBeClassified);

        // Put in database --> TODO should really be own test but the issue with adding
        // to the database in different tests
        processorDB.putClassificationInDB(currentIssue.getId(), IssueUtils.getSqlDate(issues.get(0)), classification);

        // File webPage = new File("simple-graph.html");
        // try {
        //     Desktop.getDesktop.browse(webPage.toURI());
        // } catch (IOException e) {
        //     // TODO
        // }

    // JFrame myFrame = new JFrame();
    //     try
	// {
	// String html;
	// html="<html><head><title>Simple Page</title></head>";
	// html+="<body bgcolor='#777779'><hr/><font size=50>This is Html content</font><hr/>";
	// html+="</body></html>";
	// JEditorPane ed1=new JEditorPane("text/html",html);
	// myFrame.add(ed1);
	// myFrame.setVisible(true);
	// myFrame.setSize(600,600);
	// myFrame.setDefaultCloseOperation(0);
	// }
	// catch(Exception e)
	// {
	// 	e.printStackTrace();
	// 	System.out.println("Some problem has occured"+e.getMessage());
	// }

    // System.out.println("Should be displaying the webpage at clairesmetricshostname:80");
    // TimeUnit.SECONDS.sleep(120); // Sleep so you can see the output from the



// TODO source for Java to HTML ^^^^ Read more: http://mrbool.com/display-html-contents-with-java/24532#ixzz65HkdI7bf




        // TODO need to make a way to close the connection
        // b/c
        // thread
        // Thread[mysql-cj-abandoned-connection-cleanup,5,com.app.mycompany.GithubAccess]
        // will linger despite being asked to die via interruption
        // metrics_1 | [WARNING] NOTE: 1 thread(s) did not finish despite being asked to
        // via interruption. This is not a problem with exec:java, it is a problem with
        // the running code. Although not serious, it should be remedied.
        // metrics_1 | [WARNING] Couldn't destroy threadgroup
        // org.codehaus.mojo.exec.ExecJavaMojo$IsolatedThreadGroup[name=com.app.mycompany.GithubAccess,maxpri=10]
        // metrics_1 | java.lang.IllegalThreadStateException
        // metrics_1 | at java.lang.ThreadGroup.destroy(
    }
}
