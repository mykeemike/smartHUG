package hug_communiquent.traitement;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.json.*;


public class FacebookInfo {
	  
	  private ArrayList<Publication> publications; 
	  
	  /* Constructeur: définit les attributs à partir de json */
	  public FacebookInfo (JSONObject json) throws JSONException {
		publications = new ArrayList<Publication>();	  
		JSONArray wall = json.getJSONArray("data");
		
		for (int i=0; i<7; i++){
			String id = wall.getJSONObject(i).getString("id");
			String message = wall.getJSONObject(i).getString("message");
			int nbLike = wall.getJSONObject(i).getJSONObject("likes").getInt("count");
			int nbComments = wall.getJSONObject(i).getJSONObject("comments").getInt("count");
			 
			try {
				wall.getJSONObject(i).getJSONObject("likes").getString("Nikus");
			} catch (Exception e){
				
			}
			Publication p = new Publication(id, message, 0, nbLike, nbComments);			
			publications.add(p);			
		}	
		
	  } // Constructeur

	  /* Accesseurs */
	  public ArrayList<Publication> getPublications () {return publications;}

} // FacebookInfo

