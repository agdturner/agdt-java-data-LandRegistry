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
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.LR_Record;

/**
 * For reading and processing data from
 *
 * @author geoagdt
 */
public class LR_Generalise_Process extends LR_Main_Process {

    public LR_Generalise_Process() {
        super();
    }

    TreeMap<String, Integer> tenureCounts;
//    TreeMap<String, Integer> districtCounts;
//    TreeMap<String, Integer> countyCounts;
//    TreeMap<String, Integer> regionCounts;
//    TreeMap<String, Integer> postcodeCounts;
//    TreeMap<String, Integer> multipleAddressIndicatorCounts;
//    TreeMap<String, Integer> PricePaidCounts;
    TreeMap<String, Integer> companyRegistrationNo1Counts;
    TreeMap<String, Integer> proprietorshipCategory1Counts;
    TreeMap<String, Integer> proprietorName1Counts;
    TreeMap<String, Integer> countryIncorporated1Counts;

    /**
     *
     * @param IDToAddress
     * @param AddressToID
     * @param area
     * @param doAll
     * @param minCC
     * @param minOC
     * @param inputDataDir
     * @param doCCOD
     * @param doOCOD
     * @param doFull
     */
    public void run(HashMap<LR_ID, String> IDToAddress,
            HashMap<String, LR_ID> AddressToID, String area, boolean doAll, int minCC, int minOC,
            File inputDataDir, boolean doCCOD, boolean doOCOD, boolean doFull) {
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
        if (doCCOD) {
            names0.add("CCOD");
        }
        if (doOCOD) {
            names0.add("OCOD");
        }
        //names1.add("COU");
        //names1.add("FULL");
        boolean isCCOD;
        if (doFull) {
            names2.add("2017_11");
            names2.add("2018_02");
        } else {
            names2.add("2017_11");
            names2.add("2017_12");
            names2.add("2018_01");
            names2.add("2018_02");
            names2.add("2018_03");
        }
        File indir;
        File outdir;
        File f;
        ArrayList<String> lines;
        PrintWriter pw = null;

        Iterator<String> ite0;
        Iterator<String> ite2;
        ite0 = names0.iterator();
        while (ite0.hasNext()) {
            name0 = ite0.next();
            isCCOD = name0.equalsIgnoreCase("CCOD");
            name00 = "";
            if (doFull) {
                name00 += name0 + "_FULL_";
            } else {
                name00 += name0 + "_COU_";
            }
            ite2 = names2.iterator();
            while (ite2.hasNext()) {
                name = name00;
                name += ite2.next();
                if (doFull) {
                    indir = new File(inputDataDir, name0);
                    indir = new File(indir, name);
                    if (doAll) {
                        outdir = new File(outputDataDir, "GeneralisedFull");
                    } else {
                        outdir = new File(outputDataDir, area + "GeneralisedFull");
                    }
                } else {
                    indir = new File(outputDataDir, area);
                    indir = new File(indir, name0);
                    outdir = new File(outputDataDir, area + "_Generalised");
                }
                System.out.println("indir " + indir);
                outdir = new File(outdir, name0);
                outdir = new File(outdir, name);
                System.out.println("outdir " + outdir);
                f = new File(indir, name + ".csv");
                if (!f.exists()) {
                    System.out.println("File " + f + " does not exist.");
                }
                lines = Generic_ReadCSV.read(f, null, 7);
                outdir.mkdirs();
                f = new File(outdir, name + "Generalised.csv");
                try {
                    pw = new PrintWriter(f);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(LR_Generalise_Process.class.getName()).log(Level.SEVERE, null, ex);
                }
                // Initialisation
                tenureCounts = new TreeMap<>();
//                districtCounts = new TreeMap<>();
//                countyCounts = new TreeMap<>();
//                regionCounts = new TreeMap<>();
//                postcodeCounts = new TreeMap<>();
//                multipleAddressIndicatorCounts = new TreeMap<>();
//                PricePaidCounts = new TreeMap<>();
                companyRegistrationNo1Counts = new TreeMap<>();
                proprietorshipCategory1Counts = new TreeMap<>();
                proprietorName1Counts = new TreeMap<>();
                countryIncorporated1Counts = new TreeMap<>();

                LR_Record r;
                for (int ID = 1; ID < lines.size(); ID++) {
                    try {
                        if (isCCOD) {
                            r = new LR_CC_COU_Record(IDToAddress, AddressToID,
                                    lines.get((int) ID));
                        } else {
                            r = new LR_OC_COU_Record(IDToAddress, AddressToID,
                                    lines.get((int) ID));
                        }
                        addToCounts(r);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace(System.err);
                    }
                }
                if (isCCOD) {
                    printGeneralisation(pw, minCC);
                } else {
                    printGeneralisation(pw, minOC);
                }
                pw.close();
            }
        }

        // Write out summaries.
    }

    void addToCounts(LR_Record r) {
        Generic_Collections.addToTreeMapStringInteger(tenureCounts, r.getTenure(), 1);
//        Generic_Collections.addToTreeMapStringInteger(districtCounts, r.getDistrict(), 1);
//        Generic_Collections.addToTreeMapStringInteger(countyCounts, r.getCounty(), 1);
//        Generic_Collections.addToTreeMapStringInteger(postcodeCounts, r.getPostcode(), 1);
//        Generic_Collections.addToTreeMapStringInteger(multipleAddressIndicatorCounts, r.getMultipleAddressIndicator(), 1);
//        Generic_Collections.addToTreeMapStringInteger(PricePaidCounts, r.getPricePaid(), 1);
        Generic_Collections.addToTreeMapStringInteger(companyRegistrationNo1Counts, r.getCompanyRegistrationNo1(), 1);
        Generic_Collections.addToTreeMapStringInteger(proprietorshipCategory1Counts, r.getProprietorshipCategory1(), 1);
        Generic_Collections.addToTreeMapStringInteger(proprietorName1Counts, r.getProprietorName1(), 1);
        Generic_Collections.addToTreeMapStringInteger(countryIncorporated1Counts, r.getCountryIncorporated1(), 1);
    }

    void printGeneralisation(PrintWriter pw, int min) {
        printGeneralisation(pw, "Tenure", tenureCounts, min);
//        printGeneralisation(pw, "District", districtCounts);
//        printGeneralisation(pw, "County", countyCounts);
//        printGeneralisation(pw, "Postcode", postcodeCounts);
        printGeneralisation(pw, "Company Registration No 1", companyRegistrationNo1Counts, min);
        printGeneralisation(pw, "Proprietorship Category 1", proprietorshipCategory1Counts, min);
        printGeneralisation(pw, "Proprietor Name 1", proprietorName1Counts, min);
        printGeneralisation(pw, "Country Incorporated 1", countryIncorporated1Counts, min);

    }

    void printGeneralisation(PrintWriter pw, String type,
            TreeMap<String, Integer> counts, int min) {
        Map<String, Integer> sortedCounts;
        sortedCounts = Generic_Collections.sortByValue(counts);
        pw.println(type);
        String var;
        int count;
        int smallCount = 0;
        boolean reportedSmallCount = false;
        Iterator<String> ite;
        pw.println("Value, Count");
        ite = sortedCounts.keySet().iterator();
        while (ite.hasNext()) {
            var = ite.next();
            count = counts.get(var);
            if (count >= min) {
                if (!reportedSmallCount) {
                    pw.println("Those with less than " + min + "," + smallCount);
                    reportedSmallCount = true;
                }
                pw.println(var + "," + count);
            } else {
                smallCount += count;
            }
        }
        pw.println();
    }
}
