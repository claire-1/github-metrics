CREATE DATABASE IF NOT EXISTS storage;
USE storage;
CREATE TABLE IF NOT EXISTS comments (
    relatedIssueId int,
    dateIssueClosed date,
    content char
);