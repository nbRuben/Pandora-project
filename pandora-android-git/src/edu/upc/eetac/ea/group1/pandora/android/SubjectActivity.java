package edu.upc.eetac.ea.group1.pandora.android;

import java.util.ArrayList;
import java.util.List;

import edu.upc.eetac.ea.group1.pandora.android.api.PostAdapter;
import edu.upc.eetac.ea.group1.pandora.android.api.PandoraAndroidApi;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Post;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SubjectActivity extends ListActivity{

	private PandoraAndroidApi api = new PandoraAndroidApi();
	private PostAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subject);
		loadContent();
	}
		
	private void loadContent(){
		TextView content = (TextView) findViewById(R.id.tvcontent);
		content.setText((String) getIntent().getExtras().get("subjectName"));
		(new FetchCommentsTask()).execute();
	}
	
	
	public void goHome(View v){
		Intent intent = new Intent(getApplicationContext(),
				MainActivity.class);
		intent.putExtra("username", (String) getIntent().getExtras().get("username"));
		startActivity(intent);
	}
	
	public void postComment(View v){
		Intent intent = new Intent(this, PostActivity.class);
		intent.putExtra("idSubject", (String) getIntent().getExtras().getString("idSubject"));
		intent.putExtra("subjectName", (String) getIntent().getExtras().getString("subjectName"));
		intent.putExtra("username", (String) getIntent().getExtras().get("username"));
		startActivity(intent);
		
	}
	
	private void printPosts(List<Post> posts){
		adapter = new PostAdapter(this,(ArrayList<Post>)posts);
		setListAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	private class FetchCommentsTask extends AsyncTask<String, Void, List<Post>> {
		private ProgressDialog pd;
		@Override
		protected List<Post> doInBackground(String... params) {
			
			List<Post> posts = null;
			try {
				posts = api.getPosts((String) getIntent().getExtras().getString("idSubject")); //cambiar el pasar id

			} catch (Exception e) {
				e.printStackTrace();
			}
				return posts;
		}
		
		@Override
		protected void onPostExecute(List<Post> result) {
			if (result.size()==0){
				printPosts(result);
			}
			else{
				printPosts(result);
			}
			if (pd != null) {
				pd.dismiss();
			}
		}
	}
	
}
