package hug_communiquent.traitement;

import java.io.Serializable;

import android.graphics.Bitmap;

public class Video implements Serializable {

	private String id;
	private String titre;
	private String description;
	private String nbViews;
	private String temps;
	private String date;
	private String metaPic;
	private Bitmap imageBitmap;

	public Video(){

	} // Constructeur

	public Video (String id, String titre, String description, String nbViews, String temps, String date, String metaPic) {
		this.id = id; 
		this.titre = titre;
		this.description = description;
		this.nbViews = nbViews; 
		this.temps = temps; 
		this.date = date;
		this.metaPic = metaPic;
		new GetBitmap(metaPic);
	} // Constructeur

	public String getId () {return id;}
	public String getTitre () {return titre;}
	public String getDescription () {return description;}
	public String getnbViews () {return nbViews;}
	public String getTemps () {return temps;}
	public String getDate () {return date;}
	public String getMetaPic () {return metaPic;}
	public Bitmap getImageBitmap() {return imageBitmap;}
	
	public void setImageBitmap(Bitmap imageBitmap){
		this.imageBitmap = imageBitmap;
	}

	public class GetBitmap implements GetBitmapFromUrl.Listener {
		private String url;
		public GetBitmap(String url) {
			this.url = url;
			new GetBitmapFromUrl(this).execute(url);
		}
		@Override
		public void onGetFromUrlResult(Bitmap result) {
			// TODO Auto-generated method stub
			setImageBitmap(result);
		}
		@Override
		public void onGetFromUrlError(Exception e) {
			// TODO Auto-generated method stub

		}

	} //GetBitmap
} // Video
