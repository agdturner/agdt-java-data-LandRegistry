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
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_ReadCSV;
import uk.ac.leeds.ccg.andyt.generic.utilities.Generic_Collections;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_ID;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.landregistry.LR_CC_COU_Record;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.landregistry.LR_OC_COU_Record;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.landregistry.LR_Record;

/**
 * For reading and processing overseas and corporate ownership land registry
 * data.
 *
 * @author Andy Turner
 */
public class LR_Transitions_Process extends LR_Main_Process {

    public LR_Transitions_Process() {
        super();
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

    public void run(HashMap<LR_ID, String> IDToAddress,
            HashMap<String, LR_ID> AddressToID, String area, File inputDataDir,
            int minCC, int minOC) {
        File outputDataDir;
        outputDataDir = Files.getOutputDataDir(Strings);

        this.AddressToID = AddressToID;
        this.IDToAddress = IDToAddress;

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

        Iterator<String> ite0;
        Iterator<String> ite2;
        ite0 = names0.iterator();
        while (ite0.hasNext()) {
            name0 = ite0.next();
            isCCOD = name0.equalsIgnoreCase("CCOD");
            name00 = "";
            name00 += name0 + "_COU_";
            //name00 += name0 + "_FULL_";
            ite2 = names2.iterator();
            while (ite2.hasNext()) {
                name = name00;
                String time;
                time = ite2.next();
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
                            ccr = new LR_CC_COU_Record(IDToAddress, AddressToID,
                                    lines.get(ID));
                            add(addedCCRTime, deletedCCRTime, ccr);
                        } else {
                            ocr = new LR_OC_COU_Record(IDToAddress, AddressToID,
                                    lines.get(ID));
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
        ArrayList<ArrayList<LR_Record>> transitions;

        /**
         * For records in transitions.
         */
        LR_Record r;

        /**
         * List of LR_Record records in transitions.
         */
        ArrayList<LR_Record> l;

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

        Iterator<LR_ID> ite3;
        LR_ID aID;
        // Go through each time in order
        ite2 = names2.iterator();
        while (ite2.hasNext()) {
            String time;
            time = ite2.next();
            addedCCRTime = addedCCR.get(time);
            deletedCCRTime = deletedCCR.get(time);
            addedOCRTime = addedOCR.get(time);
            deletedOCRTime = deletedOCR.get(time);
            // Transitions for deletedCCR
            ite3 = deletedCCRTime.keySet().iterator();
            while (ite3.hasNext()) {
                aID = ite3.next();
                l = deletedCCRTime.get(aID);
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
                    transitionType += "->";
                }
                transitionType += "DCC";
                transitionTypes.put(aID, transitionType);
                transitions.add(l);
                Generic_Collections.addToMap(transitionTypeCounts, transitionType, 1);
            }
            // Transitions for deletedOCR
            ite3 = deletedOCRTime.keySet().iterator();
            while (ite3.hasNext()) {
                aID = ite3.next();
                l = deletedOCRTime.get(aID);
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
                    transitionType += "->";
                }
                transitionType += "DOC";
                transitionTypes.put(aID, transitionType);
                transitions.add(l);
                Generic_Collections.addToMap(transitionTypeCounts, transitionType, 1);
            }
            // Transitions for addedCCR
            ite3 = addedCCRTime.keySet().iterator();
            while (ite3.hasNext()) {
                aID = ite3.next();
                l = addedCCRTime.get(aID);
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
                    transitionType += "->";
                }
                transitionType += "ACC";
                transitionTypes.put(aID, transitionType);
                transitions.add(l);
                Generic_Collections.addToMap(transitionTypeCounts, transitionType, 1);
            }
            // Transitions for addedOCR
            ite3 = addedOCRTime.keySet().iterator();
            while (ite3.hasNext()) {
                aID = ite3.next();
                l = addedOCRTime.get(aID);
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
                    transitionType += "->";
                }
                transitionType += "AOC";
                transitionTypes.put(aID, transitionType);
                transitions.add(l);
                Generic_Collections.addToMap(transitionTypeCounts, transitionType, 1);
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

    void add(HashMap<LR_ID, ArrayList<LR_Record>> added,
            HashMap<LR_ID, ArrayList<LR_Record>> deleted,
            LR_CC_COU_Record r) {
        LR_ID aID;
        String changeIndicator;
        ArrayList<LR_Record> l;
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

    void add(HashMap<LR_ID, ArrayList<LR_Record>> added,
            HashMap<LR_ID, ArrayList<LR_Record>> deleted,
            LR_OC_COU_Record r) {
        LR_ID aID;
        String changeIndicator;
        ArrayList<LR_Record> l;
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
                        pw.println("\"" + IDToAddress.get(aID) + "\"," + count);
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
