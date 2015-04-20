<?php include "mysqli_connect.php";?>
<?php include "mysqli_functions.php";?>

<?php
if (isset($_POST['tag']) && $_POST['tag'] != '') {
	
	$tag = $_POST['tag'];

	$response = array("tag" => $tag, "success" => 0, "error" => 0);

	if ($tag == 'register') {
		
		$username = $_POST['username'];
		$pass = $_POST['pass'];

		if (isUserExisted($conn, $username)) {
			
			$response["error"] = 2;
			$response["error_msg"] = "Tên đăng nhập này đã tồn tại";
			
			echo json_encode($response);
		} else {
			
			$user = storeUser($conn, $username, $pass);
			if ($user) {
				
				$response["success"] = 1;
				$response["user"]["uid"] = $user["id_user"];
				$response["user"]["username"] = $user["username"];
				
				echo json_encode($response);
			} else {
				
				$response["error"] = 1;
				$response["error_msg"] = "Error occured in Registartion";
				
				echo json_encode($response);
			}
		}
	} else {
		echo "Invalid Request";
	}
} else {
	echo "Access Denied";
}
?>