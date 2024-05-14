package Service;

import java.sql.SQLException;
import java.util.List;

public interface IService1 <T> {
    public void addAdmin(T t);
    public void deleteAdmin(int id) throws SQLException;
    public void updateAdmin(T t) throws SQLException;
    public List<T> getData();
}
