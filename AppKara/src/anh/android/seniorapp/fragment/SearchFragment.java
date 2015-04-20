package anh.android.seniorapp.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.appkara.R;

import anh.android.seniorapp.adapter.SingerAdapter;
import anh.android.seniorapp.adapter.SongAdapter;
import anh.android.seniorapp.adapter.TypeAdapter;
import anh.android.seniorapp.control.SQLiteHandler;
import anh.android.seniorapp.model.Singer;
import anh.android.seniorapp.model.Song;
import anh.android.seniorapp.model.Type;
import anh.android.seniorapp.system.AppConfig;
import anh.android.seniorapp.system.AppController;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class SearchFragment extends Fragment implements OnClickListener {

	private static final String TAG = SearchFragment.class.getSimpleName();

	private Button btnSong, btnSinger, btnType;
	private EditText inputSearch;

	private ProgressDialog pDialog;
	private SQLiteHandler db;

	private ListView lvSong;
	private ListView lvType;
	private ListView lvSinger;

	private ArrayList<Song> arrSong;
	private ArrayList<Type> arrType;
	private ArrayList<Singer> arrSinger;

	private SongAdapter adapterSong;
	private SingerAdapter adapterSinger;
	private TypeAdapter adapterType;

	private PopupMenu pp, pp2;
	int status;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater
				.inflate(R.layout.fragment_search, container, false);
		
		db = new SQLiteHandler(getActivity());
		HashMap<String, String> user = db.getUserDetails();
		String idUser = user.get("uid");
		
		btnSong = (Button) view.findViewById(R.id.btnSong);
		btnSinger = (Button) view.findViewById(R.id.btnSinger);
		btnType = (Button) view.findViewById(R.id.btnType);

		inputSearch = (EditText) view.findViewById(R.id.inputSearch);

		arrSong = new ArrayList<Song>();
		arrType = new ArrayList<Type>();
		arrSinger = new ArrayList<Singer>();

		lvSong = (ListView) view.findViewById(R.id.listSong);
		lvType = (ListView) view.findViewById(R.id.listType);
		lvSinger = (ListView) view.findViewById(R.id.listSinger);

		lvType.setVisibility(View.GONE);
		lvSinger.setVisibility(View.GONE);

		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Loading...");
		pDialog.setCancelable(false);

		arrSong.clear();
		getAllSongREQ(idUser);
		
		int myColorselect =
			    getResources().getColor(R.color.text_button_selected);
		btnSong.setBackgroundResource(R.drawable.custom_bt_song_choosen);
		btnSong.setTextColor(myColorselect);
		btnSong.setOnClickListener(this);
		btnSinger.setOnClickListener(this);
		btnType.setOnClickListener(this);
		
		adapterSong = new SongAdapter(getActivity(),arrSong);
		lvSong.setAdapter(adapterSong);
		inputSearch.setOnTouchListener(new AdapterView.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				 v.setFocusable(true);
		            v.setFocusableInTouchMode(true);
				return false;
			}
	    });
		lvSinger.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				arrSong.clear();
				db = new SQLiteHandler(getActivity());
				HashMap<String, String> user = db.getUserDetails();
				String username = user.get("username");
				int idSinger = arrSinger.get(position).getIdSinger();
				String idSingerStr = String.valueOf(idSinger);
				setVSB(1, 0, 0);
				getAllSongOfSingerREQ(idSingerStr,username);

			}
		});

		lvType.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				arrSong.clear();
				db = new SQLiteHandler(getActivity());
				HashMap<String, String> user = db.getUserDetails();
				String username = user.get("username");
				int idType = arrType.get(position).getIdType();
				String idTypeStr = String.valueOf(idType);
				setVSB(1, 0, 0);
				getAllSongOfTypeREQ(idTypeStr, username);

			}
		});

		lvSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				db = new SQLiteHandler(getActivity());
				HashMap<String, String> user = db.getUserDetails();
				String idUser = user.get("uid");
				String nameUser = user.get("username");
				
				int idSinger = arrSong.get(position).getIdSinger();
				String idSingerStr = String.valueOf(idSinger);
				
				int idType = arrSong.get(position).getIdType();
				String idTypeStr = String.valueOf(idType);
				
				if(arrSong.get(position).getUsername().toString().equals(nameUser)){
					String idSong = String.valueOf(arrSong.get(position).getId());
					popupMenu2(getActivity(), view, idUser, idSong, idSingerStr, nameUser, idTypeStr);
				}
				else{
					String idSong = String.valueOf(arrSong.get(position).getId());
					popupMenu(getActivity(), view, idUser, idSong, idSingerStr, nameUser, idTypeStr);
				}

			}
		});

		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String text = inputSearch.getText().toString()
						.toLowerCase(Locale.getDefault());
				adapterSong.filter(text);
			}
		});
		

		return view;
	}

	@Override
	public void onStop() {
		super.onStop();
		if(pDialog!=null){
			pDialog.dismiss();
			pDialog = null;
		}
	}
	
	
	@Override
	public void onClick(View v) {
		int myColorselect =
			    v.getResources().getColor(R.color.text_button_selected);
		int myColor =
			    v.getResources().getColor(R.color.text_button);
		
		if (v == btnSong) {
			status = 0;
			arrSong.clear();
			inputSearch.setText("");
			btnSong.setBackgroundResource(R.drawable.custom_bt_song_choosen);
			btnSong.setTextColor(myColorselect);
			btnSinger.setBackgroundResource(R.drawable.custom_bt_song);
			btnSinger.setTextColor(myColor);
			btnType.setBackgroundResource(R.drawable.custom_bt_song);
			btnType.setTextColor(myColor);
			inputSearch.setFocusable(false);
			/* Viet them */

			inputSearch.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {

				}

				@Override
				public void afterTextChanged(Editable s) {
					String text = inputSearch.getText().toString()
							.toLowerCase(Locale.getDefault());
					adapterSong.filter(text);
				}
			});

			/* Viet them */

			setVSB(1, 0, 0);
			
			db = new SQLiteHandler(getActivity());
			HashMap<String, String> user = db.getUserDetails();
			String idUser = user.get("uid");
			getAllSongREQ(idUser);

		} else if (v == btnSinger) {

			status = 1;
			inputSearch.setText("");
			arrSinger.clear();
			btnSinger.setBackgroundResource(R.drawable.custom_bt_song_choosen);
			btnSinger.setTextColor(myColorselect);
			btnSong.setBackgroundResource(R.drawable.custom_bt_song);
			btnSong.setTextColor(myColor);
			btnType.setBackgroundResource(R.drawable.custom_bt_song);
			btnType.setTextColor(myColor);
			/* Viet them */

			inputSearch.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {

				}

				@Override
				public void afterTextChanged(Editable s) {
					String text = inputSearch.getText().toString()
							.toLowerCase(Locale.getDefault());
					adapterSinger.filter(text);
				}
			});

			/* Viet them */

			setVSB(0, 1, 0);
			getAllSingerREQ();

		} else if (v == btnType) {
			status =2;
			inputSearch.setText("");
			arrType.clear();
			btnType.setBackgroundResource(R.drawable.custom_bt_song_choosen);
			btnType.setTextColor(myColorselect);
			btnSinger.setBackgroundResource(R.drawable.custom_bt_song);
			btnSinger.setTextColor(myColor);
			btnSong.setBackgroundResource(R.drawable.custom_bt_song);
			btnSong.setTextColor(myColor);

			/* Viet them */

			inputSearch.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {

				}

				@Override
				public void afterTextChanged(Editable s) {
					String text = inputSearch.getText().toString()
							.toLowerCase(Locale.getDefault());
					adapterType.filter(text);
				}
			});

			/* Viet them */

			setVSB(0, 0, 1);
			getAllTypeREQ();

		}
	}

	private void setVSB(int lvSongNum, int lvSingerNum, int lvTypeNum) {

		if (lvSongNum == 0) {
			lvSong.setVisibility(View.GONE);
		} else {
			lvSong.setVisibility(View.VISIBLE);
		}

		if (lvSingerNum == 0) {
			lvSinger.setVisibility(View.GONE);
		} else {
			lvSinger.setVisibility(View.VISIBLE);
		}

		if (lvTypeNum == 0) {
			lvType.setVisibility(View.GONE);
		} else {
			lvType.setVisibility(View.VISIBLE);
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

	private void getAllSongREQ(final String idUser) {

		//showProgressDialog();

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_ALL_SONG, new Response.Listener<String>() {

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
									song.setSinger(c.getString("name_singer"));
									song.setUsername(c.getString("username"));
									song.setIdSinger(c.getInt("id_singer"));
									song.setChose(2);
									
									if (db.getUserDetails().get("username")
											.toString()
											.equalsIgnoreCase(nameUser)==true) {
											song.setChose(1);
									}

									arrSong.add(song);
								}
								
								adapterSong = new SongAdapter(getActivity(),
										arrSong);
								lvSong.setAdapter(adapterSong);
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

						//hideProgressDialog();
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
				params.put(AppConfig.TAG_PARSING_TAG, "get_all_song");
				params.put("id_user", idUser);
				return params;
			}
		};

		AppController.getInstance().addToRequestQueue(strReq,
				AppConfig.TAG_REQ_GET_ALL_SONG);

	}

	private void getAllSingerREQ() {

		//showProgressDialog();

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_ALL_SINGER, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d(TAG, "Response: " + response.toString());

						try {
							JSONObject jobj = new JSONObject(response);
							int res = jobj.getInt("success");

							if (res == 1) {
								arrSinger.clear();
								JSONArray arr = jobj.getJSONArray("singers");
								for (int i = 0; i < arr.length(); i++) {
									JSONObject c = arr.getJSONObject(i);
									Singer singer = new Singer();

									singer.setIdSinger(c.getInt("id_singer"));
									singer.setNameSinger(c
											.getString("name_singer").toUpperCase());

									arrSinger.add(singer);
								}
								adapterSinger = new SingerAdapter(
										getActivity(), arrSinger);
								lvSinger.setAdapter(adapterSinger);
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

						hideProgressDialog();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "ERROR PARSING SINGERS");
						hideProgressDialog();
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put(AppConfig.TAG_PARSING_TAG, "get_all_singer");
				return params;
			}
		};

		AppController.getInstance().addToRequestQueue(strReq,
				AppConfig.TAG_REQ_GET_ALL_SINGER);
	}

	private void getAllTypeREQ() {

		showProgressDialog();

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_ALL_TYPE, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d(TAG, "Response: " + response.toString());

						try {
							JSONObject jobj = new JSONObject(response);
							int res = jobj.getInt("success");

							if (res == 1) {
								arrType.clear();
								JSONArray arr = jobj.getJSONArray("type");
								for (int i = 0; i < arr.length(); i++) {
									JSONObject c = arr.getJSONObject(i);
									Type type = new Type();

									type.setIdType(c.getInt("id_type"));
									type.setNameType(c.getString("name_type").toUpperCase());

									arrType.add(type);
								}
								adapterType = new TypeAdapter(getActivity(),
										arrType);
								lvType.setAdapter(adapterType);
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

						hideProgressDialog();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "ERROR PARSING TYPE");
						hideProgressDialog();
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put(AppConfig.TAG_PARSING_TAG, "get_all_type");
				return params;
			}
		};

		AppController.getInstance().addToRequestQueue(strReq,
				AppConfig.TAG_REQ_GET_ALL_TYPE);
	}

	private void getAllSongOfSingerREQ(final String idSinger, final String username) {

		//showProgressDialog();

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_ALL_SONG_OF_SINGER,
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
									song.setSinger(c.getString("name_singer"));
									song.setUsername(c.getString("username"));
									song.setIdSinger(c.getInt("id_singer"));
									song.setChose(2);
									
									if (db.getUserDetails().get("username")
											.toString()
											.equalsIgnoreCase(nameUser)==true) {
											song.setChose(1);
									}

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
						Log.e(TAG, "ERROR PARSING SONGS OF SINGER");
						hideProgressDialog();
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put(AppConfig.TAG_PARSING_TAG, "get_all_song_of_singer");
				params.put("id_singer", idSinger);
				params.put("username", username);
				return params;
			}
		};

		AppController.getInstance().addToRequestQueue(strReq,
				AppConfig.TAG_REQ_GET_ALL_SONG_OF_SINGER);
	}

	private void getAllSongOfTypeREQ(final String idType, final String username) {

		showProgressDialog();

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_ALL_SONG_OF_TYPE,
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
									song.setSinger(c.getString("name_singer"));
									song.setUsername(c.getString("username"));
									song.setIdType(c.getInt("id_type"));
									song.setChose(2);
									
									if (db.getUserDetails().get("username")
											.toString()
											.equalsIgnoreCase(nameUser)==true) {
											song.setChose(1);
									}
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
						Log.e(TAG, "ERROR PARSING SONGS OF TYPE");
						hideProgressDialog();
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put(AppConfig.TAG_PARSING_TAG, "get_all_song_of_type");
				params.put("id_type", idType);
				params.put("username", username);
				return params;
			}
		};

		AppController.getInstance().addToRequestQueue(strReq,
				AppConfig.TAG_REQ_GET_ALL_TYPE);
	}

	private void putFavoriteSong(final String idUser,
			final String idSong, final String idSingerStr, final String nameUser, final String idTypeStr) {

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
								if(status==0){
									getAllSongREQ(idUser);
								}
								else if(status==1){
									arrSong.clear();
									getAllSongOfSingerREQ(idSingerStr, nameUser);
								}
								else if(status==2){
									arrSong.clear();
									getAllSongOfTypeREQ(idTypeStr, nameUser);
								}
								
								//getAllSongREQ(idUser);
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
							}else if(err == 3){
								String mess = jobj.getString("error_msg");
								Toast.makeText(getActivity(), mess,
										Toast.LENGTH_SHORT).show();
							}else if (res == 1) {
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

	private void deleteASongFromFavoriteREQ(final String idUser,
			final String idSong, final String idSingerStr, final String nameUser, final String idTypeStr) {
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
								if(status==0){
									getAllSongREQ(idUser);
								}
								else if(status==1){
									arrSong.clear();
									getAllSongOfSingerREQ(idSingerStr, nameUser);
								}
								else if(status==2){
									arrSong.clear();
									getAllSongOfTypeREQ(idTypeStr, nameUser);
								}
								//getAllSongREQ(idUser);
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
	@SuppressLint("NewApi") 
	private void popupMenu(Context context, final View view, final String idUser,
			final String idSong, final String idSingerStr, final String nameUser, final String idTypeStr) {

		pp = new PopupMenu(context, view, Gravity.END);
		pp.getMenuInflater().inflate(R.menu.popup_search, pp.getMenu());
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
					putFavoriteSong(idUser, idSong, idSingerStr, nameUser, idTypeStr);
					break;
				}
				return true;
			}

		});

		pp.show();
	}
	//////////
	@SuppressLint("NewApi")
	private void popupMenu2(Context context, final View view, final String idUser,
			final String idSong, final String idSingerStr, final String nameUser, final String idTypeStr) {
		pp2 = new PopupMenu(context, view, Gravity.END);
		pp2.getMenuInflater().inflate(R.menu.popup_search_cancel, pp2.getMenu());
		Object menuHelper;
		try {
			Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
			fMenuHelper.setAccessible(true);
			menuHelper = fMenuHelper.get(pp2);
			menuHelper
					.getClass()
					.getDeclaredMethod("setForceShowIcon",
							new Class[] { boolean.class })
					.invoke(menuHelper, true);
		} catch (Exception e) {

			Log.w("Icon", e);
			pp2.show();
			return;
		}

		pp2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case R.id.btnBooking:
					putBookingSong(idUser, idSong);
					break;
				case R.id.btnCancel:
					deleteASongFromFavoriteREQ(idUser, idSong, idSingerStr, nameUser, idTypeStr);
					break;
				}
				return true;
			}

		});

		pp2.show();
	}

}