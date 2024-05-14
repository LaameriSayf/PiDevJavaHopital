package Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class    Blog {

    private int id;
    private String titre;
    private String description;
    private String iamge;
    private String lieu;
    private float rate;
    private LocalDate date;
    private List<Commentaire> commentaires;
    private CtegorieBlog ctgb;

    public Blog() {
    }

    public Blog(int id, String titre, String description, String iamge, String lieu, float rate, LocalDate date, CtegorieBlog ctgb) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.iamge = iamge;
        this.lieu = lieu;
        this.rate = rate;
        this.date = date;
        this.ctgb = ctgb;
        this.commentaires = new ArrayList<>();
    }



    public List<Commentaire> getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(List<Commentaire> commentaires) {
        this.commentaires = commentaires;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIamge() {
        return iamge;
    }

    public void setIamge(String iamge) {
        this.iamge = iamge;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public CtegorieBlog getCtgb() {
        return ctgb;
    }


    public void setCtgb(CtegorieBlog ctgb) {
        this.ctgb = ctgb;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", iamge='" + iamge + '\'' +
                ", lieu='" + lieu + '\'' +
                ", rate=" + rate +
                ", date=" + date +
                ", ctgb=" + ctgb +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Blog blog = (Blog) o;
        return id == blog.id && Float.compare(blog.rate, rate) == 0 && Objects.equals(titre, blog.titre) && Objects.equals(description, blog.description) && Objects.equals(iamge, blog.iamge) && Objects.equals(lieu, blog.lieu) && Objects.equals(date, blog.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titre, description, iamge, lieu, rate, date);
    }


}
