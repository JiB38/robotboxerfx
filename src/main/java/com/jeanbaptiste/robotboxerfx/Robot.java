package com.jeanbaptiste.robotboxerfx;

public class Robot {

    private String nom;
    private int pointsDeVie;
    private int energie;
    private int attaque;
    private int defense;

    private boolean modeGarde;
    private boolean modeEsquive;

    public Robot(String nom, int pointsDeVie, int energie, int attaque, int defense) {
        this.nom = nom;
        this.pointsDeVie = pointsDeVie;
        this.energie = energie;
        this.attaque = attaque;
        this.defense = defense;
        this.modeGarde = false;
        this.modeEsquive = false;
    }

    public String getNom() {
        return nom;
    }

    public int getPointsDeVie() {
        return pointsDeVie;
    }

    public int getEnergie() {
        return energie;
    }

    public int getAttaque() {
        return attaque;
    }

    public int getDefense() {
        return defense;
    }

    public boolean isModeGarde() {
        return modeGarde;
    }

    public boolean isModeEsquive() {
        return modeEsquive;
    }

    public boolean estKO() {
        return pointsDeVie <= 0;
    }

    public boolean aAssezEnergie(int cout) {
        return energie >= cout;
    }

    public void consommerEnergie(int valeur) {
        energie -= valeur;
        if (energie < 0) {
            energie = 0;
        }
    }

    public void recupererEnergie(int valeur) {
        energie += valeur;
        if (energie > 100) {
            energie = 100;
        }
    }

    public void activerGarde() {
        modeGarde = true;
    }

    public void activerEsquive() {
        modeEsquive = true;
    }

    public void reinitialiserEffetsTemporaires() {
        modeGarde = false;
        modeEsquive = false;
    }

    public void subirDegats(int degats) {
        pointsDeVie -= degats;
        if (pointsDeVie < 0) {
            pointsDeVie = 0;
        }
    }

    @Override
    public String toString() {
        return nom + " [PV=" + pointsDeVie + ", Energie=" + energie + "]";
    }
}
