package edu.upc.eetac.ea.group1.pandora.android;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import edu.upc.eetac.ea.group1.pandora.android.api.AdapterToListComment;
import edu.upc.eetac.ea.group1.pandora.android.api.PandoraAndroidApi;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Comment;

public class ViewCommentsActivity extends ListActivity{
	
	private AdapterToListComment adapter;
	PandoraAndroidApi api = new PandoraAndroidApi();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		(new FetchStingsTask()).execute((String) getIntent().getExtras().get("idpost"));
		
	}
	
	private void addComment(List<Comment> comments){
		adapter = new AdapterToListComment(this,(ArrayList<Comment>)comments);
		setListAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	
	public void goHome(View v){
		Intent intent = new Intent(getApplicationContext(),
				MainActivity.class);
		intent.putExtra("username", (String) getIntent().getExtras().get("username"));
		startActivity(intent);
	}

	
	public void writeComment (View v){
		EditText etComment = (EditText) findViewById(R.id.etComment);
		String contentComment = etComment.getText().toString();
		Comment comment = new Comment();
		comment.setContent(contentComment);
		Date date = new Date();
		SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		comment.setDate(formateador.format(date).toString());
		(new WriteCommentsTask()).execute(comment);
	}

	@SuppressLint("NewApi")
	private class FetchStingsTask extends AsyncTask<String, Void, List<Comment>> {
		private ProgressDialog pd;
		@SuppressLint("NewApi")
		@Override
		protected List<Comment> doInBackground(String... params) {
			
			List<Comment> comments = null;
			try {
				comments = api.getListComments(params[0]);

			} catch (Exception e) {
				e.printStackTrace();
			}
				return comments;
		}
		
		@Override
		protected void onPostExecute(List<Comment> result) {
			if (result.size()==0){
				Toast toast1 = Toast.makeText(getApplicationContext(),
						"No tiene comentarios.", Toast.LENGTH_SHORT);
				toast1.show();
			}
			else{				
				addComment(result);
			}
			if (pd != null) {
				pd.dismiss();
			}
		}
	}
	private class WriteCommentsTask extends AsyncTask<Comment, Void, Void> {
		int err=0;
		@Override
		protected Void doInBackground(Comment... params) {
	
			try {
				PandoraAndroidApi api = new PandoraAndroidApi();
				err++;
				api.writeComment(params[0],(String) getIntent().getExtras().get("username"), (String) getIntent().getExtras().get("idpost"));
				err++;
				finish();
				startActivity(getIntent());

			} catch (Exception e) {
				e.printStackTrace();
				if(err==1){
					Toast toast1 = Toast.makeText(getApplicationContext(),
							"Error al escribir el comentario", Toast.LENGTH_SHORT);
					toast1.show();
				}
				else if (err==2){
					Toast toast1 = Toast.makeText(getApplicationContext(),
							"Error al recargar la pagina", Toast.LENGTH_SHORT);
					toast1.show();
				}
				
			}
			return null;
		}
		
	}

}
