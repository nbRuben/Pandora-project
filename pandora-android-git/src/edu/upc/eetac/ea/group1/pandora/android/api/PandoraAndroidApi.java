package edu.upc.eetac.ea.group1.pandora.android.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import edu.upc.eetac.ea.group1.pandora.android.api.model.Comment;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Document;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Group;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Notification;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Post;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Schedule;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Subject;
import edu.upc.eetac.ea.group1.pandora.android.api.model.User;

public class PandoraAndroidApi {

	private final static String BASE_URL = "http://10.89.130.60:8080/pandora-api/";//"http://10.189.61.29:8080/pandora-api/"
	private final static String BASE_URL_VM = "http://147.83.7.200:8080/pandora-api/";//"http://10.0.2.2:8080/pandora-api/";//"http://147.83.7.200:8080/pandora-api/";
	private final static String BASE_URL_VM_SCHEDULE = "http://147.83.7.200:8080/PandoraSchedule-api/";
	private final static String BASE_URL_CASA = "http://192.168.1.196:8080/pandora-api/";

	Gson gson = new Gson();

	public User LoginUser(String username, String pass) {
		User data = new User();
		java.lang.reflect.Type arrayListType = new TypeToken<User>() {
		}.getType();
		gson = new Gson();
		String uurl = BASE_URL_VM + "users/" + username;
		HttpClient httpClient = WebServiceUtils.getHttpClient();
		try {
			HttpResponse response = httpClient.execute(new HttpGet(uurl));
			HttpEntity entity = response.getEntity();
			Reader reader = new InputStreamReader(entity.getContent());
			data = gson.fromJson(reader, arrayListType);
		} catch (Exception e) {
			Log.i("json array",
					"While getting server response server generate error.");
		}
		return data;
	}

	public User addUserRegister(String name, String username, String password,
			String surname, String email) throws ClientProtocolException,
			IOException {
		User data = new User();
		gson = new Gson();
		User useer = new User();
		HttpClient httpClient = WebServiceUtils.getHttpClient();
		java.lang.reflect.Type arrayListType = new TypeToken<User>() {
		}.getType();
		String uurl = BASE_URL_VM + "users";
		HttpPost httpPost = new HttpPost(uurl);
		httpPost.setHeader("Content-Type",
				"application/vnd.pandora.api.user+json");

		data.setName(name);
		data.setUsername(username);
		data.setUserpass(password);
		data.setSurname(surname);
		data.setEmail(email);

		try {
			StringEntity se = new StringEntity(gson.toJson(data));
			httpPost.setEntity(se);
			HttpResponse response = httpClient.execute(httpPost);
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == 200) {

				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				Reader reader = new InputStreamReader(content);
				useer = gson.fromJson(reader, arrayListType);
			} else
				return null;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return useer;

	}

	public User ViewProfile(String username) {
		User data = new User();
		java.lang.reflect.Type arrayListType = new TypeToken<User>() {
		}.getType();
		gson = new Gson();

		String uurl = BASE_URL_VM + "users/" + username;
		HttpClient httpClient = WebServiceUtils.getHttpClient();
		try {
			HttpResponse response = httpClient.execute(new HttpGet(uurl));
			HttpEntity entity = response.getEntity();
			Reader reader = new InputStreamReader(entity.getContent());
			data = gson.fromJson(reader, arrayListType);
		} catch (Exception e) {
			Log.i("json array",
					"While getting server response server generate error.");
		}
		return data;
	}

