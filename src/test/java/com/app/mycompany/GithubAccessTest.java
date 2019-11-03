package com.app.mycompany;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHRepository;

/**
 * Unit test for simple Github Access.
 */
public class GithubAccessTest {
    /**
     * Rigorous Test.
     */
    @Test
    public void givenNewConnectionAndRepoNameShouldGiveRepo() {
        GithubAccess github = new GithubAccess("claire-1/github-metrics");
        GHRepository repo = github.getRepo();
        assertEquals(repo.getName(), "github-metrics");
    }

    @Test
    public void givenRepoWithClosedIssuesShouldIssues() {
        List<GHIssue> expected = new LinkedList<>();
       // expected.add("");
       // TODO need to put an issue in here if want this test in the long run

        GithubAccess github = new GithubAccess("claire-1/github-metrics");
        List<GHIssue> actual = github.getClosedIssues();
        
        assertEquals(expected, actual); 
    }



}
