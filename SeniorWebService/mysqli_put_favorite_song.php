<?php include "mysqli_connect.php";?>
<?php include "mysqli_functions.php";?>

<?php


if (isset($_POST['tag']) && $_POST['tag'] != '') {

	$tag = $_POST['tag'];

	$response = array("tag" => $tag, "success" => 0, "error" => 0);

	if ($tag == 'favorite') {

		$idUser = $_POST['id_user'];
		$idSong = $_POST['id_song'];

		
		if(isCheckFavorite($conn, $idUser, $idSong)){
			
			$response["error"] = 2;
			$response["error_msg"] = "Đã tồn tại";
				
			echo json_encode($response);
		}else{
			
			if (storeSongFavorite($conn, $idUser, $idSong))  {
			
				$response["success"] = 1;
				$response["message"] = "Thành công";
					
				echo json_encode($response);
					
					
			}else{
					
				$response["error"] = 1;
				$response["error_msg"] = "Error occured in Registartion";
					
				echo json_encode($response);
			}
		}
		
	} else {
		echo "Invalid Request";
	}
} else {
	echo "Access Denied";
}

?>
