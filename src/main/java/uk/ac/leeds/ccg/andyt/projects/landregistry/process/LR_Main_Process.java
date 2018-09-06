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
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Environment;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Object;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Strings;
import uk.ac.leeds.ccg.andyt.projects.landregistry.io.LR_Files;

/**
 *
 * @author geoagdt
 */
public class LR_Main_Process extends LR_Object {

    int nInitialPropertyAddress;
    int nInitialTitleNumber;
    int nInitialTenure;
    int nInitialPostcodeDistrict;
    int nInitialCompanyRegistrationNo;
    int nInitialCountryIncorporated;
    int nInitialProprietorName;
    int nInitialProprietorshipCategory;

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
        p = new LR_Main_Process();
        p.Files.setDataDirectory(new File(System.getProperty("user.dir"), "data"));
        p.Env.loadIDToPropertyAddress();
        p.Env.loadPropertyAddressToID();
        p.Env.loadIDToTitleNumber();
        p.Env.loadTitleNumberToID();
        p.Env.loadIDToTenure();
        p.Env.loadTenureToID();
        p.Env.loadPostcodeDistrictToID();
        p.Env.loadIDToPostcodeDistrict();
        p.Env.loadIDToCompanyRegistrationNo();
        p.Env.loadCompanyRegistrationNoToID();
        p.Env.loadIDToCountryIncorporated();
        p.Env.loadCountryIncorporatedToID();
        p.Env.loadIDToProprietorName();
        p.Env.loadProprietorNameToID();
        p.Env.loadIDToProprietorshipCategory();
        p.Env.loadProprietorshipCategoryToID();
        p.nInitialPropertyAddress = p.Env.PropertyAddressToID.size();
        p.nInitialTitleNumber = p.Env.TitleNumberToID.size();
        p.nInitialTenure = p.Env.TenureToID.size();
        p.nInitialPostcodeDistrict = p.Env.PostcodeDistrictToID.size();
        p.nInitialCompanyRegistrationNo = p.Env.CompanyRegistrationNoToID.size();
        p.nInitialCountryIncorporated = p.Env.CountryIncorporatedToID.size();
        p.nInitialProprietorName = p.Env.ProprietorNameToID.size();
        p.nInitialProprietorshipCategory = p.Env.ProprietorshipCategoryToID.size();
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
//        doTransitionsAll = true;

        String area;
        area = "LEEDS";

        /**
         * If doFull the run generalises the FULL data otherwise this
         * generalises the Change Only Update (COU) data.
         */
        boolean doFull;

        boolean overwrite;

        // Select Leeds
        if (doSelectLeeds) {
            // Options
            doFull = true;
//            doFull = false;
//            overwrite = true;
            overwrite = false;
            // Run
            LR_Select_Process sp;
            sp = new LR_Select_Process(Env);
            sp.Files.setDataDirectory(Files.getDataDir());
            if (doFull) {
                sp.run(area, doFull, overwrite);
            }
            doFull = false;
            sp.run(area, doFull, overwrite);
        }

        /**
         * inputDataDir provides the location of the input data
         */
        File inputDataDir;
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

        if (doGeneralise) {
            /**
             * If doAll the run is for all areas and area is ignored.
             */
            boolean doAll;
            boolean doCCOD;
            boolean doOCOD;
            if (doGeneraliseLeeds) {
                // Options
                doFull = true;
//                doFull = false;
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
                minCC = 5;
                minOC = 1;
                if (doFull) {
                    gp.run(area, doAll, minCC, minOC, inputDataDir, doCCOD, doOCOD, doFull, overwrite);
                }
                doFull = false;
                gp.run(area, doAll, minCC, minOC, inputDataDir, doCCOD, doOCOD, doFull, overwrite);
            }
            if (doGeneraliseAll) {
                // Options
                doFull = true;
//                doFull = false;
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
                minCC = 10;
                minOC = 5;
                if (doFull) {
                    gp.run(area, doAll, minCC, minOC, inputDataDir, doCCOD, doOCOD, doFull, overwrite);
                }
                doFull = false;
                gp.run(area, doAll, minCC, minOC, inputDataDir, doCCOD, doOCOD, doFull, overwrite);
            }
        }

        if (doTransitions) {
            doFull = false;
            boolean doAll;
            if (doTransitionsLeeds) {
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
                tp.run(area, doAll, inputDataDir, minCC, minOC, overwrite);
            }
            if (doTransitionsAll) {
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

        /**
         * If IDToPropertyAddress has increased in size, write out the mappings
         * again.
         */
        if (nInitialPropertyAddress < Env.IDToPropertyAddress.size()) {
            Env.writePropertyAddressLookups();
        }
        /**
         * If IDToCompanyRegistrationNo has increased in size, write out the
         * mappings again.
         */
        if (nInitialCompanyRegistrationNo < Env.IDToCompanyRegistrationNo.size()) {
            Env.writeCompanyRegistrationNoLookups();
        }
        /**
         * If IDToCountryIncorporated has increased in size, write out the
         * mappings again.
         */
        if (nInitialCountryIncorporated < Env.IDToCountryIncorporated.size()) {
            Env.writeCountryIncorporatedLookups();
        }
        /**
         * If IDToProprietorName has increased in size, write out the mappings
         * again.
         */
        if (nInitialProprietorName < Env.IDToProprietorName.size()) {
            Env.writeProprietorNameLookups();
        }
        /**
         * If IDToTitleNumber has increased in size, write out the mappings
         * again.
         */
        if (nInitialTitleNumber < Env.IDToTitleNumber.size()) {
            Env.writeTitleNumberLookups();
        }
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
        ArrayList<String> names2;
        names2 = new ArrayList<>();
        String[] filenames;
        filenames = Files.getInputDataDir(CCODorOCOD).list();
        String[] split;
        if (doFull) {
            for (String filename : filenames) {
                if (filename.contains(Env.Strings.S_FULL)) {
                    if (!filename.contains(Env.Strings.S_zip)) {
                        split = filename.split("_" + Env.Strings.S_FULL + "_");
                        names2.add(split[1]);
                    }
                }
            }
        } else {
            for (String filename : filenames) {
                if (filename.contains(Env.Strings.S_COU)) {
                    if (!filename.contains(Env.Strings.S_zip)) {
                        split = filename.split("_" + Env.Strings.S_COU + "_");
                        names2.add(split[1]);
                    }
                }
            }
        }
        return names2;
    }

}
