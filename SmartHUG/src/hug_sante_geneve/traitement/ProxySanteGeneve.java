/**
 * GREP - smartHUG
 * 
 * Proxy du pilier santée l'application
 *
 * @author GREP
 * @version Version 1.0
 * 
 */
package hug_sante_geneve.traitement;

import hug_sante_geneve.presentation.UrgencesActivity;
import hug_service.traitement.Consultation;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import noyau.traitement.ProxyNoyau;

public class ProxySanteGeneve {
	private Context context;
	private Activity activity;
	
	public ProxySanteGeneve (Context context, Activity activity){
		this.context = context;
		this.activity = activity;
	}
	
	public void displayUrgences(){
		Intent intent = new Intent(context, UrgencesActivity.class);
		activity.startActivity(intent);
	}
	
	public void goDetailsConsultation(Consultation consultation){
		ProxyNoyau proxyNoyau = new ProxyNoyau(context, activity);
		proxyNoyau.displayDetailsConsultation(consultation);
	}
}
