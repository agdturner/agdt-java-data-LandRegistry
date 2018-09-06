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
import uk.ac.leeds.ccg.andyt.generic.utilities.time.Generic_YearMonth;
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

    HashMap<LR_ID, TreeMap<LR_ID, Integer>> Counts;
    ArrayList<LR_ID> Types;
    HashMap<LR_ID, String> IDToType;
    HashMap<String, LR_ID> TypeToID;

////    TreeMap<String, Integer> districtCounts;
////    TreeMap<String, Integer> countyCounts;
////    TreeMap<String, Integer> regionCounts;
////    TreeMap<String, Integer> postcodeCounts;
////    TreeMap<String, Integer> multipleAddressIndicatorCounts;
////    TreeMap<String, Integer> PricePaidCounts;
//    TreeMap<String, Integer> companyRegistrationNo1Counts;
//    TreeMap<String, Integer> proprietorshipCategory1Counts;
////    TreeMap<String, Integer> proprietorNameID1s;
//    TreeMap<LR_ID, Integer> proprietorName1IDCounts;
//    TreeMap<String, Integer> countryIncorporated1Counts;
    HashMap<String, Integer> TransparencyMap;

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
            names0.add(Strings.S_CCOD);
        }
        if (doOCOD) {
            names0.add(Strings.S_OCOD);
        }
        boolean isCCOD;

        // Initialise transparencyMap
        loadTransparencyMap();

        File indir;
        File outdir;
        File fin;
        File fout;
        ArrayList<String> lines;
        HashMap<LR_ID, PrintWriter> pws;
        // Initialise Types, IDToType and TypeToID.
        initTypeIDs();

        pws = new HashMap<>();
        Iterator<LR_ID> iteTypes;
        String type;
        LR_ID typeID;

        Iterator<String> ite0;
        Iterator<String> ite2;
        ite0 = names0.iterator();
        while (ite0.hasNext()) {
            name0 = ite0.next();
            isCCOD = name0.equalsIgnoreCase(Strings.S_CCOD);
            name00 = getName00(doFull, name0);
            setNames = getSetNames(doFull, name0);
            ite2 = setNames.iterator();
            while (ite2.hasNext()) {
                name = name00;
                name += ite2.next();
                if (doFull) {
                    indir = new File(inputDataDir, name0);
                    indir = new File(indir, name);
                    if (doAll) {
                        outdir = new File(outputDataDir, 
                                Strings.S_Generalised + Strings.S_FULL);
                    } else {
                        indir = new File(outputDataDir, area);
                        indir = new File(indir, name0);
                        indir = new File(indir, name);
                        outdir = new File(outputDataDir, 
                                area + Strings.S_Generalised + Strings.S_FULL);
                    }
                } else {
                    indir = new File(outputDataDir, area);
                    indir = new File(indir, name0);
                    indir = new File(indir, name);
                    outdir = new File(outputDataDir, 
                            area + Strings.S_Generalised + Strings.S_COU);
                }
                System.out.println("indir " + indir);
                outdir = new File(outdir, name0);
                outdir = new File(outdir, name);
                outdir = new File(outdir, Strings.S_Generalised);
                System.out.println("outdir " + outdir);
                fin = new File(indir, name + ".csv");
                if (!fin.exists()) {
                    System.out.println("File " + fin + " does not exist.");
                } else {
                    if (overwrite || !outdir.exists()) {
                        lines = Generic_ReadCSV.read(fin, null, 7);
                        // Initialise printwriters
                        try {
                            outdir.mkdirs();
                            iteTypes = Types.iterator();
                            while (iteTypes.hasNext()) {
                                typeID = iteTypes.next();
                                type = IDToType.get(typeID);
                                fout = new File(outdir, type.replaceAll(" ", "_") + ".csv");
                                pws.put(typeID, new PrintWriter(fout));
                            }
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(LR_Generalise_Process.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        // Initialise counts
                        Counts = new HashMap<>();
                        iteTypes = Types.iterator();
                        while (iteTypes.hasNext()) {
                            typeID = iteTypes.next();
                            Counts.put(typeID, new TreeMap<>());
                        }
                        
                        Generic_YearMonth YM = null;
                        
                        LR_Record r;
                        for (int ID = 1; ID < lines.size(); ID++) {
                            try {
                                if (isCCOD) {
                                    r = new LR_CC_COU_Record(Env, YM,
                                            lines.get((int) ID));
                                } else {
                                    r = new LR_OC_COU_Record(Env, YM,
                                            lines.get((int) ID));
                                }
                                addToCounts(r);
                            } catch (ArrayIndexOutOfBoundsException e) {
                                e.printStackTrace(System.err);
                            }
                        }
                        if (isCCOD) {
                            printGeneralisation(pws, minCC);
                        } else {
                            printGeneralisation(pws, minOC);
                        }
                        // Close printWriters
                        iteTypes = Types.iterator();
                        while (iteTypes.hasNext()) {
                            typeID = iteTypes.next();
                            pws.get(typeID).close();
                        }
                    } else {
                        System.out.println("Output directory " + outdir
                                + " already exists and overwrite is false. "
                                + "Please delete output directory or set "
                                + "overwrite to false in order to generate new "
                                + "results.");
                    }
                }
            }
        }

        // Write out summaries.
        System.out.println(" Write out summaries");

    }

    protected void addType(String type) {
        LR_ID id;
        id = new LR_ID(TypeToID.size());
        TypeToID.put(type, id);
        IDToType.put(id, type);
        Types.add(id);
    }

    protected void initTypeIDs() {
        Types = new ArrayList<>();
        TypeToID = new HashMap<>();
        IDToType = new HashMap<>();
        String s;
        LR_ID typeID;
        s = "Tenure";
        addType(s);
        Env.TypeIDTenure = TypeToID.get(s);
//        s = "District";
//        addType(s);
//        s = "County";
//        addType(s);
        s = "Company Registration No 1";
        addType(s);
        Env.TypeIDCompanyRegistrationNo1 = TypeToID.get(s);
        s = "Proprietor Name1 1";
        addType(s);
        Env.TypeIDProprietorName1 = TypeToID.get(s);
        s = "Proprietorship Category 1";
        addType(s);
        Env.TypeIDProprietorshipCategory1 = TypeToID.get(s);
        s = "Country Incorporated 1";
        addType(s);
        Env.TypeIDCountryIncorporated1 = TypeToID.get(s);
        s = "Postcode District";
        addType(s);
        Env.TypeIDPostcodeDistrict = TypeToID.get(s);
    }

    void addToCounts(LR_Record r) {
        Iterator<LR_ID> iteTypes;
        iteTypes = Types.iterator();
        LR_ID typeID;
        while (iteTypes.hasNext()) {
            typeID = iteTypes.next();
            if (typeID.equals(Env.TypeIDTenure)) {
                Generic_Collections.addToMap(Counts.get(typeID), r.getTenureID(), 1);
            } else if (typeID.equals(Env.TypeIDCompanyRegistrationNo1)) {
                Generic_Collections.addToMap(Counts.get(typeID), r.getCompanyRegistrationNo1ID(), 1);
            } else if (typeID.equals(Env.TypeIDProprietorshipCategory1)) {
                Generic_Collections.addToMap(Counts.get(typeID), r.getProprietorshipCategory1ID(), 1);
            } else if (typeID.equals(Env.TypeIDProprietorName1)) {
                Generic_Collections.addToMap(Counts.get(typeID), r.getProprietorName1ID(), 1);
            } else if (typeID.equals(Env.TypeIDCountryIncorporated1)) {
                Generic_Collections.addToMap(Counts.get(typeID), r.getCountryIncorporated1ID(), 1);
            } else if (typeID.equals(Env.TypeIDPostcodeDistrict)) {
                Generic_Collections.addToMap(Counts.get(typeID), r.getPostcodeDistrictID(), 1);
            } else {
                int debug = 1; //not sure what should be happening here!
            }
        }
//        Generic_Collections.addToMap(proprietorName1IDCounts, r.getProprietorName1ID(), 1);
////        Generic_Collections.addToTreeMapStringInteger(proprietorName1IDCounts, r.getProprietorName1ID(), 1);
//        Generic_Collections.addToTreeMapStringInteger(countryIncorporated1Counts, r.getCountryIncorporated1(), 1);
    }

    void printGeneralisation(HashMap<LR_ID, PrintWriter> pws, int min) {
        Iterator<LR_ID> iteTypes;
        iteTypes = Types.iterator();
        LR_ID typeID;
        while (iteTypes.hasNext()) {
            typeID = iteTypes.next();
            printGeneralisation(pws, typeID, Counts.get(typeID), Env.getLookup(typeID), min);
        }
    }

    /**
     *
     * @param <K>
     * @param pw
     * @param type
     * @param counts
     * @param lookup
     * @param min
     */
    <K> void printGeneralisation(HashMap<LR_ID, PrintWriter> pws, LR_ID typeID,
            Map<K, Integer> counts, Map<K, String> lookup, int min) {
        PrintWriter pw;
        pw = pws.get(typeID);
        Map<K, Integer> sortedCounts;
        sortedCounts = Generic_Collections.sortByValue(counts);
        pw.println(IDToType.get(typeID));
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

    void printGeneralisation(HashMap<String, PrintWriter> pws, String type,
            TreeMap<String, Integer> counts, int min,
            HashMap<String, Integer> transparencyMap) {
        PrintWriter pw;
        pw = pws.get(type);
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
        TransparencyMap = new HashMap<>();
        File f;
        f = Files.getTIDataFile();
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
            TransparencyMap.put(Generic_StaticString.getUpperCase(cname),
                    Integer.valueOf(split[2]));
        }
    }
}
