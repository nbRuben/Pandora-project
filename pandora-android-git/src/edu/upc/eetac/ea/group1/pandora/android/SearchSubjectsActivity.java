package edu.upc.eetac.ea.group1.pandora.android;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import edu.upc.eetac.ea.group1.pandora.android.api.PandoraAndroidApi;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Subject;
import edu.upc.eetac.ea.group1.pandora.android.api.SubjectAdapter;

public class SearchSubjectsActivity extends ListActivity {

	private PandoraAndroidApi api;
	private SubjectAdapter adapter;
	private String search;
	private ArrayList<String> match;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_subject);
		search = null;
		this.match = new ArrayList<String>();
		api = new PandoraAndroidApi();
	} 
	

	public void goHome(View v){
		Intent intent = new Intent(getApplicationContext(),
				MainActivity.class);
		intent.putExtra("username", (String) getIntent().getExtras().get("username"));
		startActivity(intent);
	}
	
	
	public void SearchSubject(View v){
		EditText etSubject = (EditText) findViewById(R.id.etSubject);
		search = etSubject.getText().toString();
		this.match.clear();
		(new FetchSubjectsTask()).execute(search);
	}

	private void printSubjects(List<Subject> subjects){
		adapter = new SubjectAdapter(this,(ArrayList<Subject>)subjects, (String) getIntent().getExtras().get("username"), (ArrayList<String>) match, search);
		setListAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	public void refreshSearch(String param){
		(new FetchSubjectsTask()).execute(param);
	}

	
	private class FetchSubjectsTask extends AsyncTask<String, Void, List<Subject>> {
		private ProgressDialog pd;
		@Override
		protected List<Subject> doInBackground(String... params) {
			List<Subject> subjects = null;
			List<Subject> mySubjects = null;
			match = new ArrayList<String>();
			try {
				subjects = api.searchSubjects(params[0]);
				mySubjects = api.getMySubjects((String) getIntent().getExtras().get("username"));
				for(Subject s1: subjects){
					match.add("false");
				}
				for(Subject s1: mySubjects){
					int cont = 0;
					for(Subject s2: subjects){
						if(s1.getId().equals(s2.getId())){
							match.set(cont, "true");
						}

						cont ++ ;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
				return subjects; 
		}
		
		@Override
		protected void onPostExecute(List<Subject> result) {
				printSubjects(result);
			if (pd != null) {
				pd.dismiss();
			}
		}
	}
	
	
	
	
	
}
