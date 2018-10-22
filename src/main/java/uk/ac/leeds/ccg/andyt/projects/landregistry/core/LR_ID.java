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

/**
 *
 * @author Andy Turner
 */
public class LR_ID implements Serializable, Comparable {

    protected final int ID;

    protected LR_ID(int ID) {
        this.ID = ID;
    }

    /**
     * @return the ID
     */
    public int getID() {
        return ID;
    }

    @Override
    public String toString() {
        return "LR_ID(" + Integer.toString(ID) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
             return false;
        }
        if (o instanceof LR_ID) {
            if (((LR_ID) o).ID == ID) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + this.ID;
        return hash;
    }

    @Override
    public int compareTo(Object o) {
        if (o == null) {
             return -1;
        }
        if (o instanceof LR_ID) {
            LR_ID o2;
            o2 = (LR_ID) o;
            if (this.ID > o2.ID) {
                return 1;
            } else if (this.ID == o2.ID) {
                return 0;
            }
        }
        return -1;
    }

}
