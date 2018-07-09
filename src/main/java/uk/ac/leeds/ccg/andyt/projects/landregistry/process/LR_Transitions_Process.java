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
import uk.ac.leeds.ccg.andyt.generic.utilities.Generic_Collections;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Environment;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_ID;
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

    protected LR_Transitions_Process() {
        super();
    }

    public LR_Transitions_Process(LR_Environment env) {
        super(env);
    }

    HashMap<LR_ID, Integer> addedCCRCount;
    HashMap<LR_ID, Integer> deletedCCRCount;
    HashMap<LR_ID, Integer> addedOCRCount;
    HashMap<LR_ID, Integer> deletedOCRCount;
//    HashMap<String, HashMap<LR_ID, ArrayList<LR_CC_COU_Record>>> addedCCR;
//    HashMap<String, HashMap<LR_ID, ArrayList<LR_CC_COU_Record>>> deletedCCR;
//    HashMap<String, HashMap<LR_ID, ArrayList<LR_OC_COU_Record>>> addedOCR;
//    HashMap<String, HashMap<LR_ID, ArrayList<LR_OC_COU_Record>>> deletedOCR;
    HashMap<String, HashMap<LR_ID, ArrayList<LR_Record>>> addedCCR;
    HashMap<String, HashMap<LR_ID, ArrayList<LR_Record>>> deletedCCR;
    HashMap<String, HashMap<LR_ID, ArrayList<LR_Record>>> addedOCR;
    HashMap<String, HashMap<LR_ID, ArrayList<LR_Record>>> deletedOCR;

    HashMap<LR_ID, LR_CC_FULL_Record> fullCCR;
    HashMap<LR_ID, LR_OC_FULL_Record> fullOCR;

    public void run(String area, File inputDataDir,
            int minCC, int minOC) {
        boolean printDiff;
//        printDiff = true;
        printDiff = false;

        File outputDataDir;
        outputDataDir = Files.getOutputDataDir(Strings);

        ArrayList<String> names0;
        //ArrayList<String> names1;
        ArrayList<String> names2;
        String name;
        String name0;
        String name00;
        names0 = new ArrayList<>();
        //names1 = new ArrayList<>();
        names2 = new ArrayList<>();
        names0.add("CCOD");
        names0.add("OCOD");
        boolean isCCOD;
        //names1.add("COU");
        //names1.add("FULL");
        //names2.add("2017_11");
        names2.add("2017_12");
        names2.add("2018_01");
        names2.add("2018_02");
        names2.add("2018_03");
        names2.add("2018_04");
        names2.add("2018_05");
        names2.add("2018_06");

        addedCCRCount = new HashMap<>();
        deletedCCRCount = new HashMap<>();
        addedOCRCount = new HashMap<>();
        deletedOCRCount = new HashMap<>();

        addedCCR = new HashMap<>();
        deletedCCR = new HashMap<>();
        addedOCR = new HashMap<>();
        deletedOCR = new HashMap<>();
//        HashMap<LR_ID, ArrayList<LR_CC_COU_Record>> addedCCRTime = null;
//        HashMap<LR_ID, ArrayList<LR_CC_COU_Record>> deletedCCRTime = null;
//        HashMap<LR_ID, ArrayList<LR_OC_COU_Record>> addedOCRTime = null;
//        HashMap<LR_ID, ArrayList<LR_OC_COU_Record>> deletedOCRTime = null;
        /**
         * Keys are PropertyAddressIDs, Values are a list of LR_Records.
         */
        HashMap<LR_ID, ArrayList<LR_Record>> addedCCRTime = null;
        HashMap<LR_ID, ArrayList<LR_Record>> deletedCCRTime = null;
        HashMap<LR_ID, ArrayList<LR_Record>> addedOCRTime = null;
        HashMap<LR_ID, ArrayList<LR_Record>> deletedOCRTime = null;

        File indir;
        File outdir;
        File f;
        ArrayList<String> lines;
        PrintWriter pw = null;
        outdir = new File(outputDataDir, area + "Transitions");
        System.out.println("outdir " + outdir);
        outdir.mkdirs();
        f = new File(outdir, "TransitionsGeneralisation.csv");
        try {
            pw = new PrintWriter(f);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LR_Transitions_Process.class.getName()).log(Level.SEVERE, null, ex);
        }

        String changeIndicator;
        LR_CC_COU_Record ccr;
        LR_OC_COU_Record ocr;
        LR_CC_FULL_Record fullccr;
        LR_OC_FULL_Record fullocr;

        File fin;
        // init fullCCR
        fullCCR = new HashMap<>();
        indir = new File(outputDataDir, area);
        indir = new File(indir, "CCOD");
        name = "CCOD_FULL_2017_11";
        indir = new File(indir, name);
        fin = new File(indir, name + ".csv");
        lines = Generic_ReadCSV.read(fin, null, 7);
        for (int ID = 1; ID < lines.size(); ID++) {
            try {
                fullccr = new LR_CC_FULL_Record(Env, lines.get(ID));
                fullCCR.put(fullccr.getPropertyAddressID(), fullccr);
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace(System.err);
            }
        }

        // init fullOCR
        fullOCR = new HashMap<>();
        indir = new File(outputDataDir, area);
        indir = new File(indir, "OCOD");
        name = "OCOD_FULL_2017_11";
        indir = new File(indir, name);
        fin = new File(indir, name + ".csv");
        lines = Generic_ReadCSV.read(fin, null, 7);
        for (int ID = 1; ID < lines.size(); ID++) {
            try {
                fullocr = new LR_OC_FULL_Record(Env, lines.get(ID));
                fullOCR.put(fullocr.getPropertyAddressID(), fullocr);
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace(System.err);
            }
        }

        // check if there are OCR in CCR
        Set<LR_ID> s;
        s = new HashSet<>();
        s.addAll(fullCCR.keySet());
        s.retainAll(fullOCR.keySet());
        System.out.println("There are " + s.size() + "oversees corporate owners.");

        Iterator<String> ites0;
        Iterator<String> ites1;
        ites0 = names0.iterator();
        while (ites0.hasNext()) {
            name0 = ites0.next();
            isCCOD = name0.equalsIgnoreCase("CCOD");
            name00 = "";
            name00 += name0 + "_COU_";
            ites1 = names2.iterator();
            while (ites1.hasNext()) {
                name = name00;
                String time;
                time = ites1.next();
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
                indir = new File(outputDataDir, area);
                indir = new File(indir, name0);
                indir = new File(indir, name);
                System.out.println("indir " + indir);
                f = new File(indir, name + ".csv");
                if (!f.exists()) {
                    System.out.println("File " + f + " does not exist.");
                }
                lines = Generic_ReadCSV.read(f, null, 7);
                //LR_Record r;
                for (int ID = 1; ID < lines.size(); ID++) {
                    try {
                        if (isCCOD) {
                            ccr = new LR_CC_COU_Record(Env, lines.get(ID));
                            add(addedCCRTime, deletedCCRTime, ccr);
                        } else {
                            ocr = new LR_OC_COU_Record(Env, lines.get(ID));
                            add(addedOCRTime, deletedOCRTime, ocr);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace(System.err);
                    }
                }
            }
        }
        printGeneralisation(pw, minCC, minOC);
        pw.close();

        f = new File(outdir, "TransitionsMapped.csv");
        try {
            pw = new PrintWriter(f);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LR_Transitions_Process.class.getName()).log(Level.SEVERE, null, ex);
        }

        /**
         * mappedTransitions contains the detailed transition for each property.
         */
        HashMap<LR_ID, ArrayList<ArrayList<LR_Record>>> mappedTransitions;

        /**
         * For transitions in mappedTransitions.
         */
        ArrayList<ArrayList<LR_Record>> transitions = null;

        /**
         * For records in transitions.
         */
        LR_Record r;

        /**
         * List of LR_Record records in transitions.
         */
        ArrayList<LR_Record> ld;
        ArrayList<LR_Record> la;

