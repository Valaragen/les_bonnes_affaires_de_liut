package com.acy.iut.fr.lesbonsplansdeliut.Objets;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boinnarr on 01/03/2016.
 */
public class Objet {
    private int id;
    private String nom;
    private String description;
    private List<String> url_photo1 = new ArrayList<>();
    private double prix;

    public Objet( String nom, String description, List<String> url_photo1, double prix) {
        this.nom = nom;
        this.description = description;
        this.url_photo1 = url_photo1;
        this.prix = prix;
    }

    public Objet( String nom, String description, double prix) {
        this.nom = nom;
        this.description = description;
        this.prix = prix;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getUrl_photo1() {
        return url_photo1;
    }

    public void setUrl_photo1(List<String> url_photo1) {
        this.url_photo1 = url_photo1;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }
}
