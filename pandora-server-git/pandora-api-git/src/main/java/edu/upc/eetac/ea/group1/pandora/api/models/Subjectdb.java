package edu.upc.eetac.ea.group1.pandora.api.models;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name= "subject")
public class Subjectdb {
	int id;
	String name; 
	List<Userdb> user; 
	List<Postdb> post;
	List<Notificationdb> notification;
	List<Documentdb> document;
	
	public Subjectdb(String name) {
		this.name = name;				
		post = new ArrayList<>();
		user = new ArrayList<>();
		notification = new ArrayList<>();
		document = new ArrayList<>();
	}
	public Subjectdb(){
		super();
		post = new ArrayList<>();
		user = new ArrayList<>();
		notification = new ArrayList<>(); 
		document = new ArrayList<>();
	}
	public Subject convertFromDB(){
		
		Subject subjectdb= new Subject();
		
		subjectdb.setId(this.id);
		subjectdb.setName(this.name);
		
		return subjectdb;
	}
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "SUBJECT_ID", unique = true, nullable = false)
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
	
	
	
	@ManyToMany(fetch = FetchType.EAGER, mappedBy ="subject")
	public List<Userdb> getUser() {
		return user;
	}
	public void setUser(List<Userdb> user) {
		this.user = user;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="subject")
	public List<Postdb> getPost() {
		return post;
	}
	public void setPost(List<Postdb> post) {
		this.post = post;
	}
	
	
	@OneToMany (fetch = FetchType.LAZY, mappedBy= "subject")
	public List<Notificationdb> getNotification() {
		return notification;
	}

	public void setNotification(List<Notificationdb> notification) {
		this.notification = notification;
	}
	
    @OneToMany (fetch = FetchType.LAZY, mappedBy= "subject")
	public List<Documentdb> getDocument() {
		return document;
	}
	public void setDocument(List<Documentdb> document) {
		this.document = document;
	}
	
	public void addUser(Userdb user){
		this.user.add(user);
	}
	
    public void addPost (Postdb post)
    {
    	this.post.add(post);
    }
    
    public void addNotificacion (Notificationdb notification)
    {
    	this.notification.add(notification);
    }
    
    
	public void addDocument(Documentdb document){
		this.document.add(document);
	}
    
	

}
