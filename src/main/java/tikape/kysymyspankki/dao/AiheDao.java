/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.kysymyspankki.dao;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import tikape.kysymyspankki.database.Database;
import tikape.kysymyspankki.domain.Aihe;
import tikape.kysymyspankki.dao.KurssiDao;
import tikape.kysymyspankki.domain.Kurssi;
/**
 *
 * @author ari
 */
public class AiheDao implements Dao<Aihe, Integer>{
 
    private Database database;
    
    public AiheDao(Database database) {
        this.database = database;
        
    }
    
    @Override
    public Aihe findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("findOne-metodia ei tueta vielä --> KurssiDao");
    }
    
    @Override
    public List<Aihe> findAll() throws SQLException {
        List<Aihe> aiheet = new ArrayList<>();
        
        try (Connection conn = database.getConnection()) {
            ResultSet result = conn.prepareStatement("SELECT id, nimi, kurssi_id FROM Aihe").executeQuery();
            
            while (result.next()) {
                aiheet.add(new Aihe(result.getInt("id"), result.getString("nimi"), result.getInt("kurssi_id")));
            }
        }
        
        return aiheet;
    }
    
    public Aihe saveOrUpdate(Aihe object) throws SQLException {
        // simply support saving -- disallow saving, if AIHE with same name
        // already exists
        
        if (object.getNimi().isEmpty() || object.getKurssiId() == null) {
            return null;
        }
        
        Aihe byName = findByName(object.getNimi(), object.getKurssiId());
        
        
        if (byName != null) {
            return byName;
        }
        
        try (Connection conn = database.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Aihe (nimi, kurssi_id) VALUES (?, ?)");

            stmt.setString(1, object.getNimi());
            stmt.setInt(2, object.getKurssiId());
            stmt.executeUpdate();
        }
        
        return findByName(object.getNimi(), object.getKurssiId());
    }
    
    
    
    private Aihe findByName(String nimi, Integer kurssiId) throws SQLException {
        // Tässä siis täytyy tarkistaa, ettei ole jo olemassa samaa kurssia ja aihetta

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT Aihe.id, Aihe.nimi, Aihe.kurssi_id FROM Aihe WHERE Aihe.nimi = ? AND Aihe.kurssi_id = ?");
            stmt.setString(1, nimi);
            stmt.setInt(2, kurssiId);
            
            ResultSet result = stmt.executeQuery();
   
            if (!result.next()) {
                return null;
            }

            Aihe aihe = new Aihe(result.getInt("id"), result.getString("nimi"), result.getInt("kurssi_id"));
            return aihe;
        }
    }
    
    @Override
    public void delete(Integer key) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Aihe WHERE Aihe.id=?");
            stmt.setInt(1, key);
            stmt.executeUpdate();
        }
        
    }
    
    public Aihe etsiKysymyksenAihe (Integer key) throws SQLException {

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT Aihe.id, Aihe.nimi, Aihe.kurssi_id FROM Kysymys, Aihe "
                    + "WHERE Kysymys.aihe_id = Aihe.id "
                    + "AND Kysymys.id=?");
            stmt.setInt(1, key);
            
            ResultSet result = stmt.executeQuery();
            
            if (!result.next()) {
                return null;
            }
            
            Aihe aihe = new Aihe(result.getInt("id"), result.getString("nimi"), result.getInt("kurssi_id"));
            return aihe;
        }
    }
    
    
    // Tarkistaa, onko tiettyyn Aiheeseen liittyen yhtään kysymystä Kysymys-taulussa.
    public Boolean onkoAiheellaKysymyksia(Integer key) throws SQLException {
        try (Connection conn = database.getConnection() ) {
            PreparedStatement stmt = conn.prepareStatement("SELECT Kysymys.aihe_id FROM Kysymys WHERE Kysymys.aihe_id=?");
            stmt.setInt(1, key);
            ResultSet result = stmt.executeQuery();
            
            if (!result.next()) {
                return Boolean.FALSE;
            }
            
            return Boolean.TRUE;
        }
    }
    
    
}
