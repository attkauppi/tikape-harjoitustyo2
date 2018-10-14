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
        
        if (object.getKysymysteksti().isEmpty()) {
            return null;
        }
        
        // jos yritetään luoda aiheeseen kysymys samalla kysymystekstillä, joka on jo
        // ja kurssikin on ihan sama, ei anneta lisätä.
        
        Kysymys byName = findByName(object.getKysymysteksti(), object.getAiheId());
        
//        System.out.println("Mitä findByName palautti byNamen arvoiksi saveOrUpdateen? : " + byName.getId() + " ; k: "+ byName.getKysymysteksti() + "; a: " +byName.getAiheId());
        System.out.println("Palasi findByNamesta");
        if (byName != null) {
            return byName;
        }
        
        System.out.println("palasiko ");
        
        System.out.println("PALASI FINDBYNAMESTA KILTISTI!");
        
        System.out.println("KysymysDao:n saveOrUpdate-metodissa edettiin try-kohtaan asti!");
        
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Kysymys (kysymysteksti, aihe_id) VALUES (?, ?)");
            System.out.println("Yrittää luoda kysymystä saveOrUpdatessa, koska kysymys on uusi!");
            stmt.setString(1, object.getKysymysteksti());
            stmt.setInt(2, object.getAiheId());
            stmt.executeUpdate();
            
        }
        
        return findByName(object.getKysymysteksti(), object.getAiheId());
        
//        throw new UnsupportedOperationException("saveOrUpdatea ei tueta vielä --> KysymysDao");
    }
    
    
        private Kysymys findByName(String kysymysteksti, Integer aiheId) throws SQLException {
        // Tässä siis täytyy tarkistaa, ettei ole jo olemassa samaa kurssia ja aihetta
        
        
        
        System.out.println("findByNamen saama nimi: " + kysymysteksti);
        System.out.println("findByNamen saama kurssiId: " + aiheId);
        
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT Kysymys.id, Kysymys.kysymysteksti, kysymys.aihe_id FROM Kysymys WHERE Kysymys.kysymysteksti = ? AND Kysymys.aihe_id = ?");
            stmt.setString(1, kysymysteksti);
            stmt.setInt(2, aiheId);
            
            ResultSet result = stmt.executeQuery();
            
            
            
            if (!result.next()) {
                return null;
            }
            
            
            
            
            System.out.println("yritti ainakin tehdä uuden KYSYMYKSEN");
            Kysymys kysymys = new Kysymys(result.getInt("id"), result.getString("kysymysteksti"), result.getInt("aihe_id"));
            System.out.println("KysymysDao: " + kysymys.getId() + "; " + kysymys.getKysymysteksti() + " ; " + kysymys.getAiheId());
            //return new Aihe(result.getInt("id"), result.getString("nimi"), result.getInt("kurssi_id"));
            return kysymys;
        }
    }
    
//    private Kysymys findByName(String kysymysteksti, Integer aiheId) throws SQLException {
//        
//        
//        List<Kysymys> kysymyksia = this.findAll();
//        
//        
//        
//        
//        System.out.println("kaikki kysymykset");
//        for (int i = 0; i < kysymyksia.size(); i++) {
//            System.out.println("id: " + kysymyksia.get(i).getId() + " ; kysymysteksti: " + kysymyksia.get(i).getKysymysteksti() + " ; aiheId: " + kysymyksia.get(i).getAiheId());
//        }
//        System.out.println("");
//        
//        System.out.println("KysymysDao:n findByName:n saama kysymysteksti: " + kysymysteksti);
//        System.out.println("KysymysDao:n findByName:n saama aiheId: " + aiheId);
//        
//        try (Connection conn = database.getConnection() ) {
////            System.out.println("try-jutun sisällä nyt kuitenkin ollaan.");
//            
//            
//            PreparedStatement stmt = conn.prepareStatement("SELECT id, kysymysteksti, aihe_id FROM Kysymys WHERE Kysymys.kysymysteksti = ? AND Kysymys.aihe_id = ?");
////            PreparedStatement stmt = conn.prepareStatement("SELECT Kysymys.id, Kysymys.kysymysteksti, Kysymys.aihe_id FROM Kysymys "
////                    + "WHERE Kysymys.kysymysteksti = ? AND Kysymys.aihe_id = ?");
//            stmt.setString(1, kysymysteksti);
//            stmt.setInt(2, aiheId);
//            
//            ResultSet result = stmt.executeQuery();
//            System.out.println("\nRESULT RESULT result: " + result.getInt("id") + "\n");
//            
//            System.out.println("edettiinkö try:ssa ainakin if-kohtaan asti?");
//            if (!result.next()) {
//                System.out.println("KysymysDao:n findByName juuttui if (!result.next()) -kohtaan");
//                return null;
//            }
//            
////            List<Kysymys> kysymyksia2 = new ArrayList<>();
////            while (result.next()) {
////                kysymyksia2.add(new Kysymys(result.getInt("id"), result.getString("kysymysteksti"), result.getInt("aihe_id")));
////            }
////            
////            System.out.println("Jos kerran ei juutu findByNamen if(!result.next)-kohtaan niin mitä helvettiä se löytää sieltä tietokannasta?");
////            System.out.println("eli edellistä iffiä seuraava looppi: ");
////            for (int i = 0; i < kysymyksia2.size(); i++) {
////                System.out.println("id: " + kysymyksia2.get(i).getId() + " ; kysymysteksti: " + kysymyksia2.get(i).getKysymysteksti() + " ; aiheId: " + kysymyksia2.get(i).getAiheId());
////            }
////            System.out.println("");
//            
//            System.out.println("jäätiinkö !result.next iffiin?");
//            
//            
//            Kysymys kysymys = new Kysymys(result.getInt("id"), result.getString("kysymysteksti"), result.getInt("aihe_id"));
//            System.out.println("KysymysDao findByname loi seuraavan kysymysen: " + kysymys.getId() + " ; " + kysymys.getKysymysteksti() + " ; " + kysymys.getAiheId());
//            
//            return kysymys;
//        }
//    }
    
    
    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Poistoa ei tueta vielä --> KysymysDao");
    }
    
    
    
    
}
