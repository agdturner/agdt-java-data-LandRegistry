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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Environment;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_ID;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Object;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Strings;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_TypeID;
import uk.ac.leeds.ccg.andyt.projects.landregistry.io.LR_Files;

/**
 *
 * @author geoagdt
 */
public class LR_Main_Process extends LR_Object {

    // For convenience
    public LR_Strings Strings;
    public LR_Files Files;

    protected LR_Main_Process() {
        super();
        Strings = Env.Strings;
        Files = Env.Files;
    }

    public LR_Main_Process(LR_Environment env) {
        super(env);
        Strings = Env.Strings;
        Files = Env.Files;
    }

    public static void main(String[] args) {
        LR_Main_Process p;
        p = new LR_Main_Process(new LR_Environment());
        p.Files.setDataDirectory(new File(System.getProperty("user.dir"), "data"));
        p.run();
    }

    // Main switch declaration
    boolean doSelect = false;
    boolean doGeneralise = false;
    boolean doGeneraliseAreas = false;
    boolean doGeneraliseAll = false;
    boolean doTransitions = false;
    boolean doTransitionsAreas = false;
    boolean doTransitionsAll = false;

    public void run() {

        // Main switches
//        writeCollections = true;
        doSelect = true;
        doGeneralise = true;
        doGeneraliseAreas = true;
//        doGeneraliseAll = true;
//        doTransitions = true;
//        doTransitionsAreas = true;
//        doTransitionsAll = true;

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
//            overwrite = true;
            overwrite = false;
            // Run
            LR_Select_Process sp;
            sp = new LR_Select_Process(Env);
            sp.Files.setDataDirectory(Files.getDataDir());
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
            f = Files.getEnvDataFile();
            if (!f.exists()) {
                Generic_StaticIO.writeObject(Env, f, "Env");
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
        HashMap<LR_ID, Integer> minsCC;
        /**
         * minsOC is the minimum count for a generalisation reported in the
         * overseas data.
         */
        HashMap<LR_ID, Integer> minsOC;

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
                inputDataDir = Files.getOutputDataDir(Strings);
                LR_Generalise_Process gp;
                gp = new LR_Generalise_Process(Env);
                gp.Files.setDataDirectory(new File(System.getProperty("user.dir"), "data"));
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
                inputDataDir = Files.getInputDataDir(Strings);
                LR_Generalise_Process gp;
                gp = new LR_Generalise_Process(Env);
                gp.Files.setDataDirectory(new File(System.getProperty("user.dir"), "data"));
                minsCC = getMinsCC(10);
                minsOC = getMinsOC(5);
                ite = areas.iterator();
                while (ite.hasNext()) {
                    area = ite.next();
                    doFull = true;
                    gp.run(area, doAll, minsCC, minsOC, inputDataDir, doCCOD, doOCOD, doFull, overwrite);
                    doFull = false;
                    gp.run(area, doAll, minsCC, minsOC, inputDataDir, doCCOD, doOCOD, doFull, overwrite);
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
                inputDataDir = Files.getOutputDataDir(Strings);
                LR_Transitions_Process tp;
                tp = new LR_Transitions_Process(Env);
                tp.Files.setDataDirectory(new File(System.getProperty("user.dir"), "data"));
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
                inputDataDir = Files.getOutputDataDir(Strings);
                LR_Transitions_Process tp;
                tp = new LR_Transitions_Process(Env);
                tp.Files.setDataDirectory(new File(System.getProperty("user.dir"), "data"));
                tp.run(area, doAll, inputDataDir, minCC, minOC, overwrite);
            }
        }
//        if (writeCollections) {
//            // If any collections have changed then write them out again.
//            Env.writeCollections();
//        }
    }

    /**
     *
     * @param defaultMin
     * @return
     */
    protected HashMap<LR_ID, Integer> getMinsCC(int defaultMin) {
        HashMap<LR_ID, Integer> result;
        result = new HashMap<>();
        Iterator<LR_TypeID> ite;
        ite = Env.NonNullTypes.iterator();
        LR_ID typeID;
        while (ite.hasNext()) {
            typeID = ite.next();
            if (typeID.equals(Env.PostcodeDistrictTypeID)) {
                result.put(typeID, 0);
            } else if (typeID.equals(Env.PricePaidTypeID)) {
                result.put(typeID, 0);
            } else {
                result.put(typeID, defaultMin);
            }
        }
        return result;
    }

    /**
     *
     * @param defaultMin
     * @return
     */
    protected HashMap<LR_ID, Integer> getMinsOC(int defaultMin) {
        HashMap<LR_ID, Integer> result;
        result = new HashMap<>();
        Iterator<LR_TypeID> ite;
        ite = Env.NonNullTypes.iterator();
        LR_ID typeID;
        while (ite.hasNext()) {
            typeID = ite.next();
            if (typeID.equals(Env.PostcodeDistrictTypeID)) {
                result.put(typeID, 0);
            } else if (typeID.equals(Env.PricePaidTypeID)) {
                result.put(typeID, 0);
            } else {
                result.put(typeID, defaultMin);
            }
        }
        return result;
    }

    protected String getName00(boolean doFull, String name0) {
        String result = "";
        if (doFull) {
            result += name0 + "_" + Env.Strings.S_FULL + "_";
        } else {
            result += name0 + "_" + Env.Strings.S_COU + "_";
        }
        return result;
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
            return getSetNames(Env.Strings.S_FULL, CCODorOCOD);
        } else {
            return getSetNames(Env.Strings.S_COU, CCODorOCOD);
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
        dir = new File(Files.getInputDataDir(CCODorOCOD), s_FULL_or_COU);
        filenames = dir.list();
        for (String filename : filenames) {
            if (filename.contains(s_FULL_or_COU)) {
                if (!filename.contains(Env.Strings.S_zip)) {
                    split = filename.split("_" + s_FULL_or_COU + "_");
                    names2.add(split[1]);
                }
            }
        }
        return names2;
    }
}
