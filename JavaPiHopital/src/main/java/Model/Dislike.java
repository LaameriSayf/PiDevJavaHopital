package Model;

public class Dislike {
    private int id;
    private Admin admin;
    private Commentaire cmtr;

    public Dislike() {
    }

    public Dislike(int id, Admin admin, Commentaire cmtr) {
        this.id = id;
        this.admin = admin;
        this.cmtr = cmtr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Commentaire getCmtr() {
        return cmtr;
    }

    public void setCmtr(Commentaire cmtr) {
        this.cmtr = cmtr;
    }
}
