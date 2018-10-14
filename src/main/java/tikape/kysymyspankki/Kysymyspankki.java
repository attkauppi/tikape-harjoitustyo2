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
import tikape.kysymyspankki.domain.Vastausvaihtoehto;
import tikape.kysymyspankki.dao.VastausvaihtoehtoDao;

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
        VastausvaihtoehtoDao vastausvaihtoehdot = new VastausvaihtoehtoDao(database);
        
        
        List<Kysymys> kysymyksia = new ArrayList<>();
        kysymyksia = kysymykset.findAll();
        
        for (int i = 0; i < kysymyksia.size(); i++) {
            System.out.println(kysymyksia.get(i).getAiheId() + " ; " + kysymyksia.get(i).getKysymysteksti());
        }
        
        
        System.out.println("hello world");
        
        Spark.get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            
//            List<Kurssi> kurssitLista = kurssit.findAll();
            
            map.put("kysymykset", kysymykset.findAll());
            map.put("aiheet", aiheet.findAll());
            
            
            map.put("kurssit", kurssit.findAll());
            
            return new ModelAndView(map, "kurssit");
        }, new ThymeleafTemplateEngine());
        
        
        Spark.get("/kurssit", (req, res) -> {
            HashMap map = new HashMap<>();
            
//            List<Kurssi> kurssitLista = kurssit.findAll();
            
            map.put("kysymykset", kysymykset.findAll());
            map.put("aiheet", aiheet.findAll());
            
            
            map.put("kurssit", kurssit.findAll());
            
            return new ModelAndView(map, "kurssit");
        }, new ThymeleafTemplateEngine());
        
        
        Spark.post("/kurssit", (req, res) -> {
            // Kurssit, aiheet ja kysymystekstit lisätään tässä 2 vaiheessa.
            // Ensin luodaan esim. uusi kurssiolio (kurssi-niminen muuttuja),
            // jonka avulla luodaan kurssi2-niminen apumuuttuja, kun uusi
            // kurssi tallennetaan tietokantaan. kurssi2-olion id:ksi saadaan
            // oikea id-numero tällä tavalla ja sitä tarvitaan aihe-olion luomiseen.
            // Vastaavasti myös aihe-olion oikeaa id:tä tarvitaan kysymys-olion
            // luomiseen.
            
            
            
            int kursseja = kurssit.findAll().size()+1;
            
            Kurssi kurssi = new Kurssi(-1, req.queryParams("nimi"));
            
            // NÄIN SAAT AINA VIITATTUA OIKEAAN ID NUMERROON 
//            Kurssi kurssi2 = kurssit.saveOrUpdate(kurssi);
            kurssi = kurssit.saveOrUpdate(kurssi);
//            System.out.println(kurssi.getId());
            
            
            
//            System.out.println("kurssi2: " + kurssi2.getNimi() + "; " + kurssi2.getId());
//            
//            System.out.println("kurssi post-metodista: " + kurssi.getId());
//            
            
            try {
                Aihe aihe = new Aihe(-1, req.queryParams("aihe"), kurssi.getId());
                aihe = aiheet.saveOrUpdate(aihe);
                System.out.println("aihe: " + aihe.getId() + " ; aihe.nimi: " + aihe.getNimi() + " ; aihe.kurssiId: " + aihe.getKurssiId());
                Kysymys kysymys = new Kysymys(-1, req.queryParams("kysymysteksti"), aihe.getId());
//                System.out.println(" KYSYMYS:: id: " + kysymys.getId() + " kysymysteksti: " + kysymys.getKysymysteksti() + " ; aiheId: " + kysymys.getAiheId());
                
                kysymykset.saveOrUpdate(kysymys);
            } catch (Exception e) {
                System.out.println("taisi olla null virhe");
            }
            
              
            
            
            
            // Näin saat estettyä tyhjien nimien lisäämisen. Pitäisi varmaan kuitenkin laittaa
            // AiheDao:hon. ==> Lisäsit vastaavan nyt AiheDaoon.
//            if (!aihe.getNimi().isEmpty()) {
//                
//            }
            
            
//            System.out.println("aihe Kysymyspankista: " + aihe.getId() + "; " + aihe.getNimi() + "; " + aihe.getKurssiId());
////            int aiheita = aiheet.findAll().size()+1;
////            Aihe aihe = new Aihe(aiheita, req.queryParams("aihe"), kurssi.getId());
//            Aihe aihe2 = aiheet.saveOrUpdate(aihe);
//
//            
//            Kysymys kysymys = new Kysymys(-1, req.queryParams("kysymysteksti"), aihe2.getId());
            
            
            
            res.redirect("/kurssit");
            return "";
        });
        
        
