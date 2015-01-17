package edu.upc.eetac.ea.group1.pandora.api.managers;

import java.io.Serializable;
import java.util.ArrayList;
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

public class GroupImplementation implements  Serializable {

	//Atributos Hibernate
	private AnnotationConfiguration config;
	private SessionFactory factory;
	//Atributos List que usaremos
	public List<Groupdb> groups;
	//Intancia
	private static GroupImplementation instance = null;
	ConvertLists convertlist= ConvertLists.getInstance();

	private GroupImplementation(){
		super();
		this.groups = new ArrayList<Groupdb>();
		
		config = new AnnotationConfiguration();
		config.addAnnotatedClass(Userdb.class);//si queremos otra clase(tabla) le añadiriamos otra
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
	
	
	public Group getGroup(String groupid) {
		Session session = factory.openSession();
		SQLQuery query = session.createSQLQuery("SELECT * FROM grupo WHERE GROUP_ID = :idg");
		query.addEntity(Groupdb.class);
		query.setString("idg", groupid);
		System.out.println("ID grupo a escoger: "+groupid);
		session.beginTransaction();
		Group g = new Group();
		Groupdb groupquery = (Groupdb) query.uniqueResult();
		
		if (groupquery == null)
				throw new NotFoundException ("Grupo con ID " + groupid + " no encontrado");
		
		else {
			
			session.getTransaction().commit();
			
			//Obtenemos parametros del subject y los parseamos a Userdb
			
			String subjectName = groupquery.getName();
			int subjectID = groupquery.getId();
			List<Userdb> groupUsers = groupquery.getUser(); 
			List<Postdb> groupPosts = groupquery.getPost();
			List<Notificationdb> groupNotifications = groupquery.getNotification();
			
			//cambiamos las listas a db
			
			List<User> groupUsersdb = convertlist.convertListUsers(groupUsers); 
			List<Post> groupPostsdb = convertlist.convertListPosts(groupPosts);
			List<Notification> groupNotificationsdb = convertlist.convertListNotifications(groupNotifications);
			
			//Metemos la info al Subjectdb
			
			g.setName(subjectName);
			g.setId(subjectID);
			g.setUser(groupUsersdb);
			g.setPost(groupPostsdb);
			g.setNotification(groupNotificationsdb);
		}
		return g;
	}
	public List<Groupdb> getGroups() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public int addGroup(Groupdb group) {
		validateGroup(group,0);
		
		try{
			Session session = factory.openSession();
		
			SessionFactory factory = config.buildSessionFactory();
			Session sesion = factory.getCurrentSession();
			sesion.beginTransaction();	
			
			//Guardo User
			sesion.save(group);
			sesion.getTransaction().commit();
		}catch(Exception e){
			return 0;
		}
		return 1;
	}

	
	public int updateGroup(Groupdb group, String idGroup) {
		System.out.println("Update id: " + idGroup );
		int id = Integer.valueOf(idGroup);
		System.out.println("id: "+id);
		Session session = factory.openSession();
		 try{
		 session.beginTransaction();
		 
		 Groupdb groupquery = (Groupdb) session.get(Groupdb.class, id);
		 groupquery.setName(group.getName());
		 session.update(groupquery);
		 }catch(Exception e){
			 return 0;			
		 }
		 
		 session.getTransaction().commit();//end of transaction
		 session.close();//end of  session
		 return 1;
	}

	
	public int deleteGroup(String idGroup) {
		System.out.println("Delete id: " + idGroup );
		int id = Integer.valueOf(idGroup);
		System.out.println("id: "+id);
		Session session = factory.openSession();
		 try{
		 session.beginTransaction();
		 Groupdb groupquery = (Groupdb) session.get(Groupdb.class, id);
		 System.out.println("Nomnbre: "+groupquery.getName());	 
		 session.delete(groupquery);
		 }catch(Exception e){
			 
			 return 0;
		 }
		 
		 session.getTransaction().commit();//end of transaction
		 session.close();//end of  session
		 return 1;
	}
	
	public List<Group> getGroupsByUser(String username) {
		Session session = factory.openSession();
		session.beginTransaction();
		System.out.println("User: " + username);
		Userdb userquery = (Userdb) session.get(Userdb.class, username);
		System.out.println("User:" + userquery.getName());
		System.out.println("Grupos: "+userquery.getGrupo().get(0).getName());
		session.getTransaction().commit();
		List<Group> grupos = new ArrayList<Group>();
		grupos = convertlist.convertListGroups(userquery.getGrupo());
		
		
		return grupos;//userquery.getGrupo();
		
	}
	public int exitFromGroup(String groupid, String username){
		int gid = Integer.valueOf(groupid);
		System.out.println("Delete id: " + gid);
		Session session = factory.openSession();
		 try{
		 session.beginTransaction();
		 Userdb userquery = (Userdb) session.get(Userdb.class, username);
		 System.out.println("User: " + userquery.getName());
		 Iterator<Groupdb> it= userquery.getGrupo().iterator();
		 System.out.println("Has groups: "+ userquery.getGrupo().size());
		 while (it.hasNext()) {
			 Groupdb g = (Groupdb) it.next();
			 g.getId();
			 System.out.println("Group id in it: " + g.getId());
			 if (g.getId()==gid){
				 System.out.println("Removing id" + g.getId());
				 it.remove();
			 }
		 }
		 System.out.println("Checking userquery - Size: "+userquery.getGrupo().size());
		 session.update(userquery);
		 }catch(Exception e){
			 
			 return 0;
		 }
		 session.getTransaction().commit();
		 session.close();
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
		 session.getTransaction().commit();//end of transaction
		 session.close();//end of  session
		return 1;
	}
	public int addPostToGroup(Postdb post){
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

	
	public User login (String username, String password) {
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

	private void validateGroup(Groupdb group, int op) {
		if(group.getName()==null)
			throw new BadRequestException("Es obligatoria llenar el campo Name.");
		if(group.getName().length()>254)
			throw new BadRequestException("Demasiados caracteres para tu Name, no puede superrar los 254 caracteres.");
		if(op==0){
			if(searchGroupInDB(group.getName())!=0)//Miramos si el subject ya existe
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

	
	public int addPost(Postdb post, int idSubject, int idGroup) {
		// TODO Auto-generated method stub
		try {
			SessionFactory factory = config.buildSessionFactory();
			Session sesion = factory.getCurrentSession();
			sesion.beginTransaction();
			SubjectImplementation impl = SubjectImplementation.getInstance();
			post.setSubject(impl.getSubjectdb(idSubject));
			sesion.save(post);
			sesion.getTransaction().commit();
			sesion.close();
			
			return 1;
		} catch (Exception e) {

			return 0;
		}
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
