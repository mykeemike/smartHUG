/**
 * GREP - smartHUG
 * 
 * Activité du détail des consultations
 *
 * @author GREP
 * @version Version 1.0
 * 
 */
package noyau.localisation;

import noyau.presentation.R;
import hug_service.traitement.Consultation;

import java.io.IOException;
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
import android.text.util.Linkify;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

public class DetailsConsultationActivity extends MapActivity implements LocationListener{
	
	private TextView tvNom, tvRue, tvNpaVille, tvTelephone,tvWebsite, tvDpt, tvUnite, tvService, tvRemarque;
	private MapView mapView;
	private ImageButton btnCall;
	private ImageButton btnDirections;
	private LocationManager locManager;
	private Geocoder geoCoder;
	private MapController controller;
	
	GeoTools geoTools;
	
	private Consultation cons;
	private Object obj;
	private Emplacement emp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation_details);
        definirVariables();
        createListeners();
        Intent intent = getIntent();
        //obj= (Object) intent.getSerializableExtra("consul");
        cons= (Consultation) intent.getSerializableExtra("consul");
       
        emp= cons.getEmplacement();
        showDetails(cons);        
    }
    
    private class LoadMap extends AsyncTask <String, Void, JSONObject> {
    	private Exception exception = null; /* Exception qui s'est éventuellement produite dans doInBackground() */ 
    	private Location loc=null;
		@Override
		protected JSONObject doInBackground(String... params) {
			List<Overlay> mapOverlays = mapView.getOverlays();
			Drawable drawableDestination = getResources().getDrawable(R.drawable.mappin);
			ItemizedOverlayList itemizedoverlayDestination = new ItemizedOverlayList(drawableDestination, DetailsConsultationActivity.this);
		
			Drawable drawableUser = getResources().getDrawable(R.drawable.androidmarker);
			ItemizedOverlayList itemizedoverlayUser = new ItemizedOverlayList(drawableUser, DetailsConsultationActivity.this);
		
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
			
			try {	
				// Destination
				List<Address> addresses = geoCoder.getFromLocationName(emp.toString(), 1);
				Address ad = addresses.get(0);
				
				int latEmp=(int) (ad.getLatitude()*1000000);
				int longEmp=(int) (ad.getLongitude()*1000000);
				
				GeoPoint point = new GeoPoint(latEmp,longEmp);
				OverlayItem overlayitem = new OverlayItem(point, cons.getNom(), emp.getAdresse());			
				itemizedoverlayDestination.addOverlay(overlayitem);

				if(loc!=null){
					// Départ
					GeoPoint pointDep = new GeoPoint((int) (latUser*1000000),(int) (longUser*1000000));								
					OverlayItem overlayitemUser = new OverlayItem(pointDep, ad.getAddressLine(0),ad.getLocality());
					itemizedoverlayUser.addOverlay(overlayitemUser);

					latUser *=1000000;	
					longUser *=1000000;	

					double fitFactor = 1.5;
					controller.zoomToSpan((int) (Math.abs(latUser - latEmp) * fitFactor), (int)(Math.abs(longUser - longEmp) * fitFactor));
					controller.animateTo(new GeoPoint((int) ((latUser + latEmp)/2),(int) ((longUser+longEmp)/2) ));
					mapOverlays.add(itemizedoverlayUser);
				}
				mapOverlays.add(itemizedoverlayDestination);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onPostExecute(JSONObject result) {
			if(loc==null){Toast.makeText(getApplicationContext(), "Impossible d'accéder à votre localisation, vérifiez vos paramètres", Toast.LENGTH_LONG).show();}
		}	
    }
    
    private void createListeners(){
    	btnCall.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View arg0) { 	
                callEmplacement(cons.getTel()); 
            }             
        }); 
    	btnDirections.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View arg0) { 
                directionsToEmplacement(emp); 
            }			            
        });
    }
        
    private void definirVariables() {
    	tvNom= (TextView)findViewById(R.id.tvDetailNom);
    	tvRue = (TextView)findViewById(R.id.tvDetailAdr);
    	tvNpaVille = (TextView)findViewById(R.id.tvDetailAdr2);
    	tvTelephone = (TextView)findViewById(R.id.tvDetailTel);
    	tvWebsite = (TextView)findViewById(R.id.tvDetailSite);      
    	tvDpt = (TextView)findViewById(R.id.tvDetailDpt); 
    	tvUnite = (TextView)findViewById(R.id.tvDetailUnite); 
    	tvService = (TextView)findViewById(R.id.tvDetailService); 
    	tvRemarque = (TextView)findViewById(R.id.tvDetailRem); 
    	btnCall = (ImageButton)findViewById(R.id.ibTelephone);    	
    	btnDirections = (ImageButton)findViewById(R.id.ibLocalisation);
    	
    	locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    	locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    	locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        
    	geoCoder = new Geocoder(getBaseContext(),Locale.getDefault());
    	mapView = (MapView)findViewById(R.id.mapView);
    	mapView.setBuiltInZoomControls(true);    	
    	controller = mapView.getController();    
    	
    	geoTools = GeoTools.newInstance(getApplicationContext());
	}
    
    private void directionsToEmplacement(Emplacement emp) {
    	String nav = "google.navigation:q="+emp.getRoutableAdress();
    	Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(nav)); 
    	startActivity(i);		
	} 

	public void showDetails(Consultation cons){
    	tvNom.setText(cons.getNom());
    	tvRue.setText(emp.getAdresse());
    	tvNpaVille.setText(emp.getNpa()+" "+emp.getVille());
    	tvService.setText(cons.getService());
    	tvUnite.setText(cons.getUnite());
    	tvRemarque.setText(cons.getRemarque());
    	tvTelephone.setText(cons.getTel());
    	tvDpt.setText(cons.getDepartement());
    	Linkify.addLinks(tvWebsite, Linkify.ALL);
    	showOnMap(emp);	
    }
	
	private void callEmplacement(String tel){
		Intent callIntent = new Intent(Intent.ACTION_CALL);          
        callIntent.setData(Uri.parse("tel:"+tel));          
        startActivity(callIntent);
	}
	
	private void showOnMap(Emplacement emp){
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
		// TODO Auto-generated method stub		
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