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

public class ManagerImplementation implements  Serializable{
	
	//Atributos Hibernate
	private AnnotationConfiguration config;
	private SessionFactory factory;
	//Atributos List que usaremos
	public List<Userdb> users;
	public List<Groupdb> groups;
	public List<Subjectdb> subjects;
	//Intancia
	private static ManagerImplementation instance = null;
	
	
	
	private ManagerImplementation(){
		super();
		
		this.users = new ArrayList<Userdb>();
		this.groups = new ArrayList<Groupdb>();
		this.subjects = new ArrayList<Subjectdb>();
		
		config = new AnnotationConfiguration();
		config.addAnnotatedClass(Userdb.class);//si queremos otra clase(tabla) le añadiriamos otra
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
			
			//Obtenemos parametros del user y lo pasamos al Userdb
			String username = userquery.getUsername();
			String pass = userquery.getUserpass();
			String email = userquery.getEmail();
			String name = userquery.getName();
			String surname = userquery.getSurname();
			List <Postdb> posts = userquery.getPost();
			List <Groupdb> group = userquery.getGrupo();
			List <Subjectdb> subect = userquery.getSubject();
						
			//Cambiamos los comments, groups y subjects a db
			List<Post> postsdb = convertListPosts(posts);
			List<Group> groupsdb = convertListGroups(group);
			List<Subject> subjectsdb = convertListSubjects(subect);
			
			//Metemos la info al Userdb
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
		
		//Primero de todo comprobamos que los datos sean correctos.
		validateUser(user,0);
		
		//hibernate session
		SessionFactory factory = config.buildSessionFactory();
		Session sesion = factory.getCurrentSession();
		sesion.beginTransaction();	
		
		//Guardo User
		sesion.save(user);
		sesion.getTransaction().commit();
		
		
		return null;
	}

	
	public int updateUser(Userdb user, String username) {
		
		validateUser(user,1);
		
		//hibernate session
		SessionFactory factory = config.buildSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();	
		
		try{
			session.save(user); //guardo user
			session.getTransaction().commit();	
			
		}catch(Exception e){
			
			session.close();
			return 0;
			
		}finally{
			
			session.close();	
			
		}
		
		return 1;
	}

	
	public int deleteUser(String username) {

		//Primero de todo eliminamos los comentarios del usuario y el usuario en los grupos.
		
		return 0;
	}

	
	public Post getPost(int idpost) {
		// TODO Auto-generated method stub
		return null;
	}


	
	public int updatePost(Postdb post, int idpost) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public int deletePost(int idpost) {
		// TODO Auto-generated method stub
		return 0;
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
			
			//Obtenemos parametros del subject y los parseamos a Userdb
			
			String subjectName = subjectquery.getName();
			int subjectID = subjectquery.getId();
			List<Userdb> subjectUsers = subjectquery.getUser(); 
			List<Postdb> subjectPosts = subjectquery.getPost();
			List<Notificationdb> subjectNotifications = subjectquery.getNotification();
			
			//cambiamos las listas a db
			
			List<User> subjectUsersdb = convertListUsers(subjectUsers); 
			List<Post> subjectPostdb = convertListPosts(subjectPosts);
			List<Notification> subjectNotificationdb = convertListNotifications(subjectNotifications);
			
			//Metemos la info al Subjectdb
			
			s.setName(subjectName);
			s.setId(subjectID);
			s.setUser(subjectUsersdb);
			s.setPost(subjectPostdb);
			s.setNotification(subjectNotificationdb);
		}
	
		return s;
	}

	
	public int addSubject(Subjectdb subject) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public int updateSubject(Subjectdb subject, int idsubject) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public int deleteSubject(int idsubject) {
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

	public List<Groupdb> getGroups() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Subject> getSubjects() {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateSubject(String subject, Subject subjects2) {
		// TODO Auto-generated method stub
		
	}

	public void updateGroup(String group, Groupdb groups2) {
		// TODO Auto-generated method stub
		
	}

	public void updateUser(User user, String username) {
		// TODO Auto-generated method stub
		
	}

	public List<Subject> getUserSubjects() {
		// TODO Auto-generated method stub
		return null;
	}

	public Subject getUserSubject(String username, int subject) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Group> getUserGroups() {
		// TODO Auto-generated method stub
		return null;
	}

	public Group getUserGroup(String username, int group) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public Comment getComment(int idcomment) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public int addComment(Commentdb comment,String owner, int postid) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public int updateComment(Commentdb postt, int idcomment) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public int deleteComment(int idcomment) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public List<Post> getActivityRecent(String username) {
		//Transformar el formato del date, introducirlo segun el usuario.
		Date now = new Date();
		System.out.println("Fecha (Date): "+now);
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String dnow =ft.format(now);
		System.out.println("Fecha (SimpleDateFormat): "+ft.format(now) );
		System.out.println("Fecha (String): "+dnow );
		Session session = factory.openSession();
		SQLQuery query = session.createSQLQuery("SELECT * FROM post p INNER JOIN user u WHERE p.user_USERNAME=u.USERNAME AND DATE<:date");
		query.addEntity(Postdb.class);
		query.setString("date", dnow);
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
							p.add(post); 
						}
					}
				}
				
			}
		}
			
		return p;
	}
	
	//Esto m lo he sacado yo de la manga------------------------------------------
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
				if(searchUserInDB(user.getUsername())!=0)//Miramos si el user ya existe
					throw new BadRequestException("Usuario "+ user.getUsername()+" ya existe.");
			}
		}
		
		private void validateSubject(Subjectdb subject, int op) {
			if(subject.getName()==null)
				throw new BadRequestException("Es obligatoria llenar el campo Name.");
			if(subject.getName().length()>254)
				throw new BadRequestException("Demasiados caracteres para tu Name, no puede superrar los 254 caracteres.");
			if(op==0){
				if(searchSubjectInDB(subject.getId())!=0)//Miramos si el subject ya existe
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
			
			if(userquery == null){//No existe ningun user con ese username
				session.close();
				return 1;
			}
				
			else{			
				session.close();
				return 0;
			}
				
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
