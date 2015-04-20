package anh.android.seniorapp.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.example.appkara.R;
import anh.android.seniorapp.adapter.SongAdapter;
import anh.android.seniorapp.control.SQLiteHandler;
import anh.android.seniorapp.model.Song;
import anh.android.seniorapp.system.AppConfig;
import anh.android.seniorapp.system.AppController;

@SuppressLint("NewApi")
public class FavoriteFragment extends Fragment implements OnItemClickListener {

	private static final String TAG = FavoriteFragment.class.getSimpleName();

	private ListView lvSong;
	private ArrayList<Song> arrSong;
	private SongAdapter adapterSong;

	private ProgressDialog pDialog;
	private SQLiteHandler db;

	private PopupMenu pp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_favorite, container,
				false);

		lvSong = (ListView) view.findViewById(R.id.listFavorite);
		arrSong = new ArrayList<Song>();

		db = new SQLiteHandler(getActivity());
		HashMap<String, String> user = db.getUserDetails();
		String idUser = user.get("uid");

		Log.d("TAG", idUser);

		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Loading...");
		pDialog.setCancelable(false);

		arrSong.clear();
		getAllSongREQ(idUser);

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

	/* This function user to get all song from a user has id */
	private void getAllSongREQ(final String idUser) {

		showProgressDialog();

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_VIEW_FAVORITE_LIST,
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

									song.setId(c.getInt("id_song"));
									song.setName(c.getString("name").toUpperCase());
									song.setLyric(c.getString("lyric"));
									song.setSinger(c.getString("name_singer"));
									song.setChose(1);

									arrSong.add(song);
								}

								adapterSong = new SongAdapter(getActivity(),
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
				params.put(AppConfig.TAG_PARSING_TAG, "view_favorite_list");
				params.put("id_user", idUser);
				return params;
			}
		};

		AppController.getInstance().addToRequestQueue(strReq,
				AppConfig.TAG_REQ_VIEW_ALL_FAVORITE_LIST);

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

	/* This function use to delete a song from favorite list */
	private void deleteASongFromFavoriteREQ(final String idUser,
			final String idSong) {
		showProgressDialog();

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_DELETE_ONE_FAVORITE,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d(TAG, "Response: " + response.toString());

						try {
							JSONObject jobj = new JSONObject(response);
							int res = jobj.getInt("success");

							if (res == 1) {

								arrSong.clear();
								String mess = jobj.getString("message");
								Toast.makeText(getActivity(), mess,
										Toast.LENGTH_SHORT).show();
								getAllSongREQ(idUser);

							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

						hideProgressDialog();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "ERROR PARSING DELETE");
						hideProgressDialog();
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put(AppConfig.TAG_PARSING_TAG,
						"delete_a_song_from_favorite_list");
				params.put("id_user", idUser);
				params.put("id_song", idSong);
				return params;
			}
		};

		AppController.getInstance().addToRequestQueue(strReq,
				AppConfig.TAG_REQ_DELETE_ONE_FROM_FAVORITE);
	}

	/* This function use to 'DELETE ALL SONG' from favorite list */
	private void deleteAllSongFavoriteREQ(final String idUser) {
		showProgressDialog();

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_DELETE_ALL_FAVORITE,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d(TAG, "Response: " + response.toString());

						try {
							JSONObject jobj = new JSONObject(response);
							int res = jobj.getInt("success");

							if (res == 1) {

								arrSong.clear();
								String mess = jobj.getString("message");
								Toast.makeText(getActivity(), mess,
										Toast.LENGTH_SHORT).show();
								// getAllSongREQ(idUser);

								/* viet them */
								adapterSong = new SongAdapter(getActivity(),
										arrSong);
								lvSong.setAdapter(adapterSong);
								adapterSong.notifyDataSetChanged();
								/* viet them */

							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

						hideProgressDialog();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "ERROR PARSING DELETE");
						hideProgressDialog();
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put(AppConfig.TAG_PARSING_TAG,
						"delete_all_song_from_favorite_list");
				params.put("id_user", idUser);
				return params;
			}
		};

		AppController.getInstance().addToRequestQueue(strReq,
				AppConfig.TAG_REQ_DELETE_ALL_SONG_FAVORITE);
	}

	private void popupMenu(Context context, View view, final String idUser,
			final String idSong) {

		pp = new PopupMenu(context, view, Gravity.END);
		pp.getMenuInflater().inflate(R.menu.popup_favorite, pp.getMenu());
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
				case R.id.btnUnFavorite:
					deleteASongFromFavoriteREQ(idUser, idSong);
					getAllSongREQ(idUser);
					break;
				case R.id.btnDeleteAll:
					deleteAllSongFavoriteREQ(idUser);
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
		String idSong = String.valueOf(arrSong.get(position).getId());

		popupMenu(getActivity(), view, idUser, idSong);

	}
}
