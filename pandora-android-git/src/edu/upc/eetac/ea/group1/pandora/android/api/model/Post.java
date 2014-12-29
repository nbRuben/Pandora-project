package edu.upc.eetac.ea.group1.pandora.android.api.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Post {
	String id;
	String content;
	String date;
	User user;
	Group grupo;
	Subject subject;
	List<Comment> comment;
	
	public Post (){}
	
		
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}

//	public void setDate(String strDate) throws ParseException {
//		SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//		Date date = formatoDelTexto.parse(strDate);
//		this.date = date;
//	}



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


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


}
