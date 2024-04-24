package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {


    private static DataBase instance;
    private final String URL = "jdbc:mysql://127.0.0.1:3306/pidev";
    private final String USERNAME = "root";
    private final String PASSWORD = "";


    Connection cnx;



    public DataBase() {

        try {
            cnx = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            System.out.println("Connected ...");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("____not connected____ ");

        }

    }

    public static DataBase getInstance() {
        if (instance == null)
            instance = new DataBase();

        return instance;
    }


    public Connection getCnx() {
        return cnx;
    }

}
