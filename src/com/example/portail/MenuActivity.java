package com.example.portail;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MenuActivity extends Activity {

	public boolean onCreateOptionsMenu(Menu menu) {
   	 
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.layout.menu, menu);
 
        //Il n'est pas possible de modifier l'icône d'entête du sous-menu via le fichier XML on le fait donc en JAVA
    	//menu.getItem(0).getSubMenu().setHeaderIcon(R.drawable.option_white);
 
        return true;
     }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
           case R.id.menu_messages:
   			   startActivity(new Intent(this, MessagesActivity.class));
               return true;
           case R.id.menu_trombi:
   			   startActivity(new Intent(this, TrombiListeActivity.class));
               return true;
           case R.id.menu_petits_cours:
   			   startActivity(new Intent(this, PetitsCoursActivity.class));
               return true;
          case R.id.menu_calendrier:
        	  startActivity(new Intent(this, CalendrierActivity.class));
              return true;
          case R.id.menu_logout:
        	  //On supprime les préférences
              SharedPreferences settings = getSharedPreferences("Authentification", 0);
      		  SharedPreferences.Editor editor = settings.edit();
      	      editor.clear();
      	      editor.commit();
      	      //On supprime les cookies
      	      ChargementDonnees.supprimerCookies();
      	      startActivity(new Intent(this, LoginActivity.class));
      	      return true;  
        }
        return false;
    }
}
