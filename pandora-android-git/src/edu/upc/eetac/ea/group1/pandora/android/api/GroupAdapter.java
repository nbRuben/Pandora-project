package edu.upc.eetac.ea.group1.pandora.android.api;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import edu.upc.eetac.ea.group1.pandora.android.R;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Group;

public class GroupAdapter extends BaseAdapter{
	
	private List<Group> data;
	private LayoutInflater inflater;
	private int poss = 0;
	
	public GroupAdapter(Context context,List<Group> data) {
		super();
		inflater = LayoutInflater.from(context);
		this.data = data;
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
		return Long.parseLong(((Group)getItem(position)).getId());
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		
		ViewHolder viewHolder=null;
		if (convertView == null) {			
		convertView = inflater.inflate(R.layout.group_detail,null);
		viewHolder = new ViewHolder();
		viewHolder.tvgrupo=(TextView) convertView.findViewById(R.id.tvgrupo);
		viewHolder.tvid=(TextView) convertView.findViewById(R.id.tvid);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String grupo = data.get(position).getName();
		String id = (data.get(position).getId());
		viewHolder.tvgrupo.setText(grupo);
		viewHolder.tvid.setText(id);
		return convertView;
	}
	private static class ViewHolder {
		TextView tvgrupo;
		TextView tvid;
	}
}
