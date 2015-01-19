package edu.upc.eetac.ea.group1.pandora.api.managers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import edu.upc.eetac.ea.group1.pandora.api.ConvertLists;
import edu.upc.eetac.ea.group1.pandora.api.models.Document;
import edu.upc.eetac.ea.group1.pandora.api.models.Documentdb;
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
public class SubjectImplementation implements Serializable {

	@Context
	private Application app;
	private AnnotationConfiguration config;
	private SessionFactory factory;
	public List<Subjectdb> subjects;
	private static SubjectImplementation instance = null;
	ConvertLists convertList = ConvertLists.getInstance();

	private SubjectImplementation() {
		super();
		this.subjects = new ArrayList<Subjectdb>();

		config = new AnnotationConfiguration();
		config.addAnnotatedClass(Userdb.class);
		config.addAnnotatedClass(Groupdb.class);
		config.addAnnotatedClass(Subjectdb.class);
		config.addAnnotatedClass(Notificationdb.class);
		config.configure();
		factory = config.buildSessionFactory();
	}

	public static SubjectImplementation getInstance() {
		if (instance == null) {
			instance = new SubjectImplementation();
		}
		return instance;
	}

	public Subjectdb getSubjectdb(int id) {
		Session session = factory.openSession();
		SQLQuery query = session
				.createSQLQuery("SELECT * FROM subject WHERE SUBJECT_ID = :idsubject");
		query.addEntity(Subjectdb.class);
		query.setInteger("idsubject", id);
		session.beginTransaction();
		Subjectdb subjectquery = (Subjectdb) query.uniqueResult();

		//session.close();
		return subjectquery;
	}

	public Subject getSubject(int id) {
		Session session = factory.openSession();
		SQLQuery query = session
				.createSQLQuery("SELECT * FROM subject WHERE SUBJECT_ID = :idsubject");
		query.addEntity(Subjectdb.class);
		query.setInteger("idsubject", id);
		session.beginTransaction();
		Subject s = new Subject();
		Subjectdb subjectquery = (Subjectdb) query.uniqueResult();

		if (subjectquery == null)
			throw new NotFoundException("Subject con ID " + id
					+ " no encontrado");

		else {

			session.getTransaction().commit();
			String subjectName = subjectquery.getName();
			int subjectID = subjectquery.getId();
			List<Userdb> subjectUsers = subjectquery.getUser();
			List<Postdb> subjectPosts = subjectquery.getPost();
			List<Notificationdb> subjectNotifications = subjectquery
					.getNotification();
			List<User> subjectUsersdb = convertList
					.convertListUsers(subjectUsers);
			List<Post> subjectPostdb = convertList
					.convertListPosts(subjectPosts);
			List<Notification> subjectNotificationdb = convertList
					.convertListNotifications(subjectNotifications);
			s.setName(subjectName);
			s.setId(subjectID);
			s.setUser(subjectUsersdb);
			s.setPost(subjectPostdb);
			s.setNotification(subjectNotificationdb);
		}

		return s;
	}

	public List<Subject> getSubjects() {
		// TODO Auto-generated method stub
		Session session = factory.openSession();
		SQLQuery query = session.createSQLQuery("SELECT * FROM subject");
		query.addEntity(Subjectdb.class);
		session.beginTransaction();

		@SuppressWarnings("unchecked")
		List<Subjectdb> subjectdb = query.list();
		List<Subject> subjects = convertList.convertListSubjects(subjectdb);

		session.getTransaction().commit();
		session.close();

		return subjects;
	}
	
	private void validateSubject(Subjectdb subject, int op) {
		if (subject.getName() == null)
			throw new BadRequestException(
					"Es obligatoria llenar el campo Name.");
		if (subject.getName().length() > 254)
			throw new BadRequestException(
					"Demasiados caracteres para tu Name, no puede superrar los 254 caracteres.");
		if (op == 0) {
			if (searchSubjectInDB(subject.getId()) != 0)// Miramos si el subject
														// ya existe
				throw new BadRequestException("Subject " + subject.getName()
						+ " ya existe.");
		}

	}

