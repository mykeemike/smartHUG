package noyau.traitement;

import java.io.*;
import java.util.*;

/**
 * GREP 
 * 
 * Modélisation d'un Emplacement
 *
 * @author GREP
 * @version Version 1.0
*/
public class Emplacement implements Comparable<Emplacement>, Serializable {
  
  private int id;
  private float latitude;
  private float longitude;
  private String adresse;
  private String npa;
  private String ville;
  private String pays;
  
  /* Constructeur */
  public Emplacement (int id, float latitude, float longitude, String adresse, String npa, String ville, String pays) {
    this.id = id; this.latitude = latitude; this.longitude = longitude; this.adresse = adresse; this.npa = npa; this.ville = ville; this.pays = pays;
  } // Constructeur

  public int getId () {return id;}
  public float getLatitude () {return latitude;}
  public float getLongitude () {return longitude;}
  public String getNpa () {return npa;}
  public String getAdresse () {return adresse;}
  public String getVille () {return ville;}
  public String getPays () {return pays;}
  
  public void setLatitude(float lat) {this.latitude = lat;}
  public void setLongitude(float longi) {this.longitude = longi;}
  public void setNpa(String npa) {this.npa = npa;}
  public void setAdresse(String adr){this.adresse = adr;}
  public void setVille(String ville) {this.ville = ville;}
  public void setPays(String pays) {this.pays = pays;}
  
  public String toString(){return adresse+" ,"+npa+" "+ville;}
  public String getRoutableAdress(){return adresse+"+"+npa+"+"+ville;}
  
  public boolean equals (Object obj) {return ((Emplacement)obj).id == id;}
  public int compareTo (Emplacement e) {return adresse.compareTo(e.adresse);}

} // Emplacement
