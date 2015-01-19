package edu.upc.eetac.ea.group1.pandora.android;


import java.util.ArrayList;
import java.util.List;

import edu.upc.eetac.ea.group1.pandora.android.api.model.Group;
import edu.upc.eetac.ea.group1.pandora.android.api.GroupAdapter;
import edu.upc.eetac.ea.group1.pandora.android.api.PandoraAndroidApi;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class GroupActivity extends ListActivity{

	private final static String TAG = GroupActivity.class.toString();
	private ArrayList<Group> groupList = new ArrayList<Group>();
	private GroupAdapter adapter;
	private String username;
	private PandoraAndroidApi api = new PandoraAndroidApi();
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
				inflater.inflate(R.menu.groups_menu, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case R.id.MnuOpc1:
        	Intent intent = new Intent(this, CreateGroupActivity.class);
    		intent.putExtra("username", username);
        	startActivity(intent);
    		return true;
        case R.id.MnuOpc2:
        	Intent intent2 = new Intent(this, ExitGroupActivity.class);
        	intent2.putExtra("username", username);
    		startActivity(intent2);
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
	public void goHome(View v) {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.putExtra("username",
				(String) getIntent().getExtras().get("username"));
		startActivity(intent);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_v2);
		(new FetchStingsTask()).execute();
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
	Group g = groupList.get(position);
	Intent intent = new Intent (this, SelectedGroupActivity.class);
	intent.putExtra("id",g.getId());
	intent.putExtra("username",username);
	startActivity(intent);
	}
	
	private void addGroups(List<Group> group){
		adapter = new GroupAdapter(this,group);
		setListAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	private class FetchStingsTask extends
	AsyncTask<Void, Void, List<Group>>{
			private ProgressDialog pd;
		
		@Override
		protected List<Group> doInBackground(Void... params) {
			
			username = (String) getIntent().getExtras().get("username");
			groupList=api.getGroupsByUser(username);
			return groupList;
		}
		@Override
		protected void onPostExecute(List<Group> result){
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