//        /**
//         * List of LR_OC_COU_Record records in transitions.
//         */
//        ArrayList<LR_OC_COU_Record> lOC;
//
//        /**
//         * List of LR_CC_COU_Record records in transitions.
//         */
//        ArrayList<LR_CC_COU_Record> lCC;
        /**
         * transitionTypes gives a type of transition for each property.
         */
        HashMap<LR_ID, String> transitionTypes;

        /**
         * For name that describes a property ownership transition.
         */
        String transitionType;

        /**
         * transitionTypeCounts counts the number of transitions of each type.
         */
        TreeMap<String, Integer> transitionTypeCounts;

        /**
         * For the transition type count.
         */
        int transitionTypeCount;

        // Initialisation
        mappedTransitions = new HashMap<>();
        transitionTypes = new HashMap<>();
        transitionTypeCounts = new TreeMap<>();

        // For messages
        String m;

        Iterator<LR_ID> ite1;
        Iterator<LR_Record> ite2;
        Iterator<LR_Record> ite3;

        LR_ID aID;
        LR_ID lrID;

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

            LR_CC_FULL_Record recc;
            LR_CC_COU_Record recca;
            LR_CC_COU_Record reccd;
            LR_OC_FULL_Record reco;
            LR_OC_COU_Record recoa;
            LR_OC_COU_Record recod;
            ArrayList<LR_Record> loa0;
            ArrayList<LR_Record> lod0;
            ArrayList<LR_Record> lca0;
            ArrayList<LR_Record> lcd0;
            String destinationCountry;
            String originCountry;
            int nod;
            int noa;
            int ncd;
            int nca;
            /**
             * Transitions for deletedCCR.
             */
            LR_CC_FULL_Record cc;
            ite1 = deletedCCRTime.keySet().iterator();
            while (ite1.hasNext()) {
                aID = ite1.next();
                ld = deletedCCRTime.get(aID);
                ncd = checkList(ld, aID, "deleted");
                // Iterate over each deletion.
                ite2 = ld.iterator();
                while (ite2.hasNext()) {
                    reccd = (LR_CC_COU_Record) ld.get(0);
                    /**
                     * Has it also been added?
                     */
                    if (addedCCRTime.containsKey(aID)) {
                        la = addedCCRTime.get(aID);
                        addedCCRTime.remove(aID);
                        if (printDiff) {
                            m = "Deleted CCR is also added, so is really updated.";
                            System.out.println(m);
                            if (fullCCR.containsKey(aID)) {
                                cc = fullCCR.get(aID);
                                System.out.println("Current:");
                                System.out.println(cc);
                            }
                            System.out.println("Deleted:");
                            System.out.println(ld.get(0));
                            System.out.println("Added:");
                            System.out.println(la.get(0));
                        }
                        nca = checkList(la, aID, "added");
                        if (nca != ncd) {
                            int debug = 1;
                        }
                        if (nca > 1 || ncd > 1) {
                            int debug = 1;
                        }
                        LR_Record lr;
                        ite3 = la.iterator();
                        Object[] diff;
                        while (ite3.hasNext()) {
                            lr = ite3.next();
                            lrID = lr.getPropertyAddressID();
                            recca = (LR_CC_COU_Record) lr;
                            // Examine difference between fullCCR and cccou 
                            diff = difference(reccd, recca, printDiff);
                            if ((Boolean) diff[2]) {
                                /**
                                 * Figure out if the cc change is also foreign
                                 * ownership change and if so who it changed
                                 * from and to and if there was a change in
                                 * country. Record all this in the
                                 * transitionType.
                                 */
                                if (fullOCR.containsKey(aID)) {
                                    if (addedOCRTime.containsKey(lrID)) {
                                        loa0 = addedOCRTime.get(lrID);
                                        addedOCRTime.remove(lrID);
                                        noa = checkList(loa0, lrID, "added"); // Assume list of 1.
                                        recoa = (LR_OC_COU_Record) loa0.get(0);
                                        fullOCR.put(lrID, recoa);
                                        destinationCountry = recoa.getCountryIncorporated1();
                                        if (deletedOCRTime.containsKey(lrID)) {
                                            lod0 = deletedOCRTime.get(lrID);
                                            deletedOCRTime.remove(lrID);
                                            nod = checkList(lod0, lrID, "deleted"); // Assume list of 1.
                                            if (noa != nod) {
                                                int debug = 1;
                                            }
                                            if (noa > 1 || nod > 1) {
                                                int debug = 1;
                                            }
                                            recod = (LR_OC_COU_Record) lod0.get(0);
                                            originCountry = recod.getCountryIncorporated1();
                                            transitionType = "CU_OU_" + originCountry + "_" + destinationCountry;
                                        } else {
                                            transitionType = "CU_OA_UK_" + destinationCountry;
                                        }
                                    } else {
                                        if (deletedOCRTime.containsKey(lrID)) {
                                            lod0 = deletedOCRTime.get(lrID);
                                            deletedOCRTime.remove(lrID);
                                            checkList(lod0, lrID, "deleted"); // Assume list of 1.
                                            recod = (LR_OC_COU_Record) lod0.get(0);
                                            fullOCR.remove(lrID);
                                            originCountry = recod.getCountryIncorporated1();
                                            transitionType = "CU_OD_" + originCountry + "United Kingdom";
                                        } else {
                                            reco = fullOCR.get(aID);
                                            originCountry = reco.getCountryIncorporated1();
                                            destinationCountry = recca.getCountryIncorporated1();
                                            if (!destinationCountry.equalsIgnoreCase(reccd.getCountryIncorporated1())) {
                                                int debug = 1;
                                            }
                                            transitionType = "CU_" + originCountry + "_" + destinationCountry;
                                        }
                                    }
                                } else {
                                    if (addedOCRTime.containsKey(lrID)) {
                                        loa0 = addedOCRTime.get(lrID);
                                        addedOCRTime.remove(lrID);
                                        checkList(loa0, lrID, "added"); // Assume list of 1.
                                        recoa = (LR_OC_COU_Record) loa0.get(0);
                                        destinationCountry = recoa.getCountryIncorporated1();
                                        if (deletedOCRTime.containsKey(lrID)) {
                                            lod0 = deletedOCRTime.get(lrID);
                                            deletedOCRTime.remove(lrID);
                                            checkList(lod0, lrID, "deleted"); // Assume list of 1.
                                            recod = (LR_OC_COU_Record) lod0.get(0);
                                            originCountry = recod.getCountryIncorporated1();
                                            transitionType = "CU_OU_" + originCountry + "_" + destinationCountry;
                                        } else {
                                            transitionType = "CU_OA_United Kingdom_" + destinationCountry;
                                        }
                                    } else {
                                        if (deletedOCRTime.containsKey(lrID)) {
                                            lod0 = deletedOCRTime.get(lrID);
                                            deletedOCRTime.remove(lrID);
                                            checkList(lod0, lrID, "deleted"); // Assume list of 1.
                                            recod = (LR_OC_COU_Record) lod0.get(0);
                                            originCountry = recod.getCountryIncorporated1();
                                            transitionType = "CU_OD_" + originCountry + "_United Kingdom";
                                        } else {
                                            destinationCountry = recca.getCountryIncorporated1();
                                            originCountry = "United Kingdom";
                                            transitionType = "CU_" + originCountry + "_" + destinationCountry;
                                            if (!originCountry.equalsIgnoreCase(destinationCountry)) {
                                                int debug = 1;
                                            }
                                        }
                                    }
                                }
                                addToMappedTransitions(aID, mappedTransitions,
                                        transitions, transitionType,
                                        transitionTypes, transitionTypeCounts,
                                        ld);
                            } else {
                                // Remove from added (changes not significant).
                                if (addedCCRTime.containsKey(aID)) {
                                    addedCCRTime.remove(aID); // This needs more consideration - we could be removing other adds that would make a difference in terms of ownership!
                                } else {
                                    int debug = 1;
                                }
                            }
//                        } else {
//                            System.out.println("Wierdness");
//                        }
                        }
                    } else {
                        if (fullOCR.containsKey(aID)) {
                            if (addedOCRTime.containsKey(aID)) {
                                loa0 = addedOCRTime.get(aID);
                                addedOCRTime.remove(aID);
                                checkList(loa0, aID, "added"); // Assume list of 1.
                                recoa = (LR_OC_COU_Record) loa0.get(0);
                                fullOCR.put(aID, recoa);
                                destinationCountry = recoa.getCountryIncorporated1();
                                if (deletedOCRTime.containsKey(aID)) {
                                    lod0 = deletedOCRTime.get(aID);
                                    deletedOCRTime.remove(aID);
                                    checkList(lod0, aID, "deleted"); // Assume list of 1.
                                    recod = (LR_OC_COU_Record) lod0.get(0);
                                    originCountry = recod.getCountryIncorporated1();
                                    transitionType = "CD_OU_" + originCountry + "_" + destinationCountry;
                                } else {
                                    transitionType = "CD_OA_UK_" + destinationCountry;
                                }
                            } else {
                                if (deletedOCRTime.containsKey(aID)) {
                                    lod0 = deletedOCRTime.get(aID);
                                    deletedOCRTime.remove(aID);
                                    checkList(lod0, aID, "deleted"); // Assume list of 1.
                                    recod = (LR_OC_COU_Record) lod0.get(0);
                                    fullOCR.remove(aID);
                                    originCountry = recod.getCountryIncorporated1();
                                    transitionType = "CD_OD_" + originCountry + "_United Kingdom";
                                } else {
                                    reco = fullOCR.get(aID);
                                    originCountry = reco.getCountryIncorporated1();
                                    transitionType = "CD_O_" + originCountry + "_United Kingdom";
                                }
                            }
                        } else {
                            if (addedOCRTime.containsKey(aID)) {
                                loa0 = addedOCRTime.get(aID);
                                addedOCRTime.remove(aID);
                                checkList(loa0, aID, "added"); // Assume list of 1.
                                recoa = (LR_OC_COU_Record) loa0.get(0);
                                destinationCountry = recoa.getCountryIncorporated1();
                                if (deletedOCRTime.containsKey(aID)) {
                                    lod0 = deletedOCRTime.get(aID);
                                    checkList(lod0, aID, "deleted"); // Assume list of 1.
                                    deletedOCRTime.remove(aID);
                                    recod = (LR_OC_COU_Record) lod0.get(0);
                                    originCountry = recod.getCountryIncorporated1();
                                    transitionType = "CD_OU_" + originCountry + "_" + destinationCountry;
                                } else {
                                    transitionType = "CD_OA_United Kingdom_" + destinationCountry;
                                }
                            } else {
                                if (deletedOCRTime.containsKey(aID)) {
                                    lod0 = deletedOCRTime.get(aID);
                                    deletedOCRTime.remove(aID);
                                    checkList(lod0, aID, "deleted"); // Assume list of 1.
                                    recod = (LR_OC_COU_Record) lod0.get(0);
                                    originCountry = recod.getCountryIncorporated1();
                                    transitionType = "CD_OD_" + originCountry + "United Kingdom";
                                } else {
                                    //destinationCountry = reccca.getCountryIncorporated1();
                                    destinationCountry = reccd.getCountryIncorporated1();
                                    originCountry = "United Kingdom";
                                    transitionType = "CD_" + originCountry + "_" + destinationCountry;
                                    if (!originCountry.equalsIgnoreCase(destinationCountry)) {
                                        int debug = 1;
                                    }
                                }
                            }
                        }
                        addToMappedTransitions(aID, mappedTransitions,
                                transitions, transitionType,
                                transitionTypes, transitionTypeCounts,
                                ld);
                    }
                }
            }
            // Transitions for deletedOCR
            System.out.println("There are " + deletedOCRTime.size() + " deletedOCRTime records to process");
            ite1 = deletedOCRTime.keySet().iterator();
            while (ite1.hasNext()) {
                aID = ite1.next();
                ld = deletedOCRTime.get(aID);
                recod = (LR_OC_COU_Record) ld.get(0);
                if (addedOCRTime.containsKey(aID)) {
                    la = addedOCRTime.get(aID);
                    addedOCRTime.remove(aID);
                    if (printDiff) {
                        m = "Deleted OCR is also added, so this is really updated.";
                        System.out.println(m);
                        System.out.println("Current:");
                        System.out.println("fullOCR.containsKey(aID): " + fullOCR.containsKey(aID));
                        System.out.println("fullCCR.containsKey(aID): " + fullCCR.containsKey(aID));
                        System.out.println("Deleted:");
                        System.out.println(recod);
                        System.out.println("Added:");
                        System.out.println(la.get(0));
                    }
                    checkList(ld, aID, "deleted");
                    checkList(la, aID, "added");
                    Iterator<LR_Record> ite;
                    LR_Record lr;
                    ite = la.iterator();
                    Object[] diff;
                    while (ite.hasNext()) {
                        lr = ite.next();
                        lrID = lr.getPropertyAddressID();
                        recoa = (LR_OC_COU_Record) lr;
                        // Examine difference between fullCCR and cccou 
                        diff = difference(recod, recoa, printDiff);
                        if ((Boolean) diff[2]) {
                            if (addedCCRTime.containsKey(lrID)) {
                                lca0 = addedCCRTime.get(lrID);
                                addedCCRTime.remove(lrID);
                                checkList(lca0, lrID, "added"); // Assume list of 1.
                                recca = (LR_CC_COU_Record) lca0.get(0);
                                fullCCR.put(lrID, recca);
                                destinationCountry = recca.getCountryIncorporated1();
                                if (deletedOCRTime.containsKey(aID)) {
                                    int debug = 1; // This should not happen as already dealt with this.
                                    transitionType = "?";
                                } else {
                                    originCountry = recod.getCountryIncorporated1();
                                    transitionType = "CU_OA_" + originCountry + "_" + destinationCountry;
                                }
                            } else {
                                originCountry = recod.getCountryIncorporated1();
                                destinationCountry = recoa.getCountryIncorporated1();
                                transitionType = "CU_" + originCountry + "_" + destinationCountry;
                            }
                            addToMappedTransitions(aID, mappedTransitions,
                                    transitions, transitionType,
                                    transitionTypes, transitionTypeCounts,
                                    ld);
                        } else {
                            fullOCR.put(lrID, recoa);
                        }
                    }
                } else {
                    originCountry = recod.getCountryIncorporated1();
                    destinationCountry = "United Kingdom";
                    transitionType = "CD_" + originCountry + "_" + destinationCountry;
                    addToMappedTransitions(aID, mappedTransitions,
                            transitions, transitionType,
                            transitionTypes, transitionTypeCounts,
                            ld);
                }

            }

            // Transitions for addedCCR
            System.out.println("There are up to " + addedCCRTime.size() + " addedCCRTime records to process");
            ite1 = addedCCRTime.keySet().iterator();
            while (ite1.hasNext()) {
                aID = ite1.next();
                la = addedCCRTime.get(aID);
                recca = (LR_CC_COU_Record) la.get(0);
                if (addedOCRTime.containsKey(aID)) {
                    addedOCRTime.remove(aID);
                    if (fullCCR.containsKey(aID) || fullOCR.containsKey(aID)) {
                        int debug = 1;
                        transitionType = "?";
                    } else {
                        originCountry = "United Kingdom";
                        destinationCountry = recca.getCountryIncorporated1();
                        transitionType = "CA_OA_" + originCountry + "_" + destinationCountry;
                    }
                } else {
                    originCountry = "United Kingdom";
                    destinationCountry = recca.getCountryIncorporated1();
                    transitionType = "CA_" + originCountry + "_" + destinationCountry;
                }
                addToMappedTransitions(aID, mappedTransitions,
                        transitions, transitionType,
                        transitionTypes, transitionTypeCounts,
                        la);
            }
            // Transitions for addedOCR
            System.out.println("There are up to " + addedOCRTime.size() + " addedOCRTime records to process");
            ite1 = addedOCRTime.keySet().iterator();
            while (ite1.hasNext()) {
                aID = ite1.next();
                la = addedOCRTime.get(aID);
                recoa = (LR_OC_COU_Record) la.get(0);
                originCountry = "United Kingdom";
                destinationCountry = recoa.getCountryIncorporated1();
                transitionType = "OA_" + originCountry + "_" + destinationCountry;
                addToMappedTransitions(aID, mappedTransitions,
                        transitions, transitionType,
                        transitionTypes, transitionTypeCounts,
                        la);
            }
        }
        // Write out transition results
        pw.println("Count,TransitionType");
        Iterator<String> ite4;
        ite4 = transitionTypeCounts.keySet().iterator();
        while (ite4.hasNext()) {
            transitionType = ite4.next();
            transitionTypeCount = transitionTypeCounts.get(transitionType);
            pw.println("" + transitionTypeCount + "," + transitionType);
        }
        pw.close();
    }

    protected int checkList(ArrayList<LR_Record> l, LR_ID aID, String name) {
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
     */
    protected void addToMappedTransitions(LR_ID aID,
            HashMap<LR_ID, ArrayList<ArrayList<LR_Record>>> mappedTransitions,
            ArrayList<ArrayList<LR_Record>> transitions,
            String transitionType0,
            HashMap<LR_ID, String> transitionTypes,
            TreeMap<String, Integer> transitionTypeCounts,
            ArrayList<LR_Record> l) {
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
            transitionType += "->";
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
            titleNumberChange = doDiff("TitleNumber", s0, s1, printDiff, titleNumberChange);
            changeCount++;
        }
        s0 = f.getCompanyRegistrationNo1();
        s1 = c.getCompanyRegistrationNo1();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setCompanyRegistrationNo1(s1);
            ownershipChange = doDiff("CompanyRegistrationNo1", s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        s0 = f.getCompanyRegistrationNo2();
        s1 = c.getCompanyRegistrationNo2();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setCompanyRegistrationNo2(s1);
            ownershipChange = doDiff("CompanyRegistrationNo2", s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        s0 = f.getCompanyRegistrationNo3();
        s1 = c.getCompanyRegistrationNo3();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setCompanyRegistrationNo3(s1);
            ownershipChange = doDiff("CompanyRegistrationNo3", s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        s0 = f.getCompanyRegistrationNo4();
        s1 = c.getCompanyRegistrationNo4();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setCompanyRegistrationNo4(s1);
            ownershipChange = doDiff("CompanyRegistrationNo4", s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        s0 = f.getPricePaid();
        s1 = c.getPricePaid();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setPricePaid(s1);
            if (!s0.trim().isEmpty()) {
                if (printDiff) {
                    s3 = "PricePaid changed from " + s0 + " to " + s1;
                    System.out.println(s3);
                    if (!s1.trim().isEmpty()) {
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
            tenureChange = doDiff("Tenure", s0, s1, printDiff, tenureChange);
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
            f.setPostcode(s1);
            doDiff("Postcode", s0, s1, printDiff, true);
            changeCount++;
        }
        s0 = f.getPropertyAddress();
        s1 = c.getPropertyAddress();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setPropertyAddress(s1);
            doDiff("PropertyAddress", s0, s1, printDiff, true);
            changeCount++;
        }
        // ProprietorNames
        s0 = f.getProprietorName1();
        s1 = c.getProprietorName1();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setProprietorName1(s1);
            ownershipChange = doDiff(
                    "ProprietorName1", s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        s0 = f.getProprietorName2();
        s1 = c.getProprietorName2();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setProprietorName2(s1);
            ownershipChange = doDiff(
                    "ProprietorName2", s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        s0 = f.getProprietorName3();
        s1 = c.getProprietorName3();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setProprietorName3(s1);
            ownershipChange = doDiff("ProprietorName3",
                    s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        s0 = f.getProprietorName4();
        s1 = c.getProprietorName4();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setProprietorName4(s1);
            ownershipChange = doDiff("ProprietorName4",
                    s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        //ProprietorshipCategory
        s0 = f.getProprietorshipCategory1();
        s1 = c.getProprietorshipCategory1();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setProprietorshipCategory1(s1);
            ownershipChange = doDiff("ProprietorshipCategory1",
                    s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        s0 = f.getProprietorshipCategory2();
        s1 = c.getProprietorshipCategory2();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setProprietorshipCategory2(s1);
            ownershipChange = doDiff("ProprietorshipCategory2",
                    s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        s0 = f.getProprietorshipCategory3();
        s1 = c.getProprietorshipCategory3();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setProprietorshipCategory3(s1);
            ownershipChange = doDiff("ProprietorshipCategory3",
                    s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        s0 = f.getProprietorshipCategory4();
        s1 = c.getProprietorshipCategory4();
        if (!s0.equalsIgnoreCase(s1)) {
            f.setProprietorshipCategory4(s1);
            ownershipChange = doDiff("ProprietorshipCategory4",
                    s0, s1, printDiff, ownershipChange);
            changeCount++;
        }
        result[0] = changeCount;
        result[1] = titleNumberChange;
        result[2] = ownershipChange;
        result[3] = tenureChange;
        return result;
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

    void add(HashMap<LR_ID, ArrayList<LR_Record>> added,
            HashMap<LR_ID, ArrayList<LR_Record>> deleted,
            LR_CC_COU_Record r) {
        LR_ID aID;
        String changeIndicator;
        ArrayList<LR_Record> l;
        aID = r.getPropertyAddressID();
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

    void add(HashMap<LR_ID, ArrayList<LR_Record>> added,
            HashMap<LR_ID, ArrayList<LR_Record>> deleted,
            LR_OC_COU_Record r) {
        LR_ID aID;
        String changeIndicator;
        ArrayList<LR_Record> l;
        aID = r.getPropertyAddressID();
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
            HashMap<LR_ID, Integer> counts, int min
    ) {
        pw.println(type);
        if (!counts.isEmpty()) {
            Map<LR_ID, Integer> sortedCounts;
            sortedCounts = Generic_Collections.sortByValue(counts);
            LR_ID aID;
            int count;
            int smallCount = 0;
            boolean reportedSmallCount = false;
            Iterator<LR_ID> ite;
            pw.println("Address, Count");
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
                        pw.println("\"" + Env.IDToPropertyAddress.get(aID) + "\"," + count);
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
