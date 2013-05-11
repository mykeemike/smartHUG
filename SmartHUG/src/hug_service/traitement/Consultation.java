/**
 * GREP - smartHUG
 * 
 * Modélisation d'une Consultation
 *
 * @author GREP
 * @version Version 1.0
 * 
 */
package hug_service.traitement;


import java.io.*;
import java.util.Comparator;

import noyau.traitement.Emplacement;

public class Consultation implements Comparable<Consultation>, Serializable {

	private int id;
	private String nom;
	private String service;
	private String departement;
	private String unite;
	private String telephone;
	private String horaire;
	private String remarque;
	private Emplacement emplacement;
	private float distance;

	/* Constructeur */
	public Consultation (int id, String nom, String service, String departement, String unite, String tel, String horaire, String remarque, Emplacement empl){
		this.id = id; this.nom = nom; this.service = service; this.departement = departement; this.unite = unite; this.telephone = tel; this.horaire = horaire; this.remarque = remarque; this.emplacement = empl;}

	public int getId () {return id;}
	public String getNom () {return nom;}
	public String getService () {return service;}
	public String getDepartement () {return departement;}
	public String getUnite () {return unite;}
	public String getTel () {return telephone;}
	public String getHoraire () {return horaire;}
	public String getRemarque () {return remarque;}
	public Emplacement getEmplacement () {return emplacement;}
	public float getDistance(){return distance;}

	public void setNom (String nom) {this.nom = nom;}
	public void setService (String service) {this.service =  service;}
	public void setDepartement (String dpt) {this.departement = dpt;}
	public void setUnite (String unite) {this.unite = unite;}
	public void setTel (String tel) {this.telephone = tel;}
	public void setHoraire (String horaire) {this.horaire = horaire;}
	public void setRemarque (String remarque) {this.remarque = remarque;}
	public void setEmplacement (Emplacement empl) {this.emplacement = empl;}
	public void setDistance(float distance){this.distance = distance;}

	public boolean equals (Object obj) {return ((Consultation)obj).id == id;}

	@Override
	public int compareTo(Consultation another) {
		return (this.distance>another.distance?1:-1);
	}  
} // Consultation
