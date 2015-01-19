package edu.upc.eetac.ea.group1.pandora.android;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import edu.upc.eetac.ea.group1.pandora.android.api.PandoraAndroidApi;
import edu.upc.eetac.ea.group1.pandora.android.api.model.User;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateUserActivity extends Activity{
	String nom;	
	
	private final static String TAG = UpdateUserActivity.class.getName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_updateuser);
		Bundle bundle = this.getIntent().getExtras();
		
		nom= bundle.get("username").toString();
		Toast toast1 = Toast.makeText(getApplicationContext(),
				"Bienvenido a update profile"+nom,
				Toast.LENGTH_SHORT);
		toast1.show();
		
		
		(new ViewProfileTask()).execute(nom);
		
		
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
	
	public void goToHome(){
		Intent intent = new Intent (getApplicationContext(), MainActivity.class);
		intent.putExtra("username", nom);
		
		startActivity(intent);
	}
	
	public void setParameters(User u )
	{
		EditText text3 = (EditText) findViewById(R.id.etName);
		text3.setText(u.getName());
		EditText text4 = (EditText) findViewById(R.id.etSurname);
		text4.setText(u.getSurname());
		EditText text5 = (EditText) findViewById(R.id.etEmail);
		text5.setText(u.getEmail());
		EditText text6 = (EditText) findViewById(R.id.etPassword);
		text6.setText(u.getUserpass());
		EditText text7 = (EditText) findViewById(R.id.etconfirmPassword);
		text7.setText(u.getUserpass());
	}

	//UPLOAD DATOS USER ASINTASK
 	private class updateusertask extends AsyncTask <String, Void, User> {
	String username1;
	User resp;
	@Override
	protected User doInBackground(String... params) {
	
	    PandoraAndroidApi api = new PandoraAndroidApi();
		String name1 = params[0];
		String password1 = params[1];
		String surname = params[2];
		String email = params[3];
		
		
		try {																																																					
			
			resp = api.updateUser(name1, password1, surname, email, nom);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return resp;
	}

	@Override
	protected void onPostExecute(User result) {
		goToHome();
	 }
	}


public void update(View v) {
	
	EditText etPassword = (EditText) findViewById(R.id.etPassword);
	EditText etCPassword = (EditText) findViewById(R.id.etconfirmPassword);
	EditText etSurname= (EditText) findViewById(R.id.etSurname);
	EditText etName = (EditText) findViewById(R.id.etName);
	EditText etEmail = (EditText) findViewById(R.id.etEmail);
	 
	String password = etPassword.getText().toString();
	String cpass = etCPassword.getText().toString();
	String surname = etSurname.getText().toString();
	String name = etName.getText().toString();
	String email = etEmail.getText().toString();
	if (password.equals("")|| name.equals("") || surname.equals("") || cpass.equals ("") || email.equals(""))
	{
	
		Toast toast = Toast.makeText(getApplicationContext(),"Debe rellenar todos los campos", 
				   Toast.LENGTH_LONG);
		toast.show();
	
	}
	else
	{

		if (password.equals(cpass)){
			(new updateusertask()).execute(name, password, surname, email);
		}
		else{
			Toast toast = Toast.makeText(getApplicationContext(),"Las constraseñas no coinciden", 
			Toast.LENGTH_LONG);
			toast.show();
			}
	   }
		
	}

public void cancel(View v){
	Intent intent = new Intent(this, MainActivity.class);
	intent.putExtra("username", nom);
	startActivity(intent);
	
}
	
   

}
