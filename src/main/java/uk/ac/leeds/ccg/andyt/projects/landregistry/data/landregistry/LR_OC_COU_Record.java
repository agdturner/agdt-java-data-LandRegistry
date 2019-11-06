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

import uk.ac.leeds.ccg.andyt.generic.time.Generic_YearMonth;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Environment;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.id.LR_RecordID;

/**
 *
 * @author geoagdt
 */
public class LR_OC_COU_Record extends LR_OC_FULL_Record {

    private String ChangeIndicator;
    private String ChangeDate;

    protected LR_OC_COU_Record(LR_Environment e, LR_RecordID i) {
        super(e, i);
    }

    public LR_OC_COU_Record(LR_Environment env, LR_RecordID i,
            Generic_YearMonth YM, String line, boolean doUpdate) 
            throws Exception {
        super(env, i, YM, line, doUpdate);
        init(line);
    }

    private void init(String line) {
        String[] ls;
        ls = line.split("\",\"");
        int lineLength;
        lineLength = ls.length;
        ChangeIndicator = ls[lineLength - 2];
        ChangeDate = ls[lineLength - 1];
    }

    @Override
    public String toString() {
        return super.toString() + ",ChangeIndicator " + getChangeIndicator()
                + ",ChangeDate " + getChangeDate();
    }

    @Override
    public String toCSV() {
        return super.toCSV() + "\",\"" + getChangeIndicator()
                + "\",\"" + getChangeDate() + "\"";
    }

    public static String header() {
        return LR_OC_FULL_Record.header() + ",ChangeIndicator,ChangeDate";
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
