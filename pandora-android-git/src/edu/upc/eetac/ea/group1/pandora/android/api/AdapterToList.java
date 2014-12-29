package edu.upc.eetac.ea.group1.pandora.android.api;

import java.util.ArrayList;
import java.util.Date;

import edu.upc.eetac.ea.group1.pandora.android.R;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Post;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterToList extends BaseAdapter {
	
	private ArrayList<Post> data;
	private LayoutInflater inflater;
	
	public AdapterToList(Context context,ArrayList<Post> data){
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
		convertView = inflater.inflate(R.layout.recentactivity_detail, null);
		viewHolder = new ViewHolder();
		viewHolder.tvcontent = (TextView) convertView
				.findViewById(R.id.tvcontent);
		viewHolder.tvowner = (TextView) convertView
				.findViewById(R.id.tvowner);
		viewHolder.tvdate = (TextView) convertView
				.findViewById(R.id.tvdate);
		viewHolder.tvcomment = (TextView) convertView
				.findViewById(R.id.tvcomment);
		viewHolder.tvtypegroup = (TextView) convertView
				.findViewById(R.id.tvtypegroup);
		
		Log.i("MiniAPI","Content: "+data.get(position).getContent());
		Log.i("MiniAPI","Owner: "+data.get(position).getUser().getUsername());
		Log.i("MiniAPI","Date: "+data.get(position).getDate());
		String typegroup="Anonymous";
		if(data.get(position).getGrupo()!=null){
			Log.i("MiniAPI","Grupo Name: "+data.get(position).getGrupo().getName());
			typegroup = data.get(position).getGrupo().getName();
		}
		if(data.get(position).getSubject()!=null){
			Log.i("MiniAPI","Subject Name: "+data.get(position).getSubject().getName());
			typegroup = data.get(position).getSubject().getName();
		}
		if(data.get(position).getComment()==null){
			viewHolder.tvcomment.setText("No hay comentarios");
		}
		if(data.get(position).getComment()==null){
			viewHolder.tvcomment.setText("Sí hay comentarios");
		}
		String content = data.get(position).getContent();
		String owner = data.get(position).getUser().getUsername();
		String date = data.get(position).getDate();
		//String comment = Integer.toString(data.get(position).getComment().size());
		
		viewHolder.tvowner.setText(owner);
		viewHolder.tvcontent.setText(content);
		viewHolder.tvdate.setText(date);
		viewHolder.tvtypegroup.setText(typegroup);
		//viewHolder.tvcomment.setText("("+comment+") Comments");
		
		}
//		else {
//			viewHolder = (ViewHolder) convertView.getTag();
//		}

		return convertView;
	}
	
	private static class ViewHolder {
		TextView tvdate;
		TextView tvcontent;
		TextView tvowner;
		TextView tvtypegroup;
		TextView tvcomment;
	}

}
