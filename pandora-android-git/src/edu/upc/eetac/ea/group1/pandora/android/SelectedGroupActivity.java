package edu.upc.eetac.ea.group1.pandora.android;

import java.util.ArrayList;
import java.util.List;








import edu.upc.eetac.ea.group1.pandora.android.api.model.Group;
import edu.upc.eetac.ea.group1.pandora.android.api.PandoraAndroidApi;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Post;
import edu.upc.eetac.ea.group1.pandora.android.api.PostAdapter;
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

public class SelectedGroupActivity extends ListActivity{
	private final static String TAG = SelectedGroupActivity.class.toString();
	private String username;
	private String idgroup;
	private ArrayList<Post> postList = new ArrayList<Post>();
	private PostAdapter adapter;
	private PandoraAndroidApi api = new PandoraAndroidApi();
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
				inflater.inflate(R.menu.group_selected_menu, menu);
		return true;
	}
	

	public void goHome(View v) {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.putExtra("username",
				(String) getIntent().getExtras().get("username"));
		startActivity(intent);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case R.id.MnuOpc1:
        	Intent intent2 = new Intent(this, SearchUserActivity.class);
        	intent2.putExtra("username", username);
        	idgroup =  (String) getIntent().getExtras().get("id");
    		intent2.putExtra("id", idgroup);
    		startActivity(intent2);
    		return true;
        case R.id.MnuOpc2:
        	Intent intent = new Intent(this, PostGroupActivity.class);
        	intent.putExtra("username", username);
        	idgroup =  (String) getIntent().getExtras().get("id");
    		intent.putExtra("id", idgroup);
    		intent.putExtra("username", username);
    		startActivity(intent);
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selected_group);
		idgroup =  (String) getIntent().getExtras().get("id");
		username =  (String) getIntent().getExtras().get("username");
		(new FetchStingsTask()).execute();
	}
	private void addPosts(ArrayList<Post> post){
		adapter = new PostAdapter(this,post);
		setListAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	private class FetchStingsTask extends
	AsyncTask<Void, Void, ArrayList<Post>>{
			private ProgressDialog pd;
		
		@Override
		protected ArrayList<Post> doInBackground(Void... params) {
			
			postList=api.getPostsByGroup(idgroup);
			return postList;
		}
		@Override
		protected void onPostExecute(ArrayList<Post> result){
			if (result==null){
			}
			else{
				addPosts(result);
			}
			if (pd != null) {
				pd.dismiss();
			}
		}
	}
}
