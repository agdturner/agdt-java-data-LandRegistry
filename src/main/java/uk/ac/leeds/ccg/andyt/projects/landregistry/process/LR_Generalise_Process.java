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
import uk.ac.leeds.ccg.andyt.generic.lang.Generic_StaticString;
import uk.ac.leeds.ccg.andyt.generic.utilities.Generic_Collections;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Environment;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_ID;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.landregistry.LR_CC_COU_Record;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.landregistry.LR_OC_COU_Record;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.landregistry.LR_Record;

/**
 * For reading and processing data from
 *
 * @author geoagdt
 */
public class LR_Generalise_Process extends LR_Main_Process {

    protected LR_Generalise_Process() {
        super();
    }

    public LR_Generalise_Process(LR_Environment env) {
        super(env);
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
//    TreeMap<String, Integer> proprietorNameID1s;
    TreeMap<LR_ID, Integer> proprietorName1IDCounts;
    TreeMap<String, Integer> countryIncorporated1Counts;
    HashMap<String, Integer> transparencyMap;

    /**
     * @param area
     * @param doAll
     * @param minCC
     * @param minOC
     * @param inputDataDir
     * @param doCCOD
     * @param doOCOD
     * @param doFull
     * @param overwrite
     */
    public void run(String area, boolean doAll,
            int minCC, int minOC, File inputDataDir, boolean doCCOD,
            boolean doOCOD, boolean doFull, boolean overwrite) {

        File outputDataDir;
        outputDataDir = Files.getOutputDataDir(Strings);
        ArrayList<String> names0;
        //ArrayList<String> names1;
        ArrayList<String> setNames;
        String name;
        String name0;
        String name00;
        names0 = new ArrayList<>();
        if (doCCOD) {
            names0.add("CCOD");
        }
        if (doOCOD) {
            names0.add("OCOD");
        }
        boolean isCCOD;
        setNames = getSetNames(doFull);

        // Initialise transparencyMap
        loadTransparencyMap();

        File indir;
        File outdir;
        File fin;
        File fout;
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
            ite2 = setNames.iterator();
            while (ite2.hasNext()) {
                name = name00;
                name += ite2.next();
                if (doFull) {
                    indir = new File(inputDataDir, name0);
                    indir = new File(indir, name);
                    if (doAll) {
                        outdir = new File(outputDataDir, "GeneralisedFull");
                    } else {
                        indir = new File(outputDataDir, area);
                        indir = new File(indir, name0);
                        indir = new File(indir, name);
                        outdir = new File(outputDataDir, area + "GeneralisedFull");
                    }
                } else {
                    indir = new File(outputDataDir, area);
                    indir = new File(indir, name0);
                    indir = new File(indir, name);
                    outdir = new File(outputDataDir, area + "_Generalised");
                }
                System.out.println("indir " + indir);
                outdir = new File(outdir, name0);
                outdir = new File(outdir, name);
                System.out.println("outdir " + outdir);
                fin = new File(indir, name + ".csv");
                if (!fin.exists()) {
                    System.out.println("File " + fin + " does not exist.");
                } else {
                    outdir.mkdirs();
                    fout = new File(outdir, name + "Generalised.csv");
                    if (!overwrite || !fout.exists()) {
                        lines = Generic_ReadCSV.read(fin, null, 7);
                        try {
                            pw = new PrintWriter(fout);
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
                        proprietorName1IDCounts = new TreeMap<>();
                        countryIncorporated1Counts = new TreeMap<>();

                        LR_Record r;
                        for (int ID = 1; ID < lines.size(); ID++) {
                            try {
                                if (isCCOD) {
                                    r = new LR_CC_COU_Record(Env,
                                            lines.get((int) ID));
                                } else {
                                    r = new LR_OC_COU_Record(Env,
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
                    } else {
                        System.out.println("Output file " + fout + " already exists and is not being overwritten.");
                    }
                }
            }
        }

        // Write out summaries.
        System.out.println(" Write out summaries");

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
//        LR_ID id;
//        id = null;
//        Generic_Collections.addToMap(proprietorName1Counts, id, 1);
        Generic_Collections.addToMap(proprietorName1IDCounts, r.getProprietorName1ID(), 1);
//        Generic_Collections.addToTreeMapStringInteger(proprietorName1IDCounts, r.getProprietorName1ID(), 1);
        Generic_Collections.addToTreeMapStringInteger(countryIncorporated1Counts, r.getCountryIncorporated1(), 1);
    }

    void printGeneralisation(PrintWriter pw, int min) {
        printGeneralisation(pw, "Tenure", tenureCounts, null, min);
//        printGeneralisation(pw, "District", districtCounts);
//        printGeneralisation(pw, "County", countyCounts);
//        printGeneralisation(pw, "Postcode", postcodeCounts);
        printGeneralisation(pw, "Company Registration No 1", companyRegistrationNo1Counts, null, min);
        printGeneralisation(pw, "Proprietorship Category 1", proprietorshipCategory1Counts, null, min);
        printGeneralisation(pw, "Proprietor Name 1", proprietorName1IDCounts, Env.IDToProprietorName, min);
        printGeneralisation(pw, "Country Incorporated 1", countryIncorporated1Counts, min, transparencyMap);
        //printGeneralisation(pw, "Country Incorporated 1", countryIncorporated1Counts, null, min);
    }

//    void printGeneralisation(PrintWriter pw, String type,
//            TreeMap<String, Integer> counts, int min) {
    /**
     *
     * @param <K>
     * @param pw
     * @param type
     * @param counts
     * @param lookup
     * @param min
     */
    <K> void printGeneralisation(PrintWriter pw, String type,
            Map<K, Integer> counts, Map<K, String> lookup, int min) {
        Map<K, Integer> sortedCounts;
        sortedCounts = Generic_Collections.sortByValue(counts);
        pw.println(type);
        K k;
        Integer count;
        int smallCount = 0;
        boolean reportedSmallCount = false;
        Iterator<K> ite;
        pw.println("Value, Count");
        ite = sortedCounts.keySet().iterator();
        while (ite.hasNext()) {
            k = ite.next();
//            //Debug code
//            if (counts == null) {
//                int debug = 1;
//            }
//            if (k == null) {
//                int debug = 1;
//            }
            count = counts.get(k);
            if (count == null) {
                count = 0;
            }
            if (count >= min) {
                if (!reportedSmallCount) {
                    pw.println("Those with less than " + min + "," + smallCount);
                    reportedSmallCount = true;
                }
                if (lookup == null) {
                    pw.println("\"" + k + "\"," + count);
                } else {
                    String v;
                    v = lookup.get(k);
                    pw.println("\"" + v + "\"," + count);
                }
            } else {
                smallCount += count;
            }
        }
        pw.println();
    }

    void printGeneralisation(PrintWriter pw, String type,
            TreeMap<String, Integer> counts, int min, 
            HashMap<String, Integer> transparencyMap) {
        Map<String, Integer> sortedCounts;
        sortedCounts = Generic_Collections.sortByValue(counts);
        pw.println(type);
        String var;
        int count;
        int smallCount = 0;
        boolean reportedSmallCount = false;
        Iterator<String> ite;
        pw.println("Value, Count, CPIScore2017");
        ite = sortedCounts.keySet().iterator();
        Integer CPIScore2017;
        while (ite.hasNext()) {
            var = ite.next();
            count = counts.get(var);
            if (count >= min) {
                if (!reportedSmallCount) {
                    pw.println("Those with less than " + min + "," + smallCount);
                    reportedSmallCount = true;
                }

                if (var == null) {
                    int debug = 1;
                }
                if (transparencyMap == null) {
                    int debug = 1;
                }

                CPIScore2017 = transparencyMap.get(var);
                if (CPIScore2017 != null) {
                    pw.println("\"" + var + "\"," + count + "," + CPIScore2017);
                } else {
                    pw.println("\"" + var + "\"," + count + ",UnknownCPIScore");
                }
            } else {
                smallCount += count;
            }
        }
        pw.println();
    }

    // load transparencyMap
    protected void loadTransparencyMap() {
        transparencyMap = new HashMap<>();
        File f;
        f = Files.getTIDataFile(Strings);
        ArrayList<String> lines;
        lines = Generic_ReadCSV.read(f, null, 7);
        Iterator<String> ite;
        ite = lines.iterator();
        String l;
        String[] split;
        // Skip header
        l = ite.next();
        System.out.println(l);
        String cname;
        while (ite.hasNext()) {
            l = ite.next();
            split = l.split("\"");
            if (split.length == 1) {
                split = split[0].split(",");
                cname = split[0];
            } else {
                cname = split[1];
                split = split[2].split(",");
            }
            transparencyMap.put(Generic_StaticString.getUpperCase(cname), 
                    Integer.valueOf(split[2]));
        }
    }
}
