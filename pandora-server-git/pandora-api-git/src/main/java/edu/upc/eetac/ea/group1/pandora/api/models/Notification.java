package edu.upc.eetac.ea.group1.pandora.api.models;

public class Notification {
	
	int id; 
	int type; 
    Group grupo; 
	Subject subject;
	String username;
	boolean read;
	
	public Notification(){}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
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

	public boolean isRead() {
		return read;
	}
	public void setRead(boolean read) {
		this.read = read;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	
	
	

}
