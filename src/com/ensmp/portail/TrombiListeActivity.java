package com.ensmp.portail;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ensmp.portail.R;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class TrombiListeActivity extends MenuActivity {

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
                 new String[] {"img", "username", "nom"}, new int[] {R.id.img, R.id.titre, R.id.description});        
         vueListe.setAdapter(mSchedule);
         
         vueListe.setOnItemClickListener(new OnItemClickListener() {
 			@SuppressWarnings("unchecked")
          	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
         		HashMap<String, String> map = (HashMap<String, String>) vueListe.getItemAtPosition(position);
          		
         		Intent eleveIntent = new Intent(TrombiListeActivity.this, TrombiDetailActivity.class);
         		eleveIntent.putExtra("username", map.get("username"));
         		startActivity(eleveIntent);

         	}
          });
         
         setContentView(vueListe);
        
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
	 	            map.put("username", jsonObject.getString("username"));
	 	            map.put("nom", jsonObject.getString("first_name") + " " + jsonObject.getString("last_name"));
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
			String trombiJson = chargement.getData(getString(R.string.domain)+"/people/json/");
	    	return trombiJson;
		}
	 }
	
}
