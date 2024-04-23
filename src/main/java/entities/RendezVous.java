package entities;
import java.time.LocalDate;
import java.util.Objects;

public class RendezVous {
private String description ,file;
private int id;
private LocalDate daterdv;
private String heurerdv;
private Boolean etat;



    public RendezVous(String description, String file, LocalDate daterdv, String heurerdv) {
        this.description = description;
        this.file = file;
        this.daterdv = daterdv;
        this.heurerdv = heurerdv;
    }
    public RendezVous() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDaterdv() {
        return daterdv;
    }

    public void setDaterdv(LocalDate daterdv) {
        this.daterdv = daterdv;
    }

    public String getHeurerdv() {
        return heurerdv;
    }

    public void setHeurerdv(String heurerdv) {
        this.heurerdv = heurerdv;
    }

    public Boolean getEtat() {
        return etat;
    }

    public void setEtat(Boolean etat) {
        this.etat = etat;
    }

    @Override
    public String toString() {
        return "RendezVous{" +
                "description='" + description + '\'' +
                ", file='" + file + '\'' +
                ", daterdv=" + daterdv +
                ", heurerdv=" + heurerdv +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RendezVous that = (RendezVous) o;
        return Objects.equals(description, that.description) && Objects.equals(file, that.file) && Objects.equals(daterdv, that.daterdv) && Objects.equals(heurerdv, that.heurerdv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, file, daterdv, heurerdv);
    }



}
