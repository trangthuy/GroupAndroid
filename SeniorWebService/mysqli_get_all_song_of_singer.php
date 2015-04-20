<?php include "mysqli_connect.php";?>
<?php

if (isset($_POST['tag']) && $_POST['tag'] != '') {

	$tag = $_POST['tag'];
	
	$response = array("tag" => $tag, "success" => 0);

	if ($tag == 'get_all_song_of_singer') {

		/* Thay Doi */
		$id_singer = $_POST['id_singer'];
		$username = $_POST['username'];
		
		//$sql = "SELECT id_song, name, id_singer, id_type, lyric FROM song WHERE id_singer = $id_type";
		$sql = "SELECT song.id_song, name, singer.name_singer, singer.id_singer, song.lyric, favorite.id_user, users.username
			FROM singer
			INNER JOIN song ON singer.id_singer = song.id_singer AND singer.id_singer =  '$id_singer'
			LEFT JOIN (users
			INNER JOIN favorite ON users.id_user = favorite.id_user AND users.username =  '$username')
			ON song.id_song = favorite.id_song
			WHERE song.id_song <>1
			ORDER BY song.name";
		$result = mysqli_query($conn, $sql) or die (mysqli_error());
		
		if(mysqli_num_rows($result)>0){
			$response["songs"] = array();
			while($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){
				$song = array();
				
				$song ["id_song"] = $row ["id_song"];
				$song ["id_singer"] = $row ["id_singer"];
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
