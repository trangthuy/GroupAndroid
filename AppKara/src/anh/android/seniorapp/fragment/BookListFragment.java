package anh.android.seniorapp.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;
import anh.android.seniorapp.adapter.SongAdapter;
import anh.android.seniorapp.control.SQLiteHandler;
import anh.android.seniorapp.model.Song;
import anh.android.seniorapp.system.AppConfig;
import anh.android.seniorapp.system.AppController;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.appkara.R;

@SuppressLint("DefaultLocale")
@TargetApi(Build.VERSION_CODES.KITKAT)
public class BookListFragment extends Fragment implements OnItemClickListener {

	private static final String TAG = BookListFragment.class.getSimpleName();

	private Handler mHandler = new Handler();
	private boolean isRunning = false;
	private Thread thread;

	private ListView lvSong;
	private ArrayList<Song> arrSong;
	private SongAdapter adapterSong;

	private SQLiteHandler db;

	private ProgressDialog pDialog;

	private PopupMenu pp;
	String[] param = new String[6];
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_bookinglist, container,
				false);

		lvSong = (ListView) view.findViewById(R.id.listBookingList);

		arrSong = new ArrayList<Song>();
		adapterSong = new SongAdapter(getActivity(), arrSong);
		lvSong.setAdapter(adapterSong);

		//pDialog = new ProgressDialog(getActivity());
		//pDialog.setMessage("Loading...");
		//pDialog.setCancelable(false);

		db = new SQLiteHandler(getActivity());

		arrSong.clear();
		getAllSongREQ();

		/* Da tien trinh */

		thread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (isRunning) {
					fetchData();
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		
		});
		
		isRunning = true;
		thread.start();

		/* Da tien trinh */

		lvSong.setOnItemClickListener(this);
		
		return view;
	}

	@Override
	 public void onStop() {
	  super.onStop();
	  if(pDialog!=null){
	   pDialog.dismiss();
	   pDialog = null;
	  }
	  isRunning = false;
	 }

