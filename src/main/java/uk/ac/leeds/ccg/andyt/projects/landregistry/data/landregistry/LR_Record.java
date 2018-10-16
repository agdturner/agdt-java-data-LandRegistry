
/*
 * Copyright 2018 geoagdt.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.leeds.ccg.andyt.projects.landregistry.data.landregistry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import uk.ac.leeds.ccg.andyt.generic.data.Generic_Interval_long1;
import uk.ac.leeds.ccg.andyt.generic.lang.Generic_StaticString;
import uk.ac.leeds.ccg.andyt.generic.utilities.time.Generic_YearMonth;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Environment;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_ID;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_ID2;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Object;

/**
 *
 * @author geoagdt
 */
public abstract class LR_Record extends LR_Object {

    protected Generic_YearMonth YM;
    protected LR_ID2 ID;
    protected LR_ID TitleNumberID;
    private String TitleNumber;
    private String Tenure;
    private String PropertyAddress;
    private String District;
    private String Region;
    private String Postcode;
    private String PostcodeDistrict; // Set from Postcode
    private String MultipleAddressIndicator;
    private long PricePaid;
    private LR_ID PricePaidClass; // Set from PricePaid using Env.PricePaidLookup
    private String CompanyRegistrationNo1;
    private String County;
    private String ProprietorName1;
    private String ProprietorshipCategory1;
    private String AdditionalProprietorIndicator;
    private String CompanyRegistrationNo2;
    private String CompanyRegistrationNo3;
    private String CompanyRegistrationNo4;
    private String DateProprietorAdded;
    private String Proprietor1Address1;
    private String Proprietor1Address2;
    private String Proprietor1Address3;
    private String Proprietor2Address1;
    private String Proprietor2Address2;
    private String Proprietor2Address3;
    private String Proprietor3Address1;
    private String Proprietor3Address2;
    private String Proprietor3Address3;
    private String Proprietor4Address1;
    private String Proprietor4Address2;
    private String Proprietor4Address3;
    private String ProprietorName2;
    private String ProprietorName3;
    private String ProprietorName4;
    private String ProprietorshipCategory2;
    private String ProprietorshipCategory3;
    private String ProprietorshipCategory4;

    protected LR_Record() {
    }

    /**
     * Creates a simple copy of r without changing any collections.
     *
     * @param r
     */
    public LR_Record(LR_Record r) {
        Env = r.Env;
        YM = r.YM;
        ID = r.ID;
        setTitleNumber(r.getTitleNumber());
        setTenure(r.getTenure());
        setPropertyAddress(r.getPropertyAddress());
        setDistrict(r.getDistrict());
        setCounty(r.getCounty());
        setRegion(r.getRegion());
        setPostcode(r.getPostcode());
        setPostcodeDistrict(r.getPostcodeDistrict());
        setMultipleAddressIndicator(r.getMultipleAddressIndicator());
        setPricePaid(r.getPricePaid());
        setProprietorName1(r.getProprietorName1());
        setCompanyRegistrationNo1(r.getCompanyRegistrationNo1());
        setProprietorshipCategory1(r.getProprietorshipCategory1());
        setProprietor1Address1(r.getProprietor1Address1());
        setProprietor1Address2(r.getProprietor1Address2());
        setProprietor1Address3(r.getProprietor1Address3());
        setProprietorName2(r.getProprietorName2());
        setCompanyRegistrationNo2(r.getCompanyRegistrationNo2());
        setProprietorshipCategory2(r.getProprietorshipCategory2());
        setProprietor2Address1(r.getProprietor2Address1());
        setProprietor2Address2(r.getProprietor2Address2());
        setProprietor2Address3(r.getProprietor2Address3());
        setProprietorName3(r.getProprietorName3());
        setCompanyRegistrationNo3(r.getCompanyRegistrationNo3());
        setProprietorshipCategory3(r.getProprietorshipCategory3());
        setProprietor3Address1(r.getProprietor3Address1());
        setProprietor3Address2(r.getProprietor3Address2());
        setProprietor3Address3(r.getProprietor3Address3());
        setProprietorName4(r.getProprietorName4());
        setCompanyRegistrationNo4(r.getCompanyRegistrationNo4());
        setProprietorshipCategory4(r.getProprietorshipCategory4());
        setProprietor4Address1(r.getProprietor4Address1());
        setProprietor4Address2(r.getProprietor4Address2());
        setProprietor4Address3(r.getProprietor4Address3());
        setDateProprietorAdded(r.getDateProprietorAdded());
        setAdditionalProprietorIndicator(r.getAdditionalProprietorIndicator());
    }

