package edu.upc.eetac.ea.group1.pandora.android.api;

import java.util.ArrayList;

import edu.upc.eetac.ea.group1.pandora.android.MySubjectsActivity;
import edu.upc.eetac.ea.group1.pandora.android.R;
import edu.upc.eetac.ea.group1.pandora.android.ViewProfileActivity;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Subject;
import edu.upc.eetac.ea.group1.pandora.android.api.model.User;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UsersAdapter extends BaseAdapter{
	
	private ArrayList<User> data;
	private LayoutInflater inflater;
	private PandoraAndroidApi api;
	private String username;
	public Context context;
	
	public UsersAdapter(Context context, ArrayList<User> data, String username) {
		super();
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.data = data;
		this.username = username;
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
		ViewHolder viewHolder=null;
		if (convertView == null) {			
		convertView = inflater.inflate(R.layout.user_detail,null);
		viewHolder = new ViewHolder();
		
		viewHolder.tvid=(TextView) convertView.findViewById(R.id.tvuser);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final String id = (data.get(position).getUsername());
		viewHolder.tvid.setText(id);
		final LinearLayout userLayout = (LinearLayout) convertView.findViewById(R.id.lluser);
		
		userLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), ViewProfileActivity.class);
				intent.putExtra("nom", id);
				intent.putExtra("username", username);
				context.startActivity(intent);
			}
			
		});
		return convertView;
	}
	

	private static class ViewHolder {
		TextView tvid;
	}

}
