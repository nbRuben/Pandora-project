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
import edu.upc.eetac.ea.group1.pandora.api.models.Group;
import edu.upc.eetac.ea.group1.pandora.api.models.Groupdb;
import edu.upc.eetac.ea.group1.pandora.api.models.Notification;
import edu.upc.eetac.ea.group1.pandora.api.models.Notificationdb;
import edu.upc.eetac.ea.group1.pandora.api.models.Post;
import edu.upc.eetac.ea.group1.pandora.api.models.Postdb;
import edu.upc.eetac.ea.group1.pandora.api.models.Subjectdb;
import edu.upc.eetac.ea.group1.pandora.api.models.User;
import edu.upc.eetac.ea.group1.pandora.api.models.Userdb;

@SuppressWarnings("serial")
public class GroupImplementation implements  Serializable {
	private AnnotationConfiguration config;
	private SessionFactory factory;
	public List<Groupdb> groups;
	private static GroupImplementation instance = null;
	ConvertLists convertlist= ConvertLists.getInstance();

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
	
	
	public Group getGroup(String groupid) {
		Session session = factory.openSession();
		SQLQuery query = session.createSQLQuery("SELECT * FROM grupo WHERE GROUP_ID = :idg");
		query.addEntity(Groupdb.class);
		query.setString("idg", groupid);
		session.beginTransaction();
		Group g = new Group();
		Groupdb groupquery = (Groupdb) query.uniqueResult();
		
		if (groupquery == null)
				throw new NotFoundException ("Grupo con ID " + groupid + " no encontrado");
		else {
			
			session.getTransaction().commit();
			
			String subjectName = groupquery.getName();
			int subjectID = groupquery.getId();
			List<Userdb> groupUsers = groupquery.getUser(); 
			List<Postdb> groupPosts = groupquery.getPost();
			List<Notificationdb> groupNotifications = groupquery.getNotification();
			
			List<User> groupUsersdb = convertlist.convertListUsers(groupUsers); 
			List<Post> groupPostsdb = convertlist.convertListPosts(groupPosts);
			List<Notification> groupNotificationsdb = convertlist.convertListNotifications(groupNotifications);
			
			g.setName(subjectName);
			g.setId(subjectID);
			g.setUser(groupUsersdb);
			g.setPost(groupPostsdb);
			g.setNotification(groupNotificationsdb);
		}
		return g;
	}

	public int addGroup(Groupdb group) {
		validateGroup(group,0);
		try{
			SessionFactory factory = config.buildSessionFactory();
			Session sesion = factory.getCurrentSession();
			sesion.beginTransaction();	
			sesion.save(group);
			sesion.getTransaction().commit();
		}catch(Exception e){
			return 0;
		}
		return 1;
	}

	public int updateGroup(Groupdb group, String idGroup) {
		int id = Integer.valueOf(idGroup);
		Session session = factory.openSession();
		 try{
		 session.beginTransaction();
		 Groupdb groupquery = (Groupdb) session.get(Groupdb.class, id);
		 groupquery.setName(group.getName());
		 session.update(groupquery);
		 }catch(Exception e){
			 return 0;			
		 }
		 session.getTransaction().commit();
		 session.close();
		 return 1;
	}

	
	public int deleteGroup(String idGroup) {
		int id = Integer.valueOf(idGroup);
		Session session = factory.openSession();
		 try{
		 session.beginTransaction();
		 Groupdb groupquery = (Groupdb) session.get(Groupdb.class, id); 
		 session.delete(groupquery);
		 }catch(Exception e){
			 
			 return 0;
		 }
		 session.getTransaction().commit();
		 session.close();
		 return 1;
	}
	
	public List<Group> getGroupsByUser(String username) {
		Session session = factory.openSession();
		session.beginTransaction();
		Userdb userquery = (Userdb) session.get(Userdb.class, username);
		session.getTransaction().commit();
		List<Group> grupos = new ArrayList<Group>();
		grupos = convertlist.convertListGroups(userquery.getGrupo());
		return grupos;
		
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
			 }
		 }
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
		 session.getTransaction().commit();
		 session.close();
		return 1;
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

}