	private int searchSubjectInDB(int id) {

		Session session = factory.openSession();
		SQLQuery query = session
				.createSQLQuery("Selet * FROM subject WHERE SUBJECT_ID = :id");
		query.addEntity(Subjectdb.class);
		query.setInteger("id", id);
		session.beginTransaction();
		Subjectdb subjectquery = (Subjectdb) query.uniqueResult();
		session.getTransaction().commit();

		if (subjectquery == null) {
			session.close();
			return 1;
		} else {
			session.close();
			return 1;
		}

	}

	public int addPost(Postdb post, int idSubject) {
		// TODO Auto-generated method stub
		try {
			SessionFactory factory = config.buildSessionFactory();
			Session sesion = factory.getCurrentSession();
			sesion.beginTransaction();
			post.setSubject(getSubjectdb(idSubject));
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

	public void addDocumentsToSubject(int idSubject, Documentdb document) {
		SessionFactory factory = config.buildSessionFactory();
		Session sesion = factory.openSession();
		sesion.beginTransaction();
		document.setSubject(getSubjectdb(idSubject));
		sesion.save(document);
		sesion.getTransaction().commit();
		sesion.close();
	}

	public List<Post> getPostsFromSubject(int idSubject) {
		// TODO Auto-generated method stub
		Session session = factory.openSession();
		SQLQuery query = session
				.createSQLQuery("SELECT * FROM post WHERE subject_SUBJECT_ID= :idSubject ORDER BY date DESC");
		query.addEntity(Postdb.class);
		query.setInteger("idSubject", idSubject);
		session.beginTransaction();

		@SuppressWarnings("unchecked")
		List<Postdb> postsdb = query.list();
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

	public List<Document> getDocumentsFromSubject(int idSubject) {
		// TODO Auto-generated method stub
		Session session = factory.openSession();
		SQLQuery query = session
				.createSQLQuery("SELECT * FROM document WHERE subject_SUBJECT_ID= :idSubject");
		query.addEntity(Documentdb.class);
		query.setInteger("idSubject", idSubject);
		session.beginTransaction();

		@SuppressWarnings("unchecked")
		List<Documentdb> documentsdb = query.list();
		List<Document> documents = new ArrayList<Document>();
		for (Documentdb d : documentsdb) {
			Document doc = new Document();
			doc.setId(d.getId());
			doc.setName(d.getName());
			doc.setUsername(d.getUsername());
			documents.add(doc);
		}

		session.getTransaction().commit();
		session.close();
		return documents;
	}

	public List<Subject> searchSubject(String subject) {
		// TODO Auto-generated method stub
		Session session = factory.openSession();
		SQLQuery query = session
				.createSQLQuery("SELECT * FROM subject WHERE name LIKE :subject");
		query.addEntity(Subjectdb.class);
		query.setString("subject", "%" + subject + "%");
		session.beginTransaction();

		@SuppressWarnings("unchecked")
		List<Subjectdb> subjectdb = query.list();
		List<Subject> subjects = convertList.convertListSubjects(subjectdb);

		session.getTransaction().commit();
		session.close();

		return subjects;
	}

	public Subject searchSubjectById(int idSubject) {
		// TODO Auto-generated method stub
		Session session = factory.openSession();
		SQLQuery query = session
				.createSQLQuery("SELECT * FROM subject WHERE SUBJECT_ID = :idSubject");
		query.addEntity(Subjectdb.class);
		query.setInteger("idSubject", idSubject);
		session.beginTransaction();

		@SuppressWarnings("unchecked")
		Subjectdb subjectdb = (Subjectdb) query.uniqueResult();
		Subject subject = subjectdb.convertFromDB();

		session.getTransaction().commit();
		session.close();

		return subject;
	}

}
