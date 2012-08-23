package com.ensmp.portail.widget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.ensmp.portail.ChargementDonnees;
import com.ensmp.portail.ImageListActivity;
import com.ensmp.portail.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/**
 * Example widget provider
 * 
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class UILWidgetProvider extends AppWidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		// Initialize ImageLoader with configuration.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCacheSize(1500000) // 1.5 Mb
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.enableLogging() // Not necessary in common
				.build();
		ImageLoader.getInstance().init(config);

		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
			updateAppWidget(context, appWidgetManager, appWidgetId);
		}
		Log.d("widget", "Updating Example Widgets.");
		//context.startService(new Intent(context, MediaMinesFetcherService.class));
	}
	

	static void updateAppWidget(Context context, final AppWidgetManager appWidgetManager, final int appWidgetId) {

		
		final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
		
		Intent intent = new Intent(context, ImageListActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,	intent, 0);
        views.setOnClickPendingIntent(R.id.image_left, pendingIntent);
        views.setOnClickPendingIntent(R.id.image_right, pendingIntent);
        

		//String[] imageUrls = context.getResources().getStringArray(R.array.heavy_images);		
		ChargementDonnees chargement = new ChargementDonnees();
		String albumsJson = chargement.getData(context.getString(R.string.domain)+"/associations/mediamines/json/");
				
		JSONArray jsonArray = null;
		String url1 = "";
		String url2 = "";
         try {
 			jsonArray = new JSONArray(albumsJson);
 				JSONObject jsonObject = jsonArray.getJSONObject(0); //premier album
 				jsonObject = jsonObject.getJSONObject("fields");
 	            JSONArray arrayPhotos = jsonObject.getJSONArray("photos");
 	            url1 = context.getString(R.string.domain)+arrayPhotos.getString(0).replace(" ", "%20"); //premiere photo
 	            url2 = context.getString(R.string.domain)+arrayPhotos.getString(1).replace(" ", "%20");	//deuxieme photo       	           
 	            
 		} catch (JSONException e) {
 			e.printStackTrace();
 		}
				
		
		ImageView tempImageView1 = new ImageView(context);
		ImageView tempImageView2 = new ImageView(context);
		tempImageView1.setLayoutParams(new LayoutParams(70, 70)); // 70 - approximate size of ImageView in widget
		tempImageView2.setLayoutParams(new LayoutParams(70, 70)); // 70 - approximate size of ImageView in widget				        

		ImageLoader.getInstance().displayImage(url1, tempImageView1, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete(Bitmap loadedImage) {
				views.setImageViewBitmap(R.id.image_left, loadedImage);
				appWidgetManager.updateAppWidget(appWidgetId, views);
			}
		});
		ImageLoader.getInstance().displayImage(url2, tempImageView2, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete(Bitmap loadedImage) {
				views.setImageViewBitmap(R.id.image_right, loadedImage);
				appWidgetManager.updateAppWidget(appWidgetId, views);
			}
		});
		
		
		
		

		
	}
}
