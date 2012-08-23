package com.ensmp.portail;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ensmp.portail.R;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ImageListActivity extends MenuActivity {

	public ListView vueListe;
	public ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
    public HashMap<String, String> map;
    public SimpleAdapter mSchedule;
    
    public String[][] imageUrls;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
         
         
         vueListe = (ListView) getLayoutInflater().inflate(R.layout.message_liste, null);
         
         ChargementAlbumsTask chargementAlbums = new ChargementAlbumsTask();
         chargementAlbums.execute();
    

       
         mSchedule = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.message_item,
                 new String[] {"title", "title"}, new int[] {R.id.titre, R.id.description});        
         vueListe.setAdapter(mSchedule);
         
         vueListe.setOnItemClickListener(new OnItemClickListener() {
 			@SuppressWarnings("unchecked")
          	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
         		HashMap<String, String> map = (HashMap<String, String>) vueListe.getItemAtPosition(position);
          		
         		/*Intent eleveIntent = new Intent(ImageListActivity.this, TrombiDetailActivity.class);
         		eleveIntent.putExtra("username", map.get("username"));
         		startActivity(eleveIntent);*/
         		
         	  
          	  /*String[] heavyImages = getResources().getStringArray(R.array.heavy_images);
          	  String[] lightImages = getResources().getStringArray(R.array.light_images);
          	  
          	  imageUrls = new String[heavyImages.length + lightImages.length];
          	  List<String> urls = new ArrayList<String>();
          	  urls.addAll(Arrays.asList(heavyImages));
          	  urls.addAll(Arrays.asList(lightImages));
          	  imageUrls = (String[]) urls.toArray(new String[0]);*/
          	  
          	  Intent intent = new Intent(ImageListActivity.this, ImageGridActivity.class);
          	  intent.putExtra(Extra.IMAGES, imageUrls[position]);
          	  startActivity(intent);
         	}
          });
         
         setContentView(vueListe);
        
    }
    
    private class ChargementAlbumsTask extends AsyncTask {

	     protected void onPostExecute(Object result) {
	    	 String albumsJson = (String)result;
	    	 listItem.clear();
	    	 JSONArray jsonArray = null;
	         try {
	 			jsonArray = new JSONArray(albumsJson);
	 			imageUrls = new String[jsonArray.length()][];
	 			for (int i=0; i<jsonArray.length();i++) {
	 				JSONObject jsonObject = jsonArray.getJSONObject(i);
	 				jsonObject = jsonObject.getJSONObject("fields");
	 				map = new HashMap<String, String>();
	 	            map.put("title", jsonObject.getString("title"));
	 	            map.put("index", ""+i);
	 	            JSONArray arrayPhotos = jsonObject.getJSONArray("photos");
	 	            imageUrls[i] = new String[arrayPhotos.length()];
	 	            for (int j=0; j<arrayPhotos.length(); j++) {
	 	            	imageUrls[i][j]=getString(R.string.domain)+arrayPhotos.getString(j).replace(" ", "%20");
	 	            	Log.d("image", imageUrls[i][j]);
	 	            }	 	           
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
			String albumsJson = chargement.getData(getString(R.string.domain)+"/associations/mediamines/json/");
	    	return albumsJson;
		}
	 }
	
}
