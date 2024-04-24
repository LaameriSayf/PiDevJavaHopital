package Interface;

import java.util.ArrayList;

public interface IBlog<T> {
    void add (T t);
    ArrayList<T> getAll();
    void update(T t);
    boolean delete(T t);

}
