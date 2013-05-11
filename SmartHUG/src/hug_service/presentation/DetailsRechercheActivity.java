package hug_service.presentation;
 
import noyau.presentation.R;

import hug_service.traitement.Consultation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;




import noyau.localisation.DetailsConsultationActivity;
import noyau.traitement.Emplacement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class DetailsRechercheActivity extends Activity {
	
	private ListView lvConsultationDetails;
	private ArrayList<Consultation> alConsultations;
	private void definirVariables () {
		lvConsultationDetails = (ListView)findViewById(R.id.lvConsultationDetails);
		alConsultations = new ArrayList<Consultation>();
		alConsultations.add(new Consultation(1,"Centre de dermatologie","Dermatologie", "Departement dermatologique", "Unité de soins de la peau","022 666 66 66", "8h-18h","Lorem Ipsum", new Emplacement(1,0, 0, "Rue grand-bureau, 8","1227", "Acacias","Suisse")));
		alConsultations.add(new Consultation(1,"Centre de radiologie","Radiologie", "Departement radiologique", "Unité radiologique", "022 309 21 05","8h-18h","Lorem Ipsum", new Emplacement(1,0, 0, "place d'armes, 12","1227", "Carouge","Suisse")));
		alConsultations.add(new Consultation(1,"Centre d'addictologie","Addictologie", "Departement addictologique","Programme expérimental de prescription de stupéfiants", "022 757 12 30","8h-18h","Lorem Ipsum", new Emplacement(1,0, 0, "Route de Loëx, 173A","1233", "Bernex","Suisse")));
		//alConsultations.add(new Consultation(1,"Centre de dermatologie","Dermatologie","022 757 12 30","8h-18h","Bla bla", new Emplacement(1,0, 0, "Route de Chancy 98","1213", "Onex","Suisse")));
	}
	
	private void afficherListe(){    	  
		/* Création de la liste des noms de colonnes et des identifiants des vues destination */
        String[] from = new String[] {"Nomconsult", "typeConsult", "addrConsult", "telConsult"};
        int[] to = new int[] {R.id.tvNomConsultation, R.id.tvTypeConsult, R.id.tvAdrrConsult, R.id.tvTelConsult};
                
        /* Création de la liste des données */
        List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        for (Consultation c: alConsultations) {          	
          HashMap<String, Object> map = new HashMap<String, Object>();
          Emplacement e = c.getEmplacement();
          map.put(from[0], c.getNom());
          map.put(from[1],c.getService()); 
          map.put(from[2],e.toString()); 
          map.put(from[3],c.getTel()); 
          map.put("consultation",c);           
          data.add(map);          
        }
        
        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), data, R.layout.une_consultation, from, to);
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

			
				// TODO Auto-generated method stub
				
			   	
		}); 
    }
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation_recherche);
        definirVariables ();
        createListeners();
        afficherListe();
	}
	
}