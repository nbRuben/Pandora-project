package edu.upc.eetac.ea.group1.pandora.api.managers;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

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

public class PostImplementation implements Serializable {

	//Atributos Hibernate
		private AnnotationConfiguration config;
		private SessionFactory factory;
		//Atributos List que usaremos
		public List<Postdb> posts;
		public List<Commentdb> comments;
		//Intancia
		private static PostImplementation instance = null;
		ConvertLists convertlist= ConvertLists.getInstance();

		private PostImplementation(){
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
		
		public static PostImplementation getInstance(){
			if(instance==null){
				instance = new PostImplementation();
			}
			return instance;
		}

	
	public Post getPost(int idpost) {
		// TODO Auto-generated method stub
		return null;
	}
	public Postdb getPostdb(int idpost) {

		Session session = factory.openSession();
		SQLQuery query = session.createSQLQuery("SELECT * FROM post WHERE POST_ID = :idpost");
		query.addEntity(Postdb.class);
		query.setInteger("idpost", idpost);
		session.beginTransaction();
		Postdb postquery=(Postdb) query.uniqueResult();
		
		session.close();
		return postquery;
	}

	
	public int updatePost(Postdb postt, int idpost) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public int deletePost(int idpost) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public List<Post> getActivityRecent(String username) {
		//Transformar el formato del date, introducirlo segun el usuario.
				Date now = new Date();
				System.out.println("Fecha (Date): "+now);
				SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				String dnow =ft.format(now);
				System.out.println("Fecha (SimpleDateFormat): "+ft.format(now) );
				System.out.println("Fecha (String): "+dnow );
				Session session = factory.openSession();
				SQLQuery query = session.createSQLQuery("SELECT * FROM post p INNER JOIN user u WHERE p.user_USERNAME=u.USERNAME"); //AND DATE<:date");
				query.addEntity(Postdb.class);
				//query.setString("date", dnow);
				session.beginTransaction();
				
				List<Post> p = new ArrayList<Post>();
				List<Postdb> postsdb = query.list();
				
				if(postsdb == null){
					System.out.println("No hay actividad reciente.");
					throw new NotFoundException ("No hay actividad reciente.");
					
				}
				else{
					session.getTransaction().commit();
					
					
					for(Postdb postdb: postsdb){
						Post post = postdb.convertFromDB();
						System.out.println("ID del post: "+postdb.getId());
						
						if(postdb.getGrupo()!= null){
							System.out.println("Dentro de un grupo.");
							for(Userdb userdb: postdb.getGrupo().getUser()){
								System.out.println("Revisando si el user esta en el posible grupo");
								if(username.equals(userdb.getUsername()))
									System.out.println("Post de grupo metido.");
									System.out.println("User propietario: "+postdb.getUser().getUsername());
									post.setUser(postdb.getUser().convertFromDB());
									post.setGrupo(postdb.getGrupo().convertFromDB());
									post.setComment(convertlist.convertListComments(postdb.getComments()));
									p.add(post);
							}
						}
						else{
							System.out.println("Estamos dentro de una asignatura.");
							for(Userdb userdb: postdb.getSubject().getUser()){
								System.out.println("Revisando si el user esta en el posible asigantura");
								System.out.println("Userdb.getUsername()="+userdb.getUsername()+" username="+username);
								if(username.equals(userdb.getUsername())){
									System.out.println("Dentro hemos encontrado el usuario. PostID= "+ post.getId());
									System.out.println("Post de asignatura metido");
									System.out.println("User propietario: "+postdb.getUser().getUsername());
									post.setUser(postdb.getUser().convertFromDB());
									post.setSubject(postdb.getSubject().convertFromDB());
									post.setComment(convertlist.convertListComments(postdb.getComments()));
									p.add(post); 
								}
							}
						}
						
					}
				}
					
				return p;
	}
	
	public int addPostToGroup(Postdb post){
		return 0;
	}
	public int deletePostFromGroup(String postid){
		int id = Integer.valueOf(postid);
		Session session = factory.openSession();
		 try{
			 
		 session.beginTransaction();
		 Postdb postquery = (Postdb) session.get(Postdb.class, id);
		 session.delete(postquery);
		 }catch(Exception e){
			 
			 return 0;
		 }
		 session.getTransaction().commit();
		 session.close();
		return 1;
	}
	
	public Postdb searchPostDB(int postid){
		
		Session session = factory.openSession();
		SQLQuery query = session.createSQLQuery("SELECT * FROM post WHERE POST_ID = :postid");
		query.addEntity(Userdb.class);
		query.setInteger("postid", postid);
		session.beginTransaction();
		Postdb postquery = (Postdb) query.uniqueResult();
		session.getTransaction().commit();
		
		if(postquery == null){//No existe ningun user con ese username
			throw new NotFoundException("No se ha encontrado el post: "+ postid);
		}
			
		else{			
			session.close();
			return postquery;
		}
			
	}
	
}
