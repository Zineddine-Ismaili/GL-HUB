package com.example.tp_android.model;

public class Etudiant {

    private String nom;
    private String prenom;
    private String tel;
    private String idE;


    public Etudiant(String nom, String prenom, String tel, String idE){
        this.nom= new String(nom);
        this.prenom= new String(prenom);
        this.tel= new String(tel);
        this.idE = new String(idE);
    }

    public String getIdE() {
        return idE;
    }

    public void setIdE(String idE) {
        this.idE = idE;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }


    @Override
    public String toString() {
        return "Etudiant{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", tel='" + tel + '\'' +
                ", idE='" + idE + '\'' +
                '}';
    }
}


