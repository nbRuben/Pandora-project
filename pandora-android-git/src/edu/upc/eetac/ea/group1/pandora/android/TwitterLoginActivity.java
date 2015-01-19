package edu.upc.eetac.ea.group1.pandora.android;

import edu.upc.eetac.ea.group1.pandora.android.api.OAuthTokens;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TwitterLoginActivity extends Activity {
	final String TAG = getClass().getName();
	private OAuthProvider httpOauthprovider = new DefaultOAuthProvider(OAuthTokens.REQUEST_URL, OAuthTokens.ACCESS_URL, OAuthTokens.AUTHORIZE_URL);
	private CommonsHttpOAuthConsumer httpOauthConsumer = new CommonsHttpOAuthConsumer(OAuthTokens.CONSUMER_KEY, OAuthTokens.CONSUMER_SECRET);
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onResume() {
		super.onResume();
		WebView webview = new WebView(this);
        webview.getSettings().setJavaScriptEnabled(true);  
        webview.setVisibility(View.VISIBLE);
        setContentView(webview);
        
        try {
        	
        	String authUrl = httpOauthprovider.retrieveRequestToken(httpOauthConsumer, OAuthTokens.OAUTH_CALLBACK_URL);
				 
	        webview.setWebViewClient(new WebViewClient() {  
		        
		       
	        	@Override  
	            public void onPageStarted(WebView view, String url,Bitmap bitmap)  {  
	        		System.out.println("onPageStarted : " + url);
	            }
	        	
	        	@Override  
	            public void onPageFinished(WebView view, String url)  {  
	        		
	        		Uri uri = Uri.parse(url);
	        		
	        		if (uri != null && uri.toString().startsWith(OAuthTokens.OAUTH_CALLBACK_URL)) {
	        			
	        			view.setVisibility(View.INVISIBLE);
	        			 
	        	        String verifier = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
	        	 
	        	        try {
	        	            httpOauthprovider.retrieveAccessToken(httpOauthConsumer, verifier);
	        	            String userAccesToken = httpOauthConsumer.getToken();
	        	            String userAccesTokenSecret = httpOauthConsumer.getTokenSecret();
	        	 
	        	            SharedPreferences settings = getBaseContext().getSharedPreferences("your_app_prefs", 0);
	        	            SharedPreferences.Editor editor = settings.edit();
	        	            editor.putString("user_token", userAccesToken);
	        	            editor.putString("user_token_secret", userAccesTokenSecret);
	        	            editor.commit();
	        	            
	        	            System.out.println("Comprobando tokens: "+userAccesToken+", "+userAccesTokenSecret);
	        	            Intent i = new Intent(TwitterLoginActivity.this, MainActivity.class);
	        	            i.putExtra("username", "twitter");
	        	            startActivity(i);
	        	            finish();
	        	            
	        	 
	        	        } catch (Exception e) {
	        	        	e.printStackTrace();
	        	        }	  		      
	        		}
	        	}	
	        });  
	        
	        webview.loadUrl(authUrl);
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        
	}


}
