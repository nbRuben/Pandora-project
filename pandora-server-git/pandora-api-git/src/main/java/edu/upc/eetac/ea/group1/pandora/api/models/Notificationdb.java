package edu.upc.eetac.ea.group1.pandora.api.models;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table (name = "notification")
public class Notificationdb {
	
	int id; 
	int type; 
    Groupdb grupo; 
	Subjectdb subject;
	String username;
	boolean read;
	

	public Notificationdb (int type, Groupdb group, boolean read, String username){
		
		this.type=type;
		this.grupo=group;
		this.read = read;
		this.username = username;
		
	}
	
	public Notificationdb (int type, Subjectdb subject, boolean read, String username){
		
		this.type= type;
		this.subject=subject;
		this.read = read;
		this.username = username;
	}
	
	public Notificationdb (){}
	
	public Notification convertFromDB(){
		
		Notification notificationdb= new Notification();
		
		notificationdb.setId(this.id);
		notificationdb.setType(this.type);
		notificationdb.setRead(this.read);
		
		return notificationdb;
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "NOTIFICATION_ID", unique = true, nullable = false)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "TYPE", nullable = false)
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	@Type(type="yes_no")
	@Column(name = "SEEN", nullable = false)
	public boolean isRead() {
		return read;
	}
	public void setRead(boolean read) {
		this.read = read;
	}
	
	@ManyToOne (fetch = FetchType.LAZY)
	public Groupdb getGrupo() {
		return grupo;
	}
	public void setGrupo(Groupdb grupo) {
		this.grupo = grupo;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	public Subjectdb getSubject() {
		return subject;
	}
	public void setSubject(Subjectdb subject) {
		this.subject = subject;
	} 
	
	@Column(name = "USERNAME", nullable = false)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
