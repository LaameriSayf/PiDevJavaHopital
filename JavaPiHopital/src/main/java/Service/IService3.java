package Service;

import java.sql.SQLException;
import java.util.List;

public interface IService3 <T> {
    public void addPatient(T t);

    public void deletePatient(int id) throws SQLException;

    public void updatePatient(T t) throws SQLException;


    public List<T> getData();
}
