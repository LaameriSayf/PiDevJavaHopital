package Models;

import java.util.Date;

public class dossiermedical {
    int id;
    String resultatexamen;
    Date date_creation;
    String antecedentspersonelles;
    String image;
    int patient_id;
    public dossiermedical() {
    }

    public dossiermedical(int id, String resultatexamen, Date date_creation, String antecedentspersonelles, String image, int patient_id) {
        this.id = id;
        this.patient_id = patient_id;
        this.resultatexamen = resultatexamen;
        this.date_creation = date_creation;
        this.antecedentspersonelles = antecedentspersonelles;
        this.image = image;

    }



    public  int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public  String getResultatexamen() {
        return resultatexamen;
    }

    public void setResultatexamen(String resultatexamen) {
        this.resultatexamen = resultatexamen;
    }

    public Date getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(Date date_creation) {
        this.date_creation = date_creation;
    }

    public  String getAntecedentspersonelles() {
        return antecedentspersonelles;
    }

    public void setAntecedentspersonelles(String antecedentspersonelles) {
        this.antecedentspersonelles = antecedentspersonelles;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    @Override
    public String toString() {
        return "dossiermedical{" +
                "id=" + id +
                ", resultatexamen='" + resultatexamen + '\'' +
                ", date_creation=" + date_creation +
                ", antecedentspersonelles='" + antecedentspersonelles + '\'' +
                ", image='" + image + '\'' +
                ", patient_id=" + patient_id +
                '}';
    }
    private Patient patient;


    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patientatient) {
        this.patient = patient;
    }

    public int getPatientId() {
        if (patient!= null) {
            return patient.getId();
        } else {
            return 1;
        }
    }




}
