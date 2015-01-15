package edu.upc.eetac.ea.group1.pandora.android.api.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Notification implements Serializable {
	
	String id; 
	int type; 
    Group grupo; 
	Subject subject;
	String username;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	

}
