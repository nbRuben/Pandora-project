package edu.upc.eetac.ea.group1.pandora.api.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import edu.upc.eetac.ea.group1.pandora.api.MediaType;
import edu.upc.eetac.ea.group1.pandora.api.managers.GroupImplementation;
import edu.upc.eetac.ea.group1.pandora.api.managers.PostImplementation;
import edu.upc.eetac.ea.group1.pandora.api.models.Group;
import edu.upc.eetac.ea.group1.pandora.api.models.Groupdb;


@Path("/groups")
public class GroupResource {
	
	@GET
	@Path ("/{group}")
	public Group getGroup(@PathParam("group") String group) {
	    	
		GroupImplementation groupImpl = GroupImplementation.getInstance();
	    Group g = groupImpl.getGroup(group);
	    	
	   return g;
	        
	}
	
	@GET
	@Path ("/users/{user}")
	public List<Group> getGroupbyUser(@PathParam("user") String username) {
	    	
		GroupImplementation groupImpl = GroupImplementation.getInstance();
	    List<Group> groups = groupImpl.getGroupsByUser(username);
	    	
	   return groups;
	        
	}
	
	@POST
	@Consumes(MediaType.PANDORA_API_GROUP)
    public int addGroup(Groupdb group) {
    	
		GroupImplementation groupImpl = GroupImplementation.getInstance();
		return groupImpl.addGroup(group);       
    }

	@POST
	@Path ("/{user}/{groupid}")
    public int addUserToGroup(@PathParam("groupid") String groupid,@PathParam("user") String username) {
    	
		GroupImplementation groupImpl = GroupImplementation.getInstance();
		return groupImpl.addUserToGroup(groupid, username);       
    }
	
	@PUT
    @Path("/{grupoid}")
	@Consumes(MediaType.PANDORA_API_GROUP)
	public int updateGroup (@PathParam("grupoid") String groupid, Groupdb groups){
	    	
		GroupImplementation groupImpl = GroupImplementation.getInstance();
		return groupImpl.updateGroup(groups, groupid);
		
	}
	
	@DELETE
    @Path ("/{group}")
    public int deleteGroup (@PathParam("group") String groupid)
    { 
		GroupImplementation groupImpl = GroupImplementation.getInstance();
		return groupImpl.deleteGroup(groupid);	 
    }
	
	@DELETE
    @Path ("/{user}/{groupid}")
    public int exitFromGroup (@PathParam("groupid") String groupid,@PathParam("user") String username)
    { 
		GroupImplementation groupImpl = GroupImplementation.getInstance();
		return groupImpl.exitFromGroup(groupid,username);	 
    }
	
	@DELETE
	@Path ("/posts/{id}")
	public int  addPostToGroup(@PathParam("id") String postid) {
    	
    	PostImplementation postImpl =  PostImplementation.getInstance();
    	return postImpl.deletePostFromGroup(postid);       
    }
	

}
