package edu.upc.eetac.ea.group1.pandora.android.api;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import edu.upc.eetac.ea.group1.pandora.android.R;
import edu.upc.eetac.ea.group1.pandora.android.api.model.ScheduleDaySubject;

public class AdapterToSchedule extends BaseAdapter{

	private List<ScheduleDaySubject>  data;
	private LayoutInflater inflater;
	
	public AdapterToSchedule(Context context,List<ScheduleDaySubject> data){
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
		System.out.println("En el adapter");
		convertView = inflater.inflate(R.layout.schedule_detail, null);	
			ViewHolder viewHolder = null;
				viewHolder = new ViewHolder();
				viewHolder.tvday = (TextView) convertView
						.findViewById(R.id.tvday);
				viewHolder.tvsubject = (TextView) convertView
						.findViewById(R.id.tvsubject);
				viewHolder.tvtime = (TextView) convertView
						.findViewById(R.id.tvtime);
				viewHolder.tvday.setText(data.get(position).getDay());
				viewHolder.tvsubject.setText(data.get(position).getSubject());
				viewHolder.tvtime.setText(data.get(position).getTime());
			
			return convertView;
	}
	private static class ViewHolder {
		TextView tvday;
		TextView tvsubject;
		TextView tvtime;
	}
	

}
