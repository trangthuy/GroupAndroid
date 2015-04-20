package anh.android.seniorapp.fragment;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import anh.android.seniorapp.control.SQLiteHandler;
import anh.android.seniorapp.system.AppConfig;
import anh.android.seniorapp.system.AppController;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.appkara.R;

public class ProfileFragment extends Fragment implements OnClickListener {

	private static final String TAG = ProfileFragment.class.getSimpleName();

	private TextView tvName, tvPass, tvClick;
	private SQLiteHandler db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_profile, container,
				false);

		db = new SQLiteHandler(getActivity());

		tvName = (TextView) view.findViewById(R.id.tvname);
		tvPass = (TextView) view.findViewById(R.id.tvpass);
		tvClick = (TextView) view.findViewById(R.id.tvclick);

		HashMap<String, String> user = db.getUserDetails();
		String username = user.get("username");
		String password = user.get("password");

		tvName.setText(username);
		tvPass.setText(password);

		tvClick.setOnClickListener(this);

		return view;
	}

	private void updatePassword(final String username, final String pass,
			final String newPass) {

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_UPDATE_PASS, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d(TAG, "Update Response: " + response.toString());

						try {
							JSONObject jObj = new JSONObject(response);

							int res = jObj.getInt("success");
							if (res == 1) {

								db.updateUser(username, pass, newPass);
								tvPass.setText(db.getUserDetails()
										.get("password").toString());

								String message = jObj.getString("message");
								Toast.makeText(getActivity(), message,
										Toast.LENGTH_SHORT).show();

							}

							else {
								Toast.makeText(getActivity(),
										"Error when update password!",
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
				params.put(AppConfig.TAG_PARSING_TAG, "update");
				params.put("username", username);
				params.put("pass", pass);
				params.put("newPass", newPass);

				return params;
			}

		};

		AppController.getInstance().addToRequestQueue(strReq,
				AppConfig.TAG_REQ_UPDATE_PASS);
	}

	@Override
	public void onClick(View v) {

		if (v == tvClick) {

			final Dialog dialog = new Dialog(getActivity());
			dialog.getWindow();
			dialog.setTitle("Đổi mật khẩu");
			dialog.setContentView(R.layout.activity_dialog);
			dialog.show();

			final EditText edtCurrentPassword = (EditText) dialog
					.findViewById(R.id.edtCrurentPass);

			final EditText edtNewPassword = (EditText) dialog
					.findViewById(R.id.edtNewPass);

			final EditText edtReNewPassword = (EditText) dialog
					.findViewById(R.id.edtReNewPass);

			Button btnChange = (Button) dialog.findViewById(R.id.btnChange);
			Button btnCancle = (Button) dialog.findViewById(R.id.btnClose);

			btnChange.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					String currentPass = edtCurrentPassword.getText()
							.toString();
					String newPass = edtNewPassword.getText().toString();
					String reNewPass = edtReNewPassword.getText().toString();

					String username = db.getUserDetails().get("username")
							.toString();
					String password = db.getUserDetails().get("password")
							.toString();

					if (currentPass.trim().length() > 0
							&& newPass.trim().length() > 0
							&& reNewPass.trim().length() > 0) {

						if (!currentPass.equals(password)) {
							Toast.makeText(getActivity(),
									"Bạn nhập sai mật khẩu hiện tại!",
									Toast.LENGTH_SHORT).show();
						} else {

							if (!newPass.equals(reNewPass)) {
								Toast.makeText(getActivity(),
										"Không khớp mật khẩu!",
										Toast.LENGTH_SHORT).show();
							} else {

								if (currentPass.equals(password)) {

									updatePassword(username, password, newPass);
									dialog.cancel();

								} else {

									Toast.makeText(getActivity(),
											"Bạn nhập sai mật khẩu hiện tại!",
											Toast.LENGTH_SHORT).show();
									edtCurrentPassword.setText("");
									edtNewPassword.setText("");
									edtReNewPassword.setText("");

								}
							}

						}

					} else {

						Toast.makeText(getActivity(),
								"Vui lòng nhập đầy đủ thông tin!",
								Toast.LENGTH_SHORT).show();

					}

				}
			});

			btnCancle.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			dialog.show();
		}

	}

}
