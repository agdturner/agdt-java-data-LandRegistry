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

//import com.sun.org.apache.xerces.internal.impl.dv.xs.YearMonthDV;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.data.format.Generic_ReadCSV;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_IO;
import uk.ac.leeds.ccg.andyt.generic.time.Generic_YearMonth;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Environment;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.landregistry.LR_OC_FULL_Record;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.landregistry.pricepaid.LR_PricePaid_Record;

/**
 * For reading, selecting and writing out selected records.
 */
public class LR_JoinPricePaidDataAndOwnershipData_Process extends LR_Main_Process {

    boolean overwrite;

    protected LR_JoinPricePaidDataAndOwnershipData_Process() {
    }

    public LR_JoinPricePaidDataAndOwnershipData_Process(LR_Environment env, boolean overwrite) {
        super(env);
        this.overwrite = overwrite;
    }

    /**
     * @param area
     */
    public void run(String area) {

        join(area);

    }

    /**
     * Select records where PPDCategoryType equals "B" and District = "LEEDS"
     *
     * @param area
     */
    public void join(String area) {

        File fin;

        // Load OCOD into a collection
        ArrayList<LR_OC_FULL_Record> OCODList;
        OCODList = new ArrayList<>();
        File indir;
        indir = new File(Files.getDataDir(), "/output/Subsets/LEEDS/OCOD/FULL/OCOD_FULL_2017_11/");
        fin = new File(indir, "OCOD_FULL_2017_11.csv");

        LR_OC_FULL_Record or;

        Generic_YearMonth ym;
        ym = new Generic_YearMonth(Env.ge, YearMonth.of(2011, Month.NOVEMBER));

        if (!fin.exists()) {
            System.out.println("Input file " + fin + " does not exist.");
        } else {

            BufferedReader br;
            StreamTokenizer st;
            br = Generic_IO.getBufferedReader(fin);
            st = new StreamTokenizer(br);
            Generic_IO.setStreamTokenizerSyntax7(st);
            boolean read;
            read = false;
            String line;
            // read header
            Generic_ReadCSV.readLine(st, null);
            int lineNumber;
            lineNumber = 0;
            while (!read) {
                line = Generic_ReadCSV.readLine(st, null);
                if (line == null) {
                    read = true;
                } else {
                    try {
                        or = new LR_OC_FULL_Record(Env, ym, line, false);
                        OCODList.add(or);
                        //}
                    } catch (ArrayIndexOutOfBoundsException e) {
                        //e.printStackTrace(System.err);
                        System.out.println("Line " + lineNumber + " is not a nomal line:" + line);
                    } catch (Exception ex) {
                        System.err.println("Line: " + line);
                        Logger.getLogger(LR_JoinPricePaidDataAndOwnershipData_Process.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        System.out.println("OCODList.size() " + OCODList.size());

        Iterator<LR_OC_FULL_Record> ite;
        // Process data
        File outdir;
        File fout;
        PrintWriter pw = null;

        outdir = new File(Env.Files.getOutputDataDir(Strings), "PricePaid");
        System.out.println("outdir " + outdir);
        fin = new File(outdir, "pp-complete-PPDCategoryType-B-District-LEEDS.csv");
        if (!fin.exists()) {
            System.out.println("Input file " + fin + " does not exist.");
        } else {
            outdir.mkdirs();
            fout = new File(outdir, "test.csv");
            if (!fout.exists() || overwrite) {
                BufferedReader br;
                StreamTokenizer st;
                br = Generic_IO.getBufferedReader(fin);
                st = new StreamTokenizer(br);
                Generic_IO.setStreamTokenizerSyntax7(st);
                try {
                    pw = new PrintWriter(fout);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(LR_JoinPricePaidDataAndOwnershipData_Process.class.getName()).log(Level.SEVERE, null, ex);
                }
                boolean read;
                read = false;
                String line;
                LR_PricePaid_Record r;
//                // read header
//                Generic_ReadCSV.readLine(st, null);
                int lineNumber;
                lineNumber = 0;

                int matches = 0;

                while (!read) {
                    line = Generic_ReadCSV.readLine(st, null);

                    //line = Generic_ReadCSV.readLine(st, null); // debug
                    //line = Generic_ReadCSV.readLine(st, null);
                    if (line == null) {
                        read = true;
                    } else {
                        try {
                            r = new LR_PricePaid_Record(Env, line);
                            ite = OCODList.iterator();

                            String[] split;
                            String[] split2;
                            String opa;
                            String rpa;
                            String street;

                            // 44 Hanover Square, Leeds (LS3 1BQ)
                            // 44 Hanover Square, Leeds (LS3 1BQ)
                            System.out.println("Match: " + r.PAON + ", " + r.Street + ", " + r.TownOrCity);
//                            if (r.PAON.equalsIgnoreCase("44")) {
//                                if (r.Street.equalsIgnoreCase("Hanover Square")) {
//                                    if (r.TownOrCity.equalsIgnoreCase("Leeds")) {
                            while (ite.hasNext()) {
                                or = ite.next();
                                opa = or.getPropertyAddress();
                                split = opa.split(", ");
                                split2 = split[0].split(" ");
                                if (split2[0].equalsIgnoreCase(r.PAON)) {
                                    street = split[0].substring(split2[0].length()).trim();
//                                    for (int i = 0; i < split.length; i++) {
//                                        System.out.println(split[i]);
//                                    }
                                    if (street.equalsIgnoreCase(r.Street)) {
                                        split2 = split[1].split(" ");
                                        if (split2[0].equalsIgnoreCase(r.TownOrCity)) {
                                            System.out.println("Matched: " + r.PAON + ", " + r.Street + ", " + r.TownOrCity);
                                            System.out.println("opa: " + opa);
                                            System.out.println("lineNumber: " + lineNumber);
                                            matches++;
                                            pw.println(line);
                                        }
                                    }
                                }
                                int debug = 1;
                            }
//                                    }
//                                }
//                            }
                            /**
                             * while (ite.hasNext()) { or = ite.next(); opa =
                             * or.getPropertyAddress();
                             *
                             * //System.out.println("opa: " + opa); split =
                             * opa.split(", ");
                             *
                             * System.out
                             *
                             * split2 = split[0].split(" "); if
                             * (split2[0].equalsIgnoreCase(r.PAON)) { street =
                             * split[0].substring(split2[0].length());
                             * //System.out.println("r.PAON: " + r.PAON);
                             * //System.out.println("opa: " + opa); if
                             * (split2[1].equalsIgnoreCase(r.Street)) {
                             * //System.out.println("r.Street: " + r.Street);
                             * //System.out.println("opa: " + opa);
                             *
                             * if (split.length > 4) { // Deal with cases where
                             * we have no locality split2 = split[3].split(" ");
                             * if (split2[0].equalsIgnoreCase(r.TownOrCity)) {
                             * System.out.println("Matched: " + r.PAON + ", " +
                             * r.Street + ", " + r.TownOrCity);
                             * System.out.println("opa: " + opa);
                             * System.out.println("lineNumber: " + lineNumber);
                             * matches++; pw.println(line); } } else { int debug
                             * = 1; }
                             *
                             * // Deal with cases where we have a locality if
                             * (split[1].equalsIgnoreCase(r.Street)) { if
                             * (split[2].equalsIgnoreCase(r.Locality)) {
                             * //System.out.println("r.Locality: " +
                             * r.Locality); //System.out.println("opa: " + opa);
                             * split2 = split[3].split(" "); if
                             * (split2[0].equalsIgnoreCase(r.TownOrCity)) {
                             * System.out.println("Matched: " + r.PAON + ", " +
                             * r.Street + ", " + r.TownOrCity);
                             * System.out.println("opa: " + opa);
                             * System.out.println("lineNumber: " + lineNumber);
                             * matches++; pw.println(line); } } }
                             *
                             * }
                             * }
                             * //System.out.println(opa);
                             *
                             * }
                             */
                            lineNumber++;
                            //if (r.District.equalsIgnoreCase("LEEDS")) {

                            //}
                        } catch (ArrayIndexOutOfBoundsException e) {
                            //e.printStackTrace(System.err);
                            System.out.println("Line " + lineNumber + " is not a nomal line:" + line);
                        } catch (Exception ex) {
                            System.err.println("Line: " + line);
                            Logger.getLogger(LR_JoinPricePaidDataAndOwnershipData_Process.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                System.out.println("Acheived " + matches + " address matches out of a possible " + lineNumber);
                pw.close();
            } else {
                System.out.println("Output file " + fout + " already exists and is not being overwritten.");
            }
        }
    }
}
