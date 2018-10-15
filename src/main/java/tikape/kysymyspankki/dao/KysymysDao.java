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
import tikape.kysymyspankki.domain.Kysymys;
/**
 *
 * @author ari
 */
public class KysymysDao implements Dao<Kysymys, Integer>{
    
    
    
    private Database database;
    
    public KysymysDao(Database database) {
        this.database = database;
    }
    
    @Override
    public Kysymys findOne(Integer key) throws SQLException {
        
        // Jollei muu onnistu, käytä tätä esimerkkiä:
//        return findAll().stream().filter(u -> u.getId().equals(key)).findFirst().get();

        try (Connection conn = database.getConnection()) {
            
            PreparedStatement stmt = conn.prepareStatement("SELECT Kysymys.id, Kysymys.kysymysteksti, Kysymys.aihe_id FROM Kysymys WHERE Kysymys.id = ?");
            stmt.setInt(1, key);
            ResultSet result = stmt.executeQuery();
            
            if (!result.next()) {
                return null;
            }
            Kysymys kysymys = new Kysymys(result.getInt("id"), result.getString("kysymysteksti"), result.getInt("aihe_id"));
            return kysymys;
        }
        
//        throw new UnsupportedOperationException("find-one metodia ei vielä tueta --> KysymysDao");
    }
    
    @Override
    public List<Kysymys> findAll() throws SQLException {
        List<Kysymys> kysymykset = new ArrayList<>();
        
        try (Connection conn = database.getConnection()) {
            
            
            ResultSet result = conn.prepareStatement("SELECT id, kysymysteksti, aihe_id FROM Kysymys").executeQuery();
            
            while (result.next()) {
                kysymykset.add(new Kysymys(result.getInt("id"), result.getString("kysymysteksti"), result.getInt("aihe_id")));
            }
        }
        
        return kysymykset;
    }
    
    @Override
    public Kysymys saveOrUpdate(Kysymys object) throws SQLException {
        
        if (object.getKysymysteksti().isEmpty()) {
            return null;
        }
        
        // jos yritetään luoda aiheeseen kysymys samalla kysymystekstillä, joka on jo,
        // ja kurssikin on ihan sama, ei anneta lisätä.
        
        Kysymys byName = findByName(object.getKysymysteksti(), object.getAiheId());

        if (byName != null) {
            return byName;
        }

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Kysymys (kysymysteksti, aihe_id) VALUES (?, ?)");
            stmt.setString(1, object.getKysymysteksti());
            stmt.setInt(2, object.getAiheId());
            stmt.executeUpdate();
            
        }
        
        return findByName(object.getKysymysteksti(), object.getAiheId());
        
//        throw new UnsupportedOperationException("saveOrUpdatea ei tueta vielä --> KysymysDao");
    }
    
    
        private Kysymys findByName(String kysymysteksti, Integer aiheId) throws SQLException {
        // Tässä siis täytyy tarkistaa, ettei ole jo olemassa samaa kurssia ja aihetta

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT Kysymys.id, Kysymys.kysymysteksti, kysymys.aihe_id FROM Kysymys WHERE Kysymys.kysymysteksti = ? AND Kysymys.aihe_id = ?");
            stmt.setString(1, kysymysteksti);
            stmt.setInt(2, aiheId);
            
            ResultSet result = stmt.executeQuery();

            if (!result.next()) {
                return null;
            }

            Kysymys kysymys = new Kysymys(result.getInt("id"), result.getString("kysymysteksti"), result.getInt("aihe_id"));
            
            return kysymys;
        }
    }
    
    
    
    @Override
    public void delete(Integer key) throws SQLException {
        
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Kysymys WHERE Kysymys.id=?");
            stmt.setInt(1, key);
            stmt.executeUpdate();
        }
        
    }
    
    
    
    
    
    

    
    
}
