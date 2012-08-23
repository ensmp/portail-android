package com.ensmp.portail;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;

import com.ensmp.portail.R;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoginActivity extends Activity {
    
	Button btnLogin;
	Button btnLinkToRegister;
	EditText inputUsername;
	EditText inputPassword;
	TextView loginErrorMsg;
	ProgressBar spinner; 
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                
        
        chargerPreferences();
        
        setContentView(R.layout.login);
                        
        inputUsername = (EditText) findViewById(R.id.loginUsername);
		inputPassword = (EditText) findViewById(R.id.loginPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		loginErrorMsg = (TextView) findViewById(R.id.login_error);
		spinner = (ProgressBar) findViewById(R.id.loginloading);
        
        btnLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				String username = inputUsername.getText().toString();
				String password = inputPassword.getText().toString();
				LoginTask identificationHttp =new LoginTask();
				identificationHttp.execute();
			}
		});
    }
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);		
		if (hasFocus) {
			chargerPreferences();
		}
	}
	
	public void chargerPreferences() {
		SharedPreferences settings = getSharedPreferences(ChargementDonnees.PREFERENCES, 0);
	    String csrftoken = settings.getString("csrftoken", "");
	    String sessionid = settings.getString("sessionid", ""); 	    
	    
	    if (!csrftoken.equals("") && !sessionid.equals("")) {
	    	ChargementDonnees.changerCookies(csrftoken, sessionid, settings.getString("domain", ""));
	    	Intent intentMessages = new Intent(this, MessagesActivity.class);
			startActivity(intentMessages);
	    }
	}
	
	public void enregistrerPreferences() {
		SharedPreferences settings = getSharedPreferences(ChargementDonnees.PREFERENCES, 0);
		SharedPreferences.Editor editor = settings.edit();
	    editor.putString("csrftoken", ChargementDonnees.getToken());
	    editor.putString("sessionid", ChargementDonnees.getSessionId());
	    editor.putString("domain", ChargementDonnees.getDomain());
	    editor.commit();		
	}
	
	public void succesAuthentification() {
		spinner.setVisibility(View.GONE);
		enregistrerPreferences();
		loginErrorMsg.setText("Authentification réussie");
		Intent intentMessages = new Intent(this, MessagesActivity.class);
		startActivity(intentMessages);
		
	}
	
	public void echecAuthentification() {
		spinner.setVisibility(View.GONE);
		loginErrorMsg.setText("Identifiant ou mot de passe incorrects");
	}
	
	
	private class LoginTask extends AsyncTask {

		@Override
		protected void onPreExecute() {			
			super.onPreExecute();
			spinner.setVisibility(View.VISIBLE);
		}
		
	     protected void onPostExecute(Object result) {
	    	 HttpResponse response = (HttpResponse)result;
	    	 if (response.getStatusLine().getStatusCode() == 302) {
	    			//On est redirigé, authentification réussie !
	    			succesAuthentification();
	    		}
	    		else {
	    			//Code 200, pas de redirection, donc authentification echouee
	    			echecAuthentification();
	    		}
	     }
	     
		@Override
		protected Object doInBackground(Object... arg0) {
			ChargementDonnees chargement = new ChargementDonnees();
			HttpResponse response = chargement.authentifier(getString(R.string.domain),inputUsername.getText().toString(), inputPassword.getText().toString());
	    	 return response;
		}
	 }
}