/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.kysymyspankki.dao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        throw new UnsupportedOperationException("find-one metodia ei vielä tueta --> KysymysDao");
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
        
        throw new UnsupportedOperationException("saveOrUpdatea ei tueta vielä --> KysymysDao");
    }
    
    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Poistoa ei tueta vielä --> KysymysDao");
    }
    
    
    
    
}