    public static LR_Record create(boolean isCCOD, boolean doFull,
            LR_Environment env, Generic_YearMonth YM, String line,
            boolean updateIDs) throws Exception {
        if (Generic_StaticString.getCount(line, ",") > 10) {
            if (isCCOD) {
                if (doFull) {
                    return new LR_CC_FULL_Record(env, YM, line, updateIDs);
                } else {
                    return new LR_CC_COU_Record(env, YM, line, updateIDs);
                }
            } else {
                if (doFull) {
                    return new LR_OC_FULL_Record(env, YM, line, updateIDs);
                } else {
                    return new LR_OC_COU_Record(env, YM, line, updateIDs);
                }
            }
        }
        return null;
    }

    public final String[] getSplitAndTrim(String line) {
        String[] ls;
        ls = line.split("\",\"");
        for (int i = 0; i < ls.length; i++) {
            ls[i] = ls[i].trim();
        }
        return ls;
    }

    public static String header(boolean isCCOD, boolean doFull) {
        if (isCCOD) {
            if (doFull) {
                return LR_CC_FULL_Record.header();
            } else {
                return LR_CC_COU_Record.header();
            }
        } else {
            if (doFull) {
                return LR_OC_FULL_Record.header();
            } else {
                return LR_OC_COU_Record.header();
            }
        }
    }

    public abstract String toCSV();

    /**
     * @return the ID
     */
    public final LR_ID2 getID() {
        return ID;
    }

    /**
     *
     * @param s The key to set.
     * @param typeID The typeID of collection to set in.
     * @return The LR_ID of s for sType.
     */
    protected LR_ID updateNonNullCollections(String s, LR_ID typeID) {
        LR_ID result;
        HashMap<String, LR_ID> ToID;
        HashMap<LR_ID, String> IDTo;
        ToID = Env.ToIDLookups.get(typeID);
        if (ToID == null) {
            int debug = 1;
            result = null;
        } else {
            if (ToID.containsKey(s)) {
                result = ToID.get(s);
            } else {
                result = new LR_ID(ToID.size());
                ToID.put(s, result);
                IDTo = Env.IDToLookups.get(typeID);
                IDTo.put(result, s);
                Env.UpdatedNonNullTypes.put(typeID, true);
            }
        }
        return result;
    }

    /**
     * @param s TitleNumber
     * @param updateIDs IFF true then collections are updated otherwise ID is
     * set from data pulled from existing collections.
     * @throws Exception
     */
    public final void initTitleNumber(String s, boolean updateIDs) throws Exception {
        if (s.isEmpty()) {
            throw new Exception("TitleNumber is empty");
        }
        setTitleNumber(s);
        if (updateIDs) {
            TitleNumberID = updateNonNullCollections(s, Env.TitleNumberTypeID);
        } else {
            TitleNumberID = Env.ToIDLookups.get(Env.TitleNumberTypeID).get(s);
        }
    }

    /**
     *
     * @param s Tenure
     * @param updateIDs IFF true then collections are updated otherwise ID is
     * set from data pulled from existing collections.
     * @throws Exception
     */
    public final void initTenure(String s, boolean updateIDs) throws Exception {
        if (!(s.equalsIgnoreCase(Env.Strings.S_Leasehold)
                || s.equalsIgnoreCase(Env.Strings.S_Freehold))) {
            throw new Exception("Unexpected Tenure: \"" + s + "\"");
        }
        setTenure(s);
        if (updateIDs) {
            updateNonNullCollections(s, Env.TenureTypeID);
        }
    }

