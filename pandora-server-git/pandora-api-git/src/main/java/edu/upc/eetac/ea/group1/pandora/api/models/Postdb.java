package edu.upc.eetac.ea.group1.pandora.api.models;

import static javax.persistence.GenerationType.IDENTITY;



import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table (name = "post")
public class Postdb {
	
	int id;
	String content;
	Date date;
	Userdb user;
	Groupdb grupo;
	Subjectdb subject;
	List<Commentdb> comments;
		
	public Postdb(String content, Date d, Userdb u, Groupdb g, Subjectdb s) {
		this.content = content;	
		this.date=d;
		this.user = u;
		this.grupo = g;
		this.subject = s;		
	}
	
	public Postdb(){}
	
	public Post convertFromDB (){
		Post post= new Post();
		
		post.setId(this.id);
		post.setContent(this.content);
		post.setDate(this.date);
		
		return post;
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column (name = "POST_ID", unique = true, nullable = false)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column (name = "CONTENT", nullable = false)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Column(name="DATE")
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	public Userdb getUser() {
		return user;
	}
	public void setUser(Userdb user) {
		this.user = user;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	public Groupdb getGrupo() {
		return grupo;
	}
	public void setGrupo(Groupdb grupo) {
		this.grupo = grupo;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	public Subjectdb getSubject() {
		return subject;
	}
	public void setSubject(Subjectdb subject) {
		this.subject = subject;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy="post")
	public List<Commentdb> getComments() {
		return comments;
	}
	public void setComments(List<Commentdb> comments) {
		this.comments = comments;
	}


    
}
