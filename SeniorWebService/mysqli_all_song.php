<?php include "mysqli_connect.php";?>

<?php
if (isset($_POST['tag']) && $_POST['tag'] != '') {

	$tag = $_POST['tag'];

	$response = array("tag" => $tag, "success" => 0);

	if ($tag == 'get_all_song') {
		$idUser = $_POST ['id_user'];

		/* Thay Doi */
		/*$sql = "SELECT song.id_song, song.name, song.id_singer, song.id_type, song.lyric, singer.name_singer 
		FROM song INNER JOIN singer 
		ON singer.id_singer = song.id_singer  WHERE id_song<>1 ORDER BY song.name";*/
		$sql = "SELECT song.id_song, name, singer.name_singer,singer.id_singer, song.lyric, favorite.id_user, users.username, 				users.id_user
			FROM singer INNER JOIN song ON singer.id_singer = song.id_singer 
			LEFT JOIN (users INNER JOIN favorite ON users.id_user = favorite.id_user AND favorite.id_user = '$idUser' ) 
			ON song.id_song = favorite.id_song
			WHERE song.id_song <> 1
			ORDER BY song.name";
		$result = mysqli_query($conn, $sql) or die (mysqli_error());
		
		if(mysqli_num_rows($result)>0){
			$response["songs"] = array();
			while($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){
				$song = array();
				
				$song ["id_song"] = $row ["id_song"];
 				$song ["name"] = $row ["name"];
 				$song ["name_singer"] = $row ["name_singer"];
				$song ["id_singer"] = $row ["id_singer"];
				//$song ["id_type"] = $row ["id_type"];
				$song ["lyric"] = $row ["lyric"];
				$song ["username"] = $row ["username"];
				$song ["id_user"] = $row ["id_user"];

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
