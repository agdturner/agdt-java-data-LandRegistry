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
package uk.ac.leeds.ccg.andyt.projects.landregistry.data.transparencyinternational;

import uk.ac.leeds.ccg.andyt.projects.landregistry.data.landregistry.*;
import java.io.Serializable;

/**
 *
 * @author geoagdt
 */
public class LR_TI_Record implements Serializable {

    private final String Country;
    private final String ISO3;
    private final int CPIScore2017;

    public LR_TI_Record(String line) {
        String[] ls;
        ls = line.split("\"");
        if (ls.length > 1) {
            Country = ls[1];
            ls = ls[2].split(",");
            ISO3 = ls[1];
            CPIScore2017 = new Integer(ls[2]);
        } else {
            ls = line.split(",");
            Country = ls[0];
            ISO3 = ls[1];
            CPIScore2017 = new Integer(ls[2]);
        }
    }

    @Override
    public String toString() {
        return "Country " + getCountry()
                + ",ISO3 " + getISO3()
                + ",CPIScore2017 " + getCPIScore2017();
    }

    public String getCountry() {
        return Country;
    }

    public String getISO3() {
        return ISO3;
    }

    public int getCPIScore2017() {
        return CPIScore2017;
    }

}
