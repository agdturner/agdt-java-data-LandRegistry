/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.landregistry.core;

import java.io.Serializable;

/**
 *
 * @author geoagdt
 */
public class LR_TypeID extends LR_ID implements Serializable {
    
    private final String type;
    
    public LR_TypeID(int ID, String type){
        super(ID);
        this.type = type;
    }
    
    /**
     * 
     * @return type
     */
    public String getType() {
        return type;
    }
}
