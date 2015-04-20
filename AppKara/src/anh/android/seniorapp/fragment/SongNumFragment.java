package anh.android.seniorapp.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import anh.android.seniorapp.adapter.SongNumAdapter;
import anh.android.seniorapp.model.SongNum;
import anh.android.seniorapp.system.AppConfig;
import anh.android.seniorapp.system.AppController;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.appkara.R;

public class SongNumFragment extends Fragment  {

	private static final String TAG = SongNumFragment.class.getSimpleName();

	private EditText inputSearch;

	private ProgressDialog pDialog;

	private ListView lvSong;

	private ArrayList<SongNum> arrSong;
	private SongNumAdapter adapterSong;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_song_num, container,
				false);

		arrSong = new ArrayList<SongNum>();
		lvSong = (ListView) view.findViewById(R.id.listSong);

		inputSearch = (EditText) view.findViewById(R.id.inputSearch);
		
//		btnSongVN = (Button) view.findViewById(R.id.btnSongVN);
//		btnSongEN = (Button) view.findViewById(R.id.btnSongEN);

		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Loading...");
		pDialog.setCancelable(false);

		arrSong.clear();
		getAllSongNumREQ();

//		btnSongVN.setOnClickListener(this);
//		btnSongEN.setOnClickListener(this);
		inputSearch.setOnTouchListener(new AdapterView.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				 v.setFocusable(true);
		            v.setFocusableInTouchMode(true);
				return false;
			}
	    });
		
		inputSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				String text = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
				adapterSong.filter(text);
			}
		});

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

	private void getAllSongNumREQ() {

		showProgressDialog();

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_ALL_SONG_NUM, new Response.Listener<String>() {

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
									SongNum song = new SongNum();

									song.setHasLyric(true);
									song.setSongId(c.getInt("song_id"));
									song.setSongTitle(c.getString("song_name"));
									song.setSongLyric(c.getString("song_lyric"));
									song.setSongAuthor(c
											.getString("song_author"));

									arrSong.add(song);
								}

								adapterSong = new SongNumAdapter(getActivity(),
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
				params.put(AppConfig.TAG_PARSING_TAG, "get_all_song_num");
				return params;
			}
		};

		AppController.getInstance().addToRequestQueue(strReq,
				AppConfig.TAG_REQ_GET_ALL_SONG_NUM);

	}

//	private void getAllSongEnREQ() {
//
//		showProgressDialog();
//
//		StringRequest strReq = new StringRequest(Method.POST,
//				AppConfig.URL_ALL_SONG_EN, new Response.Listener<String>() {
//
//					@Override
//					public void onResponse(String response) {
//						Log.d(TAG, "Response: " + response.toString());
//
//						try {
//							JSONObject jobj = new JSONObject(response);
//							int res = jobj.getInt("success");
//
//							if (res == 1) {
//								arrSong.clear();
//
//								JSONArray arr = jobj.getJSONArray("songs");
//
//								for (int i = 0; i < arr.length(); i++) {
//
//									JSONObject c = arr.getJSONObject(i);
//									SongNum song = new SongNum();
//
//									song.setHasLyric(false);
//									song.setSongId(c.getInt("song_id"));
//									song.setSongTitle(c.getString("song_name"));
//									song.setSongAuthor(c
//											.getString("song_author"));
//
//									arrSong.add(song);
//								}
//
//								adapterSong = new SongNumAdapter(getActivity(),
//										arrSong);
//								lvSong.setAdapter(adapterSong);
//
//							}
//
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//
//						hideProgressDialog();
//					}
//				}, new Response.ErrorListener() {
//
//					@Override
//					public void onErrorResponse(VolleyError error) {
//						Log.e(TAG, "ERROR PARSING SONGS");
//						hideProgressDialog();
//					}
//				}) {
//			@Override
//			protected Map<String, String> getParams() {
//				Map<String, String> params = new HashMap<String, String>();
//				params.put(AppConfig.TAG_PARSING_TAG, "get_all_song_en");
//				return params;
//			}
//		};
//
//		AppController.getInstance().addToRequestQueue(strReq,
//				AppConfig.TAG_REQ_GET_ALL_SONG_EN);
//	}

//	@Override
//	public void onClick(View v) {
//		if (v == btnSongVN) {
//			arrSong.clear();
//			btnSongVN.setBackgroundResource(R.drawable.custom_bt_song_choosen);
//			btnSongEN.setBackgroundResource(R.drawable.custom_bt_song);
//			getAllSongNumREQ();
//		} else if (v == btnSongEN) {
//			arrSong.clear();
//			btnSongEN.setBackgroundResource(R.drawable.custom_bt_song_choosen);
//			btnSongVN.setBackgroundResource(R.drawable.custom_bt_song);
//			getAllSongEnREQ();
//		}
//	}
}
