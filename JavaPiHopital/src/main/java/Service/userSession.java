package Service;
import Model.Global_user;

public class userSession {
    public static Global_user getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(Global_user currentUser) {
        userSession.currentUser = currentUser;
    }

    private static Global_user currentUser;

}
