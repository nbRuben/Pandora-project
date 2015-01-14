package edu.upc.eetac.ea.group1.pandora.api.managers;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

public class UserImplementation implements Serializable {

	// Atributos Hibernate
	private AnnotationConfiguration config;
	private SessionFactory factory;
	// Atributos List que usaremos
	public List<Userdb> users;

	// Intancia
	private static UserImplementation instance = null;

	ConvertLists convertlist = ConvertLists.getInstance();

	private UserImplementation() {
		super();

		this.users = new ArrayList<Userdb>();

		config = new AnnotationConfiguration();
		config.addAnnotatedClass(Userdb.class);// si queremos otra clase(tabla)
												// le añadiriamos otra
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

			// Obtenemos parametros del user y lo pasamos al Userdb
			/*
			 * String username = userquery.getUsername(); String pass =
			 * userquery.getUserpass(); String email = userquery.getEmail();
			 * String name = userquery.getName(); String surname =
			 * userquery.getSurname(); List<Postdb> postsdb =
			 * userquery.getPost(); List<Groupdb> groupdb =
			 * userquery.getGrupo(); List<Subjectdb> subectdb =
			 * userquery.getSubject();
			 */

			// Cambiamos los comments, groups y subjects a db
			List<Post> posts = convertlist
					.convertListPosts(userquery.getPost());
			List<Group> groups = convertlist.convertListGroups(userquery
					.getGrupo());
			List<Subject> subjects = convertlist.convertListSubjects(userquery
					.getSubject());

			// Metemos la info al Userdb
			/*
			 * u.setUsername(username); u.setUserpass(pass); u.setEmail(email);
			 * u.setName(name); u.setSurname(surname); u.setPosts(postsdb);
			 * u.setGroups(groupsdb); u.setSubjects(subjectsdb);
			 */

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

		// Primero de todo comprobamos que los datos sean correctos.
		validateUser(user, 0);

		// hibernate session
		SessionFactory factory = config.buildSessionFactory();
		Session sesion = factory.getCurrentSession();
		sesion.beginTransaction();

		User u = new User();
		u.setName(user.getName());
		u.setUsername(user.getName());
		u.setUserpass(user.getUserpass());
		u.setSurname(user.getSurname());
		u.setEmail(user.getEmail());

		// Guardo User
		sesion.save(user);
		sesion.getTransaction().commit();

		return u;
	}

	public int updateUser(Userdb user, String username) {
		validateUser(user, 1);

		// hibernate session
		SessionFactory factory = config.buildSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();

		try {
			session.save(user); // guardo user
			session.getTransaction().commit();

		} catch (Exception e) {

			session.close();
			return 0;

		} finally {

			session.close();

		}

		return 1;
	}

	public List<Subject> getUserSubjects(String username) {
		// TODO Auto-generated method stub
		Session session = factory.openSession();
		SQLQuery query = session
				.createSQLQuery("SELECT s.name, s.subject_id FROM subject s, user_subject u WHERE u.user_USERNAME = :username AND u.subject_SUBJECT_ID = s.subject_id");
		query.addEntity(Subjectdb.class);
		query.setString("username", username);
		System.out.println("Query: " + query.toString());
		session.beginTransaction();

		System.out.println("");
		@SuppressWarnings("unchecked")
		List<Subjectdb> subjectdb = query.list();
		List<Subject> subjects = convertlist.convertListSubjects(subjectdb);

		session.getTransaction().commit();
		session.close();

		System.out.println("Entrega de subjects: ");
		return subjects;
	}

	public int deleteUser(String username) {
		// TODO Auto-generated method stub
		return 0;
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
			if (searchUserInDB(user.getUsername()) == 0)// Miramos si el user ya
														// existe
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

		if (userquery == null) {// No existe ningun user con ese username
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

		if (userquery == null) {// No existe ningun user con ese username
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
			// hibernate session
			System.out.println("dentro de la api");
			System.out.println("el user es " + username);
			SessionFactory factory = config.buildSessionFactory();
			Session sesion = factory.getCurrentSession();
			sesion.beginTransaction();
			Userdb u = (Userdb) sesion.get(Userdb.class, new String(username));
			System.out.println("el usuario es " + u);
			SubjectImplementation impl = SubjectImplementation.getInstance();
			Subjectdb s = impl.getSubjectdb(idSubject);
			System.out.println("el subjects es " + s.getName());
			System.out.println("ya he cogido todos los datos");
			u.addSubject(s);
			System.out.println("subject guardado");
			// Guardo User
			sesion.save(u);
			System.out.println("usuario guardado");
			sesion.getTransaction().commit();
			System.out.println("commit hecho");

		} catch (Exception e) {
			e.printStackTrace();
			// throw new
			// BadRequestException("No se ha podido crear el usuario.");
		}

	}

	public void deleteSubjectFromUser(String username, int idSubject) {
		System.out.println("borrando");
		SessionFactory factory = config.buildSessionFactory();
		Session sesion = factory.getCurrentSession();
		sesion.beginTransaction();
		Userdb u = (Userdb) sesion.get(Userdb.class, new String(username));
		SubjectImplementation impl = SubjectImplementation.getInstance();
		Subjectdb s = impl.getSubjectdb(idSubject);
		List<Subjectdb> ls = u.getSubject();
		for (int i = 0; i < ls.size(); i++) {
			System.out.println("Contiene: " + ls.get(i).getName());
		}
		for (int i = 0; i < ls.size(); i++) {
			if (ls.get(i).getId() == s.getId()) {
				ls.remove(i);
			}
		}
		for (int i = 0; i < ls.size(); i++) {
			System.out.println("Contiene ahora: " + ls.get(i).getName());
		}
		u.setSubject(ls);
		// Guardo User
		sesion.save(u);
		sesion.getTransaction().commit();
		System.out.println("commit hecho");
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
		// Guardo User
		session.update(notification);
		session.getTransaction().commit();
	}

}
