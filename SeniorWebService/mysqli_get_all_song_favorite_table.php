<?php include "mysqli_connect.php";?>

<?php
if (isset($_POST['tag']) && $_POST['tag'] != '') {

	$tag = $_POST['tag'];

	$response = array("tag" => $tag, "success" => 0);

	if ($tag == 'best_favorite_song_list') {

		/* Thay Doi */
		$sql = "SELECT song.id_song, name, name_singer, lyric, COUNT(favorite.id_song) 
			FROM song
			INNER JOIN favorite ON song.id_song = favorite.id_song
			INNER JOIN singer ON song.id_singer = singer.id_singer
			GROUP BY favorite.id_song
			ORDER BY COUNT(favorite.id_song) DESC
			LIMIT 5";
		$result = mysqli_query($conn, $sql) or die (mysqli_error());
		
		if($result){
			$response["rating"] = array();
			$i = 1;
			while($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){
				
				$song = array();
				$song["stt"] = $i;
				$song["id_song"] = $row["id_song"];
          		$song["name"] = $row["name"];
	 			$song["name_singer"] = $row["name_singer"];
          		$song["lyric"] = $row["lyric"];
	  			$song["count"] = $row["COUNT(favorite.id_song)"];
				$i++;
				array_push($response["rating"], $song);
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
