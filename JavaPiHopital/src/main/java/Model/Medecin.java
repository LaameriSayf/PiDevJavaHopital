package Model;

import java.sql.Timestamp;

public class Medecin extends Global_user{
    private String specialite;
    private int etat;

    public Medecin(int cin, String nom, String prenom, int genre, String email, String password, String image, String role, Timestamp dateNaissance, boolean interlock, int numtel, String specialite, int etat) {
        super(cin, nom, prenom, genre, email, password, image, role, dateNaissance, interlock, numtel);
        this.specialite = specialite;
        this.etat = etat;
    }

    public Medecin(int id, int cin, int numtel, int genre, String nom, String prenom, String email, String password, String image, String role, Timestamp dateNaissance, boolean interlock) {
        super(id, cin, numtel, genre, nom, prenom, email, password, image, role, dateNaissance, interlock);
    }

    public Medecin(int id, int cin, int numtel, int genre, String nom, String prenom, String email, String password, String image, String role, Timestamp dateNaissance, boolean interlock, String specialite, int etat) {
        super(id, cin, numtel, genre, nom, prenom, email, password, image, role, dateNaissance, interlock);
        this.specialite = specialite;
        this.etat = etat;
    }

    public Medecin() {
    }

    public Medecin(int id, int cin, int numtel, int etat, int genre, String nom, String prenom, String email, String password, String specialite, String image, String role, Timestamp dateNaissance, boolean interlock) {
        super(id, cin, numtel, genre, nom, prenom, email, password, image, role, dateNaissance, interlock);
        this.specialite = specialite;
        this.etat = etat;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    @Override
    public String toString() {
        return super.toString() + ", specialite='" + specialite + '\'' + ", etat=" + etat +
                '}';
    }
}
