package edu.upc.eetac.ea.group1.pandora.api.models;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table (name = "comment")
public class Commentdb {
	
	int id;
	String content;
	Userdb user;
	Postdb post;
	
	public Commentdb(String c, Userdb u, Postdb p ){
		this.content=c;
		this.user=u;
		this.post=p;
	}
	
	public Commentdb(){}
	
	public Comment convertFromDB (){
		Comment comment = new Comment();
		
		comment.setId(this.id);
		comment.setContent(this.content);
				
		return comment;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column (name = "COMMENT_ID", unique = true, nullable = false)
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

	@ManyToOne(fetch = FetchType.LAZY)
	public Userdb getUser() {
		return user;
	}

	public void setUser(Userdb user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Postdb getPost() {
		return post;
	}

	public void setPost(Postdb post) {
		this.post = post;
	}

	
}