    /**
     * If s is blank then PropertyAddress is set to a unique number and
     * TitleNumberID is added to Env.TitleNumberIDsOfNullPropertyAddress.
     *
     * @param s PropertyAddress
     * @param updateIDs IFF true then collections are updated otherwise ID is
     * set from data pulled from existing collections.
     */
    public final void initPropertyAddressAndID(String s, boolean updateIDs) {
        LR_ID typeID;
        typeID = Env.PropertyAddressTypeID;
        if (s.isEmpty()) {
            if (updateIDs) {
                setPropertyAddress(updateNullCollection(typeID));
            } else {
                LR_ID paID;
                paID = Env.TitleNumberIDToAddressIDLookup.get(TitleNumberID);
                String pa;
                pa = Env.IDToLookups.get(typeID).get(paID);
                setPropertyAddress(pa);
            }
        } else {
            setPropertyAddress(s);
        }
        String pa;
        pa = getPropertyAddress();
        if (updateIDs) {
            LR_ID id;
            id = updateNonNullCollections(pa, typeID);
            // init ID
            ID = new LR_ID2(TitleNumberID, id);
            if (!Env.IDs.contains(ID)) {
                Env.IDs.add(ID);
                Env.UpdatedIDs = true;
            }
            HashSet<LR_ID> titleNumberIDs;
            if (Env.AddressIDToTitleNumberIDsLookup.containsKey(id)) {
                titleNumberIDs = Env.AddressIDToTitleNumberIDsLookup.get(id);
            } else {
                titleNumberIDs = new HashSet<>();
                Env.AddressIDToTitleNumberIDsLookup.put(id, titleNumberIDs);
            }
            if (!titleNumberIDs.contains(TitleNumberID)) {
                titleNumberIDs.add(TitleNumberID);
                Env.UpdatedAddressIDToTitleNumberIDsLookup = true;
            }
            LR_ID change;
            change = Env.TitleNumberIDToAddressIDLookup.put(TitleNumberID, id);
            if (change != null) {
                if (!change.equals(id)) {
                    String previousAddress;
                    previousAddress = Env.IDToLookups.get(typeID).get(change);
                    System.out.println("Address for TitleNumer " + TitleNumber
                            + " changed from \"" + previousAddress + "\" to \""
                            + pa + "\"");
                    Env.UpdatedTitleNumberIDToAddressIDLookup = true;
                }
            }
        } else {
            LR_ID addressID;
            addressID = Env.TitleNumberIDToAddressIDLookup.get(TitleNumberID);
            ID = new LR_ID2(TitleNumberID, addressID);
        }
    }

    /**
     * If updateIDs then if s is blank then PricePaid is set to a unique
     * negative number and a record is kept to look up this number from ID. If !updateIDs
     * then the unique number set previously is obtained from what is stored.
     *
     * @param s PricePaid
     * @param updateIDs IFF true then collections are updated otherwise ID is
     * set from data pulled from existing collections.
     */
    public final void initPricePaid(String s, boolean updateIDs) {
        if (s.isEmpty()) {
            if (updateIDs) {
                setPricePaid(updateNullPricePaid());
            } else {
                HashMap<LR_ID2, LR_ID> m;
                m = Env.NullCollections.get(Env.PricePaidTypeID);
                setPricePaid(Long.valueOf(Env.IDToType.get(m.get(ID))));
            }
        } else {
            try {
                long l;
                l = Long.valueOf(s);
                setPricePaid(l);
                setPricePaidClass(l);
            } catch (NumberFormatException e) {
                System.err.println("PricePaid is: \"" + s + "\" which is not "
                        + "recognised as a long.");
                setPricePaid(updateNullPricePaid());
            }
        }
    }

    public void setPricePaidClass(long l) {
        Iterator<LR_ID> ite;
        ite = Env.PricePaidLookup.keySet().iterator();
        LR_ID k;
        Generic_Interval_long1 i;
        while (ite.hasNext()) {
            k = ite.next();
            i = Env.PricePaidLookup.get(k);
            if (i.isInInterval(l)) {
                PricePaidClass = k;
            }
        }
//        if (PricePaidClass == null) {
//            k = new LR_ID(Env.PricePaidLookup.size());
//        }
    }

    /**
     * The LR_IDs effectively start at -10000.
     * @return 
     */
    public final long updateNullPricePaid() {
        LR_ID typeID;
        typeID = Env.PricePaidTypeID;
        return Long.valueOf("-" + (updateNullCollection(typeID) + 10000));
    }

