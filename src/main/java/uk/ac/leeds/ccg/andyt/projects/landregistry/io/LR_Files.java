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
package uk.ac.leeds.ccg.andyt.projects.landregistry.io;

import java.io.File;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_Files;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Strings;

/**
 *
 * @author geoagdt
 */
public class LR_Files extends Generic_Files {

    protected LR_Strings Strings;

    protected LR_Files(){}
    
//    public LR_Files(LR_Strings Strings) {
//        this.Strings = Strings;
//    }

    /**
     *
     * @param Strings
     * @param dataDirName
     */
    public LR_Files(LR_Strings Strings, String dataDirName) {
        super(dataDirName);
        this.Strings = Strings;
    }

    public File getInputDataDir() {
        if (InputDataDir == null) {
            InputDataDir = new File(getDataDir(), Strings.getS_input());
        }
        return InputDataDir;
    }
    
    public File getInputDataDir(String s) {
        return new File(getInputDataDir(), s);
    }
    
    public File getTIDataFile() {
        File result;
        File dir;
        dir = new File(getInputDataDir(Strings), "TransparencyInternational");
        result = new File(dir, "Selection.csv");
        return result;
    }
    
    public File getGeneratedDataFile(String name, String type) {
        File dir;
        dir = getGeneratedDataDir(Strings);
        File f;
        f = new File(dir, name + "_" + type + "." + Strings.S_dat);
        return f;
    }
}
