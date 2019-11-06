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
public class LR_ValueID extends Data_ID_int {

    private final String Value;

    public LR_ValueID(int ID, String Value) {
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
        return "LR_ValueID(" + super.toString() + ", Value=" + Value + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o != null) {
            if (o instanceof LR_TypeID) {
                if (super.equals(o)) {
                    LR_ValueID o2 = (LR_ValueID) o;
                    if (this.hashCode() == o2.hashCode()) {
                        return this.Value.equalsIgnoreCase(o2.Value);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.Value);
        return hash;
    }
}
