package edu.upc.eetac.ea.group1.pandora.android.api.model;

import java.util.ArrayList;
import java.util.List;


public class Schedule {
	
	public int id;
	public String subject;
	public List<GroupSubject> groups;
	
	public Schedule (){
		super();
		this.groups = new ArrayList<GroupSubject>();
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public List<GroupSubject> getGroups() {
		return groups;
	}

	public void setGroups(List<GroupSubject> groups) {
		this.groups = groups;
	}
	
}
