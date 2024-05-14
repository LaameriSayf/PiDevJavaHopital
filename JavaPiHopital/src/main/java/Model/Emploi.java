package Model;

import java.time.LocalDate;
import java.util.Objects;
public class Emploi {
    public int id;
    private String titre;
private LocalDate start;
private LocalDate end;
private String description;
    public Emploi() {
    }

    public Emploi(String titre, LocalDate start, LocalDate end, String description) {
        this.titre = titre;
        this.start = start;
        this.end = end;
        this.description = description;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Emploi emploi)) return false;
        return Objects.equals(getTitre(), emploi.getTitre()) && Objects.equals(getStart(), emploi.getStart()) && Objects.equals(getEnd(), emploi.getEnd()) && Objects.equals(getDescription(), emploi.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitre(), getStart(), getEnd(), getDescription());
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
