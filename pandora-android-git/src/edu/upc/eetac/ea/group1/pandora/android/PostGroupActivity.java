package edu.upc.eetac.ea.group1.pandora.android;


import edu.upc.eetac.ea.group1.pandora.android.api.PandoraAndroidApi;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

public class PostGroupActivity extends Activity{
	
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
	

	public void goHome(View v) {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.putExtra("username",
				(String) getIntent().getExtras().get("username"));
		startActivity(intent);
	}
	private class PostCommentsTask extends AsyncTask<String, Void, Void> {
		private ProgressDialog pd;
		@Override
		protected Void doInBackground(String... params) {
	
			try {
				PandoraAndroidApi api = new PandoraAndroidApi();
				String id = (String) getIntent().getExtras().getString("id");
				String user = (String) getIntent().getExtras().get("username");
				api.addPostToGroup(params[0],id,user);
				Intent intent = new Intent(getApplicationContext(),  MainActivity.class);
				intent.putExtra("username", (String) getIntent().getExtras().get("username"));
				startActivity(intent);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
	}

}
