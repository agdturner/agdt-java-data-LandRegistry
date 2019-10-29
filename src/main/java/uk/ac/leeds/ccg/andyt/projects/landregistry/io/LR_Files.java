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
import java.io.IOException;
import uk.ac.leeds.ccg.andyt.data.io.Data_Files;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Strings;

/**
 *
 * @author geoagdt
 */
public class LR_Files extends Data_Files {

    /**
     * @param dir
     */
    public LR_Files(File dir) throws IOException {
        super(dir);
    }
    
    public File getInputDataDir(String s) {
        return new File(getInputDir(), s);
    }
    
    public File getTIDataFile() {
        File r = new File(getInputDir(), "TransparencyInternational");
        r = new File(r, "Selection.csv");
        return r;
    }
    
    public File getGeneratedDataFile(String name, String type) {
        return new File(getGeneratedDir(), 
                name + LR_Strings.symbol_underscore + type + DOT_DAT);
    }
}
