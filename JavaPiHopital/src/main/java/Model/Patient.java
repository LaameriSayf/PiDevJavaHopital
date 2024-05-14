package Model;

import java.sql.Timestamp;

public class Patient extends Global_user{
    private int numcarte;

    public Patient(int cin, String nom, String prenom, int genre, String email, String password, String image, String role, Timestamp dateNaissance, boolean interlock, int numtel, int numcarte) {
        super(cin, nom, prenom, genre, email, password, image, role, dateNaissance, interlock, numtel);
        this.numcarte = numcarte;
    }

    public Patient(int id, int cin, int numtel, int genre, String nom, String prenom, String email, String password, String image, String role, Timestamp dateNaissance, boolean interlock, int numcarte) {
        super(id, cin, numtel, genre, nom, prenom, email, password, image, role, dateNaissance, interlock);
        this.numcarte = numcarte;

    }

    public Patient(int cin, String nom, String prenom, int genre, String email, String password, String image, String role, Timestamp dateNaissance, boolean interlock, int numtel) {
        super(cin, nom, prenom, genre, email, password, image, role, dateNaissance, interlock, numtel);
    }

    public Patient() {
    }



    public int getNumcarte() {
        return numcarte;
    }

    public void setNumcarte(int numcarte) {
        this.numcarte = numcarte;
    }

    @Override
    public String toString() {
        return super.toString() + ", numcarte=" + numcarte +
                '}';
    }
}
