/**
 * GREP - smartHUG
 * 
 * Activité du pilier Urgences
 *
 * @author GREP
 * @version Version 1.0
 * 
 */
package hug_sante_geneve.presentation;
 
import noyau.localisation.DetailsConsultationActivity;
import noyau.traitement.Emplacement;
import noyau.presentation.R;
import noyau.presentation.R.layout;
import hug_sante_geneve.traitement.ProxySanteGeneve;
import hug_service.traitement.Consultation;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class UrgencesActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urgences);
	}
	
	public void clickHopitalDetail (View v){
		Consultation consultation = new Consultation(1,"Hôpital de Carouge","","","","022 309 45 45","24h sur 24h - 7j/7j","", new Emplacement(1,0, 0, "Avenue du Cardinal-Mermillod 1","1227", "Carouge","Suisse"));
		ProxySanteGeneve proxySanteGeneve = new ProxySanteGeneve(getApplicationContext(), this);
		proxySanteGeneve.goDetailsConsultation(consultation);
	}
	
	public void call(View v){
		Intent callIntent = new Intent(Intent.ACTION_CALL);          
        callIntent.setData(Uri.parse("tel:144"));          
        startActivity(callIntent);
	}
}