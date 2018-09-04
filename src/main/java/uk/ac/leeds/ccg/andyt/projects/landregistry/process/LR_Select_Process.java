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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_ReadCSV;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Environment;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.landregistry.LR_CC_COU_Record;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.landregistry.LR_CC_FULL_Record;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.landregistry.LR_OC_COU_Record;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.landregistry.LR_OC_FULL_Record;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.landregistry.LR_Record;

/**
 * For reading, selecting and writing out selected records.
 */
public class LR_Select_Process extends LR_Main_Process {

    boolean overwrite;

    protected LR_Select_Process() {
    }

    public LR_Select_Process(LR_Environment env) {
        super(env);
    }

    /**
     * @param area
     * @param doFull If doFull the run selects from the FULL data otherwise it
     * selects from the Change Only Update (COU) data.
     * @param overwrite
     */
    public void run(String area, boolean doFull, boolean overwrite) {
        this.overwrite = overwrite;
        File inputDataDir;
        inputDataDir = Files.getInputDataDir(Strings);
        File outputDataDir;
        outputDataDir = Files.getOutputDataDir(Strings);

        ArrayList<String> names0;
        //ArrayList<String> names1;
        ArrayList<String> setNames;
        String name;
        String name0;
        String name00;
        names0 = new ArrayList<>();
        names0.add(Env.Strings.S_CCOD);
        names0.add(Env.Strings.S_OCOD);
        boolean isCCOD;

        File indir;
        File outdir;
        File fin;
        File fout;
        PrintWriter pw = null;

        Iterator<String> ite0;
        Iterator<String> ite2;
        ite0 = names0.iterator();
        while (ite0.hasNext()) {
            name0 = ite0.next();
            isCCOD = name0.equalsIgnoreCase(Env.Strings.S_CCOD);
            name00 = getName00(doFull, name0);
            setNames = getSetNames(doFull, name0);
            ite2 = setNames.iterator();
            while (ite2.hasNext()) {
                name = name00;
                name += ite2.next();
                indir = new File(inputDataDir, name0);
                indir = new File(indir, name);
                System.out.println("indir " + indir);
                outdir = new File(outputDataDir, area);
                outdir = new File(outdir, name0);
                outdir = new File(outdir, name);
                System.out.println("outdir " + outdir);
                fin = new File(indir, name + ".csv");
                if (!fin.exists()) {
                    System.out.println("Input file " + fin + " does not exist.");
                } else {
                    outdir.mkdirs();
                    fout = new File(outdir, name + ".csv");
                    if (!fout.exists() || overwrite) {
                        BufferedReader br;
                        StreamTokenizer st;
                        br = Generic_StaticIO.getBufferedReader(fin);
                        st = new StreamTokenizer(br);
                        Generic_StaticIO.setStreamTokenizerSyntax7(st);
                        try {
                            pw = new PrintWriter(fout);
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(LR_Select_Process.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (isCCOD) {
                            if (doFull) {
                                pw.println(LR_CC_FULL_Record.header());
                            } else {
                                pw.println(LR_CC_COU_Record.header());
                            }
                        } else {
                            if (doFull) {
                                pw.println(LR_OC_FULL_Record.header());
                            } else {
                                pw.println(LR_OC_COU_Record.header());
                            }
                        }
                        boolean read;
                        read = false;
                        String line;
                        LR_Record r;
                        // read header
                        Generic_ReadCSV.readLine(st, null);
                        int ID;
                        ID = 1;
                        while (!read) {
                            line = Generic_ReadCSV.readLine(st, null);
                            if (line == null) {
                                read = true;
                            } else {
                                try {
                                    if (isCCOD) {
                                        if (doFull) {
                                             r = new LR_CC_FULL_Record(Env, line);
                                        }else {
                                            r = new LR_CC_COU_Record(Env, line);
                                        }
                                    } else {
                                        if (doFull) {
                                            r = new LR_OC_FULL_Record(Env, line);
                                        } else {
                                            r = new LR_OC_COU_Record(Env, line);
                                        }
                                    }
                                    if (r.getDistrict().equalsIgnoreCase(area)) {
                                        //System.out.println(r.toCSV());
                                        pw.println(r.toCSV());
                                    }
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    //e.printStackTrace(System.err);
                                    System.out.println("Line " + ID + " is not a nomal line:" + line);
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
        Env.writeCompanyRegistrationNoLookups();
        Env.writeCountryIncorporatedLookups();
        Env.writePropertyAddressLookups();
        Env.writeProprietorNameLookups();
        Env.writeTitleNumberLookups();
        Env.writeProprietorshipCategoryLookups();
        System.out.println("Written out lookups if necessary.");
    }

}
