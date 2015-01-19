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


import javax.ws.rs.QueryParam;

import edu.upc.eetac.ea.group1.pandora.api.MediaType;
import edu.upc.eetac.ea.group1.pandora.api.managers.GroupImplementation;
import edu.upc.eetac.ea.group1.pandora.api.managers.PostImplementation;
import edu.upc.eetac.ea.group1.pandora.api.managers.SubjectImplementation;
import edu.upc.eetac.ea.group1.pandora.api.models.Group;
import edu.upc.eetac.ea.group1.pandora.api.models.Groupdb;
import edu.upc.eetac.ea.group1.pandora.api.models.Post;
import edu.upc.eetac.ea.group1.pandora.api.models.Postdb;
import edu.upc.eetac.ea.group1.pandora.api.models.Subject;
import edu.upc.eetac.ea.group1.pandora.api.models.User;

@Path("/groups")
public class GroupResource {
	
	@GET
	public List<Group> getGroups()
	{
		List<Group> subs = new ArrayList<Group>();
		GroupImplementation impl = GroupImplementation.getInstance();
		subs= impl.getGroups();
		
		return subs;
	}
	
	@GET
	@Path ("/{group}")
	public Group getGroup(@PathParam("group") String group) {
	    	
		Group sub = new Group();
		GroupImplementation impl =  GroupImplementation.getInstance();
	   sub = impl.getGroup(group);
	    	
	   return sub;
	        
	 }
	@GET
	@Path("/user/search")
	public List<User> searchUsers(@QueryParam("user") String user)
	{
		List<User> u = new ArrayList<User>();
		GroupImplementation impl = GroupImplementation.getInstance();
		u = impl.searchUser(user);
		
		return u;
	}
	@GET
	@Path ("/users/{user}")
	public List<Group> getGroupbyUser(@PathParam("user") String username) {
	    	
		List<Group> groups = new ArrayList<Group>();
		GroupImplementation impl =  GroupImplementation.getInstance();
	   groups = impl.getGroupsByUser(username);
	    	
	   return groups;
	        
	 }
	
	@GET
	@Path ("/posts/{id}")
	public List<Post> getPostsByUser(@PathParam("id") int id) {
	    	
		List<Post> post = new ArrayList<Post>();
		GroupImplementation impl =  GroupImplementation.getInstance();
	   post = impl.getPostsByGroup(id);
	    	
	   return post;
	        
	 }
	
	@GET
	@Path ("/{groupid}/users")
	public List<User> getUsersByGroup(@PathParam("groupid") int groupid) {
	    	
		List<User> users = new ArrayList<User>();
		GroupImplementation impl =  GroupImplementation.getInstance();
		users = impl.getUsersByGroup(groupid);
	   return users;
	        
	 }
	
	@POST
	@Consumes(MediaType.PANDORA_API_GROUP)
    public Group addGroup(Groupdb group) {
    	
		GroupImplementation impl =  GroupImplementation.getInstance();
    	Group id = impl.addGroup(group);
    	return id;
    }
	
	@PUT
    @Path("/{groupid}")
	@Consumes(MediaType.PANDORA_API_GROUP)
	public void updateGroup (@PathParam("groupid") String groupid, Groupdb group){
	    	
		GroupImplementation impl =  GroupImplementation.getInstance();
	    impl.updateGroup(group, groupid);
	       
	}
	
	@POST
	@Path ("/{user}/{groupid}")
    public void addUserToGroup(@PathParam("groupid") String groupid,@PathParam("user") String username) {
    	
		GroupImplementation impl =  GroupImplementation.getInstance();
    	impl.addUserToGroup(groupid, username);       
    }
	
	@POST
	@Path ("/notification/{groupid}/{username}")
    public void addInvitationNotification(@PathParam("groupid") int groupid,@PathParam("username") String username) {
    	
		GroupImplementation impl =  GroupImplementation.getInstance();
    	impl.addNotification(groupid,username);       
    }
	@POST
	@Path ("/{idgroup}/posts")
	@Consumes(MediaType.PANDORA_API_POST)
    public int addPost(@PathParam("idgroup") int idgroup, Postdb post) {
    	
		GroupImplementation impl =  GroupImplementation.getInstance();
    	return impl.addPost(post, idgroup);
    }
	@DELETE
	@Path ("/posts/{id}")
	public void delPostToGroup(@PathParam("id") String postid) {
    	
		GroupImplementation impl =  GroupImplementation.getInstance();
    	impl.deletePostFromGroup(postid);       
    }

	@DELETE
    @Path ("/{groupid}")
    public void deleteGroup (@PathParam("groupid") String groupid)
    { 
		GroupImplementation impl =  GroupImplementation.getInstance();
   	    impl.deleteGroup(groupid);	 
    }
	
	@DELETE
    @Path ("/{user}/{groupid}")
    public void exitFromGroup (@PathParam("groupid") String groupid,@PathParam("user") String username)
    { 
		GroupImplementation impl =  GroupImplementation.getInstance();
   	    impl.exitFromGroup(groupid,username);	 
    }

}
