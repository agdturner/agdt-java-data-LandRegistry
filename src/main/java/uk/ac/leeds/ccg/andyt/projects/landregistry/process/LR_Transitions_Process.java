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
import uk.ac.leeds.ccg.andyt.generic.io.Generic_ReadCSV;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.generic.utilities.Generic_Collections;
import uk.ac.leeds.ccg.andyt.generic.utilities.time.Generic_YearMonth;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Environment;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_ID;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_ID2;
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
    public TreeMap<Generic_YearMonth, HashMap<LR_ID, ArrayList<LR_ID>>> YMTitleNumberIDToCompanyRegistrationNoID;
    /**
     * This provides a store to look up TitleNumbers for a company at any given
     * YearMonth. If a company registration number is not known for the
     * YearMonth, then either it is for a company not yet seen in the data, or
     * it is for a company that has not gained any titles. To find the titles
     * the company has, then search backwards. If a company gains any titles,
     * all title numbers are added, so the search only needs to go back to find
     * a record.
     */
    public TreeMap<Generic_YearMonth, HashMap<LR_ID, HashSet<LR_ID>>> YMCompanyRegistrationNoIDToTitleNumberID;
    /**
     * This provides a store to look up company registration numbers from
     * proprietor names at any given YearMonth. (There may be multiple company
     * registration numbers for a single proprietor name!)
     */
    public TreeMap<Generic_YearMonth, HashMap<LR_ID, HashSet<LR_ID>>> YMProprietorNameIDToCompanyRegistrationNoID;
    /**
     * This provides a store to look up a proprietor names from company
     * registration numbers at any given YearMonth.
     */
    public TreeMap<Generic_YearMonth, HashMap<LR_ID, LR_ID>> YMCompanyRegistrationNoIDToProprietorNameID;

    /**
     * Set to true if TitleNumberID to CompanyRegistrationNoID lookups are
     * modified.
     */
    public boolean UpdatedTitleNumberCompanyRegistrationNoLookups;
    public boolean UpdatedProprietorNameIDCompanyRegistrationNoLookups;

    protected LR_Transitions_Process() {
        super();
    }

    public LR_Transitions_Process(LR_Environment env) {
        super(env);
    }

    HashMap<LR_ID2, Integer> addedCCRCount;
    HashMap<LR_ID2, Integer> deletedCCRCount;
    HashMap<LR_ID2, Integer> addedOCRCount;
    HashMap<LR_ID2, Integer> deletedOCRCount;
    HashMap<String, HashMap<LR_ID2, ArrayList<LR_CC_COU_Record>>> addedCCR;
    HashMap<String, HashMap<LR_ID2, ArrayList<LR_CC_COU_Record>>> deletedCCR;
    HashMap<String, HashMap<LR_ID2, ArrayList<LR_OC_COU_Record>>> addedOCR;
    HashMap<String, HashMap<LR_ID2, ArrayList<LR_OC_COU_Record>>> deletedOCR;
