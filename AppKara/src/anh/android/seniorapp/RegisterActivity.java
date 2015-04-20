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

public class RegisterActivity extends ActionBarActivity implements OnClickListener{

	private static final String TAG = RegisterActivity.class.getSimpleName();
	
	private Button btnRegister;
	private Button btnLinkToLogin;

	private EditText inputUsername;
	private EditText inputPassword;
	private EditText inputRePassword;
	
	private ProgressDialog pDialog;
	private SessionManager session;
	private SQLiteHandler db;
	
	@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_register);
			
			inputUsername = (EditText) findViewById(R.id.edt_Username);
			inputPassword = (EditText) findViewById(R.id.edt_Password);
			inputRePassword= (EditText) findViewById(R.id.edt_RePassword);
			
			btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
			btnRegister = (Button) findViewById(R.id.btnRegister);
			
			pDialog = new ProgressDialog(this);
			pDialog.setCancelable(false);
			
			session = new SessionManager(getApplicationContext());

			db = new SQLiteHandler(getApplicationContext());

			if (session.isLoggedIn()) {
				Intent intent = new Intent(RegisterActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();
			}
			
			btnRegister.setOnClickListener(this);
			btnLinkToLogin.setOnClickListener(this);
			
	}
	
	private void registerUser(final String username, final String password) {

		pDialog.setMessage("Registering ...");
		showDialog();

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_REGISTER, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d(TAG, "Register Response: " + response.toString());
						hideDialog();

						try {
							JSONObject jObj = new JSONObject(response);

							String res = jObj.getString(AppConfig.TAG_PARSING_SUCCESS);
							if (Integer.parseInt(res) == 1) {

								JSONObject user = jObj.getJSONObject(AppConfig.TAG_PARSING_USER);

								String uid = user.getString(AppConfig.TAG_PARSING_UID);
								String name = user.getString(AppConfig.TAG_PARSING_USERNAME);

								db.addUser(uid, name, password);
								Log.e(TAG, uid + " "+name + " " + password);
								
								Intent intent = new Intent(
										RegisterActivity.this,
										LoginActivity.class);
								startActivity(intent);
								finish();
							}

							else {

								String errorMsg = jObj.getString(AppConfig.TAG_PARSING_ERROR);
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
						Log.e(TAG, "Registration Error: " + error.getMessage());
						Toast.makeText(getApplicationContext(),
								error.getMessage(), Toast.LENGTH_LONG).show();
						hideDialog();
					}
				}) {

			@Override
			protected Map<String, String> getParams() {

				Map<String, String> params = new HashMap<String, String>();
				
				params.put(AppConfig.TAG_PARSING_TAG, AppConfig.TAG_PARSING_REGISTER);
				params.put(AppConfig.TAG_PARSING_USERNAME, username);
				params.put(AppConfig.TAG_PARSING_PASSWORD, password);

				return params;
			}

		};

		AppController.getInstance().addToRequestQueue(strReq, AppConfig.TAG_REQ_REGISTER);
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
		if(v==btnRegister){
			
			String username = inputUsername.getText().toString();
			String password = inputPassword.getText().toString();
			String rePassword = inputRePassword.getText().toString();

			if (!username.isEmpty() && !password.isEmpty() && !rePassword.isEmpty() && password.equals(rePassword)) {
				registerUser(username, password);
			} else {
				Toast.makeText(getApplicationContext(),
						"Please enter your details!", Toast.LENGTH_LONG)
						.show();
			}
		}else if(v== btnLinkToLogin){
			Intent i = new Intent(getApplicationContext(),
					LoginActivity.class);
			startActivity(i);
			finish();
		}
		
	}
}
