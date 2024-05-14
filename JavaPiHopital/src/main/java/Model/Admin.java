package Model;

import java.sql.Timestamp;

public class Admin extends Global_user{


    public Admin(int cin, String nom, String prenom, int genre, String email, String password, String image, String role, Timestamp dateNaissance, boolean interlock, int numtel) {
        super(cin, nom, prenom, genre, email, password, image, role, dateNaissance, interlock, numtel);
    }

    public Admin(int id, int cin, int numtel, int genre, String nom, String prenom, String email, String password, String image, String role, Timestamp dateNaissance, boolean interlock) {
        super(id, cin, numtel, genre, nom, prenom, email, password, image, role, dateNaissance, interlock);
    }

    public Admin() {

    }
}
