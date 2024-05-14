package Model;

import java.sql.Timestamp;

public class Pharmacien extends Global_user{

    private int poste;

    public Pharmacien(int cin, String nom, String prenom, int genre, String email, String password, String image, String role, Timestamp dateNaissance, boolean interlock, int numtel, int poste) {
        super(cin, nom, prenom, genre, email, password, image, role, dateNaissance, interlock, numtel);
        this.poste = poste;
    }

    public Pharmacien(int id, int cin, int numtel, int genre, String nom, String prenom, String email, String password, String image, String role, Timestamp dateNaissance, boolean interlock, int poste) {
        super(id, cin, numtel, genre, nom, prenom, email, password, image, role, dateNaissance, interlock);
        this.poste = poste;
    }
    public Pharmacien() {
    }

    public int getPoste() {
        return poste;
    }

    public void setPoste(int poste) {
        this.poste = poste;
    }

    @Override
    public String toString() {
        return super.toString() + ", poste=" + poste +
                '}';
    }
}

