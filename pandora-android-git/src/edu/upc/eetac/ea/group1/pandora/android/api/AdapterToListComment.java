package edu.upc.eetac.ea.group1.pandora.android.api;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import edu.upc.eetac.ea.group1.pandora.android.R;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Comment;

public class AdapterToListComment extends BaseAdapter {

	private ArrayList<Comment> data;
	private LayoutInflater inflater;
	
	public AdapterToListComment(Context context,ArrayList<Comment> data){
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
		ViewHolder viewHolder = null;
		if (convertView == null) {			
		convertView = inflater.inflate(R.layout.comment_detail, null);
		viewHolder = new ViewHolder();
		viewHolder.tvcontent = (TextView) convertView
				.findViewById(R.id.tvcontent);
		viewHolder.tvowner = (TextView) convertView
				.findViewById(R.id.tvowner);
		viewHolder.tvdate = (TextView) convertView
				.findViewById(R.id.tvdate);
		String content = data.get(position).getContent();
		String owner = data.get(position).getUser().getUsername();
		String date = data.get(position).getDate().toString();
		
		viewHolder.tvowner.setText(owner);
		viewHolder.tvcontent.setText(content);
		viewHolder.tvdate.setText(date);
		
		}
		
		return convertView;
	}
	
	private static class ViewHolder {
		TextView tvdate;
		TextView tvcontent;
		TextView tvowner;
	}

}

