package Service;
import Model.Emploi;

import java.sql.SQLException;
import java.util.List;

public interface EmploiService {
    void ajouterEmploi(Emploi E) throws SQLException;
    void modifierEmploi(Emploi E,int id) throws SQLException;
    void supprimerEmploi(int id) throws SQLException;
    List<Emploi> recuperer() throws SQLException;




}
