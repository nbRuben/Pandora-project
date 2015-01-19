package edu.upc.eetac.ea.group1.pandora.android.api;

import java.util.ArrayList;

import edu.upc.eetac.ea.group1.pandora.android.LoginActivity;
import edu.upc.eetac.ea.group1.pandora.android.MySubjectsActivity;
import edu.upc.eetac.ea.group1.pandora.android.R;
import edu.upc.eetac.ea.group1.pandora.android.SearchSubjectsActivity;
import edu.upc.eetac.ea.group1.pandora.android.SubjectActivity;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Subject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SubjectAdapter extends BaseAdapter {

	private ArrayList<Subject> data;
	private ArrayList<String> match;
	private LayoutInflater inflater;
	private PandoraAndroidApi api;
	private String username;
	public Context context;
	private String search;

	public SubjectAdapter(Context context, ArrayList<Subject> data,	String username, ArrayList<String> match, String search) {
		super();
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.data = data;
		this.username = username;
		this.match = match;
		this.search = search;
		this.api = new PandoraAndroidApi();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.subject_detail, null);
			viewHolder = new ViewHolder();
			viewHolder.tvsubject = (TextView) convertView
					.findViewById(R.id.tvsubject);
			viewHolder.tvsubjectID = (TextView) convertView
					.findViewById(R.id.tvsubjectID);
			String add = match.get(position);

			final Button addButton = (Button) convertView.findViewById(R.id.bAdd);
			if (add.equals("true")) {
				addButton.setBackgroundColor(Color.RED);
				addButton.setText("Eliminar");
			} else {
				addButton.setText("Añadir");
			}

			final TextView tvSubjectID = (TextView) convertView
					.findViewById(R.id.tvsubjectID);
			final TextView tvSubjectName = (TextView) convertView
					.findViewById(R.id.tvsubject);
			
			addButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					String idSubject = tvSubjectID.getText().toString();
					if (addButton.getText().toString().equals("Eliminar")) {
						(new ManageSubjectsTask()).execute("1", idSubject, username);
					} else {
						(new ManageSubjectsTask()).execute("0", idSubject, username);
					}

					((Activity) context).finish();
					((Activity) context).startActivity(((Activity) context).getIntent());
					if (context instanceof SearchSubjectsActivity) {
						Intent intent = new Intent(arg0.getContext(), MySubjectsActivity.class);
						intent.putExtra("username", username);
						context.startActivity(intent);
					}
				}

			});

			final LinearLayout subjectLayout = (LinearLayout) convertView.findViewById(R.id.llsubject);

			subjectLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (context instanceof MySubjectsActivity) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(context,
								SubjectActivity.class);
						intent.putExtra("username", username);
						intent.putExtra("subjectName", tvSubjectName.getText().toString());
						intent.putExtra("idSubject", tvSubjectID.getText().toString());
						context.startActivity(intent);

					}

				}

			});

			viewHolder.tvsubjectID.setText(data.get(position).getId());
			viewHolder.tvsubject.setText(data.get(position).getName());
		}
		return convertView;
	}

	private static class ViewHolder {
		TextView tvsubject;
		TextView tvsubjectID;
	}

	private class ManageSubjectsTask extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... params) {
			if (params[0].equals("0")) {
				api.addSubjectToUser(params[1], params[2]);

			}
			if (params[0].equals("1")) {
				api.deleteUserFromSubject(params[1], params[2]);
			}
			return null;
		}
	}

}
