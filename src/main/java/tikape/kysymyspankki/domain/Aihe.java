/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.kysymyspankki.domain;

/**
 *
 * @author ari
 */
public class Aihe {
    
    private Integer id;
    private String nimi;
    private Integer kurssiId;
    
    public Aihe() {    
    
    }
    
    public Aihe(Integer id, String nimi, Integer kurssiId) {
        this.id = id;
        this.nimi = nimi;
        this.kurssiId = kurssiId;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public String getNimi() {
        return this.nimi;
    }
    
    public Integer getKurssiId() {
        return this.kurssiId;
    }
    
    
}
