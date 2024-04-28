package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {
    private String url = "jdbc:mysql://localhost:3306/test_pi";
    private String login = "root";
    private String pwd = "";
    private static DataBase instance;
    private Connection cnx;

    public DataBase() {
        try {
            cnx = DriverManager.getConnection(url, login, pwd);
            System.out.println("Connection établie!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Connection getCnx() {
        return cnx;
    }

    public static DataBase getInstance(){
        if (instance == null)
            instance = new DataBase();
        return instance;
    }

}
