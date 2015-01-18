package edu.upc.eetac.ea.group1.pandora.android;

import edu.upc.eetac.ea.group1.pandora.android.api.PandoraAndroidApi;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.app.Activity;
import android.content.Intent;

public class PostActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_comment);
	}
	
	public void sendComment(View v){
		EditText postContent = (EditText) findViewById(R.id.postContent);
		String content = postContent.getText().toString();
		(new PostCommentsTask()).execute(content);
	}
	
	public void cancel(View v){
		Intent intent = new Intent(getApplicationContext(), SubjectActivity.class);
		intent.putExtra("idSubject", (String) getIntent().getExtras().getString("idSubject"));
		intent.putExtra("subjectName", (String) getIntent().getExtras().getString("subjectName"));
		intent.putExtra("username", (String) getIntent().getExtras().get("username"));
		startActivity(intent);
	}
	

	private class PostCommentsTask extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... params) {
	
			try {
				PandoraAndroidApi api = new PandoraAndroidApi();
				api.addPost(params[0], (String) getIntent().getExtras().getString("idSubject"), (String) getIntent().getExtras().get("username"));
				Intent intent = new Intent(getApplicationContext(), SubjectActivity.class);
				intent.putExtra("idSubject", (String) getIntent().getExtras().getString("idSubject"));
				intent.putExtra("subjectName", (String) getIntent().getExtras().getString("subjectName"));
				intent.putExtra("username", (String) getIntent().getExtras().get("username"));
				startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		
	}

}


