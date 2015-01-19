package edu.upc.eetac.ea.group1.pandora.android;

import java.util.ArrayList;
import java.util.List;

import edu.upc.eetac.ea.group1.pandora.android.api.PandoraAndroidApi;
import edu.upc.eetac.ea.group1.pandora.android.api.UserAdapter;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Group;
import edu.upc.eetac.ea.group1.pandora.android.api.model.User;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

public class SearchUserActivity extends ListActivity{
	private final static String TAG = SelectedGroupActivity.class.toString();
	private String username;
	private String idgroup;
	private UserAdapter adapter;
	private ArrayList<User> userList = new ArrayList<User>();
	private PandoraAndroidApi api = new PandoraAndroidApi();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		idgroup =  (String) getIntent().getExtras().get("id");
		username =  (String) getIntent().getExtras().get("username");
		setContentView(R.layout.search_user);
		
	}
	private void addGroups(List<User> users){
		adapter = new UserAdapter(this,users,idgroup,username);
		setListAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	

	public void goHome(View v) {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.putExtra("username",
				(String) getIntent().getExtras().get("username"));
		startActivity(intent);
	}
	
	public void SearchSubject(View v){
		EditText etSubject = (EditText) findViewById(R.id.etSubject);
		String user = etSubject.getText().toString();
		(new FetchStingsTask()).execute(user);
	}
	
	public void sendInvitation() {
		

		}
	
	private class FetchStingsTask extends
	AsyncTask<String, Void, List<User>>{
			private ProgressDialog pd;
		
		@Override
		protected List<User> doInBackground(String... params) {
			
			username = "user1";
			
			userList=api.getUsers(params[0]);
			return userList;
		}
		@Override
		protected void onPostExecute(List<User> result){
			if (result==null){
			}
			else{
				addGroups(result);
				
			}
			if (pd != null) {
				pd.dismiss();
			}
		}
	}

}



