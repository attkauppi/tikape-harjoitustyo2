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
public class Vastausvaihtoehto {
    
    private Integer id;
    private String vastausvaihtoehto;
    private Boolean oikeinVaarin;
    private Integer kysymysId;
    
    public Vastausvaihtoehto() {
    }
    
    public Vastausvaihtoehto(Integer id, String vastausvaihtoehto, Boolean oikein_vaarin, Integer kysymysId) {
        this.id = id;
        this.vastausvaihtoehto = vastausvaihtoehto;
        this.oikeinVaarin = oikein_vaarin;
        this.kysymysId = kysymysId;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getVastausvaihtoehto() {
        return this.vastausvaihtoehto;
    }
    
    public Boolean getOikeinVaarin() {
        return this.oikeinVaarin;
    }
    
    public Integer getKysymysId() {
        return this.kysymysId;
    }
    
}
