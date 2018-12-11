/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.landregistry.data.transparencyinternational;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import uk.ac.leeds.ccg.andyt.data.format.Generic_ReadCSV;
import uk.ac.leeds.ccg.andyt.generic.lang.Generic_String;

/**
 *
 * @author geoagdt
 */
public class LR_TI_Handler {

    public LR_TI_Handler() {
    }

    public static HashMap<String, Integer> loadFromSource(File f) {
        HashMap<String, Integer> result;
        result = new HashMap<>();
        ArrayList<String> lines;
        if (!f.exists()) {
            System.out.println("File " + f + " does not exist.");
        }
        lines = Generic_ReadCSV.read(f, null, 7);
        Iterator<String> ite;
        ite = lines.iterator();
        String line;
        // skip header;
        ite.next();
            LR_TI_Record r;
        while(ite.hasNext()) {
            line = ite.next();
            r = new LR_TI_Record(line);
            result.put(Generic_String.getUpperCase(r.getCountry()),
                    r.getCPIScore2017());
        }
        return result;
    }
}
