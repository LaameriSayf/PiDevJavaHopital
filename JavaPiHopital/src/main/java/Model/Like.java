package Model;

public class Like {
    private int id;
    private Commentaire cmtr;

    private Admin admin;

    public Like() {
    }

    public Like(int id, Commentaire cmtr, Admin admin) {
        this.id = id;
        this.cmtr = cmtr;
        this.admin = admin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Commentaire getCmtr() {
        return cmtr;
    }

    public void setCmtr(Commentaire cmtr) {
        this.cmtr = cmtr;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "Like{" +
                "id=" + id +
                ", cmtr=" + cmtr +
                ", admin=" + admin +
                '}';
    }
}
