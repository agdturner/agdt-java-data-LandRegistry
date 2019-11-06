package uk.ac.leeds.ccg.andyt.projects.landregistry.core;

import uk.ac.leeds.ccg.andyt.projects.landregistry.data.id.LR_ValueID;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.id.LR_TypeID;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.id.LR_ValueID_TypeID;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import uk.ac.leeds.ccg.andyt.data.core.Data_Environment;
import uk.ac.leeds.ccg.andyt.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.andyt.data.interval.Data_IntervalLong1;
import uk.ac.leeds.ccg.andyt.data.postcode.Data_UKPostcodeHandler;
import uk.ac.leeds.ccg.andyt.generic.lang.Generic_String;
import uk.ac.leeds.ccg.andyt.projects.landregistry.io.LR_Files;

/**
 *
 * @author geoagdt
 */
public class LR_Environment extends LR_MemoryManager {

    public transient final Data_Environment de;

    public final transient LR_Files files;
    
    /**
     * For convenience.
     */
    public transient final Generic_Environment env;

    /**
     * Stores the {@link ge#env} log ID for the log set up for WaAS.
     */
    public final int logID;
    
    public transient Data_UKPostcodeHandler PostcodeHandler;
    
    public transient final HashSet<String> NumeralsHashSet;
        

    public transient static final String EOL = System.getProperty("line.separator");

    /**
     * A collection of all unique IDs ()current records.
     */
    public final HashSet<LR_ValueID_TypeID> IDs;

    /**
     * For storing NonNullTypes used for indexing IDToLookups, ToIDLookups and
     * UpdatedNonNullTypes.
     */
    public final HashSet<LR_TypeID> NonNullTypes;

    /**
     * For storing NonNullPricePaidTypes.
     */
    public final HashSet<LR_TypeID> NonNullPricePaidTypes;

    /**
     * For storing NullTypes used for indexing NullCollections.
     */
    public final HashSet<LR_TypeID> NullTypes;

    /**
     * For storing all types.
     */
    public final HashSet<LR_TypeID> TypeIDs;

    /**
     * Keys are String names of Types and values are Types. This is the reverse
     * of IDToType.
     */
    public final HashMap<String, LR_TypeID> TypeToTypeID;

    /**
     * Keys are typeID and values are Collections of lookups from the ID of a
     * record to the respective value ID of the value assigned for the typeID.
     */
    public final HashMap<LR_TypeID, HashMap<LR_ValueID_TypeID, LR_ValueID>> NullTitleNumberIDCollections;

    /**
     * Keys are Address IDs, Values are collections of TitleNumberIDs.
     */
    public final HashMap<LR_ValueID, HashSet<LR_ValueID>> AddressIDToTitleNumberIDsLookup;

    /**
     * Keys are TitleNumberIDs, values are Address IDs.
     */
    public final HashMap<LR_ValueID, LR_ValueID> TitleNumberIDToAddressIDLookup;

    /**
     * Keys are types and values are Sets of values.
     */
    public final HashMap<LR_TypeID, HashSet<String>> Values;

    /**
     * Keys are types and values are Sets of value IDs.
     */
    public final HashMap<LR_TypeID, HashSet<LR_ValueID>> ValueIDs;

    /**
     * Keys are types and values are Lookups of a value String to an value ID.
     */
    public final HashMap<LR_TypeID, HashMap<String, LR_ValueID>> ValueReverseLookups;

    /**
     * For looking up the upper and lower bounds for PricePaid data classes
     */
    public HashMap<LR_ValueID, Data_IntervalLong1> PricePaidLookup;

    long MinPricePaidClass;
    long MaxPricePaidClass;

    /**
     * For storing TitleNumber TypeID.
     */
    public final LR_TypeID TitleNumberTypeID;

    /**
     * For storing CompanyRegistrationNo TypeID.
     */
    public final LR_TypeID CompanyRegistrationNoTypeID;

    /**
     * For storing District TypeID.
     */
    public final LR_TypeID DistrictTypeID;

    /**
     * For storing County TypeID.
     */
    public final LR_TypeID CountyTypeID;

    /**
     * For storing Region TypeID.
     */
    public final LR_TypeID RegionTypeID;

    /**
     * For storing PostcodeDistrict TypeID.
     */
    public final LR_TypeID PostcodeDistrictTypeID;

    /**
     * For storing PricePaid TypeID.
     */
    public final LR_TypeID PricePaidTypeID;

