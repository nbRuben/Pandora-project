package edu.upc.eetac.ea.group1.pandora.android.api.model;

public class Comment {
	
	int id;
	String content;
	User user;
	Post post;
	
	public Comment (String c, User u, Post p ){
		this.content=c;
		this.user=u;
		this.post=p;
	}
	
	public Comment(){}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}
	
	

}
