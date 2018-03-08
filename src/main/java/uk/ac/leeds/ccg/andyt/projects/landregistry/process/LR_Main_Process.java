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

/**
 *
 * @author geoagdt
 */
public class LR_Main_Process {
    
    public LR_Main_Process(){}
    
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
        LR_Generalise_Process gp;
        gp = new LR_Generalise_Process();
        gp.Files.setDataDirectory(new File(System.getProperty("user.dir"), "data"));
        gp.run(area);
        
    }
}
