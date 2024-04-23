package services;

import entities.RendezVous;

import java.sql.SQLException;
import java.util.List;

public interface RdvService {


    void ajouterRDV(RendezVous R) throws SQLException;
    void modifier (RendezVous R,int id) throws SQLException;
    void supprimer(int id) throws SQLException;
    List<RendezVous> recuperer() throws SQLException;





}
