/**
 * GREP - smartHUG
 * 
 * Proxy du noyau de l'application
 *
 * @author GREP
 * @version Version 1.0
 * 
 */
package noyau.traitement;

import java.util.ArrayList;

import hug_communiquent.presentation.NewsActivity;
import hug_service.presentation.ConsultationActivity;
import hug_service.traitement.Consultation;
import hug_service.traitement.ProxyHugService;
import noyau.localisation.DetailsConsultationActivity;
import noyau.localisation.DistanceActivity;
import noyau.localisation.FullMapActivity;
import noyau.presentation.SettingsFacebookActivity;
import noyau.presentation.SettingsTwitterActivity;
import sante_geneve.presentation.UrgencesActivity;
import sante_geneve.traitement.ProxySanteGeneve;
import twitter4j.Twitter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import hug_communiquent.traitement.ProxyHugCommuniquent;

public class ProxyNoyau {
	
	private ProxyHugCommuniquent proxyHugCommuniquent;
	private ProxySanteGeneve proxySanteGeneve; 
	private ProxyHugService proxyHugService;
	private Context context;
	private Activity activity;
	public ProxyNoyau (Context context, Activity activity){
		this.context = context;
		this.activity = activity;
	}
	
	public void goFacebook(){
		proxyHugCommuniquent = new ProxyHugCommuniquent(context, activity);
		proxyHugCommuniquent.displayFacebook();
	}
	
	public void goTwitter(){
		proxyHugCommuniquent = new ProxyHugCommuniquent(context, activity);
		proxyHugCommuniquent.displayTwitter();
	}

	public void goYoutube(){
		proxyHugCommuniquent = new ProxyHugCommuniquent(context, activity);
		proxyHugCommuniquent.displayYoutube();
	}

	public void goRSS(){
		proxyHugCommuniquent = new ProxyHugCommuniquent(context, activity);
		proxyHugCommuniquent.displayRSS();
	}
	
	public void goUrgences(){
		proxySanteGeneve = new ProxySanteGeneve(context, activity);
		proxySanteGeneve.displayUrgences();
	}
	
	public void goConsultation(){
		proxyHugService = new ProxyHugService(context, activity);
		proxyHugService.displayConsultation();
	}

	public void displayOutils(ArrayList<Consultation> alConsultations){
		Intent intent = new Intent(context, FullMapActivity.class);
		intent.putExtra("listCons", alConsultations);
		activity.startActivity(intent);
	}
	
	public void displayGuider(ArrayList<Consultation> alConsultations){
		Intent intent = new Intent(context, DistanceActivity.class);
		intent.putExtra("listCons", alConsultations);
		activity.startActivity(intent);
	}
	
	public void displayDetailsConsultation(Consultation consultation){
		Intent intent = new Intent(context, DetailsConsultationActivity.class);
		intent.putExtra("consul", consultation);
		activity.startActivity(intent);
	}
	
	public void displaySettingsFacebook(){
		Intent intent = new Intent(context, SettingsFacebookActivity.class);
		activity.startActivity(intent);
	}
	
	public void displaySettingsTwitter(){
		Intent intent = new Intent(context, SettingsTwitterActivity.class);
		activity.startActivity(intent);
	}
	
}