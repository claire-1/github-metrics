package com.app.mycompany;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueComment;

public class CommentProcessorTest {

    // Given a comment, put it in the database
    @Test
    public void givenDatabaseConnectionShouldConnectWithoutErrors() throws SQLException {
        // TODO not the greatest test either b/c really should mock/assume connection
        // works so don't need to test it in the end
        // TODO need to connect to dockerized database!!!!! (see docker-compose.yml)
        CommentProcessor processorDB = new CommentProcessor("mysqlhost:3306", "storage");
        GithubAccess access = new GithubAccess("claire-1/github-metrics");
        List<GHIssue> issues = access.getClosedIssues();
        List<GHIssueComment> commentsForFirstIssue = access.getComments(issues.get(0));
        // TODO source for date conversion: https://stackoverflow.com/questions/530012/how-to-convert-java-util-date-to-java-sql-date
        Date utilDate = issues.get(0).getClosedAt();
        // java.sql.Date date = java.sql.Date.valueOf(utilDate);
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        processorDB.putCommentsInDB(issues.get(0).getId(), sqlDate, commentsForFirstIssue);

        // long issueNumber = "GHIssueComment@4bb33f74[body=another
        // comment!,responseHeaderFields={null=[HTTP/1.1 200 OK],
        // X-Accepted-OAuth-Scopes=[], Server=[GitHub.com],
        // Access-Control-Allow-Origin=[*], Referrer-Policy=[origin-when-cross-origin,
        // strict-origin-when-cross-origin], X-Frame-Options=[deny],
        // Strict-Transport-Security=[max-age=31536000; includeSubdomains; preload],
        // Access-Control-Expose-Headers=[ETag, Link, Location, Retry-After,
        // X-GitHub-OTP, X-RateLimit-Limit, X-RateLimit-Remaining, X-RateLimit-Reset,
        // X-OAuth-Scopes, X-Accepted-OAuth-Scopes, X-Poll-Interval,
        // X-GitHub-Media-Type], X-RateLimit-Remaining=[4932],
        // X-GitHub-Media-Type=[unknown, github.v3],
        // Content-Security-Policy=[default-src 'none'], Content-Encoding=[gzip],
        // X-XSS-Protection=[1; mode=block],
        // X-GitHub-Request-Id=[A5D0:FEB0:11D72D2E:157482DD:5DC559A0],
        // Content-Type=[application/json; charset=utf-8], Status=[200 OK],
        // Transfer-Encoding=[chunked], X-Content-Type-Options=[nosniff],
        // X-RateLimit-Reset=[1573217776], Date=[Fri, 08 Nov 2019 12:03:44 GMT],
        // Cache-Control=[private, max-age=60, s-maxage=60],
        // ETag=[W/\"88be2388acc4ce5b3e06d316161e86a6\"], Vary=[Accept-Encoding, Accept,
        // Authorization, Cookie, X-GitHub-OTP], X-RateLimit-Limit=[5000],
        // X-OAuth-Scopes=[]},url=https://api.github.com/repos/claire-1/github-metrics/issues/comments/549185670,id=549185670]";
        assertEquals("another comment!", processorDB.getComments());
    }

}