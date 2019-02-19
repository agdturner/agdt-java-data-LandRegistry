package uk.ac.leeds.ccg.andyt.projects.landregistry.core;

import java.io.Serializable;

/**
 * @author Andy Turner
 */
public abstract class LR_Object implements Serializable {

    /**
     * A reference to LR_Environment
     */
    protected transient LR_Environment env;

    public LR_Object(LR_Environment env) {
        this.env = env;
    }
}
