package edu.upc.eetac.ea.group1.pandora.api.managers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.BadRequestException;
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
public class GroupImplementation implements  Serializable {

	private AnnotationConfiguration config;
	private SessionFactory factory;
	public List<Groupdb> groups;
	private static GroupImplementation instance = null;
	ConvertLists convertList= ConvertLists.getInstance();

	private GroupImplementation(){
		super();
		this.groups = new ArrayList<Groupdb>();
		
		config = new AnnotationConfiguration();
		config.addAnnotatedClass(Userdb.class);
		config.addAnnotatedClass(Groupdb.class);
		config.addAnnotatedClass(Subjectdb.class);
		config.addAnnotatedClass(Notificationdb.class);
		config.configure();
		factory = config.buildSessionFactory();
	}
	
	public static GroupImplementation getInstance(){
		if(instance==null){
			instance = new GroupImplementation();
		}
		return instance;
	}
	
	
	public List<Group> getGroups() {
		List<Group> grupos = new ArrayList<Group>();
		Session session = factory.openSession();
		SQLQuery query = session.createSQLQuery("SELECT * FROM grupo");
		query.addEntity(Groupdb.class);
		session.beginTransaction();
		List<Groupdb> g = (List<Groupdb>)query.list();
		
		session.getTransaction().commit();
		grupos = convertListGroups(g);
		return grupos;
	}
	
	public List<Post> getPostsByGroup(int id){
	
		System.out.println("id: "+id);
		Session session = factory.openSession();
		SQLQuery groupquery = session.createSQLQuery("SELECT * FROM post WHERE grupo_GROUP_ID= :id");
		groupquery.addEntity(Postdb.class);
		groupquery.setInteger("id", id);
		session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<Postdb> postsdb = groupquery.list();
		List<Post> posts = new ArrayList<Post>();
		for (Postdb p : postsdb) {
			Post post = new Post();
			post.setId(p.getId());
			post.setContent(p.getContent());
			post.setUser(p.getUser().convertFromDB());
			post.setDate(p.getDate());
			posts.add(post);
		}

		session.getTransaction().commit();
		session.close();
		return posts;
	
	}
	
	
	public List<Group> getGroupsByUser(String username) {
		Session session = factory.openSession();
		session.beginTransaction();
		System.out.println("User: " + username);
		Userdb userquery = (Userdb) session.get(Userdb.class, username);
		System.out.println("User:" + userquery.getName());
		session.getTransaction().commit();
		List<Group> grupos = new ArrayList<Group>();
		grupos = convertListGroups(userquery.getGrupo());
		
		
		return grupos;
		
	}
	
	public Group getGroup(String idGroup) {
		Session session = factory.openSession();
		SQLQuery query = session.createSQLQuery("SELECT * FROM grupo WHERE GROUP_ID = :idg");
		query.addEntity(Groupdb.class);
		query.setString("idg", idGroup);
		session.beginTransaction();
		Group g = new Group();
		Groupdb groupquery = (Groupdb) query.uniqueResult();
		
		if (groupquery == null)
				throw new NotFoundException ("Grupo con ID " + idGroup + " no encontrado");
		
		else {
			
			session.getTransaction().commit();
			
			
			String subjectName = groupquery.getName();
			int subjectID = groupquery.getId();
			List<Userdb> groupUsers = groupquery.getUser(); 
			List<Postdb> groupPosts = groupquery.getPost();
			List<Notificationdb> groupNotifications = groupquery.getNotification();
			
			
			List<User> groupUsersdb = convertListUsers(groupUsers); 
			List<Post> groupPostsdb = convertListPosts(groupPosts);
			List<Notification> groupNotificationsdb = convertListNotifications(groupNotifications);
			
			
			g.setName(subjectName);
			g.setId(subjectID);
			g.setUser(groupUsersdb);
			g.setPost(groupPostsdb);
			g.setNotification(groupNotificationsdb);
		}
		return g;
	}

	public Group addGroup(Groupdb group) {
		
		validateGroup(group,0);
		Session session = factory.openSession();
		SessionFactory factory = config.buildSessionFactory();
		Session sesion = factory.getCurrentSession();
		sesion.beginTransaction();	
		
		//Guardo User
		sesion.save(group);
		int id = (Integer) sesion.getIdentifier(group);
		sesion.getTransaction().commit();
		
		Group gr = getGroup(Integer.toString(id));
		return gr;
	}

	public void updateGroup(Groupdb group, String Groupid) {
		int id = Integer.valueOf(Groupid);
		Session session = factory.openSession();
		 try{
		 session.beginTransaction();
		 
		 Groupdb groupquery = (Groupdb) session.get(Groupdb.class, id);
		 groupquery.setName(group.getName());
		 session.update(groupquery);
		 }catch(Exception e){
			 
			
		 }
		 
		 session.getTransaction().commit();
		 session.close();

	}

