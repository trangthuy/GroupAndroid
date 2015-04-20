<?php include "mysqli_connect.php";?>
<?php

if (isset($_POST['tag']) && $_POST['tag'] != '') {

	$tag = $_POST['tag'];
	
	$response = array("tag" => $tag, "success" => 0);

	if ($tag == 'get_all_song_of_type') {

		/* Thay Doi */
		$id_type = $_POST['id_type'];
		$username = $_POST['username'];
		
		$sql = "SELECT song.id_song, name, singer.name_singer, song.lyric, users.username, type_song.id_type
			FROM singer 
			INNER JOIN song ON singer.id_singer = song.id_singer 
			INNER JOIN type_song ON type_song.id_type = song.id_type AND type_song.id_type = '$id_type'
			LEFT JOIN (users INNER JOIN favorite ON users.id_user = favorite.id_user AND users.username = '$username') 
			ON song.id_song = favorite.id_song
			WHERE song.id_song <> 1
			GROUP BY song.name
			ORDER BY song.name";
		
		$result = mysqli_query($conn, $sql) or die (mysqli_error());
		
		if(mysqli_num_rows($result)>0){
			$response["songs"] = array();
			while($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){
				$song = array();
				
				$song ["id_song"] = $row ["id_song"];
				$song ["id_type"] = $row ["id_type"];
 				$song ["name"] = $row ["name"];
				$song ["lyric"] = $row ["lyric"];
				$song ["name_singer"] = $row ["name_singer"];
				$song ["username"] = $row ["username"];

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
