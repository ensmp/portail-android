package com.example.portail;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;

public class PetitsCoursActivity extends MapActivity implements OnGestureListener, OnDoubleTapListener 
{

	private MapView mapView;
	private GestureDetector detector;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.petits_cours);
		
		mapView = (MapView) findViewById(R.id.mapView);
	    mapView.setBuiltInZoomControls(true);
	    
	    MapController mc = mapView.getController();
	    mc.setZoom(13);
	    mc.setCenter(new GeoPoint(48842591, 2341071)); //La meuh
	    
	    detector = new GestureDetector(this,this);
	    
	    
	    ChargementPetitsCoursTask chargementTrombi = new ChargementPetitsCoursTask();
        chargementTrombi.execute();
	    
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
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
    
    
    

	public class ListItemizedOverlay extends ItemizedOverlay<OverlayItem>
	{
	
		private ArrayList<OverlayItem> arrayListOverlayItem = new ArrayList<OverlayItem>();
	
		private Context context;

		public ListItemizedOverlay(Drawable defaultMarker, Context pContext)
		{
			super(boundCenterBottom(defaultMarker));
			this.context = pContext;
		}
	
		@Override
		 protected OverlayItem createItem(int i)
		 {
		  	return arrayListOverlayItem.get(i);
		 }
	
		@Override
		 public int size()
		 {
		  	return arrayListOverlayItem.size();
		 }
	
		 public void addOverlayItem(OverlayItem overlay)
		 {
			  arrayListOverlayItem.add(overlay);
			  populate();
		 }
		 
		 @Override
		 protected boolean onTap(int index)
		 {
			 OverlayItem item = arrayListOverlayItem.get(index);
			 AlertDialog.Builder dialog = new AlertDialog.Builder(context);
			 dialog.setTitle(item.getTitle());
			 dialog.setMessage(item.getSnippet());
			 dialog.show();
			 return true;
		 }
	
	}
	
	private class ChargementPetitsCoursTask extends AsyncTask {

	     protected void onPostExecute(Object result) {
	    	 String petitsCoursJson = (String)result;
	    	 JSONArray jsonArray = null;
	    	 Drawable drawable = PetitsCoursActivity.this.getResources().getDrawable(R.drawable.marker);
	 		 ListItemizedOverlay itemizedoverlay = new ListItemizedOverlay(drawable, PetitsCoursActivity.this);
	         try {
	 			jsonArray = new JSONArray(petitsCoursJson);
	 			for (int i=0; i<jsonArray.length();i++) {
	 				JSONObject jsonObject = jsonArray.getJSONObject(i);
	 				
	 	            GeoPoint geoPoint = new GeoPoint((int)(Double.parseDouble(jsonObject.getString("latitude"))*1e6), (int)(Double.parseDouble(jsonObject.getString("longitude"))*1e6));
		 		    OverlayItem overlayitem = new OverlayItem(geoPoint, jsonObject.getString("titre"), "Matière : " + jsonObject.getString("matiere") + " - Niveau : " + jsonObject.getString("niveau") + "\n" + jsonObject.getString("description"));
		 		    itemizedoverlay.addOverlayItem(overlayitem);
	 			}

	 		    mapView.getOverlays().clear();    
	 		    mapView.getOverlays().add(itemizedoverlay);
	 				 	        

	 			
	 		} catch (JSONException e) {
	 			e.printStackTrace();
	 		}
	     }
	     
		@Override
		protected Object doInBackground(Object... arg0) {
			ChargementDonnees chargement = new ChargementDonnees();
			String petitsCoursJson = chargement.getData("http://10.0.2.2:8000/petitscours/json/");
	    	return petitsCoursJson;
		}
	 }

	public boolean onDoubleTap(MotionEvent e) {
		Log.d("Event", "Double Tap");
		mapView.getController().zoomInFixing((int)e.getX(), (int)e.getY());
		return false;
	}

	public boolean onDoubleTapEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onSingleTapConfirmed(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}