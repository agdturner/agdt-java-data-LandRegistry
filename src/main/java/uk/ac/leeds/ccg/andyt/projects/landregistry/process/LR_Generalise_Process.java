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
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.lang.Generic_String;
import uk.ac.leeds.ccg.andyt.stats.Generic_Statistics;
import uk.ac.leeds.ccg.andyt.generic.util.Generic_Collections;
import uk.ac.leeds.ccg.andyt.generic.time.Generic_YearMonth;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Environment;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_ID;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_ID2;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Strings;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_TypeID;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_ValueID;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.landregistry.LR_PricePaidData;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.landregistry.LR_Record;

/**
 * For reading and processing data from
 *
 * @author geoagdt
 */
public class LR_Generalise_Process extends LR_Main_Process {

   public LR_Generalise_Process(LR_Environment env) {
        super(env);
    }

    /**
     * For storing counts of the number of classes of a particular value for
     * NonNull variables.
     */
    HashMap<LR_TypeID, TreeMap<LR_ValueID, Integer>> NonNullCounts;

    /**
     * For storing price paid data for a data.
     */
    LR_PricePaidData PricePaid;

    /**
     * For storing price paid data for a particular NonNull variable. Key is a
     * typeID, Value key is the value ID
     */
    HashMap<LR_TypeID, TreeMap<LR_ValueID, LR_PricePaidData>> NonNullPricePaid;

