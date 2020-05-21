# github-metrics
To display information about engineering data gotten through the GitHub API.

I wrote a blog post about this project [here](https://dev.to/claire1/ai-github-api-issue-sentiment-classification-4913)!

## Social Graph Project Description
For this project, I got comments from issues on Github repositories. I used Naive Bayes to do sentiment analysis on the comments to decide if they were really resolved or if they were simply closed. I choose Naive Bayes to be my classification algorithm because it is the algorithm traditionally used for spam detection, which I thought was a similar process to this. After I classify the issues, I display the results in a bar chat and in a bubble chart. The bar chart lets you see the number of issues resolved and closed over time. The bubble chart lets you see the individual resolved and closed issues and click on links to see more information about each issue. 

I used an API to access Github referenced below. This API is not perfect. It does not tell you if it cannot get all the issues for a repository due to rate limiting so if there are a large number of closed issues, this social graph might not display all of them since it doesn't know that there are more and doesn't have a way to get the others. Also, this API also includes closed pull requests when it says it is getting all the closed issues. 

For the classification algorithm, after struggling for quite a while to make the classifier work, I found a tutorial and associated code that I was able to make work, both referenced below. The results of the classifier heavily depends on the data specified in the training data file. To show that this classifier works, I found an arff file with a lot of data for spam detection and then wrote tests using that data to do spam detection. The tests can be found in `MainTest.java` . For classifying Github issues, I looked at the closed issues in `liyasthomas/postwoman` can copied over the final comments. Most of the issues in this repository seem to be resolved so I made the decision to classify things like saying that the issue is a duplicate and then closing the issue is really an unresolved issue. I choose to classify in this way so I could see different issues being classified in different ways. It is clear that there is a lot of bias in the classification based on how the training data is classified, and sentiment analysis is quite difficult. Also, it seems likely that links to related pull requests give a better suggestion as to whether or not an issue is resolved so those would be good to include in future implementations. If this Social Graph was going to be used in a work-place, much thought would need to be put into collecting and classifying data. 

I ran my Social Graph for these repositories for testing: `claire-1/github-metrics`, `tootsuite/mastodon`, and `liyasthomas/postwoman`. `tootsuite/mastodon` has the most Github issues and takes a very long time to run. 


## How to run
This project is fully dockerized. You must have docker and docker-compose installed. To run this project, go to the `github-metrics` directory and then run `./run.sh`. You must have `sudo` access for this project to run. If you run this project a lot, you may wish to use `sudo docker volume rm $(sudo docker volume ls -qf dangling=true)` and `sudo docker system prune -a` to clean up unused docker images. To see the displayed pages, visit `http://localhost:8080/bubble.html` or `http://localhost:8080/bar-chart.html`.

## Implementation Details
I implemented this project in Java. In one docker container, it fetches using a Github Java API referenced below. Then, it runs a Naive Bayes classifier on the last comment on the closed issue to decide whether the issue was actually resolved or just closed. This classification is then put into a SQL database running in another docker container. Once all the issues have been processed, it queries the database to calculate the number of closed issues and group them by the times they were closed. Then, it outputs the result of that query in a JSON file to be displayed by `bar-chart.html`. Next, it queries the database again to get the url associated with each issue in the correct format to be able transform the result of this query into a `csv` file. `bubble.html` uses this `csv` file to display all the closed issues with clickable links. To display, the start-up script for the main docker container runs the `http-server` command to start a webserver at `localhost:8080` that is accessible from the host machine of the docker container. I found that running this command was the easiest way to display the webpage to the host computer from within the docker container. 

## Social Graph Demos
This gif shows the data as displayed by `bubble.html.` It has a different color for resolved and unresolved issues and displays links to more details about the issues when you hover over the bubbles. \
![gif of bubble page](demo/bubbleChart.gif)


Image of bar chart produced by `bar-chart.html.` It has a different color for resolved and unresolved issues and groups the issues by month and year. \
![image of bar chart](demo/barChartTest.png)


Gif of the button working to go between the bar chart and the bubble chart. \
![gif of button working to go between bar chart and bubble chart pages](demo/button.gif)


## Github Access
As of commit 28baed9d3c4b461f8fa2ae6dc12bd5f5d88550c4, I can access Github through a Java Github API. To run this, run `./run.sh` in the `github-metrics` directory. This requires docker to be installed to work, and you need sudo access. My authentication token for Github access is in `github.env`. I do not know if you will need your own or not, but you can change it there if you need to. The part of the code that prints information about issues, such as the one shown below, is in `GithubAccess.java`. 

## Github Access Example Output
Example output from getting all the issues associated with this repository at this time:
`
[GHIssue@3f577e31[assignee=<null>,assignees={},state=closed,number=1,comments=1,labels=[],title=WIP test comment title,milestone=<null>,locked=false,responseHeaderFields={null=[HTTP/1.1 200 OK], X-Accepted-OAuth-Scopes=[repo], Server=[GitHub.com], Access-Control-Allow-Origin=[*], Referrer-Policy=[origin-when-cross-origin, strict-origin-when-cross-origin], X-Frame-Options=[deny], Strict-Transport-Security=[max-age=31536000; includeSubdomains; preload], Access-Control-Expose-Headers=[ETag, Link, Location, Retry-After, X-GitHub-OTP, X-RateLimit-Limit, X-RateLimit-Remaining, X-RateLimit-Reset, X-OAuth-Scopes, X-Accepted-OAuth-Scopes, X-Poll-Interval, X-GitHub-Media-Type], X-RateLimit-Remaining=[4892], X-GitHub-Media-Type=[unknown, github.v3], Content-Security-Policy=[default-src 'none'], Content-Encoding=[gzip], X-XSS-Protection=[1; mode=block], X-GitHub-Request-Id=[FACA:AA07:5A08524:6C41D99:5DC18420], Content-Type=[application/json; charset=utf-8], Status=[200 OK], Transfer-Encoding=[chunked], X-Content-Type-Options=[nosniff], X-RateLimit-Reset=[1572966946], Date=[Tue, 05 Nov 2019 14:16:01 GMT], Cache-Control=[private, max-age=60, s-maxage=60], ETag=[W/"b3a1298eec4b8d416de012fa9207c3b6"], Vary=[Accept-Encoding, Accept, Authorization, Cookie, X-GitHub-OTP], X-RateLimit-Limit=[5000], X-OAuth-Scopes=[]},url=https://api.github.com/repos/claire-1/github-metrics/issues/1,id=516898487]]
`

## Souces
### Very Important Sources
Java Github API: https://github.com/github-api/github-api \
Source for bubble chart: https://www.freecodecamp.org/news/a-gentle-introduction-to-d3-how-to-build-a-reusable-bubble-chart-9106dc4f6c46/ and https://github.com/dmesquita/reusable_bubble_chart \
Source for working classification: based on this tutorial http://jmgomezhidalgo.blogspot.com/2013/04/a-simple-text-classifier-in-java-with.html with this repo https://github.com/jmgomezh/tmweka/tree/master/FilteredClassifier \
Source for bar chart: newest source: https://stackoverflow.com/questions/39169948/grouped-bar-chart-from-json-data-instead-of-csv

### Other Important Sources
About how to get a Github OAuth token: https://help.github.com/en/github/authenticating-to-github/creating-a-personal-access-token-for-the-command-line \
About how to specify Github OAuth token as an environment variable: https://issues.jenkins-ci.org/browse/JENKINS-37581 \
About how to execute Java with maven: https://stackoverflow.com/questions/18011494/failed-to-execute-goal-org-codehaus-mojoexec-maven-plugin1-2java-default-cli \
Example arff file for training data http://users.eecs.northwestern.edu/~ahu340/eecs349-ps1/train.arff \
package.json file needed to init npm from: https://github.com/yyx990803/npm-http-server/blob/master/package.json \
Source for how to use Naive Bayes from Weka in Java: https://www.codingame.com/playgrounds/6734/machine-learning-with-java---part-5-naive-bayes \

For displaying a simple graph with d3: download file D3-Tips-and-Tricks-3902-extras/d3jscodesamples/13/simple-graph.html from http://www.d3noob.org/2013/02/using-mysql-database-as-source-of-data.html  \

Sources for query to get classification results: \
https://stackoverflow.com/questions/14565788/how-to-group-by-month-from-date-field-using-sql \
https://stackoverflow.com/questions/53848520/group-by-several-columns-with-count-on-another-column-sql-server \
https://www.w3schools.com/sql/trymysql.asp?filename=trysql_func_mysql_date_format \

Source for FileWriter https://www.journaldev.com/878/java-write-to-file \
Source for making String comment into Instances with Attributes: https://github.com/alfredfrancis/spam-classification-weka-java/blob/master/WekaClassifier.java \
Source for dealing with ResultSets: https://www.javatpoint.com/example-to-connect-to-the-mysql-database \
Weka and getting Instances from SQL database: https://waikato.github.io/weka-wiki/use_weka_in_your_java_code/ \
Source for how to use PreparedStatements in Java: https://alvinalexander.com/java/java-mysql-insert-example-preparedstatement \
Source for checking return value of bash command for start.sh: https://stackoverflow.com/questions/15471264/how-to-check-if-is-not-equal-to-zero-in-unix-shell-scripting/15471312 \
Source for how to convert ResultSet to JSON http://biercoff.com/nice-and-simple-converter-of-java-resultset-into-jsonarray-or-xml/ \
Source for writing ResultSet to CSV http://www.codecodex.com/wiki/Write_a_SQL_result_set_to_a_comma_seperated_value_(CSV)_file \
Source for changing Java util date to SQL date https://stackoverflow.com/questions/530012/how-to-convert-java-util-date-to-java-sql-date \
Source for making SQL connection: https://javarevisited.blogspot.com/2016/09/javasqlsqlexception-no-suitable-driver-mysql-jdbc-localhost.html#ixzz64gKoyLbl \
Source for button for webpage: https://www.w3schools.com/css/tryit.asp?filename=trycss_buttons_hover \
Source for SQL in docker container: https://stackoverflow.com/questions/36617682/docker-compose-mysql-import-sql  \
