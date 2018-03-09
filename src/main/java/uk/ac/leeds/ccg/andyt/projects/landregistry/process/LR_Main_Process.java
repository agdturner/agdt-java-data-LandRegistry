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
package uk.ac.leeds.ccg.andyt.projects.landregistry.process;

import java.io.File;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Strings;
import uk.ac.leeds.ccg.andyt.projects.landregistry.io.LR_Files;

/**
 *
 * @author geoagdt
 */
public class LR_Main_Process {
    
    LR_Strings Strings;
    LR_Files Files;
    
    public LR_Main_Process(){
        Files = new LR_Files();
        Strings = new LR_Strings();
    }
    
    public static void main(String[] args) {
        LR_Main_Process p;
        p = new LR_Main_Process();
        p.run();
    }
    
    public void run() {
        String area;
        area = "LEEDS";
//        // Select Leeds
//        LR_Select_Process sp;
//        sp = new LR_Select_Process();
//        sp.Files.setDataDirectory(new File(System.getProperty("user.dir"), "data"));
//        sp.run(area);
        
        // Generalise Leeds
                // min is the minimum count for a generlisation reported
        int min;
        // inputDataDir provides the location of the input data
        File inputDataDir;
        //inputDataDir = Files.getOutputDataDir(Strings);
        inputDataDir = Files.getInputDataDir(Strings);

        
        LR_Generalise_Process gp;
        gp = new LR_Generalise_Process();
        gp.Files.setDataDirectory(new File(System.getProperty("user.dir"), "data"));
        min = 5;
        gp.run(area, min, inputDataDir);
        
    }
}
