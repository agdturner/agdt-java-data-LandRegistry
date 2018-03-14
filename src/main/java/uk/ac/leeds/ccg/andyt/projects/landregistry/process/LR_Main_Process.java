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
import java.util.HashMap;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_ID;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Strings;
import uk.ac.leeds.ccg.andyt.projects.landregistry.io.LR_Files;

/**
 *
 * @author geoagdt
 */
public class LR_Main_Process {

    HashMap<LR_ID, String> IDToAddress;
    HashMap<String, LR_ID> AddressToID;

    int IDtoAddressInitialSize;

    LR_Strings Strings;
    LR_Files Files;

    public LR_Main_Process() {
        Files = new LR_Files();
        Strings = new LR_Strings();
    }

    public static void main(String[] args) {
        LR_Main_Process p;
        p = new LR_Main_Process();
        p.Files.setDataDirectory(new File(System.getProperty("user.dir"), "data"));
        p.loadIDToAddress();
        p.loadAddressToID();
        p.IDtoAddressInitialSize = p.AddressToID.size();
        p.run();
    }

    // Main switch declaration
    boolean doSelectLeeds = false;
    boolean doGeneralise = false;
    boolean doGeneraliseLeeds = false;
    boolean doGeneraliseAll = false;
    boolean doTransitions = false;
    boolean doTransitionsLeeds = false;
    boolean doTransitionsAll = false;

    public void run() {

        // Main switches
        doSelectLeeds = true;
//        doGeneralise = true;
//        doGeneraliseLeeds = true;
//        doGeneraliseAll = true;
//        doTransitions = true;
//        doTransitionsLeeds = true;
        
        String area;
        area = "LEEDS";

        /**
         * If doFull the run generalises the FULL data otherwise this
         * generalises the Change Only Update (COU) data.
         */
        boolean doFull;

        if (doSelectLeeds) {
            // Select Leeds
            LR_Select_Process sp;
            sp = new LR_Select_Process();
            sp.Files.setDataDirectory(Files.getDataDir());
            doFull = true;
            sp.run(IDToAddress, AddressToID, area, doFull);
            doFull = false;
            sp.run(IDToAddress, AddressToID, area, doFull);
        }

        /**
         * inputDataDir provides the location of the input data
         */
        File inputDataDir;

        if (doGeneralise) {
            /**
             * minCC is the minimum count for a generalisation reported in the
             * corporate data.
             */
            int minCC;
            /**
             * minCC is the minimum count for a generalisation reported in the
             * overseas data.
             */
            int minOC;
            /**
             * If doAll the run is for all areas and area is ignored.
             */
            boolean doAll;
            boolean doCCOD;
            boolean doOCOD;
            if (doGeneraliseLeeds) {
                doAll = false;
                doCCOD = true;
                doOCOD = true;
                inputDataDir = Files.getOutputDataDir(Strings);
                LR_Generalise_Process gp;
                gp = new LR_Generalise_Process();
                gp.Files.setDataDirectory(new File(System.getProperty("user.dir"), "data"));
                minCC = 5;
                minOC = 1;
                doFull = true;
                gp.run(IDToAddress, AddressToID, area, doAll, minCC, minOC, inputDataDir, doCCOD, doOCOD, doFull);
                doFull = false;
                gp.run(IDToAddress, AddressToID, area, doAll, minCC, minOC, inputDataDir, doCCOD, doOCOD, doFull);
            }
            if (doGeneraliseAll) {
                // Generalise All
                doAll = true;
                doCCOD = true;
                doOCOD = true;
                inputDataDir = Files.getInputDataDir(Strings);
                LR_Generalise_Process gp;
                gp = new LR_Generalise_Process();
                gp.Files.setDataDirectory(new File(System.getProperty("user.dir"), "data"));
                minCC = 10;
                minOC = 5;
                doFull = true;
                gp.run(IDToAddress, AddressToID, area, doAll, minCC, minOC, inputDataDir, doCCOD, doOCOD, doFull);
                doFull = false;
                gp.run(IDToAddress, AddressToID, area, doAll, minCC, minOC, inputDataDir, doCCOD, doOCOD, doFull);
            }
        }

        if (doTransitions) {
            if (doTransitionsLeeds) {
                int min = 1;
                inputDataDir = Files.getOutputDataDir(Strings);
                LR_Transitions_Process tp;
                tp = new LR_Transitions_Process();
                tp.Files.setDataDirectory(new File(System.getProperty("user.dir"), "data"));
                tp.run(IDToAddress, AddressToID, area, inputDataDir, min);
            }
//            if {doTransitionsAll) {
//                
//            }
        }

        /**
         * If IDToAddress, AddressToID have increased in size, write them out
         * again.
         */
        if (IDtoAddressInitialSize < IDToAddress.size()) {
            writeIDToAddress();
        }
    }

    protected void writeIDToAddress() {
        File f;
        f = new File(Files.getGeneratedDataDir(Strings), "IDToAddress.dat");
        Generic_StaticIO.writeObject(IDToAddress, f);
        f = new File(Files.getGeneratedDataDir(Strings), "AddressToID.dat");
        Generic_StaticIO.writeObject(AddressToID, f);
    }

    protected void loadIDToAddress() {
        File f;
        f = new File(Files.getGeneratedDataDir(Strings), "IDToAddress.dat");
        if (!f.exists()) {
            IDToAddress = new HashMap<>();
        } else {
            IDToAddress = (HashMap<LR_ID, String>) Generic_StaticIO.readObject(f);
        }
    }

    protected void loadAddressToID() {
        File f;
        f = new File(Files.getGeneratedDataDir(Strings), "AddressToID.dat");
        if (!f.exists()) {
            AddressToID = new HashMap<>();
        } else {
            AddressToID = (HashMap<String, LR_ID>) Generic_StaticIO.readObject(f);
        }
    }

}
