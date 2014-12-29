package edu.upc.eetac.ea.group1.pandora.api;

import java.util.ArrayList;
import java.util.List;

import edu.upc.eetac.ea.group1.pandora.api.models.Comment;
import edu.upc.eetac.ea.group1.pandora.api.models.Commentdb;
import edu.upc.eetac.ea.group1.pandora.api.models.Group;
import edu.upc.eetac.ea.group1.pandora.api.models.Groupdb;
import edu.upc.eetac.ea.group1.pandora.api.models.Notification;
import edu.upc.eetac.ea.group1.pandora.api.models.Notificationdb;
import edu.upc.eetac.ea.group1.pandora.api.models.Post;
import edu.upc.eetac.ea.group1.pandora.api.models.Postdb;
import edu.upc.eetac.ea.group1.pandora.api.models.Subject;
import edu.upc.eetac.ea.group1.pandora.api.models.Subjectdb;
import edu.upc.eetac.ea.group1.pandora.api.models.User;
import edu.upc.eetac.ea.group1.pandora.api.models.Userdb;

public class ConvertLists {
	
	//Intancia
		private static ConvertLists instance = null;
		
		
		
		private ConvertLists(){
			super();
		}
		
		public static ConvertLists getInstance(){
			if(instance==null){
				instance = new ConvertLists();
			}
			return instance;
		}

	public List<Post> convertListPosts( List<Postdb> postsdb){
		
		List<Post> posts = new ArrayList<Post>();
		for (Postdb postdb : postsdb){

			Post post = postdb.convertFromDB();
			posts.add(post);
		}
		
		return posts;
	}
	public List<Group> convertListGroups( List<Groupdb> groupsdb){
		
		List<Group> groups = new ArrayList<Group>();
		for (Groupdb groupdb : groupsdb){

			Group group = groupdb.convertFromDB();
			groups.add(group);
		}
		
		return groups;
	}
	public List<Subject> convertListSubjects( List<Subjectdb> subjectsdb){
		
		List<Subject> subjects = new ArrayList<Subject>();
		for (Subjectdb subjectdb : subjectsdb){

			Subject subject = subjectdb.convertFromDB();
			subjects.add(subject);
		}
		
		return subjects;
	}
	
	public List<User> convertListUsers(List<Userdb> usersdb){
		
		List<User> users = new ArrayList<User>();
		for (Userdb userdb : usersdb){
			
			User user = userdb.convertFromDB();
			users.add(user);
		}
		return users;
	}
	
	public List<Notification> convertListNotifications(List<Notificationdb> notificationsdb){
		
		List<Notification> notifications = new ArrayList<Notification>();
		for (Notificationdb notificationdb : notificationsdb){
			
			Notification notification = notificationdb.convertFromDB();
			notifications.add(notification);
		}
		return notifications;
	}
	
	public List<Comment> convertListComments (List<Commentdb> commentsdb){
		List<Comment> comments = new ArrayList<Comment>();
		for(Commentdb commentdb: commentsdb){
			Comment comment = commentdb.convertFromDB();
			comments.add(comment);
		}
		return comments;
	}
}
