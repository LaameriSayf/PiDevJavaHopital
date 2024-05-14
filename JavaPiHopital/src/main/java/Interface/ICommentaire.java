package Interface;

import java.util.ArrayList;

public interface ICommentaire <T>{

    ArrayList<T> getAll();
    void update(T t);
    boolean delete(T t);

}
