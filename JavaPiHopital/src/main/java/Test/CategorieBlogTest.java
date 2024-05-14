package Test;

import Model.CtegorieBlog;
import Service.BlogService;
import Service.CategoriBlogService;

import java.util.ArrayList;

public class CategorieBlogTest {

    public static void main(String[] args) {
        /**********************************InstansiationCatgorieBlog*************************************/

        CtegorieBlog ct1 = new CtegorieBlog(456, "as", "sasa");
        CtegorieBlog ct2 = new CtegorieBlog(456, "Firmi", "firmi");
        CtegorieBlog ct3 = new CtegorieBlog(456, "TestJAVAcAT", "testjavacateg");

        CategoriBlogService cs = new CategoriBlogService();
        /**************************TestAffichageCatgorieBlog**********************************************/
       System.out.println("javafx"+cs.getAll());
        /*************************TestAjouterCategorieBlog******************************************/
        cs.add(ct2);
        /**********************************TestUpdateCategorieBlog**********************************************/
        ct2.setId(456);
        ct2.setTitrecategorie("Test Update");
        ct2.setDescription("testupdate");
        cs.update(ct2);
        /*********************************TestSupprimerCategorieBlog*******************************************/
                    ct1.setId(110);
                    cs.delete(ct1);
        /**********************************Test recherche selon any caractere**********************************/

            //System.out.println(cs.getBytitreDescription(ct3));
        /*********************************Test TRier By titre Categorie blogs ***************************************/
             //   System.out.println(cs.TrierByTitre(true)); // Pour un tri croissant
               // System.out.println(cs.TrierByTitre(false)); // Pour un tri decroissant
        /****************************Test Trier By Description Categorieblogs*****************************************/
            //System.out.println("Liste trier croisant selon description"+cs.TrierByDescription(true));//Pour trier croissant
                //System.out.println("Liste trier decroissant selon desscription"+cs.TrierByDescription(false));//Pour trier decrpoissant

        System.out.println(cs.getCategorieBlogById(113));

        System.out.println("Nombre categorie"+cs.NombreCategorieBlog());


    }
}
