package edu.upc.eetac.ea.group1.pandora.api.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import edu.upc.eetac.ea.group1.pandora.api.MediaType;
import edu.upc.eetac.ea.group1.pandora.api.managers.CommentImplementation;
import edu.upc.eetac.ea.group1.pandora.api.models.Comment;
import edu.upc.eetac.ea.group1.pandora.api.models.Commentdb;


@Path("/posts")
public class PostResource {
	
	@GET
	@Path ("/{idpost}")
	public List<Comment> getComments(@PathParam("idpost") int postid){
		
		CommentImplementation commentImpl = CommentImplementation.getInstance();
		List<Comment> comments = commentImpl.getComments(postid);
		
		return comments;        
	}
	
	@POST
	@Path("/{idpost}")
	@Consumes(MediaType.PANDORA_API_COMMENT)
	public void addComments(@PathParam("idpost")int idpost,Commentdb comment ){
		System.out.println("Dentro del PostResource");
		CommentImplementation commentImpl = CommentImplementation.getInstance();
		int result = commentImpl.addComment(comment, idpost);
		
	}
}