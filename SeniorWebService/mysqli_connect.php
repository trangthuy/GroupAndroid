<?php
	header('Content-type: text/plain; charset=utf-8');

	$DB_HOST = "localhost";
	$DB_USER = "root";
	$DB_PASS = "root";
	$DB_DATABASE = "karaoke";
	
	$conn = mysqli_connect ( $DB_HOST, $DB_USER, $DB_PASS, $DB_DATABASE ) or die ( mysqli_error () );
	
	mysqli_set_charset($conn, 'utf8');
?>
