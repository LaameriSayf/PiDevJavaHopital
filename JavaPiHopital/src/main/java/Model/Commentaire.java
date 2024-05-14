package Model;

import java.util.ArrayList;
import java.util.Collection;

public class Commentaire {
    private int id ;
    private Blog blog;
    private  Admin admin;
    private String contenue;
    private int nbLike;
    private int nbDislike;
    private Collection<Like> likes;
    private Collection<Dislike> dislikes;




    public Commentaire() {
    }

    public Commentaire(int id, Blog blog, Admin admin, String contenue, int nbLike, int nbDislike) {
        this.id = id;
        this.blog = blog;
        this.admin = admin;
        this.contenue = contenue;
        this.nbLike = nbLike;
        this.nbDislike = nbDislike;
        this.likes = new ArrayList<>();
        this.dislikes = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public String getContenue() {
        return contenue;
    }

    public void setContenue(String contenue) {
        this.contenue = contenue;
    }

    public int getNbLike() {
        return nbLike;
    }

    public void setNbLike(int nbLike) {
        this.nbLike = nbLike;
    }

    public int getNbDislike() {
        return nbDislike;
    }

    public void setNbDislike(int nbDislike) {
        this.nbDislike = nbDislike;
    }

    public Collection<Like> getLikes() {
        return likes;
    }

    public void setLikes(Collection<Like> likes) {
        this.likes = likes;
    }

    public Collection<Dislike> getDislikes() {
        return dislikes;
    }

    public void setDislikes(Collection<Dislike> dislikes) {
        this.dislikes = dislikes;
    }

    public void addLike(Like like) {
        if (likes == null) {
            likes = new ArrayList<>();
        }
        likes.add(like);
        nbLike++; // Incrémenter le compteur de likes
    }


    public void removeLike(Like like) {
        if (likes != null) {
            likes.remove(like);
            nbLike--; // Décrémenter le compteur de likes
        }
    }

    public void addDislike(Dislike dislike) {
        if (dislikes == null) {
            dislikes = new ArrayList<>();
        }
        dislikes.add(dislike);
        nbDislike++; // Incrémenter le compteur de dislikes
    }

    public void removeDislike(Dislike dislike) {
        if (dislikes != null) {
            dislikes.remove(dislike);
            nbDislike--; // Décrémenter le compteur de dislikes
        }
    }


    @Override
    public String toString() {
        return "Commentaire{" +
                "id=" + id +
                ", blog=" + blog +
                ", admin=" + admin +
                ", contenue='" + contenue + '\'' +
                ", nbLike=" + nbLike +
                ", nbDislike=" + nbDislike +
                '}';
    }



}
