package Test;

import Controller.MenuDashboardLayout;
import Model.Blog;
import Model.CtegorieBlog;
import Service.BlogService;

import java.time.LocalDate;

public class BlogTest {
    public static void main(String[] args) {
        // Création d'une catégorie de blog
        CtegorieBlog ctm = new CtegorieBlog();
ctm.setId(97);
        // Obtention de la date actuelle
        LocalDate date = LocalDate.now();

        // Création d'un objet Blog avec des données fictives
            Blog b1=new Blog(52,"dd","dd","ddd","ee",4,date,ctm);
        // Création d'une instance de BlogService
             BlogService bs = new BlogService();

        /***********************Appel de la méthode add pour ajouter le blog******************************/
              //    bs.add(b1);
        /******************************Appel de La methode afficher de blog et categorie associer*******************************/
                    System.out.println("Liste des blogs avec categorie associer"+bs.getAll());
        /********************************Supprimer blog***************************************************/
            // System.out.println("Blog doit supprier"+bs.delete(b1));
        /*********************************Tester methode get blog by id ********************************/
                  System.out.println(bs.getBlogById(52));
        /*********************************Modifier une Blog et categorie associer *************************/
            //      bs.update(b1);

      //  bs.shareFacebook(b1);

        MenuDashboardLayout mdl=new MenuDashboardLayout();

        System.out.println(bs.getTitleOfBlogById(117));
        System.out.println(bs.getCommentsByBlogId(3));
    }

}
