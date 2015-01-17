package edu.upc.eetac.ea.group1.pandora.api.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class Userdb {
	
	String username;
	String userpass; 
	String email;
	String name; 
	String surname;
	List<Commentdb> comment;
    List<Postdb> post;
    List<Groupdb> grupo;
    List<Subjectdb> subject;
    
	public Userdb (String username, String password, String email, String name,  String surname){
		this.username = username;
		this.userpass = password;
		this.email = email;
		this.name = name;
		this.surname = surname;
		
		
		post = new ArrayList<>();
		grupo = new ArrayList<>();
		subject = new ArrayList<>();
		
	}	
	public Userdb(){
		super();
		post = new ArrayList<>();
		grupo = new ArrayList<>();
		subject = new ArrayList<>();
	};
	
	public User convertFromDB(){
		
		User userdb= new User();
		
		userdb.setUsername(this.username);
		userdb.setUserpass(this.userpass);
		userdb.setEmail(this.email);
		userdb.setName(this.name);
		userdb.setSurname(this.surname);
		
		return userdb;
	}
	
	@Id
    @Column(name= "USERNAME", unique = true, nullable = false)
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Column(name = "USERPASS", nullable = false)
	public String getUserpass() {
		return userpass;
	}
	public void setUserpass(String userpass) {
		this.userpass = userpass;
	}
	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Column(name = "NAME", nullable = false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "SURNAME", nullable = false)
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy="user")
	public List<Postdb> getPost() {
		return post;
	}
	public void setPost(List<Postdb> post) {
		this.post = post;
	}
	
	@ManyToMany (fetch = FetchType.LAZY)
    public List<Groupdb> getGrupo() {
		return grupo;
	}
	public void setGrupo(List<Groupdb> grupo) {
		this.grupo = grupo;
	}
	@ManyToMany (fetch = FetchType.LAZY)
	public List<Subjectdb> getSubject() {
		return subject;
	}
	public void setSubject(List<Subjectdb> subject) {
		this.subject = subject;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="user")
	public List<Commentdb> getComment() {
		return comment;
	}
	public void setComment(List<Commentdb> comment) {
		this.comment = comment;
	}
	
    public void addSubject(Subjectdb subject) {
		this.subject.add(subject);
		
	}
    public void addPost (Postdb post)
    {
    	this.post.add(post);
    }
    public void addGroup (Groupdb grupo)
    {
    	this.grupo.add(grupo);
    }
    


}
