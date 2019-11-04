package com.app.mycompany;

import java.io.IOException;
import java.util.List;

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
            return repo.getIssues(GHIssueState.CLOSED);
        } catch (IOException e) {
            throw new RuntimeException("GithubAccess|could not get issues", e);
        }
    }

    public List<GHIssueComment> getComments(GHIssue issue) {
        try {
            return issue.getComments();
        } catch (IOException e) {
            throw new RuntimeException("GithubAccess|could not get issue comments", e);
        }
    }
    // TODO get all comments for an issue in a repo
    // https://github.com/github-api/github-api/blob/master/src/main/java/org/kohsuke/github/GHIssue.java#L287
    // TODO then need to classify them as closed or resolved using spam classification algorithm

}
