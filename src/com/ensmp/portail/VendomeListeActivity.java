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

public class VendomeListeActivity extends MenuActivity {

	public static ListView vueListe;
	public ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
    public HashMap<String, String> map;
    public SimpleAdapter mSchedule;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);       
        
        vueListe = (ListView) getLayoutInflater().inflate(R.layout.message_liste, null);
        
        ChargementVendomesTask chargementVendomes = new ChargementVendomesTask();
        chargementVendomes.execute();

        mSchedule = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.message_item,
                new String[] {"img", "titre", "description"}, new int[] {R.id.img, R.id.titre, R.id.description});        
        vueListe.setAdapter(mSchedule);
        
        vueListe.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("unchecked")
         	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
        		HashMap<String, String> map = (HashMap<String, String>) vueListe.getItemAtPosition(position);
        		/*AlertDialog.Builder adb = new AlertDialog.Builder(VendomeListeActivity.this);
        		adb.setTitle(map.get("titre"));
        		adb.setMessage(map.get("objet"));
        		adb.setPositiveButton("Ok", null);
        		adb.show();*/
        		Intent i = new Intent(VendomeListeActivity.this, VendomeDetailActivity.class);
        		i.putExtra("url", map.get("url"));
        		startActivity(i);
        	}
         });
                
        
        setContentView(vueListe);
        
        
    }
    
    private class ChargementVendomesTask extends AsyncTask {

	     protected void onPostExecute(Object result) {
	    	 String vendomesJson = (String)result;
	    	 listItem.clear();
	    	 JSONArray jsonArray = null;
	         try {
	 			jsonArray = new JSONArray(vendomesJson);
	 			for (int i=0; i<jsonArray.length();i++) {
	 				JSONObject jsonObject = jsonArray.getJSONObject(i);
	 	        	map = new HashMap<String, String>();
	 	            map.put("titre", jsonObject.getString("titre"));
	 	            map.put("description", jsonObject.getString("date"));
	 	            map.put("url", jsonObject.getString("fichier"));
	 	            //map.put("img", String.valueOf(getResources().getIdentifier("drawable/logo_"+jsonObject.getString("association_pseudo"), null, getPackageName())));
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
			String vendomesJson = chargement.getData(getString(R.string.domain)+"/associations/vendome/archives/json/");
	    	return vendomesJson;
		}
	 }

}
