package uk.ac.leeds.ccg.andyt.projects.landregistry.core;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import uk.ac.leeds.ccg.andyt.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.projects.landregistry.io.LR_Files;

/**
 *
 * @author geoagdt
 */
public class LR_Environment extends LR_OutOfMemoryErrorHandler
        implements Serializable {

    public Generic_Environment ge;

    public LR_Strings Strings;
    public LR_Files Files;

    /**
     * A collection of all current Comprised of NonNullTypes and NullTypes.
     */
    public HashSet<LR_ID2> IDs;

    public ArrayList<LR_ID> NonNullTypes;
    public ArrayList<LR_ID> NullTypes;
    public HashMap<LR_ID, String> IDToType;
    public HashMap<String, LR_ID> TypeToID;
    public HashMap<LR_ID, HashMap<LR_ID, String>> IDToLookups;
    public HashMap<LR_ID, HashMap<String, LR_ID>> ToIDLookups;
    public HashMap<LR_ID, Boolean> UpdatedNonNullTypes;
    public HashMap<LR_ID, Boolean> UpdatedNullTypes;

    public HashMap<LR_ID, HashSet<LR_ID>> NullTitleNumberIDCollections;

    /**
     * Set to true if a new ID is added.
     */
    public boolean UpdatedIDs;

    public LR_Environment() {
        Strings = new LR_Strings();
        Files = new LR_Files(Strings);
        ge = new Generic_Environment(Files, Strings);
        initCollections();
        UpdatedIDs = false;
    }

    public void writeIDs() {
        if (UpdatedIDs) {
            String s;
            s = Strings.S_IDs;
            File f;
            f = Files.getGeneratedDataFile(s, Strings.S_HashSet);
            Generic_StaticIO.writeObject(IDs, f, s);
            UpdatedIDs = false;
        }
    }

    public void loadIDs() {
        File f;
        f = Files.getGeneratedDataFile(Strings.S_IDs, Strings.S_HashSet);
        if (!f.exists()) {
            IDs = new HashSet<>();
        } else {
            IDs = (HashSet<LR_ID2>) Generic_StaticIO.readObject(f);
        }
    }

    public void writeLookup(LR_ID typeID) {
        if (UpdatedNonNullTypes.get(typeID)) {
            String typeName0;
            typeName0 = IDToType.get(typeID);
            String typeName;
            typeName = Strings.S_IDTo + typeName0;
            File f;
            f = Files.getGeneratedDataFile(typeName, Strings.S_HashMap);
            Generic_StaticIO.writeObject(IDToLookups.get(typeID), f, typeName);
            typeName = typeName0 + Strings.S_ToID;
            f = Files.getGeneratedDataFile(typeName, Strings.S_HashMap);
            Generic_StaticIO.writeObject(ToIDLookups.get(typeID), f, typeName);
            UpdatedNonNullTypes.put(typeID, false);
        }
    }

    public void loadIDTo(LR_ID typeID) {
        String typeName0;
        typeName0 = IDToType.get(typeID);
        String typeName;
        typeName = Strings.S_IDTo + typeName0;
        File f;
        f = Files.getGeneratedDataFile(typeName, Strings.S_HashMap);
        HashMap<LR_ID, String> IDTo;
        if (!f.exists()) {
            IDTo = new HashMap<>();
        } else {
            IDTo = (HashMap<LR_ID, String>) Generic_StaticIO.readObject(f);
        }
        IDToLookups.put(typeID, IDTo);
    }

    public void loadToID(LR_ID typeID) {
        String typeName0;
        typeName0 = IDToType.get(typeID);
        String typeName;
        typeName = typeName0 + Strings.S_ToID;
        File f;
        f = Files.getGeneratedDataFile(typeName, Strings.S_HashMap);
        HashMap<String, LR_ID> ToID;
        if (!f.exists()) {
            ToID = new HashMap<>();
        } else {
            ToID = (HashMap<String, LR_ID>) Generic_StaticIO.readObject(f);
        }
        ToIDLookups.put(typeID, ToID);
    }

    public void loadNullCollection(String s) {
        LR_ID typeID;
        typeID = TypeToID.get(s);
        HashSet<LR_ID> c;
        File f;
        f = Files.getGeneratedDataFile(s, Strings.S_HashSet);
        if (!f.exists()) {
            c = new HashSet<>();
        } else {
            c = (HashSet<LR_ID>) Generic_StaticIO.readObject(f);
        }
        NullTitleNumberIDCollections.put(typeID, c);
    }

    public void writeNullCollection(LR_ID typeID) {
        if (UpdatedNullTypes.get(typeID)) {
            String s;
            s = IDToType.get(typeID);
            File f;
            f = Files.getGeneratedDataFile(
                    Strings.S_Null + s + Strings.S_TitleNumber + Strings.S_ID,
                    Strings.S_HashSet);
            Generic_StaticIO.writeObject(
                    NullTitleNumberIDCollections.get(typeID), f, s);
            UpdatedNullTypes.put(typeID, false);
        }
    }

    protected void addNonNullType(String type) {
        LR_ID id;
        id = new LR_ID(NonNullTypes.size());
        if (!IDToType.containsKey(id)) {
            TypeToID.put(type, id);
            IDToType.put(id, type);
        }
        NonNullTypes.add(id);
        UpdatedNonNullTypes.put(id, false);
        IDToLookups.put(id, new HashMap<>());
        ToIDLookups.put(id, new HashMap<>());
    }

    
    protected LR_ID addNullType(String type) {
        LR_ID id;
        id = new LR_ID(NullTypes.size());
        if (!IDToType.containsKey(id)) {
            TypeToID.put(type, id);
            IDToType.put(id, type);
        }
        NullTypes.add(id);
        UpdatedNullTypes.put(id, false);
        NullTitleNumberIDCollections.put(id, new HashSet<>());
        return id;
    }

    protected final void initCollections() {
        NonNullTypes = new ArrayList<>();
        NullTypes = new ArrayList<>();
        TypeToID = new HashMap<>();
        IDToType = new HashMap<>();
        IDToLookups = new HashMap<>();
        ToIDLookups = new HashMap<>();
        UpdatedNonNullTypes = new HashMap<>();
        UpdatedNullTypes = new HashMap<>();
        NullTitleNumberIDCollections = new HashMap<>();
        addNonNullType(Strings.S_TitleNumber);
        addNonNullType(Strings.S_PropertyAddress);
        addNonNullType(Strings.S_Tenure);
        addNonNullType(Strings.S_PricePaid);
        addNonNullType(Strings.S_CompanyRegistrationNo);
        addNonNullType(Strings.S_ProprietorName);
        addNonNullType(Strings.S_ProprietorshipCategory);
        addNonNullType(Strings.S_CountryIncorporated);
        addNonNullType(Strings.S_PostcodeDistrict);
        addNullType(Strings.S_PropertyAddress);
        addNullType(Strings.S_PricePaid);
        addNullType(Strings.S_CompanyRegistrationNo);
        addNullType(Strings.S_ProprietorName);
        addNullType(Strings.S_ProprietorshipCategory);
        addNullType(Strings.S_CountryIncorporated);
        addNullType(Strings.S_PostcodeDistrict);
    }

    /**
     * Write out collections if necessary
     */
    public void writeCollections() {
        writeIDs();
        Iterator<LR_ID> ite;
        ite = NonNullTypes.iterator();
        while (ite.hasNext()) {
            writeLookup(ite.next());
        }
        ite = NullTypes.iterator();
        while (ite.hasNext()) {
            writeNullCollection(ite.next());
        }
    }

}
