package edu.upc.eetac.ea.group1.pandora.android;

import java.util.ArrayList;
import java.util.List;

import edu.upc.eetac.ea.group1.pandora.android.api.PandoraAndroidApi;
import edu.upc.eetac.ea.group1.pandora.android.api.SubjectAdapter;
import edu.upc.eetac.ea.group1.pandora.android.api.UsersAdapter;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Subject;
import edu.upc.eetac.ea.group1.pandora.android.api.model.User;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SearchUsersActivity extends ListActivity{
	
	private PandoraAndroidApi api;
	private UsersAdapter adapter;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_user);
		api = new PandoraAndroidApi();
	} 
	
	public void goHome(View v){
		Intent intent = new Intent(getApplicationContext(),
				MainActivity.class);
		intent.putExtra("username", (String) getIntent().getExtras().get("username"));
		startActivity(intent);
	}
	
	public void SearchSubject(View v){
		EditText etSubject = (EditText) findViewById(R.id.etSubject);
		(new FetchUsersTask()).execute(etSubject.getText().toString());
	}
	
	private void printUsers(List<User> users){
		adapter = new UsersAdapter(this,(ArrayList<User>)users, (String) getIntent().getExtras().get("username"));
		setListAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	private class FetchUsersTask extends AsyncTask<String, Void, List<User>> {
		private ProgressDialog pd;
		@Override
		protected List<User> doInBackground(String... params) {
			List<User> users = new ArrayList<User>();
			try {
				users = api.getUsers(params[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return users;
		}
		
		@Override
		protected void onPostExecute(List<User> result) {
				printUsers(result);
			if (pd != null) {
				pd.dismiss();
			}
		}
	}
	

}
