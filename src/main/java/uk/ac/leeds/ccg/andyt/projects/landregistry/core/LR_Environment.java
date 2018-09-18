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

    /**
     * Comprised of NonNullTypes and NullTypes.
     */
    public ArrayList<LR_ID> Types;
    
    public ArrayList<LR_ID> NonNullTypes;
    public ArrayList<LR_ID> NullTypes;
    public HashMap<LR_ID, String> IDToType;
    public HashMap<String, LR_ID> TypeToID;
    public HashMap<LR_ID, HashMap<LR_ID, String>> IDToLookups;
    public HashMap<LR_ID, HashMap<String, LR_ID>> ToIDLookups;
    public HashMap<LR_ID, Boolean> UpdatedTypes;

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

    /**
     * For returning the ToIDLookup indexed by typeID.
     *
     * @param typeID
     * @return
     */
    public HashMap<String, LR_ID> getToIDLookup(LR_ID typeID) {
        return ToIDLookups.get(typeID);
    }

    /**
     * For returning the IDToLookup indexed by typeID.
     *
     * @param typeID
     * @return
     */
    public HashMap<LR_ID, String> getIDToLookup(LR_ID typeID) {
        return IDToLookups.get(typeID);
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
        if (UpdatedTypes.get(typeID)) {
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
            UpdatedTypes.put(typeID, false);
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
        if (UpdatedTypes.get(typeID)) {
            String s;
            s = IDToType.get(typeID);
            File f;
            f = Files.getGeneratedDataFile(s, Strings.S_HashSet);
            Generic_StaticIO.writeObject(
                    NullTitleNumberIDCollections.get(typeID), f, s);
            UpdatedTypes.put(typeID, false);
        }
    }

    protected void addType(String type) {
        LR_ID id;
        id = new LR_ID(Types.size());
        TypeToID.put(type, id);
        IDToType.put(id, type);
        Types.add(id);
        NonNullTypes.add(id);
        UpdatedTypes.put(id, false);
        IDToLookups.put(id, new HashMap<>());
        ToIDLookups.put(id, new HashMap<>());
    }

    protected LR_ID addTypeNull(String type) {
        LR_ID id;
        id = new LR_ID(Types.size());
        TypeToID.put(type, id);
        IDToType.put(id, type);
        Types.add(id);
        NullTypes.add(id);
        UpdatedTypes.put(id, false);
        NullTitleNumberIDCollections.put(id, new HashSet<>());
        return id;
    }

    protected final void initCollections() {
        Types = new ArrayList<>();
        NonNullTypes = new ArrayList<>();
        NullTypes = new ArrayList<>();
        TypeToID = new HashMap<>();
        IDToType = new HashMap<>();
        IDToLookups = new HashMap<>();
        ToIDLookups = new HashMap<>();
        UpdatedTypes = new HashMap<>();
        NullTitleNumberIDCollections = new HashMap<>();
        addType(Strings.S_TitleNumber);
        addType(Strings.S_PropertyAddress);
        addType(Strings.S_Tenure);
        addType(Strings.S_PricePaid);
        addType(Strings.S_CompanyRegistrationNo1);
        addType(Strings.S_ProprietorName1);
        addType(Strings.S_ProprietorshipCategory1);
        addType(Strings.S_CountryIncorporated1);
        addType(Strings.S_PostcodeDistrict);
        addTypeNull(Strings.S_TitleNumberIDsOfNullPropertyAddresses);
        addTypeNull(Strings.S_TitleNumberIDsOfNullPricePaid);
        addTypeNull(Strings.S_TitleNumberIDsOfNullProprietorName1);
        addTypeNull(Strings.S_TitleNumberIDsOfNullCompanyRegistrationNo1);
        addTypeNull(Strings.S_TitleNumberIDsOfNullProprietorshipCategory1);
    }

    public HashSet getNullTitleNumberIDCollections(LR_ID typeID) {
        HashSet result;
        result = NullTitleNumberIDCollections.get(typeID);
        if (result == null) {
            result = new HashSet<>();
            NullTitleNumberIDCollections.put(typeID, result);
        }
        return result;
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
