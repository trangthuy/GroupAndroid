package anh.android.seniorapp.system;

public class AppConfig {
	
	/* Genymotion vitual machine */
//	public static final String URL_LOGIN = "http://10.0.3.2/SeniorWebService/mysqli_login.php";
//	public static final String URL_REGISTER = "http://10.0.3.2/SeniorWebService/mysqli_register.php";
//	public static final String URL_ALL_SONG = "http://10.0.3.2/SeniorWebService/mysqli_all_song.php";
//	public static final String URL_ALL_SINGER = "http://10.0.3.2/SeniorWebService/mysqli_all_singer.php";
//	public static final String URL_ALL_TYPE = "http://10.0.3.2/SeniorWebService/mysqli_all_type.php";
//	public static final String URL_ALL_SONG_OF_SINGER = "http://10.0.3.2/SeniorWebService/mysqli_get_all_song_of_singer.php";
//	public static final String URL_ALL_SONG_OF_TYPE = "http://10.0.3.2/SeniorWebService/mysqli_get_all_song_of_type.php";
//	public static final String URL_PUT_BOOKING_LIST = "http://10.0.3.2/SeniorWebService/mysqli_put_booking_song.php";
//	public static final String URL_PUT_FAVORITE_LIST = "http://10.0.3.2/SeniorWebService/mysqli_put_favorite_song.php";
//	public static final String URL_VIEW_BOOKED_LIST = "http://10.0.3.2/SeniorWebService/mysqli_view_booked_list.php";
//	public static final String URL_VIEW_FAVORITE_LIST = "http://10.0.3.2/SeniorWebService/mysqli_view_favarite_list.php";
	
	/* Emulator machine */
	public static final String URL_LOGIN 				= "http://10.0.2.2/SeniorWebService/mysqli_login.php";
	public static final String URL_REGISTER 			= "http://10.0.2.2/SeniorWebService/mysqli_register.php";
	public static final String URL_ALL_SONG 			= "http://10.0.2.2/SeniorWebService/mysqli_all_song.php";
	public static final String URL_ALL_SONG_NUM 		= "http://10.0.2.2/SeniorWebService/mysqli_all_song_num.php";
	public static final String URL_ALL_SONG_EN 		    = "http://10.0.2.2/SeniorWebService/mysqli_all_song_en.php";
	public static final String URL_ALL_SINGER 			= "http://10.0.2.2/SeniorWebService/mysqli_all_singer.php";
	public static final String URL_ALL_TYPE 			= "http://10.0.2.2/SeniorWebService/mysqli_all_type.php";
	public static final String URL_ALL_SONG_OF_SINGER 	= "http://10.0.2.2/SeniorWebService/mysqli_get_all_song_of_singer.php";
	public static final String URL_ALL_SONG_OF_TYPE 	= "http://10.0.2.2/SeniorWebService/mysqli_get_all_song_of_type.php";
	public static final String URL_ALL_FAVORITE_TOP  	= "http://10.0.2.2/SeniorWebService/mysqli_get_all_song_favorite_table.php";
	public static final String URL_PUT_BOOKING_LIST 	= "http://10.0.2.2/SeniorWebService/mysqli_put_booking_song.php";
	public static final String URL_PUT_FAVORITE_LIST 	= "http://10.0.2.2/SeniorWebService/mysqli_put_favorite_song.php";
	public static final String URL_VIEW_BOOKED_LIST 	= "http://10.0.2.2/SeniorWebService/mysqli_view_booked_list.php";
	public static final String URL_VIEW_FAVORITE_LIST 	= "http://10.0.2.2/SeniorWebService/mysqli_view_favarite_list.php";
	public static final String URL_DELETE_SONG_BOOKED 	= "http://10.0.2.2/SeniorWebService/mysqli_delete_a_booked_song.php";
	public static final String URL_DELETE_ONE_FAVORITE  = "http://10.0.2.2/SeniorWebService/mysqli_delete_a_song_from_favorite_table.php";
	public static final String URL_DELETE_ALL_FAVORITE  = "http://10.0.2.2/SeniorWebService/mysqli_delete_all_song_from_favorite_list.php";
	public static final String URL_UPDATE_PASS  		= "http://10.0.2.2/SeniorWebService/mysqli_update_password.php";
	public static final String URL_UPDATE_SONG  		= "http://10.0.2.2/SeniorWebService/mysqli_update_song.php";
	
	/* Real Host */
	
	public static final String TAG_PARSING_TAG 			= "tag";
	public static final String TAG_PARSING_LOGIN 		= "login";
	public static final String TAG_PARSING_REGISTER		= "register";
	public static final String TAG_PARSING_SUCCESS 		= "success";
	public static final String TAG_PARSING_ERROR 		= "error_msg";
	public static final String TAG_PARSING_USERNAME 	= "username";
	public static final String TAG_PARSING_PASSWORD 	= "pass";
	public static final String TAG_PARSING_UID 			= "uid";
	public static final String TAG_PARSING_USER 		= "user";
	
	/* TAG close request*/
	public static final String TAG_REQ_LOGIN 					= "req_login";
	public static final String TAG_REQ_REGISTER 				= "req_register";
	public static final String TAG_REQ_GET_ALL_SONG 			= "req_get_all_song";
	public static final String TAG_REQ_GET_ALL_SONG_NUM 		= "req_get_all_song_num";
	public static final String TAG_REQ_GET_ALL_SONG_EN 			= "req_get_all_song_en";
	public static final String TAG_REQ_GET_ALL_SONG_OF_SINGER 	= "req_get_all_song_of_singer";
	public static final String TAG_REQ_GET_ALL_TOP_LIST			= "req_get_all_song_top_list";
	public static final String TAG_REQ_GET_ALL_SINGER 			= "req_get_all_singer";
	public static final String TAG_REQ_GET_ALL_TYPE 			= "req_get_all_type";
	public static final String TAG_REQ_STORE_SONG_BOOKING 		= "req_put_store_booking";
	public static final String TAG_REQ_STORE_SONG_FAVORITE 		= "req_put_store_favorite";
	public static final String TAG_REQ_VIEW_ALL_BOOKING_LIST 	= "req_put_view_booking_list";
	public static final String TAG_REQ_VIEW_ALL_FAVORITE_LIST 	= "req_put_view_favorite_list";
	public static final String TAG_REQ_DELETE_BOOKED_LIST 		= "req_delete_song_booked_list";
	public static final String TAG_REQ_DELETE_ONE_FROM_FAVORITE = "req_delete_a_song_favorite_list";
	public static final String TAG_REQ_DELETE_ALL_SONG_FAVORITE = "req_delete_all_song_favorite_list";
	public static final String TAG_REQ_UPDATE_PASS 				= "req_update_pass";
	
	
	
}
