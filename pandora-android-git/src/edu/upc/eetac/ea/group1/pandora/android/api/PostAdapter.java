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
		convertView = inflater.inflate(R.layout.comment_details, null);
		viewHolder = new ViewHolder();
		viewHolder.tvpost = (TextView) convertView
				.findViewById(R.id.tvpost);
		viewHolder.tvauthor = (TextView) convertView
				.findViewById(R.id.tvauthor);

		String author = data.get(position).getUser().getUsername();
		String content = data.get(position).getContent();
		viewHolder.tvauthor.setText(author);
		viewHolder.tvpost.setText(content);
		}
		return convertView;
	}
	
	private static class ViewHolder {
		TextView tvpost;
		TextView tvauthor;
	}

}
