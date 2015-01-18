package edu.upc.eetac.ea.group1.pandora.api.managers;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

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
public class ManagerImplementation implements  Serializable{
	
	private AnnotationConfiguration config;
	private SessionFactory factory;
	public List<Userdb> users;
	public List<Groupdb> groups;
	public List<Subjectdb> subjects;
	private static ManagerImplementation instance = null;

	private ManagerImplementation(){
		super();
		
		this.users = new ArrayList<Userdb>();
		this.groups = new ArrayList<Groupdb>();
		this.subjects = new ArrayList<Subjectdb>();
		
		config = new AnnotationConfiguration();
		config.addAnnotatedClass(Userdb.class);
		config.addAnnotatedClass(Groupdb.class);
		config.addAnnotatedClass(Subjectdb.class);
		config.addAnnotatedClass(Notificationdb.class);
		config.configure();
		factory = config.buildSessionFactory();
	}
	
	public static ManagerImplementation getInstance(){
		if(instance==null){
			instance = new ManagerImplementation();
		}
		return instance;
	}
	
    public String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
       
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    
	public User getUser(String user) {
		
		Session session = factory.openSession();
		SQLQuery query = session.createSQLQuery("SELECT * FROM user WHERE username = :user");
		query.addEntity(Userdb.class);
		query.setString("user", user);
		session.beginTransaction();
		User u = new User();
		Userdb userquery = (Userdb) query.uniqueResult();
		
		if(userquery == null)
			throw new NotFoundException ("Usuario "+ user + " no encontrado");
		else{
			session.getTransaction().commit();
			String username = userquery.getUsername();
			String pass = userquery.getUserpass();
			String email = userquery.getEmail();
			String name = userquery.getName();
			String surname = userquery.getSurname();
			List <Postdb> posts = userquery.getPost();
			List <Groupdb> group = userquery.getGrupo();
			List <Subjectdb> subect = userquery.getSubject();
						
			List<Post> postsdb = convertListPosts(posts);
			List<Group> groupsdb = convertListGroups(group);
			List<Subject> subjectsdb = convertListSubjects(subect);
			
			u.setUsername(username);
			u.setUserpass(pass);
			u.setEmail(email);
			u.setName(name);
			u.setSurname(surname);
			u.setPosts(postsdb);
			u.setGroups(groupsdb);
			u.setSubjects(subjectsdb);
		}
		return u;
	}

	
	public User addUser(Userdb user) {
		validateUser(user,0);
		
		SessionFactory factory = config.buildSessionFactory();
		Session sesion = factory.getCurrentSession();
		sesion.beginTransaction();	
		
		sesion.save(user);
		sesion.getTransaction().commit();
		
		return null;
	}

	
	public int updateUser(Userdb user, String username) {
		
		validateUser(user,1);
		SessionFactory factory = config.buildSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();	
		
		try{
			session.save(user); 
			session.getTransaction().commit();	
		}catch(Exception e){
			
			session.close();
			return 0;
			
		}finally{
			session.close();	
		}
		return 1;
	}
	
	public Subject getSubject(int id) {
		
		Session session = factory.openSession();
		SQLQuery query = session.createSQLQuery("SELECT * FROM subject WHERE SUBJECT_ID = :idsubject");
		query.addEntity(Subjectdb.class);
		query.setInteger("idsubject", id);
		session.beginTransaction();
		Subject s = new Subject();
		Subjectdb subjectquery = (Subjectdb) query.uniqueResult();
		
		if (subjectquery == null)
				throw new NotFoundException ("Subject con ID " + id + " no encontrado");
		else {
			
			session.getTransaction().commit();
			
			String subjectName = subjectquery.getName();
			int subjectID = subjectquery.getId();
			List<Userdb> subjectUsers = subjectquery.getUser(); 
			List<Postdb> subjectPosts = subjectquery.getPost();
			List<Notificationdb> subjectNotifications = subjectquery.getNotification();
			
			List<User> subjectUsersdb = convertListUsers(subjectUsers); 
			List<Post> subjectPostdb = convertListPosts(subjectPosts);
			List<Notification> subjectNotificationdb = convertListNotifications(subjectNotifications);
			
			s.setName(subjectName);
			s.setId(subjectID);
			s.setUser(subjectUsersdb);
			s.setPost(subjectPostdb);
			s.setNotification(subjectNotificationdb);
		}
	
		return s;
	}
	
