/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.landregistry.data.transparencyinternational;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import uk.ac.leeds.ccg.andyt.data.format.Data_ReadTXT;
import uk.ac.leeds.ccg.andyt.generic.lang.Generic_String;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Environment;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Object;

/**
 *
 * @author geoagdt
 */
public class LR_TI_Handler extends LR_Object {

    public LR_TI_Handler(LR_Environment e) {
        super(e);
    }

    public HashMap<String, Integer> loadFromSource(File f) throws IOException {
        HashMap<String, Integer> r = new HashMap<>();
        if (!f.exists()) {
            System.out.println("File " + f + " does not exist.");
        }
        Data_ReadTXT reader = new Data_ReadTXT(env.de);
        ArrayList<String> lines = reader.read(f, null, 7);
        Iterator<String> ite = lines.iterator();
        // skip header;
        ite.next();
        while (ite.hasNext()) {
            LR_TI_Record rec = new LR_TI_Record(ite.next());
            r.put(rec.getCountry().toLowerCase(), rec.getCPIScore2017());
        }
        return r;
    }
}
