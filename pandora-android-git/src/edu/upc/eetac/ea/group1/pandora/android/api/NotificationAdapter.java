package edu.upc.eetac.ea.group1.pandora.android.api;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import edu.upc.eetac.ea.group1.pandora.android.R;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Notification;

public class NotificationAdapter extends BaseAdapter{
	
	public String subjectName;
	private ArrayList<Notification> data;
	private LayoutInflater inflater;
	private PandoraAndroidApi api = new PandoraAndroidApi();
	private String username;
	public Context context;
	
	public NotificationAdapter(Context context, ArrayList<Notification> data,
			String username) {
		super();
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.data = data;
		this.username = username;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			if(data.get(position).getType() != 3){
				convertView = inflater.inflate(R.layout.notification_detail, null);
				viewHolder = new ViewHolder();
				viewHolder.tvTarget = (TextView) convertView
						.findViewById(R.id.tvTarget);
				viewHolder.tvNotificationContent = (TextView) convertView
						.findViewById(R.id.tvNotificationContent);
				String NotificationContent = null;
				if(data.get(position).getType() == 1){
					NotificationContent = "Se ha subido un nuevo archivo";
				}
				if(data.get(position).getType() == 2){
					NotificationContent = "Tu post tiene una nueva respuesta";
				}
				
				viewHolder.tvTarget.setText(data.get(position).getSubject().getName());
				viewHolder.tvNotificationContent.setText(NotificationContent);
				(new UpdateNotificationTask()).execute(data.get(position));
			}else{
				convertView = inflater.inflate(R.layout.invitation_detail, null);
				final Button acceptButton = (Button) convertView.findViewById(R.id.bAccept);
				final Button rejectButton = (Button) convertView.findViewById(R.id.bReject);
				viewHolder = new ViewHolder();
				viewHolder.tvNotificationContent = (TextView) convertView
						.findViewById(R.id.tvNotificationContent);
				viewHolder.tvNotificationContent.setText("Te han invitado al grupo " + data.get(position).getSubject().getName()); //CAMBIAR A GRUPOS
				acceptButton.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						(new UpdateNotificationTask()).execute(data.get(position));

						((Activity) context).finish();
						((Activity) context).startActivity(((Activity) context).getIntent());
					}
					
				});
				
				rejectButton.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						(new UpdateNotificationTask()).execute(data.get(position));

						((Activity) context).finish();
						((Activity) context).startActivity(((Activity) context).getIntent());
					}
					
				});
			}
		}
		return convertView;
	}

	private static class ViewHolder {
		TextView tvTarget;
		TextView tvNotificationContent;
	}
	
	private class UpdateNotificationTask extends AsyncTask<Notification, Void, Void> {
		private ProgressDialog pd;
		@Override
		protected Void doInBackground(Notification... params) {
			api.updateNotification(params[0]);
			return null;
		}
		
	}

}

