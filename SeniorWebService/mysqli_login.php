<?php include "mysqli_connect.php";?>
<?php include "mysqli_functions.php";?>

<?php
if (isset($_POST['tag']) && $_POST['tag'] != '') {
	
	$tag = $_POST['tag'];

	$response = array("tag" => $tag, "success" => 0, "error" => 0);

	if ($tag == 'login') {
		
		$username = $_POST['username'];
		$pass = $_POST['pass'];

		$user = getUserByUsernameAndPassword($conn, $username, $pass);
		
		if ($user != false) {
			
			$response["success"] = 1;
			$response["user"]["uid"] = $user["id_user"];
			$response["user"]["username"] = $user["username"];
			
			echo json_encode($response);
			
		} else {
			
			$response["error"] = 1;
			$response["error_msg"] = "Bạn đã nhập sai thông tin!";
			
			echo json_encode($response);
			
		}
	} else {
		echo "Invalid Request";
	}
} else {
	echo "Access Denied";
}
?>