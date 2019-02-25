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
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.data.format.Data_ReadCSV;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_IO;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Environment;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.landregistry.pricepaid.LR_PricePaid_Record;

/**
 * For reading, selecting and writing out selected records.
 */
public class LR_LoadPricePaidData_Process extends LR_Main_Process {

    boolean overwrite;

    public LR_LoadPricePaidData_Process(LR_Environment env, boolean overwrite) {
        super(env);
        this.overwrite = overwrite;
    }

    /**
     * @param area
     */
    public void run(String area) {
        
        //selectBPPDCategoryType();
        //selectArea(area);
        test(area);
        
    }
    
    /**
     * Select records where PPDCategoryType equals "B" and District = "LEEDS"
     * @param area 
     */
    public void test(String area) {
        File outdir;
        File fin;
        File fout;
        PrintWriter pw = null;
        outdir = new File(env.files.getOutputDataDir(), "PricePaid");
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
                    Logger.getLogger(LR_LoadPricePaidData_Process.class.getName()).log(Level.SEVERE, null, ex);
                }
                boolean read;
                read = false;
                String line;
                LR_PricePaid_Record r;
//                // read header
//                Data_ReadCSV.readLine(st, null);
                int lineNumber;
                lineNumber = 0;

                while (!read) {
                    line = Data_ReadCSV.readLine(st, null);
                    if (line == null) {
                        read = true;
                    } else {
                        try {
                            r = new LR_PricePaid_Record(env, line);
                            //if (r.District.equalsIgnoreCase("LEEDS")) {
                                pw.println(line);
                            //}
                        } catch (ArrayIndexOutOfBoundsException e) {
                            //e.printStackTrace(System.err);
                            System.out.println("Line " + lineNumber + " is not a nomal line:" + line);
                        } catch (Exception ex) {
                            System.err.println("Line: " + line);
                            Logger.getLogger(LR_LoadPricePaidData_Process.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                pw.close();
            } else {
                System.out.println("Output file " + fout + " already exists and is not being overwritten.");
            }
        }
    }
    
    /**
     * Select records where PPDCategoryType equals "B" and District = "LEEDS"
     * @param area 
     */
    public void selectArea(String area) {
        File indir;
        File outdir;
        File fin;
        File fout;
        PrintWriter pw = null;

        indir = new File(env.files.getInputDataDir(), "PricePaid");
        System.out.println("indir " + indir);
        outdir = new File(env.files.getOutputDataDir(), "PricePaid");
        System.out.println("outdir " + outdir);
        fin = new File(outdir, "pp-complete-PPDCategoryType-B.csv");
        if (!fin.exists()) {
            System.out.println("Input file " + fin + " does not exist.");
        } else {
            outdir.mkdirs();
            fout = new File(outdir, "pp-complete-PPDCategoryType-B-District-LEEDS.csv");
            if (!fout.exists() || overwrite) {
                BufferedReader br;
                StreamTokenizer st;
                br = Generic_IO.getBufferedReader(fin);
                st = new StreamTokenizer(br);
                Generic_IO.setStreamTokenizerSyntax7(st);
                try {
                    pw = new PrintWriter(fout);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(LR_LoadPricePaidData_Process.class.getName()).log(Level.SEVERE, null, ex);
                }
                boolean read;
                read = false;
                String line;
                LR_PricePaid_Record r;
//                // read header
//                Data_ReadCSV.readLine(st, null);
                int lineNumber;
                lineNumber = 0;

                while (!read) {
                    line = Data_ReadCSV.readLine(st, null);
                    if (line == null) {
                        read = true;
                    } else {
                        try {
                            r = new LR_PricePaid_Record(env, line);
                            if (r.District.equalsIgnoreCase("LEEDS")) {
                                pw.println(line);
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            //e.printStackTrace(System.err);
                            System.out.println("Line " + lineNumber + " is not a nomal line:" + line);
                        } catch (Exception ex) {
                            System.err.println("Line: " + line);
                            Logger.getLogger(LR_LoadPricePaidData_Process.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                pw.close();
            } else {
                System.out.println("Output file " + fout + " already exists and is not being overwritten.");
            }
        }
    }
    
    /**
     * Write out records where PPDCategoryType equals "B".
     */
    public void selectBPPDCategoryType() {
        File indir;
        File outdir;
        File fin;
        File fout;
        PrintWriter pw = null;

        indir = new File(env.files.getInputDataDir(), "PricePaid");
        System.out.println("indir " + indir);
        outdir = new File(env.files.getOutputDataDir(), "PricePaid");
        System.out.println("outdir " + outdir);
        fin = new File(indir, "pp-complete.csv");
        if (!fin.exists()) {
            System.out.println("Input file " + fin + " does not exist.");
        } else {
            outdir.mkdirs();
            fout = new File(outdir, "pp-complete-PPDCategoryType-B.csv");
            if (!fout.exists() || overwrite) {
                BufferedReader br;
                StreamTokenizer st;
                br = Generic_IO.getBufferedReader(fin);
                st = new StreamTokenizer(br);
                Generic_IO.setStreamTokenizerSyntax7(st);
                try {
                    pw = new PrintWriter(fout);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(LR_LoadPricePaidData_Process.class.getName()).log(Level.SEVERE, null, ex);
                }
                boolean read;
                read = false;
                String line;
                LR_PricePaid_Record r;
//                // read header
//                Data_ReadCSV.readLine(st, null);
                int lineNumber;
                lineNumber = 0;

                while (!read) {
                    line = Data_ReadCSV.readLine(st, null);
                    if (line == null) {
                        read = true;
                    } else {
                        try {
                            r = new LR_PricePaid_Record(env, line);
                            if (r.PPDCategoryType.equalsIgnoreCase("B")) {
                                pw.println(line);
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            //e.printStackTrace(System.err);
                            System.out.println("Line " + lineNumber + " is not a nomal line:" + line);
                        } catch (Exception ex) {
                            System.err.println("Line: " + line);
                            Logger.getLogger(LR_LoadPricePaidData_Process.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                pw.close();
            } else {
                System.out.println("Output file " + fout + " already exists and is not being overwritten.");
            }
        }
    }
}