    /**
     * For storing PropertyAddress TypeID.
     */
    public final LR_TypeID PropertyAddressTypeID;

    /**
     * For storing ProprietorName TypeID.
     */
    public final LR_TypeID ProprietorNameTypeID;

    /**
     * For storing ProprietorshipCategory TypeID.
     */
    public final LR_TypeID ProprietorshipCategoryTypeID;

    /**
     * For storing Tenure TypeID.
     */
    public final LR_TypeID TenureTypeID;

    /**
     * For storing CountryIncorporated TypeID.
     */
    public final LR_TypeID CountryIncorporatedTypeID;

    public LR_Environment(Data_Environment de, LR_Files files) throws IOException {
        this.de = de;
        this.env = de.env;
        logID = env.initLog(LR_Strings.s_LandRegistry);
        this.files = files;
        PostcodeHandler = new Data_UKPostcodeHandler();
        NumeralsHashSet = Generic_String.getNumeralsHashSet();
        File f = this.files.getEnvDataFile();
        if (f.exists()) {
            String m = "load cache";
            logStartTag(m);
            LR_Environment cache = (LR_Environment) env.io.readObject(f);
            logEndTag(m);
            // Collections
            this.AddressIDToTitleNumberIDsLookup = cache.AddressIDToTitleNumberIDsLookup;
            this.IDs = cache.IDs;
            this.NonNullTypes = cache.NonNullTypes;
            this.NonNullPricePaidTypes = cache.NonNullPricePaidTypes;
            this.NullTitleNumberIDCollections = cache.NullTitleNumberIDCollections;
            this.NullTypes = cache.NullTypes;
            this.PricePaidLookup = cache.PricePaidLookup;
            this.TypeIDs = cache.TypeIDs;
            this.TypeToTypeID = cache.TypeToTypeID;
//            this.TypeIDToValuesLookups = cache.TypeIDToValuesLookups;
            this.ValueReverseLookups = cache.ValueReverseLookups;
            this.TitleNumberIDToAddressIDLookup = cache.TitleNumberIDToAddressIDLookup;
            this.Values = cache.Values;
            this.ValueIDs = cache.ValueIDs;
            // TypeIDs
            this.CompanyRegistrationNoTypeID = cache.CompanyRegistrationNoTypeID;
            this.CountryIncorporatedTypeID = cache.CountryIncorporatedTypeID;
            this.CountyTypeID = cache.CountyTypeID;
            this.DistrictTypeID = cache.DistrictTypeID;
            this.PostcodeDistrictTypeID = cache.PostcodeDistrictTypeID;
            this.PricePaidTypeID = cache.PricePaidTypeID;
            this.PropertyAddressTypeID = cache.PropertyAddressTypeID;
            this.ProprietorNameTypeID = cache.ProprietorNameTypeID;
            this.ProprietorshipCategoryTypeID = cache.ProprietorshipCategoryTypeID;
            this.RegionTypeID = cache.RegionTypeID;
            this.TenureTypeID = cache.TenureTypeID;
            this.TitleNumberTypeID = cache.TitleNumberTypeID;
            // Values
            this.MaxPricePaidClass = cache.MaxPricePaidClass;
            this.MinPricePaidClass = cache.MinPricePaidClass;
        } else {
            // Collections
            AddressIDToTitleNumberIDsLookup = new HashMap<>();
            NonNullPricePaidTypes = new HashSet<>();
            NonNullTypes = new HashSet();
            NullTypes = new HashSet();
            NullTitleNumberIDCollections = new HashMap<>();
            IDs = new HashSet<>();
            TitleNumberIDToAddressIDLookup = new HashMap<>();
            TypeToTypeID = new HashMap<>();
            TypeIDs = new HashSet<>();
//            TypeIDToValuesLookups = new HashMap<>();
            ValueReverseLookups = new HashMap<>();
            Values = new HashMap<>();
            ValueIDs = new HashMap<>();
            // TypeIDs
            CompanyRegistrationNoTypeID = getTypeID(LR_Strings.s_CompanyRegistrationNo);
            CountryIncorporatedTypeID = getTypeID(LR_Strings.s_CountryIncorporated);
            PostcodeDistrictTypeID = getTypeID(LR_Strings.s_PostcodeDistrict);
            DistrictTypeID = getTypeID(LR_Strings.s_District);
            CountyTypeID = getTypeID(LR_Strings.s_County);
            RegionTypeID = getTypeID(LR_Strings.s_Region);
            PricePaidTypeID = getTypeID(LR_Strings.s_PricePaid);
            PropertyAddressTypeID = getTypeID(LR_Strings.s_PropertyAddress);
            ProprietorNameTypeID = getTypeID(LR_Strings.s_ProprietorName);
            ProprietorshipCategoryTypeID = getTypeID(LR_Strings.s_ProprietorshipCategory);
            TenureTypeID = getTypeID(LR_Strings.s_Tenure);
            TitleNumberTypeID = getTypeID(LR_Strings.s_TitleNumber);
            initPricePaidLookup();
            initCollections();
        }
    }

