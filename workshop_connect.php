<?php

$con = mysql_connect('HOSTNAME', 'USER', 'PASSWORD');
mysql_select_db('DATABASE_NAME', $con);

$name=$_POST['name'];
$email=$_POST['email'];
$phone=$_POST['phone'];

mysql_query("INSERT INTO table_android (name, email, phone) VALUES ('{$name}', '{$email}', '{$phone}')");

mysql_close();

?>