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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import uk.ac.leeds.ccg.andyt.data.core.Data_Environment;
import uk.ac.leeds.ccg.andyt.data.format.Data_ReadTXT;
import uk.ac.leeds.ccg.andyt.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_Defaults;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Environment;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.id.LR_ValueID_TypeID;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Object;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Strings;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.id.LR_TypeID;
import uk.ac.leeds.ccg.andyt.projects.landregistry.io.LR_Files;

/**
 *
 * @author geoagdt
 */
public class LR_Main_Process extends LR_Object {

    // For convenience
    public LR_Files files;
    protected final Data_ReadTXT reader;

    public LR_Main_Process(LR_Environment env) {
        super(env);
        files = this.env.files;
        reader = new Data_ReadTXT(env.de);
    }

    public static void main(String[] args) {
        try {
            LR_Files files = new LR_Files(Generic_Defaults.getDefaultDir());
            Generic_Environment ge = new Generic_Environment();
            Data_Environment de = new Data_Environment(ge);
            LR_Environment env = new LR_Environment(de, files);
            LR_Main_Process p = new LR_Main_Process(env);
            p.run();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    // Main switch declaration
    boolean doSelect = false;
    boolean doGeneralise = false;
    boolean doGeneraliseAreas = false;
    boolean doGeneraliseAll = false;
    boolean doTransitions = false;
    boolean doTransitionsAreas = false;
    boolean doTransitionsAll = false;
    boolean doLoadPricePaidData = false;
    boolean doJoinPricePaidDataAndOwnershipData = true;

    public void run() throws IOException {

        // Main switches
//        writeCollections = true;
        doSelect = true;
//        doGeneralise = true;
//        doGeneraliseAreas = true;
//        doGeneraliseAll = true;
//        doTransitions = true;
//        doTransitionsAreas = true;
//        doTransitionsAll = true;
//        doLoadPricePaidData = true;
//        doJoinPricePaidDataAndOwnershipData = true;

        ArrayList<String> areas;
        areas = new ArrayList<>();
        areas.add("LEEDS");
        //areas.add("BRADFORD");
        String area;
        Iterator<String> ite;

        /**
         * If doFull the run generalises the FULL data otherwise this
         * generalises the Change Only Update (COU) data.
         */
        boolean doFull;

        boolean overwrite;

        // Select Leeds
        if (doSelect) {
            // Options
            overwrite = true;
            //overwrite = false;
            // Run
            LR_Select_Process sp;
            sp = new LR_Select_Process(env);
            sp.files.setDir(files.getDir());
            ite = areas.iterator();
            while (ite.hasNext()) {
                area = ite.next();
                doFull = true;
                sp.run(area, doFull, overwrite);
                doFull = false;
                sp.run(area, doFull, overwrite);
            }

            // Write out cache if it does not exist.
            File f;
            f = files.getEnvDataFile();
            if (!f.exists()) {
                env.env.io.writeObject(env, f, "Env");
            }
        }

        /**
         * inputDataDir provides the location of the input data
         */
        File inputDataDir;
        /**
         * minsCC is the minimum count for a generalisation reported in the
         * corporate data.
         */
        HashMap<LR_TypeID, Integer> minsCC;
        /**
         * minsOC is the minimum count for a generalisation reported in the
         * overseas data.
         */
        HashMap<LR_TypeID, Integer> minsOC;

        if (doGeneralise) {
            /**
             * If doAll the run is for all areas and area is ignored.
             */
            boolean doAll;
            boolean doCCOD;
            boolean doOCOD;
            if (doGeneraliseAreas) {
                // Options
                overwrite = true;
//            overwrite = false;
                // Run
                doAll = false;
                doCCOD = true;
                doOCOD = true;
                inputDataDir = files.getOutputDir();
                LR_Generalise_Process gp;
                gp = new LR_Generalise_Process(env);
                gp.files.setDir(new File(System.getProperty("user.dir"), "data"));
                minsCC = getMinsCC(5);
                minsOC = getMinsOC(1);
                ite = areas.iterator();
                while (ite.hasNext()) {
                    area = ite.next();
                    doFull = true;
                    gp.run(area, doAll, minsCC, minsOC, inputDataDir, doCCOD, doOCOD, doFull, overwrite);
//                    doFull = false;
//                    gp.run(area, doAll, minsCC, minsOC, inputDataDir, doCCOD, doOCOD, doFull, overwrite);
                }
            }
            if (doGeneraliseAll) {
                // Options
                overwrite = true;
//            overwrite = false;
                // Run
                doAll = true;
                doCCOD = true;
                doOCOD = true;
                inputDataDir = files.getInputDir();
                LR_Generalise_Process gp;
                gp = new LR_Generalise_Process(env);
                gp.files.setDir(new File(System.getProperty("user.dir"), "data"));
                minsCC = getMinsCC(10);
                minsOC = getMinsOC(5);
                ite = areas.iterator();
                while (ite.hasNext()) {
                    area = ite.next();
                    doFull = true;
                    gp.run(area, doAll, minsCC, minsOC, inputDataDir, doCCOD, doOCOD, doFull, overwrite);
//                    doFull = false;
//                    gp.run(area, doAll, minsCC, minsOC, inputDataDir, doCCOD, doOCOD, doFull, overwrite);
                }
            }
        }

        if (doTransitions) {
            int minCC;
            int minOC;
            boolean doAll;
            if (doTransitionsAreas) {
                doAll = false;
                // Options
                overwrite = true;
//            overwrite = false;
                // Run
                minCC = 2;
                minOC = 2;
                inputDataDir = files.getOutputDir();
                LR_Transitions_Process tp;
                tp = new LR_Transitions_Process(env);
                tp.files.setDir(new File(System.getProperty("user.dir"), "data"));
                ite = areas.iterator();
                while (ite.hasNext()) {
                    area = ite.next();
                    tp.run(area, doAll, inputDataDir, minCC, minOC, overwrite);
                }
            }
            if (doTransitionsAll) {
                area = "";
                doAll = true;
                // Options
                overwrite = true;
//            overwrite = false;
                // Run
                minCC = 2;
                minOC = 2;
                inputDataDir = files.getOutputDir();
                LR_Transitions_Process tp;
                tp = new LR_Transitions_Process(env);
                tp.files.setDir(new File(System.getProperty("user.dir"), "data"));
                tp.run(area, doAll, inputDataDir, minCC, minOC, overwrite);
            }
        }
        //        if (writeCollections) {
        //            // If any collections have changed then write them out again.
        //            env.writeCollections();
        //        }

        // Select Leeds
        if (doLoadPricePaidData) {
            // Options
            overwrite = true;
//            overwrite = false;
            // Run
            LR_LoadPricePaidData_Process p;
            p = new LR_LoadPricePaidData_Process(env, overwrite);
            p.files.setDir(files.getDir());
            ite = areas.iterator();
            while (ite.hasNext()) {
                area = ite.next();
                p.run(area);
            }
        }

        // Join
        if (doJoinPricePaidDataAndOwnershipData) {
            // Options
            overwrite = true;
//            overwrite = false;
            // Run
            LR_JoinPricePaidDataAndOwnershipData_Process p;
            p = new LR_JoinPricePaidDataAndOwnershipData_Process(env, overwrite);
            p.files.setDir(files.getDir());
            ite = areas.iterator();
            while (ite.hasNext()) {
                area = ite.next();
                p.run(area);
            }
        }

        // Write out cache if it does not exist.
        File f;
        f = files.getEnvDataFile();
        if (!f.exists()) {
            env.env.io.writeObject(env, f, "Env");
        }

    }

    /**
     *
     * @param defaultMin
     * @return
     */
    protected HashMap<LR_TypeID, Integer> getMinsCC(int defaultMin) {
        HashMap<LR_TypeID, Integer> r = new HashMap<>();
        Iterator<LR_TypeID> ite = env.NonNullTypes.iterator();
        while (ite.hasNext()) {
            LR_TypeID typeID = ite.next();
            if (typeID.equals(env.PostcodeDistrictTypeID)) {
                r.put(typeID, 0);
            } else if (typeID.equals(env.PricePaidTypeID)) {
                r.put(typeID, 0);
            } else {
                r.put(typeID, defaultMin);
            }
        }
        return r;
    }

    /**
     *
     * @param defaultMin
     * @return
     */
    protected HashMap<LR_TypeID, Integer> getMinsOC(int defaultMin) {
        HashMap<LR_TypeID, Integer> r = new HashMap<>();
        Iterator<LR_TypeID> ite = env.NonNullTypes.iterator();
        while (ite.hasNext()) {
            LR_TypeID typeID = ite.next();
            if (typeID.equals(env.PostcodeDistrictTypeID)) {
                r.put(typeID, 0);
            } else if (typeID.equals(env.PricePaidTypeID)) {
                r.put(typeID, 0);
            } else {
                r.put(typeID, defaultMin);
            }
        }
        return r;
    }

    protected String getName00(boolean doFull, String name0) {
        String r = "";
        if (doFull) {
            r += name0 + "_" + LR_Strings.s_FULL + "_";
        } else {
            r += name0 + "_" + LR_Strings.s_COU + "_";
        }
        return r;
    }

    /**
     * Returns names of data to process.
     *
     * @param doFull If true then names of FULL data are returned. Otherwise
     * names of COU data are returned.
     * @param CCODorOCOD For either Commercial or Overseas data depending
     * respectively on whether CCODorOCOD = "CCOD" or "OCOD".
     * @return
     */
    protected ArrayList<String> getSetNames(boolean doFull, String CCODorOCOD) {
        if (doFull) {
            return getSetNames(LR_Strings.s_FULL, CCODorOCOD);
        } else {
            return getSetNames(LR_Strings.s_COU, CCODorOCOD);
        }
    }

    /**
     * Returns names of data to process.
     *
     * @param s_FULL_or_COU Expects either "FULL" or "COU" depending.
     * @param CCODorOCOD For either Commercial or Overseas data depending
     * respectively on whether CCODorOCOD = "CCOD" or "OCOD".
     * @return
     */
    protected ArrayList<String> getSetNames(String s_FULL_or_COU, String CCODorOCOD) {
        ArrayList<String> names2;
        names2 = new ArrayList<>();
        File dir;
        String[] filenames;
        String[] split;
        dir = new File(files.getInputDataDir(CCODorOCOD), s_FULL_or_COU);
        if (dir.mkdirs()) {
            System.err.println("Warning: Input data is not in place. Assume "
                    + "processing with other data...");
        }
        filenames = dir.list();
        for (String filename : filenames) {
            if (filename.contains(s_FULL_or_COU)) {
                if (!filename.contains(LR_Strings.s_zip)) {
                    split = filename.split("_" + s_FULL_or_COU + "_");
                    names2.add(split[1]);
                }
            }
        }
        return names2;
    }
}
