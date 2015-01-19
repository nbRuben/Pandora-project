package edu.upc.eetac.ea.group1.pandora.android.api;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import edu.upc.eetac.ea.group1.pandora.android.R;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Document;

public class DocumentAdapter extends BaseAdapter{

	private ArrayList<Document> data;
	private LayoutInflater inflater;
	public Context context;
	
	public DocumentAdapter(Context context, ArrayList<Document> data) {
		super();
		this.context = context;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.document_detail, null);
			viewHolder = new ViewHolder();
			viewHolder.tvDocumentName = (TextView) convertView
					.findViewById(R.id.tvDocumentName);
			viewHolder.tvUsername = (TextView) convertView
					.findViewById(R.id.tvUsername);
			
			viewHolder.tvDocumentName.setText(data.get(position).getName());
			viewHolder.tvUsername.setText(data.get(position).getUsername());
			
		}
		return convertView;
	}

	private static class ViewHolder {
		TextView tvDocumentName;
		TextView tvUsername;
	}


}
