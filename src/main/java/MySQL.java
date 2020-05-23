import java.sql.*;

/**
 * Created by Meyssam on 10.07.2016.
 */
public class MySQL {

    public static String username = "bot";
    public static String password = "discord";
    public static String database = "discord";
    public static String host = "localhost";
    public static int port = 3306;
    public static Connection con;

    public static void connect() {
        System.out.println("MySQL-Verbindung wird aufgebaut!");
        if(!isConnected()) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
                System.out.println("MySQL-Verbindung aufgebaut!");
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("Es ist ein Fehler beim Aufbauen der MySQL-Verbindung aufgetreten!");
            }
        }
    }

    public static void close() {
        if(isConnected()) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Es ist ein Fehler beim Schließen der MySQL-Verbindung aufgetreten!");
            }
            System.out.println("MySQL-Verbindung geschlossen!");
        }
    }

    public static boolean isConnected() {
        return con != null;
    }

    public static void createTable() {

        /**
         * Syntax: Spielername, UUID, Kills, Deaths, Spiele, Coins
         */

        if(isConnected()) {
            try {
              //  con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS Stats (Spielername VARCHAR(100), UUID VARCHAR(100), Kills INT(100), Deaths INT(100), Spiele INT(100), Coins INT(100))");
             //   con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS Kits (Spielername VARCHAR(100), UUID VARCHAR(100), Tank BOOLEAN, Crafter BOOLEAN, Bergarbeiter BOOLEAN)");
                con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS serverlist (ID VARCHAR(100), CMD VARCHAR(5))");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void update(String qry) {
        if(isConnected()) {
            try {
                con.createStatement().executeUpdate(qry);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ResultSet getResult(String qry) {
        if(isConnected()) {
            try {
                return con.createStatement().executeQuery(qry);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