	public User updateUser(String name, String password, String surname,
			String email, String nom) throws ClientProtocolException,
			IOException {
		User data = new User();
		gson = new Gson();
		User useer = new User();
		HttpClient httpClient = WebServiceUtils.getHttpClient();
		java.lang.reflect.Type arrayListType = new TypeToken<User>() {
		}.getType();
		String uurl = BASE_URL_VM + "users/" + nom;
		HttpPut httpPut = new HttpPut(uurl);
		httpPut.setHeader("Content-Type",
				"application/vnd.pandora.api.user+json");

		if (name != null) {
			data.setName(name);
		}

		if (nom != null) {
			data.setUsername(nom);
		}

		if (password != null) {
			data.setUserpass(password);
		}

		if (surname != null) {
			data.setSurname(surname);
		}

		if (email != null) {
			data.setEmail(email);
		}

		try {
			StringEntity se = new StringEntity(gson.toJson(data));
			httpPut.setEntity(se);
			HttpResponse response = httpClient.execute(httpPut);
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == 200) {

				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				Reader reader = new InputStreamReader(content);
				useer = gson.fromJson(reader, arrayListType);

				// content.close();
			} else
				return null;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return useer;

	}

	public List<Post> getListRecentActivity(String user) throws JSONException {

		Gson gson = new Gson();
		List<Post> data = new ArrayList<Post>();

		java.lang.reflect.Type arrayListType = new TypeToken<ArrayList<Post>>() {
		}.getType();

		HttpClient httpClient = WebServiceUtils.getHttpClient();

		try {
			HttpResponse response = httpClient.execute(new HttpGet(BASE_URL_VM
					+ "users/" + user + "/activityrecent"));
			if (response == null) {
				Log.i("MiniAPI",
						"No se ha recibido bien la respuesta del servidor.");
			}
			HttpEntity entity = response.getEntity();
			Reader reader = new InputStreamReader(entity.getContent());
			data = gson.fromJson(reader, arrayListType);

		} catch (Exception e) {
			Log.i("json array",
					"While getting server response server generate error.");
		}

		return data;

	}

	public List<Comment> getListComments(String post) {
		Gson gson = new Gson();
		List<Comment> data = new ArrayList<Comment>();

		java.lang.reflect.Type arrayListType = new TypeToken<ArrayList<Comment>>() {
		}.getType();

		HttpClient httpClient = WebServiceUtils.getHttpClient();

		try {
			String url = BASE_URL_VM + "posts/" + post;
			HttpResponse response = httpClient.execute(new HttpGet(url));
			if (response == null) {
				Log.i("MiniAPI",
						"No se ha recibido bien la respuesta del servidor.");
			}
			HttpEntity entity = response.getEntity();
			Reader reader = new InputStreamReader(entity.getContent());
			data = gson.fromJson(reader, arrayListType);

		} catch (Exception e) {
			Log.i("json array",
					"While getting server response server generate error.");
		}

		return data;
	}

	public List<Post> getPosts(String idSubject) {
		List<Post> posts = new ArrayList<Post>();
		java.lang.reflect.Type arrayListType = new TypeToken<ArrayList<Post>>() {
		}.getType();
		String url = BASE_URL_VM + "/subjects/" + idSubject + "/posts";

		HttpClient httpClient = WebServiceUtils.getHttpClient();
		try {
			HttpResponse response = httpClient.execute(new HttpGet(url));
			HttpEntity entity = response.getEntity();
			Reader reader = new InputStreamReader(entity.getContent());
			posts = gson.fromJson(reader, arrayListType);
		} catch (Exception e) {

		}
		return posts;

	}

	public List<Subject> searchSubjects(String subject) {
		List<Subject> subjects = new ArrayList<Subject>();
		java.lang.reflect.Type arrayListType = new TypeToken<ArrayList<Subject>>() {
		}.getType();
		String url = BASE_URL_VM + "/subjects/search?subject=" + subject;

		HttpClient httpClient = WebServiceUtils.getHttpClient();
		try {
			HttpResponse response = httpClient.execute(new HttpGet(url));
			HttpEntity entity = response.getEntity();
			Reader reader = new InputStreamReader(entity.getContent());
			subjects = gson.fromJson(reader, arrayListType);
		} catch (Exception e) {

		}
		return subjects;

	}

