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

@SuppressWarnings("serial")
public class PostImplementation implements Serializable {

		private AnnotationConfiguration config;
		private SessionFactory factory;
		public List<Postdb> posts;
		public List<Commentdb> comments;
		private static PostImplementation instance = null;
		ConvertLists convertlist= ConvertLists.getInstance();

		private PostImplementation(){
			super();
			this.posts = new ArrayList<Postdb>();
			this.comments = new ArrayList<Commentdb>();
			
			config = new AnnotationConfiguration();
			config.addAnnotatedClass(Userdb.class);
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

	public List<Post> getActivityRecent(String username) {
				Date now = new Date();
				SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				String dnow =ft.format(now);
				Session session = factory.openSession();
				SQLQuery query = session.createSQLQuery("SELECT * FROM post p INNER JOIN user u WHERE p.user_USERNAME=u.USERNAME ORDER BY p.date DESC"); 
				query.addEntity(Postdb.class);
				session.beginTransaction();
				
				List<Post> p = new ArrayList<Post>();
				List<Postdb> postsdb = query.list();
				
				if(postsdb == null){
					throw new NotFoundException ("No hay actividad reciente.");
					
				}
				else{
					session.getTransaction().commit();
					
					
					for(Postdb postdb: postsdb){
						Post post = postdb.convertFromDB();
						
						if(postdb.getGrupo()!= null){
							for(Userdb userdb: postdb.getGrupo().getUser()){
								if(username.equals(userdb.getUsername()))
									post.setUser(postdb.getUser().convertFromDB());
									post.setGrupo(postdb.getGrupo().convertFromDB());
									post.setComment(convertlist.convertListComments(postdb.getComments()));
									p.add(post);
							}
						}
						else{
							for(Userdb userdb: postdb.getSubject().getUser()){
								if(username.equals(userdb.getUsername())){
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
		
		if(postquery == null){
			throw new NotFoundException("No se ha encontrado el post: "+ postid);
		}
			
		else{			
			session.close();
			return postquery;
		}
			
	}
	
}
