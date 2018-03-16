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
package uk.ac.leeds.ccg.andyt.projects.landregistry.data.landregistry;

import java.io.Serializable;

/**
 *
 * @author geoagdt
 */
public class LR_CC_COU_Record extends LR_CC_FULL_Record implements Serializable {

    private final String ChangeIndicator;
    private final String ChangeDate;

    public LR_CC_COU_Record(long ID, String line) {
        super(ID, line);
        String[] ls;
        ls = line.split("\",\"");
        int lineLength;
        lineLength = ls.length;
        ChangeIndicator = ls[lineLength - 2];
        ChangeDate = ls[lineLength - 1];
    }

    @Override
    public String toString() {
        return super.toString() + "ChangeIndicator " + getChangeIndicator()
                + ",ChangeDate " + getChangeDate();
    }

    @Override
    public String toCSV() {
        return super.toCSV() + "," + getChangeIndicator()
                + "," + getChangeDate();
    }

    public static String header() {
        return LR_CC_FULL_Record.header() + "ChangeIndicator,ChangeDate";
    }

    /**
     * @return the ChangeIndicator
     */
    public String getChangeIndicator() {
        return ChangeIndicator;
    }

    /**
     * @return the ChangeDate
     */
    public String getChangeDate() {
        return ChangeDate;
    }

}
