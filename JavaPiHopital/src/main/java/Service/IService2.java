package Service;

import java.sql.SQLException;
import java.util.List;

public interface IService2<T> {
    public void addMedecin(T t);
    public void deleteMedecin(int id) throws SQLException;
    public void updateMedecin(T t) throws SQLException;
    public List<T> getData();



}