//    HashMap<String, HashMap<LR_ID, ArrayList<LR_Record>>> addedCCR;
//    HashMap<String, HashMap<LR_ID, ArrayList<LR_Record>>> deletedCCR;
//    HashMap<String, HashMap<LR_ID, ArrayList<LR_Record>>> addedOCR;
//    HashMap<String, HashMap<LR_ID, ArrayList<LR_Record>>> deletedOCR;

    HashMap<LR_ID2, LR_CC_FULL_Record> fullCCR;
    HashMap<LR_ID2, LR_OC_FULL_Record> fullOCR;

    public void run(String area, boolean doAll, File inputDataDir,
            int minCC, int minOC, boolean overwrite) {
        boolean printDiff;
//        printDiff = true;
        printDiff = false;

        File outputDataDir;
        outputDataDir = Files.getOutputDataDir(Strings);

        ArrayList<String> names0;
        //ArrayList<String> names1;
        ArrayList<String> names2 = null;
        String name;
        String name0;
        String name00;
        names0 = new ArrayList<>();
        //names1 = new ArrayList<>();
        names0.add(Env.Strings.S_CCOD);
        names0.add(Env.Strings.S_OCOD);
        boolean isCCOD;
        //names1.add("COU");
        //names1.add("FULL");
        
        boolean upDateIDs = true;

        YMTitleNumberIDToCompanyRegistrationNoID = new TreeMap<>();
        YMCompanyRegistrationNoIDToTitleNumberID = new TreeMap<>();
        YMProprietorNameIDToCompanyRegistrationNoID = new TreeMap<>();
        YMCompanyRegistrationNoIDToProprietorNameID = new TreeMap<>();
        HashMap<LR_ID, ArrayList<LR_ID>> TitleNumberIDToCompanyRegistrationNoID;
        HashMap<LR_ID, HashSet<LR_ID>> CompanyRegistrationNoIDToTitleNumberID;
        HashMap<LR_ID, HashSet<LR_ID>> ProprietorNameIDToCompanyRegistrationNoID;
        HashMap<LR_ID, LR_ID> CompanyRegistrationNoIDToProprietorNameID;

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
        PrintWriter pw = null;
        if (doAll) {
            outdir = new File(outputDataDir, Strings.S_Transitions);
        } else {
            outdir = new File(outputDataDir, area + Strings.S_Transitions);
            outdir = new File(outdir, Strings.S_Subsets);
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
        ym = new Generic_YearMonth(Env.ge, "2017-11");

        TitleNumberIDToCompanyRegistrationNoID = new HashMap<>();
        CompanyRegistrationNoIDToTitleNumberID = new HashMap<>();
        ProprietorNameIDToCompanyRegistrationNoID = new HashMap<>();
        CompanyRegistrationNoIDToProprietorNameID = new HashMap<>();
        YMTitleNumberIDToCompanyRegistrationNoID.put(ym, TitleNumberIDToCompanyRegistrationNoID);
        YMCompanyRegistrationNoIDToTitleNumberID.put(ym, CompanyRegistrationNoIDToTitleNumberID);
        YMProprietorNameIDToCompanyRegistrationNoID.put(ym, ProprietorNameIDToCompanyRegistrationNoID);
        YMCompanyRegistrationNoIDToProprietorNameID.put(ym, CompanyRegistrationNoIDToProprietorNameID);

        LR_ID CompanyRegistrationNo1ID;
        LR_ID CompanyRegistrationNo2ID;
        LR_ID CompanyRegistrationNo3ID;
        LR_ID CompanyRegistrationNo4ID;
        LR_ID TitleNumberID;
        LR_ID ProprietorName1ID;
        LR_ID ProprietorName2ID;
        LR_ID ProprietorName3ID;
        LR_ID ProprietorName4ID;

        // init fullCCR
        fullCCR = new HashMap<>();
        indir = new File(outputDataDir, Strings.S_Subsets);
        indir = new File(indir, area);
        indir = new File(indir, "CCOD");
        indir = new File(indir, Strings.S_FULL);
        name = "CCOD_FULL_2017_11";
        indir = new File(indir, name);
        fin = new File(indir, name + ".csv");
        lines = Generic_ReadCSV.read(fin, null, 7);
        for (int ID = 1; ID < lines.size(); ID++) {
            try {
                fullccr = new LR_CC_FULL_Record(Env, ym, lines.get(ID), upDateIDs);
                fullCCR.put(fullccr.getID(), fullccr);
                CompanyRegistrationNo1ID = fullccr.getCompanyRegistrationNo1ID();
                CompanyRegistrationNo2ID = fullccr.getCompanyRegistrationNo2ID();
                CompanyRegistrationNo3ID = fullccr.getCompanyRegistrationNo3ID();
                CompanyRegistrationNo4ID = fullccr.getCompanyRegistrationNo4ID();
                TitleNumberID = fullccr.getTitleNumberID();
                ProprietorName1ID = fullccr.getProprietorName1ID();
                ProprietorName2ID = fullccr.getProprietorName2ID();
                ProprietorName3ID = fullccr.getProprietorName3ID();
                ProprietorName4ID = fullccr.getProprietorName4ID();
                // Update key lookups
                updateKeyLookups(CompanyRegistrationNo1ID,
                        CompanyRegistrationNo2ID, CompanyRegistrationNo3ID,
                        CompanyRegistrationNo4ID, TitleNumberID,
                        ProprietorName1ID, ProprietorName2ID, ProprietorName3ID,
                        ProprietorName4ID,
                        TitleNumberIDToCompanyRegistrationNoID,
                        CompanyRegistrationNoIDToTitleNumberID,
                        ProprietorNameIDToCompanyRegistrationNoID,
                        CompanyRegistrationNoIDToProprietorNameID);
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace(System.err);
            } catch (Exception ex) {
                Logger.getLogger(LR_Transitions_Process.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // init fullOCR
        fullOCR = new HashMap<>();
        indir = new File(outputDataDir, Strings.S_Subsets);
        indir = new File(indir, area);
        indir = new File(indir, "OCOD");
        indir = new File(indir, Strings.S_FULL);
        name = "OCOD_FULL_2017_11";
        indir = new File(indir, name);
        fin = new File(indir, name + ".csv");
        lines = Generic_ReadCSV.read(fin, null, 7);
        for (int ID = 1; ID < lines.size(); ID++) {
            try {
                fullocr = new LR_OC_FULL_Record(Env, ym, lines.get(ID), upDateIDs);
                fullOCR.put(fullocr.getID(), fullocr);
                CompanyRegistrationNo1ID = fullocr.getCompanyRegistrationNo1ID();
                CompanyRegistrationNo2ID = fullocr.getCompanyRegistrationNo2ID();
                CompanyRegistrationNo3ID = fullocr.getCompanyRegistrationNo3ID();
                CompanyRegistrationNo4ID = fullocr.getCompanyRegistrationNo4ID();
                TitleNumberID = fullocr.getTitleNumberID();
                ProprietorName1ID = fullocr.getProprietorName1ID();
                ProprietorName2ID = fullocr.getProprietorName2ID();
                ProprietorName3ID = fullocr.getProprietorName3ID();
                ProprietorName4ID = fullocr.getProprietorName4ID();
                // Update key lookups
                updateKeyLookups(CompanyRegistrationNo1ID,
                        CompanyRegistrationNo2ID, CompanyRegistrationNo3ID,
                        CompanyRegistrationNo4ID, TitleNumberID,
                        ProprietorName1ID, ProprietorName2ID, ProprietorName3ID,
                        ProprietorName4ID,
                        TitleNumberIDToCompanyRegistrationNoID,
                        CompanyRegistrationNoIDToTitleNumberID,
                        ProprietorNameIDToCompanyRegistrationNoID,
                        CompanyRegistrationNoIDToProprietorNameID);
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace(System.err);
            } catch (Exception ex) {
                Logger.getLogger(LR_Transitions_Process.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        writeTitleNumberCompanyRegistrationNoIDLookups(ym);
        writeProprietorNameIDCompanyRegistrationNoIDLookups(ym);

        // Check if there are OCR in CCR
        Set<LR_ID2> s;
        s = new HashSet<>();
        s.addAll(fullCCR.keySet());
        s.retainAll(fullOCR.keySet());
        System.out.println("There are " + s.size() + " oversees corporate owners.");
        Iterator<LR_ID2> ite;
        LR_ID2 id2;
        String address;
        String titleNumber;
        LR_ID companyRegistrationNoID;
        LR_ID proprietorNameID;
        String companyRegistrationNo;
        String proprietorName;
        ite = s.iterator();
        System.out.println("CompanyRegistrationNo, TitleNumber, ProprietorName, Address");
        while (ite.hasNext()) {
            id2 = ite.next();
            address = Env.IDToLookups.get(Env.TypeToID.get(Strings.S_PropertyAddress)).get(id2.getPropertyAddressID());
            titleNumber = Env.IDToLookups.get(Env.TypeToID.get(Strings.S_TitleNumber)).get(id2.getTitleNumberID());
            ArrayList<LR_ID> companyRegistrationNoIDs;
            companyRegistrationNoIDs = TitleNumberIDToCompanyRegistrationNoID.get(id2.getTitleNumberID());
            companyRegistrationNoID = companyRegistrationNoIDs.get(companyRegistrationNoIDs.size() - 1);
            proprietorNameID = CompanyRegistrationNoIDToProprietorNameID.get(companyRegistrationNoID);
            companyRegistrationNo = Env.IDToLookups.get(Env.TypeToID.get(Strings.S_CompanyRegistrationNo)).get(companyRegistrationNoID);
            proprietorName = Env.IDToLookups.get(Env.TypeToID.get(Strings.S_ProprietorName)).get(proprietorNameID);
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
                ym = new Generic_YearMonth(Env.ge, time.replaceAll("_", "-"));
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
                    indir = new File(outputDataDir, Strings.S_Subsets);
                    indir = new File(indir, area);
                }
                indir = new File(indir, name0);

                indir = new File(indir, name);
                System.out.println("indir " + indir);
                fin = new File(indir, name + ".csv");
                if (!fin.exists()) {
                    System.out.println("File " + fin + " does not exist.");
                }
                lines = Generic_ReadCSV.read(fin, null, 7);
                //LR_Record r;
                for (int ID = 1; ID < lines.size(); ID++) {
                    try {
                        if (isCCOD) {
                            ccr = new LR_CC_COU_Record(Env, ym, lines.get(ID));
                            add(addedCCRTime, deletedCCRTime, ccr);
                        } else {
                            ocr = new LR_OC_COU_Record(Env, ym, lines.get(ID));
                            add(addedOCRTime, deletedOCRTime, ocr);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace(System.err);
                    } catch (Exception ex) {
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

    // Update key lookups
    protected void updateKeyLookups(LR_ID CompanyRegistrationNo1ID,
            LR_ID CompanyRegistrationNo2ID, LR_ID CompanyRegistrationNo3ID,
            LR_ID CompanyRegistrationNo4ID, LR_ID TitleNumberID,
            LR_ID ProprietorName1ID, LR_ID ProprietorName2ID,
            LR_ID ProprietorName3ID, LR_ID ProprietorName4ID,
            HashMap<LR_ID, ArrayList<LR_ID>> TitleNumberIDToCompanyRegistrationNoID,
            HashMap<LR_ID, HashSet<LR_ID>> CompanyRegistrationNoIDToTitleNumberID,
            HashMap<LR_ID, HashSet<LR_ID>> ProprietorNameIDToCompanyRegistrationNoID,
            HashMap<LR_ID, LR_ID> CompanyRegistrationNoIDToProprietorNameID) {
        ArrayList<LR_ID> CompanyRegistrationNoIDs;
        HashSet<LR_ID> TitleNumberIDs;
        HashSet<LR_ID> CompanyRegistrationNoIDs2;
        LR_ID ProprietorNameID;
        // Update TitleNumberIDToCompanyRegistrationNoID
        if (TitleNumberIDToCompanyRegistrationNoID.containsKey(TitleNumberID)) {
            CompanyRegistrationNoIDs = TitleNumberIDToCompanyRegistrationNoID.get(TitleNumberID);
        } else {
            CompanyRegistrationNoIDs = new ArrayList<>();
            TitleNumberIDToCompanyRegistrationNoID.put(TitleNumberID, CompanyRegistrationNoIDs);
        }
        CompanyRegistrationNoIDs.add(CompanyRegistrationNo1ID);
        CompanyRegistrationNoIDs.add(CompanyRegistrationNo2ID);
        CompanyRegistrationNoIDs.add(CompanyRegistrationNo3ID);
        CompanyRegistrationNoIDs.add(CompanyRegistrationNo4ID);
        // Update CompanyRegistrationNoIDToTitleNumberID
        if (CompanyRegistrationNoIDToTitleNumberID.containsKey(CompanyRegistrationNo1ID)) {
            TitleNumberIDs = CompanyRegistrationNoIDToTitleNumberID.get(CompanyRegistrationNo1ID);
        } else {
            TitleNumberIDs = new HashSet<>();
            CompanyRegistrationNoIDToTitleNumberID.put(CompanyRegistrationNo1ID, TitleNumberIDs);
        }
        TitleNumberIDs.add(TitleNumberID);
        // Update ProprietorNameIDToCompanyRegistrationNoID
        // ProprietorName1ID
        if (ProprietorNameIDToCompanyRegistrationNoID.containsKey(ProprietorName1ID)) {
            CompanyRegistrationNoIDs2 = ProprietorNameIDToCompanyRegistrationNoID.get(ProprietorName1ID);
        } else {
            CompanyRegistrationNoIDs2 = new HashSet<>();
            ProprietorNameIDToCompanyRegistrationNoID.put(ProprietorName1ID, TitleNumberIDs);
        }
        CompanyRegistrationNoIDs2.add(CompanyRegistrationNo1ID);
        // ProprietorName2ID
        if (ProprietorNameIDToCompanyRegistrationNoID.containsKey(ProprietorName2ID)) {
            CompanyRegistrationNoIDs2 = ProprietorNameIDToCompanyRegistrationNoID.get(ProprietorName2ID);
        } else {
            CompanyRegistrationNoIDs2 = new HashSet<>();
            ProprietorNameIDToCompanyRegistrationNoID.put(ProprietorName2ID, TitleNumberIDs);
        }
        CompanyRegistrationNoIDs2.add(CompanyRegistrationNo2ID);
        // ProprietorName3ID
        if (ProprietorNameIDToCompanyRegistrationNoID.containsKey(ProprietorName3ID)) {
            CompanyRegistrationNoIDs2 = ProprietorNameIDToCompanyRegistrationNoID.get(ProprietorName3ID);
        } else {
            CompanyRegistrationNoIDs2 = new HashSet<>();
            ProprietorNameIDToCompanyRegistrationNoID.put(ProprietorName3ID, TitleNumberIDs);
        }
        CompanyRegistrationNoIDs2.add(CompanyRegistrationNo3ID);
        // ProprietorName4ID
        if (ProprietorNameIDToCompanyRegistrationNoID.containsKey(ProprietorName4ID)) {
            CompanyRegistrationNoIDs2 = ProprietorNameIDToCompanyRegistrationNoID.get(ProprietorName4ID);
        } else {
            CompanyRegistrationNoIDs2 = new HashSet<>();
            ProprietorNameIDToCompanyRegistrationNoID.put(ProprietorName4ID, TitleNumberIDs);
        }
        CompanyRegistrationNoIDs2.add(CompanyRegistrationNo4ID);
        // Update CompanyRegistrationNoIDToProprietorNameID
        // CompanyRegistrationNo1ID
        String companyRegistrationNo;
        if (CompanyRegistrationNoIDToProprietorNameID.containsKey(CompanyRegistrationNo1ID)) {
            companyRegistrationNo = Env.IDToLookups.get(Env.TypeToID.get(Strings.S_CompanyRegistrationNo)).get(CompanyRegistrationNo1ID);
            ProprietorNameID = CompanyRegistrationNoIDToProprietorNameID.get(CompanyRegistrationNo1ID);
            if (ProprietorNameID != ProprietorName1ID) {
                if (!ProprietorNameID.equals(ProprietorName1ID)) {
                    // This is unexpected, the proprietor name has changed!
                    // Report the ProprietorName change.
                    reportProprietorNameChange(companyRegistrationNo, ProprietorNameID, ProprietorName1ID);
                }
            }
        } else {
            ProprietorNameID = ProprietorName1ID;
            CompanyRegistrationNoIDToProprietorNameID.put(CompanyRegistrationNo1ID, ProprietorNameID);
        }
        // CompanyRegistrationNo2ID
        if (CompanyRegistrationNoIDToProprietorNameID.containsKey(CompanyRegistrationNo2ID)) {
            companyRegistrationNo = Env.IDToLookups.get(Env.TypeToID.get(Strings.S_CompanyRegistrationNo)).get(CompanyRegistrationNo2ID);
            ProprietorNameID = CompanyRegistrationNoIDToProprietorNameID.get(CompanyRegistrationNo2ID);
            if (ProprietorNameID != ProprietorName2ID) {
                if (!ProprietorNameID.equals(ProprietorName2ID)) {
                    // This is unexpected, the proprietor name has changed!
                    reportProprietorNameChange(companyRegistrationNo, ProprietorNameID, ProprietorName2ID);
                }
            }
        } else {
            ProprietorNameID = ProprietorName2ID;
            CompanyRegistrationNoIDToProprietorNameID.put(CompanyRegistrationNo2ID, ProprietorNameID);
        }
        // CompanyRegistrationNo3ID
        if (CompanyRegistrationNoIDToProprietorNameID.containsKey(CompanyRegistrationNo3ID)) {
                        companyRegistrationNo = Env.IDToLookups.get(Env.TypeToID.get(Strings.S_CompanyRegistrationNo)).get(CompanyRegistrationNo3ID);
            ProprietorNameID = CompanyRegistrationNoIDToProprietorNameID.get(CompanyRegistrationNo3ID);
            if (ProprietorNameID != ProprietorName3ID) {
                if (!ProprietorNameID.equals(ProprietorName3ID)) {
                    // This is unexpected, the proprietor name has changed!
                    reportProprietorNameChange(companyRegistrationNo, ProprietorNameID, ProprietorName3ID);
                }
            }
        } else {
            ProprietorNameID = ProprietorName3ID;
            CompanyRegistrationNoIDToProprietorNameID.put(CompanyRegistrationNo3ID, ProprietorNameID);
        }
        // CompanyRegistrationNo4ID
        if (CompanyRegistrationNoIDToProprietorNameID.containsKey(CompanyRegistrationNo4ID)) {
            companyRegistrationNo = Env.IDToLookups.get(Env.TypeToID.get(Strings.S_CompanyRegistrationNo)).get(CompanyRegistrationNo4ID);
            ProprietorNameID = CompanyRegistrationNoIDToProprietorNameID.get(CompanyRegistrationNo4ID);
            if (ProprietorNameID != ProprietorName4ID) {
                if (!ProprietorNameID.equals(ProprietorName4ID)) {
                    // This is unexpected, the proprietor name has changed!
                    reportProprietorNameChange(companyRegistrationNo, ProprietorNameID, ProprietorName4ID);
                }
            }
        } else {
            ProprietorNameID = ProprietorName4ID;
            CompanyRegistrationNoIDToProprietorNameID.put(CompanyRegistrationNo4ID, ProprietorNameID);
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
            ArrayList<? extends LR_Record> ld,
            ArrayList<? extends LR_Record> la) {
        if (printDiff) {
            String m;
            m = "Deleted " + type + " is also added, so this is really updated.";
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
            m = "Record with ID " + aID + " " + name + " " + result
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
            ArrayList<? extends LR_Record> l, String delimeter) {
        String transitionType;
        if (mappedTransitions.containsKey(aID)) {
            transitions = mappedTransitions.get(aID);
            transitionType = transitionTypes.get(aID);
            transitionTypes.remove(aID);
            Generic_Collections.addToMap(transitionTypeCounts, transitionType,
                    -1);
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
            titleNumberChange = doDiff(Strings.S_TitleNumber, s0, s1, printDiff, titleNumberChange);
            changeCount++;
        }
        s0 = f.getCompanyRegistrationNo1();
        s1 = c.getCompanyRegistrationNo1();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setCompanyRegistrationNo1(s1);
            ownershipChange = doDiff(Strings.S_CompanyRegistrationNo1, s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        s0 = f.getCompanyRegistrationNo2();
        s1 = c.getCompanyRegistrationNo2();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setCompanyRegistrationNo2(s1);
            ownershipChange = doDiff(Strings.S_CompanyRegistrationNo2, s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        s0 = f.getCompanyRegistrationNo3();
        s1 = c.getCompanyRegistrationNo3();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setCompanyRegistrationNo3(s1);
            ownershipChange = doDiff(Strings.S_CompanyRegistrationNo3, s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        s0 = f.getCompanyRegistrationNo4();
        s1 = c.getCompanyRegistrationNo4();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setCompanyRegistrationNo4(s1);
            ownershipChange = doDiff(Strings.S_CompanyRegistrationNo4, s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        long l0;
        long l1;
        l0 = f.getPricePaid();
        l1 = c.getPricePaid();
        if (l0 != l1) {
            f.setPricePaid(l1);
            if (l0 < 0) {
                if (printDiff) {
                    s3 = "PricePaid changed from " + l0 + " to " + l1;
                    System.out.println(s3);
                    if (l1 > 0) {
                        s3 = "Got new price, perhaps look in sales data?!";
                    } else {
                        s3 = "Not got new price, perhaps look in sales data?!";
                    }
                    System.out.println(s3);
                }
            }
            changeCount++;
        }
        s0 = f.getTenure();
        s1 = c.getTenure();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setTenure(s1);
            tenureChange = doDiff(Strings.S_Tenure, s0, s1, printDiff, tenureChange);
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
            f.initPostcodeAndPostcodeDistrict(s1);
            doDiff(Strings.S_Postcode, s0, s1, printDiff, true);
            changeCount++;
        }
        s0 = f.getPropertyAddress();
        s1 = c.getPropertyAddress();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setPropertyAddress(s1);
            doDiff(Strings.S_PropertyAddress, s0, s1, printDiff, true);
            changeCount++;
        }
        // ProprietorNames
        s0 = f.getProprietorName1();
        s1 = c.getProprietorName1();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setProprietorName1(s1);
            ownershipChange = doDiff(
                    Strings.S_ProprietorName1, s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        s0 = f.getProprietorName2();
        s1 = c.getProprietorName2();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setProprietorName2(s1);
            ownershipChange = doDiff(
                    Strings.S_ProprietorName2, s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        s0 = f.getProprietorName3();
        s1 = c.getProprietorName3();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setProprietorName3(s1);
            ownershipChange = doDiff(Strings.S_ProprietorName3,
                    s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        s0 = f.getProprietorName4();
        s1 = c.getProprietorName4();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setProprietorName4(s1);
            ownershipChange = doDiff(Strings.S_ProprietorName4,
                    s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        //ProprietorshipCategory
        s0 = f.getProprietorshipCategory1();
        s1 = c.getProprietorshipCategory1();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setProprietorshipCategory1(s1);
            ownershipChange = doDiff(Strings.S_ProprietorshipCategory1,
                    s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        s0 = f.getProprietorshipCategory2();
        s1 = c.getProprietorshipCategory2();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setProprietorshipCategory2(s1);
            ownershipChange = doDiff(Strings.S_ProprietorshipCategory2,
                    s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        s0 = f.getProprietorshipCategory3();
        s1 = c.getProprietorshipCategory3();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setProprietorshipCategory3(s1);
            ownershipChange = doDiff(Strings.S_ProprietorshipCategory3,
                    s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        s0 = f.getProprietorshipCategory4();
        s1 = c.getProprietorshipCategory4();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setProprietorshipCategory4(s1);
            ownershipChange = doDiff(Strings.S_ProprietorshipCategory4,
                    s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        result[0] = changeCount;
        result[1] = titleNumberChange;
        result[2] = ownershipChange;
        result[3] = tenureChange;
        return result;
    }

    // Report the ProprietorName change.
    protected void reportProprietorNameChange(String companyRegistrationNo,
            LR_ID ProprietorNameID, LR_ID ProprietorName1ID) {
        if (!companyRegistrationNo.isEmpty()) {
            String s;
            String proprietorName0;
            String proprietorName1;
            proprietorName0 = Env.IDToLookups.get(Env.TypeToID.get(Strings.S_ProprietorName)).get(ProprietorNameID);
            proprietorName1 = Env.IDToLookups.get(Env.TypeToID.get(Strings.S_ProprietorName)).get(ProprietorName1ID);
            s = "Proprietor name for company registration number \""
                    + companyRegistrationNo + "\" changed from \""
                    + proprietorName0 + "\" to \"" + proprietorName1 + "\"";
            System.out.println(s);
        }
    }

    protected boolean doDiff(String name, String s0,
            String s1, boolean printDiff, boolean change) {
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
    public Object[] difference(LR_CC_FULL_Record f, LR_CC_COU_Record c, boolean printDiff) {
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
    public Object[] difference(LR_OC_FULL_Record f, LR_OC_COU_Record c, boolean printDiff) {
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
            f.setCountryIncorporated2(s1);
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
            f.setCountryIncorporated3(s1);
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
            f.setCountryIncorporated4(s1);
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
        l.add(r);
    }

    void printGeneralisation(PrintWriter pw, int minCC, int minOC) {
        printGeneralisation(pw, "addedCCR", addedCCRCount, minCC);
        printGeneralisation(pw, "deletedCCR", deletedCCRCount, minCC);
        printGeneralisation(pw, "addedOCR", addedOCRCount, minOC);
        printGeneralisation(pw, "deletedOCR", deletedOCRCount, minOC);
    }

    void printGeneralisation(PrintWriter pw, String type,
            HashMap<LR_ID2, Integer> counts, int min
    ) {
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
                        pw.println("Those with less than " + min + "," + smallCount);
                        reportedSmallCount = true;
                    }
                    if (aID != null) {
                        pw.println("\"" + Env.IDToLookups.get(Env.TypeToID.get(Strings.S_PropertyAddress)).get(aID.getPropertyAddressID()) + "\",\""
                                + Env.IDToLookups.get(Env.TypeToID.get(Strings.S_TitleNumber)).get(aID.getTitleNumberID()) + "\"," + count);
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

    public void writeTitleNumberCompanyRegistrationNoIDLookups(Generic_YearMonth ym) {
        if (UpdatedTitleNumberCompanyRegistrationNoLookups) {
            File dir;
            dir = new File(Files.getGeneratedDataDir(Strings), ym.getYYYYMM());
            File f;
            f = new File(dir, "TitleNumberIDToCompanyRegistrationNoID.dat");
            Generic_StaticIO.writeObject(YMTitleNumberIDToCompanyRegistrationNoID.get(ym), f);
            f = new File(dir, "CompanyRegistrationNoIDToTitleNumberID.dat");
            Generic_StaticIO.writeObject(YMCompanyRegistrationNoIDToTitleNumberID.get(ym), f);
        }
    }

    public HashMap<LR_ID, ArrayList<LR_ID>> loadTitleNumberIDToCompanyRegistrationNoID(Generic_YearMonth ym) {
        File dir;
        dir = new File(Files.getGeneratedDataDir(Strings), ym.getYYYYMM());
        File f;
        f = new File(dir, "TitleNumberIDToCompanyRegistrationNoID.dat");
        if (!f.exists()) {
            return new HashMap<>();
        } else {
            return (HashMap<LR_ID, ArrayList<LR_ID>>) Generic_StaticIO.readObject(f);
        }
    }

    public HashMap<LR_ID, HashSet<LR_ID>> loadCompanyRegistrationNoIDToTitleNumberID(Generic_YearMonth ym) {
        File f;
        f = new File(Files.getGeneratedDataDir(Strings), "CompanyRegistrationNoIDToTitleNumberID.dat");
        if (!f.exists()) {
            return new HashMap<>();
        } else {
            return (HashMap<LR_ID, HashSet<LR_ID>>) Generic_StaticIO.readObject(f);
        }
    }

    public void writeProprietorNameIDCompanyRegistrationNoIDLookups(Generic_YearMonth ym) {
        if (UpdatedProprietorNameIDCompanyRegistrationNoLookups) {
            File dir;
            dir = new File(Files.getGeneratedDataDir(Strings), ym.getYYYYMM());
            File f;
            f = new File(dir, "ProprietorNameIDToCompanyRegistrationNoID.dat");
            Generic_StaticIO.writeObject(YMProprietorNameIDToCompanyRegistrationNoID.get(ym), f);
            f = new File(dir, "CompanyRegistrationNoIDToProprietorNameID.dat");
            Generic_StaticIO.writeObject(YMCompanyRegistrationNoIDToProprietorNameID.get(ym), f);
        }
    }

    public HashMap<LR_ID, HashSet<LR_ID>> loadProprietorNameIDToCompanyRegistrationNoIDLookup(Generic_YearMonth ym) {
        File dir;
        dir = new File(Files.getGeneratedDataDir(Strings), ym.getYYYYMM());
        File f;
        f = new File(dir, "ProprietorNameIDToCompanyRegistrationNoID.dat");
        if (!f.exists()) {
            return new HashMap<>();
        } else {
            return (HashMap<LR_ID, HashSet<LR_ID>>) Generic_StaticIO.readObject(f);
        }
    }

    public HashMap<LR_ID, ArrayList<LR_ID>> loadCompanyRegistrationNoIDToProprietorNameLookup(Generic_YearMonth ym) {
        File dir;
        dir = new File(Files.getGeneratedDataDir(Strings), ym.getYYYYMM());
        File f;
        f = new File(dir, "CompanyRegistrationNoIDToProprietorNameID.dat");
        if (!f.exists()) {
            return new HashMap<>();
        } else {
            return (HashMap<LR_ID, ArrayList<LR_ID>>) Generic_StaticIO.readObject(f);
        }
    }

}