    /**
     *
     * @param typeID
     * @return
     */
    public String updateNullCollection(LR_ID typeID) {
        String result;
        HashMap<LR_ID2, LR_ID> m;
        m = Env.NullCollections.get(typeID);
        if (m == null) {
            m = new HashMap<LR_ID2, LR_ID>();
            Env.NullCollections.put(typeID, m);
        }
        int i;
        i = m.size();
        result = Integer.toString(i);
        m.put(ID, new LR_ID(i));
        Env.UpdatedNullTypes.put(typeID, true);
        return result;
    }

    /**
     * If updateIDs then if s is blank then ProprietorName1 is set to a unique
     * number and a record is kept to look up this number from ID. If !updateIDs
     * then the unique number set previously is obtained from what is stored.
     *
     * @param s ProprietorName1
     * @param updateIDs IFF true then collections are updated otherwise ID is
     * set from data pulled from existing collections.
     */
    public final void initProprietorName1(String s, boolean updateIDs) {
        LR_ID typeID;
        typeID = Env.ProprietorNameTypeID;
        if (s.isEmpty()) {
            if (updateIDs) {
                setProprietorName1(updateNullCollection(typeID));
            } else {
                HashMap<LR_ID2, LR_ID> m;
                m = Env.NullCollections.get(typeID);
                setProprietorName1(Env.IDToType.get(m.get(ID)));
            }
        } else {
            setProprietorName1(s);
        }
        if (updateIDs) {
            updateNonNullCollections(getProprietorName1(), typeID);
        }
    }

    /**
     * If updateIDs then if s is blank then CompanyRegistrationNo1 is set to a unique
     * number and a record is kept to look up this number from ID. If !updateIDs
     * then the unique number set previously is obtained from what is stored.
     *
     * @param s CompanyRegistrationNo1
     * @param updateIDs IFF true then collections are updated otherwise ID is
     * set from data pulled from existing collections.
     */
    public final void initCompanyRegistrationNo1(String s, boolean updateIDs) {
        LR_ID typeID;
        typeID = Env.CompanyRegistrationNoTypeID;
        if (s.isEmpty()) {
            if (updateIDs) {
                setCompanyRegistrationNo1(updateNullCollection(typeID));
            } else {
                HashMap<LR_ID2, LR_ID> m;
                m = Env.NullCollections.get(typeID);
                setCompanyRegistrationNo1(Env.IDToType.get(m.get(ID)));
            }
        } else {
            setCompanyRegistrationNo1(s);
        }
        if (updateIDs) {
            updateNonNullCollections(getCompanyRegistrationNo1(), typeID);
        }
    }

    /**
     * If updateIDs then if s is blank then ProprietorshipCategory1 is set to a unique
     * number and a record is kept to look up this number from ID. If !updateIDs
     * then the unique number set previously is obtained from what is stored.
     *
     * @param s ProprietorshipCategory1
     * @param updateIDs IFF true then collections are updated otherwise ID is
     * set from data pulled from existing collections.
     */
    public final void initProprietorshipCategory1(String s, boolean updateIDs) {
        LR_ID typeID;
        typeID = Env.ProprietorshipCategoryTypeID;
        if (s.isEmpty()) {
            if (updateIDs) {
                setProprietorshipCategory1(updateNullCollection(typeID));
            } else {
                HashMap<LR_ID2, LR_ID> m;
                m = Env.NullCollections.get(typeID);
                setProprietorshipCategory1(Env.IDToType.get(m.get(ID)));
            }
        } else {
            setProprietorshipCategory1(s);
        }
        if (updateIDs) {
            updateNonNullCollections(getProprietorshipCategory1(), typeID);
        }
    }

    /**
     * @return the TitleNumberID
     */
    public final LR_ID getTitleNumberID() {
        return TitleNumberID;
    }

    /**
     * @return the PropertyAddressID
     */
    public final LR_ID getPropertyAddressID() {
        return ID.getPropertyAddressID();
    }

    /**
     * @return the ProprietorName1ID
     */
    public final LR_ID getProprietorName1ID() {
        return Env.ToIDLookups.get(Env.ProprietorNameTypeID).get(getProprietorName1());
    }

    /**
     * @return the ProprietorName2ID
     */
    public final LR_ID getProprietorName2ID() {
        return Env.ToIDLookups.get(Env.ProprietorNameTypeID).get(getProprietorName2());
    }

