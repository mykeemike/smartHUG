/**
 * GREP - smartHUG
 * 
 * Activité qui gère les distances 
 *
 * @author GREP
 * @version Version 1.0
 * 
 */
package noyau.localisation;

import noyau.presentation.R;
import hug_service.traitement.Consultation;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import noyau.traitement.Emplacement;
import noyau.traitement.GeoTools;

import org.json.JSONObject;



import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import noyau.presentation.R;
public class DistanceActivity extends Activity implements LocationListener {

	private GeoTools geoTools;

	private ArrayList<Consultation> alConsultations;
	private ListView lvConsultationDetails;
	
	private LocationManager locManager;
	private Geocoder geoCoder;
	private Location loc;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consultation_recherche);
		definirVariables();
		createListeners();
		Intent intent = getIntent();
		alConsultations= (ArrayList<Consultation>) intent.getSerializableExtra("listCons");
		locManager = (LocationManager)DistanceActivity.this.getSystemService(Context.LOCATION_SERVICE);
		geoCoder = new Geocoder(DistanceActivity.this,Locale.getDefault());		

		List<String> providers = locManager.getAllProviders();
		Criteria criteria = new Criteria();
		String bestProvider = locManager.getBestProvider(criteria, false);
		loc = locManager.getLastKnownLocation(bestProvider);			
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,this);
		methodeAffichage();
	}

	public float getDistanceFromUser(Emplacement emp) {
		double latUser=0,longUser=0;			
		if(loc!=null){
			latUser = loc.getLatitude();  
			longUser=  loc.getLongitude();
		}else{
			//latUser = 46.17659880; 
			//longUser=  6.14004850;			
		}
		List<Address> addresses;
		try {
			addresses = geoCoder.getFromLocationName(emp.toString(), 1);
			Address ad = addresses.get(0);
			float[] result=new float[5];			

			Location.distanceBetween(latUser,longUser, ad.getLatitude(), ad.getLongitude(),result);
			float distance = result[0];
			return distance;			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	private class AfficherListe extends AsyncTask <String, Void, JSONObject> {
		private Exception exception = null; /* Exception qui s'est éventuellement produite dans doInBackground() */

		@Override
		protected JSONObject doInBackground(String... params) {
			/* Création de la liste des données*/
			for (Consultation c: alConsultations) { 
				c.setDistance(geoTools.getDistanceFromUser(loc,c.getEmplacement()));
			}
			Collections.sort(alConsultations);	
			return null;

		}	
		protected void onPostExecute(JSONObject result) {
			String[] from = new String[] {"Nomconsult", "typeConsult", "addrConsult", "telConsult", "distanceConsult"};
			int[] to = new int[] {R.id.tvNomConsultation, R.id.tvTypeConsult, R.id.tvAdrrConsult, R.id.tvTelConsult, R.id.tvDistance};

			
			DecimalFormat df = new DecimalFormat("###.##");
			String strDistance;

			List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
			for (Consultation c: alConsultations) {          	
				HashMap<String, Object> map = new HashMap<String, Object>();
				Emplacement e = c.getEmplacement();
				map.put(from[0], c.getNom());
				map.put(from[1],c.getService()); 
				map.put(from[2],e.toString()); 
				map.put(from[3],c.getTel()); 
				float distance = c.getDistance(); 
				if(distance>1000) {distance /= 1000; strDistance= df.format(distance) + " km";}else{distance = Math.round(distance); strDistance= df.format(distance) + " m"; }
				if(distance==-1){strDistance="Localisation indisponible";}
				
				map.put(from[4], strDistance);
				map.put("consultation",c);           
				data.add(map);          
			}

			SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), data, R.layout.une_consultation_distance, from, to);			
			adapter.notifyDataSetChanged();
			lvConsultationDetails.setAdapter(adapter);			
	     }
	}

	private void definirVariables () {
		geoTools = GeoTools.newInstance(DistanceActivity.this);		
		lvConsultationDetails = (ListView)findViewById(R.id.lvConsultationDetails);
	}

	private void afficherListe(){ 	
		/* Création de la liste des noms de colonnes et des identifiants des vues destination */
		String[] from = new String[] {"Nomconsult", "typeConsult", "addrConsult", "telConsult", "distanceConsult"};
		int[] to = new int[] {R.id.tvNomConsultation, R.id.tvTypeConsult, R.id.tvAdrrConsult, R.id.tvTelConsult, R.id.tvDistance};

		/* Création de la liste des données */ 
		for (Consultation c: alConsultations) { 
			c.setDistance(getDistanceFromUser(c.getEmplacement()));
		}
		Collections.sort(alConsultations);
		DecimalFormat df = new DecimalFormat("###.##");
		String strDistance;

		List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (Consultation c: alConsultations) {          	
			HashMap<String, Object> map = new HashMap<String, Object>();
			Emplacement e = c.getEmplacement();
			map.put(from[0], c.getNom());
			map.put(from[1],c.getService()); 
			map.put(from[2],e.toString()); 
			map.put(from[3],c.getTel()); 
			float distance = c.getDistance(); 
			if(distance>1000) {distance /= 1000;strDistance= df.format(distance) + " km";}else{distance = Math.round(distance);strDistance= df.format(distance) + " m"; }
			map.put(from[4], strDistance);
			map.put("consultation",c);           
			data.add(map);          
		}		
		SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), data, R.layout.une_consultation_distance, from, to);

		adapter.notifyDataSetChanged();
		lvConsultationDetails.setAdapter(adapter);
	}

	private void createListeners(){
		lvConsultationDetails.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				HashMap item = (HashMap) parent.getItemAtPosition(position);
				Consultation cons = (Consultation)item.get("consultation");
				Intent intent = new Intent(getApplicationContext(), DetailsConsultationActivity.class);
				intent.putExtra("consul", cons);
				startActivityForResult(intent, 0);        			
			}
		}); 
	}

	private void methodeAffichage(){
		//afficherListe();
		new AfficherListe().execute("");
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
		new AfficherListe().execute("");
	}

	@Override
	public void onProviderDisabled(String provider) {}

	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}
}