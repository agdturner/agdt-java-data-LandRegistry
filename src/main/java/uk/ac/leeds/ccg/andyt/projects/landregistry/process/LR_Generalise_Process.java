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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_ReadCSV;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.generic.lang.Generic_StaticString;
import uk.ac.leeds.ccg.andyt.generic.math.statistics.Generic_Statistics;
import uk.ac.leeds.ccg.andyt.generic.utilities.Generic_Collections;
import uk.ac.leeds.ccg.andyt.generic.utilities.time.Generic_YearMonth;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Environment;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_ID;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_ID2;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.landregistry.LR_PricePaidData;
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

    /**
     * For storing counts of the number of classes of a particular value for
     * NonNull variables.
     */
    HashMap<LR_ID, TreeMap<LR_ID, Integer>> NonNullCounts;

    /**
     * For storing price paid data for a data.
     */
    LR_PricePaidData PricePaid;

    /**
     * For storing price paid data for a particular NonNull variable. Key is a
     * typeID, Value key is the value ID
     */
    HashMap<LR_ID, TreeMap<LR_ID, LR_PricePaidData>> NonNullPricePaid;

    /**
     * For storing the number of null values for variables.
     */
    HashMap<LR_ID, Integer> NullCounts;

    /**
     * For storing transparency values for each country. This data is loaded
     * from a file.
     */
    HashMap<String, Integer> TransparencyMap;

    /**
     * @param area
     * @param doAll
     * @param minsCC
     * @param minsOC
     * @param inputDataDir
     * @param doCCOD
     * @param doOCOD
     * @param doFull
     * @param overwrite
     */
    public void run(String area, boolean doAll,
            HashMap<LR_ID, Integer> minsCC, HashMap<LR_ID, Integer> minsOC,
            File inputDataDir, boolean doCCOD,
            boolean doOCOD, boolean doFull, boolean overwrite) {
        System.out.println("run(String,boolean,HashMap,HashMap,File,boolean,boolean,boolean,boolean)");
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

        boolean upDateIDs = true;
        // Initialise transparencyMap
        loadTransparencyMap();

        File indir;
        File outdir;
        File fin;
        File fout;
        ArrayList<String> lines;
        HashMap<LR_ID, PrintWriter> nonNullPWs;
        HashMap<LR_ID, PrintWriter> nonNullPricePaidPWs;
        PrintWriter pw;
        // Initialise Types, IDToType and TypeToID.

        nonNullPWs = new HashMap<>();
        nonNullPricePaidPWs = new HashMap<>();
        Iterator<LR_ID> iteTypes;

        String type;
        LR_ID typeID;

        HashMap<LR_ID2, LR_ID> nullPricePaid;
        nullPricePaid = Env.NullCollections.get(Env.PricePaidTypeID);
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
                indir = new File(inputDataDir, name0);
                if (doFull) {
                    indir = new File(indir, Strings.S_FULL);
                    indir = new File(indir, name);
                    if (doAll) {
                        outdir = new File(outputDataDir, Strings.S_Generalised);
                        outdir = new File(outdir, Strings.S_FULL);
                    } else {
                        indir = new File(outputDataDir, Strings.S_Subsets);
                        indir = new File(indir, area);
                        indir = new File(indir, name0);
                        indir = new File(indir, Strings.S_FULL);
                        indir = new File(indir, name);
                        outdir = new File(outputDataDir, Strings.S_Generalised);
                        outdir = new File(outdir, Strings.S_Subsets);
                        outdir = new File(outdir, area);
                        outdir = new File(outdir, Strings.S_FULL);
                    }
                } else {
                    indir = new File(indir, Strings.S_COU);
                    indir = new File(indir, name);
                    if (doAll) {
                        outdir = new File(outputDataDir, Strings.S_Generalised);
                        outdir = new File(outdir, Strings.S_COU);
                    } else {
                        indir = new File(outputDataDir, Strings.S_Subsets);
                        indir = new File(indir, area);
                        indir = new File(indir, name0);
                        indir = new File(indir, Strings.S_COU);
                        indir = new File(indir, name);
                        outdir = new File(outputDataDir, Strings.S_Generalised);
                        outdir = new File(outdir, Strings.S_Subsets);
                        outdir = new File(outdir, area);
                        outdir = new File(outdir, Strings.S_COU);
                    }
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
                        BufferedReader br;
                        StreamTokenizer st;
                        br = Generic_StaticIO.getBufferedReader(fin);
                        st = new StreamTokenizer(br);
                        Generic_StaticIO.setStreamTokenizerSyntax7(st);
                        boolean read;
                        read = false;
                        String line;
                        LR_Record r;
                        // read header
                        Generic_ReadCSV.readLine(st, null);
                        int ID;
                        ID = 1;
                        Generic_YearMonth YM = null;
                        // Initialise printwriters
                        try {
                            outdir.mkdirs();
                            /**
                             * Initialise collections and outputs.
                             */

                            // Init NonNullCounts
                            NonNullCounts = new HashMap<>();
                            type = Strings.S_CountryIncorporated1;
                            typeID = Env.CountryIncorporatedTypeID;
                            addNonNullCounts(type, typeID, outdir, nonNullPWs);
                            type = Strings.S_PostcodeDistrict;
                            typeID = Env.PostcodeDistrictTypeID;
                            addNonNullCounts(type, typeID, outdir, nonNullPWs);
                            type = Strings.S_PricePaid;
                            typeID = Env.PricePaidTypeID;
                            addNonNullCounts(type, typeID, outdir, nonNullPWs);
//                            type = Strings.S_ProprietorName;
//                            typeID = Env.ProprietorNameTypeID;
//                            addNonNullCounts(type, typeID, outdir, nonNullPWs);
                            type = Strings.S_ProprietorshipCategory1;
                            typeID = Env.ProprietorshipCategoryTypeID;
                            addNonNullCounts(type, typeID, outdir, nonNullPWs);
                            type = Strings.S_Tenure;
                            typeID = Env.TenureTypeID;
                            addNonNullCounts(type, typeID, outdir, nonNullPWs);

                            // Init NonNullPricePaid
                            // Init PricePaid
                            NonNullPricePaid = new HashMap<>();
//                            type = "";
//                            typeID = Env.PricePaidTypeID;
//                            addNonNullPricePaidType(type, typeID, outdir, nonNullPricePaidPWs);
                            // Init S_Tenure PricePaid
                            type = Strings.S_Tenure;
                            typeID = Env.TenureTypeID;
                            addNonNullPricePaidType(type, typeID, outdir, nonNullPricePaidPWs);
                            // Init S_CountryIncorporated1 PricePaid
                            type = Strings.S_CountryIncorporated1;
                            typeID = Env.CountryIncorporatedTypeID;
                            addNonNullPricePaidType(type, typeID, outdir, nonNullPricePaidPWs);
                            // Init S_Tenure PricePaid
                            type = Strings.S_ProprietorshipCategory1;
                            typeID = Env.ProprietorshipCategoryTypeID;
                            addNonNullPricePaidType(type, typeID, outdir, nonNullPricePaidPWs);

                            // Init NullCounts 
                            fout = new File(outdir, "GeneralCounts.csv");
                            pw = new PrintWriter(fout);
                            NullCounts = new HashMap<>();
                            iteTypes = Env.NullTypes.iterator();
                            while (iteTypes.hasNext()) {
                                typeID = iteTypes.next();
                                NullCounts.put(typeID, 0);
                            }

                            while (!read) {
                                line = Generic_ReadCSV.readLine(st, null);
                                if (line == null) {
                                    read = true;
                                } else {
                                    try {
                                        r = LR_Record.create(isCCOD, doFull, Env, YM, line, upDateIDs);
                                        if (r != null) {
                                            addToNonNullCounts(r, nonNullPWs.keySet());
                                            addToNonNullPricePaid(nullPricePaid, r, nonNullPricePaidPWs.keySet());
                                            addToNullCounts(r);
                                        }
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        //e.printStackTrace(System.err);
                                        System.out.println("Line " + ID + " is not a nomal line:" + line);
                                    } catch (Exception ex) {
                                        System.err.println("Line: " + line);
                                        Logger.getLogger(LR_Select_Process.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }

                            // Output NonNullCounts
                            if (isCCOD) {
                                printNonNullGeneralisation(nonNullPWs, minsCC);
                            } else {
                                printNonNullGeneralisation(nonNullPWs, minsOC);
                            }

                            // Outputs NonNullPricePaid
                            printNonNullPricePaidGeneralisation(nonNullPricePaidPWs);

                            // Output NullCounts
                            iteTypes = Env.NullTypes.iterator();
                            int n;
                            while (iteTypes.hasNext()) {
                                typeID = iteTypes.next();
                                type = Env.IDToType.get(typeID);
                                n = NullCounts.get(typeID);
                                pw.println("Count of null " + type + " " + n);
                            }

                            // Close printWriters
                            iteTypes = nonNullPWs.keySet().iterator();
                            while (iteTypes.hasNext()) {
                                typeID = iteTypes.next();
                                nonNullPWs.get(typeID).close();
                            }
                            iteTypes = nonNullPricePaidPWs.keySet().iterator();
                            while (iteTypes.hasNext()) {
                                typeID = iteTypes.next();
                                nonNullPricePaidPWs.get(typeID).close();
                            }
                            pw.close();
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(LR_Generalise_Process.class.getName()).log(Level.SEVERE, null, ex);
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

    /**
     *
     * @param type
     * @param typeID
     * @param outdir
     * @param pws
     */
    protected void addNonNullCounts(String type, LR_ID typeID,
            File outdir, HashMap<LR_ID, PrintWriter> pws) {
        File f;
        f = new File(outdir, type + ".csv");
        try {
            pws.put(typeID, new PrintWriter(f));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LR_Generalise_Process.class.getName()).log(Level.SEVERE, null, ex);
        }
        NonNullCounts.put(typeID, new TreeMap<>());
    }

    /**
     *
     * @param type
     * @param typeID
     * @param outdir
     * @param pws
     */
    protected void addNonNullPricePaidType(String type, LR_ID typeID,
            File outdir, HashMap<LR_ID, PrintWriter> pws) {
        File f;
        f = new File(outdir, type + Strings.S_PricePaid + ".csv");
        try {
            pws.put(typeID, new PrintWriter(f));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LR_Generalise_Process.class.getName()).log(Level.SEVERE, null, ex);
        }
        TreeMap<LR_ID, LR_PricePaidData> m;
        m = new TreeMap<>();
        Iterator<LR_ID> ite;
        LR_ID valueID;
        NonNullPricePaid.put(typeID, m);
        HashMap<LR_ID, String> vRLookup;
        vRLookup = Env.ValueReverseLookups.get(typeID);
        if (vRLookup != null) {
            ite = vRLookup.keySet().iterator();
        } else {
            ite = null;
        }
        if (ite == null) {
            System.out.println("No reverse lookup for " + type);
        } else {
            while (ite.hasNext()) {
                valueID = ite.next();

//                // Debug
//                String s = vRLookup.get(valueID);
//                System.out.println(s);
                m.put(valueID, new LR_PricePaidData(Env));
            }
        }
    }

    /**
     *
     * @param r
     * @param s
     */
    protected void addToNonNullCounts(LR_Record r, Set<LR_ID> s) {
        if (r != null) {
            Iterator<LR_ID> iteTypes;
            LR_ID typeID;
            LR_ID id;
            iteTypes = s.iterator();
            while (iteTypes.hasNext()) {
                typeID = iteTypes.next();
                if (typeID.equals(Env.TenureTypeID)) {
                    id = r.getTenureID();
                    Generic_Collections.addToMap(NonNullCounts.get(typeID), id, 1);
                } else if (typeID.equals(Env.CompanyRegistrationNoTypeID)) {
                    id = r.getCompanyRegistrationNo1ID();
                    Generic_Collections.addToMap(NonNullCounts.get(typeID), id, 1);
                } else if (typeID.equals(Env.ProprietorshipCategoryTypeID)) {
                    id = r.getProprietorshipCategory1ID();
                    Generic_Collections.addToMap(NonNullCounts.get(typeID), id, 1);
                } else if (typeID.equals(Env.ProprietorNameTypeID)) {
                    id = r.getProprietorName1ID();
                    Generic_Collections.addToMap(NonNullCounts.get(typeID), id, 1);
                } else if (typeID.equals(Env.CountryIncorporatedTypeID)) {
                    id = r.getCountryIncorporated1ID();
                    Generic_Collections.addToMap(NonNullCounts.get(typeID), id, 1);
                } else if (typeID.equals(Env.PostcodeDistrictTypeID)) {
                    id = r.getPostcodeDistrictID();
                    if (id != null) {
                        Generic_Collections.addToMap(NonNullCounts.get(typeID), id, 1);
                    }
                } else if (typeID.equals(Env.PricePaidTypeID)) {
                    id = r.getPricePaidClass();
                    if (id != null) {
                        Generic_Collections.addToMap(NonNullCounts.get(typeID), id, 1);
                    }
                } else {
                    String type;
                    type = Env.IDToType.get(typeID);
                    System.out.println("Type " + type + " is not a NonNull type that is counted!");
                }
            }
        }
    }

    protected void addToNonNullPricePaid(HashMap<LR_ID2, LR_ID> nullPricePaid,
            LR_Record r, Set<LR_ID> s) {
        if (r != null) {
            LR_ID id;
            Iterator<LR_ID> iteTypes;
            LR_ID typeID;
            iteTypes = s.iterator();
            while (iteTypes.hasNext()) {
                typeID = iteTypes.next();
                if (typeID == null) {
                    System.err.println("typeID is null");
                } else {
                    if (typeID.equals(Env.TenureTypeID)) {
                        id = r.getTenureID();
//                        TreeMap<LR_ID, LR_PricePaidData> m;
//                        m = NonNullPricePaid.get(typeID);
//                        LR_PricePaidData ppd;
//                        ppd = m.get(id);
//                        if (ppd != null) {
//                            ppd.add(r);
//                        }
                        NonNullPricePaid.get(typeID).get(id).add(r);
                    } else if (typeID.equals(Env.CountryIncorporatedTypeID)) {
                        if (!r.getCountryIncorporated1().trim().isEmpty()) {
                            id = r.getCountryIncorporated1ID();
                            //if (!Env.NullCollections.get(typeID).containsKey(r.getID())) {
                            LR_PricePaidData ppd;
                            ppd = NonNullPricePaid.get(typeID).get(id);
                            if (ppd != null) {
                                ppd.add(r);
                            }
                            //}
                        }
                    } else if (typeID.equals(Env.ProprietorshipCategoryTypeID)) {
                        if (!r.getProprietorshipCategory1().trim().isEmpty()) {
                            id = r.getProprietorshipCategory1ID();
                            LR_PricePaidData ppd;
                            ppd = NonNullPricePaid.get(typeID).get(id);
                            if (ppd != null) {
                                ppd.add(r);
                            }
                        }
                    } else {
                        System.err.println("typeID is not recognised");
                    }
                }
            }
        }
    }

    protected void addToNullCounts(LR_Record r) {
        Iterator<LR_ID> iteTypes;
        LR_ID typeID;
        iteTypes = NullCounts.keySet().iterator();
        HashMap<LR_ID2, LR_ID> m;
        while (iteTypes.hasNext()) {
            typeID = iteTypes.next();
            m = Env.NullCollections.get(typeID);
            if (m != null) {
                if (Env.NullCollections.get(typeID).containsKey(r.getID())) {
                    NullCounts.put(typeID, NullCounts.get(typeID) + 1);
                }
            } else {
                NullCounts.put(typeID, NullCounts.get(typeID) + 1);
            }
        }
    }

    /**
     *
     * @param pws
     */
    protected void printNonNullPricePaidGeneralisation(
            HashMap<LR_ID, PrintWriter> pws) {
        Iterator<LR_ID> ite;
        ite = pws.keySet().iterator();
        LR_ID typeID;
        while (ite.hasNext()) {
            typeID = ite.next();
            printGeneralisation(pws.get(typeID), NonNullPricePaid.get(typeID),
                    Env.ValueReverseLookups.get(typeID));
        }
    }

    /**
     *
     * @param pw
     * @param pricePaid
     * @param nameMap
     */
    protected void printGeneralisation(PrintWriter pw,
            TreeMap<LR_ID, LR_PricePaidData> pricePaid,
            HashMap<LR_ID, String> nameMap) {
        LR_ID id;
//        ArrayList<BigDecimal> pp;
        Iterator<LR_ID> ite;
        TreeMap<LR_ID, Integer> pricePaidCounts;
        int count;
        String value;
        LR_PricePaidData ppd;
        Iterator<LR_ID> ite3;
        String name;
        LR_ID variableValueID;
        ite3 = nameMap.keySet().iterator();
        while (ite3.hasNext()) {
            variableValueID = ite3.next();
            name = nameMap.get(variableValueID);
            pw.println(name);
            pw.println("Classes");
            pw.println("Value, Count");
            ppd = pricePaid.get(variableValueID);
            pricePaidCounts = ppd.getPricePaidCounts();
            ite = pricePaidCounts.keySet().iterator();
            while (ite.hasNext()) {
                id = ite.next();
                count = pricePaidCounts.get(id);
                value = Env.PricePaidLookup.get(id).toString();
                pw.println("\"" + value + "\"," + count);
            }
            BigDecimal[] summaryStatistics;
            summaryStatistics = Generic_Statistics.getSummaryStatistics_0(
                    ppd.getPricePaid(), 3, RoundingMode.HALF_EVEN);
            BigDecimal bd;
            pw.println();
            pw.println("SummaryStatistics");
            bd = summaryStatistics[0];
            if (bd != null) {
                pw.println("sum " + bd.toPlainString());
            }
            bd = summaryStatistics[1];
            if (bd != null) {
                pw.println("mean " + bd.toPlainString());
            }
            bd = summaryStatistics[2];
            if (bd != null) {
                pw.println("median " + bd.toPlainString());
            }
            bd = summaryStatistics[3];
            if (bd != null) {
                pw.println("q1 " + bd.toPlainString());
            }
            bd = summaryStatistics[4];
            if (bd != null) {
                pw.println("q3 " + bd.toPlainString());
            }
            bd = summaryStatistics[5];
            if (bd != null) {
                pw.println("mode " + bd.toPlainString());
            }
            bd = summaryStatistics[6];
            if (bd != null) {
                pw.println("min " + bd.toPlainString());
            }
            bd = summaryStatistics[7];
            if (bd != null) {
                pw.println("max " + bd.toPlainString());
            }
            bd = summaryStatistics[8];
            if (bd != null) {
                pw.println("numberOfDifferentValues " + bd.toPlainString());
            }
            bd = summaryStatistics[9];
            if (bd != null) {
                pw.println("numberOfDifferentValuesInMode " + bd.toPlainString());
            }
            bd = summaryStatistics[10];
            if (bd != null) {
                pw.println("numberOfSameValuesInAnyPartOfMode " + bd.toPlainString());
            }
        }
    }

    /**
     *
     * @param pws
     * @param mins
     */
    protected void printNonNullGeneralisation(HashMap<LR_ID, PrintWriter> pws, HashMap<LR_ID, Integer> mins) {
        Iterator<LR_ID> iteTypes;
        iteTypes = pws.keySet().iterator();
        LR_ID typeID;
        int min;
        while (iteTypes.hasNext()) {
            typeID = iteTypes.next();
            min = mins.get(typeID);
            printGeneralisation(pws, typeID, NonNullCounts.get(typeID),
                    Env.IDToLookups.get(typeID), min);
        }
    }

    /**
     *
     * @param <K>
     * @param pws
     * @param typeID
     * @param counts
     * @param lookup
     * @param min
     */
    protected <K> void printGeneralisation(HashMap<LR_ID, PrintWriter> pws, LR_ID typeID,
            Map<K, Integer> counts, Map<K, String> lookup, int min) {
        PrintWriter pw;
        pw = pws.get(typeID);
        pw.println(Env.IDToType.get(typeID));
        K k;
        Integer count;
        int smallCount = 0;
        boolean reportedSmallCount = false;
        Iterator<K> ite;
        pw.println("Value, Count");
        if (typeID.equals(Env.PricePaidTypeID)) {
            ite = counts.keySet().iterator();
            while (ite.hasNext()) {
                k = ite.next();
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
                        pw.println("\"" + Env.PricePaidLookup.get(k).toString() + "\"," + count);
                    }
                } else {
                    smallCount += count;
                }
            }
        } else {
            Map<K, Integer> sortedCounts;
            sortedCounts = Generic_Collections.sortByValue(counts);
            ite = sortedCounts.keySet().iterator();
            while (ite.hasNext()) {
                k = ite.next();
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
        }
        pw.println();
    }

    /**
     *
     * @param pws
     * @param type
     * @param counts
     * @param min
     * @param transparencyMap
     */
    protected void printGeneralisation(HashMap<String, PrintWriter> pws, String type,
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

    /**
     * Loads TransparencyMap
     */
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
