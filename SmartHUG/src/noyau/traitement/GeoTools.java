/**
 * GREP - smartHUG
 * 
 * Outils de services pour la géolocalisation
 *
 * @author GREP
 * @version Version 1.0
 * 
 */
package noyau.traitement;


import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import twitter4j.Twitter;


import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GeoTools implements LocationListener {
	private static GeoTools instance = null;
	private static LocationManager locManager;
	private static  Geocoder geoCoder;
	private static Context context;

	public GeoTools(Context context){
		this.context = context;
		//locManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		geoCoder = new Geocoder(context,Locale.getDefault());		
	}

	/* Création de l'unique instance */
	public static GeoTools newInstance (Context context) {
		if (instance == null) instance = new GeoTools(context);
		return instance;
	} // newInstance

	public float getDistanceFromUser(Location loc,Emplacement emp){	
		double latUser=0.0,longUser=0.0;
		if(loc!=null){
			latUser = loc.getLatitude() ;   
			longUser= loc.getLongitude();
		} else return -1;
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

	@Override
	public void onLocationChanged(Location location) {}

	@Override
	public void onProviderDisabled(String provider) {}

	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}

}