    /**
     * @return the ProprietorName3ID
     */
    public final LR_ID getProprietorName3ID() {
       return Env.ToIDLookups.get(Env.ProprietorNameTypeID).get(getProprietorName3());
    }

    /**
     * @return the ProprietorName4ID
     */
    public final LR_ID getProprietorName4ID() {
        return Env.ToIDLookups.get(Env.ProprietorNameTypeID).get(getProprietorName4());
    }

    /**
     * @return the CompanyRegistrationNo1ID
     */
    public final LR_ID getCompanyRegistrationNo1ID() {
        return Env.ToIDLookups.get(Env.CompanyRegistrationNoTypeID).get(getCompanyRegistrationNo1());
    }

    /**
     * @return the CompanyRegistrationNo2ID
     */
    public final LR_ID getCompanyRegistrationNo2ID() {
        return Env.ToIDLookups.get(Env.CompanyRegistrationNoTypeID).get(getCompanyRegistrationNo2());
    }

    /**
     * @return the CompanyRegistrationNo3ID
     */
    public final LR_ID getCompanyRegistrationNo3ID() {
        return Env.ToIDLookups.get(Env.CompanyRegistrationNoTypeID).get(getCompanyRegistrationNo3());
    }

    /**
     * @return the CompanyRegistrationNo4ID
     */
    public final LR_ID getCompanyRegistrationNo4ID() {
        return Env.ToIDLookups.get(Env.CompanyRegistrationNoTypeID).get(getCompanyRegistrationNo4());
    }

    /**
     * @return the TitleNumber
     */
    public final String getTitleNumber() {
        return TitleNumber;
    }

    /**
     * @return the Tenure
     */
    public final String getTenure() {
        return Tenure;
    }

    /**
     * @return the TenureID
     */
    public final LR_ID getTenureID() {
        return Env.ToIDLookups.get(Env.TenureTypeID).get(getTenure());
    }

    /**
     * @return the District
     */
    public final String getDistrict() {
        return District;
    }

    /**
     * @return the Region
     */
    public final String getRegion() {
        return Region;
    }

    /**
     * @return the Postcode
     */
    public final String getPostcode() {
        return Postcode;
    }

    /**
     * @return the PostcodeDistrict
     */
    public final String getPostcodeDistrict() {
        return PostcodeDistrict;
    }

    /**
     * @return the PostcodeDistrictID
     */
    public final LR_ID getPostcodeDistrictID() {
        return Env.ToIDLookups.get(Env.PostcodeDistrictTypeID).get(getPostcodeDistrict());
    }

    /**
     * @return the PricePaid
     */
    public final long getPricePaid() {
        return PricePaid;
    }

    /**
     * @return the PricePaidClass
     */
    public final LR_ID getPricePaidClass() {
        return PricePaidClass;
    }

    /**
     * @return the CompanyRegistrationNo1
     */
    public final String getCompanyRegistrationNo1() {
        return CompanyRegistrationNo1;
    }

    /**
     * @return the County
     */
    public final String getCounty() {
        return County;
    }

    /**
     * @return the ProprietorName1
     */
    public final String getProprietorName1() {
        return ProprietorName1;
    }

    /**
     * @return the ProprietorshipCategory1
     */
    public final String getProprietorshipCategory1() {
        return ProprietorshipCategory1;
    }

    /**
     * @return the ProprietorshipCategory1ID
     */
    public final LR_ID getProprietorshipCategory1ID() {
        return Env.ToIDLookups.get(Env.ProprietorshipCategoryTypeID).get(getProprietorshipCategory1());
    }

    /**
     * @return the AdditionalProprietorIndicator
     */
    public final String getAdditionalProprietorIndicator() {
        return AdditionalProprietorIndicator;
    }

    /**
     * @return the CompanyRegistrationNo2
     */
    public final String getCompanyRegistrationNo2() {
        return CompanyRegistrationNo2;
    }

    /**
     * @return the CompanyRegistrationNo3
     */
    public final String getCompanyRegistrationNo3() {
        return CompanyRegistrationNo3;
    }

    /**
     * @return the CompanyRegistrationNo4
     */
    public final String getCompanyRegistrationNo4() {
        return CompanyRegistrationNo4;
    }

