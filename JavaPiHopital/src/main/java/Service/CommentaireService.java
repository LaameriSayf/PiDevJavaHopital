package Service;

import Interface.ICommentaire;
import Model.Admin;
import Model.Commentaire;
import Model.Dislike;
import Model.Like;
import Util.DataBase;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import javafx.scene.control.Alert;

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
            stm.setInt(2,6);
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

public void DincrementeNbrdisLike(Commentaire cmtr) {
    String rqt = "UPDATE `commentaire` SET nbdislike = nbdislike - 1 WHERE id = ?";

    try {
        PreparedStatement stm = cnx.prepareStatement(rqt);
        stm.setInt(1, cmtr.getId());
        stm.executeUpdate();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}
/****************************************************************************************************************************************/
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


public void like(Commentaire cmtr, Admin admin) {
    String likeQuery = "INSERT INTO `like`(`commentaire_id`,`userr_id`) VALUES(?,?)";
    String likeDeleteQuery = "DELETE FROM `like` WHERE commentaire_id=? AND userr_id = ?";
    String dislikeDeleteQuery = "DELETE FROM `dislike` WHERE commentaire_id=? AND userr_id = ?";

    try {
        if (!hasUserLikedComment(cmtr.getId(), admin.getId())) {
            // Supprimer le dislike de l'utilisateur s'il existe
            removeDisLike(cmtr.getId(), admin.getId());

            // Supprimer le like de l'utilisateur s'il existe déjà
            removeLike(cmtr.getId(), admin.getId());

            // Insérer le like de l'utilisateur
            PreparedStatement likeStm = cnx.prepareStatement(likeQuery);
            likeStm.setInt(1, cmtr.getId());
            likeStm.setInt(2, admin.getId());
            likeStm.executeUpdate();

            // Mettre à jour le nombre de likes dans le commentaire
            incrementeNbrLike(cmtr);

            // JavaFX Alert: Like successful
            showInformationAlert("Success", "You liked the comment successfully!");
        } else {
            // JavaFX Alert: Already liked
            showWarningAlert("Warning", "You already liked this comment before :)");
        }
    } catch (SQLException e) {
        // Handle SQL exception
        handleSQLException(e);
    }
}

    public void dislike(Commentaire cmtr, Admin admin) {
        String dislikeQuery = "INSERT INTO `dislike`(`commentaire_id`,`userr_id`) VALUES(?,?)";
        String dislikeDeleteQuery = "DELETE FROM `dislike` WHERE commentaire_id=? AND userr_id = ?";
        String likeDeleteQuery = "DELETE FROM `like` WHERE commentaire_id=? AND userr_id = ?";

        try {
            if (!hasUserDislikedComment(cmtr.getId(), admin.getId())) {
                // Supprimer le like de l'utilisateur s'il existe
                removeLike(cmtr.getId(), admin.getId());

                // Supprimer le dislike de l'utilisateur s'il existe déjà
                removeDisLike(cmtr.getId(), admin.getId());

                // Insérer le dislike de l'utilisateur
                PreparedStatement dislikeStm = cnx.prepareStatement(dislikeQuery);
                dislikeStm.setInt(1, cmtr.getId());
                dislikeStm.setInt(2, admin.getId());
                dislikeStm.executeUpdate();

                // Mettre à jour le nombre de dislikes dans le commentaire
                incrementeNbrDisLike(cmtr);

                // JavaFX Alert: Dislike successful
                showInformationAlert("Success", "You disliked the comment successfully!");
            } else {
                // JavaFX Alert: Already disliked
                showWarningAlert("Warning", "You already disliked this comment before :)");
            }
        } catch (SQLException e) {
            // Handle SQL exception
            handleSQLException(e);
        }
    }


    private void showInformationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void handleSQLException(SQLException e) {
        // Ici, vous pouvez ajouter un traitement spécifique à votre application, par exemple, logger l'erreur ou afficher un message d'erreur à l'utilisateur
        throw new RuntimeException(e);
    }
    // Méthode pour afficher une alerte d'avertissement
    private void showWarningAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /***********************************Verifier si utilisateur mettre like *********************************************************************************/


public boolean update(int commentaireId, String nouveauContenu, int userId) {
    // Vérifier si le commentaire appartient à l'utilisateur
    if (belongsToUser(commentaireId, userId)) {
        String rqt = "UPDATE commentaire SET contenue = ? WHERE id = ?";
        try {
            PreparedStatement stm = cnx.prepareStatement(rqt);
            stm.setString(1, nouveauContenu);
            stm.setInt(2, commentaireId);
            int rowsAffected = stm.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la modification du contenu du commentaire : " + e);
        }
    } else {
        // Le commentaire n'appartient pas à l'utilisateur
        return false;
    }
}

    public boolean belongsToUser(int commentaireId, int userId) {
        String query = "SELECT * FROM commentaire WHERE id = ? AND idadmin_id = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, commentaireId);
            statement.setInt(2, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la vérification de l'appartenance du commentaire à l'utilisateur : " + e);
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
        // Supprimer le like de la table 'like'
        PreparedStatement stm=cnx.prepareStatement(rqt);
        stm.setInt(1,commentaireId);
        stm.setInt(2,userId);
        stm.executeUpdate();

        // Décrémenter le nombre de likes dans la table 'commentaire' si le nombre actuel est supérieur à zéro
        String updateQuery = "UPDATE `commentaire` SET nblike = GREATEST(nblike - 1, 0) WHERE id = ?";
        PreparedStatement updateStm = cnx.prepareStatement(updateQuery);
        updateStm.setInt(1, commentaireId);
        updateStm.executeUpdate();

        return true;
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}


/***************************************RemoveDislike******************************************************************************************/
public boolean removeDisLike(int commentaireId, int userId){
    String rqt="DELETE FROM `dislike` WHERE commentaire_id=? AND userr_id = ?";
    try{
        // Supprimer le dislike de la table 'dislike'
        PreparedStatement stm=cnx.prepareStatement(rqt);
        stm.setInt(1,commentaireId);
        stm.setInt(2,userId);
        stm.executeUpdate();

        // Décrémenter le nombre de dislikes dans la table 'commentaire' si le nombre actuel est supérieur à zéro
        String updateQuery = "UPDATE `commentaire` SET nbdislike = GREATEST(nbdislike - 1, 0) WHERE id = ?";
        PreparedStatement updateStm = cnx.prepareStatement(updateQuery);
        updateStm.setInt(1, commentaireId);
        updateStm.executeUpdate();

        return true;
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

/*******************************************************************Fin************************************************************************/

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

/**************************************************************************************************************************************/

public int nbrlike(Commentaire comment) {
    String query = "SELECT nblike FROM commentaire WHERE id = ?";
    try {
        PreparedStatement stm = cnx.prepareStatement(query);
        stm.setInt(1, comment.getId());

        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            int nbLike = rs.getInt("nblike");
            return nbLike;
        } else {
            System.out.println("Comment not found");
            return 0; // ou une valeur par défaut appropriée si le commentaire n'est pas trouvé
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

    public int nbrdislike(Commentaire comment) {
        String query = "SELECT nbdislike FROM commentaire WHERE id = ?";
        try {
            PreparedStatement stm = cnx.prepareStatement(query);
            stm.setInt(1, comment.getId());

            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                int nbDislike = rs.getInt("nbdislike");
                return nbDislike;
            } else {
                System.out.println("Comment not found");
                return 0; // ou une valeur par défaut appropriée si le commentaire n'est pas trouvé
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
