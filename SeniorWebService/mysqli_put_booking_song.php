<?php include "mysqli_connect.php";?>
<?php include "mysqli_functions.php";?>

<?php


if (isset($_POST['tag']) && $_POST['tag'] != '') {

	$tag = $_POST['tag'];

	$response = array("tag" => $tag, "success" => 0, "error" => 0);

	if ($tag == 'booking') {

		$idUser = $_POST['id_user'];
		$idSong = $_POST['id_song'];

		if(isCheckBookingList($conn, $idSong)){
			
			$response["error"] = 2;
			$response["error_msg"] = "Bài này đã được đăng ký!";
				
			echo json_encode($response);
		}
		else if(isCheckLimit($conn, $idUser)){
			$response["error"] = 3;
			$response["error_msg"] = "Bạn không được quyền đăng ký liên tiếp 3 bài!";
				
			echo json_encode($response);
		}
		else{
			
			if (storeSongBooking($conn, $idUser, $idSong))  {
			
				$response["success"] = 1;
				$response["message"] = "Đăng ký thành công!";
					
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
