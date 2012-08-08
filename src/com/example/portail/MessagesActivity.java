package com.example.portail;


import java.util.ArrayList;
import java.util.HashMap;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MessagesActivity extends MenuActivity {

	public static ListView vueListe;
	public ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
    public HashMap<String, String> map;
    public SimpleAdapter mSchedule;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);       
        
        vueListe = (ListView) getLayoutInflater().inflate(R.layout.message_liste, null);
        
        ChargementMessagesTask chargementMessages = new ChargementMessagesTask();
        chargementMessages.execute();

        mSchedule = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.message_item,
                new String[] {"img", "titre", "description"}, new int[] {R.id.img, R.id.titre, R.id.description});        
        vueListe.setAdapter(mSchedule);
        
        vueListe.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("unchecked")
         	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
        		HashMap<String, String> map = (HashMap<String, String>) vueListe.getItemAtPosition(position);
        		AlertDialog.Builder adb = new AlertDialog.Builder(MessagesActivity.this);
        		adb.setTitle(map.get("titre"));
        		adb.setMessage(map.get("objet"));
        		adb.setPositiveButton("Ok", null);
        		adb.show();
        	}
         });
        
        
        
        setContentView(vueListe);
        
        
    }
    
    private class ChargementMessagesTask extends AsyncTask {

	     protected void onPostExecute(Object result) {
	    	 String messagesJson = (String)result;
	    	 listItem.clear();
	    	 JSONArray jsonArray = null;
	         try {
	 			jsonArray = new JSONArray(messagesJson);
	 			for (int i=0; i<jsonArray.length();i++) {
	 				JSONObject jsonObject = jsonArray.getJSONObject(i);
	 	        	map = new HashMap<String, String>();
	 	            map.put("titre", jsonObject.getString("objet"));
	 	            map.put("description", jsonObject.getString("association"));
	 	            map.put("objet", jsonObject.getString("contenu"));
	 	            map.put("img", String.valueOf(getResources().getIdentifier("drawable/logo_"+jsonObject.getString("association_pseudo"), null, getPackageName())));
	 	            listItem.add(map);
	 	            Log.d("Ajout", jsonObject.getString("objet"));
	 			}
	 			mSchedule.notifyDataSetChanged();
	 		} catch (JSONException e) {
	 			e.printStackTrace();
	 		}
	     }
	     
		@Override
		protected Object doInBackground(Object... arg0) {
			ChargementDonnees chargement = new ChargementDonnees();
			String messagesJson = chargement.getData("http://10.0.2.2:8000/messages/json/");
	    	return messagesJson;
		}
	 }

}
