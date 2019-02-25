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
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.data.format.Data_ReadCSV;
import uk.ac.leeds.ccg.andyt.generic.util.Generic_Collections;
import uk.ac.leeds.ccg.andyt.generic.time.Generic_YearMonth;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Environment;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_ID;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_ID2;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Strings;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_ValueID;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.landregistry.LR_CC_COU_Record;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.landregistry.LR_CC_FULL_Record;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.landregistry.LR_OC_COU_Record;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.landregistry.LR_OC_FULL_Record;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.landregistry.LR_Record;

/**
 * For reading and processing overseas and corporate ownership land registry
 * data.
 *
 * @author Andy Turner
 */
public class LR_Transitions_Process extends LR_Main_Process {

    /**
     * This provides a store to look up company registration numbers from
     * TitleNumbers. If a TitleNumber is not known for a YearMonth, then the
     * TitleNumber has either not yet been in the data, or is in a record for an
     * earlier YearMonth. Keys are YearMonth, Values are HashMaps with Keys as
     * TitleNumberIDs and Values as lists of CompanyRegistrationNoIDs where the
     * first in the list is CompanyRegistrationNo1ID, the second is
     * CompanyRegistrationNo2ID, the third is CompanyRegistrationNo3ID, and the
     * fourth is CompanyRegistrationNo4ID.
     */
    public TreeMap<Generic_YearMonth, HashMap<LR_ValueID, ArrayList<LR_ValueID>>> YMTitleNumberIDToCompanyRegistrationNoIDs;
    /**
     * This provides a store to look up TitleNumbers for a company at any given
     * YearMonth. If a company registration number is not known for the
     * YearMonth, then either it is for a company not yet seen in the data, or
     * it is for a company that has not gained any titles. To find the titles
     * the company has, then search backwards. If a company gains any titles,
     * all title numbers are added, so the search only needs to go back to find
     * a record.
     */
    public TreeMap<Generic_YearMonth, HashMap<LR_ValueID, HashSet<LR_ValueID>>> YMCompanyRegistrationNoIDToTitleNumberIDs;
    /**
     * This provides a store to look up company registration numbers from
     * proprietor names at any given YearMonth. (There may be multiple company
     * registration numbers for a single proprietor name!)
     */
    public TreeMap<Generic_YearMonth, HashMap<LR_ValueID, ArrayList<LR_ValueID>>> YMProprietorNameIDToCompanyRegistrationNoIDs;
    /**
     * This provides a store to look up a proprietor names from company
     * registration numbers at any given YearMonth.
     */
    public TreeMap<Generic_YearMonth, HashMap<LR_ValueID, ArrayList<LR_ValueID>>> YMCompanyRegistrationNoIDToProprietorNameIDs;

    /**
     * Set to true if TitleNumberID to CompanyRegistrationNoID lookups are
     * modified.
     */
    public boolean UpdatedTitleNumberCompanyRegistrationNoLookups;
    public boolean UpdatedProprietorNameIDCompanyRegistrationNoLookups;

    public LR_Transitions_Process(LR_Environment env) {
        super(env);
    }

    public HashMap<LR_ID2, ArrayList<LR_ValueID>> IDToProprietorNameIDs;

    HashMap<LR_ID2, Integer> addedCCRCount;
    HashMap<LR_ID2, Integer> deletedCCRCount;
    HashMap<LR_ID2, Integer> addedOCRCount;
    HashMap<LR_ID2, Integer> deletedOCRCount;
    HashMap<String, HashMap<LR_ID2, ArrayList<LR_CC_COU_Record>>> addedCCR;
    HashMap<String, HashMap<LR_ID2, ArrayList<LR_CC_COU_Record>>> deletedCCR;
    HashMap<String, HashMap<LR_ID2, ArrayList<LR_OC_COU_Record>>> addedOCR;
    HashMap<String, HashMap<LR_ID2, ArrayList<LR_OC_COU_Record>>> deletedOCR;

    HashMap<LR_ID2, LR_CC_FULL_Record> fullCCR;
    HashMap<LR_ID2, LR_OC_FULL_Record> fullOCR;

    public void run(String area, boolean doAll, File inputDataDir,
            int minCC, int minOC, boolean overwrite) {
        boolean printDiff;
//        printDiff = true;
        printDiff = false;

        File outputDataDir;
        outputDataDir = Files.getOutputDataDir();

        ArrayList<String> names0;
        //ArrayList<String> names1;
        ArrayList<String> names2 = null;
        String name;
        String name0;
        String name00;
        names0 = new ArrayList<>();
        //names1 = new ArrayList<>();
        names0.add(LR_Strings.s_CCOD);
        names0.add(LR_Strings.s_OCOD);
        boolean isCCOD;
        //names1.add("COU");
        //names1.add("FULL");

        boolean upDateIDs = true;

        IDToProprietorNameIDs = new HashMap<>();
        YMTitleNumberIDToCompanyRegistrationNoIDs = new TreeMap<>();
        YMCompanyRegistrationNoIDToTitleNumberIDs = new TreeMap<>();
        YMProprietorNameIDToCompanyRegistrationNoIDs = new TreeMap<>();
        YMCompanyRegistrationNoIDToProprietorNameIDs = new TreeMap<>();
        HashMap<LR_ValueID, ArrayList<LR_ValueID>> titleNumberIDToCompanyRegistrationNoIDs;
        HashMap<LR_ValueID, HashSet<LR_ValueID>> companyRegistrationNoIDToTitleNumberIDs;
        HashMap<LR_ValueID, ArrayList<LR_ValueID>> proprietorNameIDToCompanyRegistrationNoIDs;
        HashMap<LR_ValueID, ArrayList<LR_ValueID>> companyRegistrationNoIDToProprietorNameIDs;

        addedCCRCount = new HashMap<>();
        deletedCCRCount = new HashMap<>();
        addedOCRCount = new HashMap<>();
        deletedOCRCount = new HashMap<>();

        addedCCR = new HashMap<>();
        deletedCCR = new HashMap<>();
        addedOCR = new HashMap<>();
        deletedOCR = new HashMap<>();

        HashMap<LR_ID2, ArrayList<LR_CC_COU_Record>> addedCCRTime = null;
        HashMap<LR_ID2, ArrayList<LR_CC_COU_Record>> deletedCCRTime = null;
        HashMap<LR_ID2, ArrayList<LR_OC_COU_Record>> addedOCRTime = null;
        HashMap<LR_ID2, ArrayList<LR_OC_COU_Record>> deletedOCRTime = null;

        File indir;
        File outdir;
        File fin;
        File fout;
        ArrayList<String> lines;

        // Initialise Transitions Generalisation output
        PrintWriter pw = null;
        outdir = new File(outputDataDir, LR_Strings.s_Transitions);
        if (!doAll) {
            outdir = new File(outputDataDir, LR_Strings.s_Transitions);
            outdir = new File(outdir, LR_Strings.s_Subsets);
            outdir = new File(outdir, area);
        }
        System.out.println("outdir " + outdir);
        outdir.mkdirs();
        fout = new File(outdir, "TransitionsGeneralisation.csv");
        try {
            pw = new PrintWriter(fout);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LR_Transitions_Process.class.getName()).log(Level.SEVERE, null, ex);
        }

        String changeIndicator;
        LR_CC_COU_Record ccr;
        LR_OC_COU_Record ocr;
        LR_CC_FULL_Record fullccr;
        LR_OC_FULL_Record fullocr;

        Generic_YearMonth ym;
        ym = new Generic_YearMonth(env.ge, "2017-11");

        // Initialise key lookups
        titleNumberIDToCompanyRegistrationNoIDs = new HashMap<>();
        companyRegistrationNoIDToTitleNumberIDs = new HashMap<>();
        proprietorNameIDToCompanyRegistrationNoIDs = new HashMap<>();
        companyRegistrationNoIDToProprietorNameIDs = new HashMap<>();
        YMTitleNumberIDToCompanyRegistrationNoIDs.put(ym, titleNumberIDToCompanyRegistrationNoIDs);
        YMCompanyRegistrationNoIDToTitleNumberIDs.put(ym, companyRegistrationNoIDToTitleNumberIDs);
        YMProprietorNameIDToCompanyRegistrationNoIDs.put(ym, proprietorNameIDToCompanyRegistrationNoIDs);
        YMCompanyRegistrationNoIDToProprietorNameIDs.put(ym, companyRegistrationNoIDToProprietorNameIDs);

