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
import edu.upc.eetac.ea.group1.pandora.api.managers.SubjectImplementation;
import edu.upc.eetac.ea.group1.pandora.api.models.Post;
import edu.upc.eetac.ea.group1.pandora.api.models.Postdb;
import edu.upc.eetac.ea.group1.pandora.api.models.Subject;
import edu.upc.eetac.ea.group1.pandora.api.models.Subjectdb;
import edu.upc.eetac.ea.group1.pandora.api.models.User;
import edu.upc.eetac.ea.group1.pandora.api.models.Userdb;

@Path("/subjects")
public class SubjectResource {
	
	@GET
	public List<Subject> getSubjects()
	{
		List<Subject> s = new ArrayList<Subject>();
		SubjectImplementation impl = SubjectImplementation.getInstance();
		s= impl.getSubjects();
		
		return s;
	}
	
	@GET
	@Path("/search")
	public List<Subject> searchSubjects(@QueryParam("subject") String subject)
	{
		List<Subject> s = new ArrayList<Subject>();
		SubjectImplementation impl = SubjectImplementation.getInstance();
		s= impl.searchSubject(subject);
		
		return s;
	}
	
	@GET
	@Path("/searchById")
	public Subject searchSubjectsById(@QueryParam("idSubject") int idSubject)
	{
		Subject s = new Subject();
		SubjectImplementation impl = SubjectImplementation.getInstance();
		s= impl.searchSubjectById(idSubject);
		
		return s;
	}
	
	@GET
	@Path ("/{idsubject}")
	public Subject getSubject(@PathParam("idsubject") int idsubject) {

		SubjectImplementation impl = SubjectImplementation.getInstance();
	   Subject sub = impl.getSubject(idsubject);
	    	
	   return sub;
	        
	 }
	
	@GET
	@Path ("/{idsubject}/posts")
	public List<Post> getPostsFromSubject(@PathParam("idsubject") int idSubject)
	{
		List<Post> posts = new ArrayList<Post>();
		SubjectImplementation impl = SubjectImplementation.getInstance();
		posts= impl.getPostsFromSubject(idSubject);
		
		return posts;
	}
	
	@PUT
    @Path("/{idsubject}")
	@Consumes(MediaType.PANDORA_API_SUBJECT)
	public void updateSubject(@PathParam("subject") int idsubject, Subjectdb subject){
	    	

		SubjectImplementation impl = SubjectImplementation.getInstance();
	    impl.updateSubject( subject, idsubject);
	       
	}
	
	@DELETE
    @Path ("/{idsubject}")
    public void deleteUser (@PathParam("idsubject") int idsubject)
    { 
		SubjectImplementation impl = SubjectImplementation.getInstance();
   	    impl.deleteSubject(idsubject);	 
    }
	
	@POST
	@Consumes(MediaType.PANDORA_API_SUBJECT)
    public void addSubject(Subjectdb subject) {

		SubjectImplementation impl = SubjectImplementation.getInstance();
    	impl.addSubject(subject);       
    }
	
	@POST
	@Path ("/{idsubject}/posts")
	@Consumes(MediaType.PANDORA_API_POST)
    public int addPost(@PathParam("idsubject") int idSubject, Postdb post) {
    	
		SubjectImplementation impl =  SubjectImplementation.getInstance();
    	return impl.addPost(post, idSubject);
    }
	
	
}
