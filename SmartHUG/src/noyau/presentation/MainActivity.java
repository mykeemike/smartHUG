package noyau.presentation;


import java.util.ArrayList;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import noyau.traitement.Emplacement;
import noyau.presentation.R;
import hug_service.traitement.Consultation;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import noyau.traitement.ProxyNoyau;
import com.facebook.*;
import com.facebook.model.*;

public class MainActivity extends Activity {
	
	private final int RS_CHOIX = 1;
	
	private final int RS_FACEBOOK = 0;
	private final int RS_TWITTER = 1;
	private final int RS_YOUTUBE = 2;
	private final int RS_RSS = 3;
	ArrayList<Consultation> alConsultations = new ArrayList<Consultation>();
	
	private ProxyNoyau proxyNoyau;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);	 
	    //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_titlebar);	       
		proxyNoyau = new ProxyNoyau(getApplicationContext(), this);
		alConsultations.add(new Consultation(1,"Centre de dermatologie","Dermatologie", "Departement dermatologique", "Unité de soins de la peau","022 666 66 66", "8h-18h","Lorem Ipsum", new Emplacement(1,0, 0, "Rue grand-bureau, 8","1227", "Acacias","Suisse")));
		alConsultations.add(new Consultation(1,"Centre de radiologie","Radiologie", "Departement radiologique", "Unité radiologique", "022 309 21 05","8h-18h","Lorem Ipsum", new Emplacement(1,0, 0, "place d'armes, 12","1227", "Carouge","Suisse")));
		alConsultations.add(new Consultation(1,"Centre d'addictologie","Addictologie", "Departement addictologique","Programme expérimental de prescription de stupéfiants", "022 757 12 30","8h-18h","Lorem Ipsum", new Emplacement(1,0, 0, "Route de Loëx, 173A","1233", "Bernex","Suisse")));
	}

	public void clickFacebook (View v){
		proxyNoyau.goFacebook();
	} 
	
	public void clickTwitter (View v){
		proxyNoyau.goTwitter();
	}
	
	public void clickYoutube (View v){
		proxyNoyau.goYoutube();
	}
	
	public void clickRss (View v){
		proxyNoyau.goRSS();
	}
	
	public void clickUrgences (View v){
		proxyNoyau.goUrgences();
	}
	
	public void clickConsultation (View v){
		proxyNoyau.goConsultation();
	}
		
	public void clickOutils (View v){
		proxyNoyau.displayOutils(alConsultations);
	}
	
	public void clickGuider (View v){
		proxyNoyau.displayGuider(alConsultations);
	}
	
	public void call(View v){
		Intent callIntent = new Intent(Intent.ACTION_CALL);          
        callIntent.setData(Uri.parse("tel:144"));          
        startActivity(callIntent);
	}
	  
	  /* Menu */
	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	      getMenuInflater().inflate(R.menu.activity_main, menu);
	      return true;
	  }	  
	  
	  @Override
	  public boolean onOptionsItemSelected (MenuItem item) {
		  switch (item.getItemId()) {
	      case R.id.itSettings: 		
	    	  proxyNoyau.displaySettingsFacebook();
	    	  return true;
	      case R.id.itSettingsTwitter:
	    	  proxyNoyau.displaySettingsTwitter();
	    	  return true;
	      default:
	        return super.onOptionsItemSelected(item);
	    }    
	  } // onOptionsItemSelected
}

