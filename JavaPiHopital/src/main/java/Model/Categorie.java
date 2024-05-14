package Model;

import java.util.ArrayList;
import java.util.List;

public class Categorie {
    private int id;
    private String nom_cat;
    private String type_cat;
    private String description_cat;

    private List <Medicament> MedicamentList;

    public Categorie(String nom_cat, String type_cat, String description_cat) {
    }

    public Categorie(int id, String nom_cat, String type_cat, String description_cat) {
        this.id = id;
        this.nom_cat = nom_cat;
        this.type_cat = type_cat;
        this.description_cat = description_cat;
        this.MedicamentList=new ArrayList<>();
    }

    public Categorie() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom_cat() {
        return nom_cat;
    }

    public void setNom_cat(String nom_cat) {
        this.nom_cat = nom_cat;
    }

    public String getType_cat() {
        return type_cat;
    }

    public void setType_cat(String type_cat) {
        this.type_cat = type_cat;
    }

    public String getDescription_cat() {
        return description_cat;
    }

    public void setDescription_cat(String description_cat) {
        this.description_cat = description_cat;
    }

    @Override
    public String toString() {
        return "Categorie{" +
                "id=" + id +
                ", nom_cat='" + nom_cat + '\'' +
                ", type_cat='" + type_cat + '\'' +
                ", description_cat='" + description_cat + '\'' +
                '}';
    }


}
