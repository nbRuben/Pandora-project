package edu.upc.eetac.ea.group1.pandora.android.api;

import java.util.ArrayList;

import edu.upc.eetac.ea.group1.pandora.android.R;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Post;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PostAdapter extends BaseAdapter{
	
	private ArrayList<Post> data;
	private LayoutInflater inflater;
	
	public PostAdapter(Context context,ArrayList<Post> data){
		super();
		inflater = LayoutInflater.from(context);
		this.data = data;
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
		convertView = inflater.inflate(R.layout.post_details, null);
		viewHolder = new ViewHolder();
		viewHolder.tvpost = (TextView) convertView
				.findViewById(R.id.tvpost);
		viewHolder.tvauthor = (TextView) convertView
				.findViewById(R.id.tvauthor);
		viewHolder.tvpostID = (TextView) convertView
				.findViewById(R.id.tvpostID);

		System.out.println(data.get(position));
		viewHolder.tvauthor.setText(data.get(position).getUser().getUsername());
		viewHolder.tvpost.setText(data.get(position).getContent());
		viewHolder.tvpostID.setText(data.get(position).getId());
		}
		return convertView;
	}
	
	private static class ViewHolder {
		TextView tvpost;
		TextView tvauthor;
		TextView tvpostID;
	}

}
