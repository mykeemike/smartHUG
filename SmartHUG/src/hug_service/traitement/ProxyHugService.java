/**
 * GREP - smartHUG
 * 
 * Proxy du pilier "a votre service"
 *
 * @author GREP
 * @version Version 1.0
 * 
 */
package hug_service.traitement;

import hug_sante_geneve.presentation.UrgencesActivity;
import hug_service.presentation.ConsultationActivity;
import hug_service.presentation.DetailsRechercheActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class ProxyHugService {
	private Context context;
	private Activity activity;
	
	public ProxyHugService (Context context, Activity activity){
		this.context = context;
		this.activity = activity;
	}
	
	public void displayConsultation(){
		Intent intent = new Intent(context, ConsultationActivity.class);
		activity.startActivity(intent);
	}
	
	public void displayDetailsRecherche(){
		Intent intent = new Intent(context, DetailsRechercheActivity.class);
		activity.startActivity(intent);
	}
}
