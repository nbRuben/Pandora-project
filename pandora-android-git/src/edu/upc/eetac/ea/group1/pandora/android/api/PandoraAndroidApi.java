package edu.upc.eetac.ea.group1.pandora.android.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import edu.upc.eetac.ea.group1.pandora.android.api.model.Notification;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Post;
import edu.upc.eetac.ea.group1.pandora.android.api.model.Subject;
import edu.upc.eetac.ea.group1.pandora.android.api.model.User;

public class PandoraAndroidApi {

	private final static String BASE_URL = "http://10.89.130.60:8080/pandora-api/";
	private final static String BASE_URL_VM = "http://10.0.2.2:8080/pandora-api/";
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
			String surname, String email) throws ClientProtocolException, IOException {
		User data = new User();
		gson = new Gson();
		User useer = new User();
		HttpClient httpClient = WebServiceUtils.getHttpClient();
		java.lang.reflect.Type arrayListType = new TypeToken<User>() {
		}.getType();
		String uurl = BASE_URL + "users";
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

	public List<Post> getListRecentActivity(String user) throws JSONException {

		Gson gson = new Gson();
		List<Post> data = new ArrayList<Post>();

		java.lang.reflect.Type arrayListType = new TypeToken<ArrayList<Post>>() {
		}.getType();

		HttpClient httpClient = WebServiceUtils.getHttpClient();

		try {
			HttpResponse response = httpClient.execute(new HttpGet(BASE_URL_VM
					+ "users/" + user + "/activityrecent"));
			Log.i("MiniAPI", "GET a la URL: " + BASE_URL_VM + "users/" + user
					+ "/activityrecent");
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

	public List<Post> getComments(String idSubject) {
		List<Post> posts = new ArrayList<Post>();
		java.lang.reflect.Type arrayListType = new TypeToken<ArrayList<Post>>() {
		}.getType();
		String url = BASE_URL_VM + "/subjects/" + idSubject + "/comments";

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

	public List<Subject> getSubjects(String subject) {
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
		String url = BASE_URL_VM + "/users/" + username;

		HttpClient httpClient = WebServiceUtils.getHttpClient();
		try {
			HttpResponse response = httpClient.execute(new HttpGet(url));
			HttpEntity entity = response.getEntity();
			Reader reader = new InputStreamReader(entity.getContent());
			u = gson.fromJson(reader, arrayListType);
		} catch (Exception e) {

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
		Date date = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        String dateString =  formateador.format(date).toString();
		p.setDate(dateString);

		try {
			StringEntity se = new StringEntity(gson.toJson(p));
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

	public List<Notification> getNotifications() {
		String url = BASE_URL_VM + "/notifications";
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

}