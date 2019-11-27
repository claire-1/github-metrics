CREATE DATABASE IF NOT EXISTS storage;
USE storage;
CREATE TABLE IF NOT EXISTS classifierResults (
    relatedIssueUrl VARCHAR(2083),
    dateIssueClosed date,
    classifiedIssueStatus text
);
