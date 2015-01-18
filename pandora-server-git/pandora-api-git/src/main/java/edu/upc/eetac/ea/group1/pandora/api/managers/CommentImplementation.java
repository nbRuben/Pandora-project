package edu.upc.eetac.ea.group1.pandora.api.managers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.classic.Session;

import edu.upc.eetac.ea.group1.pandora.api.ConvertLists;
import edu.upc.eetac.ea.group1.pandora.api.models.Comment;
import edu.upc.eetac.ea.group1.pandora.api.models.Commentdb;
import edu.upc.eetac.ea.group1.pandora.api.models.Groupdb;
import edu.upc.eetac.ea.group1.pandora.api.models.Notificationdb;
import edu.upc.eetac.ea.group1.pandora.api.models.Postdb;
import edu.upc.eetac.ea.group1.pandora.api.models.Subjectdb;
import edu.upc.eetac.ea.group1.pandora.api.models.Userdb;

@SuppressWarnings("serial")
public class CommentImplementation implements Serializable {

	private AnnotationConfiguration config;
	private SessionFactory factory;
	public List<Postdb> posts;
	public List<Commentdb> comments;
	private static CommentImplementation instance = null;
	ConvertLists convertlist = ConvertLists.getInstance();
	UserImplementation userImpl = UserImplementation.getInstance();
	PostImplementation postImpl = PostImplementation.getInstance();

	private CommentImplementation() {
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

	public static CommentImplementation getInstance() {
		if (instance == null) {
			instance = new CommentImplementation();
		}
		return instance;
	}

	public Comment getComment(int idcomment) {

		Session session = factory.openSession();
		SQLQuery query = session
				.createSQLQuery("SELECT * FROM comment WHERE COMMENT_ID=:commentid");
		query.addEntity(Commentdb.class);
		query.setInteger(":commentid", idcomment);
		session.beginTransaction();
		Comment c = new Comment();
		Commentdb commentquery = (Commentdb) query.uniqueResult();

		if (commentquery == null) {
			throw new NotFoundException("Comment " + idcomment
					+ " no encontrado.");
		} else {
			session.getTransaction().commit();
			c.setId(commentquery.getId());
			c.setContent(commentquery.getContent());
			c.setDate(commentquery.getDate());
			c.setUser(commentquery.getUser().convertFromDB());
			c.setPost(commentquery.getPost().convertFromDB());
		}
		return c;
	}

	public List<Comment> getComments(int postid) {

		Session session = factory.openSession();
		SQLQuery query = session
				.createSQLQuery("SELECT * FROM comment WHERE post_POST_ID=:postid");
		query.addEntity(Commentdb.class);
		query.setInteger("postid", postid);
		session.beginTransaction();

		List<Commentdb> commentsdb = query.list();
		List<Comment> comments = new ArrayList<Comment>();

		for (Commentdb cdb : commentsdb) {

			Comment c = new Comment();
			c.setContent(cdb.getContent());
			c.setId(cdb.getId());
			c.setDate(cdb.getDate());
			c.setPost(cdb.getPost().convertFromDB());
			c.setUser(cdb.getUser().convertFromDB());

			comments.add(c);
		}

		session.getTransaction().commit();
		session.close();

		return comments;
	}

	public int addComment(Commentdb comment, int postid) {
		try {
			SessionFactory factory = config.buildSessionFactory();
			Session sesion = factory.getCurrentSession();
			sesion.beginTransaction();
			Date date = new Date();
			comment.setDate(date);
			comment.setPost(postImpl.getPostdb(postid));
			sesion.save(comment);
			sesion.getTransaction().commit();
			sesion.close();
			return 1;
		} catch (Exception e) {

			return 0;
		}
	}

	public int updateComment(Commentdb comment, int idcomment) {

		validateComment(comment);
		SessionFactory factory = config.buildSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();

		try {
			session.save(comment);
			session.getTransaction().commit();
		} catch (Exception e) {
			session.close();
			return 0;
		} finally {
			session.close();
		}

		return 1;
	}

	public int deleteComment(int idcomment) {

		Session session = factory.openSession();
		try {
			session.beginTransaction();

			Commentdb comment = (Commentdb) session.get(Commentdb.class,
					new Integer(idcomment));

			session.delete(comment);

		} catch (Exception e) {

			return 0;
		}

		session.getTransaction().commit();
		session.close();
		return 1;
	}

	private void validateComment(Commentdb comment) {
		if (comment.getContent() == null)
			throw new BadRequestException(
					"Es obligatorio que haya un contenido.");
		if (comment.getContent().length() > 254)
			throw new BadRequestException(
					"Demasiados caracteres en el contenido ( >254 )");

	}

}
