package edu.upc.eetac.ea.group1.pandora.android;

import java.io.IOException;

import edu.upc.eetac.ea.group1.pandora.android.api.PandoraAndroidApi;
import edu.upc.eetac.ea.group1.pandora.android.api.model.User;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class ViewProfileActivity extends Activity{
String nom;
String username;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewprofile);
		Bundle bundle = this.getIntent().getExtras();
		username = (String) getIntent().getExtras().get("username");
	    nom= bundle.get("nom").toString();
		TextView text1 = (TextView) findViewById(R.id.llUsername);
		text1.setText(nom);
		TextView text2 = (TextView) findViewById(R.id.headerr);
		text2.setText(nom);
	
		(new ViewProfileTask()).execute(nom);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.profile_menu, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.MnuOpc1:
			Intent intent = new Intent(this, SearchUsersActivity.class);
			intent.putExtra("username",
					(String) getIntent().getExtras().get("username"));
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	
	public void goHome(View v){
		Intent intent = new Intent(getApplicationContext(),
				MainActivity.class);
		intent.putExtra("username", (String) getIntent().getExtras().get("username"));
		startActivity(intent);
	}
	
	private class ViewProfileTask extends AsyncTask<String, Void, User> {

		@Override
		protected User doInBackground(String... params) {
			// TODO Auto-generated method stub
			User user = null;
			PandoraAndroidApi api = new PandoraAndroidApi();
			user = api.ViewProfile(params[0]);
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
					setParameters(result);
			}
		}
	}
	
	public void setParameters(User u )
	{
		TextView text3 = (TextView) findViewById(R.id.llName);
		text3.setText(u.getName());
		TextView text4 = (TextView) findViewById(R.id.llSurname);
		text4.setText(u.getSurname());
		TextView text5 = (TextView) findViewById(R.id.llEmail);
		text5.setText(u.getEmail());
		
	}
	public void horario(View v){
		Intent intent = new Intent(this, ScheduleActivity.class);
		intent.putExtra("nom", nom);
		intent.putExtra("username", username);
		startActivity(intent);			
	}
}
