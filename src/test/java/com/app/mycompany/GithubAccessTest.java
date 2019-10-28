package com.app.mycompany;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
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



}
