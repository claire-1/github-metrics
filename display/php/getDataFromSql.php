<!-- TODO Source: http://www.d3noob.org/2013/02/using-mysql-database-as-source-of-data.html
TODO new source: https://stackoverflow.com/questions/3351882/how-to-convert-mysqli-result-to-json -->


<?php
    $username = "root"; 
    $password = "root";   
    $host = "comments-sql-db";
    //$port = "3306";
    $database="storage";
    
    $server = mysql_connect($host, $username, $password)
        or die('Error connecting to mysql: '.mysql_error());

    $connection = mysql_select_db($database, $server);

    $myquery = "
SELECT  `relatedIssueId`, `dateIssueClosed` FROM  `classifierResults`
";
    $query = mysql_query($myquery);
    
    if ( ! $query ) {
        echo mysql_error();
        die;
    }
    
    $data = array();
    
    for ($x = 0; $x < mysql_num_rows($query); $x++) {
        $data[] = mysql_fetch_assoc($query);
    }
    
    //echo json_encode($data);     
    $myObj->name = "John";
    $myObj->age = 30;
    $myObj->city = "New York";

    $myJSON = json_encode($myObj);

    echo "HELLOOOOOOOOOO";
    echo $myJSON;
     
    mysql_close($server);
?>