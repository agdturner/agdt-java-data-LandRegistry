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
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_ReadCSV;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.landregistry.LR_CC_COU_Record;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.landregistry.LR_OC_COU_Record;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.landregistry.LR_Record;

/**
 * For reading, selecting and writing out selected records.
 */
public class LR_Select_Process extends LR_Main_Process {

    public LR_Select_Process() {
        super();
    }

    public void run(String area) {
        File inputDataDir;
        inputDataDir = Files.getInputDataDir(Strings);
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
        names0.add("CCOD");
        names0.add("OCOD");
        boolean isCCOD;
        //names1.add("COU");
        //names1.add("FULL");
        names2.add("2017_11");
//        names2.add("2017_12");
//        names2.add("2018_01");
        names2.add("2018_02");
//        names2.add("2018_03");

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
//            name00 += name0 + "_COU_";
            name00 += name0 + "_FULL_";
            ite2 = names2.iterator();
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
                f = new File(indir, name + ".csv");
                if (!f.exists()) {
                    System.out.println("File " + f + " does not exist.");
                }
                lines = Generic_ReadCSV.read(f, null, 7);
                outdir.mkdirs();
                f = new File(outdir, name + ".csv");
                try {
                    pw = new PrintWriter(f);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(LR_Select_Process.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (isCCOD) {
                    pw.println(LR_CC_COU_Record.header());
                } else {
                    pw.println(LR_OC_COU_Record.header());
                }

                //LR_FULL_Record r;
                LR_Record r;
                for (long ID = 1; ID < lines.size(); ID++) {
                    //r = new LR_FULL_Record(ID, lines.get((int) ID));
                    try {
                        if (isCCOD) {
                            r = new LR_CC_COU_Record(ID, lines.get((int) ID));
                        } else {
                            r = new LR_OC_COU_Record(ID, lines.get((int) ID));
                        }
                        if (r.getDistrict().equalsIgnoreCase(area)) {
                            //System.out.println(r.toCSV());
                            pw.println(r.toCSV());
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace(System.err);
                        System.out.println("" + ID + " lines.size() " + lines.size());
                        System.out.println("line " + lines.get((int) ID));
                    }
                }
                pw.close();
            }
        }
    }

}
