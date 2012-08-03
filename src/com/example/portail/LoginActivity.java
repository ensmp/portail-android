package com.example.portail;

import org.apache.http.HttpResponse;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {
    
	Button btnLogin;
	Button btnLinkToRegister;
	EditText inputUsername;
	EditText inputPassword;
	TextView loginErrorMsg;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        inputUsername = (EditText) findViewById(R.id.loginUsername);
		inputPassword = (EditText) findViewById(R.id.loginPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		loginErrorMsg = (TextView) findViewById(R.id.login_error);
        
        btnLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				String username = inputUsername.getText().toString();
				String password = inputPassword.getText().toString();
				Log.d("Button", "Login");
				LoginTask identificationHttp =new LoginTask();
				identificationHttp.execute();
			}
		});
    }
	
	public void succesAuthentification() {
		loginErrorMsg.setText("Authentification réussie");
		Intent intentMessages = new Intent(this, MessagesActivity.class);
		startActivity(intentMessages);
		
	}
	
	public void echecAuthentification() {
		loginErrorMsg.setText("Identifiant ou mot de passe incorrects");
	}
	
	
	private class LoginTask extends AsyncTask {

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
			HttpResponse response = chargement.authentifier(inputUsername.getText().toString(), inputPassword.getText().toString());
	    	 return response;
		}
	 }
}