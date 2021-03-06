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
import java.sql.Connection;
import java.sql.DriverManager;
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
        
        // Herokua varten tarvittava taika:
        // asetetaan portti jos heroku antaa PORT-ympäristömuuttujan
        if (System.getenv("PORT") != null) {
            System.out.println("Herokun portti löytyi!");
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }
        
        
//        for (int i = 0; i < kysymyksia.size(); i++) {
//            System.out.println(kysymyksia.get(i).getAiheId() + " ; " + kysymyksia.get(i).getKysymysteksti());
//        }
        
        
        System.out.println("hello world");
        
        
        Spark.get("/kysymykset", (req, res) -> {
            HashMap map = new HashMap<>();
            
            
            map.put("kysymykset", kysymykset.findAll());
            map.put("aiheet", aiheet.findAll());
            
            
            map.put("kurssit", kurssit.findAll());
            
            return new ModelAndView(map, "kysymykset");
        }, new ThymeleafTemplateEngine());
        
        Spark.get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            
            
            map.put("kysymykset", kysymykset.findAll());
            map.put("aiheet", aiheet.findAll());
            
            
            map.put("kurssit", kurssit.findAll());
            
            return new ModelAndView(map, "kysymykset");
        }, new ThymeleafTemplateEngine());
        
        Spark.post("/kysymykset", (req, res) -> {
            // Kurssit, aiheet ja kysymystekstit lisätään tässä 2 vaiheessa.
            // Ensin luodaan esim. uusi kurssiolio (kurssi-niminen muuttuja),
            // jonka avulla luodaan kurssi2-niminen apumuuttuja, kun uusi
            // kurssi tallennetaan tietokantaan. kurssi2-olion id:ksi saadaan
            // oikea id-numero tällä tavalla ja sitä tarvitaan aihe-olion luomiseen.
            // Vastaavasti myös aihe-olion oikeaa id:tä tarvitaan kysymys-olion
            // luomiseen.
            
            if (req.queryParams("nimi").isEmpty()||req.queryParams("aihe").isEmpty()||req.queryParams("kysymysteksti").isEmpty()) {
                res.redirect("/");
                return "";
            }
            
   
            
            int kursseja = kurssit.findAll().size()+1;
            
            Kurssi kurssi = new Kurssi(-1, req.queryParams("nimi"));
            
            
            
            // NÄIN SAAT AINA VIITATTUA OIKEAAN ID NUMERROON 
//            Kurssi kurssi2 = kurssit.saveOrUpdate(kurssi);

            try {
                
                kurssi = kurssit.saveOrUpdate(kurssi);
                Aihe aihe = new Aihe(-1, req.queryParams("aihe"), kurssi.getId());
                aihe = aiheet.saveOrUpdate(aihe);
            
                Kysymys kysymys = new Kysymys(-1, req.queryParams("kysymysteksti"), aihe.getId());
                kysymykset.saveOrUpdate(kysymys);
  
            } catch (Exception e) {
                System.out.println("taisi olla null virhe");
            }
            

            res.redirect("/");
            return "";
        });
        
        // Luo yksittäisen kysymyksen vastausvaihtoehdot näyttävän sivun.
       Spark.get("/kysymykset/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            
            
            
            Integer kysymysId = Integer.parseInt(req.params(":id"));
            
            map.put("kurssi", kurssit.etsiKysymyksenKurssi(kysymysId).getNimi());
            map.put("aihe", aiheet.etsiKysymyksenAihe(kysymysId).getNimi());
            map.put("kysymys", kysymykset.findOne(kysymysId));
            map.put("vastausvaihtoehdot", vastausvaihtoehdot.etsiKysymyksenVastausvaihtoehdot(kysymysId));
            
            return new ModelAndView(map, "kysymys");
        }, new ThymeleafTemplateEngine());
        

        
        //  metodi, joka poistaa kysymyksiä, jos poista nappia painetaan.
        Spark.post("/kysymykset/:id", (req, res) -> {
            System.out.println("Tuhottavan kysymyksen id: " + req.params(":id"));
            
            Aihe aihe = aiheet.etsiKysymyksenAihe(Integer.parseInt(req.params(":id")));


            kysymykset.delete(Integer.parseInt(req.params(":id")));
            
            
            Boolean onkoAihePoistettava = aiheet.onkoAiheellaKysymyksia(aihe.getId());
            
            if (onkoAihePoistettava==Boolean.FALSE) {
                aiheet.delete(aihe.getId());
                
                // Jääkö kurssille mitään muita aiheita?
                if (kurssit.onkoKurssillaAiheita(aihe.getKurssiId()) == Boolean.FALSE) {
                    kurssit.delete(aihe.getKurssiId());
                }
            }
            
            
            
            res.redirect("/");
            return "";
        });
        
        // Yksittäisiin kysymyksiin liittyvä juttu
        Spark.post("/create/kysymykset/:id", (req, res) -> {
            
            Integer kysymysId = Integer.parseInt(req.params("id"));
            String redirect = "/kysymykset/"+kysymysId;
            
            
            // Tieto /kysymykset/id:n lomakkeesta tullut, uuden vastaustekstin
            // oikeutta tai vääryyttä koskeva tieto saadaan ikävästi String-muodossa.
            // Jos checkboxia on painettu, saadaan arvo "on" ja jos ei ole painettu
            // saadaan arvo null.
            String oikein = req.queryParams("oikein");
            Boolean bOikein = Boolean.FALSE;
            
            if (oikein != null) {
                bOikein = Boolean.TRUE;
            }

            String vastausteksti = req.queryParams("vastausteksti");

            
            
            Vastausvaihtoehto vastaus = new Vastausvaihtoehto(-1, vastausteksti, bOikein, kysymysId);
            vastausvaihtoehdot.saveOrUpdate(vastaus);
            
            res.redirect(redirect);
            return "";
        });
        
        
        Spark.post("/delete/kysymykset/:id/:id2", (req, res) -> {
            
            System.out.println("req.tietoja: " + req.pathInfo());
            
            HashMap map = new HashMap<>();
            
            Integer kysymysId = Integer.parseInt(req.params("id"));
            Integer vastausId = Integer.parseInt(req.params(":id2"));
            
            vastausvaihtoehdot.delete(vastausId);
            
            // apumuuttuja, jolla muodostetaan osoite, johon käyttäjä ohjataan
            // lopuksi.
            String redirect = "/kysymykset/"+kysymysId;
            
            
            res.redirect(redirect);
            return "";
            
        });
        
        
    } 
    
}
