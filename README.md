# github-metrics
For SWE module. To display information about engineering data gotten through the GitHub API.

## Measuring a software engineer report
My report on measuring software engineers can be found in this repo: https://github.com/claire-1/software-engineering-module. See the README there for the name of the file.

## Github Access
As of commit 28baed9d3c4b461f8fa2ae6dc12bd5f5d88550c4, I can access Github through a Java Github API. To run this, run `./run.sh` in the `github-metrics` directory. This requires docker to be installed to work, and you need sudo access. My authentication token for Github access is in `github.env`. I do not know if you will need your own or not, but you can change it there if you need to. The part of the code that prints information about issues, such as the one shown below, is in `GithubAccess.java`. 
Example output from getting all the issues associated with this repository at this time:
`
[GHIssue@3f577e31[assignee=<null>,assignees={},state=closed,number=1,comments=1,labels=[],title=WIP test comment title,milestone=<null>,locked=false,responseHeaderFields={null=[HTTP/1.1 200 OK], X-Accepted-OAuth-Scopes=[repo], Server=[GitHub.com], Access-Control-Allow-Origin=[*], Referrer-Policy=[origin-when-cross-origin, strict-origin-when-cross-origin], X-Frame-Options=[deny], Strict-Transport-Security=[max-age=31536000; includeSubdomains; preload], Access-Control-Expose-Headers=[ETag, Link, Location, Retry-After, X-GitHub-OTP, X-RateLimit-Limit, X-RateLimit-Remaining, X-RateLimit-Reset, X-OAuth-Scopes, X-Accepted-OAuth-Scopes, X-Poll-Interval, X-GitHub-Media-Type], X-RateLimit-Remaining=[4892], X-GitHub-Media-Type=[unknown, github.v3], Content-Security-Policy=[default-src 'none'], Content-Encoding=[gzip], X-XSS-Protection=[1; mode=block], X-GitHub-Request-Id=[FACA:AA07:5A08524:6C41D99:5DC18420], Content-Type=[application/json; charset=utf-8], Status=[200 OK], Transfer-Encoding=[chunked], X-Content-Type-Options=[nosniff], X-RateLimit-Reset=[1572966946], Date=[Tue, 05 Nov 2019 14:16:01 GMT], Cache-Control=[private, max-age=60, s-maxage=60], ETag=[W/"b3a1298eec4b8d416de012fa9207c3b6"], Vary=[Accept-Encoding, Accept, Authorization, Cookie, X-GitHub-OTP], X-RateLimit-Limit=[5000], X-OAuth-Scopes=[]},url=https://api.github.com/repos/claire-1/github-metrics/issues/1,id=516898487]]
`
## Souces
Java Github API: https://github.com/github-api \
About how to get a Github OAuth token: https://help.github.com/en/github/authenticating-to-github/creating-a-personal-access-token-for-the-command-line \
About how to specify Github OAuth token as an environment variable: https://issues.jenkins-ci.org/browse/JENKINS-37581 \
About how to execute Java with maven: https://stackoverflow.com/questions/18011494/failed-to-execute-goal-org-codehaus-mojoexec-maven-plugin1-2java-default-cli \
Example arff file for training data http://users.eecs.northwestern.edu/~ahu340/eecs349-ps1/train.arff \
package.json file needed to init npm from: https://github.com/yyx990803/npm-http-server/blob/master/package.json \
Source for how to use Naive Bayes from Weka in Java: https://www.codingame.com/playgrounds/6734/machine-learning-with-java---part-5-naive-bayes \

Sources for query to get classification results: \
https://stackoverflow.com/questions/14565788/how-to-group-by-month-from-date-field-using-sql \
https://stackoverflow.com/questions/53848520/group-by-several-columns-with-count-on-another-column-sql-server \
https://www.w3schools.com/sql/trymysql.asp?filename=trysql_func_mysql_date_format \

Source for FileWriter https://www.journaldev.com/878/java-write-to-file \
Source for making String comment into Instances with Attributes: https://github.com/alfredfrancis/spam-classification-weka-java/blob/master/WekaClassifier.java \
Source for dealing with ResultSets: https://www.javatpoint.com/example-to-connect-to-the-mysql-database \
Weka and getting Instances from SQL database: https://waikato.github.io/weka-wiki/use_weka_in_your_java_code/ \
Source for how to use PreparedStatements in Java: https://alvinalexander.com/java/java-mysql-insert-example-preparedstatement \