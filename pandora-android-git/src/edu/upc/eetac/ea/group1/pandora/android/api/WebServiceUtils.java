package edu.upc.eetac.ea.group1.pandora.android.api;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class WebServiceUtils {

	 public static HttpClient getHttpClient(){
	        HttpParams httpParameters = new BasicHttpParams();
	        int timeoutConnection = 50000;
	        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
	        int timeoutSocket = 50000;
	        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);           
	        HttpClient httpclient = new DefaultHttpClient(httpParameters);          
	        return httpclient;
	     }

	}
