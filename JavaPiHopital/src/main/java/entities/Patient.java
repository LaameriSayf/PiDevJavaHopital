package entities;

public class Patient {

    Integer id ;
    String nom;
    String email;
    String prenom;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Patient(Integer id, String nom, String email, String prenom) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.prenom = prenom;
    }
}
