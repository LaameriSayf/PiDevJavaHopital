package Model;

import java.util.Objects;

public class CtegorieBlog {

    private int id;
    private String titrecategorie;
    private String description;

    public CtegorieBlog() {
    }

    public CtegorieBlog(int id, String titrecategorie, String description) {
        this.id =++id;
        this.titrecategorie = titrecategorie;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitrecategorie() {
        return titrecategorie;
    }

    public void setTitrecategorie(String titrecategorie) {
        this.titrecategorie = titrecategorie;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CtegorieBlog{" +
                "id=" + id +
                ", titrecategorie='" + titrecategorie + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CtegorieBlog that = (CtegorieBlog) o;
        return id == that.id && Objects.equals(titrecategorie, that.titrecategorie) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titrecategorie, description);
    }
}
