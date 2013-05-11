package hug_service.presentation;
 
import hug_service.traitement.ProxyHugService;
import noyau.presentation.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ConsultationActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation);
	}
	
	public void clickRecherche (View v) {
		ProxyHugService proxyHugService  = new ProxyHugService(getApplicationContext(), this);
		proxyHugService.displayDetailsRecherche();
	}
}