	public List<Post> getActivityRecent(String username) {
		Date now = new Date();
		System.out.println("Fecha (Date): "+now);
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String dnow =ft.format(now);
		Session session = factory.openSession();
		SQLQuery query = session.createSQLQuery("SELECT * FROM post p INNER JOIN user u WHERE p.user_USERNAME=u.USERNAME AND DATE<:date");
		query.addEntity(Postdb.class);
		query.setString("date", dnow);
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
							p.add(post);
					}
				}
				else{
					for(Userdb userdb: postdb.getSubject().getUser()){
						if(username.equals(userdb.getUsername())){
							post.setUser(postdb.getUser().convertFromDB());
							post.setSubject(postdb.getSubject().convertFromDB());
							p.add(post); 
						}
					}
				}
				
			}
		}
			
		return p;
	}
	
		public List<Post> convertListPosts( List<Postdb> postsdb){
			
			List<Post> posts = new ArrayList<Post>();
			for (Postdb postdb : postsdb){

				Post post = postdb.convertFromDB();
				posts.add(post);
			}
			
			return posts;
		}
		public List<Group> convertListGroups( List<Groupdb> groupsdb){
			
			List<Group> groups = new ArrayList<Group>();
			for (Groupdb groupdb : groupsdb){

				Group group = groupdb.convertFromDB();
				groups.add(group);
			}
			
			return groups;
		}
		public List<Subject> convertListSubjects( List<Subjectdb> subjectsdb){
			
			List<Subject> subjects = new ArrayList<Subject>();
			for (Subjectdb subjectdb : subjectsdb){

				Subject subject = subjectdb.convertFromDB();
				subjects.add(subject);
			}
			
			return subjects;
		}
		
		public List<User> convertListUsers(List<Userdb> usersdb){
			
			List<User> users = new ArrayList<User>();
			for (Userdb userdb : usersdb){
				
				User user = userdb.convertFromDB();
				users.add(user);
			}
			return users;
		}
		
		public List<Notification> convertListNotifications(List<Notificationdb> notificationsdb){
			
			List<Notification> notifications = new ArrayList<Notification>();
			for (Notificationdb notificationdb : notificationsdb){
				
				Notification notification = notificationdb.convertFromDB();
				notifications.add(notification);
			}
			return notifications;
		}

		private void validateUser(Userdb user, int op) {
			
			if(user.getUsername()==null)
				throw new BadRequestException("Es obligatoria llenar el campo Username.");
			if(user.getUsername().length()>254)
				throw new BadRequestException("Demasiados caracteres para tu Username, no puede superrar los 254 caracteres.");
			if(user.getUserpass()==null)
				throw new BadRequestException("Es obligatoria llenar el campo Password.");
			if(user.getUserpass().length()>254)
				throw new BadRequestException("Demasiados caracteres para tu Password, no puede superrar los 254 caracteres.");
			if(user.getEmail().length()>254)
				throw new BadRequestException("Demasiados caracteres para tu Email, no puede superrar los 254 caracteres.");
			if(user.getName()==null)
				throw new BadRequestException("Es obligatoria llenar el campo Name.");
			if(user.getName().length()>254)
				throw new BadRequestException("Demasiados caracteres para tu Name, no puede superrar los 254 caracteres.");
			if(op==0){
				if(searchUserInDB(user.getUsername())!=0)
					throw new BadRequestException("Usuario "+ user.getUsername()+" ya existe.");
			}
		}
		
		private void validateSubject(Subjectdb subject, int op) {
			if(subject.getName()==null)
				throw new BadRequestException("Es obligatoria llenar el campo Name.");
			if(subject.getName().length()>254)
				throw new BadRequestException("Demasiados caracteres para tu Name, no puede superrar los 254 caracteres.");
			if(op==0){
				if(searchSubjectInDB(subject.getId())!=0)
					throw new BadRequestException("Subject "+ subject.getName()+" ya existe.");
			}
		}
		
		private int searchSubjectInDB(int id) {
			
			Session session = factory.openSession();
			SQLQuery query = session.createSQLQuery("Selet * FROM subject WHERE SUBJECT_ID = :id");
			query.addEntity(Subjectdb.class);
			query.setInteger("id", id);
			session.beginTransaction();
			Subjectdb subjectquery = (Subjectdb) query.uniqueResult();
			session.getTransaction().commit();
			
			if (subjectquery == null){
				session.close();
				return 1;
			}
			else {
				session.close();
				return 1;
			}
		
		}

		private int searchUserInDB(String username){
			
			Session session = factory.openSession();
			SQLQuery query = session.createSQLQuery("SELECT * FROM user WHERE username = :user");
			query.addEntity(Userdb.class);
			query.setString("user", username);
			session.beginTransaction();
			Userdb userquery = (Userdb) query.uniqueResult();
			session.getTransaction().commit();
			
			if(userquery == null){
				session.close();
				return 1;
			}
				
			else{			
				session.close();
				return 0;
			}
				
		}

}
