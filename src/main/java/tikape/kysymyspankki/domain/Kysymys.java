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
public class Kysymys {
    
    private Integer id;
    private String kysymysteksti;
    private Integer aiheId;
    
    
    public Kysymys()  {
        
    }
    
    public Kysymys(Integer id, String kysymysteksti, Integer aiheId) {
        this.id = id;
        this.kysymysteksti = kysymysteksti;
        this.aiheId = aiheId;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public String getKysymysteksti() {
        return this.kysymysteksti;
    }
    
    public Integer getAiheId() {
        return this.aiheId;
    }
    
}