    /**
     * @return the DateProprietorAdded
     */
    public final String getDateProprietorAdded() {
        return DateProprietorAdded;
    }

    /**
     * @return the MultipleAddressIndicator
     */
    public final String getMultipleAddressIndicator() {
        return MultipleAddressIndicator;
    }

    /**
     * @return the PropertyAddress
     */
    public final String getPropertyAddress() {
        return PropertyAddress;
    }

    /**
     * @return the Proprietor1Address1
     */
    public final String getProprietor1Address1() {
        return Proprietor1Address1;
    }

    /**
     * @return the Proprietor1Address2
     */
    public final String getProprietor1Address2() {
        return Proprietor1Address2;
    }

    /**
     * @return the Proprietor1Address3
     */
    public final String getProprietor1Address3() {
        return Proprietor1Address3;
    }

    /**
     * @return the Proprietor2Address1
     */
    public final String getProprietor2Address1() {
        return Proprietor2Address1;
    }

    /**
     * @return the Proprietor2Address2
     */
    public final String getProprietor2Address2() {
        return Proprietor2Address2;
    }

    /**
     * @return the Proprietor2Address3
     */
    public final String getProprietor2Address3() {
        return Proprietor2Address3;
    }

    /**
     * @return the Proprietor3Address1
     */
    public final String getProprietor3Address1() {
        return Proprietor3Address1;
    }

    /**
     * @return the Proprietor3Address2
     */
    public final String getProprietor3Address2() {
        return Proprietor3Address2;
    }

    /**
     * @return the Proprietor3Address3
     */
    public final String getProprietor3Address3() {
        return Proprietor3Address3;
    }

    /**
     * @return the Proprietor4Address1
     */
    public final String getProprietor4Address1() {
        return Proprietor4Address1;
    }

    /**
     * @return the Proprietor4Address2
     */
    public final String getProprietor4Address2() {
        return Proprietor4Address2;
    }

    /**
     * @return the Proprietor4Address3
     */
    public final String getProprietor4Address3() {
        return Proprietor4Address3;
    }

    /**
     * @return the ProprietorName2
     */
    public final String getProprietorName2() {
        return ProprietorName2;
    }

    /**
     * @return the ProprietorName3
     */
    public final String getProprietorName3() {
        return ProprietorName3;
    }

    /**
     * @return the ProprietorName4
     */
    public final String getProprietorName4() {
        return ProprietorName4;
    }

    /**
     * @return the ProprietorshipCategory2
     */
    public final String getProprietorshipCategory2() {
        return ProprietorshipCategory2;
    }

    /**
     * @return the ProprietorshipCategory2ID
     */
    public final LR_ID getProprietorshipCategory2ID() {
        return Env.ToIDLookups.get(Env.ProprietorshipCategoryTypeID).get(getProprietorshipCategory2());
    }

    /**
     * @return the ProprietorshipCategory3
     */
    public final String getProprietorshipCategory3() {
        return ProprietorshipCategory3;
    }

    /**
     * @return the ProprietorshipCategory3ID
     */
    public final LR_ID getProprietorshipCategory3ID() {
        return Env.ToIDLookups.get(Env.ProprietorshipCategoryTypeID).get(getProprietorshipCategory3());
    }

    /**
     * @return the ProprietorshipCategory4
     */
    public final String getProprietorshipCategory4() {
        return ProprietorshipCategory4;
    }

    /**
     * @return the ProprietorshipCategory4ID
     */
    public final LR_ID getProprietorshipCategory4ID() {
        return Env.ToIDLookups.get(Env.ProprietorshipCategoryTypeID).get(getProprietorshipCategory4());
    }

    /**
     * @return the CountryIncorporated1
     */
    public String getCountryIncorporated1() {
        return Env.Strings.S_United_Kingdom;
    }

    /**
     * @return the CountryIncorporated1ID
     */
    public LR_ID getCountryIncorporated1ID() {
        return Env.ToIDLookups.get(Env.CountryIncorporatedTypeID).get(getCountryIncorporated1());
    }

    /**
     * @param TitleNumber the TitleNumber to set
     */
    public final void setTitleNumber(String TitleNumber) {
        this.TitleNumber = TitleNumber;
    }

    /**
     * @param Tenure the Tenure to set
     */
    public final void setTenure(String Tenure) {
        this.Tenure = Tenure;
    }

