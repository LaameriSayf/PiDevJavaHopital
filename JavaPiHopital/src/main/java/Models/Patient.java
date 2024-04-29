package Models;

import java.util.Date;

public class Patient extends Global_User{

    int numcarte;

    public Patient(int id, int cin, int numtel, int genre, String nom, String prenom, String email, String password, String image, String role, Date dateNaissance, boolean interlock, int numcarte) {
        super(id, cin, numtel, genre, nom, prenom, email, password, image, role, dateNaissance, interlock);
        this.numcarte = numcarte;
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
        return "Patient{" +
                "numcarte=" + numcarte +
                '}';
    }
}

