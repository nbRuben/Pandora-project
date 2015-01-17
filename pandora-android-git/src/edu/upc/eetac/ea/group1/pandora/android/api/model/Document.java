package edu.upc.eetac.ea.group1.pandora.android.api.model;

import java.io.File;

public class Document {

	private int id;
	private String name;
	private Subject subject;
	private Group grupo;
	private String username;
	
	public Document (){}
	
	public Document (int id, String name, Subject subject, String username){
		this.id = id;
		this.name = name;
		this.subject = subject;
		this.username = username;
	}
	
	public Document (int id, String name, Group grupo, String username){
		this.id = id;
		this.name = name;
		this.grupo = grupo;
		this.username = username;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Subject getSubject() {
		return subject;
	}
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	public Group getGrupo() {
		return grupo;
	}
	public void setGrupo(Group grupo) {
		this.grupo = grupo;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}	

}