//        Spark.get("/kysymykset", (req, res) -> {
//            HashMap map = new HashMap<>();
//            
//            map.put("kysymykset", kysymykset.findAll());
//            
//            return new ModelAndView(map, "kysymykset");
//            
//        }, new ThymeleafTemplateEngine());
        
        
        // Kopio kurssit-sivun get-metodista
        Spark.get("/kysymykset", (req, res) -> {
            HashMap map = new HashMap<>();
            
//            List<Kurssi> kurssitLista = kurssit.findAll();
            
            map.put("kysymykset", kysymykset.findAll());
            map.put("aiheet", aiheet.findAll());
            
            
            map.put("kurssit", kurssit.findAll());
            
            return new ModelAndView(map, "kysymykset");
        }, new ThymeleafTemplateEngine());
        
        // kopio kurssit-sivun post-metodista
        Spark.post("/kysymykset", (req, res) -> {
            // Kurssit, aiheet ja kysymystekstit lisätään tässä 2 vaiheessa.
            // Ensin luodaan esim. uusi kurssiolio (kurssi-niminen muuttuja),
            // jonka avulla luodaan kurssi2-niminen apumuuttuja, kun uusi
            // kurssi tallennetaan tietokantaan. kurssi2-olion id:ksi saadaan
            // oikea id-numero tällä tavalla ja sitä tarvitaan aihe-olion luomiseen.
            // Vastaavasti myös aihe-olion oikeaa id:tä tarvitaan kysymys-olion
            // luomiseen.
            
            
            
            int kursseja = kurssit.findAll().size()+1;
            
            Kurssi kurssi = new Kurssi(-1, req.queryParams("nimi"));
            
            // NÄIN SAAT AINA VIITATTUA OIKEAAN ID NUMERROON 
//            Kurssi kurssi2 = kurssit.saveOrUpdate(kurssi);
            kurssi = kurssit.saveOrUpdate(kurssi);
//            System.out.println(kurssi.getId());
            
            
            
//            System.out.println("kurssi2: " + kurssi2.getNimi() + "; " + kurssi2.getId());
//            
//            System.out.println("kurssi post-metodista: " + kurssi.getId());
//            
            
            try {
                Aihe aihe = new Aihe(-1, req.queryParams("aihe"), kurssi.getId());
                aihe = aiheet.saveOrUpdate(aihe);
                System.out.println("aihe: " + aihe.getId() + " ; aihe.nimi: " + aihe.getNimi() + " ; aihe.kurssiId: " + aihe.getKurssiId());
                Kysymys kysymys = new Kysymys(-1, req.queryParams("kysymysteksti"), aihe.getId());
//                System.out.println(" KYSYMYS:: id: " + kysymys.getId() + " kysymysteksti: " + kysymys.getKysymysteksti() + " ; aiheId: " + kysymys.getAiheId());
                
                kysymykset.saveOrUpdate(kysymys);
            } catch (Exception e) {
                System.out.println("taisi olla null virhe");
            }
            
              
            
            
            
            // Näin saat estettyä tyhjien nimien lisäämisen. Pitäisi varmaan kuitenkin laittaa
            // AiheDao:hon. ==> Lisäsit vastaavan nyt AiheDaoon.
//            if (!aihe.getNimi().isEmpty()) {
//                
//            }
            
            
//            System.out.println("aihe Kysymyspankista: " + aihe.getId() + "; " + aihe.getNimi() + "; " + aihe.getKurssiId());
////            int aiheita = aiheet.findAll().size()+1;
////            Aihe aihe = new Aihe(aiheita, req.queryParams("aihe"), kurssi.getId());
//            Aihe aihe2 = aiheet.saveOrUpdate(aihe);
//
//            
//            Kysymys kysymys = new Kysymys(-1, req.queryParams("kysymysteksti"), aihe2.getId());
            
            
            
            res.redirect("/kysymykset");
            return "";
        });
        
        
       Spark.get("/kysymykset/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            
            Integer kysymysId = Integer.parseInt(req.params(":id"));
            map.put("kysymys", kysymykset.findOne(kysymysId));
            
            map.put("vastausvaihtoehdot", vastausvaihtoehdot.etsiKysymyksenVastausvaihtoehdot(kysymysId));
            
            return new ModelAndView(map, "kysymys");
        }, new ThymeleafTemplateEngine());
        
        
//        Spark.get("/kysymys/:id", (req, res) -> {
//            HashMap map = new HashMap<>();
//            
//            Integer kysymysId = Integer.parseInt(req.params(":id"));
//            map.put("kysymys", kysymykset.findOne(kysymysId));
//            
//            return new ModelAndView(map, "kysymys");
//        }, new ThymeleafTemplateEngine());
        
        
        // Metodi, joka poistaa kysymyksiä, jos poista-nappia painetaan.
        Spark.post("/kurssit/:id", (req, res) -> {
            System.out.println("Tuhottavan kysymyksen id: " + req.params(":id"));
            
            kysymykset.delete(Integer.parseInt(req.params(":id")));
            
            res.redirect("/kurssit");
            return "";
        });
        
        
        
    } 
    
}
