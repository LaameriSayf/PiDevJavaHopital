package Test;

import Interface.ICommentaire;
import Model.Admin;
import Model.Blog;
import Model.Commentaire;
import Model.CtegorieBlog;
import Service.CommentaireService;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.ArrayList;

public class CommenataireTest {
    /*public static void main(String[] args) {
        CtegorieBlog ctgb=new CtegorieBlog();
        Blog b=new Blog(52,"","","","",4, LocalDate.now(),ctgb);
        Admin a=new Admin(4,4,"","","");
        Commentaire cm1=new Commentaire(67,b,a,"welcome java comentaire avec modification1",0,0);
        CommentaireService cms=new CommentaireService();

        /*************************Test fonction ajouter commentaire***************************************************/
        // cms.add(cm1);

         /*************************Test modifier contenue commentaire**************************************************/
       //  cms.update(cm1);
         /****************************Test Delete Commentaire**************************************************************/
        //cms.delete(cm1);
         /********************Test Like********************************************************/
       // cms.like(cm1,a);
         /******************Test Dislike***********************************************/
         //cms.dislike(cm1,a);

    //    cms.incrementeNbrLike(cm1);
      //  cms.incrementeNbrDisLike(cm1);

//
/*******************************Partager sur Facebook************************************************************************************************/

    //}
 /**************************************************Fin**************************************************************************************/
}//
