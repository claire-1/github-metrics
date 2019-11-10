CREATE DATABASE IF NOT EXISTS storage;
USE storage;
CREATE TABLE IF NOT EXISTS comments (
    relatedIssueId bigint,
    dateIssueClosed date,
    content text
);

CREATE TABLE IF NOT EXISTS classifierResults (
    relatedIssueId bigint,
    dateIssueClosed date,
    classifiedIssueStatus text
);
