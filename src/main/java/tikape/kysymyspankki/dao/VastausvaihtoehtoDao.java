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
import tikape.kysymyspankki.domain.Vastausvaihtoehto;
import tikape.kysymyspankki.database.Database;
/**
 *
 * @author ari
 */
public class VastausvaihtoehtoDao implements Dao<Vastausvaihtoehto, Integer> {
    
    private Database database;
    
    public VastausvaihtoehtoDao(Database database) {
        this.database = database;
    }
    
    @Override
    public Vastausvaihtoehto findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("VastausvaihtoehtoDao: findOne-metodia ei tueta vielä!");
    }
    
    @Override
    public List<Vastausvaihtoehto> findAll() throws SQLException {
        List<Vastausvaihtoehto> vastausvaihtoehdot = new ArrayList<>();
        
        try (Connection conn = database.getConnection()) {
            ResultSet result = conn.prepareStatement("SELECT id, vastausvaihtoehto, oikein_vaarin, kysymys_id FROM Vastausvaihtoehto").executeQuery();
            
            while (result.next() ) {
                vastausvaihtoehdot.add(new Vastausvaihtoehto(result.getInt("id"), result.getString("vastausvaihtoehto"), result.getBoolean("oikein_vaarin"), result.getInt("kysymys_id")));
                
            }
        }
        Vastausvaihtoehto vastaus = new Vastausvaihtoehto(1, "joo", Boolean.FALSE, 2);
        return vastausvaihtoehdot;
    }
    
    @Override
    public Vastausvaihtoehto saveOrUpdate(Vastausvaihtoehto object) throws SQLException {
        
        // Jos vastausvaihtoehdon "nimeä" vastaava kenttä on tyhjä, ei kelpuuteta vastaus-
        // vaihtoehtoa, joka yritettiin lisätä.
        
        if (object.getVastausvaihtoehto().isEmpty()) {
            return null;
        }
        
        // Jos vastausvaihtoehdolla on sama nimi ja kysymys_id, katsotaan se samaksi. Jos
        // taas oikein_vaarin kentässä on eri arvot, tämä voi olla tarkoituksenmukaista. 
        
        // # ==> Mutta tämä lähestymistapa ei kyllä salli eri arvoja oikein_vaarin kenttiinkään
        // vai salliiko?
        Vastausvaihtoehto byName = findByName(object.getVastausvaihtoehto(), object.getOikeinVaarin(), object.getKysymysId());
        
        if (byName != null) {
            return byName;
        }
        
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Vastausvaihtoehto (vastausvaihtoehto, oikein_vaarin, kysymys_id) VALUES (?, ?, ?)");
            
            stmt.setString(1, object.getVastausvaihtoehto());
            stmt.setBoolean(2, object.getOikeinVaarin());
            stmt.setInt(3, object.getKysymysId());
        }
        
        return findByName(object.getVastausvaihtoehto(), object.getOikeinVaarin(), object.getKysymysId());
    }
    
    private Vastausvaihtoehto findByName(String vastausvaihtoehto, Boolean oikeinVaarin, Integer kysymysId) throws SQLException {
        // Tässä siis täytyy tarkistaa, ettei ole jo olemassa samaa kurssia ja aihetta
        
        
        
        System.out.println("Vastausvaihtoehto findByName:n saamat tiedot: v" + vastausvaihtoehto + " ; o/v: " + oikeinVaarin + " ; kId: " + kysymysId);
        
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, vastausvaihtoehto, oikein_vaarin, kysymys_id FROM Vastausvaihtoehto "
                    + "WHERE Vastausvaihtoehto.vastausvaihtoehto = ? "
                    + "AND Vastausvaihtoehto.oikein_vaarin = ? "
                    + "AND Vastausvaihtoehto.kysymys_id = ?");
            stmt.setString(1, vastausvaihtoehto);
            stmt.setBoolean(2, oikeinVaarin);
            stmt.setInt(3, kysymysId);
            
            ResultSet result = stmt.executeQuery();
            
            
            
            if (!result.next()) {
                return null;
            }
            
            
            Vastausvaihtoehto vastaus = new Vastausvaihtoehto(result.getInt("id"), result.getString("vastausvaihtoehto"), result.getBoolean("oikein_vaarin"), result.getInt("kysymys_id"));
            
            System.out.println("yritti ainakin tehdä uuden aiheen");
//            Vastausvaihtoehto vastausvaihtoehto = new Vastausvaihtoehto(result.getInt("id"), result.getString("vastausvaihtoehto"), result.getBoolean("oikein_vaarin"), result.getInt("kysymys_id"));
            System.out.println("VastausvaihtoehtoDao loi seuraavan : " + vastaus.getId() + "; " + vastaus.getVastausvaihtoehto() + " ; " + vastaus.getOikeinVaarin() + " ; " + vastaus.getKysymysId());
            //return new Aihe(result.getInt("id"), result.getString("nimi"), result.getInt("kurssi_id"));
            return vastaus;
        }
    }
    
    @Override
    public void delete(Integer key) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Vastausvaihtoehto WHERE Vastausvaihtoehto.id = ?");
            stmt.setInt(1, key);
            stmt.executeUpdate();
        }
    }
    
    public List<Vastausvaihtoehto> etsiKysymyksenVastausvaihtoehdot(Integer key) throws SQLException {
        
        List<Vastausvaihtoehto> tietytVastausvaihtoehdot= new ArrayList<>();
        
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT Vastausvaihtoehto.id, Vastausvaihtoehto.vastausvaihtoehto, Vastausvaihtoehto.oikein_vaarin, Vastausvaihtoehto.kysymys_id "
                    + "FROM Kysymys, Vastausvaihtoehto " +
                    "WHERE Kysymys.id=Vastausvaihtoehto.kysymys_id " +
                    "AND Kysymys.id=?");
            
            stmt.setInt(1, key);
            
            ResultSet result = stmt.executeQuery();
            
            while (result.next()) {
                tietytVastausvaihtoehdot.add(new Vastausvaihtoehto(result.getInt("id"), result.getString("vastausvaihtoehto"), result.getBoolean("oikein_vaarin"), result.getInt("kysymys_id")));
                
            }
        }
        
        return tietytVastausvaihtoehdot;
        
    }
    
    
}
