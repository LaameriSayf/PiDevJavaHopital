package Model;
import java.time.LocalDate;
import java.util.Date;

public class Medicament {
    private int id;
    private String ref_med;
    private String nom_med;
    private LocalDate date_amm;
    private LocalDate date_expiration;
    private int qte;
    private String description;
    private String etat;
    private String image;
    private Categorie categorie;





    public Medicament(int id,Categorie categorie,String ref_med, String nom_med, LocalDate date_amm, LocalDate date_expiration, int qte, String description, String etat, String image) {
       this.id=id;

        this.ref_med = ref_med;
        this.nom_med = nom_med;
        this.date_amm = date_amm;
        this.date_expiration = date_expiration;
        this.qte = qte;
        this.description = description;
        this.etat = etat;
        this.image = image;
        this.categorie=categorie;
    }
    public Medicament() {
    }


    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public String getRef_med() {
        return ref_med;
    }

    public void setRef_med(String ref_med) {
        this.ref_med = ref_med;
    }

    public String getNom_med() {
        return nom_med;
    }

    public void setNom_med(String nom_med) {
        this.nom_med = nom_med;
    }

    public LocalDate getDate_amm() {
        return  date_amm;
    }

    public void setDate_amm(LocalDate date_amm) {
        this.date_amm = date_amm;
    }

    public LocalDate getDate_expiration() {
        return  date_expiration;
    }

    public void setDate_expiration(LocalDate date_expiration) {
        this.date_expiration = date_expiration;
    }

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Medicament{" +
                "id=" + id +
                ", ref_med='" + ref_med + '\'' +
                ", nom_med='" + nom_med + '\'' +
                ", date_amm=" + date_amm +
                ", date_expiration=" + date_expiration +
                ", qte=" + qte +
                ", description='" + description + '\'' +
                ", etat='" + etat + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
