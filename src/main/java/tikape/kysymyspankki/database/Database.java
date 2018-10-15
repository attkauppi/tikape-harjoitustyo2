/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.kysymyspankki.database;

/**
 *
 * @author ari
 */

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    
    private String databaseAddress;
    
    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }
    
//    public Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(databaseAddress);
//    }
    
    
    public Connection getConnection() throws SQLException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        if (dbUrl != null && dbUrl.length() > 0) {
            System.out.println("käyttää mukamas herokua: ");
            System.out.println("dbUrl: " + dbUrl);
            return DriverManager.getConnection(dbUrl);
        }
        File tiedosto = new File("db", "Kysymyspankki.db");
////        Database database = new Database("jdbc:sqlite:" + tiedosto.getAbsolutePath());
//
        System.out.println("Nyt ei jostain syystä käytäkään herokun tietokantaa!");
        return DriverManager.getConnection("jdbc:sqlite:"+tiedosto.getAbsolutePath());
//        return DriverManager.getConnection(databaseAddress);
    }
    
    
}
