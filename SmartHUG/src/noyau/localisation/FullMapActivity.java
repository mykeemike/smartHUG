/**
 * GREP - smartHUG
 * 
 * Activité de la carte entière 
 *
 * @author GREP
 * @version Version 1.0
 * 
 */
package noyau.localisation;

import noyau.presentation.R;
import hug_service.traitement.Consultation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import noyau.traitement.Emplacement;
import noyau.traitement.GeoTools;
import noyau.traitement.ItemizedOverlayList;

import org.json.JSONObject;


import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;


import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

public class FullMapActivity extends MapActivity implements LocationListener{
	
	private MapView mapView;
	private LocationManager locManager;
	private Geocoder geoCoder;
	private MapController controller;
	private ArrayList<Emplacement> alEmp;
	
	GeoTools geoTools;	
	private ArrayList<Consultation> alCons;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_full_map);
    	definirVariables();
    	createListeners();
    	Intent intent = getIntent();
    	alCons= (ArrayList<Consultation>) intent.getSerializableExtra("listCons");
    	showDetails(alCons);        
    }
   
    private class LoadMap extends AsyncTask <String, Void, JSONObject> {
    	private Exception exception = null; /* Exception qui s'est éventuellement produite dans doInBackground() */ 
    	Location loc=null;
		@Override
		protected JSONObject doInBackground(String... params) {
			List<Overlay> mapOverlays = mapView.getOverlays();
			Drawable drawableDestination = getResources().getDrawable(R.drawable.mappin);
			ItemizedOverlayList itemizedoverlayDestination = new ItemizedOverlayList(drawableDestination,mapView.getContext());
		
			Drawable drawableUser = getResources().getDrawable(R.drawable.androidmarker);
			ItemizedOverlayList itemizedoverlayUser = new ItemizedOverlayList(drawableUser, mapView.getContext());
		
			// Get the location manager
			locManager = (LocationManager) getSystemService(LOCATION_SERVICE);

			// List all providers:
			List<String> providers = locManager.getAllProviders();
			Criteria criteria = new Criteria();
			String bestProvider = locManager.getBestProvider(criteria, false);
			loc = locManager.getLastKnownLocation(bestProvider);
					
			double latUser=0,longUser=0;			
			if(loc!=null){
				latUser = loc.getLatitude();  
				longUser=  loc.getLongitude();
			}else{
				//latUser = 46.17659880; 
				//longUser=  6.14004850;			
			}		
			
			ArrayList<GeoPoint> items = new ArrayList<GeoPoint>();
			
			int i=0;
			for (Emplacement e: alEmp) {  
				
				try {	
					// Destination
					List<Address> addresses = geoCoder.getFromLocationName(e.toString(), 1);
					Address ad = addresses.get(0);
					int latEmp=(int) (ad.getLatitude()*1000000);
					int longEmp=(int) (ad.getLongitude()*1000000);
					
					GeoPoint point = new GeoPoint(latEmp,longEmp);
					items.add(point);
					OverlayItem overlayitem = new OverlayItem(point, alCons.get(i).getNom(), e.getAdresse()+"\n"+e.getNpa()+" "+e.getVille());			
					itemizedoverlayDestination.addOverlay(overlayitem);					
				} catch (IOException exc) {
					exc.printStackTrace();
				}
				i++;
			}
			if(loc!=null){
				// Départ
				GeoPoint pointDep = new GeoPoint((int) (latUser*1000000),(int) (longUser*1000000));								
				OverlayItem overlayitemUser = new OverlayItem(pointDep, null,null);
				itemizedoverlayUser.addOverlay(overlayitemUser);
				items.add(pointDep);				
				latUser  *=1000000;	
				longUser *=1000000;
			}
			
			int minLat = Integer.MAX_VALUE;
			int maxLat = Integer.MIN_VALUE;
			int minLon = Integer.MAX_VALUE;
			int maxLon = Integer.MIN_VALUE;


			for (GeoPoint item : items) { 
				int lat = item.getLatitudeE6();
				int lon = item.getLongitudeE6();

				maxLat = Math.max(lat, maxLat);
				minLat = Math.min(lat, minLat);
				maxLon = Math.max(lon, maxLon);
				minLon = Math.min(lon, minLon);
			}

			double fitFactor = 1.2;
			controller.zoomToSpan((int)(Math.abs(maxLat - minLat)*fitFactor), (int)(Math.abs(maxLon - minLon)*fitFactor));
			controller.animateTo(new GeoPoint( (maxLat + minLat)/2,(maxLon + minLon)/2)); 
		
			if(loc!=null){mapOverlays.add(itemizedoverlayUser);}
			mapOverlays.add(itemizedoverlayDestination);
			
			return null;
		}
		
		protected void onPostExecute(JSONObject result) {
			if(loc==null){Toast.makeText(getApplicationContext(), "Impossible d'accéder à votre localisation, vérifiez vos paramètres", Toast.LENGTH_LONG).show();
			}
			
		}		
    }
    
    private void createListeners(){}
           
    private void definirVariables() {    	
    	locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    	locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    	locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        
    	geoCoder = new Geocoder(getBaseContext(),Locale.getDefault());
    	mapView = (MapView)findViewById(R.id.mapView);
    	mapView.setBuiltInZoomControls(true);    	
    	controller = mapView.getController();    
    	
    	geoTools = GeoTools.newInstance(getApplicationContext());
	}    

    public void showDetails(ArrayList<Consultation> alCons){
		alEmp = new ArrayList<Emplacement>();
		for (Consultation c: alCons) {          
			alEmp.add(c.getEmplacement());
		}		
    	showOnMap(alEmp);	
    }
	
	private void showOnMap(ArrayList<Emplacement> alEmp){
		
		new LoadMap().execute("");
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public void onLocationChanged(Location location) {
		mapView.getOverlays().clear();
		
		new LoadMap().execute("");
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub		
	}	
}