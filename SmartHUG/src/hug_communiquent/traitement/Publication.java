package hug_communiquent.traitement;

import java.util.StringTokenizer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;
import android.widget.VideoView;

public class Publication {
	private String id;
	private String statut;
	private String shortStatut;
	/* 0 = USUAL, 1 = PICTURE, 2 = VIDEO */
	private int type;
	private int nbLikes;
	private int nbComments;	
	private Bitmap imageBitmap;
	private VideoView video;
	
	public Publication(String id, String statut, int type, int nbLikes, int nbComments){
		this.id = id;
		
		this.statut = statut;
		StringTokenizer st = new StringTokenizer(statut, " ");
		shortStatut = "";
		int cpt = 0;
		while (st.hasMoreTokens()&&cpt<15){
			shortStatut += st.nextToken()+" ";
			cpt++;
		}
		if (shortStatut.trim().length() < this.statut.trim().length()){
			shortStatut += "... Afficher la suite";
		}		
		
		this.type = type;
		this.nbLikes = nbLikes;
		this.nbComments = nbComments;
	}//Constructeur
	
	public String getId(){return id;}
	public String getStatut(){return statut;}
	public String getShortStatut(){return shortStatut;}
	public int getType() {return type;}
	public int getLikes() {return nbLikes;}
	public int getComments() {return nbComments;}
	public VideoView getVideo(){return video;}
	public Bitmap getImageBitmap(){return imageBitmap;}
	
	public void setLikes(int newLikes){this.nbLikes = newLikes;}
	public void setComments(int newComments){this.nbComments = newComments;}
	public void setImageBitmap(Bitmap i){imageBitmap = i;}
	public void setVideo(VideoView v){video = v;}
		
	public boolean equals (Object obj){return ((Publication)obj).id == id;}
	public String toString() {return id + " " + statut + " type =" + type;}	
}