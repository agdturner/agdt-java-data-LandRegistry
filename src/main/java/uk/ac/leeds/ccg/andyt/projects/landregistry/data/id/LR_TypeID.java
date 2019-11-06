/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.landregistry.data.id;

import java.util.Objects;
import uk.ac.leeds.ccg.andyt.data.id.Data_ID_int;

/**
 *
 * @author geoagdt
 */
public class LR_TypeID extends Data_ID_int {

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
        return "LR_TypeID(" + super.toString() + ", Type=" + Type + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o != null) {
            if (o instanceof LR_TypeID) {
                if (super.equals(o)) {
                    LR_TypeID o2 = (LR_TypeID) o;
                    if (this.hashCode() == o2.hashCode()) {
                        return this.Type.equalsIgnoreCase(o2.Type);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.Type);
        return hash;
    }

}
