package edu.upc.eetac.ea.group1.pandora.android;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import edu.upc.eetac.ea.group1.pandora.android.api.PandoraAndroidApi;
import edu.upc.eetac.ea.group1.pandora.android.api.model.User;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private User user = new User();

	public String getMD5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String hashtext = number.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	public void signIn(View v) {
		EditText etUsername = (EditText) findViewById(R.id.etUsername);
		EditText etPassword = (EditText) findViewById(R.id.etPassword);
		user.setUsername(etUsername.getText().toString());
		user.setUserpass(getMD5(etPassword.getText().toString()));
		username = etUsername.getText().toString();
		String password = etPassword.getText().toString();

		if (user.getUsername().equals("") || user.getUserpass().equals("")) {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Debe rellenar todos los campos", Toast.LENGTH_LONG);
			toast.show();
		} else {
			(new LoginTask()).execute(username, password);
		}
	}

	String username;

	@SuppressLint("NewApi")
	private class LoginTask extends AsyncTask<String, Void, User> {

		@Override
		protected User doInBackground(String... params) {
			// TODO Auto-generated method stub
			User user = null;
			PandoraAndroidApi api = new PandoraAndroidApi();
			user = api.LoginUser(params[0], params[1]);
			return user;
		}

		@Override
		protected void onPostExecute(User result) {

			if (result == null) {
				// error al obtener el usuario
				Toast toast1 = Toast.makeText(getApplicationContext(),
						"Algo ha salido mal. Intentelo de nuevo.",
						Toast.LENGTH_SHORT);
				toast1.show();

			} else {

				// el User se ha obtenido bien
				String pass = getMD5(result.getUserpass());
				if (user.getUserpass().equals(pass)) {

					// Pasamos el user a MiniMainActivity
					Intent intent = new Intent(getApplicationContext(),
							MainActivity.class);
					intent.putExtra("username", user.getUsername());
					startActivity(intent);

				} else {
					Toast toast1 = Toast.makeText(getApplicationContext(),
							"Username o password incorrectos. Pruebe otra vez",
							Toast.LENGTH_SHORT);
					toast1.show();

				}
			}
		}
	}

}
