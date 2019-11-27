package com.app.mycompany;

import java.io.IOException;
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
}
