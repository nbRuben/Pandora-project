package edu.upc.eetac.ea.group1.pandora.api;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import edu.upc.eetac.ea.group1.pandora.api.models.Commentdb;
import edu.upc.eetac.ea.group1.pandora.api.models.Groupdb;
import edu.upc.eetac.ea.group1.pandora.api.models.Notificationdb;
import edu.upc.eetac.ea.group1.pandora.api.models.Postdb;
import edu.upc.eetac.ea.group1.pandora.api.models.Subjectdb;
import edu.upc.eetac.ea.group1.pandora.api.models.Userdb;

public class CreateTablesDB {

	public static void main(String[] args) {
		AnnotationConfiguration config = new AnnotationConfiguration();
		config.addAnnotatedClass(Userdb.class);
		config.addAnnotatedClass(Postdb.class);
		config.addAnnotatedClass(Groupdb.class);
		config.addAnnotatedClass(Subjectdb.class);
		config.addAnnotatedClass(Commentdb.class);
		config.addAnnotatedClass(Notificationdb.class);
		config.configure();

		new SchemaExport(config).create(true, true);

		// Hibernate session
		SessionFactory factory = config.buildSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		System.out.println("Tables created.");

		// Ahora añadimos usuarios, subject, group, post, notification de inicio
		Userdb u = new Userdb("user1", "user1", "user1@ea.upc.es", "Pepe",
				"Garcia");
		session.save(u);

		Userdb u2 = new Userdb("user2", "user2", "user1@ea.upc.es", "Jose",
				"Perez");
		session.save(u2);

		Subjectdb s = new Subjectdb("Enginyeria d'aplicacions");
		u.addSubject(s);
		session.save(s);

		Groupdb g = new Groupdb("EA Rules","user1");
		u.addGroup(g);
		session.save(g);

		Date date = new Date();
		System.out.println("Fecha " + date);
		Postdb p = new Postdb(
				"A alguien le falta un componente para el grupo?", date, u,
				null, s);
		u.addPost(p);
		session.save(p);
		p = new Postdb("Hoy hay clase?", date, u, null, s);
		u2.addPost(p);
		session.save(p);
		p = new Postdb("Como nos organizamos?", date, u2, g, null);
		u.addPost(p);
		session.save(p);

		Notificationdb n = new Notificationdb(1, s, false, u.getUsername());
		g.addNotificacion(n);
		session.save(n);
		

		s = new Subjectdb("Planificacio de xarxes");
		u.addSubject(s);
		session.save(s);
		
		n = new Notificationdb(1, s, false, u.getUsername());
		g.addNotificacion(n);
		session.save(n);
		
		
		n = new Notificationdb(3, s, false, u.getUsername()); //// CAMBIAR A GRUPO
		g.addNotificacion(n);
		session.save(n);

		g = new Groupdb("PX - como mola el Pajek","user1");
		u.addGroup(g);
		session.save(g);

		s = new Subjectdb("Seguretat en xarxes");
		u2.addSubject(s);
		session.save(s);
		
		n = new Notificationdb(1, s, false, u2.getUsername());
		g.addNotificacion(n);
		session.save(n);

		s = new Subjectdb("Xarxes Locals, d'Access i Metropolitanes");
		session.save(s);

		s = new Subjectdb("Disseny d'aplicacions");
		session.save(s);

		s = new Subjectdb("Serveis Audivisuals sobre Internet");
		session.save(s);

		s = new Subjectdb("Analisis i dimensionament de xarxes");
		session.save(s);

		s = new Subjectdb("Xarxes de Transport");
		session.save(s);

		// Confirmamos lo que añadimos.
		session.getTransaction().commit();
		System.out.println("Base de Datos inicializada.");

	}

}
