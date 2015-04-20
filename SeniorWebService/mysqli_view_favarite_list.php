<?php include "mysqli_connect.php";?>

<?php
if (isset ( $_POST ['tag'] ) && $_POST ['tag'] != '') {
	
	$tag = $_POST ['tag'];
	
	$response = array (
			"tag" => $tag,
			"success" => 0 
	);
	
	if ($tag == 'view_favorite_list') {
		
		$idUser = $_POST ['id_user'];
		/* Thay Doi */
		$sql = "SELECT singer.name_singer, song.id_song, song.name, song.lyric 
		FROM singer INNER JOIN song 
		ON singer.id_singer = song.id_singer 
 		INNER JOIN favorite 
		ON song.id_song = favorite.id_song 
		INNER JOIN users 
		ON favorite.id_user = users.id_user 
		AND favorite.id_user = $idUser ORDER BY  song.name";
		$result = mysqli_query ( $conn, $sql ) or die ( mysqli_error () );
		
		if ($result ) {
			$response ["songs"] = array ();
			while ( $row = mysqli_fetch_array ( $result, MYSQLI_ASSOC ) ) {
				$song = array ();
				
				$song ["id_song"] = $row ["id_song"];
				$song ["name_singer"] = $row ["name_singer"];
				$song ["name"] = $row ["name"];
				$song ["lyric"] = $row ["lyric"];
				
				array_push ( $response ["songs"], $song );
			}
			$response ["success"] = 1;
			echo json_encode ( $response);
		} else {
			$response ["error_msg"] = "No song found";
			echo json_encode ( $response);
		}
		
		/* Thay Doi */
	} else {
		echo "Invalid Request";
	}
} else {
	echo "Access Denied";
}
?>
