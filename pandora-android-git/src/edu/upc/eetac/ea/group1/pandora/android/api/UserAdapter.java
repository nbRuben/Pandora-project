package edu.upc.eetac.ea.group1.pandora.android.api;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.upc.eetac.ea.group1.pandora.android.GroupActivity;
import edu.upc.eetac.ea.group1.pandora.android.MainActivity;
import edu.upc.eetac.ea.group1.pandora.android.R;
import edu.upc.eetac.ea.group1.pandora.android.SubjectActivity;
import edu.upc.eetac.ea.group1.pandora.android.api.model.User;

public class UserAdapter extends BaseAdapter{
	
	private List<User> data;
	private LayoutInflater inflater;
	private int poss = 0;
	public Context context;
	public String groupid;
	public String username;
	public String usernameaux;
	private PandoraAndroidApi api = new PandoraAndroidApi();
	
	public UserAdapter(Context context,List<User> data, String groupid, String username) {
		super();
		inflater = LayoutInflater.from(context);
		this.data = data;
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.data = data;
		this.groupid = groupid;
		this.username = username;
	}

	@Override
	public int getCount() {
		return data.size();
	}
	 
	@Override
	public Object getItem(int position) {
		return data.get(position);
	}
	 
	@Override
	public long getItemId(int position) {
		return Long.parseLong(((User)getItem(position)).getName());
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder=null;
		if (convertView == null) {			
		convertView = inflater.inflate(R.layout.user_detail,null);
		viewHolder = new ViewHolder();
		
		viewHolder.tvid=(TextView) convertView.findViewById(R.id.tvuser);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String id = (data.get(position).getUsername());
		usernameaux = id;
		viewHolder.tvid.setText(id);
	
		final LinearLayout subjectLayout = (LinearLayout) convertView.findViewById(R.id.lluser);
		
		subjectLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FetchStingsTask2 newTask = new FetchStingsTask2();
				newTask.execute();
			}
			
		});
		return convertView;
	}
	

	private static class ViewHolder {
		TextView tvid;
	}
	private class FetchStingsTask2 extends AsyncTask<Void, Void, Void> {

    	protected Void doInBackground(Void... urls) {
    		api.inviteUser(groupid,usernameaux);

            return null;
        }
    	 protected void onPostExecute(Void result) {
    		 Intent intent = new Intent(context, MainActivity.class);
				intent.putExtra("username", username);
		    	context.startActivity(intent);
         }
}
}
