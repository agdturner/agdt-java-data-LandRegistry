/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.landregistry.process;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_ReadCSV;
import uk.ac.leeds.ccg.andyt.generic.utilities.Generic_Collections;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.LR_COU_Record;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.LR_FULL_Record;

/**
 * For reading and processing data from
 *
 * @author geoagdt
 */
public class Test {

    public static void main(String[] args) {
        new Test().run();
    }

    TreeMap<String, Integer> tenureCounts;
    TreeMap<String, Integer> districtCounts;
    TreeMap<String, Integer> countyCounts;
    TreeMap<String, Integer> regionCounts;
    TreeMap<String, Integer> postcodeCounts;
    TreeMap<String, Integer> multipleAddressIndicatorCounts;
    TreeMap<String, Integer> PricePaidCounts;
    TreeMap<String, Integer> companyRegistrationNo1Counts;
    TreeMap<String, Integer> proprietorshipCategory1Counts;
    TreeMap<String, Integer> countryIncorporated1Counts;

    public void run() {

        File dataDir;
        dataDir = new File(System.getProperty("user.dir"), "data");

        File inputDataDir;
        inputDataDir = new File(dataDir, "input");

        File outputDataDir;
        outputDataDir = new File(dataDir, "output");

        String name;
        name = "CCOD_COU_2017_11";
        //name = "CCOD_COU_2017_12";
        //name = "CCOD_COU_2018_01";
        //name = "CCOD_COU_2018_02";
        //name = "OCOD_COU_2017_11";
        //name = "OCOD_COU_2017_12";
        //name = "OCOD_COU_2018_01";
        //name = "OCOD_COU_2018_02";

        File indir;
        indir = new File(inputDataDir, name);

        System.out.println("indir " + indir);

        File outdir;
        outdir = new File(outputDataDir, name);

        System.out.println("outdir " + outdir);

        File f;
        f = new File(indir, name + ".csv");

        if (!f.exists()) {
            System.out.println("File " + f + " does not exist.");
        }

        ArrayList<String> lines;
        lines = Generic_ReadCSV.read(f, null, 7);

        outdir.mkdirs();
        f = new File(outdir, name + ".csv");

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(f);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Initialisation
        tenureCounts = new TreeMap<>();
        districtCounts = new TreeMap<>();
        countyCounts = new TreeMap<>();
        regionCounts = new TreeMap<>();
        postcodeCounts = new TreeMap<>();
        multipleAddressIndicatorCounts = new TreeMap<>();
        PricePaidCounts = new TreeMap<>();
        companyRegistrationNo1Counts = new TreeMap<>();
        proprietorshipCategory1Counts = new TreeMap<>();
        countryIncorporated1Counts = new TreeMap<>();

        pw.println(LR_COU_Record.header());

        //LR_FULL_Record r;
        LR_COU_Record r;
        for (long ID = 1; ID < lines.size(); ID++) {
            //r = new LR_FULL_Record(ID, lines.get((int) ID));
            try {
                r = new LR_COU_Record(ID, lines.get((int) ID));
                if (r.getDistrict().equalsIgnoreCase("LEEDS")) {
                    System.out.println(r.toCSV());
                    pw.print(r.toCSV());
                }
            } catch (ArrayIndexOutOfBoundsException e) {
//                e.printStackTrace(System.err);
//                System.out.println("" + ID + " lines.size() " + lines.size());
//                System.out.println("line " + lines.get((int) ID));
            }
            //addToCounts(r);
        }

        // Write out summaries.
    }

    void addToCounts(LR_FULL_Record r) {
        Generic_Collections.addToTreeMapStringInteger(tenureCounts, r.getTenure(), 1);
        Generic_Collections.addToTreeMapStringInteger(districtCounts, r.getDistrict(), 1);
        Generic_Collections.addToTreeMapStringInteger(countyCounts, r.getCounty(), 1);
        Generic_Collections.addToTreeMapStringInteger(postcodeCounts, r.getPostcode(), 1);
        Generic_Collections.addToTreeMapStringInteger(multipleAddressIndicatorCounts, r.getMultipleAddressIndicator(), 1);
        Generic_Collections.addToTreeMapStringInteger(PricePaidCounts, r.getPricePaid(), 1);
        Generic_Collections.addToTreeMapStringInteger(companyRegistrationNo1Counts, r.getCompanyRegistrationNo1(), 1);
        Generic_Collections.addToTreeMapStringInteger(proprietorshipCategory1Counts, r.getProprietorshipCategory1(), 1);
        Generic_Collections.addToTreeMapStringInteger(countryIncorporated1Counts, r.getCountryIncorporated1(), 1);
    }
}
