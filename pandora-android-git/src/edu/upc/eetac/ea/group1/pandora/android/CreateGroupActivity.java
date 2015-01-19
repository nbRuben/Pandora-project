package edu.upc.eetac.ea.group1.pandora.android;


import edu.upc.eetac.ea.group1.pandora.android.api.model.Group;
import edu.upc.eetac.ea.group1.pandora.android.api.PandoraAndroidApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateGroupActivity extends Activity{

	private final static String TAG = GroupActivity.class.toString();
	private String username;
	private PandoraAndroidApi api = new PandoraAndroidApi();
	
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "Metemos Layout");
		System.out.println("Metemos Layout");
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.create_group);
	}
	
	public void sendGroup(View v){
		EditText postGroup= (EditText) findViewById(R.id.etGroupName);
		String content = postGroup.getText().toString();
		(new PostGroupTask()).execute(content);
	}
	public void goHome(View v) {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.putExtra("username",
				(String) getIntent().getExtras().get("username"));
		startActivity(intent);
	}

	private class PostGroupTask extends AsyncTask<String, Void, Void> {
		private ProgressDialog pd;
		@Override
		protected Void doInBackground(String... params) {
	
			try {
				PandoraAndroidApi api = new PandoraAndroidApi();
				Group result = api.addGroup(params[0], (String) getIntent().getExtras().get("username"));
				
				if (result != null){
					api.AddToGroup(username, result.getId());
					Intent intent = new Intent(getApplicationContext(), MainActivity.class);
					intent.putExtra("username", (String) getIntent().getExtras().get("username"));
					startActivity(intent);
				}
				else {
					Toast toast = Toast.makeText(getApplicationContext(),
							"No se ha podido crear el Grupo", Toast.LENGTH_LONG);
					toast.show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
	}
	public void create(View v) {
		EditText etGroupName = (EditText) findViewById(R.id.etGroupName);
		String groupname = etGroupName.getText().toString();
		if (groupname.equals("")){
			Toast toast = Toast.makeText(getApplicationContext(),"Debe rellenar todos los campos", 
					   Toast.LENGTH_LONG);
			toast.show();
		}
		else
		{
			new PostGroupTask().execute(groupname);
		}
	}
	public void cancel(View v){
		Intent intent = new Intent(this, GroupActivity.class);
		startActivity(intent);
		
	}
}


