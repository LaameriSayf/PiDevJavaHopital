package Service;

import Model.dossiermedical;

import java.sql.SQLException;
import java.util.List;

public interface IService5<T>{
    dossiermedical afficher(T t ) throws SQLException;
    void ajouter (T t ) throws SQLException;
    void modifier (T t) throws SQLException;


    void supprimer (int postId ) throws SQLException;
    List<T> getData() throws SQLException;

}