        LR_ValueID companyRegistrationNo1ID;
        LR_ValueID companyRegistrationNo2ID;
        LR_ValueID companyRegistrationNo3ID;
        LR_ValueID companyRegistrationNo4ID;
        LR_ValueID titleNumberID;
        LR_ValueID proprietorName1ID;
        LR_ValueID proprietorName2ID;
        LR_ValueID proprietorName3ID;
        LR_ValueID proprietorName4ID;

        // init fullCCR
        fullCCR = new HashMap<>();
        indir = new File(outputDataDir, LR_Strings.s_Subsets);
        indir = new File(indir, area);
        indir = new File(indir, "CCOD");
        indir = new File(indir, LR_Strings.s_FULL);
        name = "CCOD_FULL_2017_11";
        indir = new File(indir, name);
        fin = new File(indir, name + ".csv");
        lines = Data_ReadCSV.read(fin, null, 7);
        LR_ID2 ID;
        for (int line = 1; line < lines.size(); line++) {
            try {
                fullccr = new LR_CC_FULL_Record(env, ym, lines.get(line), upDateIDs);
                fullCCR.put(fullccr.getID(), fullccr);
                ID = fullccr.getID();
                // update IDToProprietorNameIDs
                proprietorName1ID = fullccr.getProprietorName1ID();
                proprietorName2ID = fullccr.getProprietorName2ID();
                proprietorName3ID = fullccr.getProprietorName3ID();
                proprietorName4ID = fullccr.getProprietorName4ID();
                updateIDToProprietorNameIDs(ID, proprietorName1ID,
                        proprietorName2ID, proprietorName3ID,
                        proprietorName4ID);
                companyRegistrationNo1ID = fullccr.getCompanyRegistrationNo1ID();
                companyRegistrationNo2ID = fullccr.getCompanyRegistrationNo2ID();
                companyRegistrationNo3ID = fullccr.getCompanyRegistrationNo3ID();
                companyRegistrationNo4ID = fullccr.getCompanyRegistrationNo4ID();
                titleNumberID = fullccr.getTitleNumberID();
                // Update key lookups
                updateKeyLookups(ID, companyRegistrationNo1ID,
                        companyRegistrationNo2ID, companyRegistrationNo3ID,
                        companyRegistrationNo4ID, titleNumberID,
                        proprietorName1ID, proprietorName2ID, proprietorName3ID,
                        proprietorName4ID,
                        titleNumberIDToCompanyRegistrationNoIDs,
                        companyRegistrationNoIDToTitleNumberIDs,
                        proprietorNameIDToCompanyRegistrationNoIDs,
                        companyRegistrationNoIDToProprietorNameIDs);
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
                Logger.getLogger(LR_Transitions_Process.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // init fullOCR
        fullOCR = new HashMap<>();
        indir = new File(outputDataDir, LR_Strings.s_Subsets);
        indir = new File(indir, area);
        indir = new File(indir, "OCOD");
        indir = new File(indir, LR_Strings.s_FULL);
        name = "OCOD_FULL_2017_11";
        indir = new File(indir, name);
        fin = new File(indir, name + ".csv");
        lines = Data_ReadCSV.read(fin, null, 7);
        for (int line = 1; line < lines.size(); line++) {
            try {
                fullocr = new LR_OC_FULL_Record(env, ym, lines.get(line), upDateIDs);
                fullOCR.put(fullocr.getID(), fullocr);
                ID = fullocr.getID();
                companyRegistrationNo1ID = fullocr.getCompanyRegistrationNo1ID();
                companyRegistrationNo2ID = fullocr.getCompanyRegistrationNo2ID();
                companyRegistrationNo3ID = fullocr.getCompanyRegistrationNo3ID();
                companyRegistrationNo4ID = fullocr.getCompanyRegistrationNo4ID();
                titleNumberID = fullocr.getTitleNumberID();
                proprietorName1ID = fullocr.getProprietorName1ID();
                proprietorName2ID = fullocr.getProprietorName2ID();
                proprietorName3ID = fullocr.getProprietorName3ID();
                proprietorName4ID = fullocr.getProprietorName4ID();
                updateIDToProprietorNameIDs(ID, proprietorName1ID,
                        proprietorName2ID, proprietorName3ID,
                        proprietorName4ID);
                // Update key lookups
                updateKeyLookups(ID, companyRegistrationNo1ID,
                        companyRegistrationNo2ID, companyRegistrationNo3ID,
                        companyRegistrationNo4ID, titleNumberID,
                        proprietorName1ID, proprietorName2ID, proprietorName3ID,
                        proprietorName4ID,
                        titleNumberIDToCompanyRegistrationNoIDs,
                        companyRegistrationNoIDToTitleNumberIDs,
                        proprietorNameIDToCompanyRegistrationNoIDs,
                        companyRegistrationNoIDToProprietorNameIDs);
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
                Logger.getLogger(LR_Transitions_Process.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Check if there are OCR in CCR
        Set<LR_ID2> s;
        s = new HashSet<>();
        s.addAll(fullCCR.keySet());
        s.retainAll(fullOCR.keySet());
        System.out.println("There are " + s.size() + " oversees corporate owners.");
        Iterator<LR_ID2> ite;
        String address;
        String titleNumber;
        LR_ValueID companyRegistrationNoID;
        LR_ValueID proprietorNameID;
        String companyRegistrationNo;
        String proprietorName;
        ArrayList<LR_ValueID> companyRegistrationNoIDs;
        ArrayList<LR_ValueID> proprietorNameIDs;
        ite = s.iterator();
        System.out.println("CompanyRegistrationNo, TitleNumber, ProprietorName, Address");
        while (ite.hasNext()) {
            ID = ite.next();
            address = ID.getPropertyAddressID().getValue();
            titleNumber = ID.getTitleNumberID().getValue();
            companyRegistrationNoIDs = titleNumberIDToCompanyRegistrationNoIDs.get(ID.getTitleNumberID());
            if (companyRegistrationNoIDs.size() > 1) {
                companyRegistrationNoID = companyRegistrationNoIDs.get(companyRegistrationNoIDs.size() - 1);
                proprietorNameIDs = companyRegistrationNoIDToProprietorNameIDs.get(companyRegistrationNoID);
                proprietorNameID = proprietorNameIDs.get(proprietorNameIDs.size() - 1);
            } else {
                proprietorNameIDs = IDToProprietorNameIDs.get(ID);
                proprietorNameID = proprietorNameIDs.get(proprietorNameIDs.size() - 1);
                companyRegistrationNoIDs = proprietorNameIDToCompanyRegistrationNoIDs.get(proprietorNameID);
                companyRegistrationNoID = companyRegistrationNoIDs.get(companyRegistrationNoIDs.size() - 1);
            }
            companyRegistrationNo = companyRegistrationNoID.getValue();
            proprietorName = proprietorNameID.getValue();
            System.out.println(companyRegistrationNo + ", " + titleNumber + ", " + proprietorName + ", " + address);
        }

        System.exit(0);
        Iterator<String> ites0;
        Iterator<String> ites1;
        ites0 = names0.iterator();
        while (ites0.hasNext()) {
            name0 = ites0.next();
            isCCOD = name0.equalsIgnoreCase("CCOD");
            name00 = "";
            name00 += name0 + "_COU_";
            names2 = getSetNames(false, name0);
            names2.remove("2017_10");
            ites1 = names2.iterator();
            while (ites1.hasNext()) {
                name = name00;
                String time;
                time = ites1.next();
                ym = new Generic_YearMonth(env.ge, time.replaceAll("_", "-"));
                name += time;
                if (isCCOD) {
                    addedCCRTime = new HashMap<>();
                    addedCCR.put(time, addedCCRTime);
                    deletedCCRTime = new HashMap<>();
                    deletedCCR.put(time, deletedCCRTime);
                } else {
                    addedOCRTime = new HashMap<>();
                    addedOCR.put(time, addedOCRTime);
                    deletedOCRTime = new HashMap<>();
                    deletedOCR.put(time, deletedOCRTime);
                }
                if (doAll) {
                    indir = inputDataDir;
                } else {
                    indir = new File(outputDataDir, LR_Strings.s_Subsets);
                    indir = new File(indir, area);
                }
                indir = new File(indir, name0);

                indir = new File(indir, name);
                System.out.println("indir " + indir);
                fin = new File(indir, name + ".csv");
                if (!fin.exists()) {
                    System.out.println("File " + fin + " does not exist.");
                }
                lines = Data_ReadCSV.read(fin, null, 7);
                //LR_Record r;
                for (int line = 1; line < lines.size(); line++) {
                    try {
                        if (isCCOD) {
                            // ccr = new LR_CC_COU_Record(env, ym, lines.get(ID));
                            // add(addedCCRTime, deletedCCRTime, ccr);
                        } else {
                            //  ocr = new LR_OC_COU_Record(env, ym, lines.get(ID));
                            // add(addedOCRTime, deletedOCRTime, ocr);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace(System.err);
                    } catch (Exception ex) {
                        ex.printStackTrace(System.err);
                        Logger.getLogger(LR_Transitions_Process.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        printGeneralisation(pw, minCC, minOC);
        pw.close();

        fout = new File(outdir, "TransitionsMapped.csv");
        try {
            pw = new PrintWriter(fout);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LR_Transitions_Process.class.getName()).log(Level.SEVERE, null, ex);
        }

        /**
         * mappedTransitions contains the detailed transition for each property.
         */
        HashMap<LR_ID2, ArrayList<ArrayList<? extends LR_Record>>> mTs;

        /**
         * For transitions in mappedTransitions.
         */
        ArrayList<ArrayList<? extends LR_Record>> ts = null;

        /**
         * List of records in transitions.
         */
        ArrayList<LR_OC_COU_Record> lod;
        ArrayList<LR_OC_COU_Record> loa;
        ArrayList<LR_CC_COU_Record> lcd;
        ArrayList<LR_CC_COU_Record> lca;

        /**
         * transitionTypes gives a type of transition for each property.
         */
        HashMap<LR_ID2, String> tts;

        /**
         * For name that describes a property ownership transition.
         */
        String tt;

        /**
         * transitionTypeCounts counts the number of transitions of each type.
         */
        TreeMap<String, Integer> ttc;

        /**
         * For the transition type count.
         */
        int transitionTypeCount;

        // Initialisation
        mTs = new HashMap<>();
        tts = new HashMap<>();
        ttc = new TreeMap<>();

        // For messages
        String m;

        Iterator<LR_ID2> ite1;

        Iterator<LR_CC_COU_Record> itec1;
        Iterator<LR_CC_COU_Record> itec2;
        Iterator<LR_OC_COU_Record> iteo1;
        Iterator<LR_OC_COU_Record> iteo2;

        LR_ID2 aID;
        /**
         * Delimeter
         */
        String d = " -> ";

        // Go through each time in order
        ites0 = names2.iterator();
        while (ites0.hasNext()) {
            String time;
            time = ites0.next();
            System.out.println(time);
            addedCCRTime = addedCCR.get(time);
            deletedCCRTime = deletedCCR.get(time);
            addedOCRTime = addedOCR.get(time);
            deletedOCRTime = deletedOCR.get(time);

            //LR_CC_FULL_Record recc;
            LR_CC_COU_Record recca;
            LR_CC_COU_Record reccd;
            //LR_OC_FULL_Record reco;
            LR_OC_COU_Record recoa;
            LR_OC_COU_Record recod;
            String destc;
            String desto;
            String origc;
            String origo;
            int nod;
            int noa;
            int ncd;
            int nca;
            Object[] diff;
            /**
             * Transitions for deletedCCR.
             */
            ite1 = deletedCCRTime.keySet().iterator();
            while (ite1.hasNext()) {
                aID = ite1.next();
                lcd = deletedCCRTime.get(aID);
                ncd = checkList(lcd, aID, "deleted");
                /**
                 * Has it also been added?
                 */
                if (addedCCRTime.containsKey(aID)) {
                    lca = addedCCRTime.get(aID);
                    printDiff("CCR", printDiff, aID, lcd, lca);
                    nca = checkList(lca, aID, "added");
                    if (nca == ncd) {
                        // Iterate over each deletion.
                        itec1 = lcd.iterator();
                        itec2 = lca.iterator();
                        while (itec1.hasNext()) {
                            reccd = (LR_CC_COU_Record) itec1.next();
                            recca = (LR_CC_COU_Record) itec2.next();
                            diff = difference(reccd, recca, printDiff);
                            if ((Boolean) diff[2]) {
                                /**
                                 * Figure out if the cc change is also foreign
                                 * ownership change and record which country it
                                 * changed from and to.
                                 */
                                origc = reccd.getCountryIncorporated1();
                                destc = recca.getCountryIncorporated1();
                                if (deletedOCRTime.containsKey(aID)) {
                                    lod = deletedOCRTime.get(aID);
                                    nod = checkList(lod, aID, "deleted");
                                    if (addedOCRTime.containsKey(aID)) {
                                        loa = addedOCRTime.get(aID);
                                        noa = checkList(loa, aID, "added");
                                        if (noa == nod && nca == noa) {
                                            tt = "";
                                            iteo1 = lod.iterator();
                                            iteo2 = loa.iterator();
                                            while (iteo1.hasNext()) {
                                                recod = iteo1.next();
                                                recoa = iteo2.next();
                                                origo = recod.getCountryIncorporated1();
                                                desto = recoa.getCountryIncorporated1();
                                                tt = addTransition(tt, "CU_OU__" + origc + "_" + destc + "__" + origo + "_" + desto, d);
                                                fullCCR.put(aID, recca);
                                                fullOCR.put(aID, recoa);
                                                addToMappedTransitions(aID, mTs, ts, tt, tts, ttc, lod, d);
                                                if (itec1.hasNext()) {
                                                    reccd = itec1.next();
                                                    recca = itec2.next();
                                                    origc = reccd.getCountryIncorporated1();
                                                    destc = recca.getCountryIncorporated1();
                                                }
                                            }
                                            deletedOCRTime.remove(aID);
                                            addedOCRTime.remove(aID);
                                        } else {
                                            int debug = 1;
                                            tt = "?";
                                        }
                                    } else {
                                        if (nod == 1) {
                                            origo = lod.get(0).getCountryIncorporated1();
                                            tt = "CU_OD__" + origc + "_" + destc + "__" + origo;
                                            fullCCR.put(aID, recca);
                                            fullOCR.remove(aID);
                                        } else {
                                            int debug = 1;
                                            tt = "?";
                                        }
                                    }
                                } else {
                                    if (addedOCRTime.containsKey(aID)) {
                                        loa = addedOCRTime.get(aID);
                                        noa = checkList(loa, aID, "added");
                                        if (noa > 1) {
                                            int debug = 1;
                                            tt = "?";
                                        } else {
                                            addedOCRTime.remove(aID);
                                            recoa = loa.get(0);
                                            origo = recoa.getCountryIncorporated1();
                                            tt = "CU_OA__" + origc + "_" + destc + "__" + origo;
                                            fullCCR.put(aID, recca);
                                            fullOCR.put(aID, recoa);
                                        }
                                    } else {
                                        tt = "CU__" + origc + "_" + destc;
                                        fullCCR.put(aID, recca);
                                    }
                                }
                                addToMappedTransitions(aID, mTs, ts, tt, tts, ttc, lcd, d);
                            } else {
                                // Remove from added (changes not significant).
                                if (addedCCRTime.containsKey(aID)) {
                                    fullCCR.put(aID, recca);
                                    addedCCRTime.remove(aID);
                                } else {
                                    int debug = 1;
                                }
                            }
                        }
                    } else {
                        if (nca > ncd) {
                            tt = "";
                            itec1 = lca.iterator();
                            itec2 = lcd.iterator();
                            while (itec1.hasNext()) {
                                recca = itec1.next();
                                origc = recca.getCountryIncorporated1();

                                String date;
                                date = recca.getChangeDate();
                                System.out.println(date);

                                if (fullCCR.containsKey(aID)) {
                                    LR_CC_FULL_Record r;
                                    r = fullCCR.get(aID);
                                    //diff = difference(r, recca, printDiff);

                                    System.out.println("Difference between existing and added:");

                                    diff = difference(r, recca, true);
                                    if ((Boolean) diff[2]) {
                                        int debug = 1;
                                    } else {
                                        int debug = 1;
                                    }
                                }
                                tt = addTransition(tt, "CA__" + origc, d);
                                fullCCR.put(aID, recca);
                                if (itec2.hasNext()) {
                                    reccd = itec2.next();
                                    origc = reccd.getCountryIncorporated1();

                                    String date1;
                                    date1 = reccd.getChangeDate();
                                    System.out.println(date1);

                                    recca = itec1.next();
                                    destc = recca.getCountryIncorporated1();

                                    System.out.println("Difference between deleted and added:");
                                    diff = difference(reccd, recca, true);
                                    if ((Boolean) diff[2]) {
                                        int debug = 1;
                                    } else {
                                        int debug = 1;
                                    }

                                    String date2;
                                    date2 = recca.getChangeDate();
                                    System.out.println(date2);

                                    tt = addTransition(tt, "CU__" + origc + "_" + destc, d);

                                    System.out.println(tt);

                                    fullCCR.put(aID, recca);
                                }

                                System.out.println(tt);

                            }
                            if (fullOCR.containsKey(aID)) {
                                int debug = 1;
                            }
                            if (deletedOCRTime.containsKey(aID)) {
                                int debug = 1;
                            }
                            if (addedOCRTime.containsKey(aID)) {
                                int debug = 1;
                            }
                            addToMappedTransitions(aID, mTs, ts, tt, tts, ttc, lcd, d);
                        } else {
                            if (ncd > nca) {
                                if (fullCCR.containsKey(aID)) {
                                    int debug = 1;
                                } else {
                                    int debug = 1;
                                }
                            }
                            addedCCRTime.remove(aID);
                        }
                    }
                } else {
                    if (ncd == 1) {
                        origc = lcd.get(0).getCountryIncorporated1();
                        if (deletedOCRTime.containsKey(aID)) {
                            lod = deletedOCRTime.get(aID);
                            nod = checkList(lod, aID, "deleted");
                            if (addedOCRTime.containsKey(aID)) {
                                loa = addedOCRTime.get(aID);
                                noa = checkList(loa, aID, "added");
                                if (nod == noa && nod == 1) {
                                    origo = lod.get(0).getCountryIncorporated1();
                                    recoa = loa.get(0);
                                    desto = recoa.getCountryIncorporated1();
                                    tt = "CD_OU__" + origc + "__" + origo + "_" + desto;
                                    fullCCR.remove(aID);
                                    fullOCR.put(aID, recoa);
                                } else {
                                    int debug = 1;
                                    tt = "?";
                                }
                            } else {
                                if (nod == 1) {
                                    origo = lod.get(0).getCountryIncorporated1();
                                    tt = "CD_OD__" + origc + "__" + origo;
                                    fullCCR.remove(aID);
                                    fullOCR.remove(aID);
                                } else {
                                    int debug = 1;
                                    tt = "?";
                                }
                            }
                            deletedOCRTime.remove(aID);
                        } else {
                            tt = "CD__" + origc;
                            fullCCR.remove(aID);
                        }
                        addToMappedTransitions(aID, mTs, ts, tt, tts, ttc, lcd, d);
                    } else {
                        if (deletedOCRTime.containsKey(aID)) {
                            lod = deletedOCRTime.get(aID);
                            nod = checkList(lod, aID, "deleted");
                            if (addedOCRTime.containsKey(aID)) {
                                loa = addedOCRTime.get(aID);
                                noa = checkList(loa, aID, "added");
                                int debug = 1;
                            } else {
                                int debug = 1;
                            }
                        } else {
                            if (addedOCRTime.containsKey(aID)) {
                                loa = addedOCRTime.get(aID);
                                noa = checkList(loa, aID, "added");
                                int debug = 1;
                            } else {
                                int debug = 1;
                            }
                        }
                    }
                }
            }
            // Transitions for deletedOCR
            System.out.println("There are " + deletedOCRTime.size() + " deletedOCRTime records to process");
            ite1 = deletedOCRTime.keySet().iterator();
            while (ite1.hasNext()) {
                aID = ite1.next();
                lod = deletedOCRTime.get(aID);
                if (addedOCRTime.containsKey(aID)) {
                    loa = addedOCRTime.get(aID);
                    printDiff("OCR", printDiff, aID, lod, loa);
                    nod = checkList(lod, aID, "deleted");
                    noa = checkList(loa, aID, "added");
                    if (noa == nod) {
                        iteo1 = lod.iterator();
                        iteo2 = loa.iterator();
                        while (iteo2.hasNext()) {
                            recod = iteo1.next();
                            recoa = iteo2.next();
                            diff = difference(recod, recoa, printDiff);
                            if ((Boolean) diff[2]) {
                                origo = recod.getCountryIncorporated1();
                                desto = recoa.getCountryIncorporated1();
                                if (addedCCRTime.containsKey(aID)) {
                                    lca = addedCCRTime.get(aID);
                                    nca = checkList(lca, aID, "added");
                                    if (nca == 1) {
                                        addedCCRTime.remove(aID);
                                        recca = (LR_CC_COU_Record) lca.get(0);
                                        destc = recca.getCountryIncorporated1();
                                        tt = "CA_OU__" + destc + "__" + origo + "_" + desto;
                                        fullCCR.put(aID, recca);
                                        fullOCR.put(aID, recoa);
                                    } else {
                                        int debug = 1;
                                        tt = "?";
                                    }
                                } else {
                                    tt = "OU__" + origo + "_" + desto;
                                    fullOCR.put(aID, recoa);
                                }
                                addToMappedTransitions(aID, mTs, ts, tt, tts, ttc, lod, d);
                            } else {
                                fullOCR.put(aID, recoa);
                            }
                        }
                    } else {
                        int debug = 1;
                    }
                } else {
                    nod = checkList(lod, aID, "deleted");
                    if (nod == 1) {
                        recod = (LR_OC_COU_Record) lod.get(0);
                        origo = recod.getCountryIncorporated1();
                        if (deletedCCRTime.containsKey(aID)) {
                            lcd = deletedCCRTime.get(aID);
                            ncd = checkList(lcd, aID, "deleted");
                            if (ncd == 1) {
                                origc = lod.get(0).getCountryIncorporated1();
                                if (addedCCRTime.containsKey(aID)) {
                                    lca = addedCCRTime.get(aID);
                                    nca = checkList(lca, aID, "added");
                                    if (nca == 1) {
                                        recca = lca.get(0);
                                        destc = recca.getCountryIncorporated1();
                                        tt = "CU_OD__" + origc + "_" + destc + "__" + origo;
                                        fullCCR.put(aID, recca);
                                        fullOCR.remove(aID);
                                    } else {
                                        int debug = 1;
                                        tt = "?";
                                    }
                                } else {
                                    tt = "CD_OD__" + origc + "__" + origo;
                                    fullCCR.remove(aID);
                                    fullOCR.remove(aID);
                                }
                            } else {
                                int debug = 1;
                                tt = "?";
                            }
                        } else {
                            if (addedCCRTime.containsKey(aID)) {
                                lca = addedCCRTime.get(aID);
                                nca = checkList(lca, aID, "added");
                                if (nca == 1) {
                                    recca = lca.get(0);
                                    destc = recca.getCountryIncorporated1();
                                    tt = "CA_OD__" + destc + "__" + origo;
                                    fullCCR.put(aID, recca);
                                } else {
                                    int debug = 1;
                                    tt = "?";
                                }
                            } else {
                                tt = "OD__" + origo;
                                fullOCR.remove(aID);
                            }
                        }
                    } else {
                        int debug = 1;
                        tt = "?";
                    }
                    addToMappedTransitions(aID, mTs, ts, tt, tts, ttc, lod, d);
                }
                fullOCR.remove(aID);
            }

            HashSet<LR_ID> done;
            done = new HashSet<>();
            // Transitions for addedCCR
            System.out.println("There are up to " + addedCCRTime.size() + " addedCCRTime records to process");
            ite1 = addedCCRTime.keySet().iterator();
            while (ite1.hasNext()) {
                aID = ite1.next();
                lca = addedCCRTime.get(aID);
                nca = checkList(lca, aID, "added");
                if (addedOCRTime.containsKey(aID)) {
                    loa = addedOCRTime.get(aID);
                    noa = checkList(loa, aID, "added");
                    if (nca == noa && nca == 1) {
                        recca = lca.get(0);
                        recoa = loa.get(0);
                        destc = recca.getCountryIncorporated1();
                        desto = recoa.getCountryIncorporated1();
                        tt = "CA_OA__" + destc + "__" + desto;
                        addedOCRTime.remove(aID);
                        fullCCR.put(aID, recca);
                        fullOCR.put(aID, recoa);
                    } else {
                        int debug = 1;
                        tt = "?";
                    }
                } else {
                    switch (nca) {
                        case 1:
                            recca = lca.get(0);
                            destc = recca.getCountryIncorporated1();
                            tt = "CA__" + destc;
                            fullCCR.put(aID, recca);
                            break;
                        default:
                            itec1 = lca.iterator();
                            recca = itec1.next();
                            origc = recca.getCountryIncorporated1();
                            tt = "CA__" + origc;
                            LR_CC_COU_Record recca2;
                            while (itec1.hasNext()) {
                                recca2 = itec1.next();
                                diff = difference(recca, recca2, printDiff);
                                if ((Boolean) diff[2]) {
                                    origc = recca.getCountryIncorporated1();
                                    tt = addTransition(tt, "CA__" + origc, d);
                                    fullCCR.put(aID, recca2);
                                } else {
                                    fullCCR.put(aID, recca);
                                }
                                recca = recca2;
                            }
                            break;
                    }
                }
                addToMappedTransitions(aID, mTs, ts, tt, tts, ttc, lca, d);
            }
            // Transitions for addedOCR
            System.out.println("There are up to " + addedOCRTime.size() + " addedOCRTime records to process");
            ite1 = addedOCRTime.keySet().iterator();
            while (ite1.hasNext()) {
                aID = ite1.next();
                tt = "";
                loa = addedOCRTime.get(aID);
                iteo1 = loa.iterator();
                while (iteo1.hasNext()) {
                    recoa = iteo1.next();
                    destc = recoa.getCountryIncorporated1();
                    tt = addTransition(tt, "OA__" + destc, d);
                    fullOCR.put(aID, recoa);
                }
                addToMappedTransitions(aID, mTs, ts, tt, tts, ttc, loa, d);
            }
        }
        // Write out transition results
        pw.println("Count,TransitionType");
        ites0 = ttc.keySet().iterator();
        while (ites0.hasNext()) {
            tt = ites0.next();
            transitionTypeCount = ttc.get(tt);
            if (transitionTypeCount > 0) {
                pw.println("" + transitionTypeCount + "," + tt);
            }
        }
        pw.close();
    }

    protected void updateIDToProprietorNameIDs(LR_ID2 ID,
            LR_ValueID proprietorName1ID, LR_ValueID proprietorName2ID,
            LR_ValueID proprietorName3ID, LR_ValueID proprietorName4ID) {
        if (proprietorName4ID != null) {
            Generic_Collections.addToListIfDifferentFromLast(IDToProprietorNameIDs,
                    ID, proprietorName4ID);
        }
        if (proprietorName3ID != null) {
            Generic_Collections.addToListIfDifferentFromLast(IDToProprietorNameIDs,
                    ID, proprietorName3ID);
        }
        if (proprietorName4ID != null) {
            Generic_Collections.addToListIfDifferentFromLast(IDToProprietorNameIDs,
                    ID, proprietorName2ID);
        }
        if (proprietorName1ID != null) {
            Generic_Collections.addToListIfDifferentFromLast(IDToProprietorNameIDs,
                    ID, proprietorName1ID);
        }
    }

    /**
     * Updates key lookups.
     *
     * @param companyRegistrationNo1ID
     * @param companyRegistrationNo2ID
     * @param companyRegistrationNo3ID
     * @param companyRegistrationNo4ID
     * @param titleNumberID
     * @param proprietorName1ID
     * @param proprietorName2ID
     * @param proprietorName3ID
     * @param proprietorName4ID
     * @param titleNumberIDToCompanyRegistrationNoID TitleNumberID to
     * CompanyRegistrationNoID lookup.
     * @param companyRegistrationNoIDToTitleNumberIDs CompanyRegistrationNoID to
     * TitleNumberID lookup.
     * @param proprietorNameIDToCompanyRegistrationNoIDs ProprietorNameID to
     * CompanyRegistrationNoID lookup.
     * @param companyRegistrationNoIDToProprietorNameIDs CompanyRegistrationNoID
     * to ProprietorNameID lookup.
     */
    protected void updateKeyLookups(LR_ID2 ID, LR_ValueID companyRegistrationNo1ID,
            LR_ValueID companyRegistrationNo2ID, LR_ValueID companyRegistrationNo3ID,
            LR_ValueID companyRegistrationNo4ID, LR_ValueID titleNumberID,
            LR_ValueID proprietorName1ID, LR_ValueID proprietorName2ID,
            LR_ValueID proprietorName3ID, LR_ValueID proprietorName4ID,
            HashMap<LR_ValueID, ArrayList<LR_ValueID>> titleNumberIDToCompanyRegistrationNoID,
            HashMap<LR_ValueID, HashSet<LR_ValueID>> companyRegistrationNoIDToTitleNumberIDs,
            HashMap<LR_ValueID, ArrayList<LR_ValueID>> proprietorNameIDToCompanyRegistrationNoIDs,
            HashMap<LR_ValueID, ArrayList<LR_ValueID>> companyRegistrationNoIDToProprietorNameIDs) {
        ArrayList<LR_ValueID> titleNumberCompanyRegistrationNoIDs = null;
        HashSet<LR_ValueID> titleNumberIDs;
        ArrayList<LR_ValueID> proprietorNameCompanyRegistrationNoIDs;
        LR_ValueID proprietorNameID;
        // Update titleNumberIDToCompanyRegistrationNoID
        if (titleNumberID == null) {
            // count?
            int debug = 1;
        } else {
            if (titleNumberIDToCompanyRegistrationNoID.containsKey(titleNumberID)) {
                titleNumberCompanyRegistrationNoIDs = titleNumberIDToCompanyRegistrationNoID.get(titleNumberID);
            } else {
                titleNumberCompanyRegistrationNoIDs = new ArrayList<>();
                titleNumberIDToCompanyRegistrationNoID.put(titleNumberID, titleNumberCompanyRegistrationNoIDs);
            }
        }
        // Update companyRegistrationNoIDToTitleNumberID and get init titleNumberIDs
        int nNullCompanyRegistrationNo1ID;
        nNullCompanyRegistrationNo1ID = addValue(
                companyRegistrationNo1ID, titleNumberCompanyRegistrationNoIDs);
        titleNumberIDs = getAndAddToSet(companyRegistrationNo1ID,
                companyRegistrationNoIDToTitleNumberIDs);
        int nNullCompanyRegistrationNo2ID;
        nNullCompanyRegistrationNo2ID = addValue(
                companyRegistrationNo2ID, titleNumberCompanyRegistrationNoIDs);
        companyRegistrationNoIDToTitleNumberIDs.put(companyRegistrationNo2ID, titleNumberIDs);
        int nNullCompanyRegistrationNo3ID;
        nNullCompanyRegistrationNo3ID = addValue(
                companyRegistrationNo3ID, titleNumberCompanyRegistrationNoIDs);
        companyRegistrationNoIDToTitleNumberIDs.put(companyRegistrationNo3ID, titleNumberIDs);
        int nNullCompanyRegistrationNo4ID;
        nNullCompanyRegistrationNo4ID = addValue(
                companyRegistrationNo4ID, titleNumberCompanyRegistrationNoIDs);
        companyRegistrationNoIDToTitleNumberIDs.put(companyRegistrationNo4ID, titleNumberIDs);
        titleNumberIDs.add(titleNumberID);

        // Update proprietorNameIDToCompanyRegistrationNoID
        if (proprietorNameIDToCompanyRegistrationNoIDs.containsKey(proprietorName1ID)) {
            proprietorNameCompanyRegistrationNoIDs = proprietorNameIDToCompanyRegistrationNoIDs.get(proprietorName1ID);
        } else {
            proprietorNameCompanyRegistrationNoIDs = new ArrayList<>();
            proprietorNameIDToCompanyRegistrationNoIDs.put(proprietorName1ID, proprietorNameCompanyRegistrationNoIDs);
        }
        int nNullProprietorName1ID;
        nNullProprietorName1ID = addValue(
                proprietorNameIDToCompanyRegistrationNoIDs, proprietorName1ID,
                companyRegistrationNo1ID);
        proprietorNameCompanyRegistrationNoIDs = getAndAddToArrayList(
                proprietorName1ID, proprietorNameIDToCompanyRegistrationNoIDs);
        int nNullProprietorName2ID;
        nNullProprietorName2ID = addValue(
                proprietorNameIDToCompanyRegistrationNoIDs, proprietorName2ID,
                companyRegistrationNo2ID);
        proprietorNameIDToCompanyRegistrationNoIDs.put(proprietorName2ID,
                proprietorNameCompanyRegistrationNoIDs);
        int nNullProprietorName3ID;
        nNullProprietorName3ID = addValue(
                proprietorNameIDToCompanyRegistrationNoIDs, proprietorName3ID,
                companyRegistrationNo3ID);
        proprietorNameIDToCompanyRegistrationNoIDs.put(proprietorName3ID,
                proprietorNameCompanyRegistrationNoIDs);
        int nNullProprietorName4ID;
        nNullProprietorName4ID = addValue(
                proprietorNameIDToCompanyRegistrationNoIDs, proprietorName4ID,
                companyRegistrationNo4ID);
        proprietorNameIDToCompanyRegistrationNoIDs.put(proprietorName4ID,
                proprietorNameCompanyRegistrationNoIDs);

        // Update companyRegistrationNoIDToProprietorNameIDs
        // CompanyRegistrationNo1ID
        addValueAndReportDifferenceInAddedValueAndTheLastInTheList(
                companyRegistrationNoIDToProprietorNameIDs,
                companyRegistrationNo1ID, proprietorName1ID);
        // CompanyRegistrationNo2ID
        addValueAndReportDifferenceInAddedValueAndTheLastInTheList(
                companyRegistrationNoIDToProprietorNameIDs,
                companyRegistrationNo2ID, proprietorName2ID);
        // CompanyRegistrationNo3ID
        addValueAndReportDifferenceInAddedValueAndTheLastInTheList(
                companyRegistrationNoIDToProprietorNameIDs,
                companyRegistrationNo3ID, proprietorName3ID);
        // CompanyRegistrationNo4ID
        addValueAndReportDifferenceInAddedValueAndTheLastInTheList(
                companyRegistrationNoIDToProprietorNameIDs,
                companyRegistrationNo4ID, proprietorName4ID);

    }

    protected int addValue(LR_ValueID valueID,
            ArrayList<LR_ValueID> l) {
        int result;
        if (valueID == null) {
            result = 1;
        } else {
            l.add(valueID);
            result = 0;
        }
        return result;
    }

    protected int addValue(LR_ValueID value, LR_ValueID key,
            HashMap<LR_ValueID, HashSet<LR_ValueID>> m) {
        int result;
        if (value == null) {
            result = 1;
        } else {
            HashSet<LR_ValueID> valueIDs;
            if (m.containsKey(key)) {
                valueIDs = m.get(key);
            } else {
                valueIDs = new HashSet<>();
            }
            valueIDs.add(value);
            result = 0;
        }
        return result;
    }

    protected int addValue(
            HashMap<LR_ValueID, ArrayList<LR_ValueID>> m,
            LR_ValueID value, LR_ValueID key) {
        int result;
        if (value == null) {
            result = 1;
        } else {
            Generic_Collections.addToListIfDifferentFromLast(m, key, value);
            result = 0;
        }
        return result;
    }

    /**
     *
     * @param valueID
     * @param m
     * @return
     */
    protected HashSet<LR_ValueID> getAndAddToSet(LR_ValueID valueID,
            HashMap<LR_ValueID, HashSet<LR_ValueID>> m) {
        HashSet<LR_ValueID> result;
        if (m.containsKey(valueID)) {
            result = m.get(valueID);
        } else {
            result = new HashSet<>();
            m.put(valueID, result);
        }
        return result;
    }

    /**
     *
     * @param valueID
     * @param m
     * @return
     */
    protected ArrayList<LR_ValueID> getAndAddToArrayList(LR_ValueID valueID,
            HashMap<LR_ValueID, ArrayList<LR_ValueID>> m) {
        ArrayList<LR_ValueID> result;
        if (m.containsKey(valueID)) {
            result = m.get(valueID);
        } else {
            result = new ArrayList<>();
            m.put(valueID, result);
        }
        return result;
    }

    /**
     * adds value into the values of m for key.
     *
     * @param m
     * @param key
     * @param value
     */
    public void addValueAndReportDifferenceInAddedValueAndTheLastInTheList(
            HashMap<LR_ValueID, ArrayList<LR_ValueID>> m,
            LR_ValueID key, LR_ValueID value) {
        if (key != null) {
            LR_ValueID v2;
            ArrayList<LR_ValueID> l;
            if (m.containsKey(key)) {
                String s;
                s = key.getValue();
                l = m.get(key);
                v2 = l.get(l.size() - 1);
                if (v2 != value) {
                    if (!v2.equals(value)) {
                        // This is unexpected, the proprietor name has changed!
                        reportProprietorNameChange(s, v2, value);
                    }
                }
            } else {
                v2 = value;
                l = new ArrayList<>();
                l.add(v2);
                m.put(key, l);
            }
        }
    }

    protected String addTransition(String t0, String t1, String delimeter) {
        if (t0.isEmpty()) {
            return t1;
        } else {
            return t0 + delimeter + t1;

        }
    }

    protected void printDiff(String type, boolean printDiff, LR_ID2 aID,
            ArrayList<? extends LR_Record> ld, ArrayList<? extends LR_Record> la) {
        if (printDiff) {
            String m;
            m = "Deleted " + type
                    + " is also added, so this is really updated.";
            System.out.println(m);
            System.out.println("Current:");
            boolean b;
            b = fullOCR.containsKey(aID);
            System.out.println("fullOCR.containsKey(aID): " + b);
            LR_OC_FULL_Record ocf;
            ocf = fullOCR.get(aID);
            System.out.println(ocf.toString());
            b = fullCCR.containsKey(aID);
            System.out.println("fullCCR.containsKey(aID): " + b);
            LR_CC_FULL_Record ccf;
            ccf = fullCCR.get(aID);
            System.out.println(ccf.toString());
            Iterator<? extends LR_Record> ite;
            System.out.println("Deleted:");
            ite = ld.iterator();
            while (ite.hasNext()) {
                m = ite.next().toString();
                System.out.println(m);
            }
            System.out.println("Added:");
            ite = la.iterator();
            while (ite.hasNext()) {
                m = ite.next().toString();
                System.out.println(m);

            }
        }
    }

    /**
     *
     * @param l
     * @param aID
     * @param name
     * @return
     */
    protected int checkList(ArrayList<? extends LR_Record> l, LR_ID2 aID, String name) {
        int result;
        result = l.size();
        if (result > 1) {
            String m;
            m = "Record with ID " + aID
                    + " " + name
                    + " " + result
                    + " times (more than once in a time step)!";
            System.out.println(m);

        }
        return result;

    }

    /**
     *
     * @param aID
     * @param mappedTransitions
     * @param transitions
     * @param transitionType0 OC Overseas Corporate, HC Home Corporate, FO
     * Foreign Ownership
     * @param transitionTypes
     * @param transitionTypeCounts
     * @param l
     * @param delimeter
     */
    protected void addToMappedTransitions(LR_ID2 aID,
            HashMap<LR_ID2, ArrayList<ArrayList<? extends LR_Record>>> mappedTransitions,
            ArrayList<ArrayList<? extends LR_Record>> transitions,
            String transitionType0,
            HashMap<LR_ID2, String> transitionTypes,
            TreeMap<String, Integer> transitionTypeCounts,
            ArrayList<? extends LR_Record> l,
            String delimeter
    ) {
        String transitionType;
        if (mappedTransitions.containsKey(aID)) {
            transitions = mappedTransitions.get(aID);
            transitionType = transitionTypes.get(aID);
            transitionTypes.remove(aID);
            Generic_Collections.addToMap(transitionTypeCounts, transitionType, -1);
        } else {
            transitions = new ArrayList<>();
            mappedTransitions.put(aID, transitions);
            transitionType = "";
        }
        if (!transitionType.isEmpty()) {
            transitionType += delimeter;
        }
        transitionType += (transitionType0.replaceAll(",", "-")).replaceAll(" ", "");
        transitionTypes.put(aID, transitionType);
        transitions.add(l);
        Generic_Collections.addToMap(transitionTypeCounts, transitionType, 1);
    }

    /**
     * Explore changes
     *
     * @param f
     * @param c
     * @param printDiff
     * @return Object[5] result: result[0] = Integer (change count); result[1] =
     * Boolean (true iff TitleNumber has changed; result[2] = Boolean (true iff
     * there has been some form of ownership change); result[3] = Boolean (true
     * iff Tenure has changed); result[4] = null.
     */
    public Object[] difference(LR_Record f, LR_Record c, boolean printDiff) {
        boolean updateIDs = false;
        Object[] result;
        result = new Object[5];
        boolean titleNumberChange;
        boolean ownershipChange;
        boolean tenureChange;
        titleNumberChange = false;
        ownershipChange = false;
        tenureChange = false;
        String s0;
        String s1;
        String s3;
        int changeCount;
        changeCount = 0;
        s0 = f.getTitleNumber();
        s1 = c.getTitleNumber();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setTitleNumber(s1);
            titleNumberChange = doDiff(LR_Strings.s_TitleNumber, s0, s1, printDiff, titleNumberChange);
            changeCount++;
        }
        s0 = f.getCompanyRegistrationNo1();
        s1 = c.getCompanyRegistrationNo1();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setCompanyRegistrationNo1(s1);
            ownershipChange = doDiff(LR_Strings.s_CompanyRegistrationNo1, s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        s0 = f.getCompanyRegistrationNo2();
        s1 = c.getCompanyRegistrationNo2();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setCompanyRegistrationNo2(s1, updateIDs);
            ownershipChange = doDiff(LR_Strings.s_CompanyRegistrationNo2, s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        s0 = f.getCompanyRegistrationNo3();
        s1 = c.getCompanyRegistrationNo3();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setCompanyRegistrationNo3(s1, updateIDs);
            ownershipChange = doDiff(LR_Strings.s_CompanyRegistrationNo3, s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        s0 = f.getCompanyRegistrationNo4();
        s1 = c.getCompanyRegistrationNo4();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setCompanyRegistrationNo4(s1, updateIDs);
            ownershipChange = doDiff(LR_Strings.s_CompanyRegistrationNo4, s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        Long l0;
        Long l1;
        l0 = f.getPricePaidValue();
        l1 = c.getPricePaidValue();
        if (l0 == null) {
            if (l1 == null) {
            } else {
                s3 = "PricePaid changed from null to " + l1
                        + " perhaps look in sales data?!";
            }
        } else {
            if (l1 == null) {
                s3 = "PricePaid changed from " + l0
                        + " to null!";
            } else {
                if (l0.longValue() != l1.longValue()) {
                    if (printDiff) {
                        s3 = "PricePaid changed from " + l0 + " to " + l1
                                + " perhaps look in sales data?!";
                        System.out.println(s3);
                    }
                }
            }
            changeCount++;
        }
        s0 = f.getTenure();
        s1 = c.getTenure();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setTenure(s1);
            tenureChange = doDiff(LR_Strings.s_Tenure, s0, s1, printDiff, tenureChange);
            changeCount++;
        }
        // MultipleAddressIndicator
        s0 = f.getMultipleAddressIndicator();
        s1 = c.getMultipleAddressIndicator();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setMultipleAddressIndicator(s1);
            doDiff("MultipleAddressIndicator", s0, s1, printDiff, true);
            changeCount++;
        }
        s0 = f.getPostcode();
        s1 = c.getPostcode();
        if (!s0.equalsIgnoreCase(s1)) {
            //f.initPostcodeAndPostcodeDistrict(s1);
            doDiff(LR_Strings.s_Postcode, s0, s1, printDiff, true);
            changeCount++;
        }
        s0 = f.getPropertyAddress();
        s1 = c.getPropertyAddress();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setPropertyAddress(s1);
            doDiff(LR_Strings.s_PropertyAddress, s0, s1, printDiff, true);
            changeCount++;
        }
        // ProprietorNames
        s0 = f.getProprietorName1();
        s1 = c.getProprietorName1();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setProprietorName1(s1);
            ownershipChange = doDiff(LR_Strings.s_ProprietorName1, s0, s1, printDiff, ownershipChange);
            changeCount++;

        }
        s0 = f.getProprietorName2();
        s1 = c.getProprietorName2();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setProprietorName2(s1, updateIDs);
            ownershipChange = doDiff(LR_Strings.s_ProprietorName2, s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        s0 = f.getProprietorName3();
        s1 = c.getProprietorName3();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setProprietorName3(s1, updateIDs);
            ownershipChange = doDiff(LR_Strings.s_ProprietorName3, s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        s0 = f.getProprietorName4();
        s1 = c.getProprietorName4();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setProprietorName4(s1, updateIDs);
            ownershipChange = doDiff(LR_Strings.s_ProprietorName4, s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        //ProprietorshipCategory
        s0 = f.getProprietorshipCategory1();
        s1 = c.getProprietorshipCategory1();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setProprietorshipCategory1(s1);
            ownershipChange = doDiff(LR_Strings.s_ProprietorshipCategory1, s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        s0 = f.getProprietorshipCategory2();
        s1 = c.getProprietorshipCategory2();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setProprietorshipCategory2(s1, updateIDs);
            ownershipChange = doDiff(LR_Strings.s_ProprietorshipCategory2, s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        s0 = f.getProprietorshipCategory3();
        s1 = c.getProprietorshipCategory3();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setProprietorshipCategory3(s1, updateIDs);
            ownershipChange = doDiff(LR_Strings.s_ProprietorshipCategory3, s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        s0 = f.getProprietorshipCategory4();
        s1 = c.getProprietorshipCategory4();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setProprietorshipCategory4(s1, updateIDs);
            ownershipChange = doDiff(LR_Strings.s_ProprietorshipCategory4, s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        result[0] = changeCount;
        result[1] = titleNumberChange;
        result[2] = ownershipChange;
        result[3] = tenureChange;
        return result;
    }

    /**
     * Report the ProprietorName change.
     *
     * @param companyRegistrationNo
     * @param ProprietorName0ID
     * @param ProprietorName1ID
     */
    protected void reportProprietorNameChange(String companyRegistrationNo,
            LR_ValueID ProprietorName0ID, LR_ValueID ProprietorName1ID) {
        if (!companyRegistrationNo.isEmpty()) {
            String s;
            String proprietorName0;
            String proprietorName1;
            proprietorName0 = ProprietorName0ID.getValue();
            proprietorName1 = ProprietorName1ID.getValue();
            s = "Proprietor name for company registration number \""
                    + companyRegistrationNo + "\" changed from \""
                    + proprietorName0 + "\" to \"" + proprietorName1 + "\"";
            System.out.println(s);

        }
    }

    protected boolean doDiff(String name, String s0, String s1, boolean printDiff, boolean change) {
        if (!s0.trim().isEmpty()) {
            if (printDiff) {
                String s;
                s = name + " changed from " + s0 + " to " + s1;
                System.out.println(s);
            }
            change = true;
        }
        return change;

    }

    /**
     * Explore changes
     *
     * @param f
     * @param c
     * @param printDiff
     * @return Object[5] result: result[0] = Integer (change count); result[1] =
     * Boolean (true iff TitleNumber has changed; result[2] = Boolean (true iff
     * there has been some form of ownership change); result[3] = Boolean (true
     * iff Tenure has changed); result[4] = null.
     */
    public Object[] difference(LR_CC_FULL_Record f,
            LR_CC_COU_Record c,
            boolean printDiff) {
        //String s0;
        //String s1;
        Object[] result;
        String s3;
        int changeCount;
        result = difference((LR_Record) f, (LR_Record) c, printDiff);
        changeCount = (Integer) result[0];
        if (printDiff) {
            if (changeCount > 1) {
                s3 = "There were " + changeCount + " changes.";
                System.out.println(s3);
            }
        }
        return result;
    }

    /**
     * Explore changes
     *
     * @param f
     * @param c
     * @param printDiff
     * @return Object[5] result: result[0] = Integer (change count); result[1] =
     * Boolean (true iff TitleNumber has changed; result[2] = Boolean (true iff
     * there has been some form of ownership change); result[3] = Boolean (true
     * iff Tenure has changed); result[4] = Boolean (true iff one of Country
     * Incorporated has changed).
     */
    public Object[] difference(LR_OC_FULL_Record f, LR_OC_COU_Record c,
            boolean printDiff) {
        boolean updateIDs = false;
        String s0;
        String s1;
        String s3;
        boolean countryChanged;
        countryChanged = false;
        Object[] result;
        int changeCount;
        result = difference((LR_Record) f, (LR_Record) c, printDiff);
        changeCount = (Integer) result[0];
        // CountryIncorporated
        s0 = f.getCountryIncorporated1();
        s1 = c.getCountryIncorporated1();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setCountryIncorporated1(s1);
            if (!s0.trim().isEmpty()) {
                s3 = "CountryIncorporated1 changed from " + s0 + " to " + s1;
                System.out.println(s3);
                countryChanged = true;
            }
            changeCount++;
        }
        s0 = f.getCountryIncorporated2();
        s1 = c.getCountryIncorporated2();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setCountryIncorporated2(s1, updateIDs);
            if (!s0.trim().isEmpty()) {
                s3 = "CountryIncorporated2 changed from " + s0 + " to " + s1;
                System.out.println(s3);
                countryChanged = true;
            }
            changeCount++;
        }
        s0 = f.getCountryIncorporated3();
        s1 = c.getCountryIncorporated3();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setCountryIncorporated3(s1, updateIDs);
            if (!s0.trim().isEmpty()) {
                s3 = "CountryIncorporated3 changed from " + s0 + " to " + s1;
                System.out.println(s3);
                countryChanged = true;
            }
            changeCount++;
        }
        s0 = f.getCountryIncorporated4();
        s1 = c.getCountryIncorporated4();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setCountryIncorporated4(s1, updateIDs);
            if (!s0.trim().isEmpty()) {
                s3 = "CountryIncorporated4 changed from " + s0 + " to " + s1;
                System.out.println(s3);
                countryChanged = true;
            }
            changeCount++;
        }
        if (changeCount > 1) {
            s3 = "There were " + changeCount + " changes.";
            System.out.println(s3);
        }
        result[4] = countryChanged;
        return result;
    }

    void add(HashMap<LR_ID2, ArrayList<LR_CC_COU_Record>> added,
            HashMap<LR_ID2, ArrayList<LR_CC_COU_Record>> deleted,
            LR_CC_COU_Record r) {
        LR_ID2 aID;
        String changeIndicator;
        ArrayList<LR_CC_COU_Record> l;
        aID = r.getID();
        changeIndicator = r.getChangeIndicator();
        if (changeIndicator.equalsIgnoreCase("A")) {
            Generic_Collections.addToMap(addedCCRCount, aID, 1);
            if (added.containsKey(aID)) {
                l = added.get(aID);
//                System.out.println("CC Property with address "
//                        + r.getPropertyAddress()
//                        + " added multiple times in a given month!");
            } else {
                l = new ArrayList<>();
                added.put(aID, l);
            }
        } else {
            Generic_Collections.addToMap(deletedCCRCount, aID, 1);
            if (deleted.containsKey(aID)) {
                l = deleted.get(aID);
//                System.out.println("CC Property with address "
//                        + r.getPropertyAddress()
//                        + " deleted multiple times in a given month!");
            } else {
                l = new ArrayList<>();
                deleted.put(aID, l);
            }
        }
        l.add(r);
    }

    void add(HashMap<LR_ID2, ArrayList<LR_OC_COU_Record>> added,
            HashMap<LR_ID2, ArrayList<LR_OC_COU_Record>> deleted,
            LR_OC_COU_Record r) {
        LR_ID2 aID;
        String changeIndicator;
        ArrayList<LR_OC_COU_Record> l;
        aID = r.getID();
        changeIndicator = r.getChangeIndicator();
        if (changeIndicator.equalsIgnoreCase("A")) {
            Generic_Collections.addToMap(addedOCRCount, aID, 1);
            if (added.containsKey(aID)) {
                l = added.get(aID);
//                System.out.println("OC Property with address "
//                        + r.getPropertyAddress()
//                        + " added multiple times in a given month!");
            } else {
                l = new ArrayList<>();
                added.put(aID, l);
            }
        } else {
            Generic_Collections.addToMap(deletedOCRCount, aID, 1);
            if (deleted.containsKey(aID)) {
                l = deleted.get(aID);
//                System.out.println("OC Property with address "
//                        + r.getPropertyAddress()
//                        + " deleted multiple times in a given month!");
            } else {
                l = new ArrayList<>();
                deleted.put(aID, l);
            }
        }
        l
                .add(r
                );

    }

    void printGeneralisation(PrintWriter pw, int minCC, int minOC) {
        printGeneralisation(pw, "addedCCR", addedCCRCount, minCC);
        printGeneralisation(pw, "deletedCCR", deletedCCRCount, minCC);
        printGeneralisation(pw, "addedOCR", addedOCRCount, minOC);
        printGeneralisation(pw, "deletedOCR", deletedOCRCount, minOC);

    }

    void printGeneralisation(PrintWriter pw, String type,
            HashMap<LR_ID2, Integer> counts, int min) {
        pw.println(type);
        if (!counts.isEmpty()) {
            Map<LR_ID2, Integer> sortedCounts;
            sortedCounts = Generic_Collections.sortByValue(counts);
            LR_ID2 aID;
            int count;
            int smallCount = 0;
            boolean reportedSmallCount = false;
            Iterator<LR_ID2> ite;
            pw.println("Address, TitleNumber, Count");
            ite = sortedCounts.keySet().iterator();
            while (ite.hasNext()) {
                aID = ite.next();
                count = counts.get(aID);
                if (count >= min) {
                    if (!reportedSmallCount) {
                        pw.println("Those with less than " + min
                                + "," + smallCount);
                        reportedSmallCount = true;
                    }
                    if (aID != null) {
                        pw.println(
                                "\"" + aID.getPropertyAddressID().getValue() + "\",\""
                                + aID.getTitleNumberID().getValue() + "\"," + count);
                    } else {
                        System.out.println("null ID");
                    }
                } else {
                    smallCount += count;
                }
            }
        } else {
            System.out.println("Zero count for " + type);
        }
        pw.println();
    }

}
