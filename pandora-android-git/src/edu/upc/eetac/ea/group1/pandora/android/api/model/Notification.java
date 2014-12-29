package edu.upc.eetac.ea.group1.pandora.android.api.model;

public class Notification {
	
	String id; 
	int type; 
    Group grupo; 
	Subject subject;
	
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
	
	
	

}
