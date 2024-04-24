package Service;

import Interface.ICommentaire;
import Model.Admin;
import Model.Commentaire;
import Model.Dislike;
import Model.Like;
import Util.DataBase;
import com.mysql.cj.x.protobuf.MysqlxCrud;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CommentaireService implements ICommentaire<Commentaire>{
    private Connection cnx;

    public CommentaireService(){
        this.cnx= DataBase.getInstance().getCnx();
    }
    /*****************************Ajouter Commentaire en meme temps affecter a blog specifique et user specifique ************************/

    public void add(int idBlog, String contenu) {
        String rqt ="INSERT INTO `commentaire`(`idblog_id`,`idadmin_id`,`contenue`,`nblike`,`nbdislike`) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement stm= cnx.prepareStatement(rqt);
            stm.setInt(1, idBlog);
            stm.setInt(2,4);
            stm.setString(3, contenu);
            stm.setInt(4, 0); // Valeur initiale de nblike à 0
            stm.setInt(5, 0); // Valeur initiale de nbdislike à 0
            stm.executeUpdate();
            System.out.println("Commentaire ajouté avec succès");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout du commentaire : " + e);
        }
    }


    /*************************************Afficher all commentaire with blog associer et user *****************************************************************/


    @Override
    public ArrayList<Commentaire> getAll() {
        ArrayList<Commentaire> lcm=new ArrayList<>();
        String rqt="SELECT * FROM `commentaire` ";
        try {
            PreparedStatement stm= cnx.prepareStatement(rqt);
            ResultSet rs=stm.executeQuery();
            while (rs.next()){
                Commentaire cmm=new Commentaire();
                cmm.setId(rs.getInt("id"));
              //cmm.setAdmin(rs.getInt("idadmin_id"));

            }
        } catch (SQLException e) {
            throw new RuntimeException("Ereure dans fonction Afficher all commentaire"+e);
        }
        return null;
    }
/**********************************************************Modifier commentaire*********************************************************************/

    @Override
    public void update(Commentaire commentaire) {
        String rqt="UPDATE commentaire SET contenue = ?  WHERE id = ?";
        try {
            PreparedStatement stm= cnx.prepareStatement(rqt);
            stm.setString(1,commentaire.getContenue());
            stm.setInt(2,commentaire.getId());
            stm.executeUpdate();
            System.out.println("Contenue de commentaire a modififer avec succes");
        } catch (SQLException e) {
            throw new RuntimeException("Ereure lors de modification de contenu de commentaire"+e);
        }

    }
