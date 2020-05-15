package com.pafloca.greenguy;

public class ModelEvent {

    private String titre;
    private String soustitre;
    private String description;
    private String publieur;
    private String date;
    private String lieu;
    private String id;

    public ModelEvent(){

    }

    public ModelEvent(String titre, String soustitre, String description, String publieur, String date, String lieu) {
        this.titre = titre;
        this.soustitre = soustitre;
        this.description = description;
        this.publieur = publieur;
        this.date = date;
        this.lieu = lieu;
    }
    public ModelEvent(String titre, String id) {
        this.titre = titre;
        this.id = id;

    }
    public String getId() {
        return id;
    }
    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getSoustitre() {
        return soustitre;
    }

    public void setSoustitre(String soustitre) {
        this.soustitre = soustitre;
    }

    public String getPublieur() {
        return publieur;
    }

    public void setPublieur(String publieur) {
        this.publieur = publieur;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
