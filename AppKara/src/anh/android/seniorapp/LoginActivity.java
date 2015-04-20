package anh.android.seniorapp;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import anh.android.seniorapp.control.SQLiteHandler;
import anh.android.seniorapp.control.SessionManager;
import anh.android.seniorapp.system.AppConfig;
import anh.android.seniorapp.system.AppController;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.appkara.R;


public class LoginActivity extends ActionBarActivity implements OnClickListener {

	private static final String TAG = LoginActivity.class.getSimpleName();

	private Button btnLogin;
	private Button btnLinkToRegister;

	private EditText inputUsername;
	private EditText inputPassword;

	private ProgressDialog pDialog;
	private SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		inputUsername = (EditText) findViewById(R.id.edt_Username);
		inputPassword = (EditText) findViewById(R.id.edt_Password);

		btnLogin = (Button) findViewById(R.id.btn_Login);
		btnLinkToRegister = (Button) findViewById(R.id.btn_LinkToRegisterScreen);

		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);

		session = new SessionManager(getApplicationContext());

		if (session.isLoggedIn()) {
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}

		btnLogin.setOnClickListener(this);
		btnLinkToRegister.setOnClickListener(this);

	}

	private void checkLogin(final String name, final String pass) {

		pDialog.setMessage("Logging in ...");
		showDialog();

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_LOGIN, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d(TAG, "Login Response: " + response.toString());
						hideDialog();

						try {
							JSONObject jObj = new JSONObject(response);

							String res = jObj
									.getString(AppConfig.TAG_PARSING_SUCCESS);
							if (Integer.parseInt(res) == 1) {

								session.setLogin(true);

								JSONObject user = jObj
										.getJSONObject(AppConfig.TAG_PARSING_USER);

								SQLiteHandler db = new SQLiteHandler(
										getApplicationContext());
								db.deleteUsers();
								db.addUser(
										user.getString(AppConfig.TAG_PARSING_UID),
										user.getString(AppConfig.TAG_PARSING_USERNAME),
										pass);

								Intent intent = new Intent(LoginActivity.this,
										MainActivity.class);
								startActivity(intent);

								finish();
							}

							else {

								String errorMsg = jObj
										.getString(AppConfig.TAG_PARSING_ERROR);
								Toast.makeText(getApplicationContext(),
										errorMsg, Toast.LENGTH_LONG).show();
							}
						} catch (JSONException e) {

							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "Login Error: " + error.getMessage());
						Toast.makeText(getApplicationContext(),
								error.getMessage(), Toast.LENGTH_LONG).show();
						hideDialog();
					}
				}) {

			@Override
			protected Map<String, String> getParams() {

				Map<String, String> params = new HashMap<String, String>();
				params.put(AppConfig.TAG_PARSING_TAG,
						AppConfig.TAG_PARSING_LOGIN);
				params.put(AppConfig.TAG_PARSING_USERNAME, name);
				params.put(AppConfig.TAG_PARSING_PASSWORD, pass);

				return params;
			}

		};

		AppController.getInstance().addToRequestQueue(strReq,
				AppConfig.TAG_REQ_LOGIN);
	}

	private void showDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hideDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}

	@Override
	public void onClick(View v) {

		if (v == btnLogin) {

			String username = inputUsername.getText().toString();
			String password = inputPassword.getText().toString();

			if (username.trim().length() > 0 && password.trim().length() > 0) {
				checkLogin(username, password);
			} else {
				Toast.makeText(getApplicationContext(),
						"Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_LONG)
						.show();
			}

		} else if (v == btnLinkToRegister) {
			Intent i = new Intent(getApplicationContext(),
					RegisterActivity.class);
			startActivity(i);
			finish();
		}

	}
}
