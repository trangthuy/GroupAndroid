<?php include "mysqli_connect.php";?>
<?php include "mysqli_functions.php";?>

<?php

	if (isset($_POST['tag']) && $_POST['tag'] != '') {
	
		$tag = $_POST['tag'];
	
		$response = array("tag" => $tag, "success" => 0, "error" => 0);
	
		if ($tag == 'update_song') {
			
			$idUserPreOne 		= $_POST['idUserPreOne'];
			$idSongPreOne 		= $_POST['idSongPreOne'];
			$idOrderOne 		= $_POST['idOrderOne'];
			
			$idUserPreTwo 		= $_POST['idUserPreTwo'];
			$idSongPreTwo 		= $_POST['idSongPreTwo'];
			$idOrderTwo 		= $_POST['idOrderTwo'];
	
			if(updateSongByIdAndUser($conn, $idUserPreOne, $idSongPreOne, $idOrderOne,
					$idUserPreTwo, $idSongPreTwo, $idOrderTwo)){
					
				$response["success"] = 1;
				$response["message"] = "Delay successful!";
					
				echo json_encode($response);
					
			}
			else{
				
				$response["error"] = 1;
				$response["error_msg"] = "Error occured when delay";
				
				echo json_encode($response);
			}
			
		} else {
			echo "Invalid Request";
		}
	} else {
		echo "Access Denied";
	}

?>
