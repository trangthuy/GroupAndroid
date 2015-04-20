<?php include "mysqli_connect.php";?>

<?php
if (isset ( $_POST['tag'] ) && $_POST['tag'] != '') {
	
	$tag = $_POST['tag'];
	
	$response = array (
			"tag" => $tag,
			"success" => 0 
	);
	
	if ($tag == 'view_booked_list') {
		
		/* Thay Doi */
		$sql = "SELECT bookinglist.id_order, bookinglist.id_user, song.id_song, song.name, users.username, song.lyric 
			FROM bookinglist INNER JOIN song 
			ON bookinglist.id_song = song.id_song 
			INNER JOIN users 
			ON users.id_user = bookinglist.id_user
			WHERE id_order<>1 
			ORDER BY bookinglist.id_order";
		$result = mysqli_query ( $conn, $sql ) or die ( mysqli_error () );
		$i=1;
		if ($result) {
			$response ["songs"] = array ();
			while ( $row = mysqli_fetch_array ( $result, MYSQLI_ASSOC ) ) {
				$song = array ();
				
				$song ["id_song"] = $row ["id_song"];
				$song ["name"] = $i.". ".$row ["name"];
				$song ["username"] = $row ["username"];
				$song ["lyric"] = $row ["lyric"];
				$song ["id_user"] = $row ["id_user"];
				$song ["id_order"] = $row ["id_order"];
				$i++;
				array_push ( $response ["songs"], $song );
			}
			$response ["success"] = 1;
			echo json_encode ( $response);
		} else {
			$response ["error_msg"] = "No song found";
			echo json_encode ( $response);
		}
		
		/* Thay Doi */
	} else {
		echo "Invalid Request";
	}
} else {
	echo "Access Denied";
}
?>
