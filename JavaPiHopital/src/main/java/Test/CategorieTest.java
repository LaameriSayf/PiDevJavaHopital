package Test;

import Model.Categorie;
import Service.CategorieService;

public class CategorieTest {
    public static void main(String[] args) {
        Categorie cat =new Categorie(7,"saif", "saif","saif");
        CategorieService cs=new CategorieService();
        cs.addCategorie(cat);
        //cs.updateCategorie(cat);
       // cs.deleteCategorie(cat);
        System.out.println(cs.getData());
    }
}
