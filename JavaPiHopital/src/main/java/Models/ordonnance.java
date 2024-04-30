package Models;

import java.util.Date;

public class ordonnance {
    int id ;
    Date dateprescription;
    Date renouvellement ;
    String medecamentprescrit;
    String adresse ;
    int idpatient_id ;

    int dossiermedical_id;
    String digitalSignature;


    public ordonnance() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdpatient_id() {
        return idpatient_id;
    }

    public void setIdpatient_id(int idpatient_id) {
        this.idpatient_id = idpatient_id;
    }


    public Date getDateprescription() {
        return dateprescription;
    }

    public void setDateprescription(Date dateprescription) {
        this.dateprescription = dateprescription;
    }

    public Date getRenouvellement() {
        return renouvellement;
    }

    public void setRenouvellement(Date renouvellement) {
        this.renouvellement = renouvellement;
    }

    public String getMedecamentprescrit() {
        return medecamentprescrit;
    }

    public void setMedecamentprescrit(String medecamentprescrit) {
        this.medecamentprescrit = medecamentprescrit;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getDigitalSignature() {
        return digitalSignature;
    }

    public void setDigitalSignature(String digitalSignature) {
        this.digitalSignature = digitalSignature;
    }






    public ordonnance(int id, Date dateprescription, Date renouvellement, String medecamentprescrit, String adresse, int idpatient_id, int dossiermedical_id,String digitalSignature ) {
        this.id = id;
        this.digitalSignature =digitalSignature ;
        this.dateprescription = dateprescription;
        this.renouvellement = renouvellement;
        this.medecamentprescrit = medecamentprescrit;
        this.adresse = adresse;
        this.idpatient_id = idpatient_id;
        this.dossiermedical_id = dossiermedical_id;
    }

    public int getDossiermedical_id() {
        return dossiermedical_id;
    }

    public void setDossiermedical_id(int dossiermedical_id) {
        this.dossiermedical_id = dossiermedical_id;
    }

    @Override
    public String toString() {
        return "ordonnance{" +
                "id=" + id +
                ", dateprescription=" + dateprescription +
                ", renouvellement=" + renouvellement +
                ", medecamentprescrit='" + medecamentprescrit + '\'' +
                ", adresse='" + adresse + '\'' +
                ", idpatient_id=" + idpatient_id +
                ", dossiermedical_id=" + dossiermedical_id +
                ", digitalSignature='" + digitalSignature + '\'' +
                '}';
    }
}
