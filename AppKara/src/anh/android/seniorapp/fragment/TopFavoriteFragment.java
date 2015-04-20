package anh.android.seniorapp.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
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
import com.example.appkara.R;
import anh.android.seniorapp.adapter.SongFavoriteAdapter;
import anh.android.seniorapp.control.SQLiteHandler;
import anh.android.seniorapp.model.SongFavorite;
import anh.android.seniorapp.system.AppConfig;
import anh.android.seniorapp.system.AppController;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

@TargetApi(Build.VERSION_CODES.KITKAT) public class TopFavoriteFragment extends Fragment implements OnItemClickListener{

private static final String TAG = FavoriteFragment.class.getSimpleName();
	
	private ListView lvSong;
	private ArrayList<SongFavorite> arrSong;
	private SongFavoriteAdapter adapterSong;
	
	private ProgressDialog pDialog;
	private SQLiteHandler db;
	
	private PopupMenu pp;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_hot_favorite, container, false);
		
		lvSong = (ListView) view.findViewById(R.id.lvHotFavorite);
		arrSong = new ArrayList<SongFavorite>();
		
		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Loading...");
		pDialog.setCancelable(false);
		
		arrSong.clear();
		getAllSongTopFavoriteREQ();
		
		lvSong.setOnItemClickListener(this);
		
		return view;
	}
	
	@Override
	public void onStop() {
		super.onStop();
		if (pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		}
	}
	
	private void showProgressDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hideProgressDialog() {
		if (pDialog.isShowing())
			pDialog.hide();
	}
	
	private void getAllSongTopFavoriteREQ() {

		showProgressDialog();

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_ALL_FAVORITE_TOP, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						
						Log.d(TAG, "Response: " + response.toString());

						try {
							JSONObject jobj = new JSONObject(response);
							int res = jobj.getInt("success");

							if (res == 1) {
								arrSong.clear();
								JSONArray arr = jobj.getJSONArray("rating");
								
								for (int i = 0; i < arr.length(); i++) {
									JSONObject c = arr.getJSONObject(i);
									SongFavorite song = new SongFavorite();

									song.setSongNumIC(c.getInt("stt"));
									song.setSongId(c.getInt("id_song"));
									song.setSongName(c.getString("name").toUpperCase());
									song.setSongSinger(c.getString("name_singer"));
									song.setSongLyric(c.getString("lyric"));
									song.setSongRating(c.getInt("count"));
									
									arrSong.add(song);
								}
								
								adapterSong = new SongFavoriteAdapter(getActivity(),
										arrSong);
								lvSong.setAdapter(adapterSong);
							}
							
						} catch (JSONException e) {
							e.printStackTrace();
						}

						hideProgressDialog();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "ERROR PARSING SONGS");
						hideProgressDialog();
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put(AppConfig.TAG_PARSING_TAG, "best_favorite_song_list");
				return params;
			}
		};

		AppController.getInstance().addToRequestQueue(strReq,
				AppConfig.TAG_REQ_GET_ALL_TOP_LIST);

	}

	private void putFavoriteSong(final String idUser, final String idSong) {

		showProgressDialog();

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_PUT_FAVORITE_LIST,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d(TAG, "Response: " + response.toString());

						try {
							JSONObject jobj = new JSONObject(response);
							
							int res = jobj.getInt("success");
							int err = jobj.getInt("error");

							if (err == 2) {
								String mess = jobj.getString("error_msg");
								Toast.makeText(getActivity(), mess,
										Toast.LENGTH_SHORT).show();
							} else if (res == 1) {
								String mess = jobj.getString("message");
								Toast.makeText(getActivity(), mess,
										Toast.LENGTH_SHORT).show();
							} else {
								String mess = jobj.getString("error_msg");
								Toast.makeText(getActivity(), mess,
										Toast.LENGTH_SHORT).show();
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

						hideProgressDialog();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "ERROR PARSING SONGS TO FAVORITE");
						hideProgressDialog();
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put(AppConfig.TAG_PARSING_TAG, "favorite");
				params.put("id_user", idUser);
				params.put("id_song", idSong);
				return params;
			}
		};

		AppController.getInstance().addToRequestQueue(strReq,
				AppConfig.TAG_REQ_STORE_SONG_FAVORITE);
	}

	private void putBookingSong(final String idUser, final String idSong) {

		showProgressDialog();

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_PUT_BOOKING_LIST,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d(TAG, "Response: " + response.toString());

						try {
							JSONObject jobj = new JSONObject(response);
							
							int res = jobj.getInt("success");
							int err = jobj.getInt("error");

							if (err == 2) {
								String mess = jobj.getString("error_msg");
								Toast.makeText(getActivity(), mess,
										Toast.LENGTH_SHORT).show();
							} else if (res == 1) {
								String mess = jobj.getString("message");
								Toast.makeText(getActivity(), mess,
										Toast.LENGTH_SHORT).show();
							} else {
								String mess = jobj.getString("error_msg");
								Toast.makeText(getActivity(), mess,
										Toast.LENGTH_SHORT).show();
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

						hideProgressDialog();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "ERROR PARSING SONGS TO BOOKING");
						hideProgressDialog();
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put(AppConfig.TAG_PARSING_TAG, "booking");
				params.put("id_user", idUser);
				params.put("id_song", idSong);
				return params;
			}
		};

		AppController.getInstance().addToRequestQueue(strReq,
				AppConfig.TAG_REQ_STORE_SONG_BOOKING);
	}
	
	private void popupMenu(Context context, View view, final String idUser,
			final String idSong) {

		pp = new PopupMenu(context, view, Gravity.END);
		pp.getMenuInflater().inflate(R.menu.pop_top_favorite, pp.getMenu());
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
					putBookingSong(idUser, idSong);
					break;
				case R.id.btnFavorite:
					putFavoriteSong(idUser, idSong);
					break;
				}
				return true;
			}

		});

		pp.show();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		db = new SQLiteHandler(getActivity());
		HashMap<String, String> user = db.getUserDetails();
		String idUser = user.get("uid");
		String idSong = String.valueOf(arrSong.get(position).getSongId());
		popupMenu(getActivity(), view, idUser, idSong);
		
	}
	
	
	
}
