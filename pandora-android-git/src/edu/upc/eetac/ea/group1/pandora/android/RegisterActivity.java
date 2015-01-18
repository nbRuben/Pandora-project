package edu.upc.eetac.ea.group1.pandora.android;

import java.io.IOException;

import edu.upc.eetac.ea.group1.pandora.android.api.PandoraAndroidApi;
import edu.upc.eetac.ea.group1.pandora.android.api.model.User;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	private class registertask extends AsyncTask<String, Void, User> {

		private String username1;
		private User resp;

		@Override
		protected User doInBackground(String... params) {

			PandoraAndroidApi api = new PandoraAndroidApi();
			String name1 = params[0];
			username1 = params[1];
			String password1 = params[2];
			String surname = params[3];
			String email = params[4];

			try {
				resp = api.addUserRegister(name1, username1, password1,
						surname, email);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return resp;
		}

		@Override
		protected void onPostExecute(User result) {

			if (result == null) {
				Toast toast = Toast.makeText(getApplicationContext(),
						"Usuario ya registrado", Toast.LENGTH_LONG);
				toast.show();
			} else {
				Toast toast = Toast.makeText(getApplicationContext(),
						" Bienvenido " + username1, Toast.LENGTH_LONG);
				toast.show();
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				intent.putExtra("username", username1);
				startActivity(intent);

			}

		}

	}

	private final static String TAG = LoginActivity.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		SharedPreferences prefs = getSharedPreferences("pandora-api",
				Context.MODE_PRIVATE);

		setContentView(R.layout.activity_register);
	}

	public void register(View v) {
		EditText etUsername = (EditText) findViewById(R.id.etUsername);
		EditText etPassword = (EditText) findViewById(R.id.etPassword);
		EditText etCPassword = (EditText) findViewById(R.id.etconfirmPassword);
		EditText etSurname = (EditText) findViewById(R.id.etSurname);
		EditText etName = (EditText) findViewById(R.id.etName);
		EditText etEmail = (EditText) findViewById(R.id.etEmail);

		String username = etUsername.getText().toString();
		String password = etPassword.getText().toString();
		String cpass = etCPassword.getText().toString();
		String surname = etSurname.getText().toString();
		String name = etName.getText().toString();
		String email = etEmail.getText().toString();
		if (username.equals("") || password.equals("") || name.equals("")
				|| surname.equals("") || cpass.equals("")) {

			Toast toast = Toast.makeText(getApplicationContext(),
					"Debe rellenar todos los campos", Toast.LENGTH_LONG);
			toast.show();

		} else {
			if (password.equals(cpass)) {
				(new registertask()).execute(name, username, password, surname,
						email);
			}

			else {
				Toast toast = Toast.makeText(getApplicationContext(),
						"Las constraseñas no coinciden", Toast.LENGTH_LONG);
				toast.show();
			}

		}
	}

	public void cancel(View v) {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);

	}
}
