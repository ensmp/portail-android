package com.example.portail;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class TrombiListeActivity extends Activity {

	public ListView vueListe;
	public ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
    public HashMap<String, String> map;
    public SimpleAdapter mSchedule;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
         
         
         vueListe = (ListView) getLayoutInflater().inflate(R.layout.message_liste, null);
         
         ChargementTrombiTask chargementTrombi = new ChargementTrombiTask();
         chargementTrombi.execute();
    

       
         mSchedule = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.message_item,
                 new String[] {"img", "titre", "description"}, new int[] {R.id.img, R.id.titre, R.id.description});        
         vueListe.setAdapter(mSchedule);
         
         vueListe.setOnItemClickListener(new OnItemClickListener() {
 			@SuppressWarnings("unchecked")
          	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
         		HashMap<String, String> map = (HashMap<String, String>) vueListe.getItemAtPosition(position);
         		AlertDialog.Builder adb = new AlertDialog.Builder(TrombiListeActivity.this);
         		adb.setTitle(map.get("username"));
         		adb.setMessage(map.get("username"));
         		adb.setPositiveButton("Ok", null);
         		adb.show();
         	}
          });
         
         setContentView(vueListe);
        
    }
    
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
           return true;
     }
        return false;
    }
    
    
    private class ChargementTrombiTask extends AsyncTask {

	     protected void onPostExecute(Object result) {
	    	 String trombiJson = (String)result;
	    	 listItem.clear();
	    	 JSONArray jsonArray = null;
	         try {
	 			jsonArray = new JSONArray(trombiJson);
	 			for (int i=0; i<jsonArray.length();i++) {
	 				JSONObject jsonObject = jsonArray.getJSONObject(i);
	 				map = new HashMap<String, String>();
	 	            map.put("titre", jsonObject.getString("username"));
	 	            map.put("description", jsonObject.getString("first_name") + " " + jsonObject.getString("last_name"));
	 	            map.put("img", String.valueOf(getResources().getIdentifier("drawable/trombi_"+jsonObject.getString("username"), null, getPackageName())));
	 	            listItem.add(map);
	 			}
	 			mSchedule.notifyDataSetChanged();
	 		} catch (JSONException e) {
	 			e.printStackTrace();
	 		}
	     }
	     
		@Override
		protected Object doInBackground(Object... arg0) {
			ChargementDonnees chargement = new ChargementDonnees();
			String trombiJson = chargement.getData("http://10.0.2.2:8000/people/json/");
	    	return trombiJson;
		}
	 }
	
}
