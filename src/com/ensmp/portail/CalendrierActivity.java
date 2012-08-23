package com.ensmp.portail;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ensmp.portail.CalendrierVue.OnDispatchDateSelectListener;
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
import android.widget.LinearLayout;

import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class CalendrierActivity extends MenuActivity implements OnDispatchDateSelectListener {
	
	JSONArray tableauEvenements;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);       
        
        ChargementCalendrierTask chargementCalendrier = new ChargementCalendrierTask();
        chargementCalendrier.execute();
        
        LinearLayout calendrierGlobal = (LinearLayout) getLayoutInflater().inflate(R.layout.calendrier_global, null);
        CalendrierVue vueCalendrier = (CalendrierVue) calendrierGlobal.findViewById(R.id.calendrier);
        vueCalendrier.setOnDispatchDateSelectListener(this);
        
        setContentView(R.layout.calendrier_global);
                
    }
    
    private class ChargementCalendrierTask extends AsyncTask {

	     protected void onPostExecute(Object result) {
	    	 String messagesJson = (String)result;
	    	 tableauEvenements = null;
	    	 
	         try {
	        	 tableauEvenements = new JSONArray(messagesJson);	 			
	 		} catch (JSONException e) {
	 			e.printStackTrace();
	 		}
	     }
	     
		@Override
		protected Object doInBackground(Object... arg0) {
			ChargementDonnees chargement = new ChargementDonnees();
			String calendrierJson = chargement.getData(getString(R.string.domain)+"/calendrier/json/");
	    	return calendrierJson;
		}
	 }

	public void onDispatchDateSelect(Date date) {
		SimpleDateFormat formatDateJson = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		TextView texteDate = (TextView) findViewById(R.id.afficher_date);
		texteDate.setText("");
		for (int i=0; i<tableauEvenements.length();i++) {
				JSONObject jsonObject;
				try {
					jsonObject = tableauEvenements.getJSONObject(i);
					Date dateStart = formatDateJson.parse(jsonObject.getString("start"));
					if (date.getDate() == dateStart.getDate() && date.getMonth() == dateStart.getMonth() && date.getYear() == dateStart.getYear()) {
						texteDate.setText(jsonObject.getString("title"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	        	
			}
		
	}

}
