package Service;

import java.sql.SQLException;
import java.util.List;

public interface IService4 <T> {
    public void addPharmacien(T t);
    public void deletePharmacien(int id) throws SQLException;
    public void updatePharmacien(T t) throws SQLException;
    public List<T> getData();
}
