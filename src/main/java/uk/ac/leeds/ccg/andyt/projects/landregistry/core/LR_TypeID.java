/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.landregistry.core;

import java.io.Serializable;
//import java.util.Objects;

/**
 *
 * @author geoagdt
 */
public class LR_TypeID extends LR_ID implements Serializable {

    protected final String Type;

    public LR_TypeID(int ID, String Type) {
        super(ID);
        this.Type = Type;
    }

    /**
     *
     * @return type
     */
    public String getType() {
        return Type;
    }

    @Override
    public String toString() {
        return "LR_TypeID(" + super.toString() + ", Type(" + Type + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LR_TypeID) {
            if (super.equals(o)) {
                LR_TypeID o2 = (LR_TypeID) o;
                if (this.hashCode() == o2.hashCode()) {
                    return this.Type.equalsIgnoreCase(o2.Type);
                }
            }
        }
        return false;
    }

//    @Override
//    public int hashCode() {
//        int hash = 7;
//        hash = 79 * hash + Objects.hashCode(this.Type);
//        return hash;
//    }
//
//    @Override
//    public int compareTo(Object o) {
//        if (o == null) {
//            return -1;
//        }
//        if (o instanceof LR_TypeID) {
//            int s;
//            s = super.compareTo(o);
//            if (s == 0) {
//                int compare = this.Type.compareTo(((LR_TypeID) o).Type);
//                if (compare < 0) {
//                    return -1;
//                } else if (compare > 0) {
//                    return 1;
//                } else {
//                    return 0;
//                }
//            }
//            return s;
//        }
//        return -1;
//    }
}
