package Service;
import java.sql.SQLException;
import java.util.List;
public interface IService <T> {
    public void addGlobal_user(T t);
    public void deleteGlobal_user(int id) throws SQLException;
    public void updateGlobal_user(T t) throws SQLException;

    public List<T> getData();
}