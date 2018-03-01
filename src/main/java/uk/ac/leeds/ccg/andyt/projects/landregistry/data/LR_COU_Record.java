package uk.ac.leeds.ccg.andyt.projects.landregistry.data;

import java.io.Serializable;

/**
 *
 * @author geoagdt
 */
public class LR_COU_Record extends LR_FULL_Record implements Serializable {

    private final String ChangeIndicator;
    private final String ChangeDate;

    
    public LR_COU_Record(long ID, String line) {
        super(ID, line);
        String[] ls;
        ls = line.split("\",\"");
        ChangeIndicator = ls[39];
        ChangeDate = ls[40];
        }

    @Override
    public String toString() {
        return super.toString() + "ChangeIndicator " + getChangeIndicator()
                + ",ChangeDate " + getChangeDate();
    }

    @Override
    public String toCSV() {
        return super.toCSV() + "," + getChangeIndicator()
                + "," + getChangeDate();
    }

    public static String header() {
        return LR_FULL_Record.header() + "ChangeIndicator,ChangeDate";
    }
    
    /**
     * @return the ChangeIndicator
     */
    public String getChangeIndicator() {
        return ChangeIndicator;
    }

    /**
     * @return the ChangeDate
     */
    public String getChangeDate() {
        return ChangeDate;
    }
    
}
