package edu.upc.eetac.ea.group1.pandora.api.models;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.File;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table (name = "document")
public class Documentdb {

	private int id;
	private String name;
	private Subjectdb subject;
	private Groupdb grupo;
	private String username;
	
	public Documentdb (){}
	
	public Documentdb (int id, String name, Subjectdb subject, String username){
		this.id = id;
		this.name = name;
		this.subject = subject;
		this.username = username;
	}
	
	public Documentdb (int id, String name, Groupdb grupo, String username){
		this.id = id;
		this.name = name;
		this.grupo = grupo;
		this.username = username;
	}
	
	public Document convertFromDB(){
		Document d = new Document();
		d.setId(this.id);
		d.setName(this.name);
		d.setUsername(this.username);
		return d;
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "DOCUMENT_ID", unique = true, nullable = false)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name = "NAME", nullable = false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	public Subjectdb getSubject() {
		return subject;
	}
	public void setSubject(Subjectdb subject) {
		this.subject = subject;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	public Groupdb getGrupo() {
		return grupo;
	}
	public void setGrupo(Groupdb grupo) {
		this.grupo = grupo;
	}
	@Column(name = "USERNAME", nullable = false)
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
}
