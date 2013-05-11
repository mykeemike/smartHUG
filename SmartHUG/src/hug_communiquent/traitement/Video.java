package hug_communiquent.traitement;

import java.io.Serializable;

public class Video implements Serializable {

	private String id;
	private String titre;
	private String description;
	private String nbViews;
	private String temps;
	private String date;

	public Video(){

	} // Constructeur

	public Video (String id, String titre, String description, String nbViews, String temps, String date) {
		this.id = id; 
		this.titre = titre;
		this.description = description;
		this.nbViews = nbViews; 
		this.temps = temps; 
		this.date = date;
	} // Constructeur

	public String getId () {return id;}
	public String getTitre () {return titre;}
	public String getDescription () {return description;}
	public String getnbViews () {return nbViews;}
	public String getTemps () {return temps;}
	public String getDate () {return date;}

} // Video
