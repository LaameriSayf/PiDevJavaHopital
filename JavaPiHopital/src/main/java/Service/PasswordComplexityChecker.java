package Service;

public class PasswordComplexityChecker {


    public static String checkPasswordComplexity(String password) {
        if (password.length() < 8) {
            return "Faible";
        } else if (password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            return "Fort";
        } else {
            return "Moyen";
        }
    }
}
