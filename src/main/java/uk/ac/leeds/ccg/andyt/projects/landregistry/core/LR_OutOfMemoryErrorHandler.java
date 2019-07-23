package uk.ac.leeds.ccg.andyt.projects.landregistry.core;

import uk.ac.leeds.ccg.andyt.generic.memory.Generic_OutOfMemoryErrorHandler;

/**
 *
 * @author Andy Turner
 */
public abstract class LR_OutOfMemoryErrorHandler 
        extends Generic_OutOfMemoryErrorHandler {

    //static final long serialVersionUID = 1L;
    public static long Memory_Threshold = 10000000;

    @Override
    public boolean swapDataAny() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean swapDataAny(boolean handleOutOfMemoryError) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean checkAndMaybeFreeMemory() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
