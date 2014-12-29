package edu.upc.eetac.ea.group1.pandora.android;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.TextView;
import edu.upc.eetac.ea.group1.pandora.android.api.PandoraAndroidApi;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Subject;
import edu.upc.eetac.ea.group1.pandora.android.api.SubjectAdapter;

public class MySubjectsActivity extends ListActivity {

	private PandoraAndroidApi api = new PandoraAndroidApi();
	private SubjectAdapter adapter;

	private  List<String> match;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_subjects);
		this.match = new ArrayList<String>();
		loadContent();
		
	} 
	
	public void goHome(View v){
		Intent intent = new Intent(getApplicationContext(),
				MainActivity.class);
		intent.putExtra("username", (String) getIntent().getExtras().get("username"));
		startActivity(intent);
	}
	
	public void goToActivity(View v){
		Intent intent = new Intent(this, SubjectActivity.class);
		intent.putExtra("username", (String) getIntent().getExtras().get("username"));
		TextView subjectName = (TextView) findViewById(R.id.tvsubject);
		intent.putExtra("subjectName", subjectName.getText().toString());
		Log.i("SelectedGroupActivity","Nos vamos a Join con: "+(String) getIntent().getExtras().get("username"));
    	startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
				inflater.inflate(R.menu.subject_menu, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case R.id.MnuOpc1:
        	Intent intent = new Intent(this, SearchSubjectsActivity.class);
    		intent.putExtra("username", (String) getIntent().getExtras().get("username"));
    		Log.i("SelectedGroupActivity","Nos vamos a Join con: "+(String) getIntent().getExtras().get("username"));
        	startActivity(intent);
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
	
	private void loadContent(){
		(new FetchSubjectsTask()).execute((String) getIntent().getExtras().get("username"));
	}
	
	private void printSubjects(List<Subject> subjects){
		adapter = new SubjectAdapter(this,(ArrayList<Subject>)subjects, (String) getIntent().getExtras().get("username"), (ArrayList<String>) match);
		setListAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	
	private class FetchSubjectsTask extends AsyncTask<String, Void, List<Subject>> {
		private ProgressDialog pd;
		@Override
		protected List<Subject> doInBackground(String... params) {
			
			List<Subject> subjects = null;
			try {
				subjects = api.getMySubjects(params[0]); 
				for(Subject s: subjects){
					match.add("true");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
				return subjects; 
		}
		
		@Override
		protected void onPostExecute(List<Subject> result) {
			if (result.size()==0){
				
				printSubjects(result);
			}
			else{
				
				printSubjects(result);
			}
			if (pd != null) {
				pd.dismiss();
			}
		}
	}
}