    /**
     * For storing the number of null values for variables.
     */
    HashMap<LR_TypeID, Integer> NullCounts;

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
     * @throws java.io.IOException
     */
    public void run(String area, boolean doAll,
            HashMap<LR_ID, Integer> minsCC, HashMap<LR_ID, Integer> minsOC,
            File inputDataDir, boolean doCCOD,
            boolean doOCOD, boolean doFull, boolean overwrite) throws IOException {
        System.out.println("run(String,boolean,HashMap,HashMap,File,boolean,boolean,boolean,boolean)");
        ArrayList<String> names0;
        //ArrayList<String> names1;
        ArrayList<String> setNames;
        String name;
        String name0;
        String name00;
        names0 = new ArrayList<>();
        if (doCCOD) {
            names0.add(LR_Strings.s_CCOD);
        }
        if (doOCOD) {
            names0.add(LR_Strings.s_OCOD);
        }
        boolean isCCOD;

        // Initialise transparencyMap
        loadTransparencyMap();

        File indir;
        File outdir;
        File outputDataDir;
        outputDataDir = env.files.getOutputDir();
        File fin;
        File fout;
        ArrayList<String> lines;
        HashMap<LR_TypeID, PrintWriter> nonNullPWs;
        HashMap<LR_TypeID, PrintWriter> nonNullPricePaidPWs;
        PrintWriter pw;
        // Initialise Types, IDToType and TypeToID.

        Iterator<LR_TypeID> iteTypes;

        String type;
        LR_TypeID typeID;

//        HashMap<LR_ID2, LR_ValueID> nullPricePaid;
//        nullPricePaid = env.NullTitleNumberIDCollections.get(env.PricePaidTypeID);
        Iterator<String> ite0;
        Iterator<String> ite2;
        ite0 = names0.iterator();
        while (ite0.hasNext()) {
            name0 = ite0.next();
            isCCOD = name0.equalsIgnoreCase(LR_Strings.s_CCOD);
            name00 = getName00(doFull, name0);
            setNames = getSetNames(doFull, name0);
            ite2 = setNames.iterator();
            while (ite2.hasNext()) {
                name = name00;
                name += ite2.next();
                indir = new File(inputDataDir, name0);
                if (doFull) {
                    indir = new File(indir, LR_Strings.s_FULL);
                    indir = new File(indir, name);
                    if (doAll) {
                        outdir = new File(outputDataDir, LR_Strings.s_Generalised);
                        outdir = new File(outdir, LR_Strings.s_FULL);
                    } else {
                        indir = new File(outputDataDir, LR_Strings.s_Subsets);
                        indir = new File(indir, area);
                        indir = new File(indir, name0);
                        indir = new File(indir, LR_Strings.s_FULL);
                        indir = new File(indir, name);
                        outdir = new File(outputDataDir, LR_Strings.s_Generalised);
                        outdir = new File(outdir, LR_Strings.s_Subsets);
                        outdir = new File(outdir, area);
                        outdir = new File(outdir, LR_Strings.s_FULL);
                    }
                } else {
                    indir = new File(indir, LR_Strings.s_COU);
                    indir = new File(indir, name);
                    if (doAll) {
                        outdir = new File(outputDataDir, LR_Strings.s_Generalised);
                        outdir = new File(outdir, LR_Strings.s_COU);
                    } else {
                        indir = new File(outputDataDir, LR_Strings.s_Subsets);
                        indir = new File(indir, area);
                        indir = new File(indir, name0);
                        indir = new File(indir, LR_Strings.s_COU);
                        indir = new File(indir, name);
                        outdir = new File(outputDataDir, LR_Strings.s_Generalised);
                        outdir = new File(outdir, LR_Strings.s_Subsets);
                        outdir = new File(outdir, area);
                        outdir = new File(outdir, LR_Strings.s_COU);
                    }
                }
                System.out.println("indir " + indir);
                outdir = new File(outdir, name0);
                outdir = new File(outdir, name);
                outdir = new File(outdir, LR_Strings.s_Generalised);
                System.out.println("outdir " + outdir);
                fin = new File(indir, name + ".csv");
                if (!fin.exists()) {
                    System.out.println("File " + fin + " does not exist.");
                } else {
                    if (overwrite || !outdir.exists()) {
                        BufferedReader br;
                        StreamTokenizer st;
                        br = env.env.io.getBufferedReader(fin);
                        st = new StreamTokenizer(br);
                        env.env.io.setStreamTokenizerSyntax7(st);
                        boolean read;
                        read = false;
                        String line;
                        LR_Record r;
                        // read header
                        reader.readLine(st, null);
                        int lineNumber;
                        lineNumber = 0;
                        int N;
                        N = 0;
                        Generic_YearMonth YM = null;
                        // Initialise printwriters
                        nonNullPWs = new HashMap<>();
                        nonNullPricePaidPWs = new HashMap<>();
                        try {
                            outdir.mkdirs();
                            /**
                             * Initialise collections and outputs.
                             */
                            // Init NonNullCounts
                            NonNullCounts = new HashMap<>();
                            type = LR_Strings.s_CountryIncorporated1;
                            typeID = env.CountryIncorporatedTypeID;
                            addNonNullCounts(type, typeID, outdir, nonNullPWs);
                            type = LR_Strings.s_PostcodeDistrict;
                            typeID = env.PostcodeDistrictTypeID;
                            addNonNullCounts(type, typeID, outdir, nonNullPWs);
                            type = LR_Strings.s_PricePaid;
                            typeID = env.PricePaidTypeID;
                            addNonNullCounts(type, typeID, outdir, nonNullPWs);
//                            type = strings.S_ProprietorName;
//                            typeID = env.ProprietorNameTypeID;
//                            addNonNullCounts(type, typeID, outdir, nonNullPWs);
                            type = LR_Strings.s_ProprietorshipCategory1;
                            typeID = env.ProprietorshipCategoryTypeID;
                            addNonNullCounts(type, typeID, outdir, nonNullPWs);
                            type = LR_Strings.s_Tenure;
                            typeID = env.TenureTypeID;
                            addNonNullCounts(type, typeID, outdir, nonNullPWs);

                            // Init NonNullPricePaid
                            // Init PricePaid
                            NonNullPricePaid = new HashMap<>();
//                            type = "";
//                            typeID = env.PricePaidTypeID;
//                            addNonNullPricePaidType(type, typeID, outdir, nonNullPricePaidPWs);
                            // Init S_Tenure PricePaid
                            type = LR_Strings.s_Tenure;
                            typeID = env.TenureTypeID;
                            addNonNullPricePaidType(type, typeID, outdir, nonNullPricePaidPWs, nonNullPWs);
                            // Init S_CountryIncorporated1 PricePaid
                            type = LR_Strings.s_CountryIncorporated1;
                            typeID = env.CountryIncorporatedTypeID;
                            addNonNullPricePaidType(type, typeID, outdir, nonNullPricePaidPWs, nonNullPWs);
                            // Init S_ProprietorshipCategory1 PricePaid
                            type = LR_Strings.s_ProprietorshipCategory1;
                            typeID = env.ProprietorshipCategoryTypeID;
                            addNonNullPricePaidType(type, typeID, outdir, nonNullPricePaidPWs, nonNullPWs);
                            // Init Region PricePaid
                            type = LR_Strings.s_Region;
                            typeID = env.RegionTypeID;
                            addNonNullPricePaidType(type, typeID, outdir, nonNullPricePaidPWs, nonNullPWs);
                            // Init County PricePaid
                            type = LR_Strings.s_County;
                            typeID = env.CountyTypeID;
                            addNonNullPricePaidType(type, typeID, outdir, nonNullPricePaidPWs, nonNullPWs);
                            // Init District PricePaid
                            type = LR_Strings.s_District;
                            typeID = env.DistrictTypeID;
                            addNonNullPricePaidType(type, typeID, outdir, nonNullPricePaidPWs, nonNullPWs);
                            // Init ProprietorName1 PricePaid
                            type = LR_Strings.s_ProprietorName1;
                            typeID = env.ProprietorNameTypeID;
                            addNonNullPricePaidType(type, typeID, outdir, nonNullPricePaidPWs, nonNullPWs);
                            // Init CompanyRegistrationNo1 PricePaid
                            type = LR_Strings.s_CompanyRegistrationNo1;
                            typeID = env.CompanyRegistrationNoTypeID;
                            addNonNullPricePaidType(type, typeID, outdir, nonNullPricePaidPWs, nonNullPWs);

                            // Init NullCounts 
                            fout = new File(outdir, "GeneralCounts.csv");
                            pw = new PrintWriter(fout);
                            NullCounts = new HashMap<>();
                            iteTypes = env.NullTypes.iterator();
                            while (iteTypes.hasNext()) {
                                typeID = iteTypes.next();
                                NullCounts.put(typeID, 0);
                            }

                            while (!read) {
                                lineNumber ++;
                                line = reader.readLine(st, null);
                                if (line == null) {
                                    read = true;
                                } else {
                                    try {
                                        r = LR_Record.create(isCCOD, doFull, env, YM, line, false);
                                        if (r != null) {
                                            addToNonNullCounts(r, nonNullPWs.keySet());
                                            //addToNonNullPricePaid(nullPricePaid, r, nonNullPricePaidPWs.keySet());
                                            addToNonNullPricePaid(r, nonNullPricePaidPWs.keySet());
                                            addToNullCounts(r);
                                            N ++;
                                        }
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        e.printStackTrace(System.err);
                                        System.out.println("Line: " + lineNumber + "\"" + line + "\"");
                                        Logger.getLogger(LR_Select_Process.class.getName()).log(Level.SEVERE, null, e);
                                    } catch (Exception ex) {
                                        ex.printStackTrace(System.err);
                                        System.err.println("Line: " + lineNumber + "\"" + line + "\"");
                                        Logger.getLogger(LR_Select_Process.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }

                            // Output NonNullCounts and NonNullPricePaid
                            if (isCCOD) {
                                printNonNullGeneralisation(nonNullPWs, minsCC);
                                printNonNullPricePaidGeneralisation(nonNullPricePaidPWs, minsCC);
                            } else {
                                printNonNullGeneralisation(nonNullPWs, minsOC);
                                printNonNullPricePaidGeneralisation(nonNullPricePaidPWs, minsOC);
                            }

                            // Output GeneralCounts
                            pw.println("GeneralCounts");
                            pw.println("Number of records " + N);
                            pw.println();
                            // Output NullCounts
                            pw.println("Counts of Null values");
                            iteTypes = env.NullTypes.iterator();
                            int n;
                            while (iteTypes.hasNext()) {
                                typeID = iteTypes.next();
                                type = typeID.getType();
                                n = NullCounts.get(typeID);
                                pw.println("Count of null " + type + " " + n);
                            }
                            pw.println();
                            // Output NullCounts
                            pw.println("Percentages of Null values");
                            iteTypes = env.NullTypes.iterator();
                            double n2;
                            double percentage;
                            while (iteTypes.hasNext()) {
                                typeID = iteTypes.next();
                                type = typeID.getType();
                                n2 = NullCounts.get(typeID);
                                percentage = (n2 * 100.0d) / (double) N;
                                pw.println("Percentage of null " + type + " " 
                                        + String.format( "%.2f", percentage));
                            } 
                            pw.println();
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
        System.out.println("Write out summaries");
        
    }

    /**
     *
     * @param type
     * @param typeID
     * @param outdir
     * @param pws
     */
    protected void addNonNullCounts(String type, LR_TypeID typeID,
            File outdir, HashMap<LR_TypeID, PrintWriter> pws) {
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
     * @param nonNullPWs
     */
    protected void addNonNullPricePaidType(String type, LR_TypeID typeID,
            File outdir, HashMap<LR_TypeID, PrintWriter> pws, 
            HashMap<LR_TypeID, PrintWriter> nonNullPWs) {
        if (!nonNullPWs.containsKey(typeID)) {
            addNonNullCounts(type, typeID, outdir, nonNullPWs);
        }
        File f;
        f = new File(outdir, type + LR_Strings.s_PricePaid + ".csv");
        try {
            pws.put(typeID, new PrintWriter(f));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LR_Generalise_Process.class.getName()).log(Level.SEVERE, null, ex);
        }
        TreeMap<LR_ValueID, LR_PricePaidData> m;
        m = new TreeMap<>();
        Iterator<LR_ValueID> ite;
        ite = env.ValueIDs.get(typeID).iterator();

//        if (!ite.hasNext()) {
//            System.out.println("env.ValueIDs.get(typeID).iterator() is empty! type " + type + " typeID" + typeID);
//        }
        LR_ValueID valueID;
        while (ite.hasNext()) {
            valueID = ite.next();
            m.put(valueID, new LR_PricePaidData(env));
        }
        NonNullPricePaid.put(typeID, m);
    }

    /**
     *
     * @param r
     * @param s
     */
    protected void addToNonNullCounts(LR_Record r, Set<LR_TypeID> s) {
        if (r != null) {
            Iterator<LR_TypeID> iteTypes;
            LR_TypeID typeID;
            LR_ValueID id;
            iteTypes = s.iterator();
            while (iteTypes.hasNext()) {
                typeID = iteTypes.next();
                if (typeID.equals(env.TenureTypeID)) {
                    id = r.getTenureID();
                    Generic_Collections.addToMap(NonNullCounts.get(typeID), id, 1);
                } else if (typeID.equals(env.CompanyRegistrationNoTypeID)) {
                    id = r.getCompanyRegistrationNo1ID();
                    if (id != null) {
                            Generic_Collections.addToMap(NonNullCounts.get(typeID), id, 1);
                    }
                } else if (typeID.equals(env.ProprietorshipCategoryTypeID)) {
                    id = r.getProprietorshipCategory1ID();
                    if (id != null) {
                        Generic_Collections.addToMap(NonNullCounts.get(typeID), id, 1);
                    }
                } else if (typeID.equals(env.ProprietorNameTypeID)) {
                    id = r.getProprietorName1ID();
                    if (id != null) {
                        Generic_Collections.addToMap(NonNullCounts.get(typeID), id, 1);
                    }
                } else if (typeID.equals(env.CountryIncorporatedTypeID)) {
                    id = r.getCountryIncorporated1ID();
                    if (id != null) {
                        Generic_Collections.addToMap(NonNullCounts.get(typeID), id, 1);
                    }
                } else if (typeID.equals(env.PostcodeDistrictTypeID)) {
                    id = r.getPostcodeDistrictID();
                    if (id != null) {
                        Generic_Collections.addToMap(NonNullCounts.get(typeID), id, 1);
                    }
                } else if (typeID.equals(env.PricePaidTypeID)) {
                    id = r.getPricePaidClass();
                    if (id != null) {
                        Generic_Collections.addToMap(NonNullCounts.get(typeID), id, 1);
                    }
                } else if (typeID.equals(env.RegionTypeID)) {
                    id = r.getRegionID();
                    if (id != null) {
                        Generic_Collections.addToMap(NonNullCounts.get(typeID), id, 1);
                    }
                } else if (typeID.equals(env.CountyTypeID)) {
                    id = r.getCountyID();
                    if (id != null) {
                        Generic_Collections.addToMap(NonNullCounts.get(typeID), id, 1);
                    }
                } else if (typeID.equals(env.DistrictTypeID)) {
                    id = r.getDistrictID();
                    if (id != null) {
                            Generic_Collections.addToMap(NonNullCounts.get(typeID), id, 1);
                    }
                } else {
                    System.out.println("Type " + typeID.getType()
                            + " is not a NonNull type that is counted!");
                }
            }
        }
    }

    protected void addToNonNullPricePaid(LR_Record r, Set<LR_TypeID> s) {
        if (r != null) {
            LR_ValueID valueID;
            Iterator<LR_TypeID> iteTypes;
            LR_TypeID typeID;
            iteTypes = s.iterator();
            while (iteTypes.hasNext()) {
                typeID = iteTypes.next();
                if (typeID == null) {
                    System.err.println("typeID is null");
                } else {
                    if (typeID.equals(env.TenureTypeID)) {
                        valueID = r.getTenureID();
                        TreeMap<LR_ValueID, LR_PricePaidData> m;
                        m = NonNullPricePaid.get(typeID);
//                        // This is supposed to be already done!
//                        if (m == null) {
//                            m = new TreeMap<>();
//                            NonNullPricePaid.put(typeID, m);
//                            int debug = 1;
//                        }
                        LR_PricePaidData ppd;
                        ppd = m.get(valueID);
//                        // This is supposed to be already done!
//                        if (ppd == null) {
//                            ppd = new LR_PricePaidData(env);
//                            m.put(valueID, ppd);
//                            int debug = 1;
//                        }
                        ppd.add(r);
                        //System.out.println("Tenure " + r.getTenure());
                        //NonNullPricePaid.get(typeID).get(valueID).add(r);
                    } else if (typeID.equals(env.CountryIncorporatedTypeID)) {
                        if (!r.getCountryIncorporated1().trim().isEmpty()) {
                            valueID = r.getCountryIncorporated1ID();
                            TreeMap<LR_ValueID, LR_PricePaidData> m;
                            m = NonNullPricePaid.get(typeID);
//                            // This is supposed to be already done!
//                            if (m == null) {
//                                m = new TreeMap<>();
//                                NonNullPricePaid.put(typeID, m);
//                                int debug = 1;
//                            }
                            LR_PricePaidData ppd;
                            ppd = m.get(valueID);
                            // For adding United_Kingdom. (@TODO find a better way to do this.)
                            // This is supposed to be already done! 
                            if (ppd == null) {
                                ppd = new LR_PricePaidData(env);
                                m.put(valueID, ppd);
                                int debug = 1;
                            }
                            ppd.add(r);
                            //System.out.println("CountryIncorporated1 " + r.getCountryIncorporated1());
                            //if (!env.NullCollections.get(typeID).containsKey(r.getID())) {
                        }
                    } else if (typeID.equals(env.ProprietorshipCategoryTypeID)) {
                        if (!r.getProprietorshipCategory1().trim().isEmpty()) {
                            valueID = r.getProprietorshipCategory1ID();
                            TreeMap<LR_ValueID, LR_PricePaidData> m;
                            m = NonNullPricePaid.get(typeID);
//                            // This is supposed to be already done!
//                            if (m == null) {
//                                m = new TreeMap<>();
//                                NonNullPricePaid.put(typeID, m);
//                                int debug = 1;
//                            }
                            LR_PricePaidData ppd;
                            ppd = m.get(valueID);
//                            // This is supposed to be already done!
//                            if (ppd == null) {
//                                ppd = new LR_PricePaidData(env);
//                                m.put(valueID, ppd);
//                                int debug = 1;
//                            }
                            ppd.add(r);
                            //System.out.println("ProprietorshipCategory1 " + r.getProprietorshipCategory1());
                            //if (!env.NullCollections.get(typeID).containsKey(r.getID())) {                            
                        }
                    } else if (typeID.equals(env.ProprietorNameTypeID)) {
                        if (!r.getProprietorName1().trim().isEmpty()) {
                            valueID = r.getProprietorName1ID();
                            TreeMap<LR_ValueID, LR_PricePaidData> m;
                            m = NonNullPricePaid.get(typeID);
//                            // This is supposed to be already done!
//                            if (m == null) {
//                                m = new TreeMap<>();
//                                NonNullPricePaid.put(typeID, m);
//                                int debug = 1;
//                            }
                            LR_PricePaidData ppd;
                            ppd = m.get(valueID);
//                            // This is supposed to be already done!
//                            if (ppd == null) {
//                                ppd = new LR_PricePaidData(env);
//                                m.put(valueID, ppd);
//                                int debug = 1;
//                            }
                            ppd.add(r);
                            //System.out.println("ProprietorName1 " + r.getProprietorName1());
                            //if (!env.NullCollections.get(typeID).containsKey(r.getID())) {                            
                        }
                    } else if (typeID.equals(env.CompanyRegistrationNoTypeID)) {
                        if (!r.getCompanyRegistrationNo1().trim().isEmpty()) {
                            valueID = r.getCompanyRegistrationNo1ID();
                            if (valueID != null) {
                                TreeMap<LR_ValueID, LR_PricePaidData> m;
                                m = NonNullPricePaid.get(typeID);
                                // This is supposed to be already done!
                                if (m == null) {
                                    m = new TreeMap<>();
                                    NonNullPricePaid.put(typeID, m);
                                    int debug = 1;
                                }
                                LR_PricePaidData ppd;
                                ppd = m.get(valueID);
                            // This is supposed to be already done!
                            if (ppd == null) {
                                ppd = new LR_PricePaidData(env);
                                m.put(valueID, ppd);
                                int debug = 1;
                            }
                                ppd.add(r);
                            }
                            //System.out.println("CompanyRegistrationNo1 " + r.getCompanyRegistrationNo1());
                            //if (!env.NullCollections.get(typeID).containsKey(r.getID())) {                            
                        }
                    } else if (typeID.equals(env.RegionTypeID)) {
                        if (!r.getRegion().trim().isEmpty()) {
                            valueID = r.getRegionID();
                            if (valueID != null) {
                                TreeMap<LR_ValueID, LR_PricePaidData> m;
                                m = NonNullPricePaid.get(typeID);
                                // This is supposed to be already done!
                                if (m == null) {
                                    m = new TreeMap<>();
                                    NonNullPricePaid.put(typeID, m);
                                    int debug = 1;
                                }
                                LR_PricePaidData ppd;
                                ppd = m.get(valueID);
//                            // This is supposed to be already done!
//                            if (ppd == null) {
//                                ppd = new LR_PricePaidData(env);
//                                m.put(valueID, ppd);
//                                int debug = 1;
//                            }
                                ppd.add(r);
                            }
                            //System.out.println("Region " + r.getRegion());
                            //if (!env.NullCollections.get(typeID).containsKey(r.getID())) {                            
                        }
                    } else if (typeID.equals(env.CountyTypeID)) {
                        if (!r.getCounty().trim().isEmpty()) {
                            valueID = r.getCountyID();
                            if (valueID != null) {
                                TreeMap<LR_ValueID, LR_PricePaidData> m;
                                m = NonNullPricePaid.get(typeID);
                                // This is supposed to be already done!
                                if (m == null) {
                                    m = new TreeMap<>();
                                    NonNullPricePaid.put(typeID, m);
                                    int debug = 1;
                                }
                                LR_PricePaidData ppd;
                                ppd = m.get(valueID);
//                            // This is supposed to be already done!
//                            if (ppd == null) {
//                                ppd = new LR_PricePaidData(env);
//                                m.put(valueID, ppd);
//                                int debug = 1;
//                            }
                                ppd.add(r);
                            }
                            //System.out.println("County " + r.getCounty());
                            //if (!env.NullCollections.get(typeID).containsKey(r.getID())) {                            
                        }
                    } else if (typeID.equals(env.DistrictTypeID)) {
                        if (!r.getDistrict().trim().isEmpty()) {
                            valueID = r.getDistrictID();
                            if (valueID != null) {
                                TreeMap<LR_ValueID, LR_PricePaidData> m;
                                m = NonNullPricePaid.get(typeID);
                                // This is supposed to be already done!
                                if (m == null) {
                                    m = new TreeMap<>();
                                    NonNullPricePaid.put(typeID, m);
                                    int debug = 1;
                                }
                                LR_PricePaidData ppd;
                                ppd = m.get(valueID);
//                            // This is supposed to be already done!
//                            if (ppd == null) {
//                                ppd = new LR_PricePaidData(env);
//                                m.put(valueID, ppd);
//                                int debug = 1;
//                            }
                                ppd.add(r);
                            }
                            //System.out.println("District " + r.getDistrict());
                            //if (!env.NullCollections.get(typeID).containsKey(r.getID())) {                            
                        }
                    } else {
                        System.err.println("typeID is not recognised");
                    }
                }
            }
        }
    }

    protected void addToNullCounts(LR_Record r) {
        Iterator<LR_TypeID> iteTypes;
        LR_TypeID typeID;
        iteTypes = NullCounts.keySet().iterator();
        HashMap<LR_ID2, LR_ValueID> m;
        while (iteTypes.hasNext()) {
            typeID = iteTypes.next();
            m = env.NullTitleNumberIDCollections.get(typeID);
            if (m != null) {
                if (env.NullTitleNumberIDCollections.get(typeID).containsKey(r.getID())) {
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
     * @param mins
     */
    protected void printNonNullPricePaidGeneralisation(
            HashMap<LR_TypeID, PrintWriter> pws, HashMap<LR_ID, Integer> mins) {
        Iterator<LR_TypeID> ite;
        ite = pws.keySet().iterator();
        LR_TypeID typeID;
        while (ite.hasNext()) {
            typeID = ite.next();
            if (!NonNullCounts.containsKey(typeID)) {
                System.out.println("!NonNullCounts.containsKey(typeID)");
            } else {
                printGeneralisation(pws.get(typeID), NonNullPricePaid.get(typeID),
                        env.ValueIDs.get(typeID), NonNullCounts.get(typeID),
                        mins.get(typeID));
            }
        }
    }

    /**
     * Print price paid generalisation
     * @param pw
     * @param pricePaid
     * @param valueIDs
     * @param counts
     * @param min
     */
    protected void printGeneralisation(PrintWriter pw,
            TreeMap<LR_ValueID, LR_PricePaidData> pricePaid,
            HashSet<LR_ValueID> valueIDs, Map<LR_ValueID, Integer> counts, 
            int min) {
        LR_ValueID ppValueID;
        Iterator<LR_ValueID> ite;
        Iterator<LR_ValueID> ite2;
        TreeMap<LR_ValueID, Integer> ppCounts;
        Integer count;
        String value;
        LR_PricePaidData ppd;
//        Iterator<LR_ValueID> ite3;
        String name;
        LR_ValueID valueID;

        int smallCount = 0;
        boolean reportedSmallCount = false;
        int allCount;
        allCount = 0;

        Map<LR_ValueID, Integer> sortedCounts;
        sortedCounts = Generic_Collections.sortByValue(counts);
        ite = sortedCounts.keySet().iterator();
        while (ite.hasNext()) {
            valueID = ite.next();
            count = counts.get(valueID);
            if (count == null) {
                count = 0;
            }
            allCount += count;
            if (count >= min) {
                if (!reportedSmallCount) {
                    pw.println("Those with less than " + min + "," + smallCount);
                    pw.println();
                    reportedSmallCount = true;
                }
                pw.println("\"" + valueID.getValue() + "\"," + count);
                name = valueID.getValue();
                ppd = pricePaid.get(valueID);
                ppCounts = ppd.getPricePaidCounts();
                ite2 = ppCounts.keySet().iterator();
                if (ite2.hasNext()) {
                    pw.println(name);
                    pw.println("PricePaid Classes");
                    pw.println("Value, Count");
                    while (ite2.hasNext()) {
                        ppValueID = ite2.next();
                        count = ppCounts.get(ppValueID);
                        value = env.PricePaidLookup.get(ppValueID).toString();
                        pw.println("\"" + value + "\"," + count);
                    }
                    printSummaryStatistics(ppd.getPricePaid(), pw);
                } else {
                    pw.println("No price paid data is available for these listings!");
                    pw.println();
                }
            } else {
                smallCount += count;
            }
        }

//        ite3 = valueIDs.iterator();
//        while (ite3.hasNext()) {
//            valueID = ite3.next();
//            count = counts.get(valueID);
//            if (count > min) {
//                name = valueID.getValue();
//                ppd = pricePaid.get(valueID);
//                ppCounts = ppd.getPricePaidCounts();
//                ite = ppCounts.keySet().iterator();
//                if (ite.hasNext()) {
//                    pw.println(name);
//                    pw.println("PricePaid Classes");
//                    pw.println("Value, Count");
//                    while (ite.hasNext()) {
//                        ppValueID = ite.next();
//                        count = ppCounts.get(ppValueID);
//                        value = env.PricePaidLookup.get(ppValueID).toString();
//                        pw.println("\"" + value + "\"," + count);
//                    }
//                    printSummaryStatistics(ppd.getPricePaid(), pw);
//                }
//            }
//        }
        pw.println("All Count " + allCount);
        pw.println();
    }

    public void printSummaryStatistics(ArrayList<BigDecimal> data,
            PrintWriter pw) {
        BigDecimal[] summaryStatistics;
        summaryStatistics = Generic_Statistics.getSummaryStatistics_0(
                data, 3, RoundingMode.HALF_EVEN);
        pw.println("SummaryStatistics");
        BigDecimal bd;
        bd = summaryStatistics[0];
        if (bd != null) {
            pw.println("sum " + bd.toPlainString());
        }
        pw.println("n " + data.size());
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
        pw.println();
        pw.println();
    }

    /**
     *
     * @param pws
     * @param mins
     */
    protected void printNonNullGeneralisation(
            HashMap<LR_TypeID, PrintWriter> pws, HashMap<LR_ID, Integer> mins) {
        Iterator<LR_TypeID> iteTypes;
        iteTypes = pws.keySet().iterator();
        LR_TypeID typeID;
        int min;
        while (iteTypes.hasNext()) {
            typeID = iteTypes.next();
            min = mins.get(typeID);
            printGeneralisation(pws, typeID, NonNullCounts.get(typeID),
                    env.ValueIDs.get(typeID), min);
        }
    }

    /**
     *
     * @param pws
     * @param typeID
     * @param counts
     * @param valueIDs
     * @param min
     */
    protected void printGeneralisation(
            HashMap<LR_TypeID, PrintWriter> pws, LR_TypeID typeID,
            Map<LR_ValueID, Integer> counts, HashSet<LR_ValueID> valueIDs, int min) {
        PrintWriter pw;
        pw = pws.get(typeID);
        pw.println(typeID.getType());
        LR_ValueID valueID;
        Integer count;
        int smallCount = 0;
        boolean reportedSmallCount = false;
        int allCount;
        allCount = 0;
        Iterator<LR_ValueID> ite;
        pw.println("Value, Count");
        if (typeID.equals(env.PricePaidTypeID)) {
            ite = counts.keySet().iterator();
            while (ite.hasNext()) {
                valueID = ite.next();
                count = counts.get(valueID);
                if (count == null) {
                    count = 0;
                }
                allCount += count;
                if (valueIDs == null) {
                    pw.println("\"" + valueID + "\"," + count);
                } else {
                    pw.println("\"" + env.PricePaidLookup.get(valueID).toString() + "\"," + count);
                }
            }
        } else {
            Map<LR_ValueID, Integer> sortedCounts;
            sortedCounts = Generic_Collections.sortByValue(counts);
            ite = sortedCounts.keySet().iterator();
            while (ite.hasNext()) {
                valueID = ite.next();
                count = counts.get(valueID);
                if (count == null) {
                    count = 0;
                }
                allCount += count;
                if (count >= min) {
                    if (!reportedSmallCount) {
                        pw.println("Those with less than " + min + "," + smallCount);
                        reportedSmallCount = true;
                    }
                    if (valueIDs == null) {
                        pw.println("\"" + valueID + "\"," + count);
                    } else {
                        pw.println("\"" + valueID.getValue() + "\"," + count);
                    }
                } else {
                    smallCount += count;
                }
            }
        }
        pw.println("All Count " + allCount);
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
    protected void loadTransparencyMap() throws IOException {
        TransparencyMap = new HashMap<>();
        File f;
        f = files.getTIDataFile();
        ArrayList<String> lines;
        lines = reader.read(f, null, 7);
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
            TransparencyMap.put(Generic_String.getUpperCase(cname),
                    Integer.valueOf(split[2]));
        }
    }
}
