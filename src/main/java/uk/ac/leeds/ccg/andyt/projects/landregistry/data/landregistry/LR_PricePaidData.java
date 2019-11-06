/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.landregistry.data.landregistry;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.generic.util.Generic_Collections;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Environment;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Object;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.id.LR_ValueID;

/**
 *
 * @author geoagdt
 */
public class LR_PricePaidData extends LR_Object {

    /**
     * Individual PricePaid Data.
     */
    private final ArrayList<BigDecimal> PricePaid;

    /**
     * Classified counts For storing counts of the number of classes of a
     * particular value.
     */
    private final TreeMap<LR_ValueID, Integer> PricePaidCounts;

    public LR_PricePaidData(LR_Environment env) {
        super(env);
        PricePaid = new ArrayList<>();
        PricePaidCounts = new TreeMap<>();
    }

    /**
     * @return the PricePaid
     */
    public ArrayList<BigDecimal> getPricePaid() {
        return PricePaid;
    }

    /**
     * @return the PricePaidCounts
     */
    public TreeMap<LR_ValueID, Integer> getPricePaidCounts() {
        return PricePaidCounts;
    }

    /**
     * @param r
     */
    public void add(LR_Record r) {
        Long pp;
        pp = r.getPricePaidValue();
        if (pp != null) {
            PricePaid.add(new BigDecimal(pp));
            LR_ValueID v;
            v = r.getPricePaidClass();
            if (v != null) {
                Generic_Collections.addToMap(PricePaidCounts, v, 1);
            } else {
                int debug= 1;
            }
        }
    }

}
