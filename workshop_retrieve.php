<?php

$con = mysql_connect('HOSTNAME', 'USER', 'PASSWORD');
mysql_select_db('DATABASE_NAME', $con);
 
$r = mysql_query('SELECT * FROM table_android WHERE 1');
 
while($row = mysql_fetch_array($r))
{
	$out[] = $row;
}
 
print(json_encode($out));
mysql_close($con);

?>