package Interface;

import Model.CtegorieBlog;

import java.util.ArrayList;

public interface IcategoriBlog <T>{

    void add (T t);
    ArrayList<T> getAll();
    void update(T t);
    boolean delete(T t);

    ArrayList<T>getBytitreDescription(T t); //recherche selon 2 criterer

    ArrayList<T>TrierByTitre(boolean ascending); //Trie soit croissant soit decroissant

    ArrayList<T>TrierByDescription(boolean ascending); //trie soit croissant soit decroissant
//getOne getById

}
