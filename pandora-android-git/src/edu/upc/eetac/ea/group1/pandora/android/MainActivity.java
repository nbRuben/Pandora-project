package edu.upc.eetac.ea.group1.pandora.android;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import edu.upc.eetac.ea.group1.pandora.android.api.AdapterToList;
import edu.upc.eetac.ea.group1.pandora.android.api.PandoraAndroidApi;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Notification;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Post;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Subject;

public class MainActivity extends ListActivity
{
	private AdapterToList adapter;
	private List<Post> postClick;
	PandoraAndroidApi api = new PandoraAndroidApi();
	private int newNotifications = 0;
	private List<Notification> myNotifications;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		myNotifications = new ArrayList<Notification>();
		(new FetchStingsTask()).execute((String) getIntent().getExtras().get("username"));
		(new FetchNotificationsTask()).execute((String) getIntent().getExtras().get("username"));
		//(new FetchStingsTask()).execute("user1");
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
				inflater.inflate(R.menu.navigation_menu, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case R.id.MnuOpc1:
        	Intent intent = new Intent(this, MainActivity.class);
    		intent.putExtra("username", (String) getIntent().getExtras().get("username"));
    		Log.i("SelectedGroupActivity","Nos vamos a Join con: "+(String) getIntent().getExtras().get("username"));
        	startActivity(intent);
    		return true;
        case R.id.MnuOpc2:/*
        	Intent intent2 = new Intent(this, ExitGroupActivity.class);
        	intent2.putExtra("username", username);
        	Log.i("SelectedGroupActivity","Nos vamos a Exit con: "+username);
    		startActivity(intent2);
    		return true;*/
        case R.id.MnuOpc3:
        	Intent intent3 = new Intent(this, MySubjectsActivity.class);
        	intent3.putExtra("username", (String) getIntent().getExtras().get("username"));
        	Log.i("SelectedGroupActivity","Nos vamos a Exit con: "+(String) getIntent().getExtras().get("username"));
    		startActivity(intent3);
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }

	
	private void addPosts(List<Post> posts){
		adapter = new AdapterToList(this,(ArrayList<Post>)posts);
		setListAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	protected void onListItemClick(ListView l, View v, int position, long id){
		Intent intent = new Intent(getApplicationContext(), ViewCommentsActivity.class );
			intent.putExtra("idpost", postClick.get(position).getId());
			intent.putExtra("username", (String) getIntent().getExtras().get("username"));
			startActivity(intent);
	}
	
	private void setNotifications(){
		Button bNotifications = (Button) findViewById(R.id.bNotifications);
		bNotifications.setText(Integer.toString(newNotifications));
	}
	
	public void goToNotifications(View v){
		/*Intent intent = new Intent(this, NotificationActivity.class);
		intent.putExtra("username", (String) getIntent().getExtras().get("username"));
		intent.putExtra("myNotifications", myNotifications);
    	startActivity(intent);*/
	}
	
	
	@SuppressLint("NewApi")
	private class FetchStingsTask extends AsyncTask<String, Void, List<Post>> {
		private ProgressDialog pd;
		@SuppressLint("NewApi")
		@Override
		protected List<Post> doInBackground(String... params) {
			
			List<Post> posts = null;
			try {
				posts = api.getListRecentActivity(params[0]);
				postClick=posts;
			} catch (Exception e) {
				e.printStackTrace();
				Log.i("MainActivity","Error al recibir el post de la MiniAPI.");
			}
				return posts;
		}
		
		@Override
		protected void onPostExecute(List<Post> result) {
			if (result.size()==0){
				Toast toast1 = Toast.makeText(getApplicationContext(),
						"No se encuentran actividades recientes.", Toast.LENGTH_SHORT);
				toast1.show();
				addPosts(result);
			}
			else{
				
				addPosts(result);
			}
			if (pd != null) {
				pd.dismiss();
			}
		}
	}
	
	@SuppressLint("NewApi")
	private class FetchNotificationsTask extends AsyncTask<String, Void, List<Notification>> {
		private ProgressDialog pd;
		@SuppressLint("NewApi")
		@Override
		protected List<Notification> doInBackground(String... params) {
			
			List<Notification> notifications = new ArrayList<Notification>();
			List<Subject> mySubjects = new ArrayList<Subject>();
	
			//Falta lo mismo para grupo
			try {
				mySubjects = api.getMySubjects(params[0]);
				notifications = api.getNotifications();

			} catch (Exception e) {
				e.printStackTrace();
				Log.i("MainActivity","Error al recibir el post de la MiniAPI.");
			}
			
			for(Notification n:notifications){
				System.out.println("Notificacion con id " +n.getId() + " del tipo " +n.getType() + " de la asignatura " +n.getSubject().getName());
				for(Subject s: mySubjects){
					if(s.getId().equals(n.getSubject().getId())){
						myNotifications.add(n);
					}
				}
			}
			System.out.println("llegamos y la longitud es " + myNotifications.size());
			newNotifications = myNotifications.size();
			return myNotifications;
		}
		
		@Override
		protected void onPostExecute(List<Notification> result) {

			setNotifications();
			/*if (result.size()!=0){
				Toast toast1 = Toast.makeText(getApplicationContext(),
						"No se encuentran actividades recientes.", Toast.LENGTH_SHORT);
				toast1.show();
				addPosts(result);
			}
			else{
				
				addPosts(result);
			}
			if (pd != null) {
				pd.dismiss();
			}*/
		}
	}
}
