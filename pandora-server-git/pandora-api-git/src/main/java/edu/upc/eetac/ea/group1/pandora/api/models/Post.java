package edu.upc.eetac.ea.group1.pandora.api.models;

import java.util.Date;
import java.util.List;

public class Post {
	
	int id;
	String content;
	Date date;
	User user;
	Group grupo;
	Subject subject;
	List<Comment> comment;
	
	public Post (){}
	
		
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Group getGrupo() {
		return grupo;
	}

	public void setGrupo(Group grupo) {
		this.grupo = grupo;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public List<Comment> getComment() {
		return comment;
	}

	public void setComment(List<Comment> comment) {
		this.comment = comment;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}
	

}
