package Service;

import Model.Categorie;

import java.util.ArrayList;
import java.util.List;

public interface ICategorie <T>{

        void addCategorie(T t);
        void deleteCategorie(T t);
        void updateCategorie(T t);
        List<T> getData();

    ArrayList<Categorie> getBytitreDescription(Categorie ctegorieBlog);
}