	public Subject searchSubjectsById(String idSubject) {
		Subject subject = new Subject();
		java.lang.reflect.Type objectType = new TypeToken<Subject>() {
		}.getType();
		String url = BASE_URL_VM + "/subjects/searchById?idSubject="
				+ idSubject;

		HttpClient httpClient = WebServiceUtils.getHttpClient();
		try {
			HttpResponse response = httpClient.execute(new HttpGet(url));
			HttpEntity entity = response.getEntity();
			Reader reader = new InputStreamReader(entity.getContent());
			subject = gson.fromJson(reader, objectType);
		} catch (Exception e) {

		}
		return subject;

	}

	public List<Subject> getMySubjects(String username) {
		List<Subject> subjects = new ArrayList<Subject>();
		java.lang.reflect.Type arrayListType = new TypeToken<ArrayList<Subject>>() {
		}.getType();
		String url = BASE_URL_VM + "users/" + username + "/subjects";
		HttpClient httpClient = WebServiceUtils.getHttpClient();
		try {
			HttpResponse response = httpClient.execute(new HttpGet(url));
			HttpEntity entity = response.getEntity();
			Reader reader = new InputStreamReader(entity.getContent());
			subjects = gson.fromJson(reader, arrayListType);
		} catch (Exception e) {

		}
		return subjects;
	}

	public User getUser(String username) {
		User u = new User();
		java.lang.reflect.Type arrayListType = new TypeToken<User>() {
		}.getType();
		String url = BASE_URL_VM + "users/" + username;
		HttpClient httpClient = WebServiceUtils.getHttpClient();
		try {
			HttpResponse response = httpClient.execute(new HttpGet(url));
			HttpEntity entity = response.getEntity();
			Reader reader = new InputStreamReader(entity.getContent());
			u = gson.fromJson(reader, arrayListType);
		} catch (Exception e) {
			u = null;
		}
		return u;

	}

