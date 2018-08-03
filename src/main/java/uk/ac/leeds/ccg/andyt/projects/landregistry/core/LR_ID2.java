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

    private final LR_ID TitleNumberID;
    private final LR_ID PropertyAddressID;

    public LR_ID2(LR_ID titleNumberID, LR_ID propertyAddressID) {
        TitleNumberID = titleNumberID;
        PropertyAddressID = propertyAddressID;
    }

    /**
     * @return the TitleNumberID
     */
    public LR_ID getTitleNumberID() {
        return TitleNumberID;
    }

    /**
     * @return the AddressID
     */
    public LR_ID getPropertyAddressID() {
        return PropertyAddressID;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LR_ID2) {
            LR_ID2 o2;
            o2 = (LR_ID2) o;
            if (o2.hashCode() == hashCode()) {
                if (o2.TitleNumberID.equals(TitleNumberID)) {
                    if (o2.PropertyAddressID.equals(PropertyAddressID)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.TitleNumberID);
        hash = 47 * hash + Objects.hashCode(this.PropertyAddressID);
        return hash;
    }

}
