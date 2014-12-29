package edu.upc.eetac.ea.group1.pandora.api.managers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.classic.Session;

import edu.upc.eetac.ea.group1.pandora.api.ConvertLists;
import edu.upc.eetac.ea.group1.pandora.api.models.Comment;
import edu.upc.eetac.ea.group1.pandora.api.models.Commentdb;
import edu.upc.eetac.ea.group1.pandora.api.models.Group;
import edu.upc.eetac.ea.group1.pandora.api.models.Groupdb;
import edu.upc.eetac.ea.group1.pandora.api.models.Notification;
import edu.upc.eetac.ea.group1.pandora.api.models.Notificationdb;
import edu.upc.eetac.ea.group1.pandora.api.models.Post;
import edu.upc.eetac.ea.group1.pandora.api.models.Postdb;
import edu.upc.eetac.ea.group1.pandora.api.models.Subject;
import edu.upc.eetac.ea.group1.pandora.api.models.Subjectdb;
import edu.upc.eetac.ea.group1.pandora.api.models.User;
import edu.upc.eetac.ea.group1.pandora.api.models.Userdb;

public class CommentImplementation implements Serializable{

	//Atributos Hibernate
			private AnnotationConfiguration config;
			private SessionFactory factory;
			//Atributos List que usaremos
			public List<Postdb> posts;
			public List<Commentdb> comments;
			//Intancia
			private static CommentImplementation instance = null;
			ConvertLists convertlist= ConvertLists.getInstance();
			UserImplementation userImpl= UserImplementation.getInstance();
			PostImplementation postImpl= PostImplementation.getInstance();
			private CommentImplementation(){
				super();
				this.posts = new ArrayList<Postdb>();
				this.comments = new ArrayList<Commentdb>();
				
				config = new AnnotationConfiguration();
				config.addAnnotatedClass(Userdb.class);//si queremos otra clase(tabla) le añadiriamos otra
				config.addAnnotatedClass(Groupdb.class);
				config.addAnnotatedClass(Subjectdb.class);
				config.addAnnotatedClass(Notificationdb.class);
				config.configure();
				factory = config.buildSessionFactory();
			}
			
			public static CommentImplementation getInstance(){
				if(instance==null){
					instance = new CommentImplementation();
				}
				return instance;
			}
			
	
	public Comment getComment(int idcomment) {

		Session session = factory.openSession();
		SQLQuery query = session.createSQLQuery("SELECT * FROM comment WHERE COMMENT_ID=:commentid");
		query.addEntity(Commentdb.class);
		query.setInteger(":commentid", idcomment);
		session.beginTransaction();
		Comment c = new Comment();
		Commentdb commentquery = (Commentdb) query.uniqueResult();
		
		if(commentquery== null){
			throw new NotFoundException("Comment "+idcomment+" no encontrado.");	
		}else{
			session.getTransaction().commit();
			
			//Obtenemos parametros del Commentdb y lo pasamos al Comment
			c.setId(commentquery.getId());
			c.setContent(commentquery.getContent());
			c.setUser(commentquery.getUser().convertFromDB());
			c.setPost(commentquery.getPost().convertFromDB());	
		}
		return c;
	}
	
	public List<Comment> getComments(int postid){
		
		Session session = factory.openSession();
		SQLQuery query = session.createSQLQuery("SELECT * FROM comment WHERE post_POST_ID=:postid");
		query.addEntity(Commentdb.class);
		query.setInteger(":postid", postid);
		session.beginTransaction();
		
		List<Commentdb> commentsdb = query.list();
		List<Comment> comments = new ArrayList<Comment>();
		
		for(Commentdb cdb: commentsdb){
			
			Comment c = new Comment();
			c.setContent(cdb.getContent());
			c.setId(cdb.getId());
			c.setPost(cdb.getPost().convertFromDB());
			c.setUser(cdb.getUser().convertFromDB());
			
			comments.add(c);
		}
		
		session.getTransaction().commit();
		session.close();
		
		return comments;
	}

	
	public int addComment(Commentdb comment,String owner, int postid) {
		//Validamos si el commentario cumple los requisitos.
		validateComment(comment);
		//Coger al dueño
		Userdb u = userImpl.searchUserDB(owner);
		System.out.println("Owner: "+u.getUsername());
		//Coger el post
		Postdb p = postImpl.searchPostDB(postid);
		System.out.println("Referencia Post: "+p.getId());
		try{
		//hibernate session
		SessionFactory factory = config.buildSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		
		//Guardo el comment
		session.save(comment);
		}catch(Exception e){
			return 0;
		}
		return 1;
	}



	
	public int updateComment(Commentdb comment, int idcomment) {
		
		validateComment(comment);
		
		//hibernate session
		SessionFactory factory = config.buildSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();	
		
		try{
			session.save(comment);
			session.getTransaction().commit();
		}catch(Exception e){
			session.close();
			return 0;
		}finally{
			session.close();
		}
		
		return 1;
	}

	
	public int deleteComment(int idcomment) {
		
		 Session session = factory.openSession();
		 try{
		 session.beginTransaction();//beginning of the transaction

		 Commentdb comment = (Commentdb)session.get(Commentdb.class, new Integer(idcomment));
		 
		 session.delete(comment);
		 
		 }catch(Exception e){
			 
			 return 0;
		 }
		 
		 session.getTransaction().commit();//end of transaction
		 session.close();//end of  session
		 return 1;
	}	
		
	private void validateComment(Commentdb comment) {
		if(comment.getContent()==null)
			throw new BadRequestException("Es obligatorio que haya un contenido.");
		if(comment.getContent().length()>254)
			throw new BadRequestException("Demasiados caracteres en el contenido ( >254 )");
		
	}
	
	public User getUser(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public User addUser(Userdb user) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public int updateUser(Userdb user, String username) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public int deleteUser(String username) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public Post getPost(int idpost) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public int updatePost(Postdb postt, int idpost) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public int deletePost(int idpost) {
		// TODO Auto-generated method stub
		return 0;
	}

	

	
	public Subject getSubject(int idSubject) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public int addSubject(Subjectdb subject) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public int updateSubject(Subjectdb subject, int idSubject) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public int deleteSubject(int idSubject) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public Group getGroup(String nameGroup) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public int addGroup(Groupdb group) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public int updateGroup(Groupdb group, String nameGroup) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public int deleteGroup(String nameGroup) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public Notification getNotification(String nameGroup) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public int addNotification(Groupdb group) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public int deleteNotification(String nameGroup) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public List<Post> getComments(String typeComment) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public List<Post> getActivityRecent(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public List<User> getParticipants(String nameGroup) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public List<User> searchUser(String search) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public List<Group> searchGroup(String search) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public User login(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public int deleteUserFromGroup(String username, String nameGroup) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public User addUserInGroup(Userdb user, String nameGroup) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public User addUserInSubject(Userdb user, String nameGroup) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public int addPost(Postdb post, int idSubject, int idGroup) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public List<Post> getCommentsFromSubject(int idSubject) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public List<Subject> searchSubject(String subject) {
		// TODO Auto-generated method stub
		return null;
	}

}
