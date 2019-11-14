<!-- TODO Source: http://www.d3noob.org/2013/02/using-mysql-database-as-source-of-data.html -->
<?php
    $username = "root"; 
    $password = "";   
    $host = "comments-sql-db:3306";
    $database="storage";
    
    $server = mysql_connect($host, $username, $password);
    $connection = mysql_select_db($database, $server);

    $myquery = "
SELECT  `relatedIssueId`, `dateIssueClosed` FROM  `classifierResults`
";
// <!--TODO need to add classifiedIssueStatus here-->
    $query = mysql_query($myquery);
    
    if ( ! $query ) {
        echo mysql_error();
        die;
    }
    
    $data = array();
    
    for ($x = 0; $x < mysql_num_rows($query); $x++) {
        $data[] = mysql_fetch_assoc($query);
    }
    
    echo json_encode($data);     
     
    mysql_close($server);
?>