//	private void showProgressDialog() {
//		if (!pDialog.isShowing())
//			pDialog.show();
//	}
//
//	private void hideProgressDialog() {
//		if (pDialog.isShowing())
//			pDialog.hide();
//	}

	private void getAllSongREQ() {

		//showProgressDialog();

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_VIEW_BOOKED_LIST,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d(TAG, "Response: " + response.toString());

						try {
							JSONObject jobj = new JSONObject(response);
							int res = jobj.getInt("success");

							if (res == 1) {

								arrSong.clear();
								JSONArray arr = jobj.getJSONArray("songs");
								for (int i = 0; i < arr.length(); i++) {
									JSONObject c = arr.getJSONObject(i);
									Song song = new Song();

									String nameUser = c.getString("username")
											.toString();

									song.setId(c.getInt("id_song"));
									song.setName(c.getString("name").toUpperCase());
									song.setLyric(c.getString("lyric"));
									song.setSinger(nameUser);
									song.setChose(4);
									song.setIdUserBooking(c.getInt("id_user"));
							        song.setIdOrder(c.getInt("id_order"));
									
									arrSong.add(song);
									if (db.getUserDetails().get("username")
											.toString()
											.equalsIgnoreCase(nameUser)) {
										song.setChose(3);
									}
								}
								adapterSong.notifyDataSetChanged();
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

					//	hideProgressDialog();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "ERROR PARSING SONGS");
					//	hideProgressDialog();
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put(AppConfig.TAG_PARSING_TAG, "view_booked_list");
				return params;
			}
		};

		AppController.getInstance().addToRequestQueue(strReq,
				AppConfig.TAG_REQ_VIEW_ALL_BOOKING_LIST);

	}
	private void popupMenu(Context context, View view, final String idUser,
			final String idSong, final String[] param) {

		pp = new PopupMenu(context, view, Gravity.END);
		pp.getMenuInflater().inflate(R.menu.popup_booking, pp.getMenu());
		Object menuHelper;
		try {
			Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
			fMenuHelper.setAccessible(true);
			menuHelper = fMenuHelper.get(pp);
			menuHelper
					.getClass()
					.getDeclaredMethod("setForceShowIcon",
							new Class[] { boolean.class })
					.invoke(menuHelper, true);
		} catch (Exception e) {

			Log.w("Icon", e);
			pp.show();
			return;
		}

		pp.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				
				case R.id.btnBooking:
					deleteSongBookREQ(idUser, idSong);
					break;
				
				case R.id.btnUpdateSong:
					delaySong(param);
					break;
					
				}
				return true;
			}

		});

		pp.show();
	}

	private void popupMenu(Context context, View view, final String idUser,
			final String idSong) {

		pp = new PopupMenu(context, view, Gravity.END);
		pp.getMenuInflater().inflate(R.menu.popup_booking_end, pp.getMenu());
		Object menuHelper;
		try {
			Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
			fMenuHelper.setAccessible(true);
			menuHelper = fMenuHelper.get(pp);
			menuHelper
					.getClass()
					.getDeclaredMethod("setForceShowIcon",
							new Class[] { boolean.class })
					.invoke(menuHelper, true);
		} catch (Exception e) {

			Log.w("Icon", e);
			pp.show();
			return;
		}

		pp.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case R.id.btnBooking:
					deleteSongBookREQ(idUser, idSong);
					break;
				
				
			}
				return true;
			}
		});

		pp.show();
	}

	private void deleteSongBookREQ(final String idUser, final String idSong) {
	//	showProgressDialog();

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_DELETE_SONG_BOOKED,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d(TAG, "Response: " + response.toString());

						try {
							JSONObject jobj = new JSONObject(response);
							int res = jobj.getInt("success");//

							if (res == 1) {

								String mess = jobj.getString("message");
								Toast.makeText(getActivity(), mess,
										Toast.LENGTH_SHORT).show();
								getAllSongREQ();

							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

					//	hideProgressDialog();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "ERROR PARSING DELETE");
						//hideProgressDialog();
						
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put(AppConfig.TAG_PARSING_TAG,
						"delete_a_song_from_booked_list");
				params.put("id_user", idUser);
				params.put("id_song", idSong);
				return params;
			}
		};

		AppController.getInstance().addToRequestQueue(strReq,
				AppConfig.TAG_REQ_DELETE_BOOKED_LIST);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		db = new SQLiteHandler(getActivity());
		HashMap<String, String> user = db.getUserDetails();

		final String idUser = user.get("uid");
		String userDB = user.get("username");

		final String idSong = String.valueOf(arrSong.get(position).getId());
		String userMySQL = arrSong.get(position).getSinger().toString();

		if (userDB.toString().equals(userMySQL)) {
			Log.e(TAG, userDB + " " + userMySQL);
			
			//popupMenu(getActivity(), view, idUser, idSong);
			
			arrSong.get(position);
			
			if(arrSong.size()-1>= position+1){
				
				param[0] = arrSong.get(position+1).getIdUserBooking()+"";
				param[1] = arrSong.get(position+1).getId()+"";
				param[2] = arrSong.get(position).getIdOrder()+"";
				
				param[3] = idUser;
				param[4] = idSong;
				param[5] = arrSong.get(position+1).getIdOrder()+"";
				
				popupMenu(getActivity(), view, idUser, idSong, param);
				
			}else{
				popupMenu(getActivity(), view, idUser, idSong);
			}
			
		}

	}
	private void delaySong(final String[] param) {

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_UPDATE_SONG, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d(TAG, "Delay Response: " + response.toString());

						try {
							JSONObject jObj = new JSONObject(response);

							int res = jObj.getInt("success");
							if (res == 1) {

//								db.updateUser(username, pass, newPass);
//								tvPass.setText(db.getUserDetails()
//										.get("password").toString());

								String message = jObj.getString("message");
								Toast.makeText(getActivity(), message,
										Toast.LENGTH_SHORT).show();

							}

							else {
								Toast.makeText(getActivity(),
										"Error when delay song!",
										Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "Login Error: " + error.getMessage());
						Toast.makeText(getActivity(), error.getMessage(),
								Toast.LENGTH_LONG).show();
					}
				}) {

			@Override
			protected Map<String, String> getParams() {

				Map<String, String> params = new HashMap<String, String>();
				
				params.put(AppConfig.TAG_PARSING_TAG, "update_song");
				
				params.put("idUserPreOne", param[0]);
				params.put("idSongPreOne", param[1]);
				params.put("idOrderOne", param[2]);
				
				params.put("idUserPreTwo", param[3]);
				params.put("idSongPreTwo", param[4]);
				params.put("idOrderTwo", param[5]);

				return params;
			}

		};

		AppController.getInstance().addToRequestQueue(strReq,
				"req_delay_song");
	}
	private void fetchData() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				getAllSongREQ();
			}
		});
	}
}
