package edu.upc.eetac.ea.group1.pandora.android.api.model;

import java.util.List;

public class Group {
	
	int id; 
	String name; 
	List<User> user; 
	List<Post> post;
	List<Notification> notification;
	
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
