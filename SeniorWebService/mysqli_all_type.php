<?php include "mysqli_connect.php";?>

<?php
if (isset($_POST['tag']) && $_POST['tag'] != '') {

	$tag = $_POST['tag'];

	$response = array("tag" => $tag, "success" => 0);

	if ($tag == 'get_all_type') {

		/* Thay Doi */
		$sql = "SELECT id_type, name_type FROM type_song";
		$result = mysqli_query($conn, $sql) or die (mysqli_error());
		
		if(mysqli_num_rows($result)>0){
			$response["type"] = array();
			while($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){
				$song = array();
				
				$song ["id_type"] = $row ["id_type"];
 				$song ["name_type"] = $row ["name_type"];

				array_push($response["type"], $song);
			}
			$response["success"] = 1;
			echo json_encode($response);
		}else{
			$response["error_msg"] = "No type found";
			echo json_encode($response);
		}
		
		/* Thay Doi */
	} else {
		echo "Invalid Request";
	}
} else {
	echo "Access Denied";
}
?>
