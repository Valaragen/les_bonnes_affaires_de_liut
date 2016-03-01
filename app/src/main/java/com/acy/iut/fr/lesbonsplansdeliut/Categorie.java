package com.acy.iut.fr.lesbonsplansdeliut;

/**
 * Created by boinnarr on 01/03/2016.
 */
public class Categorie {
    private int id;
    private String nom;

    public Categorie(int id_categorie, String nom) {
        this.id = id_categorie;
        this.nom = nom;
    }

    public int getId_categorie() {
        return id;
    }

    public void setId_categorie(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
