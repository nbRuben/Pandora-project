package edu.upc.eetac.ea.group1.pandora.android;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Group;
import edu.upc.eetac.ea.group1.pandora.android.api.GroupAdapter;
import edu.upc.eetac.ea.group1.pandora.android.api.PandoraAndroidApi;

public class ExitGroupActivity extends ListActivity{

	private final static String TAG = ExitGroupActivity.class.toString();
	private String username;
	private ArrayList<Group> groupList = new ArrayList<Group>();
	private GroupAdapter adapter;
	private PandoraAndroidApi api = new PandoraAndroidApi();
	private String idgroup;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_v2);
		username =  (String) getIntent().getExtras().get("username");
		(new FetchStingsTask()).execute();
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		Group g = groupList.get(position);
		Log.i(TAG, "Iten Clicked: " + g.getName());
		idgroup = g.getId();
		FetchStingsTask2 newTask = new FetchStingsTask2(v, position);
        newTask.execute();
		
		}
	public void goHome(View v) {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.putExtra("username",
				(String) getIntent().getExtras().get("username"));
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
				username = "user1";				
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
	private class FetchStingsTask2 extends AsyncTask<Void, Void, Void> {
    	private View mView;
    	private int mPosition;

    	public FetchStingsTask2(View view, int position){
    		mView = view;
    		mPosition = position;
  }
    	protected Void doInBackground(Void... urls) {
             api.ExitOfGroup(username, idgroup);

				Intent intent = new Intent(getApplicationContext(),  MainActivity.class);
				intent.putExtra("username", (String) getIntent().getExtras().get("username"));
				startActivity(intent);
            return null;
        }
    	 protected void onPostExecute(Void result) {
    		
         }
}
}
