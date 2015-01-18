package edu.upc.eetac.ea.group1.pandora.api.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import edu.upc.eetac.ea.group1.pandora.api.MediaType;
import edu.upc.eetac.ea.group1.pandora.api.managers.ManagerImplementation;
import edu.upc.eetac.ea.group1.pandora.api.managers.PostImplementation;
import edu.upc.eetac.ea.group1.pandora.api.managers.UserImplementation;
import edu.upc.eetac.ea.group1.pandora.api.models.Group;
import edu.upc.eetac.ea.group1.pandora.api.models.Groupdb;
import edu.upc.eetac.ea.group1.pandora.api.models.Notification;
import edu.upc.eetac.ea.group1.pandora.api.models.Notificationdb;
import edu.upc.eetac.ea.group1.pandora.api.models.Post;
import edu.upc.eetac.ea.group1.pandora.api.models.Subject;
import edu.upc.eetac.ea.group1.pandora.api.models.User;
import edu.upc.eetac.ea.group1.pandora.api.models.Userdb;


@Path("/users")
public class UserResource {
	
	@GET
	@Path ("/{user}")
	public User getUser(@PathParam("user") String username) {
	    	
	   UserImplementation userImpl =  UserImplementation.getInstance();
	   User us = userImpl.getUser(username);
	    	
	   return us;
	        
	}

	@GET
	@Path("/login")
	@Consumes(MediaType.PANDORA_API_USER)
	@Produces(MediaType.PANDORA_API_USER)
	public User login(@QueryParam("username") String username, @QueryParam("password") String password){
		
		UserImplementation userImpl =  UserImplementation.getInstance();
		User u = userImpl.login(username, password);
		return u;
	}
	
	@PUT
    @Path("/{user}")
	@Consumes(MediaType.PANDORA_API_USER)
	public void updateUser(@PathParam("user") String username, Userdb user){
	    	
		   UserImplementation userImpl =  UserImplementation.getInstance();
	    userImpl.updateUser(user, username);
	       
	}
	
	@GET
	@Path ("/{user}/notifications")
	public List<Notification> getNotifications(@PathParam("user") String username)
	{
		List<Notification> n = new ArrayList<Notification>();
		UserImplementation impl = UserImplementation.getInstance();
		n= impl.getNotifications(username);
		
		return n;
	}
	
	@PUT
	@Path ("/{user}/notifications/{idNotification}")
	@Consumes(MediaType.PANDORA_API_NOTIFICATION)
	public void updateNotifications(Notificationdb notification)
	{
		UserImplementation impl = UserImplementation.getInstance();
		impl.updateNotification(notification);
		
	}
	
	@POST
	@Consumes(MediaType.PANDORA_API_USER)
    public User addUser(Userdb user) {
		UserImplementation userImpl =  UserImplementation.getInstance();
    	return userImpl.addUser(user);       
    }
	
	@GET
	@Path("/{user}/subjects")
	public List<Subject> getSubjects(@PathParam ("user") String username)
	{
		List<Subject> subs = new ArrayList<Subject>();
		UserImplementation impl = UserImplementation.getInstance();
		subs= impl.getUserSubjects(username);
		
		return subs;
	}
	
	@GET
	@Path("/{user}/activityrecent")
	public List<Post> getActivityRecent(@PathParam ("user") String username){
		
		PostImplementation postImpl = PostImplementation.getInstance();
		List<Post> p = postImpl.getActivityRecent(username);
		
		return p;
	}
	
	@POST
	@Path("/{user}/subjects/{idSubject}")
	@Consumes(MediaType.PANDORA_API_USER)
	public void addSubjectToUser(@PathParam("user") String username, @PathParam("idSubject") int idSubject) {
    	
		UserImplementation impl =  UserImplementation.getInstance();
    	impl.addSubjectToUser(username, idSubject);       
	}
	
	@DELETE
	@Path("/{user}/subjects/{idSubject}")
	public void deleteSubjecFromUser(@PathParam("user") String username, @PathParam("idSubject") int idSubject) {
		UserImplementation impl =  UserImplementation.getInstance();
    	impl.deleteSubjectFromUser(username, idSubject);       
	}
    
}
