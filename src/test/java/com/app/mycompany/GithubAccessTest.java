// NOTE: I have abandoned these tests.

// package com.app.mycompany;

// import static org.junit.Assert.assertEquals;

// import java.util.List;

// import org.junit.Test;
// import org.kohsuke.github.GHIssue;
// import org.kohsuke.github.GHIssueComment;
// import org.kohsuke.github.GHRepository;

// /**
// * Unit test for simple Github Access.
// */
// public class GithubAccessTest {
// /**
// * Rigorous Test.
// */
// @Test
// public void givenNewConnectionAndRepoNameShouldGiveRepo() {
// GithubAccess github = new GithubAccess("claire-1/github-metrics");
// GHRepository repo = github.getRepo();
// assertEquals(repo.getName(), "github-metrics");
// }

// @Test
// public void givenRepoWithOneClosedIssueShouldGetOneClosedIssue() {
// GithubAccess github = new GithubAccess("claire-1/github-metrics");
// List<GHIssue> issues = github.getClosedIssues();

// assertEquals(2, issues.size());
// assertEquals("Here is my comment", issues.get(issues.size()-1).getBody());
// }

// @Test
// public void givenRepoWithCommentsOnAnIssueShouldGetFirstCommentAtLeast() {
// GithubAccess github = new GithubAccess("claire-1/github-metrics");
// List<GHIssue> issues = github.getClosedIssues();
// List<GHIssueComment> comments =
// IssueUtils.getComments(issues.get(issues.size()-1));

// assertEquals("another comment!", comments.get(0).getBody());
// }
// }
