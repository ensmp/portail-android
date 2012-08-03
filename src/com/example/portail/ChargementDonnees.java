package com.example.portail;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class ChargementDonnees
{
	
	private static CookieStore cookieStore;
	private static HttpContext localContext;

	public ChargementDonnees() {
		super();
		if (cookieStore == null) {
			cookieStore = new BasicCookieStore();
	    	localContext = new BasicHttpContext();
	    	localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		}		
	}
	

	public static HttpResponse authentifier(String username, String password) {
		getData("http://10.0.2.2:8000/accounts/login/");
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("csrfmiddlewaretoken",getToken()));
		nameValuePairs.add(new BasicNameValuePair("username", username));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		
		HttpResponse response = postData("http://10.0.2.2:8000/accounts/login/", nameValuePairs);
		return response;
	}
	
	public static String getData(String url) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		
		try {
			HttpResponse response = httpclient.execute(httpget, localContext);
			if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String reponseStr = EntityUtils.toString(response.getEntity());				
				return reponseStr;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return "erreur Http get";
	}
	
	public static HttpResponse postData(String url, List<NameValuePair> nameValuePairs) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		
		//Empecher la redirection, pour recevoir le code HTTP 302 au lieu de HTTP 200, et savoir o� on est redirig�
		httpclient.setRedirectHandler(new RedirectHandler() {			
			public boolean isRedirectRequested(HttpResponse response,
					HttpContext context) {
				return false;
			}
			
			public URI getLocationURI(HttpResponse response, HttpContext context)
					throws ProtocolException {
				return null;
			}
		});
		
		HttpPost httppost = new HttpPost(url);
		try {			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost, localContext);
			return response;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return null;
	}
	
	public static String getToken() {
		for (int i=0; i<cookieStore.getCookies().size();i++) {
        	if (cookieStore.getCookies().get(i).getName().equals("csrftoken")) {
        		return cookieStore.getCookies().get(i).getValue();
        	}
        }
		return "Pas de CSRF Token dans les cookies ...";
	}
}