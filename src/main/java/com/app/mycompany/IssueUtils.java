package com.app.mycompany;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueComment;

/*
 * Class for processing issues
 */

public class IssueUtils {

    public static List<GHIssueComment> getComments(GHIssue issue) {
        try {
            return issue.getComments();
        } catch (IOException e) {
            throw new RuntimeException("GithubAccess|could not get issue comments", e);
        }
    }

    public static Date getSqlDate(GHIssue issue) {
        java.util.Date utilDate = issue.getClosedAt();
        Date sqlDate = new Date(utilDate.getTime());

        return sqlDate;
    }
}