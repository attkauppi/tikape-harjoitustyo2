/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.kysymyspankki;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import java.io.File;
import spark.Spark;
import tikape.kysymyspankki.database.Database;
import tikape.kysymyspankki.dao.KurssiDao;
import tikape.kysymyspankki.domain.Kurssi;
import tikape.kysymyspankki.dao.AiheDao;
import tikape.kysymyspankki.domain.Kysymys;
import tikape.kysymyspankki.dao.KysymysDao;
import tikape.kysymyspankki.domain.Aihe;

/**
 *
 * @author ari
 */
public class Kysymyspankki {
    
    public static void main(String[] args) throws Exception {
        File tiedosto = new File("db", "Kysymyspankki.db");
        Database database = new Database("jdbc:sqlite:" + tiedosto.getAbsolutePath());
        KurssiDao kurssit = new KurssiDao(database);
        AiheDao aiheet = new AiheDao(database);
        KysymysDao kysymykset = new KysymysDao(database);
        
        
        List<Kysymys> kysymyksia = new ArrayList<>();
        kysymyksia = kysymykset.findAll();
        
        for (int i = 0; i < kysymyksia.size(); i++) {
            System.out.println(kysymyksia.get(i).getAiheId() + " ; " + kysymyksia.get(i).getKysymysteksti());
        }
        
        
        System.out.println("hello world");
        
        Spark.get("/kurssit", (req, res) -> {
            HashMap map = new HashMap<>();
            
//            List<Kurssi> kurssitLista = kurssit.findAll();
            
            map.put("kysymykset", kysymykset.findAll());
            map.put("aiheet", aiheet.findAll());
            
            
            map.put("kurssit", kurssit.findAll());
            
            return new ModelAndView(map, "kurssit");
        }, new ThymeleafTemplateEngine());
        
        
        Spark.post("/kurssit", (req, res) -> {
            Kurssi kurssi = new Kurssi(-1, req.queryParams("nimi"));
            kurssit.saveOrUpdate(kurssi);
            
            res.redirect("/kurssit");
            return "";
        });
        
        
        
    } 
    
}
