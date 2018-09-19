
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
//    //protected HashMap<LR_ID, LR_ID> TypeIDs;
    protected LR_ID TitleNumberID;
//    protected LR_ID TenureID;
//    protected LR_ID PropertyAddressID;
//    protected LR_ID ProprietorName1ID;
//    protected LR_ID ProprietorName2ID;
//    protected LR_ID ProprietorName3ID;
//    protected LR_ID ProprietorName4ID;
//    protected LR_ID CompanyRegistrationNo1ID;
//    protected LR_ID CompanyRegistrationNo2ID;
//    protected LR_ID CompanyRegistrationNo3ID;
//    protected LR_ID CompanyRegistrationNo4ID;
//    protected LR_ID ProprietorshipCategory1ID;
//    protected LR_ID ProprietorshipCategory2ID;
//    protected LR_ID ProprietorshipCategory3ID;
//    protected LR_ID ProprietorshipCategory4ID;
//    protected LR_ID PostcodeDistrictID;
    private String TitleNumber;
    private String Tenure;
    private String PropertyAddress;
    private String District;
    private String Region;
    private String Postcode;
    private String PostcodeDistrict; // Set from Postcode
    private String MultipleAddressIndicator;
    private long PricePaid;
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
        initPostcodeAndPostcodeDistrict(r.getPostcode());
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
            boolean upDateIDs) throws Exception {
        if (Generic_StaticString.getCount(line, ",") > 10) {
            if (isCCOD) {
                if (doFull) {
                    return new LR_CC_FULL_Record(env, YM, line, upDateIDs);
                } else {
                    return new LR_CC_COU_Record(env, YM, line);
                }
            } else {
                if (doFull) {
                    return new LR_OC_FULL_Record(env, YM, line, upDateIDs);
                } else {
                    return new LR_OC_COU_Record(env, YM, line);
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
     * @param sType The type of collection to set in.
     * @return The LR_ID of s for sType.
     */
    protected LR_ID updateNonNullCollections(String s, String sType) {
        LR_ID typeID;
        typeID = Env.TypeToID.get(sType);
        return updateNonNullCollections(s, typeID);
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
        if (ToID.containsKey(s)) {
            result = ToID.get(s);
        } else {
            result = new LR_ID(ToID.size());
            ToID.put(s, result);
            IDTo = Env.IDToLookups.get(typeID);
            IDTo.put(result, s);
            Env.UpdatedNonNullTypes.put(typeID, true);
        }
        return result;
    }

    /**
     * @param s TitleNumber
     * @throws Exception
     */
    public final void initTitleNumber(String s) throws Exception {
        if (s.isEmpty()) {
            throw new Exception("TitleNumber is empty");
        }
        setTitleNumber(s);
        String sType;
        sType = Env.Strings.S_TitleNumber;
        TitleNumberID = updateNonNullCollections(s, sType);
    }

    /**
     *
     * @param s Tenure
     * @throws Exception
     */
    public final void initTenure(String s) throws Exception {
        if (!(s.equalsIgnoreCase(Env.Strings.S_Leasehold)
                || s.equalsIgnoreCase(Env.Strings.S_Freehold))) {
            throw new Exception("Unexpected Tenure: \"" + s + "\"");
        }
        setTenure(s);
        updateNonNullCollections(s, Env.Strings.S_Tenure);
    }

    /**
     * If s is blank then PropertyAddress is set to a unique number and
     * TitleNumberID is added to Env.TitleNumberIDsOfNullPropertyAddress.
     *
     * @param s PropertyAddress
     */
    public final void initPropertyAddressAndID(String s) {
        String sType;
        sType = Env.Strings.S_PropertyAddress;
        LR_ID typeID;
        typeID = Env.TypeToID.get(sType);
        if (s.isEmpty()) {
            setPropertyAddress(updateNullCollection(typeID));
        } else {
            setPropertyAddress(s);
        }
        LR_ID id;
        id = updateNonNullCollections(getPropertyAddress(), typeID);
        // init ID
        ID = new LR_ID2(TitleNumberID, id);
        if (!Env.IDs.contains(ID)) {
            Env.IDs.add(ID);
            Env.UpdatedIDs = true;
        }
    }

    /**
     * If s is blank then PricePaid is set to a negative unique number and
     * TitleNumberID is added to Env.TitleNumberIDsOfNullPricePaid.
     *
     * @param s PricePaid
     */
    public final void initPricePaid(String s) {
        if (s.isEmpty()) {
            setPricePaid(updateNullPricePaid());
        } else {
            try {
                setPricePaid(Long.valueOf(s));
            } catch (NumberFormatException e) {
                System.err.println("PricePaid is: \"" + s + "\" which is not "
                        + "recognised as a long.");
                setPricePaid(updateNullPricePaid());
            }
        }
    }

    public final long updateNullPricePaid() {
        LR_ID typeID;
        typeID = Env.TypeToID.get(Env.Strings.S_PricePaid);
        return Long.valueOf("-" + updateNullCollection(typeID));
    }

    /**
     *
     * @param typeID
     * @return
     */
    public String updateNullCollection(LR_ID typeID) {
        String result;
        HashSet<LR_ID> c;
        c = Env.NullTitleNumberIDCollections.get(typeID);
        result = Integer.toString(c.size());
        c.add(TitleNumberID);
        Env.UpdatedNullTypes.put(typeID, true);
        return result;
    }

    /**
     * If s is blank then ProprietorName1 is set to a unique number and
     * TitleNumberID is added to Env.TitleNumberIDsOfNullProprietorName1.
     *
     * @param s ProprietorName1
     */
    public final void initProprietorName1(String s) {
        String sType;
        sType = Env.Strings.S_ProprietorName;
        LR_ID typeID;
        typeID = Env.TypeToID.get(sType);
        if (s.isEmpty()) {
            setProprietorName1(updateNullCollection(typeID));
        } else {
            setProprietorName1(s);
        }
        updateNonNullCollections(getProprietorName1(), typeID);
    }

    /**
     * If s is blank then CompanyRegistrationNo1 is set to a unique number and
     * TitleNumberID is added to Env.TitleNumberIDsOfNullCompanyRegistrationNo1.
     *
     * @param s CompanyRegistrationNo1
     */
    public final void initCompanyRegistrationNo1(String s) {
        String sType;
        sType = Env.Strings.S_CompanyRegistrationNo;
        LR_ID typeID;
        typeID = Env.TypeToID.get(sType);
        if (s.isEmpty()) {
            setCompanyRegistrationNo1(updateNullCollection(typeID));
        } else {
            setCompanyRegistrationNo1(s);
        }
        updateNonNullCollections(getCompanyRegistrationNo1(), typeID);
    }

    /**
     * If s is blank then ProprietorshipCategory1 is set to a unique number and
     * TitleNumberID is added to
     * Env.TitleNumberIDsOfNullProprietorshipCategory1.
     *
     * @param s ProprietorshipCategory1
     */
    public final void initProprietorshipCategory1(String s) {
        String sType;
        sType = Env.Strings.S_ProprietorshipCategory;
        LR_ID typeID;
        typeID = Env.TypeToID.get(sType);
        if (s.isEmpty()) {
            setProprietorshipCategory1(updateNullCollection(typeID));
        } else {
            setProprietorshipCategory1(s);
        }
        updateNonNullCollections(getProprietorshipCategory1(), typeID);
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
        LR_ID typeID;
        typeID = Env.TypeToID.get(Env.Strings.S_ProprietorName);
        return Env.ToIDLookups.get(typeID).get(getProprietorName1());
    }

    /**
     * @return the ProprietorName2ID
     */
    public final LR_ID getProprietorName2ID() {
        LR_ID typeID;
        typeID = Env.TypeToID.get(Env.Strings.S_ProprietorName);
        return Env.ToIDLookups.get(typeID).get(getProprietorName2());
    }

    /**
     * @return the ProprietorName3ID
     */
    public final LR_ID getProprietorName3ID() {
        LR_ID typeID;
        typeID = Env.TypeToID.get(Env.Strings.S_ProprietorName);
        return Env.ToIDLookups.get(typeID).get(getProprietorName3());
    }

    /**
     * @return the ProprietorName4ID
     */
    public final LR_ID getProprietorName4ID() {
        LR_ID typeID;
        typeID = Env.TypeToID.get(Env.Strings.S_ProprietorName);
        return Env.ToIDLookups.get(typeID).get(getProprietorName4());
    }

    /**
     * @return the CompanyRegistrationNo1ID
     */
    public final LR_ID getCompanyRegistrationNo1ID() {
        LR_ID typeID;
        typeID = Env.TypeToID.get(Env.Strings.S_CompanyRegistrationNo);
        return Env.ToIDLookups.get(typeID).get(getCompanyRegistrationNo1());
    }

    /**
     * @return the CompanyRegistrationNo2ID
     */
    public final LR_ID getCompanyRegistrationNo2ID() {
        LR_ID typeID;
        typeID = Env.TypeToID.get(Env.Strings.S_CompanyRegistrationNo);
        return Env.ToIDLookups.get(typeID).get(getCompanyRegistrationNo2());
    }

    /**
     * @return the CompanyRegistrationNo3ID
     */
    public final LR_ID getCompanyRegistrationNo3ID() {
        LR_ID typeID;
        typeID = Env.TypeToID.get(Env.Strings.S_CompanyRegistrationNo);
        return Env.ToIDLookups.get(typeID).get(getCompanyRegistrationNo3());
    }

    /**
     * @return the CompanyRegistrationNo4ID
     */
    public final LR_ID getCompanyRegistrationNo4ID() {
        LR_ID typeID;
        typeID = Env.TypeToID.get(Env.Strings.S_CompanyRegistrationNo);
        return Env.ToIDLookups.get(typeID).get(getCompanyRegistrationNo4());
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
        LR_ID typeID;
        typeID = Env.TypeToID.get(Env.Strings.S_Tenure);
        return Env.ToIDLookups.get(typeID).get(getTenure());
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
        LR_ID typeID;
        typeID = Env.TypeToID.get(Env.Strings.S_PostcodeDistrict);
        return Env.ToIDLookups.get(typeID).get(getPostcodeDistrict());
    }

    /**
     * @return the PricePaid
     */
    public final long getPricePaid() {
        return PricePaid;
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
        LR_ID typeID;
        typeID = Env.TypeToID.get(Env.Strings.S_ProprietorshipCategory);
        return Env.ToIDLookups.get(typeID).get(getProprietorshipCategory1());
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
        LR_ID typeID;
        typeID = Env.TypeToID.get(Env.Strings.S_ProprietorshipCategory);
        return Env.ToIDLookups.get(typeID).get(getProprietorshipCategory2());
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
        LR_ID typeID;
        typeID = Env.TypeToID.get(Env.Strings.S_ProprietorshipCategory);
        return Env.ToIDLookups.get(typeID).get(getProprietorshipCategory3());
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
        LR_ID typeID;
        typeID = Env.TypeToID.get(Env.Strings.S_ProprietorshipCategory);
        return Env.ToIDLookups.get(typeID).get(getProprietorshipCategory4());
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
        LR_ID typeID;
        typeID = Env.TypeToID.get(Env.Strings.S_CountryIncorporated1);
        return Env.ToIDLookups.get(typeID).get(getCountryIncorporated1());
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
     * @param Postcode the Postcode to set
     */
    public final void initPostcodeAndPostcodeDistrict(String Postcode) {
        this.Postcode = Postcode;
        String[] split;
        split = this.Postcode.split(" ");
        // Todo: add validation.
        this.PostcodeDistrict = split[0];
        // PostcodeDistrict
        String s;
        s = getPostcodeDistrict();
        if (!s.isEmpty()) {
            updateNonNullCollections(s, Env.Strings.S_PostcodeDistrict);
        }
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
        if (!s.isEmpty()) {
            updateNonNullCollections(s, Env.Strings.S_ProprietorshipCategory);
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
            updateNonNullCollections(s, Env.Strings.S_CompanyRegistrationNo);
        }
    }

    /**
     * @param s the CompanyRegistrationNo3 to set
     */
    public final void setCompanyRegistrationNo3(String s) {
        s = getStringWithoutLeadingZeroes(s);
        this.CompanyRegistrationNo3 = s;
        if (!s.isEmpty()) {
            updateNonNullCollections(s, Env.Strings.S_CompanyRegistrationNo);
        }
    }

    /**
     * @param s the CompanyRegistrationNo4 to set
     */
    public final void setCompanyRegistrationNo4(String s) {
        s = getStringWithoutLeadingZeroes(s);
        this.CompanyRegistrationNo4 = s;
        if (!s.isEmpty()) {
            updateNonNullCollections(s, Env.Strings.S_CompanyRegistrationNo);
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
            updateNonNullCollections(s, Env.Strings.S_ProprietorName);
        }
    }

    /**
     * @param s the ProprietorName3 to set
     */
    public final void setProprietorName3(String s) {
        this.ProprietorName3 = s;
        if (!s.isEmpty()) {
            updateNonNullCollections(s, Env.Strings.S_ProprietorName);
        }
    }

    /**
     * @param s the ProprietorName4 to set
     */
    public final void setProprietorName4(String s) {
        this.ProprietorName4 = s;
        if (!s.isEmpty()) {
            updateNonNullCollections(s, Env.Strings.S_ProprietorName);
        }
    }

    /**
     * @param s the ProprietorshipCategory2 to set
     */
    public final void setProprietorshipCategory2(String s) {
        this.ProprietorshipCategory2 = s;
        if (!s.isEmpty()) {
            updateNonNullCollections(s, Env.Strings.S_ProprietorshipCategory);
        }
    }

    /**
     * @param s the ProprietorshipCategory3 to set
     */
    public final void setProprietorshipCategory3(String s) {
        this.ProprietorshipCategory3 = s;
        if (!s.isEmpty()) {
            updateNonNullCollections(s, Env.Strings.S_ProprietorshipCategory);
        }
    }

    /**
     * @param s the ProprietorshipCategory4 to set
     */
    public final void setProprietorshipCategory4(String s) {
        this.ProprietorshipCategory4 = s;
        if (!s.isEmpty()) {
            updateNonNullCollections(s, Env.Strings.S_ProprietorshipCategory);
        }
    }
}
