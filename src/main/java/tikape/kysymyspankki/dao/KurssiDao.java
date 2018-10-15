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
import tikape.kysymyspankki.domain.Kurssi;
/**
 *
 * @author ari
 */
public class KurssiDao implements Dao<Kurssi, Integer>{
    
    private Database database;
    
    public KurssiDao(Database database) {
        this.database = database;
    }
    
    @Override
    public Kurssi findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("findOne-metodia ei tueta vielä --> KurssiDao");
    }
    
    @Override
    public List<Kurssi> findAll() throws SQLException {
        List<Kurssi> kurssit = new ArrayList<>();
        
        try (Connection conn = database.getConnection()) {
            ResultSet result = conn.prepareStatement("SELECT id, nimi FROM Kurssi").executeQuery();
            
            while (result.next()) {
                kurssit.add(new Kurssi(result.getInt("id"), result.getString("nimi")));
            }
        }
        
        return kurssit;
    }
    
    public Kurssi saveOrUpdate(Kurssi object) throws SQLException {
        // simply support saving -- disallow saving, if KURSSI with same name
        // already exists
        
        if (object.getNimi().isEmpty()) {
            return null;
        }
        
        Kurssi byName = findByName(object.getNimi());
        
        if (byName != null) {
            return byName;
        }
        
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Kurssi (nimi) VALUES (?)");
            stmt.setString(1, object.getNimi());
            stmt.executeUpdate();
        }
        
        return findByName(object.getNimi());
    }
    
    private Kurssi findByName(String nimi) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, nimi FROM Kurssi WHERE nimi = ?");
            stmt.setString(1, nimi);
            
            ResultSet result = stmt.executeQuery();
            
            if (!result.next()) {
                return null;
            }
            
            return new Kurssi(result.getInt("id"), result.getString("nimi"));
        }
    }
    
    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Poistoa ei tueta vielä --> KurssiDao");
    }
    
//    public List<Kurssi> etsiKurssinAiheet(Integer kurssiId) throws SQLException {
//        String query = "SELECT Aihe.nimi FROM Kurssi, Aihe "
//                + "WHERE Kurssi.id = Aihe.kurssi_id "
//                + "AND Aihe.kurssi_id = ?";
//        
//        List<Kurssi> kurssit = new ArrayList<>();
//        
//        try (Connection conn = database.getConnection()) {
//            PreparedStatement stmt = conn.prepareStatement(query);
//            stmt.setInt(1, kurssiId);
//        }
//    }
    
    
    public Kurssi etsiKysymyksenKurssi (Integer key) throws SQLException{
        
        
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT Kurssi.id, Kurssi.nimi FROM Kurssi, Aihe, Kysymys "
                    + "WHERE Kysymys.aihe_id=Aihe.id "
                    + "AND Aihe.kurssi_id = Kurssi.id "
                    + "AND Kysymys.id=?");
            
            stmt.setInt(1, key);
            ResultSet result = stmt.executeQuery();
            
            Kurssi kurssi = new Kurssi(result.getInt("id"), result.getString("nimi"));
            return kurssi;
        }
        
    }
    
    
    
}
