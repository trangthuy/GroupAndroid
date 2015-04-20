<?php include "mysqli_connect.php";?>

<?php
if (isset($_POST['tag']) && $_POST['tag'] != '') {

	$tag = $_POST['tag'];

	$response = array("tag" => $tag, "success" => 0);

	if ($tag == 'get_all_song_num') {

		/* Thay Doi */
		$sql = "SELECT id, title, lyric, source FROM songnum WHERE lang = 'vn' ORDER BY title";
		$result = mysqli_query($conn, $sql) or die (mysqli_error());
		
		if(mysqli_num_rows($result)>0){
			$response["songs"] = array();
			while($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){
				$song = array();
				
				$song ["song_id"] = $row ["id"];
 				$song ["song_name"] = $row ["title"];
 				$song ["song_lyric"] = $row ["lyric"];
				$song ["song_author"] = $row ["source"];

				array_push($response["songs"], $song);
			}
			$response["success"] = 1;
			echo json_encode($response);
		}else{
			$response["error_msg"] = "No song found";
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
