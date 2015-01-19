package edu.upc.eetac.ea.group1.pandora.android.api.model;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class Group implements Serializable{
	
	String id; 
	String name; 
	String owner;
	List<User> user; 
	List<Post> post;
	List<Notification> notification;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public List<User> getUser() {
		return user;
	}
	public void setUser(List<User> user) {
		this.user = user;
	}
	public List<Post> getPost() {
		return post;
	}
	public void setPost(List<Post> post) {
		this.post = post;
	}
	public List<Notification> getNotification() {
		return notification;
	}
	public void setNotification(List<Notification> notification) {
		this.notification = notification;
	}

}