	public void addPost(String content, String idSubject, String author) {

		String url = BASE_URL_VM + "subjects/" + idSubject + "/posts";
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Content-Type",
				"application/vnd.pandora.api.post+json");
		HttpClient httpClient = WebServiceUtils.getHttpClient();
		Post p = new Post();
		p.setContent(content);
		p.setUser(getUser(author));
		try {
			StringEntity se = new StringEntity(gson.toJson(p));
			httpPost.setEntity(se);
			HttpResponse response = httpClient.execute(httpPost);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addComment(String content, String idSubject, String author) {

		String url = BASE_URL_VM + "subjects/" + idSubject + "/comments";
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Content-Type",
				"application/vnd.pandora.api.comment+json");
		HttpClient httpClient = WebServiceUtils.getHttpClient();
		Post p = new Post();
		p.setContent(content);
		p.setUser(getUser(author));
		try {
			StringEntity se = new StringEntity(gson.toJson(p));
			httpPost.setEntity(se);
			HttpResponse response = httpClient.execute(httpPost);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeComment(Comment c, String owner, String idPost) {
		String url = BASE_URL_VM + "posts/" + idPost;
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Content-Type",
				"application/vnd.pandora.api.comment+json");
		HttpClient httpClient = WebServiceUtils.getHttpClient();
		c.setUser(getUser(owner));
		try {
			StringEntity se = new StringEntity(gson.toJson(c));
			httpPost.setEntity(se);
			HttpResponse response = httpClient.execute(httpPost);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addSubjectToUser(String idSubject, String username) {
		String url = BASE_URL_VM + "users/" + username + "/subjects/"
				+ idSubject;
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Content-Type",
				"application/vnd.pandora.api.user+json");
		HttpClient httpClient = WebServiceUtils.getHttpClient();
		try {
			HttpResponse response = httpClient.execute(httpPost);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void deleteUserFromSubject(String idSubject, String username) {
		String url = BASE_URL_VM + "users/" + username + "/subjects/"
				+ idSubject;
		HttpDelete httpDelete = new HttpDelete(url);
		HttpClient httpClient = WebServiceUtils.getHttpClient();
		try {
			HttpResponse response = httpClient.execute(httpDelete);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Notification> getNotifications(String username) {
		String url = BASE_URL_VM + "/users/" + username + "/notifications";
		List<Notification> notifications = new ArrayList<Notification>();
		java.lang.reflect.Type arrayListType = new TypeToken<ArrayList<Notification>>() {
		}.getType();
		HttpClient httpClient = WebServiceUtils.getHttpClient();
		try {
			HttpResponse response = httpClient.execute(new HttpGet(url));
			HttpEntity entity = response.getEntity();
			Reader reader = new InputStreamReader(entity.getContent());
			notifications = gson.fromJson(reader, arrayListType);
		} catch (Exception e) {

		}
		return notifications;
	}

	public void updateNotification(Notification n) {
		String url = BASE_URL_VM + "users/" + n.getUsername()
				+ "/notifications/" + n.getId();
		HttpPut httpPut = new HttpPut(url);
		httpPut.setHeader("Content-Type",
				"application/vnd.pandora.api.notification+json");
		HttpClient httpClient = WebServiceUtils.getHttpClient();
		try {
			StringEntity se = new StringEntity(gson.toJson(n));
			httpPut.setEntity(se);
			HttpResponse response = httpClient.execute(httpPut);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void uploadDocument(String name, String username, String id,
			String type) {
		String url = "";
		if (type.equals("0")) {
			url = BASE_URL_VM + "subjects/" + id + "/documents/";
		} else {
			url = BASE_URL_VM + "groups/" + id + "/documents/";
		}

		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Content-Type",
				"application/vnd.pandora.api.document+json");
		HttpClient httpClient = WebServiceUtils.getHttpClient();
		Document d = new Document();
		d.setName(name);
		d.setUsername(username);

		try {
			StringEntity se = new StringEntity(gson.toJson(d));
			httpPost.setEntity(se);
			HttpResponse response = httpClient.execute(httpPost);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void saveDocument(File f) {
		String url = BASE_URL_VM + "file";
		HttpPost httpPost = new HttpPost(url);
		HttpClient httpClient = WebServiceUtils.getHttpClient();
		try {
			FileEntity fe = new FileEntity(f, "document");
			httpPost.setEntity(fe);
			HttpResponse response = httpClient.execute(httpPost);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Document> getDocuments(String type, String id) {
		String url = "";
		if (type.equals("0")) {
			url = BASE_URL_VM + "subjects/" + id + "/documents/";
		} else {
			url = BASE_URL_VM + "groups/" + id + "/documents/";
		}
		List<Document> documents = new ArrayList<Document>();
		java.lang.reflect.Type arrayListType = new TypeToken<ArrayList<Document>>() {
		}.getType();
		HttpClient httpClient = WebServiceUtils.getHttpClient();
		try {
			HttpResponse response = httpClient.execute(new HttpGet(url));
			HttpEntity entity = response.getEntity();
			Reader reader = new InputStreamReader(entity.getContent());
			documents = gson.fromJson(reader, arrayListType);
		} catch (Exception e) {

		}

		return documents;
	}

	public ArrayList<Group> getGroupsByUser(String username) {
		ArrayList<Group> grupos = new ArrayList<Group>();
		Log.i("GroupsByUser", "Entramos");
		HttpClient httpclient = WebServiceUtils.getHttpClient();
		HttpGet httpget = new HttpGet(BASE_URL_VM + "groups/users/" + username);
		java.lang.reflect.Type arrayListType = new TypeToken<List<Group>>() {
		}.getType();
		Gson gson = new Gson();
		try {
			Log.i("GroupsByUser", "Try");

			HttpResponse response = httpclient.execute(httpget);

			System.out.println("Response: " + response.toString());

			HttpEntity entity = response.getEntity();
			Reader reader = new InputStreamReader(entity.getContent());
			grupos = gson.fromJson(reader, arrayListType);

		} catch (Exception e) {
			System.out.println("No hay grupos");
			Log.i("GroupsByUser", "No Hay Grupos");
		}
		return grupos;
	}

	public ArrayList<Post> getPostsByGroup(String id) {
		ArrayList<Post> posts = new ArrayList<Post>();
		Log.i("PostsByGroup", "Entramos con id: " + id);
		HttpClient httpclient = WebServiceUtils.getHttpClient();
		HttpGet httpget = new HttpGet(BASE_URL_VM + "groups/posts/" + id);
		java.lang.reflect.Type arrayListType = new TypeToken<List<Post>>() {
		}.getType();
		Gson gson = new Gson();
		try {
			Log.i("PostsByGroup", "Try");

			HttpResponse response = httpclient.execute(httpget);

			System.out.println("Response: " + response.toString());

			HttpEntity entity = response.getEntity();
			Reader reader = new InputStreamReader(entity.getContent());
			posts = gson.fromJson(reader, arrayListType);
		} catch (Exception e) {

			Log.i("PostsByGroup", "No Hay Posts");
		}
		return posts;
	}

	public ArrayList<Group> getGroups() {
		ArrayList<Group> grupos = new ArrayList<Group>();
		Log.i("GroupsByUser", "Entramos");
		HttpClient httpclient = WebServiceUtils.getHttpClient();
		HttpGet httpget = new HttpGet(BASE_URL_VM + "groups/");
		java.lang.reflect.Type arrayListType = new TypeToken<List<Group>>() {
		}.getType();
		Gson gson = new Gson();

		try {
			Log.i("GroupsByUser", "Try");

			HttpResponse response = httpclient.execute(httpget);

			HttpEntity entity = response.getEntity();
			Reader reader = new InputStreamReader(entity.getContent());
			grupos = gson.fromJson(reader, arrayListType);

		} catch (Exception e) {
			Log.i("GroupsByUser", "No Hay Grupos");
		}

		return grupos;
	}

	public void AddToGroup(String username, String id) {
		Log.i("AddToGroup", "Entramos: User" + username + "id " + id);
		HttpClient httpclient = WebServiceUtils.getHttpClient();
		HttpPost httppost = new HttpPost(BASE_URL_VM + "groups/" + username + "/"
				+ id);
		Log.i("AddToGroup", "url: " + BASE_URL_VM + "groups/" + username + "/"
				+ id);
		try {
			Log.i("GroupsByUser", "Try");
			httpclient.execute(httppost);
		} catch (Exception e) {
			Log.i("AddToGroup", "No se ha podido realizar");
		}
		Log.i("AddToGroup", "Done!");
	}

	public void ExitOfGroup(String username, String id) {
		Log.i("ExitOfGroup", "Entramos: User: " + username + " id: " + id);
		HttpClient httpclient = WebServiceUtils.getHttpClient();
		HttpDelete httpdel = new HttpDelete(BASE_URL_VM + "groups/" + username
				+ "/" + id);
		Log.i("ExitOfGroup", "url: " + BASE_URL_VM + "groups/" + username + "/"
				+ id);
		try {
			Log.i("ExitOfGroup", "Try");
			httpclient.execute(httpdel);
			Log.i("ExitOfGroup", "Done!");
		} catch (Exception e) {
			Log.i("ExitOfGroup", "No se ha podido realizar");
		}

	}

	public Group addGroup(String groupName, String userName) {
		Log.i("Crear Grupo", "Entramos: User" + userName
				+ " y Nombre de grupo " + groupName);
		HttpClient httpclient = WebServiceUtils.getHttpClient();
		HttpPost httppost = new HttpPost(BASE_URL_VM + "groups/");
		Log.i("Crear Group", "url: " + BASE_URL_VM + "groups/");
		httppost.setHeader("Content-Type",
				"application/vnd.pandora.api.group+json");
		HttpClient httpClient = WebServiceUtils.getHttpClient();
		Group g = new Group();
		g.setName(groupName);
		g.setOwner(userName);
		Log.i("Crear Group", "Nombre en grupo: " + g.getName());
		try {
			StringEntity se = new StringEntity(gson.toJson(g));
			httppost.setEntity(se);
			Log.i("Crear Group", "Metemos el objeto");
			HttpResponse response = httpClient.execute(httppost);
			HttpEntity entity = response.getEntity();
			Reader reader = new InputStreamReader(entity.getContent());

			java.lang.reflect.Type arrayListType = new TypeToken<Group>() {
			}.getType();
			g = gson.fromJson(reader, arrayListType);
			Log.i("Crear Group", "El id del grupo es " + g.getId());
			AddToGroup(userName, g.getId());
			Log.i("Crear Group", "Done");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return g;
	}

	public ArrayList<User> getParticipants(String idgroup) {
		ArrayList<User> users = new ArrayList<User>();
		Log.i("getParticipants", "Entramos");
		HttpClient httpclient = WebServiceUtils.getHttpClient();
		HttpGet httpget = new HttpGet(BASE_URL_VM + "groups/" + idgroup + "/users");
		java.lang.reflect.Type arrayListType = new TypeToken<List<User>>() {
		}.getType();
		Gson gson = new Gson();

		try {
			Log.i("getParticipants", "Try");

			HttpResponse response = httpclient.execute(httpget);

			HttpEntity entity = response.getEntity();
			Reader reader = new InputStreamReader(entity.getContent());
			users = gson.fromJson(reader, arrayListType);

		} catch (Exception e) {
			Log.i("GroupsByUser", "No Hay Grupos");
		}

		return users;
	}

	public ArrayList<User> getUsers(String Username) {
		ArrayList<User> users = new ArrayList<User>();
		Log.i("getUsers", "Entramos");
		HttpClient httpclient = WebServiceUtils.getHttpClient();
		HttpGet httpget = new HttpGet(BASE_URL_VM + "groups/user/search?user="
				+ Username);
		java.lang.reflect.Type arrayListType = new TypeToken<List<User>>() {
		}.getType();
		Gson gson = new Gson();

		try {
			Log.i("getUsers", "Try");

			HttpResponse response = httpclient.execute(httpget);


			HttpEntity entity = response.getEntity();
			Reader reader = new InputStreamReader(entity.getContent());
			users = gson.fromJson(reader, arrayListType);

		} catch (Exception e) {
			Log.i("getUsers", "No Hay users");
		}

		return users;
	}


	public void inviteUser(String groupid,String username){
		Log.i("inviteUser", "Entramos: User "+" id "+groupid + " y invitamos a: "+username);
		HttpClient httpclient = WebServiceUtils.getHttpClient();
		HttpPost httppost = new HttpPost(BASE_URL_VM+"groups/notification/"+groupid+"/"+username);
		try{
			Log.i("inviteUser", "Try");
			httpclient.execute(httppost);
		}catch (Exception e){
			Log.i("inviteUser", "No se ha podido realizar");
			}
		Log.i("inviteUser", "Done!");
	}
	public void addPostToGroup(String content, String idSubject, String author) {

		String url = BASE_URL_VM + "groups/" + idSubject + "/posts";
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Content-Type",
				"application/vnd.pandora.api.post+json");
		HttpClient httpClient = WebServiceUtils.getHttpClient();
		Post p = new Post();
		p.setContent(content);
		p.setUser(getUser(author));
		try {
			StringEntity se = new StringEntity(gson.toJson(p));
			httpPost.setEntity(se);

			HttpResponse response = httpClient.execute(httpPost);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public List<Schedule> getMySchedule(List<Subject> subjects) {

		List<Schedule> schedules = new ArrayList<Schedule>();

		java.lang.reflect.Type arrayListType = new TypeToken<ArrayList<Schedule>>() {
		}.getType();

		String url = BASE_URL_VM_SCHEDULE + "schedule/schedules/";

		String url_subjects = subjects.get(0).getId();
		for (int i = 1; i < subjects.size(); i++) {
			url_subjects = url_subjects + "," + subjects.get(i).getId();
		}

		url = url + url_subjects;
		HttpClient httpClient = WebServiceUtils.getHttpClient();
		try {
			HttpResponse response = httpClient.execute(new HttpGet(url));
			HttpEntity entity = response.getEntity();
			Reader reader = new InputStreamReader(entity.getContent());
			schedules = gson.fromJson(reader, arrayListType);
		} catch (Exception e) {

		}
		return schedules;

	}

}
