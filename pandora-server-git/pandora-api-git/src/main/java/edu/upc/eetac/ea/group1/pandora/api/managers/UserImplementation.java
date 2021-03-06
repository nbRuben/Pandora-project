package edu.upc.eetac.ea.group1.pandora.api.managers;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
import edu.upc.eetac.ea.group1.pandora.api.models.Subject;
import edu.upc.eetac.ea.group1.pandora.api.models.Subjectdb;
import edu.upc.eetac.ea.group1.pandora.api.models.User;
import edu.upc.eetac.ea.group1.pandora.api.models.Userdb;

@SuppressWarnings("serial")
public class UserImplementation implements Serializable {

	private AnnotationConfiguration config;
	private SessionFactory factory;
	public List<Userdb> users;

	private static UserImplementation instance = null;

	ConvertLists convertlist = ConvertLists.getInstance();

	private UserImplementation() {
		super();

		this.users = new ArrayList<Userdb>();

		config = new AnnotationConfiguration();
		config.addAnnotatedClass(Userdb.class);
		config.addAnnotatedClass(Groupdb.class);
		config.addAnnotatedClass(Subjectdb.class);
		config.addAnnotatedClass(Notificationdb.class);
		config.configure();
		factory = config.buildSessionFactory();
	}

	public static UserImplementation getInstance() {
		if (instance == null) {
			instance = new UserImplementation();
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
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public User getUser(String user) {

		Session session = factory.openSession();
		SQLQuery query = session
				.createSQLQuery("SELECT * FROM user WHERE username = :user");
		query.addEntity(Userdb.class);
		query.setString("user", user);
		session.beginTransaction();
		User u = new User();
		Userdb userquery = (Userdb) query.uniqueResult();

		if (userquery == null)
			throw new NotFoundException("Usuario " + user + " no encontrado");
		else {
			session.getTransaction().commit();
			List<Post> posts = convertlist
					.convertListPosts(userquery.getPost());
			List<Group> groups = convertlist.convertListGroups(userquery
					.getGrupo());
			List<Subject> subjects = convertlist.convertListSubjects(userquery
					.getSubject());

			u = userquery.convertFromDB();
			u.setPosts(posts);
			u.setGroups(groups);
			u.setSubjects(subjects);

		}

		return u;
	}

	public Userdb getUserdb(String user) {

		Session session = factory.openSession();
		SQLQuery query = session
				.createSQLQuery("SELECT * FROM user WHERE USERNAME = :user");
		query.addEntity(Userdb.class);
		query.setString("user", user);
		session.beginTransaction();
		Userdb usertquery = (Userdb) query.uniqueResult();

		session.close();
		return usertquery;

	}

	public User addUser(Userdb user) {

		validateUser(user, 0);

		SessionFactory factory = config.buildSessionFactory();
		Session sesion = factory.getCurrentSession();
		sesion.beginTransaction();

		User u = new User();
		u.setName(user.getName());
		u.setUsername(user.getName());
		u.setUserpass(user.getUserpass());
		u.setSurname(user.getSurname());
		u.setEmail(user.getEmail());

		sesion.save(user);
		sesion.getTransaction().commit();

		return u;
	}


	public List<Subject> getUserSubjects(String username) {
		// TODO Auto-generated method stub
		Session session = factory.openSession();
		SQLQuery query = session
				.createSQLQuery("SELECT s.name, s.subject_id FROM subject s, user_subject u WHERE u.user_USERNAME = :username AND u.subject_SUBJECT_ID = s.subject_id");
		query.addEntity(Subjectdb.class);
		query.setString("username", username);
		session.beginTransaction();

		@SuppressWarnings("unchecked")
		List<Subjectdb> subjectdb = query.list();
		List<Subject> subjects = convertlist.convertListSubjects(subjectdb);

		session.getTransaction().commit();
		session.close();

		return subjects;
	}

	private void validateUser(Userdb user, int op) {

		if (user.getUsername() == null)
			throw new BadRequestException(
					"Es obligatoria llenar el campo Username.");
		if (user.getUsername().length() > 254)
			throw new BadRequestException(
					"Demasiados caracteres para tu Username, no puede superrar los 254 caracteres.");
		if (user.getUserpass() == null)
			throw new BadRequestException(
					"Es obligatoria llenar el campo Password.");
		if (user.getUserpass().length() > 254)
			throw new BadRequestException(
					"Demasiados caracteres para tu Password, no puede superrar los 254 caracteres.");
		if (user.getEmail().length() > 254)
			throw new BadRequestException(
					"Demasiados caracteres para tu Email, no puede superrar los 254 caracteres.");
		if (user.getName() == null)
			throw new BadRequestException(
					"Es obligatoria llenar el campo Name.");
		if (user.getName().length() > 254)
			throw new BadRequestException(
					"Demasiados caracteres para tu Name, no puede superrar los 254 caracteres.");
		if (op == 0) {
			if (searchUserInDB(user.getUsername()) == 0)
				throw new BadRequestException("Usuario " + user.getUsername()
						+ " ya existe.");
		}
	}

	private int searchUserInDB(String username) {

		Session session = factory.openSession();
		SQLQuery query = session
				.createSQLQuery("SELECT * FROM user WHERE username = :user");
		query.addEntity(Userdb.class);
		query.setString("user", username);
		session.beginTransaction();
		Userdb userquery = (Userdb) query.uniqueResult();
		session.getTransaction().commit();

		if (userquery == null) {
			session.close();
			return 1;
		}

		else {
			session.close();
			return 0;
		}

	}

	public Userdb searchUserDB(String username) {

		Session session = factory.openSession();
		SQLQuery query = session
				.createSQLQuery("SELECT * FROM user WHERE username = :user");
		query.addEntity(Userdb.class);
		query.setString("user", username);
		session.beginTransaction();
		Userdb userquery = (Userdb) query.uniqueResult();
		session.getTransaction().commit();

		if (userquery == null) {
			throw new NotFoundException("No se ha encontrado al usuario: "
					+ username);
		}

		else {
			session.close();
			return userquery;
		}

	}

	public void addSubjectToUser(String username, int idSubject) {

		try {
			SessionFactory factory = config.buildSessionFactory();
			Session sesion = factory.getCurrentSession();
			sesion.beginTransaction();
			Userdb u = (Userdb) sesion.get(Userdb.class, new String(username));
			SubjectImplementation impl = SubjectImplementation.getInstance();
			Subjectdb s = impl.getSubjectdb(idSubject);
			u.addSubject(s);
			// Guardo User
			sesion.save(u);
			sesion.getTransaction().commit();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void deleteSubjectFromUser(String username, int idSubject) {
		SessionFactory factory = config.buildSessionFactory();
		Session sesion = factory.getCurrentSession();
		sesion.beginTransaction();
		Userdb u = (Userdb) sesion.get(Userdb.class, new String(username));
		SubjectImplementation impl = SubjectImplementation.getInstance();
		Subjectdb s = impl.getSubjectdb(idSubject);
		List<Subjectdb> ls = u.getSubject();
		for (int i = 0; i < ls.size(); i++) {
			if (ls.get(i).getId() == s.getId()) {
				ls.remove(i);
			}
		}
		u.setSubject(ls);
		sesion.save(u);
		sesion.getTransaction().commit();
	}

	public User login(String username, String password) {
		return null;
	}

	public List<Notification> getNotifications(String username) {
		Session session = factory.openSession();
		SQLQuery query = session
				.createSQLQuery("SELECT * FROM notification WHERE SEEN = 'N' and USERNAME = :username");
		query.addEntity(Notificationdb.class);
		query.setString("username", username);
		session.beginTransaction();

		@SuppressWarnings("unchecked")
		List<Notificationdb> notificationdb = query.list();
		List<Notification> notifications = new ArrayList<Notification>();
		for (Notificationdb n : notificationdb) {
			Notification not = new Notification();
			not.setId(n.getId());
			not.setType(n.getType());
			not.setRead(n.isRead());
			not.setUsername(n.getUsername());
			if (n.getGrupo() != null)
				not.setGrupo(n.getGrupo().convertFromDB());
			if (n.getSubject() != null)
				not.setSubject(n.getSubject().convertFromDB());
			notifications.add(not);
		}

		session.getTransaction().commit();
		session.close();

		return notifications;
	}

	public void updateNotification(Notificationdb notification) {
		SessionFactory factory = config.buildSessionFactory();
		Session session = factory.openSession();
		session.beginTransaction();
		notification.setRead(true);
		session.update(notification);
		session.getTransaction().commit();
	}
	
	public User updateUser(Userdb user, String username) {
		// hibernate session
		SessionFactory factory = config.buildSessionFactory();
		Session session = factory.openSession();
		session.beginTransaction();
		 User u = new User();
		  
		  u.setName(user.getName());
	      u.setUsername(username);
	      u.setUserpass(user.getUserpass());
	      u.setSurname(user.getSurname());
	      u.setEmail(user.getEmail());
		   
		session.update(user); // guardo user
		session.getTransaction().commit();

		return u;
	}

}