    /**
     * @param s the PropertyAddress to set
     */
    public final void setPropertyAddress(String s) {
        this.PropertyAddress = s;
    }

    /**
     * @param District the District to set
     */
    public final void setDistrict(String District) {
        this.District = District;
    }

    /**
     * @param Region the Region to set
     */
    public final void setRegion(String Region) {
        this.Region = Region;
    }

    /**
     * If updateIDs then if s is blank then Postcode and PostcodeDistrict are set to a unique
     * number and a record is kept to look up this number from ID. If !updateIDs
     * then the unique number set previously is obtained from what is stored.
     *
     * @param s Postcode
     * @param updateIDs IFF true then collections are updated otherwise ID is
     * set from data pulled from existing collections.
     */
    public final void initPostcodeAndPostcodeDistrict(String s, boolean updateIDs) {
        setPostcode(s);
        LR_ID typeID;
        typeID = Env.PostcodeDistrictTypeID;
        if (Env.PostcodeHandler.isValidPostcodeForm(s)) {
            String[] split;
            split = this.Postcode.split(" ");
            // PostcodeDistrict
            String s0;
            s0 = split[0];
            setPostcodeDistrict(s0);
            if (updateIDs) {
                updateNonNullCollections(s0, typeID);
            }
        } else {
            if (updateIDs) {
                setPostcodeDistrict(updateNullCollection(typeID));
            } else {
                HashMap<LR_ID2, LR_ID> m;
                m = Env.NullCollections.get(typeID);
                setPostcodeDistrict(Env.IDToType.get(m.get(ID)));
            }
        }
    }

    /**
     * @param s the Postcode set
     */
    public final void setPostcode(String s) {
        this.Postcode = s;
    }

    /**
     * @param s the PostcodeDistrict to set
     */
    public final void setPostcodeDistrict(String s) {
        this.PostcodeDistrict = s;
    }

    /**
     * @param s the MultipleAddressIndicator to set
     */
    public final void setMultipleAddressIndicator(String s) {
        this.MultipleAddressIndicator = s;
    }

    /**
     * @param PricePaid the PricePaid to set
     */
    public final void setPricePaid(long PricePaid) {
        this.PricePaid = PricePaid;
    }

    /**
     * All leading "0" in s are removed.
     *
     * @param s the CompanyRegistrationNo1 to set
     */
    public final void setCompanyRegistrationNo1(String s) {
        s = getStringWithoutLeadingZeroes(s);
        this.CompanyRegistrationNo1 = s;
    }

    public String getStringWithoutLeadingZeroes(String s) {
        if (s.length() > 1) {
            while (s.startsWith("0")) {
                s = s.substring(1);
            }
        }
        return s;
    }

    /**
     * @param County the County to set
     */
    public final void setCounty(String County) {
        this.County = County;
    }

    /**
     * @param s the ProprietorName1 to set
     */
    public final void setProprietorName1(String s) {
        this.ProprietorName1 = s;
    }

    /**
     * @param s the ProprietorshipCategory1 to set
     */
    public final void setProprietorshipCategory1(String s) {
        this.ProprietorshipCategory1 = s;
        updateProprietorshipCategory(s);
    }
    
    /**
     * @param s the ProprietorshipCategory to set
     */
    protected final void updateProprietorshipCategory(String s){
        if (!s.isEmpty()) {
            if (Env.ProprietorshipCategoryValues.add(s)) {
                Env.UpdatedProprietorshipCategoryValues = true;
            }
            updateNonNullCollections(s, Env.ProprietorshipCategoryTypeID);
        }
    }

    /**
     * @param s the AdditionalProprietorIndicator to set
     */
    public final void setAdditionalProprietorIndicator(String s) {
        this.AdditionalProprietorIndicator = s;
    }

    /**
     * @param s the CompanyRegistrationNo2 to set
     */
    public final void setCompanyRegistrationNo2(String s) {
        s = getStringWithoutLeadingZeroes(s);
        this.CompanyRegistrationNo2 = s;
        if (!s.isEmpty()) {
            updateNonNullCollections(s, Env.CompanyRegistrationNoTypeID);
        }
    }

