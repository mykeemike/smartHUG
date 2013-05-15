package hug_sante_geneve.presentation;

import hug_service.traitement.Consultation;

import java.util.ArrayList;

import noyau.localisation.DistanceActivity;
import noyau.presentation.R;
import noyau.traitement.Emplacement;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class GuiderActivity extends Activity {

	private Context context;
	private Activity activity;
	ArrayList<Consultation> alConsultations = new ArrayList<Consultation>();
	
	
	
	/* Contrôles */
	private SearchView svRechercheLibre;

	private void definirVariables () {
		svRechercheLibre = (SearchView)findViewById(R.id.svRechercheLibre);
		alConsultations.add(new Consultation(1,"Centre de dermatologie","Dermatologie", "Departement dermatologique", "Unité de soins de la peau","022 666 66 66", "8h-18h","Lorem Ipsum", new Emplacement(1,0, 0, "Rue grand-bureau, 8","1227", "Acacias","Suisse")));
		alConsultations.add(new Consultation(1,"Centre de radiologie","Radiologie", "Departement radiologique", "Unité radiologique", "022 309 21 05","8h-18h","Lorem Ipsum", new Emplacement(1,0, 0, "place d'armes, 12","1227", "Carouge","Suisse")));
		alConsultations.add(new Consultation(1,"Centre d'addictologie","Addictologie", "Departement addictologique","Programme expérimental de prescription de stupéfiants", "022 757 12 30","8h-18h","Lorem Ipsum", new Emplacement(1,0, 0, "Route de Loëx, 173A","1233", "Bernex","Suisse")));
	
	}

	private void definirListener () {
		OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
			public boolean onQueryTextChange(String newText) {
				// this is your adapter that will be filtered
				//listAdapter.getFilter().filter(newText);
				return true;
			}

			public boolean onQueryTextSubmit(String query) {
				// this is your adapter that will be filtered
				//listAdapter.getFilter().filter(query); 
				Intent intent = new Intent(getApplicationContext(), DistanceActivity.class);
				intent.putExtra("listCons", alConsultations);
				startActivity(intent);
				return true;
			}
		};
		svRechercheLibre.setOnQueryTextListener(queryTextListener);
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guider_recherche);
		definirVariables();
		definirListener ();
	}
}
