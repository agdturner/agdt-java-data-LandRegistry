/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.landregistry.core;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author geoagdt
 */
public class LR_ValueID extends LR_ID implements Serializable {
    
    private final String Value;
    
    public LR_ValueID(int ID, String Value){
        super(ID);
        this.Value = Value;
    }
    
    /**
     * 
     * @return type
     */
    public String getValue() {
        return Value;
    }
    
    @Override
    public String toString() {
        return "LR_ValueID(" + super.toString() + ", Type(" + Value + ")";
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof LR_TypeID) {
            if (super.equals(o)) {
                LR_ValueID o2 = (LR_ValueID) o;
                if (this.hashCode() == o2.hashCode()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.Value);
        return hash;
    }

    @Override
    public int compareTo(Object o) {
        if (o == null) {
            return -1;
        }
        if (o instanceof LR_ValueID) {
            int s;
            s = super.compareTo(o);
            if (s == 0) {
                int compare = this.Value.compareTo(((LR_ValueID) o).Value);
                if (compare < 0) {
                    return -1;
                } else if (compare > 0) {
                    return 1;
                } else {
                    return 0;
                }
            }
            return s;
        }
        return -1;
    }
}
