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
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_ReadCSV;
import uk.ac.leeds.ccg.andyt.generic.utilities.Generic_Collections;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_ID;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.LR_CC_COU_Record;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.LR_OC_COU_Record;

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

    HashMap<LR_ID, Integer> addedCCR;
    HashMap<LR_ID, Integer> deletedCCR;
    HashMap<LR_ID, Integer> addedOCR;
    HashMap<LR_ID, Integer> deletedOCR;

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
        names2.add("2017_11");
        names2.add("2017_12");
        names2.add("2018_01");
        names2.add("2018_02");
        names2.add("2018_03");

        addedCCR = new HashMap<>();
        deletedCCR = new HashMap<>();
        addedOCR = new HashMap<>();
        deletedOCR = new HashMap<>();

        File indir;
        File outdir;
        File f;
        ArrayList<String> lines;
        PrintWriter pw = null;

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
                name += ite2.next();
                indir = new File(outputDataDir, area);
                indir = new File(indir, name0);
                indir = new File(indir, name);
                System.out.println("indir " + indir);
                outdir = new File(outputDataDir, area + "Transitions");
                outdir = new File(outdir, name0);
                outdir = new File(outdir, name);
                System.out.println("outdir " + outdir);
                f = new File(indir, name + ".csv");
                if (!f.exists()) {
                    System.out.println("File " + f + " does not exist.");
                }
                lines = Generic_ReadCSV.read(f, null, 7);
                outdir.mkdirs();
                f = new File(outdir, name + "Transitions.csv");
                try {
                    pw = new PrintWriter(f);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(LR_Transitions_Process.class.getName()).log(Level.SEVERE, null, ex);
                }
                //LR_Record r;
                for (int ID = 1; ID < lines.size(); ID++) {
                    try {
                        if (isCCOD) {
                            ccr = new LR_CC_COU_Record(IDToAddress, AddressToID,
                                    lines.get(ID));
                            addToCounts(ccr);
                        } else {
                            ocr = new LR_OC_COU_Record(IDToAddress, AddressToID,
                                    lines.get(ID));
                            addToCounts(ocr);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace(System.err);
                    }
                }
                printGeneralisation(pw, minCC, minOC);
                pw.close();
            }
        }

        // Write out summaries.
    }

    void addToCounts(LR_CC_COU_Record r) {
        String changeIndicator;
        changeIndicator = r.getChangeIndicator();
        if (changeIndicator.equalsIgnoreCase("A")) {
            Generic_Collections.addToMap(addedCCR, r.getID(), 1);
        } else {
            Generic_Collections.addToMap(deletedCCR, r.getID(), 1);
        }
    }

    void addToCounts(LR_OC_COU_Record r) {
        String changeIndicator;
        changeIndicator = r.getChangeIndicator();
        if (changeIndicator.equalsIgnoreCase("A")) {
            Generic_Collections.addToMap(addedOCR, r.getID(), 1);
        } else {
            Generic_Collections.addToMap(deletedOCR, r.getID(), 1);
        }
    }

    void printGeneralisation(PrintWriter pw, int minCC, int minOC) {
        printGeneralisation(pw, "addedCCR", addedCCR, minCC);
        printGeneralisation(pw, "deletedCCR", deletedCCR, minCC);
        printGeneralisation(pw, "addedOCR", addedOCR, minOC);
        printGeneralisation(pw, "deletedOCR", deletedOCR, minOC);
    }

    void printGeneralisation(PrintWriter pw, String type,
            HashMap<LR_ID, Integer> counts, int min) {
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