	public int deleteGroup(String Groupid) {
		int id = Integer.valueOf(Groupid);
		Session session = factory.openSession();
		 try{
		 session.beginTransaction();
		 Groupdb groupquery = (Groupdb) session.get(Groupdb.class, id);
		 System.out.println("Nomnbre: "+groupquery.getName());	 
		 session.delete(groupquery);
		 }catch(Exception e){
			 
			 return 0;
		 }
		 
		 session.getTransaction().commit();
		 session.close();
		 return 1;
	}
	public int exitFromGroup(String groupid, String username){
		int gid = Integer.valueOf(groupid);
		Session session = factory.openSession();
		 try{
		 session.beginTransaction();
		 Userdb userquery = (Userdb) session.get(Userdb.class, username);
		 Iterator<Groupdb> it= userquery.getGrupo().iterator();
		 while (it.hasNext()) {
			 Groupdb g = (Groupdb) it.next();
			 g.getId();
			 if (g.getId()==gid){
				 it.remove();
				 session.update(userquery);
				 session.getTransaction().commit();
				 session.close();
				 	if (g.getOwner().equals(username)){
					 deleteGroup(groupid);
				 }
			 }
		 }
		 
		 }catch(Exception e){
			 
			 return 0;
		 }

		return 1;
	}
	
	public int addUserToGroup(String groupid, String username){
		int gid = Integer.valueOf(groupid);
		Session session = factory.openSession();
		 try{
		 session.beginTransaction();
		 Userdb userquery = (Userdb) session.get(Userdb.class, username);
		 Groupdb groupquery = (Groupdb) session.get(Groupdb.class, gid);
		 userquery.addGroup(groupquery);
		 session.update(userquery);
		 
		 }catch(Exception e){
			 
			 return 0;
		 }
		 session.getTransaction().commit();
		 session.close();
		return 1;
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
	public List<User> getUsersByGroup(int idgroup){
		List<User> users = new ArrayList<User>(); 
		 Session session = factory.openSession();
		 try{
		 session.beginTransaction();
		 Groupdb groupquery = (Groupdb) session.get(Groupdb.class, idgroup);
		 List<Userdb> usersdb = new ArrayList<Userdb>();
		 usersdb = groupquery.getUser();
		  users = convertList.convertListUsers(usersdb);
		 }catch(Exception e){
			 
		 }
		 return users;
		
	}
	
//////////////////////////////////////
	
	public Notification getNotification(String nameGroup) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void addNotification(int groupid, String username) {
		 Session session = factory.openSession();
		 try{
		 session.beginTransaction();
		 Groupdb groupquery = (Groupdb) session.get(Groupdb.class, groupid);
		 Notificationdb n = new Notificationdb(3, groupquery, false, username);
		 groupquery.addNotificacion(n);
		 session.save(n);
		 session.getTransaction().commit();
		 session.close();
		 }catch(Exception e){
			 
		 }
	}

	
	public List<User> searchUser(String user) {
		Session session = factory.openSession();
		SQLQuery query = session.createSQLQuery("SELECT * FROM user WHERE USERNAME LIKE :user");
		query.addEntity(Userdb.class);
		query.setString("user", "%" + user + "%");
		session.beginTransaction();

		@SuppressWarnings("unchecked")
		List<Userdb> usersdb = query.list();
		List<User> users = convertList.convertListUsers(usersdb);

		session.getTransaction().commit();
		session.close();
		
		return users;
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
			if(searchSubjectInDB(subject.getName())!=0)//Miramos si el subject ya existe
				throw new BadRequestException("Subject "+ subject.getName()+" ya existe.");
		}
			
	}
	
	private int searchSubjectInDB(String name) {
		
		Session session = factory.openSession();
		SQLQuery query = session.createSQLQuery("Select * FROM subject WHERE NAME = :name");
		query.addEntity(Subjectdb.class);
		query.setString("name", name);
		session.beginTransaction();
		Subjectdb subjectquery = (Subjectdb) query.uniqueResult();
		session.getTransaction().commit();
		
		if (subjectquery == null){
			session.close();
			return 0;
		}
		else {
			session.close();
			return 1;
		}
	
	}

	private void validateGroup(Groupdb group, int op) {
		if(group.getName()==null)
			throw new BadRequestException("Es obligatoria llenar el campo Name.");
		if(group.getName().length()>254)
			throw new BadRequestException("Demasiados caracteres para tu Name, no puede superrar los 254 caracteres.");
		if(op==0){
			if(searchGroupInDB(group.getName())!=0)
				throw new BadRequestException("Grupo "+ group.getName()+" ya existe.");
		}
			
	}
	
private int searchGroupInDB(String name) {
		
		Session session = factory.openSession();
		SQLQuery query = session.createSQLQuery("Select * FROM grupo WHERE NAME = :name");
		query.addEntity(Groupdb.class);
		query.setString("name", name);
		session.beginTransaction();
		Groupdb subjectquery = (Groupdb) query.uniqueResult();
		session.getTransaction().commit();
		
		if (subjectquery == null){
			session.close();
			return 0;
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

	public int addPost(Postdb post, int idSubject) {
		try {

			Session sesion = factory.getCurrentSession();
			sesion.beginTransaction();
			post.setGrupo(getGroupdb(idSubject));
			Date date = new Date();
			post.setDate(date);
			sesion.save(post);
			sesion.getTransaction().commit();
			sesion.close();
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}
	public Groupdb getGroupdb(int id){
		Session session = factory.openSession();
		SQLQuery query = session
				.createSQLQuery("SELECT * FROM grupo WHERE GROUP_ID = :id");
		query.addEntity(Groupdb.class);
		query.setInteger("id", id);
		session.beginTransaction();
		Groupdb subjectquery = (Groupdb) query.uniqueResult();
		return subjectquery;
	}


}