/********************************************************SupprimerCommentaire**********************************************************************************/
@Override
public boolean delete(Commentaire commentaire) {
    String deleteLikesQuery = "DELETE FROM `like` WHERE commentaire_id = ?";
    String deleteDislikesQuery = "DELETE FROM `dislike` WHERE commentaire_id = ?";
    String deleteCommentaireQuery = "DELETE FROM `commentaire` WHERE id = ?";

    try {
        // Supprimer les likes associés au commentaire
        PreparedStatement deleteLikesStatement = cnx.prepareStatement(deleteLikesQuery);
        deleteLikesStatement.setInt(1, commentaire.getId());
        deleteLikesStatement.executeUpdate();

        // Supprimer les dislikes associés au commentaire
        PreparedStatement deleteDislikesStatement = cnx.prepareStatement(deleteDislikesQuery);
        deleteDislikesStatement.setInt(1, commentaire.getId());
        deleteDislikesStatement.executeUpdate();

        // Supprimer le commentaire lui-même
        PreparedStatement deleteCommentaireStatement = cnx.prepareStatement(deleteCommentaireQuery);
        deleteCommentaireStatement.setInt(1, commentaire.getId());
        deleteCommentaireStatement.executeUpdate();

        System.out.println("Commentaire supprimé avec succès.");
        return true;
    } catch (SQLException e) {
        throw new RuntimeException("Erreur lors de la suppression du commentaire : " + e.getMessage());
    }
}
/*****************************************************Incremente Nombre LikeCommenatire********************************************************************/
public void incrementeNbrLike(Commentaire cmtr) {
    String rqt = "UPDATE `commentaire` SET nblike = nblike + 1 WHERE id = ?";

    try {
        PreparedStatement stm = cnx.prepareStatement(rqt);
        stm.setInt(1, cmtr.getId());
        stm.executeUpdate();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}
/****************************************************Desincremente Nombre dilike***********************************************************************/

    public void DincrementeNbrLike(Commentaire cmtr) {
        String rqt = "UPDATE `commentaire` SET nbdislike = nbdislike - 1 WHERE id = ?";

        try {
            PreparedStatement stm = cnx.prepareStatement(rqt);
            stm.setInt(1, cmtr.getId());
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
/*****************************************Incremente Nombre DiLike ***************************************************************************************/
    public void incrementeNbrDisLike(Commentaire cmtr) {
        String rqt = "UPDATE `commentaire` SET nbdislike = nbdislike + 1 WHERE id = ?";

        try {
            PreparedStatement stm = cnx.prepareStatement(rqt);
            stm.setInt(1, cmtr.getId());
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
/**************************************Desincremente nombre like**********************************************************************************/
public void dincrementeNbrLike(Commentaire cmtr) {
    String rqt = "UPDATE `commentaire` SET nblike = nblike - 1 WHERE id = ?";

    try {
        PreparedStatement stm = cnx.prepareStatement(rqt);
        stm.setInt(1, cmtr.getId());
        stm.executeUpdate();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

/*************************************MettreLike a Commentaire******************************************************************************************/

    public void like(Commentaire cmtr,Admin admin){
    String rqt="INSERT INTO `like`(`commentaire_id`,`userr_id`) VALUES(?,?)";
        try {
            PreparedStatement stm= cnx.prepareStatement(rqt);

            if (!hasUserLikedComment(cmtr.getId(),admin.getId())){
            stm.setInt(1,cmtr.getId());
            stm.setInt(2,admin.getId());

            stm.executeUpdate();
            incrementeNbrLike(cmtr);
            DincrementeNbrLike(cmtr);
            removeDisLike(cmtr.getId(), admin.getId());
            }else{
                System.out.println("You liked this before :) ");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
/*************************************Mettre Dislike commentaire **************************************************************************************/
public void dislike(Commentaire cmtr,Admin admin){
    String rqt="INSERT INTO `dislike`(`commentaire_id`,`userr_id`) VALUES(?,?)";
    try {
        PreparedStatement stm= cnx.prepareStatement(rqt);

        if (!hasUserDislikedComment(cmtr.getId(),admin.getId())){
            stm.setInt(1,cmtr.getId());
            stm.setInt(2,admin.getId());

            stm.executeUpdate();
            incrementeNbrDisLike(cmtr);
            dincrementeNbrLike(cmtr);
            removeLike(cmtr.getId(), admin.getId());
        }else{
            System.out.println("You disliked this before :) ");
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

}

/***********************************Verifier si utilisateur mettre like *********************************************************************************/

    private boolean hasUserLikedComment( int commentaireId, int userId) throws SQLException {
        String query = "SELECT * FROM `like` WHERE commentaire_id = ? AND userr_id = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, commentaireId);
            statement.setInt(2, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
/************************************Verifier si utilisaateur mettre dilike **********************************************************/

    private boolean hasUserDislikedComment( int commentaireId, int userId) throws SQLException {
        String query = "SELECT * FROM `dislike` WHERE commentaire_id = ? AND userr_id = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, commentaireId);
            statement.setInt(2, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
/**********************************************RemoveLike*************************************************************************************/

public boolean removeLike(int commentaireId, int userId){
    String rqt="DELETE FROM `like` WHERE commentaire_id=? AND userr_id = ?";
    try {
        PreparedStatement stm=cnx.prepareStatement(rqt);
        stm.setInt(1,commentaireId);
        stm.setInt(2,userId);
        stm.executeUpdate();
        return true;
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

/***************************************RemoveDislike******************************************************************************************/
public boolean removeDisLike(int commentaireId, int userId){
    String rqt="DELETE FROM `dislike` WHERE commentaire_id=? AND userr_id = ?";
    try{
        PreparedStatement stm=cnx.prepareStatement(rqt);
        stm.setInt(1,commentaireId);
        stm.setInt(2,userId);
        stm.executeUpdate();
        return true;
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}
/*******************************************************************Fin************************************************************************/


}
