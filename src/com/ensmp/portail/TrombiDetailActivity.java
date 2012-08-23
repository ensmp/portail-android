package com.ensmp.portail;



import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ensmp.portail.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.TableLayout;


public class TrombiDetailActivity extends MenuActivity implements OnClickListener {

	public RelativeLayout vueDetail;
    public HashMap<String, String> map;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
         
    	 Intent intent= getIntent();
    	 String username = intent.getStringExtra("username");
    	 
    	 ChargementEleveTask chargementEleve = new ChargementEleveTask();
         chargementEleve.execute(username);
                  

    	 vueDetail = (RelativeLayout) getLayoutInflater().inflate(R.layout.trombi_detail, null);
         setContentView(vueDetail);
        
         TextView texteCo = (TextView) findViewById(R.id.trombiTexteCo);
         texteCo.setOnClickListener(this);
    }    
    
    private class ChargementEleveTask extends AsyncTask {

	     protected void onPostExecute(Object result) {
	    	 String trombiJson = (String)result;
	    	 try {
				JSONObject jsonObject = new JSONObject(trombiJson);
				
				ImageView image = (ImageView) findViewById(R.id.trombiPhoto);
				image.setImageResource(getResources().getIdentifier("drawable/trombi_"+jsonObject.getString("username"), null, getPackageName()));
				
				TextView texte = (TextView) findViewById(R.id.trombiTexteNom);
				texte.setText(jsonObject.getString("first_name") + " " + jsonObject.getString("last_name"));
				texte = (TextView) findViewById(R.id.trombiTextePromo);
				texte.setText(jsonObject.getString("promo"));
				texte = (TextView) findViewById(R.id.trombiTexteDate);
				texte.setText(jsonObject.getString("birthday"));
				texte = (TextView) findViewById(R.id.trombiTexteTel);
				texte.setText(jsonObject.getString("phone"));
				texte = (TextView) findViewById(R.id.trombiTexteChambre);
				texte.setText(jsonObject.getString("chambre"));
				
				texte = (TextView) findViewById(R.id.trombiTexteCo);
				String str = jsonObject.getString("co");
				if (str.equals("null")) str = "";
				texte.setText(str);
				
				texte = (TextView) findViewById(R.id.trombiTexteParrain);
				str = jsonObject.getString("parrain");
				if (str.equals("null")) str = "";
				texte.setText(str);
				
				texte = (TextView) findViewById(R.id.trombiTexteFillot);
				str = jsonObject.getString("fillot");
				if (str.equals("null")) str = "";
				texte.setText(str);
				
				TableLayout tableau = (TableLayout) findViewById(R.id.trombiTableau);								
				LayoutParams params = new LayoutParams();
				params.weight = 1;
				
				JSONArray tableauAssoces = jsonObject.getJSONArray("assoces");
				for (int i=0; i<tableauAssoces.length(); i++) {
					if (i==0) {
						texte = (TextView) findViewById(R.id.trombiTexteAssoce);
						texte.setText(tableauAssoces.getJSONObject(i).getString("nom"));
					}
					else {
						TableRow ligne = new TableRow(TrombiDetailActivity.this);
						ligne.addView(new TextView(TrombiDetailActivity.this), params);
						TextView texteAssoce = new TextView(TrombiDetailActivity.this);
						texteAssoce.setTextAppearance(getApplicationContext(), R.style.TexteTrombiContenu);
						texteAssoce.setTextColor(Color.BLUE);
						texteAssoce.setText(tableauAssoces.getJSONObject(i).getString("nom"));
						ligne.addView(texteAssoce, params);
						tableau.addView(ligne, tableau.getChildCount());
					}					
				}
				
				
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
	     }
	     
		@Override
		protected Object doInBackground(Object... params) {
			String username = (String) params[0];			
			ChargementDonnees chargement = new ChargementDonnees();
			String trombiJson = chargement.getData("http://10.0.2.2:8000/people/"+username+"/json/");
	    	return trombiJson;
		}
	 }


	public void onClick(View v) {
		if (v.getId() == R.id.trombiTexteCo) {
	        TextView texte = (TextView) findViewById(R.id.trombiTexteCo);
	        if (!texte.getText().equals("")) {
	        	Intent eleveIntent = new Intent(this, TrombiDetailActivity.class);
	     		eleveIntent.putExtra("username", texte.getText());
	     		startActivity(eleveIntent);
	        }
		}		
		else if (v.getId() == R.id.trombiTexteParrain) {
	        TextView texte = (TextView) findViewById(R.id.trombiTexteParrain);
	        if (!texte.getText().equals("")) {
	        	Intent eleveIntent = new Intent(this, TrombiDetailActivity.class);
	     		eleveIntent.putExtra("username", texte.getText());
	     		startActivity(eleveIntent);
	        }
		}
		else if (v.getId() == R.id.trombiTexteFillot) {
	        TextView texte = (TextView) findViewById(R.id.trombiTexteParrain);
	        if (!texte.getText().equals("")) {
	        	Intent eleveIntent = new Intent(this, TrombiDetailActivity.class);
	     		eleveIntent.putExtra("username", texte.getText());
	     		startActivity(eleveIntent);
	        }
		}
	}
	
}
