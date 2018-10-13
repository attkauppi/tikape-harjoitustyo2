/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.kysymyspankki.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.kysymyspankki.database.Database;
import tikape.kysymyspankki.domain.Aihe;
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
        
        Aihe byName = findByName(object.getNimi());
        
        if (byName != null) {
            return byName;
        }
        
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Aihe (nimi) VALUES (?)");
            stmt.setString(1, object.getNimi());
            stmt.executeUpdate();
        }
        
        return findByName(object.getNimi());
    }
    
    private Aihe findByName(String nimi) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, nimi, kurssi_id FROM Aihe WHERE nimi = ?");
            stmt.setString(1, nimi);
            
            ResultSet result = stmt.executeQuery();
            
            if (!result.next()) {
                return null;
            }
            
            return new Aihe(result.getInt("id"), result.getString("nimi"), result.getInt("kurssi_id"));
        }
    }
    
    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Poistoa ei tueta vielä --> AiheDao");
    }
    
    
    
}
