package edu.upc.eetac.ea.group1.pandora.api.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import edu.upc.eetac.ea.group1.pandora.api.managers.UserImplementation;
import edu.upc.eetac.ea.group1.pandora.api.models.Notification;

@Path("/notifications")
public class NotificationResource {
	
	@GET
	public List<Notification> getNotifications()
	{
		List<Notification> n = new ArrayList<Notification>();
		UserImplementation impl = UserImplementation.getInstance();
		n= impl.getNotifications();
		
		return n;
	}
	

}
