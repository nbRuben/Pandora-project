package edu.upc.eetac.ea.group1.pandora.android;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.upc.eetac.ea.group1.pandora.android.api.DocumentAdapter;
import edu.upc.eetac.ea.group1.pandora.android.api.PandoraAndroidApi;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Document;

public class DocumentsActivity extends ListActivity {
	private PandoraAndroidApi api;
	private DocumentAdapter adapter;
	public File f;
	private static int RESULT_LOAD_IMAGE = 1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_document_list);
		api = new PandoraAndroidApi();
		if(getIntent().getExtras().getString("idSubject")!=null){
			(new GetDocumentsTask()).execute("0", getIntent().getExtras().getString("idSubject"));
		}else if(getIntent().getExtras().getString("idGroup")!=null){
			(new GetDocumentsTask()).execute("1", getIntent().getExtras().getString("idGroup"));
		}
	}
	
	public void goToUploadDoc(View v){
		Intent i = new Intent(
				Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);
		
	}
	
	private void printDocuments(List<Document> doc){
		adapter = new DocumentAdapter(this,(ArrayList<Document>)doc);
		setListAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	public void goHome(View v){
		Intent intent = new Intent(getApplicationContext(),
				MainActivity.class);
		intent.putExtra("username", (String) getIntent().getExtras().get("username"));
		startActivity(intent);
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
 
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
 
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            f = new File(picturePath);
            String imageName = f.getName();
            (new FetchDocumentTask()).execute(imageName);
            
        }
     
    }
	
	@SuppressLint("NewApi") 
	private class FetchDocumentTask extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... params) {
			
			try {
				api.uploadDocument(params[0], (String) getIntent().getExtras().get("username"), getIntent().getExtras().getString("idSubject"), "0");
				api.saveDocument(f);
				finish();
				startActivity(getIntent());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
			
		}
		
	}
	
	private class GetDocumentsTask extends AsyncTask<String, Void, List<Document>> {
		@Override
		protected List<Document> doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<Document> doc = new ArrayList<Document>();
			try {
				doc=api.getDocuments(params[0],params[1]);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return doc;
		}
		
		@Override
		protected void onPostExecute(List<Document> result){
			
			printDocuments(result);
		}
		
	}
}
