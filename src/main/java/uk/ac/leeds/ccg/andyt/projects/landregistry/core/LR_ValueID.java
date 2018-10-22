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
public class LR_ValueID extends LR_ID implements Serializable {
    
    private final String value;
    
    public LR_ValueID(int ID, String value){
        super(ID);
        this.value = value;
    }
    
    /**
     * 
     * @return type
     */
    public String getValue() {
        return value;
    }
}