    /**
     * @param s the CompanyRegistrationNo3 to set
     */
    public final void setCompanyRegistrationNo3(String s) {
        s = getStringWithoutLeadingZeroes(s);
        this.CompanyRegistrationNo3 = s;
        if (!s.isEmpty()) {
            updateNonNullCollections(s, Env.CompanyRegistrationNoTypeID);
        }
    }

    /**
     * @param s the CompanyRegistrationNo4 to set
     */
    public final void setCompanyRegistrationNo4(String s) {
        s = getStringWithoutLeadingZeroes(s);
        this.CompanyRegistrationNo4 = s;
        if (!s.isEmpty()) {
            updateNonNullCollections(s, Env.CompanyRegistrationNoTypeID);
        }
    }

    /**
     * @param s the DateProprietorAdded to set
     */
    public final void setDateProprietorAdded(String s) {
        this.DateProprietorAdded = s;
    }

    /**
     * @param s the Proprietor1Address1 to set
     */
    public final void setProprietor1Address1(String s) {
        this.Proprietor1Address1 = s;
    }

    /**
     * @param s the Proprietor1Address2 to set
     */
    public final void setProprietor1Address2(String s) {
        this.Proprietor1Address2 = s;
    }

    /**
     * @param s the Proprietor1Address3 to set
     */
    public final void setProprietor1Address3(String s) {
        this.Proprietor1Address3 = s;
    }

    /**
     * @param s the Proprietor2Address1 to set
     */
    public final void setProprietor2Address1(String s) {
        this.Proprietor2Address1 = s;
    }

    /**
     * @param s the Proprietor2Address2 to set
     */
    public final void setProprietor2Address2(String s) {
        this.Proprietor2Address2 = s;
    }

    /**
     * @param s the Proprietor2Address3 to set
     */
    public final void setProprietor2Address3(String s) {
        this.Proprietor2Address3 = s;
    }

    /**
     * @param s the Proprietor3Address1 to set
     */
    public final void setProprietor3Address1(String s) {
        this.Proprietor3Address1 = s;
    }

    /**
     * @param s the Proprietor3Address2 to set
     */
    public final void setProprietor3Address2(String s) {
        this.Proprietor3Address2 = s;
    }

    /**
     * @param s the Proprietor3Address3 to set
     */
    public final void setProprietor3Address3(String s) {
        this.Proprietor3Address3 = s;
    }

    /**
     * @param s the Proprietor4Address1 to set
     */
    public final void setProprietor4Address1(String s) {
        this.Proprietor4Address1 = s;
    }

    /**
     * @param s the Proprietor4Address2 to set
     */
    public final void setProprietor4Address2(String s) {
        this.Proprietor4Address2 = s;
    }

    /**
     * @param s the Proprietor4Address3 to set
     */
    public final void setProprietor4Address3(String s) {
        this.Proprietor4Address3 = s;
    }

    /**
     * @param s the ProprietorName2 to set
     */
    public final void setProprietorName2(String s) {
        this.ProprietorName2 = s;
        if (!s.isEmpty()) {
            updateNonNullCollections(s, Env.ProprietorNameTypeID);
        }
    }

    /**
     * @param s the ProprietorName3 to set
     */
    public final void setProprietorName3(String s) {
        this.ProprietorName3 = s;
        if (!s.isEmpty()) {
            updateNonNullCollections(s, Env.ProprietorNameTypeID);
        }
    }

    /**
     * @param s the ProprietorName4 to set
     */
    public final void setProprietorName4(String s) {
        this.ProprietorName4 = s;
        if (!s.isEmpty()) {
            updateNonNullCollections(s, Env.ProprietorNameTypeID);
        }
    }

    /**
     * @param s the ProprietorshipCategory2 to set
     */
    public final void setProprietorshipCategory2(String s) {
        this.ProprietorshipCategory2 = s;
        updateProprietorshipCategory(s);
    }

    /**
     * @param s the ProprietorshipCategory3 to set
     */
    public final void setProprietorshipCategory3(String s) {
        this.ProprietorshipCategory3 = s;
        updateProprietorshipCategory(s);
    }

    /**
     * @param s the ProprietorshipCategory4 to set
     */
    public final void setProprietorshipCategory4(String s) {
        this.ProprietorshipCategory4 = s;
        updateProprietorshipCategory(s);
    }
}