    /**
     * For loading the PricePaidLookup from a particular file.
     */
    public final void initPricePaidLookup() {
        String m = "initPricePaidLookup";
        logStartTag(m);
        File f = files.getGeneratedDataFile(LR_Strings.s_PricePaidLookup,
                LR_Strings.s_HashMap);
        MinPricePaidClass = -10000000L;
        if (!f.exists()) {
            PricePaidLookup = new HashMap<>();
            long l;
            long u;
            l = MinPricePaidClass;
            u = 0L;
            addPricePaidInterval(l, u); // -10,000,000, 0
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
            for (int ll = 0; ll < 4; ll++) {
                l = u;
                u += 10000000L;
                addPricePaidInterval(l, u); // 20,000,000, 30,000,000, 40,000,000, 50,000,000
            }
            for (int ll = 0; ll < 3; ll++) {
                l = u;
                u += 50000000L;
                addPricePaidInterval(l, u); // 100,000,000, 150,000,000, 200,000,000
            }
            l = u;
            u = 5000000000L; //5,000,000,000
            addPricePaidInterval(l, u);
            l = u;
            u = 10000000000L; //10,000,000,000
            addPricePaidInterval(l, u);
            l = u;
            u = 1000000000000L; //1,000,000,000,000
            addPricePaidInterval(l, u);
            MaxPricePaidClass = u;
//            UpdatedPricePaidLookup = true;
        } else {
            log("Loading " + f);
            PricePaidLookup = (HashMap<LR_ValueID, Data_IntervalLong1>) env.io.readObject(f);
            MaxPricePaidClass = 1000000000000L;
//            UpdatedPricePaidLookup = false;
        }
                logEndTag(m);
    }

    private void addPricePaidInterval(long l, long u) {
        Data_IntervalLong1 i = new Data_IntervalLong1(l, u);
        int s  = PricePaidLookup.size();
        PricePaidLookup.put(new LR_ValueID(s, Integer.toString(s)), i);
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
        LR_TypeID id = getTypeID(type);
        NonNullTypes.add(id);
        ValueReverseLookups.put(id, new HashMap<>());
//        TypeIDToValuesLookups.put(id, new HashSet<>());
        addValueType(id);
    }

    protected void addNonNullPricePaidType(String type) {
        LR_TypeID id = getTypeID(type);
        NonNullPricePaidTypes.add(id);
    }

    protected LR_TypeID addNullType(String type) {
        LR_TypeID id = getTypeID(type);
        NullTypes.add(id);
        NullTitleNumberIDCollections.put(id, new HashMap<>());
        return id;
    }

    protected final LR_TypeID getTypeID(String type) {
        LR_TypeID id;
        if (TypeToTypeID.containsKey(type)) {
            id = TypeToTypeID.get(type);
        } else {
            id = new LR_TypeID(TypeToTypeID.size(), type);
            TypeToTypeID.put(type, id);
            TypeIDs.add(id);
        }
        return id;
    }

