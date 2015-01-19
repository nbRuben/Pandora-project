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
import android.view.View;
import android.widget.Toast;
import edu.upc.eetac.ea.group1.pandora.android.api.AdapterToSchedule;
import edu.upc.eetac.ea.group1.pandora.android.api.PandoraAndroidApi;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Schedule;
import edu.upc.eetac.ea.group1.pandora.android.api.model.ScheduleDaySubject;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Subject;

public class ScheduleActivity extends ListActivity{
	
	PandoraAndroidApi api = new PandoraAndroidApi();
	private AdapterToSchedule adapter;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);
		(new FetchStingsTask()).execute((String) getIntent().getExtras().get("nom"));
	}
	
	private void addSchedule(List<ScheduleDaySubject> scheduleDaySubjectList) {
		adapter = new AdapterToSchedule(this,(List<ScheduleDaySubject>)scheduleDaySubjectList);
		setListAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	
	public void goHome(View v){
		Intent intent = new Intent(getApplicationContext(),
				MainActivity.class);
		intent.putExtra("username", (String) getIntent().getExtras().get("username"));
		startActivity(intent);
	}
	
	
	@SuppressLint("NewApi")
	private class FetchStingsTask extends AsyncTask<String, Void, List<Schedule>> {
		private ProgressDialog pd;
		@SuppressLint("NewApi")
		@Override
		protected List<Schedule> doInBackground(String... params) {
			
			List<Subject> subjects = null;
			List<Schedule> schedules= new ArrayList<Schedule>();
			
			try {
				subjects=api.getMySubjects(params[0]);
				schedules = api.getMySchedule(subjects);
				
			} catch (Exception e) {
				e.printStackTrace();
				Toast toast1 = Toast.makeText(getApplicationContext(),
						"No se encuentran tus asignaturas.", Toast.LENGTH_SHORT);
				toast1.show();

					
			}
				return schedules;
		}
		
		@Override
		protected void onPostExecute(List<Schedule> result) {
			List<ScheduleDaySubject> scheduleDaySubjectList = new ArrayList<ScheduleDaySubject>();
			ScheduleDaySubject [][] matrixSchedule = new ScheduleDaySubject [5][5];
			
			if (result.size()==0){
				Toast toast1 = Toast.makeText(getApplicationContext(),
						"No se encuentran tu horario.", Toast.LENGTH_SHORT);
				toast1.show();

			}
			else{
				int lu =0,ma=0,mi=0,ju=0,vi=0;
				
				for(int i= 0;i<result.size();i++){
					
					for(int j = 0; j<result.get(i).getGroups().get(0).getDays().size();j++){
						ScheduleDaySubject sds = new ScheduleDaySubject();
						if(result.get(i).getGroups().get(0).getDays().get(j).getDay().equals("Lunes")){
							 sds.setDay(result.get(i).getGroups().get(0).getDays().get(j).getDay());
							 sds.setSubject(result.get(i).getSubject());
							 sds.setTime(result.get(i).getGroups().get(0).getDays().get(j).getTime());
							
							matrixSchedule[lu][0]=sds;
							
							lu++;
						}
						if(result.get(i).getGroups().get(0).getDays().get(j).getDay().equals("Martes")){
							 sds.setDay(result.get(i).getGroups().get(0).getDays().get(j).getDay());
							 sds.setSubject(result.get(i).getSubject());
							 sds.setTime(result.get(i).getGroups().get(0).getDays().get(j).getTime());

							matrixSchedule[ma][1]=sds;
							ma++;
						}
						if(result.get(i).getGroups().get(0).getDays().get(j).getDay().equals("Miercoles")){
							 sds.setDay(result.get(i).getGroups().get(0).getDays().get(j).getDay());
							 sds.setSubject(result.get(i).getSubject());
							 sds.setTime(result.get(i).getGroups().get(0).getDays().get(j).getTime());

							
							matrixSchedule[mi][2]=sds;
							mi++;
						}
						if(result.get(i).getGroups().get(0).getDays().get(j).getDay().equals("Jueves")){
							 sds.setDay(result.get(i).getGroups().get(0).getDays().get(j).getDay());
							 sds.setSubject(result.get(i).getSubject());
							 sds.setTime(result.get(i).getGroups().get(0).getDays().get(j).getTime());

							
							matrixSchedule[ju][3]=sds;
							ju++;
						}
						if(result.get(i).getGroups().get(0).getDays().get(j).getDay().equals("Viernes")){
							 sds.setDay(result.get(i).getGroups().get(0).getDays().get(j).getDay());
							 sds.setSubject(result.get(i).getSubject());
							 sds.setTime(result.get(i).getGroups().get(0).getDays().get(j).getTime());

							
							matrixSchedule[vi][4]=sds;
							vi++;
						}
					}
				}
				for(int y=0;y<5;y++){
					for(int x=0;x<5;x++){
						if(matrixSchedule[x][y]!=null)
							scheduleDaySubjectList.add(matrixSchedule[x][y]);
					}
				}
				
				addSchedule(scheduleDaySubjectList);
				

			}
			if (pd != null) {
				pd.dismiss();
			}
		}


	}
	

}
