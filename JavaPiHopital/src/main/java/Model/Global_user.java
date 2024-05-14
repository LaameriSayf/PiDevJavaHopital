package Model;

import java.sql.Timestamp;

public class Global_user {
    private int id ,cin,numtel, genre ;
    private String nom,prenom,email,password,image,role;
    private Timestamp dateNaissance;
    private boolean interlock;

    public Global_user(int id, int cin, int numtel, int genre, String nom, String prenom, String email, String password, String image, String role, Timestamp dateNaissance, boolean interlock) {
        this.id = id;
        this.cin = cin;
        this.numtel = numtel;
        this.genre = genre;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.image = image;
        this.role = role;
        this.dateNaissance = dateNaissance;
        this.interlock = interlock;
    }

    public Global_user() {
    }

    public Global_user(int cin, String nom, String prenom, int genre, String email, String password, String image, String role, Timestamp dateNaissance, boolean interlock,int numtel) {
        this.cin = cin;
        this.numtel = numtel;
        this.nom = nom;
        this.prenom = prenom;
        this.genre = genre;
        this.email = email;
        this.password = password;
        this.image = image;
        this.role = role;
        this.dateNaissance = dateNaissance;
        this.interlock = interlock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCin() {
        return cin;
    }

    public void setCin(int cin) {
        this.cin = cin;
    }

    public int getNumtel() {
        return numtel;
    }

    public void setNumtel(int numtel) {
        this.numtel = numtel;
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

    public int getGenre() {
        return genre;
    }

    public void setGenre(int genre) {
        this.genre = genre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Timestamp dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public boolean isInterlock() {
        return interlock;
    }

    public void setInterlock(boolean interlock) {
        this.interlock = interlock;
    }

    @Override
    public String toString() {
        return "Global_user{" +
                "id=" + id +
                ", cin=" + cin +
                ", numtel=" + numtel +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", genre='" + genre + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", image='" + image + '\'' +
                ", role='" + role + '\'' +
                ", dateNaissance=" + dateNaissance +
                ", interlock=" + interlock +
                '}';
    }
}
