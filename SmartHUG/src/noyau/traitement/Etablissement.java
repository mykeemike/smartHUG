package noyau.traitement;

import java.io.*;

/**
 * GREP - smartHUG
 * 
 * Modélisation d'ue Etablissement
 *
 * @author GREP
 * @version Version 1.0
*/
public class Etablissement implements Comparable<Etablissement>, Serializable {

  private int id;
  private String nom;
  private String siteInternet;
  private String telephone;
  private String horaire;
  private String remarque;
  private Emplacement emplacement;
  
  /* Constructeur */
  public Etablissement (int id, String nom, String siteInternet, String tel, String horaire, String remarque, Emplacement empl){
	this.id = id; this.nom = nom; this.siteInternet = siteInternet; this.telephone = tel; this.horaire = horaire; this.remarque = remarque; this.emplacement = empl;}
  
  //public int getId () {return id;}
  public String getNom () {return nom;}
  public String getTypeCons () {return siteInternet;}
  public String getTel () {return telephone;}
  public String getHoraire () {return horaire;}
  public String getRemarque () {return remarque;}
  public Emplacement getEmplacement () {return emplacement;}
  
  public void setNom (String nom) {this.nom = nom;}
  public void setTypeCons (String siteInternet) {this.siteInternet =  siteInternet;}
  public void setTel (String tel) {this.telephone = tel;}
  public void setHoraire (String horaire) {this.horaire = horaire;}
  public void setRemarque (String remarque) {this.remarque = remarque;}
  public void setEmplacement (Emplacement empl) {this.emplacement = empl;}
  
  public boolean equals (Object obj) {return ((Etablissement)obj).id == id;}
  public int compareTo (Etablissement e) {return this.nom.compareTo(e.nom);}
  
  
} // Etablissement