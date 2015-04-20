<?php include "mysqli_connect.php";?>

<?php
if (isset ( $_POST ['tag'] ) && $_POST ['tag'] != '') {
	
	$tag = $_POST ['tag'];
	
	$response = array (
			"tag" => $tag,
			"success" => 0 
	);
	
	if ($tag == 'delete_all_song_from_favorite_list') {
		
		/* Thay Doi */
		$idUser = $_POST['id_user'];
		
		$sql = "delete from favorite where id_user = '$idUser'";
		
		$result = mysqli_query ( $conn, $sql ) or die ( mysqli_error () );

		if(mysqli_affected_rows($conn)>0){
			$response ["success"] = 1;
			$response ["message"] = "Đã xoá!";
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
