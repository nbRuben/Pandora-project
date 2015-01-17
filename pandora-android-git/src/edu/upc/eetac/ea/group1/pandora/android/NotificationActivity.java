package edu.upc.eetac.ea.group1.pandora.android;

import java.util.ArrayList;

import edu.upc.eetac.ea.group1.pandora.android.api.NotificationAdapter;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Notification;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NotificationActivity extends ListActivity {

	private NotificationAdapter adapter;
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notifications);
		ArrayList<Notification> notifications = (ArrayList<Notification>) getIntent().getSerializableExtra("myNotifications");
		printNotifications(notifications);
	}
	
	private void printNotifications(ArrayList<Notification> notifications){
		adapter = new NotificationAdapter(this, notifications, (String) getIntent().getExtras().get("username"));
		setListAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	public void goHome(View v){
		Intent intent = new Intent(getApplicationContext(),
				MainActivity.class);
		intent.putExtra("username", (String) getIntent().getExtras().get("username"));
		startActivity(intent);
	}
	
	
}
