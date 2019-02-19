package uk.ac.leeds.ccg.andyt.projects.landregistry.core;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import uk.ac.leeds.ccg.andyt.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.andyt.data.interval.Generic_Interval_long1;
import uk.ac.leeds.ccg.andyt.data.postcode.Generic_UKPostcode_Handler;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_IO;
import uk.ac.leeds.ccg.andyt.generic.lang.Generic_String;
import uk.ac.leeds.ccg.andyt.projects.landregistry.io.LR_Files;

/**
 *
 * @author geoagdt
 */
public class LR_Environment extends LR_OutOfMemoryErrorHandler
        implements Serializable {

    public transient Generic_Environment ge;

    public final transient LR_Strings strings;
    public final transient LR_Files files;
    public transient Generic_UKPostcode_Handler PostcodeHandler;
    
    public transient final HashSet<String> NumeralsHashSet;
        

    public transient static final String EOL = System.getProperty("line.separator");

    /**
     * A collection of all unique IDs ()current records.
     */
    public final HashSet<LR_ID2> IDs;

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
    public final HashMap<LR_TypeID, HashMap<LR_ID2, LR_ValueID>> NullTitleNumberIDCollections;

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
    public HashMap<LR_ValueID, Generic_Interval_long1> PricePaidLookup;

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

    public LR_Environment(Generic_Environment ge, LR_Files files) {
        this.ge = ge;
        this.files = files;
        this.strings = files.getStrings();
        PostcodeHandler = new Generic_UKPostcode_Handler();
        NumeralsHashSet = Generic_String.getNumeralsHashSet();
        File f;
        f = this.files.getEnvDataFile();
        if (f.exists()) {
            System.out.println("Loading cache...");
            LR_Environment cache;
            cache = (LR_Environment) Generic_IO.readObject(f);
            System.out.println("Loaded cache.");
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
            CompanyRegistrationNoTypeID = getTypeID(strings.S_CompanyRegistrationNo);
            CountryIncorporatedTypeID = getTypeID(strings.S_CountryIncorporated);
            PostcodeDistrictTypeID = getTypeID(strings.S_PostcodeDistrict);
            DistrictTypeID = getTypeID(strings.S_District);
            CountyTypeID = getTypeID(strings.S_County);
            RegionTypeID = getTypeID(strings.S_Region);
            PricePaidTypeID = getTypeID(strings.S_PricePaid);
            PropertyAddressTypeID = getTypeID(strings.S_PropertyAddress);
            ProprietorNameTypeID = getTypeID(strings.S_ProprietorName);
            ProprietorshipCategoryTypeID = getTypeID(strings.S_ProprietorshipCategory);
            TenureTypeID = getTypeID(strings.S_Tenure);
            TitleNumberTypeID = getTypeID(strings.S_TitleNumber);
            initPricePaidLookup();
            initCollections();
        }
    }

    /**
     * For loading the PricePaidLookup from a particular file.
     */
    public final void initPricePaidLookup() {
        File f;
        f = files.getGeneratedDataFile(strings.S_PricePaidLookup,
                strings.S_HashMap);
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
            System.out.println("Loading " + f);
            PricePaidLookup = (HashMap<LR_ValueID, Generic_Interval_long1>) Generic_IO.readObject(f);
            MaxPricePaidClass = 1000000000000L;
            System.out.println("Loaded " + f);
//            UpdatedPricePaidLookup = false;
        }
    }

    private void addPricePaidInterval(long l, long u) {
        Generic_Interval_long1 i;
        i = new Generic_Interval_long1(l, u);
        int s;
        s = PricePaidLookup.size();
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
        LR_TypeID typeID;
        typeID = getTypeID(type);
        NonNullTypes.add(typeID);
        ValueReverseLookups.put(typeID, new HashMap<>());
//        TypeIDToValuesLookups.put(typeID, new HashSet<>());
        addValueType(typeID);
    }

    protected void addNonNullPricePaidType(String type) {
        LR_TypeID typeID;
        typeID = getTypeID(type);
        NonNullPricePaidTypes.add(typeID);
    }

    protected LR_ID addNullType(String type) {
        LR_TypeID typeID;
        typeID = getTypeID(type);
        NullTypes.add(typeID);
        NullTitleNumberIDCollections.put(typeID, new HashMap<>());
        return typeID;
    }

    protected final LR_TypeID getTypeID(String type) {
        LR_TypeID typeID;
        if (TypeToTypeID.containsKey(type)) {
            typeID = TypeToTypeID.get(type);
        } else {
            typeID = new LR_TypeID(TypeToTypeID.size(), type);
            TypeToTypeID.put(type, typeID);
            TypeIDs.add(typeID);
        }
        return typeID;
    }

    /**
     * Initialises all the collections by either loading from file or creating
     * new empty one to be populated.
     */
    protected final void initCollections() {
        /**
         * Add NonNullTypes
         */
        addNonNullType(strings.S_TitleNumber);
        addNonNullType(strings.S_PropertyAddress);
        addNonNullType(strings.S_Tenure);
        addNonNullType(strings.S_PricePaid);
        addNonNullType(strings.S_CompanyRegistrationNo);
        addNonNullType(strings.S_ProprietorName);
        addNonNullType(strings.S_ProprietorshipCategory);
        //addNonNullType(strings.S_CountryIncorporated); // done in next line: addCountryIDToNonNull(strings.S_United_Kingdom);
        addCountryIDToNonNull(strings.S_United_Kingdom);
        addNonNullType(strings.S_PostcodeDistrict);
        addNonNullType(strings.S_District);
        addNonNullType(strings.S_County);
        addNonNullType(strings.S_Region);
        /**
         * Add NonNullPricePaidTypes
         */
        addNonNullPricePaidType(strings.S_Tenure);
        addNonNullPricePaidType(strings.S_CompanyRegistrationNo);
        addNonNullPricePaidType(strings.S_ProprietorshipCategory);
        addNonNullPricePaidType(strings.S_CountryIncorporated);
        addNonNullPricePaidType(strings.S_District);
        addNonNullPricePaidType(strings.S_County);
        addNonNullPricePaidType(strings.S_Region);
        /**
         * Add NullTypes
         */
        addNullType(strings.S_PropertyAddress);
        addNullType(strings.S_PricePaid);
        addNullType(strings.S_CompanyRegistrationNo);
        addNullType(strings.S_ProprietorName);
        addNullType(strings.S_ProprietorshipCategory);
        addNullType(strings.S_CountryIncorporated);
        addNullType(strings.S_PostcodeDistrict);
        addNullType(strings.S_District);
        addNullType(strings.S_County);
        addNullType(strings.S_Region);

        addValueType(TenureTypeID);
        addValue(TenureTypeID, strings.S_Freehold);
        addValue(TenureTypeID, strings.S_Leasehold);

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
     * @param typeID The LR_ID of the value type to be added.
     */
    protected void addValueType(LR_TypeID typeID) {
        Values.put(typeID, new HashSet<>());
        ValueIDs.put(typeID, new HashSet<>());
    }

    /**
     * Adds a value s to Values.get(valueType) and
     * ValueReverseLookups.get(valueType).This does not first test if s is
     * already a key in ValueLookups.get(valueType).
     *
     * @param typeID
     * @param s
     * @return
     */
    public LR_ValueID addValue(LR_TypeID typeID, String s) {
        LR_ValueID result;
        HashMap<String, LR_ValueID> valueReverseLookup;
        valueReverseLookup = ValueReverseLookups.get(typeID);
        if (valueReverseLookup.containsKey(s)) {
            result = valueReverseLookup.get(s);
        } else {
            int i = valueReverseLookup.size();
            result = new LR_ValueID(i, s);
            valueReverseLookup.put(s, result);
            HashSet<String> values;
            values = Values.get(typeID);
            if (values.add(s)) {
                ValueIDs.get(typeID).add(result);
            }
        }
        return result;
    }

    /**
     * For adding country to NonNull Collections.
     *
     * @param country
     */
    private void addCountryIDToNonNull(String country) {
        String type;
        type = strings.S_CountryIncorporated;
        addNonNullType(type);
        LR_TypeID typeID;
        typeID = TypeToTypeID.get(type);
        HashMap<String, LR_ValueID> m;
        m = ValueReverseLookups.get(typeID);
        if (!m.containsKey(type)) {
            LR_ValueID valueID;
            valueID = new LR_ValueID(m.size(), country);
            m.put(country, valueID);
//            TypeIDToValuesLookups.get(typeID).add(valueID);
            ValueIDs.get(typeID).add(valueID);
            Values.get(typeID).add(country);
        }
    }

}
