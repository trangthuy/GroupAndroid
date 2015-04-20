<?php include "mysqli_connect.php";?>

<?php
if (isset ( $_POST ['tag'] ) && $_POST ['tag'] != '') {
	
	$tag = $_POST ['tag'];
	
	$response = array (
			"tag" => $tag,
			"success" => 0 
	);
	
	if ($tag == 'delete_a_song_from_booked_list') {
		
		/* Thay Doi */
		$idUser = $_POST['id_user'];
		$idSong = $_POST['id_song'];
		
		$sql = "delete from bookinglist where id_song = '$idSong' AND id_user = '$idUser'";
		
		$result = mysqli_query ( $conn, $sql ) or die ( mysqli_error () );

		if(mysqli_affected_rows($conn)==1){
			$response ["success"] = 1;
			$response ["message"] = "Bài hát đã được xoá!";
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
