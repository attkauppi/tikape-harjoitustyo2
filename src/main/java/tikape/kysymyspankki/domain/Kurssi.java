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
public class Kurssi {
    
    private Integer id;
    private String nimi;
    
    public Kurssi() {
        
    }
    
    public Kurssi(Integer id, String nimi) {
        this.id = id;
        this.nimi = nimi;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public String getNimi() {
        return this.nimi;
    }
    
}
