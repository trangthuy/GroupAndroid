<?php include "mysqli_connect.php";?>

<?php
if (isset($_POST['tag']) && $_POST['tag'] != '') {

	$tag = $_POST['tag'];

	$response = array("tag" => $tag, "success" => 0);

	if ($tag == 'get_all_singer') {

		/* Thay Doi */
		$sql = "SELECT id_singer, name_singer FROM singer";
		$result = mysqli_query($conn, $sql) or die (mysqli_error());
		
		if(mysqli_num_rows($result)>0){
			$response["singers"] = array();
			while($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){
				$song = array();
				
				$song ["id_singer"] = $row ["id_singer"];
 				$song ["name_singer"] = $row ["name_singer"];

				array_push($response["singers"], $song);
			}
			$response["success"] = 1;
			echo json_encode($response);
		}else{
			$response["error_msg"] = "No singer found";
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