    /**
     * Initialises all the collections by either loading from file or creating
     * new empty one to be populated.
     */
    protected final void initCollections() {
        /**
         * Add NonNullTypes
         */
        addNonNullType(LR_Strings.s_TitleNumber);
        addNonNullType(LR_Strings.s_PropertyAddress);
        addNonNullType(LR_Strings.s_Tenure);
        addNonNullType(LR_Strings.s_PricePaid);
        addNonNullType(LR_Strings.s_CompanyRegistrationNo);
        addNonNullType(LR_Strings.s_ProprietorName);
        addNonNullType(LR_Strings.s_ProprietorshipCategory);
        //addNonNullType(LR_Strings.s_CountryIncorporated); // done in next line: addCountryIDToNonNull(LR_Strings.s_United_Kingdom);
        addCountryIDToNonNull(LR_Strings.s_United_Kingdom);
        addNonNullType(LR_Strings.s_PostcodeDistrict);
        addNonNullType(LR_Strings.s_District);
        addNonNullType(LR_Strings.s_County);
        addNonNullType(LR_Strings.s_Region);
        /**
         * Add NonNullPricePaidTypes
         */
        addNonNullPricePaidType(LR_Strings.s_Tenure);
        addNonNullPricePaidType(LR_Strings.s_CompanyRegistrationNo);
        addNonNullPricePaidType(LR_Strings.s_ProprietorshipCategory);
        addNonNullPricePaidType(LR_Strings.s_CountryIncorporated);
        addNonNullPricePaidType(LR_Strings.s_District);
        addNonNullPricePaidType(LR_Strings.s_County);
        addNonNullPricePaidType(LR_Strings.s_Region);
        /**
         * Add NullTypes
         */
        addNullType(LR_Strings.s_PropertyAddress);
        addNullType(LR_Strings.s_PricePaid);
        addNullType(LR_Strings.s_CompanyRegistrationNo);
        addNullType(LR_Strings.s_ProprietorName);
        addNullType(LR_Strings.s_ProprietorshipCategory);
        addNullType(LR_Strings.s_CountryIncorporated);
        addNullType(LR_Strings.s_PostcodeDistrict);
        addNullType(LR_Strings.s_District);
        addNullType(LR_Strings.s_County);
        addNullType(LR_Strings.s_Region);

        addValueType(TenureTypeID);
        addValue(TenureTypeID, LR_Strings.s_Freehold);
        addValue(TenureTypeID, LR_Strings.s_Leasehold);

        addValueType(ProprietorshipCategoryTypeID);
        addValueType(ProprietorNameTypeID);
        addValueType(CompanyRegistrationNoTypeID);
        addValueType(CountryIncorporatedTypeID);
        addValueType(PostcodeDistrictTypeID);
        addValueType(DistrictTypeID);
        addValueType(CountyTypeID);
        addValueType(RegionTypeID);
    }

    /**
     * Adds a value type map to ValueLookups and ValueReverseLookups
     *
     * @param id The LR_TypeID of the value type to be added.
     */
    protected void addValueType(LR_TypeID id) {
        Values.put(id, new HashSet<>());
        ValueIDs.put(id, new HashSet<>());
    }

    /**
     * Adds a value s to Values.get(valueType) and
     * ValueReverseLookups.get(valueType).This does not first test if s is
     * already a key in ValueLookups.get(valueType).
     *
     * @param id
     * @param s
     * @return
     */
    public LR_ValueID addValue(LR_TypeID id, String s) {
        LR_ValueID r;
        HashMap<String, LR_ValueID> vrl = ValueReverseLookups.get(id);
        if (vrl.containsKey(s)) {
            r = vrl.get(s);
        } else {
            int i = vrl.size();
            r = new LR_ValueID(i, s);
            vrl.put(s, r);
            HashSet<String> values = Values.get(id);
            if (values.add(s)) {
                ValueIDs.get(id).add(r);
            }
        }
        return r;
    }

    /**
     * For adding country to NonNull Collections.
     *
     * @param country
     */
    private void addCountryIDToNonNull(String country) {
        String type = LR_Strings.s_CountryIncorporated;
        addNonNullType(type);
        LR_TypeID tid = TypeToTypeID.get(type);
        HashMap<String, LR_ValueID> m;
        m = ValueReverseLookups.get(tid);
        if (!m.containsKey(type)) {
            LR_ValueID vid = new LR_ValueID(m.size(), country);
            m.put(country, vid);
//            TypeIDToValuesLookups.get(tid).add(vid);
            ValueIDs.get(tid).add(vid);
            Values.get(tid).add(country);
        }
    }

    /**
     * For convenience.
     * {@link Generic_Environment#logStartTag(java.lang.String, int)}
     *
     * @param s The tag name.
     */
    public final void logStartTag(String s) {
        env.logStartTag(s, logID);
    }

    /**
     * For convenience. {@link Generic_Environment#log(java.lang.String, int)}
     *
     * @param s The message to be logged.
     */
    public void log(String s) {
        env.log(s, logID);
    }

    /**
     * For convenience.
     * {@link Generic_Environment#logEndTag(java.lang.String, int)}
     *
     * @param s The tag name.
     */
    public final void logEndTag(String s) {
        env.logEndTag(s, logID);
    }

    @Override
    public boolean cacheDataAny() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean cacheDataAny(boolean hoome) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean checkAndMaybeFreeMemory() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
