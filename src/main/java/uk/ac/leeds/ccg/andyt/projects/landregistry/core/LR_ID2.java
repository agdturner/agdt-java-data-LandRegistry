/*
 * Copyright 2018 geoagdt.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.leeds.ccg.andyt.projects.landregistry.core;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Andy Turner
 */
public class LR_ID2 implements Serializable {

    private final LR_ValueID TitleNumberID;
    private final LR_ValueID PropertyAddressID;

    public LR_ID2(LR_ValueID titleNumberID, LR_ValueID propertyAddressID) {
        TitleNumberID = titleNumberID;
        PropertyAddressID = propertyAddressID;
    }

    @Override
    public String toString() {
        return TitleNumberID.toString() + ", " + PropertyAddressID.toString();
    }

    /**
     * @return the TitleNumberID
     */
    public LR_ValueID getTitleNumberID() {
        return TitleNumberID;
    }

    /**
     * @return the AddressID
     */
    public LR_ValueID getPropertyAddressID() {
        return PropertyAddressID;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LR_ID2) {
            LR_ID2 o2;
            o2 = (LR_ID2) o;
            if (o2.hashCode() == hashCode()) {
                if (o2.TitleNumberID == null && TitleNumberID == null) {
                    return equals(o2);
                }
                if (o2.TitleNumberID.equals(TitleNumberID)) {
                    return equals(o2);
                }
            }
        }
        return false;
    }

    private boolean equals(LR_ID2 o) {
        if (o.PropertyAddressID == null) {
            return PropertyAddressID == null;
        } else {
            if (PropertyAddressID == null) {
                return false;
            } else {
                return o.PropertyAddressID.equals(PropertyAddressID);
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.TitleNumberID);
        hash = 47 * hash + Objects.hashCode(this.PropertyAddressID);
        return hash;
    }

}
