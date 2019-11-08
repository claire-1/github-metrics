package com.app.mycompany;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueComment;
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
        try {
            // TODO should really go through and make GHIssueForSql issues and then return a
            // list of that
            // but that might be too much work right now for this project
            return repo.getIssues(GHIssueState.CLOSED);
        } catch (IOException e) {
            throw new RuntimeException("GithubAccess|could not get issues", e);
        }
    }

    // TODO get all comments for an issue in a repo
    // https://github.com/github-api/github-api/blob/master/src/main/java/org/kohsuke/github/GHIssue.java#L287
    // TODO then need to classify them as closed or resolved using spam
    // classification algorithm

    public static void main(String[] args) throws InterruptedException {
        System.out.println("HELLLOOOOOOO");
        GithubAccess github = new GithubAccess("claire-1/github-metrics");
        List<GHIssue> issues = github.getClosedIssues();

        System.out.println(issues.toString());
        TimeUnit.SECONDS.sleep(5); // Sleep so you can see the output from the container before it finishes

        GHIssue currentIssue = issues.get(0);// TODO just the first issue for now
        List<GHIssueComment> comments = IssueUtils.getComments(currentIssue);
        CommentProcessor processorDB = new CommentProcessor("mysqlhost:3306", "storage");
        processorDB.putCommentsInDB(currentIssue.getId(),
                IssueUtils.getSqlDate(issues.get(0)) /* TODO need to make an issue class */, comments);
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
