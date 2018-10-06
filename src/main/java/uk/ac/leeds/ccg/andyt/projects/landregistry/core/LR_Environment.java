package uk.ac.leeds.ccg.andyt.projects.landregistry.core;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import uk.ac.leeds.ccg.andyt.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.andyt.generic.data.Generic_Interval_long1;
import uk.ac.leeds.ccg.andyt.generic.data.Generic_UKPostcode_Handler;
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
    public Generic_UKPostcode_Handler PostcodeHandler;

    /**
     * A collection of all current Comprised of NonNullTypes and NullTypes.
     */
    public HashSet<LR_ID2> IDs;

    /**
     * For storing NonNullTypes used for indexing IDToLookups, ToIDLookups and
     * UpdatedNonNullTypes.
     */
    public HashSet<LR_ID> NonNullTypes;

    /**
     * For storing NullTypes used for indexing NullTitleNumberIDCollections.
     */
    public HashSet<LR_ID> NullTypes;

    /**
     * Keys are Types and values are String names of Types. This is the reverse
     * of TypeToID.
     */
    public HashMap<LR_ID, String> IDToType;

    /**
     * Keys are String names of Types and values are Types. This is the reverse
     * of IDToType.
     */
    public HashMap<String, LR_ID> TypeToID;

    /**
     * Keys are NonNullTypes and values are Lookups of an ID to a String. The
     * values are a reverse of the respective ToIDLookups.
     */
    public HashMap<LR_ID, HashMap<LR_ID, String>> IDToLookups;

    /**
     * Keys are NonNullTypes and values are Lookups of a String to an ID. The
     * values are a reverse of the respective IDToLookups.
     */
    public HashMap<LR_ID, HashMap<String, LR_ID>> ToIDLookups;

    /**
     * For indicating if there have been updates in the respective IDToLookup or
     * ToIDLookup.
     */
    public HashMap<LR_ID, Boolean> UpdatedNonNullTypes;

    /**
     * For indicating if there have been updates in the respective
     * NullTitleNumberIDCollection.
     */
    public HashMap<LR_ID, Boolean> UpdatedNullTypes;

    /**
     * Keys are NullTypes and values are Collections of TitleNumberIDs
     * representing those inputs with null values for that type.
     */
    public HashMap<LR_ID, HashSet<LR_ID>> NullTitleNumberIDCollections;

    /**
     * Keys are Address IDs, Values are collections of TitleNumberIDs.
     */
    public HashMap<LR_ID, HashSet<LR_ID>> AddressIDToTitleNumberIDsLookup;

    /**
     * Keys are TitleNumberIDs, values are Address IDs.
     */
    public HashMap<LR_ID, LR_ID> TitleNumberIDToAddressIDLookup;

    /**
     * Indicates if AddressToTitleNumberIDsLookup has changed.
     */
    public boolean UpdatedAddressIDToTitleNumberIDsLookup;

    /**
     * Indicates if TitleNumberIDToAddressLookup has changed.
     */
    public boolean UpdatedTitleNumberIDToAddressIDLookup;

    /**
     * Indicates if PricePaidLookup has changed.
     */
    public boolean UpdatedPricePaidLookup;

    /**
     * Set to true if a new ID is added.
     */
    public boolean UpdatedIDs;

    /**
     * For looking up the upper and lower bounds for PricePaid data classes
     */
    public HashMap<LR_ID, Generic_Interval_long1> PricePaidLookup;

    long MinPricePaidClass;
    long MaxPricePaidClass;

    public LR_Environment() {
        Strings = new LR_Strings();
        Files = new LR_Files(Strings, Strings.getS_data());
        ge = new Generic_Environment(Files, Strings);
        initCollections();
        UpdatedIDs = false;
        PostcodeHandler = new Generic_UKPostcode_Handler();
    }

    /**
     * If IDs has been updated, then write it out to file and set
     * UpdatedAddressIDToTitleNumberIDsLookup and
     * UpdatedTitleNumberIDToAddressIDLookup to true.
     */
    public void writeIDs() {
        if (UpdatedIDs) {
            write(Strings.S_IDs, Strings.S_HashSet, IDs);
            UpdatedIDs = false;
            UpdatedAddressIDToTitleNumberIDsLookup = true;
            UpdatedTitleNumberIDToAddressIDLookup = true;
        }
    }

    public void loadIDToType() {
        File f;
        f = Files.getGeneratedDataFile(Strings.S_IDToType, Strings.S_HashMap);
        if (!f.exists()) {
            IDToType = new HashMap<>();
        } else {
            System.out.println("Loading " + f);
            IDToType = (HashMap<LR_ID, String>) Generic_StaticIO.readObject(f);
            System.out.println("Loaded " + f);
        }
    }

    public void loadTypeToID() {
        File f;
        f = Files.getGeneratedDataFile(Strings.S_TypeToID, Strings.S_HashMap);
        if (!f.exists()) {
            TypeToID = new HashMap<>();
        } else {
            System.out.println("Loading " + f);
            TypeToID = (HashMap<String, LR_ID>) Generic_StaticIO.readObject(f);
            System.out.println("Loaded " + f);
        }
    }

    public void loadIDs() {
        File f;
        f = Files.getGeneratedDataFile(Strings.S_IDs, Strings.S_HashSet);
        if (!f.exists()) {
            IDs = new HashSet<>();
        } else {
            System.out.println("Loading " + f);
            IDs = (HashSet<LR_ID2>) Generic_StaticIO.readObject(f);
            System.out.println("Loaded " + f);
        }
    }

    public void writeLookup(LR_ID typeID) {
        if (UpdatedNonNullTypes.get(typeID)) {
            String type0;
            type0 = IDToType.get(typeID);
            String type;
            type = Strings.S_IDTo + type0;
            write(type, Strings.S_HashMap, IDToLookups.get(typeID));
            type = type0 + Strings.S_ToID;
            write(type, Strings.S_HashMap, ToIDLookups.get(typeID));
            UpdatedNonNullTypes.put(typeID, false);
        }
    }

    public void loadIDTo(LR_ID typeID, String type) {
        File f;
        f = Files.getGeneratedDataFile(Strings.S_IDTo + type,
                Strings.S_HashMap);
        HashMap<LR_ID, String> m;
        if (!f.exists()) {
            m = new HashMap<>();
        } else {
            System.out.println("Loading " + f);
            m = (HashMap<LR_ID, String>) Generic_StaticIO.readObject(f);
            System.out.println("Loaded " + f);
        }
        IDToLookups.put(typeID, m);
    }

    public void loadToID(LR_ID typeID, String type) {
        File f;
        f = Files.getGeneratedDataFile(type + Strings.S_ToID,
                Strings.S_HashMap);
        HashMap<String, LR_ID> m;
        if (!f.exists()) {
            m = new HashMap<>();
        } else {
            System.out.println("Loading " + f);
            m = (HashMap<String, LR_ID>) Generic_StaticIO.readObject(f);
            System.out.println("Loaded " + f);
        }
        ToIDLookups.put(typeID, m);
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
            System.out.println("Loading " + f);
            c = (HashSet<LR_ID>) Generic_StaticIO.readObject(f);
            System.out.println("Loaded " + f);
        }
        NullTitleNumberIDCollections.put(typeID, c);
    }

    /**
     * For loading NonNullTypes from a particular file.
     */
    public void loadNonNullTypes() {
        File f;
        f = Files.getGeneratedDataFile(Strings.S_NonNullTypes, Strings.S_HashSet);
        if (!f.exists()) {
            NonNullTypes = new HashSet<>();
        } else {
            System.out.println("Loading " + f);
            NonNullTypes = (HashSet<LR_ID>) Generic_StaticIO.readObject(f);
            System.out.println("Loaded " + f);
        }
    }

    /**
     * For loading the NullTypes from a particular file.
     */
    public void loadNullTypes() {
        File f;
        f = Files.getGeneratedDataFile(Strings.S_NullTypes, Strings.S_HashSet);
        if (!f.exists()) {
            NullTypes = new HashSet<>();
        } else {
            System.out.println("Loading " + f);
            NullTypes = (HashSet<LR_ID>) Generic_StaticIO.readObject(f);
            System.out.println("Loaded " + f);
        }
    }

    /**
     * For writing out the appropriate NullTitleNumberIDCollections to a
     * particular file which it is expected to perhaps get loaded again from in
     * memory handling or in a different run/process.
     *
     * @param typeID The type of NullTitleNumberIDCollection to write out.
     */
    public void writeNullCollection(LR_ID typeID) {
        if (UpdatedNullTypes.get(typeID)) {
            String s;
            s = Strings.S_Null + IDToType.get(typeID) + Strings.S_TitleNumber
                    + Strings.S_ID;
            write(s, Strings.S_HashSet, NullTitleNumberIDCollections.get(typeID));
            UpdatedNullTypes.put(typeID, false);
        }
    }

    /**
     * For writing out the AddressIDToTitleNumberIDsLookup to a particular file
     * which it is expected to perhaps get loaded again from in memory handling
     * or in a different run/process.
     */
    public void writeAddressIDToTitleNumberIDsLookup() {
        write(Strings.S_AddressIDToTitleNumberIDs, Strings.S_HashMap,
                AddressIDToTitleNumberIDsLookup);
        UpdatedAddressIDToTitleNumberIDsLookup = false;
    }

    /**
     * For loading the AddressIDToTitleNumberIDsLookup from a particular file.
     */
    public void loadAddressIDToTitleNumberIDsLookup() {
        File f;
        f = Files.getGeneratedDataFile(Strings.S_AddressIDToTitleNumberIDs,
                Strings.S_HashMap);
        if (!f.exists()) {
            AddressIDToTitleNumberIDsLookup = new HashMap<>();
        } else {
            System.out.println("Loading " + f);
            AddressIDToTitleNumberIDsLookup = (HashMap<LR_ID, HashSet<LR_ID>>) Generic_StaticIO.readObject(f);
            System.out.println("Loaded " + f);
        }
    }

    /**
     * For writing out the PricePaidLookup to a particular file which it is
     * expected to perhaps get loaded again from in memory handling or in a
     * different run/process.
     */
    public void writePricePaidLookup() {
        write(Strings.S_PricePaidLookup, Strings.S_HashMap, PricePaidLookup);
        UpdatedPricePaidLookup = false;
    }

    /**
     * For writing out the TitleNumberIDToAddressIDLookup to a particular file
     * which it is expected to perhaps get loaded again from in memory handling
     * or in a different run/process.
     */
    public void writeTitleNumberIDToAddressIDLookup() {
        write(Strings.S_TitleNumberIDToAddressID, Strings.S_HashMap,
                TitleNumberIDToAddressIDLookup);
        UpdatedTitleNumberIDToAddressIDLookup = false;
    }

    /**
     * Writes out IDToType to a specific file and reports this to stdout.
     */
    public void writeIDToType() {
        write(Strings.S_IDToType, Strings.S_HashMap, IDToType);
    }

    /**
     * Writes out TypeToID to a specific file and reports this to stdout.
     */
    public void writeTypeToID() {
        write(Strings.S_TypeToID, Strings.S_HashMap, TypeToID);
    }

    /**
     * Writes out NonNullTypes to a specific file and reports this to stdout.
     */
    public void writeNonNullTypes() {
        write(Strings.S_NonNullTypes, Strings.S_HashSet, NonNullTypes);
    }

    /**
     * Writes out NullTypes to a specific file and reports this to stdout.
     */
    public void writeNullTypes() {
        write(Strings.S_NullTypes, Strings.S_HashSet, NullTypes);
    }

    /**
     * Writes o to a specific file and reports this to stdout.
     *
     * @param name
     * @param type
     * @param o
     */
    public void write(String name, String type, Object o) {
        File f;
        f = Files.getGeneratedDataFile(name, type);
        Generic_StaticIO.writeObject(o, f, name);
    }

    /**
     * For loading the PricePaidLookup from a particular file.
     */
    public void initPricePaidLookup() {
        File f;
        f = Files.getGeneratedDataFile(Strings.S_PricePaidLookup,
                Strings.S_HashMap);
        if (!f.exists()) {
            PricePaidLookup = new HashMap<>();
            long l;
            long u;
            MinPricePaidClass = -1000L;
            l = MinPricePaidClass;
            u = 0L;
            addPricePaidInterval(l, u); // -10,000, 0
            l = u;
            u = 1000L;
            addPricePaidInterval(l, u); // 0, 1000
            l = u;
            u *= 5L;
            addPricePaidInterval(l, u); // 1000, 5000
            l = u;
            u *= 4L;
            addPricePaidInterval(l, u); // 5000, 20000
            for (int ll = 0; ll < 9; ll++) {
                l = u;
                u += 20000L;
                addPricePaidInterval(l, u); // 20000, 40000, 60000, 80000, 100,000, 120,000, 140,000, 160,000, 180,000, 200,000
            }
            for (int ll = 0; ll < 6; ll++) {
                l = u;
                u += 50000L;
                addPricePaidInterval(l, u); // 200,000, 250,000, 300,000, 350,000, 400,000, 450,000, 500,000
            }
            for (int ll = 0; ll < 6; ll++) {
                l = u;
                u += 250000L;
                addPricePaidInterval(l, u); // 750,000, 1,000,000, 1,250,000, 1,500,000, 1,750,000, 2,000,000
            }
            for (int ll = 0; ll < 4; ll++) {
                l = u;
                u += 2000000L;
                addPricePaidInterval(l, u); // 4,000,000, 6,000,000, 8,000,000, 10,000,000
            }
            MaxPricePaidClass = u;
            UpdatedPricePaidLookup = true;
        } else {
            System.out.println("Loading " + f);
            PricePaidLookup = (HashMap<LR_ID, Generic_Interval_long1>) Generic_StaticIO.readObject(f);
            MinPricePaidClass = -1000L;
            MaxPricePaidClass = 10000000L;
            System.out.println("Loaded " + f);
            UpdatedPricePaidLookup = false;
        }
    }

    private void addPricePaidInterval(long l, long u) {
        Generic_Interval_long1 i;
        i = new Generic_Interval_long1(l, u);
        PricePaidLookup.put(new LR_ID(PricePaidLookup.size()), i);
    }

    /**
     * For loading the TitleNumberIDToAddressIDLookup from a particular file.
     */
    public void loadTitleNumberIDToAddressLookup() {
        File f;
        f = Files.getGeneratedDataFile(Strings.S_TitleNumberIDToAddressID,
                Strings.S_HashMap);
        if (!f.exists()) {
            TitleNumberIDToAddressIDLookup = new HashMap<>();
        } else {
            System.out.println("Loading " + f);
            TitleNumberIDToAddressIDLookup = (HashMap<LR_ID, LR_ID>) Generic_StaticIO.readObject(f);
            System.out.println("Loaded " + f);
        }
    }

    /**
     * Adds a NonNullType with type given by type. The type and it's id are
     * added to TypeToID and IDToType, the type id is added to NonNullTypes
     * UpdatedNonNullTypes with a key given by the type id is set to false and a
     * new HashMap value is put in both IDToLookups and ToIDLookups (with the
     * key given as the type id).
     *
     * @param type
     */
    protected void addNonNullType(String type) {
        LR_ID id;
        id = getTypeID(type);
        NonNullTypes.add(id);
        UpdatedNonNullTypes.put(id, false);
        loadToID(id, type);
        loadIDTo(id, type);
    }

    protected LR_ID addNullType(String type) {
        LR_ID id;
        id = getTypeID(type);
        NullTypes.add(id);
        UpdatedNullTypes.put(id, false);
        NullTitleNumberIDCollections.put(id, new HashSet<>());
        return id;
    }

    protected LR_ID getTypeID(String type) {
        LR_ID id;
        if (TypeToID.containsKey(type)) {
            id = TypeToID.get(type);
        } else {
            id = new LR_ID(TypeToID.size());
            TypeToID.put(type, id);
            IDToType.put(id, type);
        }
        return id;
    }

    protected final void initCollections() {
        loadNonNullTypes();
        loadNullTypes();
        loadIDs();
        loadTypeToID();
        loadIDToType();
        loadAddressIDToTitleNumberIDsLookup();
        loadTitleNumberIDToAddressLookup();
        initPricePaidLookup();
        IDToLookups = new HashMap<>();
        ToIDLookups = new HashMap<>();
        UpdatedNonNullTypes = new HashMap<>();
        UpdatedNullTypes = new HashMap<>();
        NullTitleNumberIDCollections = new HashMap<>();
        addNonNullType(Strings.S_TitleNumber);
        addNonNullType(Strings.S_PropertyAddress);
        addNonNullType(Strings.S_Tenure);
        addNonNullType(Strings.S_PricePaidClass);
        addNonNullType(Strings.S_CompanyRegistrationNo);
        addNonNullType(Strings.S_ProprietorName);
        addNonNullType(Strings.S_ProprietorshipCategory);
        //addNonNullType(Strings.S_CountryIncorporated); // done in next line: addCountryIDToNonNull(Strings.S_United_Kingdom);
        addCountryIDToNonNull(Strings.S_United_Kingdom);
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
     * For adding country to NonNull Collections.
     *
     * @param country
     */
    private void addCountryIDToNonNull(String country) {
        String s;
        s = Strings.S_CountryIncorporated;
        addNonNullType(s);
        LR_ID typeID;
        typeID = TypeToID.get(s);
        HashMap<String, LR_ID> m;
        m = ToIDLookups.get(typeID);
        if (!m.containsKey(s)) {
            LR_ID id;
            id = new LR_ID(m.size());
            m.put(country, id);
            IDToLookups.get(typeID).put(id, country);
        }
    }

//    private void addCountryIDToNull(String country) {
//        String s;
//        s = Strings.S_CountryIncorporated;
//        addNonNullType(s);
//        LR_ID typeID;
//        typeID = TypeToID.get(s);
//        HashSet<LR_ID> c;
//        c = NullTitleNumberIDCollections.get(typeID);
//        LR_ID id;
//        id = ToIDLookups.get(typeID).get(country);
//        c.add(id);
//    }
    /**
     * Write out collections if necessary
     */
    public void writeCollections() {
        writeIDs();
        writeNullTypes();
        writeNonNullTypes();
        Iterator<LR_ID> ite;
        ite = NonNullTypes.iterator();
        while (ite.hasNext()) {
            writeLookup(ite.next());
        }
        ite = NullTypes.iterator();
        while (ite.hasNext()) {
            writeNullCollection(ite.next());
        }
        if (UpdatedAddressIDToTitleNumberIDsLookup) {
            writeAddressIDToTitleNumberIDsLookup();
        }
        if (UpdatedTitleNumberIDToAddressIDLookup) {
            writeTitleNumberIDToAddressIDLookup();
        }
        if (UpdatedPricePaidLookup) {
            writePricePaidLookup();
        }
    }

}
