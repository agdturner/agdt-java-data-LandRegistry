
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import uk.ac.leeds.ccg.andyt.data.interval.Generic_Interval_long1;
import uk.ac.leeds.ccg.andyt.generic.lang.Generic_String;
import uk.ac.leeds.ccg.andyt.generic.time.Generic_YearMonth;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Environment;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_ID2;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Object;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_TypeID;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_ValueID;

/**
 *
 * @author geoagdt
 */
public abstract class LR_Record extends LR_Object {

    protected Generic_YearMonth YM;
    protected LR_ID2 ID;
    protected LR_ValueID TitleNumberID;
    private String TitleNumber;
    private String Tenure;
    private String PropertyAddress;
    private String PropertyAddressPAON;
    private String PropertyAddressSAON;
    private String PropertyAddressSTREET;
    private String PropertyAddressLOCALITY;
    private String PropertyAddressCITY;
    private String District;
    private String Region;
    private String Postcode;
    private String PostcodeDistrict; // Set from Postcode
    private String MultipleAddressIndicator;
    private String PricePaid;
    private Long PricePaidValue; // Null if PricePaid is empty or not an integer value
    private LR_ValueID PricePaidClass; // Set from PricePaidValue using env.PricePaidLookup
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

    public LR_Record(LR_Environment env) {
        super(env);
    }
    
    /**
     * Creates a simple copy of r without changing any collections.
     *
     * @param r
     * @param doUpdate
     */
    public LR_Record(LR_Record r, boolean doUpdate) {
        super(r.env);
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
        setProprietorName2(r.getProprietorName2(), doUpdate);
        setCompanyRegistrationNo2(r.getCompanyRegistrationNo2(), doUpdate);
        setProprietorshipCategory2(r.getProprietorshipCategory2(), doUpdate);
        setProprietor2Address1(r.getProprietor2Address1());
        setProprietor2Address2(r.getProprietor2Address2());
        setProprietor2Address3(r.getProprietor2Address3());
        setProprietorName3(r.getProprietorName3(), doUpdate);
        setCompanyRegistrationNo3(r.getCompanyRegistrationNo3(), doUpdate);
        setProprietorshipCategory3(r.getProprietorshipCategory3(), doUpdate);
        setProprietor3Address1(r.getProprietor3Address1());
        setProprietor3Address2(r.getProprietor3Address2());
        setProprietor3Address3(r.getProprietor3Address3());
        setProprietorName4(r.getProprietorName4(), doUpdate);
        setCompanyRegistrationNo4(r.getCompanyRegistrationNo4(), doUpdate);
        setProprietorshipCategory4(r.getProprietorshipCategory4(), doUpdate);
        setProprietor4Address1(r.getProprietor4Address1());
        setProprietor4Address2(r.getProprietor4Address2());
        setProprietor4Address3(r.getProprietor4Address3());
        setDateProprietorAdded(r.getDateProprietorAdded());
        setAdditionalProprietorIndicator(r.getAdditionalProprietorIndicator());
    }

    public static LR_Record create(boolean isCCOD, boolean doFull,
            LR_Environment env, Generic_YearMonth YM, String line,
            boolean doUpdate) throws Exception {
        if (Generic_String.getCount(line, ",") > 10) {
            if (isCCOD) {
                if (doFull) {
                    return new LR_CC_FULL_Record(env, YM, line, doUpdate);
                } else {
                    return new LR_CC_COU_Record(env, YM, line, doUpdate);
                }
            } else {
                if (doFull) {
                    return new LR_OC_FULL_Record(env, YM, line, doUpdate);
                } else {
                    return new LR_OC_COU_Record(env, YM, line, doUpdate);
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
     * @param typeID The typeID of the variable.
     * @param s The variable value.
     * @param doUpdate
     */
    protected final void update(LR_TypeID typeID, String s, boolean doUpdate) {
        if (s.isEmpty()) {
            if (doUpdate) {
                updateNullCollection(typeID, s);
            }
        } else {
            env.addValue(typeID, s);
        }
    }

//    /**
//     * @param typeID The typeID of the variable.
//     * @param s The variable value.
//     */
//    protected final void update(LR_TypeID typeID, String s) {
//        env.addValue(typeID, s);
//        updateNonNullCollections(s, typeID);
//    }
//
//    /**
//     *
//     * @param s The key to set.
//     * @param typeID The typeID of collection to set in.
//     * @return The LR_TypeID of s for sType.
//     */
//    protected LR_ValueID updateNonNullCollections(String s, LR_TypeID typeID) {
//        LR_ValueID result;
//        HashMap<String, LR_ValueID> valueReverseLookup;
//        valueReverseLookup = env.ValueReverseLookups.get(typeID);
//            if (valueReverseLookup.containsKey(s)) {
//                result = valueReverseLookup.get(s);
//            } else {
//                result = new LR_ValueID(valueReverseLookup.size(), s);
//                valueReverseLookup.put(s, result);
//                HashSet<LR_ValueID> valueIDs;
//                valueIDs = env.ValueIDs.get(typeID);
//                valueIDs.add(result);
//                HashSet<String> values;
//                values = env.Values.get(typeID);
//                values.add(s);                
//            }
//        return result;
//    }
    /**
     * @param s TitleNumber
     * @param doUpdate IFF true then collections are updated otherwise ID is set
     * from data pulled from existing collections.
     * @throws Exception
     */
    public final void initTitleNumber(String s, boolean doUpdate) throws Exception {
        if (s.isEmpty()) {
            throw new Exception("TitleNumber is empty");
        }
        setTitleNumber(s);
        if (doUpdate) {
            TitleNumberID = env.addValue(env.TitleNumberTypeID, s);
        } else {
            TitleNumberID = env.ValueReverseLookups.get(env.TitleNumberTypeID).get(s);
        }
    }

    /**
     *
     * @param s Tenure
     * @param doUpdate IFF true then collections are updated otherwise ID is set
     * from data pulled from existing collections.
     * @throws Exception
     */
    public final void initTenure(String s, boolean doUpdate) throws Exception {
        if (!(s.equalsIgnoreCase(env.strings.S_Leasehold)
                || s.equalsIgnoreCase(env.strings.S_Freehold))) {
            throw new Exception("Unexpected Tenure: \"" + s + "\"");
        }
        setTenure(s);
//        if (doUpdate) {
//            updateNonNullCollections(s, env.TenureTypeID);
//        }
    }

    /**
     * If s is blank then PropertyAddress is set to a unique number and
 TitleNumberID is added to env.TitleNumberIDsOfNullPropertyAddress.
     *
     * @param s PropertyAddress
     * @param doUpdate IFF true then collections are updated otherwise ID is set
     * from data pulled from existing collections.
     */
    public final void initPropertyAddressAndID(String s, boolean doUpdate) {
        LR_TypeID typeID;
        typeID = env.PropertyAddressTypeID;
        setPropertyAddress(s);
        /**
         * Attempt to set PropertyAddressPAON, PropertyAddressSAON,
         * PropertyAddressSTREET, PropertyAddressCITY. There are many special
         * cases!
         */
        if (!s.isEmpty()) {
            String[] split;
            split = s.split(", ");
            int len;
            len = split.length;
            String s2;
            // Strip out postcode if it is the only part of the last part.
            if (split[len - 1].trim().startsWith("(")) {
                s2 = "";
                for (int i = 0; i < len - 2; i++) {
                    s2 += split[i] + ", ";
                }
                s2 += split[len - 2];
                split = s2.split(", ");
                len = split.length;
            } else {
                s2 = s;
            }

            if (len == 1) {
                String[] split2;
                split2 = s2.split(" ");
                int len2;
                len2 = split2.length;
                String pAON;
                String sAON;
                String street;

                if (env.NumeralsHashSet.contains(split2[0].substring(0, 1))) {
                    String s3;
                    s3 = split2[1].trim();
                    if (containsAddressNumberJoin(s3)) {
                        if (env.NumeralsHashSet.contains(split2[2].substring(0, 1))) {
                            /**
                             * Get up to the last numeral and assign this as
                             * PAON and assign the rest as Street and deal with
                             * cases where the number is also followed by a
                             * letter such as in the case of e.g. "8 and 8a
                             * Guilford Street".
                             */
                            //Matcher m = Pattern.compile("\\d+[^ ]").matcher(s2);
                            int i;
                            i = findIndexOfLastNumber(s2);
                            street = s2.substring(i);

                            String[] sa;
                            sa = rejoinLetter(s2, i, street);
                            pAON = sa[0];
                            street = sa[1];
                            setPropertyAddressPAON(pAON);
                            setPropertyAddressSTREET(street);
                        } else {
                            //city = setAddressComponentsCase1(pAON, split, split2, len);
                            int debug = 1;
                        }
                    } else {
                        //e.g. "130 London Road"
                        setPropertyAddressPAON(split2[0]);
                        String s4;
                        s4 = "";
                        for (int i = 1; i < split2.length - 1; i++) {
                            s4 += split2[i] + " ";
                        }
                        s4 += split2[split2.length - 1];
                        setPropertyAddressSTREET(s4);
                    }
                } else {
                    //Separate the address into 3 sections based on the positions of the numbers
                    int i1, i2;
                    String[] sa;
                    i1 = findFirstIndexOfFirstNumber(s2);
                    i2 = findIndexOfLastNumber(s2);
                    pAON = s2.substring(0, i1).trim();
                    street = s2.substring(i2).trim();
                    if (!street.isEmpty()) {
                        sa = rejoinLetter(s2.substring(i1), i2, street);
                        sAON = sa[0];
                        street = sa[1];
                        setPropertyAddressSAON(sAON);
                        setPropertyAddressSTREET(street);
                    }
                    setPropertyAddressPAON(pAON);
                    int debug = 1;

                }
            } else if (len == 2) {
                //System.out.println(s);
                String pAON, street, city;
                String[] sa;
                //if first character is a number, split into paon, street, city
                if (env.NumeralsHashSet.contains(split[0].substring(0, 1))) {
                    int i;
                    i = findIndexOfLastNumber(split[0]);
                    street = split[0].substring(i);
                    sa = rejoinLetter(split[0], i, street);
                    pAON = sa[0];
                    street = sa[1];
                    city = split[1].split(" ")[0].trim();
                    setPropertyAddressPAON(pAON);
                    setPropertyAddressSTREET(street);
                    setPropertyAddressCITY(city);
                } else {
                    int debug = 1;
                    pAON = split[0];
                    city = split[1].split(" ")[0];
                    setPropertyAddressPAON(pAON);
                    setPropertyAddressCITY(city);
                }

            } else if (len == 3) {
//                    
//                    //setPropertyAddressSAON(s);
//
//                    String pAON;
//                    pAON = split[0];
//
//                    // Need to deal with cases like Crompton Farm:
//                    // In these cases how to distinguish a placename from a street name?
////                if (pAON.equalsIgnoreCase("Compton Farm")) {
////                    int debug = 1;
////                }
//                    String street;
//                    String city;
//                    /*
//                 * If the first thing is a number then distinguish when there is more than this as a number
//                     */
//                    if (env.NumeralsHashSet.contains(pAON.substring(0, 1))) {
//
//                        String[] split2;
//                        split2 = pAON.split(" ");
//                        int len2;
//                        len2 = split2.length;
//                        String s2;
//                        s2 = split2[1].trim();
//                        if (containsAddressNumberJoin(s2)) {
//                            if (env.NumeralsHashSet.contains(split2[2].substring(0, 1))) {
//                                city = setAddressComponentsCase0(pAON, split, len);
//                            } else {
//                                city = setAddressComponentsCase1(pAON, split, split2, len);
//                            }
//                        } else {
//                            city = setAddressComponentsCase1(pAON, split, split2, len);
//                        }
//
//                    } else {
//                        city = setAddressComponentsCase0(pAON, split, len);
//                    }
////            setPropertyAddressLOCALITY();
//
//                    if (!city.startsWith("(")) {
//                        setPropertyAddressCITY(city);
//                        System.out.println(PropertyAddressPAON + ", " + PropertyAddressSTREET + ", " + PropertyAddressCITY);
//                        System.out.println(PropertyAddressPAON + ", " + PropertyAddressSTREET + ", " + PropertyAddressCITY);
//                    } else {
//                        System.out.println(PropertyAddressPAON + ", " + PropertyAddressSTREET + ", " + PropertyAddressCITY);
//                        System.out.println(PropertyAddressPAON + ", " + PropertyAddressSTREET + ", " + PropertyAddressCITY);
//                    }
//                } else {
//                    if (len == 2) {
//                        if (split[0].equalsIgnoreCase("20 - 42 Shepherd Street")) {
//                            int debug = 1;
//                        }
//                    }
//                    int debug = 1;
            }

            if (doUpdate) {
                LR_ValueID valueID;
                if (!s.isEmpty()) {
                    valueID = env.addValue(typeID, s);
                } else {
                    valueID = updateNullCollection(typeID, s);
                }
                // init ID
                ID = new LR_ID2(TitleNumberID, valueID);
                if (!env.IDs.contains(ID)) {
                    env.IDs.add(ID);
                }
                /**
                 * Add to Env.NullTitleNumberIDCollections where ID was null
                 * when updateNullCollection(typeID, s) was called above.
                 */
                if (s.isEmpty()) {
                    if (valueID.getValue().isEmpty()) {
                        env.NullTitleNumberIDCollections.get(typeID).put(ID, valueID);
//                    System.out.println("Added ID " + ID.toString() 
//                            + " ValueID " + valueID 
//                            + " to env.NullTitleNumberIDCollections for TypeID " 
//                            + typeID);
                    }
                }
                HashSet<LR_ValueID> titleNumberIDs;
                if (env.AddressIDToTitleNumberIDsLookup.containsKey(valueID)) {
                    titleNumberIDs = env.AddressIDToTitleNumberIDsLookup.get(valueID);
                } else {
                    titleNumberIDs = new HashSet<>();
                    env.AddressIDToTitleNumberIDsLookup.put(valueID, titleNumberIDs);
                }
                if (!titleNumberIDs.contains(TitleNumberID)) {
                    titleNumberIDs.add(TitleNumberID);
//                env.UpdatedAddressIDToTitleNumberIDsLookup = true;
                }
                LR_ValueID change;
                change = env.TitleNumberIDToAddressIDLookup.put(TitleNumberID, valueID);
                if (change != null) {
                    if (!change.equals(valueID)) {
//                    System.out.println("Address for TitleNumer " + TitleNumber
//                            + " changed from \"" + change.getValue()
//                            + "\" to \"" + pa + "\"");
                        System.out.println("Address for TitleNumer " + TitleNumber
                                + " changed from \"" + change.getValue()
                                + "\" to \"" + s + "\"");
                    }
                }
            } else {
                LR_ValueID addressID;
                addressID = env.TitleNumberIDToAddressIDLookup.get(TitleNumberID);
                ID = new LR_ID2(TitleNumberID, addressID);
            }
        }
    }

    /**
     * If s2 starts with a space then: return result[0] = s.substring(0, i);
     * result[1] = s2.trim();. Otherwise join the first part of s2 (before the
     * next space) to s and return this as result[0] and remove that part from
     * s2 (along with the space) and return that as result[1].
     *
     * @param s
     * @param i
     * @param s2
     * @return
     */
    private String[] rejoinLetter(String s, int i, String s2) {
        String[] result;
        result = new String[2];
        String s1;
        if (s2.startsWith(" ")) {
            s1 = s.substring(0, i);
            s2 = s2.trim();
        } else {
            String[] split3;
            split3 = s2.split(" ");
            s1 = s.substring(0, i) + split3[0];
            String s4;
            s4 = "";
            for (int j = 1; j < split3.length - 1; j++) {
                s4 += split3[j] + " ";
            }
            s4 += split3[split3.length - 1];
            s2 = s4;
        }
        result[0] = s1;
        result[1] = s2;
        return result;
    }

    /**
     * If s does not contain a number returns the length of s.
     *
     * @param s
     * @return
     */
    private int findIndexOfLastNumber(String s) {
        Matcher m = Pattern.compile("\\d").matcher(s);
        int i = s.length();
        while (m.find()) {
            i = m.end();
        }
        return i;
    }

    /**
     * If s does not contain a number returns the length of s.
     *
     * @param s
     * @return
     */
    private int findFirstIndexOfFirstNumber(String s) {
        Matcher m = Pattern.compile("\\d").matcher(s);
        int i = 0;
        boolean success;
        success = m.find();
        if (success) {
            i = m.start();
        } else {
            i = s.length();
        }
        return i;
    }

    private boolean containsAddressNumberJoin(String s) {
        return s.equalsIgnoreCase("to") || s.equalsIgnoreCase("-") || s.equalsIgnoreCase("and");
    }

    private String setAddressComponentsCase0(String pAON, String[] split, int len) {
        setPropertyAddressPAON(pAON);
        String street;
        street = split[1];
        setPropertyAddressSTREET(street);
        String city;
        city = split[len - 1].split(" ")[0].trim();
        return city;
    }

    private String setAddressComponentsCase1(String pAON, String[] split, String[] split2, int len) {
        String pAON2;
        pAON2 = split2[0];
        setPropertyAddressPAON(pAON2);
        String street;
        street = pAON.substring(pAON2.length()).trim();
        setPropertyAddressSTREET(street);
        // If the last bit starts with a " (" then set city from second to last part.
        String city;
        if (split[len - 1].trim().startsWith("(")) {
            city = split[len - 2];
        } else {
            city = split[len - 1].split(" ")[0].trim();
        }
        return city;
    }

    /**
     * If doUpdate then if s is blank then PricePaid is set to a unique negative
     * number and a record is kept to look up this number from ID. If !doUpdate
     * then the unique number set previously is obtained from what is stored.
     *
     * @param s PricePaid
     * @param doUpdate IFF true then collections are updated otherwise ID is set
     * from data pulled from existing collections.
     */
    public final void initPricePaid(String s, boolean doUpdate) {
        setPricePaid(s);
        if (s.isEmpty()) {
            if (doUpdate) {
                updateNullCollection(env.PricePaidTypeID, s);
            }
        } else {
            try {
                long l;
                l = Long.valueOf(s);
                setPricePaidValue(l);
                setPricePaidClass(l);
            } catch (NumberFormatException e) {
                System.err.println("PricePaid is: \"" + s + "\" which is not "
                        + "recognised as a long.");
            }
        }
    }

    public void setPricePaidClass(long l) {
        Iterator<LR_ValueID> ite;
        ite = env.PricePaidLookup.keySet().iterator();
        LR_ValueID k;
        Generic_Interval_long1 i;
        while (ite.hasNext()) {
            k = ite.next();
            i = env.PricePaidLookup.get(k);
            if (i.isInInterval(l)) {
                PricePaidClass = k;
            }
        }
//        if (PricePaidClass == null) {
//            k = new LR_ID(env.PricePaidLookup.size());
//        }
    }

    /**
     *
     * @param typeID
     * @param s
     * @return
     */
    protected LR_ValueID updateNullCollection(LR_TypeID typeID, String s) {
        HashMap<LR_ID2, LR_ValueID> m;
        m = env.NullTitleNumberIDCollections.get(typeID);
        if (m == null) {
            // Tested for datasets from 2017-11 to 2018-10 and this case did not occur!
            m = new HashMap<>();
            env.NullTitleNumberIDCollections.put(typeID, m);
        }

        if (ID == null) {
            // Tested for datasets from 2017-11 to 2018-10 and this case did not occur!
            int debug = 1;
        } else {
            if (ID.getPropertyAddressID() == null || ID.getTitleNumberID() == null) {
                // Tested for datasets from 2017-11 to 2018-10 and this case did not occur!
                int debug = 1;
            }
        }

        if (m.containsKey(ID)) {
            LR_ValueID v0;
            v0 = m.get(ID);
            if (s.isEmpty()) {
                String s0;
                s0 = v0.getValue();
                if (!s0.isEmpty()) {
                    // Tested for datasets from 2017-11 to 2018-10 and this case did not occur!
                    return updateNullCollection(typeID, s0, m, v0);
                } else {
                    return updateNullCollection(typeID, s0, m, v0);
                }
            } else {
                if (v0 != null) {

//                    if (typeID.equals(env.PostcodeDistrictTypeID)) { // Debugging code to check the postcode.
//                        env.PostcodeHandler.isValidPostcodeForm(s);
//                    }
                    return updateNullCollection(typeID, s, m, v0);
                } else {
                    // Tested for datasets from 2017-11 to 2018-10 and this case did not occur!
                    return updateNullCollection(typeID, s, m, v0);
                }
            }
        } else {
            return updateNullCollection(typeID, s, m, null);
        }
    }

    protected LR_ValueID updateNullCollection(LR_TypeID typeID, String s,
            HashMap<LR_ID2, LR_ValueID> m, LR_ValueID v0) {
        LR_ValueID v1;
        if (v0 == null) {
            if (ID == null) {
                v1 = env.addValue(typeID, s);
//                System.out.println("Added " + v1.toString()
//                        + " to Null Collection for typeID " + typeID.toString()
//                        + " ID null");
            } else {
                v1 = addToNullCollection(m, s);
//                System.out.println("Added " + v1.toString()
//                        + " to Null Collection for typeID " + typeID.toString()
//                        + " ID " + ID.toString());
            }
        } else {
            if (ID == null) {
                // Tested for datasets from 2017-11 to 2018-10 and this case did not occur!
                v1 = env.addValue(typeID, s);
                System.out.println("Updated Null Collection for typeID "
                        + typeID.toString() + " ID null "
                        + " from " + v0.toString() + " to " + v1.toString());
            } else {
                if (v0.getValue().equalsIgnoreCase(s)) {
//                    System.out.println("Not updated Null Collection for typeID "
//                            + typeID.toString() + " ID " + ID.toString()
//                            + " from " + v0.toString());

//                    // Tested for datasets from 2017-11 to 2018-10 and the 
//                    // following if statements do not catch anything.
//                    if (typeID.equals(env.PropertyAddressTypeID)) {
//                        int debug = 1;
//                    }
//                    if (typeID.equals(env.TitleNumberTypeID)) {
//                        int debug = 1;
//                    }
                    return v0;
                } else {
                    // Tested for datasets from 2017-11 to 2018-10 and this case did not occur!
                    v1 = addToNullCollection(m, s);
                    System.out.println("Updated Null Collection for typeID "
                            + typeID.toString() + " ID " + ID.toString()
                            + " from " + v0.toString() + " to " + v1.toString());
                }
            }
        }
        return v1;
    }

    protected LR_ValueID addToNullCollection(
            HashMap<LR_ID2, LR_ValueID> m, String s) {
        int i;
        i = m.size();
        LR_ValueID v;
        v = new LR_ValueID(i, s);
        if (ID != null) {
            m.put(ID, v);
        }
        return v;
    }

    /**
     * If doUpdate then if s is blank then ProprietorName1 is set to a unique
     * number and a record is kept to look up this number from ID. If !doUpdate
     * then the unique number set previously is obtained from what is stored.
     *
     * @param s ProprietorName1
     * @param doUpdate IFF true then collections are updated otherwise ID is set
     * from data pulled from existing collections.
     */
    public final void initProprietorName1(String s, boolean doUpdate) {
        LR_TypeID typeID;
        typeID = env.ProprietorNameTypeID;
        setProprietorName1(s);
        update(typeID, s, doUpdate);
    }

    /**
     * If doUpdate then if s is blank then CompanyRegistrationNo1 is set to a
     * unique number and a record is kept to look up this number from ID. If
     * !doUpdate then the unique number set previously is obtained from what is
     * stored.
     *
     * @param s CompanyRegistrationNo1
     * @param doUpdate IFF true then collections are updated otherwise ID is set
     * from data pulled from existing collections.
     */
    public final void initCompanyRegistrationNo1(String s, boolean doUpdate) {
        LR_TypeID typeID;
        typeID = env.CompanyRegistrationNoTypeID;
        setCompanyRegistrationNo1(s);
        update(typeID, s, doUpdate);
    }

    /**
     * If doUpdate then if s is blank then District is set to a unique number
     * and a record is kept to look up this number from ID. If !doUpdate then
     * the unique number set previously is obtained from what is stored.
     *
     * @param s CompanyRegistrationNo1
     * @param doUpdate IFF true then collections are updated otherwise ID is set
     * from data pulled from existing collections.
     */
    public final void initDistrict(String s, boolean doUpdate) {
        LR_TypeID typeID;
        typeID = env.DistrictTypeID;
        setDistrict(s);
        update(typeID, s, doUpdate);
    }

    /**
     * If doUpdate then if s is blank then County is set to a unique number and
     * a record is kept to look up this number from ID. If !doUpdate then the
     * unique number set previously is obtained from what is stored.
     *
     * @param s CompanyRegistrationNo1
     * @param doUpdate IFF true then collections are updated otherwise ID is set
     * from data pulled from existing collections.
     */
    public final void initCounty(String s, boolean doUpdate) {
        LR_TypeID typeID;
        typeID = env.CountyTypeID;
        setCounty(s);
        update(typeID, s, doUpdate);
    }

    /**
     * If doUpdate then if s is blank then Region is set to a unique number and
     * a record is kept to look up this number from ID. If !doUpdate then the
     * unique number set previously is obtained from what is stored.
     *
     * @param s CompanyRegistrationNo1
     * @param doUpdate IFF true then collections are updated otherwise ID is set
     * from data pulled from existing collections.
     */
    public final void initRegion(String s, boolean doUpdate) {
        LR_TypeID typeID;
        typeID = env.RegionTypeID;
        setRegion(s);
        update(typeID, s, doUpdate);
    }

    /**
     * If doUpdate then if s is blank then ProprietorshipCategory1 is set to a
     * unique number and a record is kept to look up this number from ID. If
     * !doUpdate then the unique number set previously is obtained from what is
     * stored.
     *
     * @param s ProprietorshipCategory1
     * @param doUpdate IFF true then collections are updated otherwise ID is set
     * from data pulled from existing collections.
     */
    public final void initProprietorshipCategory1(String s, boolean doUpdate) {
        LR_TypeID typeID;
        typeID = env.ProprietorshipCategoryTypeID;
        setProprietorshipCategory1(s);
        update(typeID, s, doUpdate);
    }

    /**
     * @return the TitleNumberID
     */
    public final LR_ValueID getTitleNumberID() {
        return TitleNumberID;
    }

    /**
     * @return the PropertyAddressID
     */
    public final LR_ValueID getPropertyAddressID() {
        return ID.getPropertyAddressID();
    }

    /**
     * @return the ProprietorName1ID
     */
    public final LR_ValueID getProprietorName1ID() {
        return env.ValueReverseLookups.get(env.ProprietorNameTypeID).get(getProprietorName1());
    }

    /**
     * @return the ProprietorName2ID
     */
    public final LR_ValueID getProprietorName2ID() {
        return env.ValueReverseLookups.get(env.ProprietorNameTypeID).get(getProprietorName2());
    }

    /**
     * @return the ProprietorName3ID
     */
    public final LR_ValueID getProprietorName3ID() {
        return env.ValueReverseLookups.get(env.ProprietorNameTypeID).get(getProprietorName3());
    }

    /**
     * @return the ProprietorName4ID
     */
    public final LR_ValueID getProprietorName4ID() {
        return env.ValueReverseLookups.get(env.ProprietorNameTypeID).get(getProprietorName4());
    }

    /**
     * @return the CompanyRegistrationNo1ID
     */
    public final LR_ValueID getCompanyRegistrationNo1ID() {
        return env.ValueReverseLookups.get(env.CompanyRegistrationNoTypeID).get(getCompanyRegistrationNo1());
    }

    /**
     * @return the CompanyRegistrationNo2ID
     */
    public final LR_ValueID getCompanyRegistrationNo2ID() {
        return env.ValueReverseLookups.get(env.CompanyRegistrationNoTypeID).get(getCompanyRegistrationNo2());
    }

    /**
     * @return the CompanyRegistrationNo3ID
     */
    public final LR_ValueID getCompanyRegistrationNo3ID() {
        return env.ValueReverseLookups.get(env.CompanyRegistrationNoTypeID).get(getCompanyRegistrationNo3());
    }

    /**
     * @return the CompanyRegistrationNo4ID
     */
    public final LR_ValueID getCompanyRegistrationNo4ID() {
        return env.ValueReverseLookups.get(env.CompanyRegistrationNoTypeID).get(getCompanyRegistrationNo4());
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
    public final LR_ValueID getTenureID() {
        return env.ValueReverseLookups.get(env.TenureTypeID).get(getTenure());
    }

    /**
     * @return the District
     */
    public final String getDistrict() {
        return District;
    }

    /**
     * @return the DistrictID
     */
    public final LR_ValueID getDistrictID() {
        return env.ValueReverseLookups.get(env.DistrictTypeID).get(getDistrict());
    }

    /**
     * @return the Region
     */
    public final String getRegion() {
        return Region;
    }

    /**
     * @return the RegionID
     */
    public final LR_ValueID getRegionID() {
        return env.ValueReverseLookups.get(env.RegionTypeID).get(getRegion());
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
    public final LR_ValueID getPostcodeDistrictID() {
        return env.ValueReverseLookups.get(env.PostcodeDistrictTypeID).get(getPostcodeDistrict());
    }

    /**
     * @return the PricePaid
     */
    public final String getPricePaid() {
        return PricePaid;
    }

    /**
     * @return the PricePaid
     */
    public final Long getPricePaidValue() {
        return PricePaidValue;
    }

    /**
     * @return the PricePaidClass
     */
    public final LR_ValueID getPricePaidClass() {
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
     * @return the CountyID
     */
    public final LR_ValueID getCountyID() {
        return env.ValueReverseLookups.get(env.CountyTypeID).get(getCounty());
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
    public final LR_ValueID getProprietorshipCategory1ID() {
        return env.ValueReverseLookups.get(env.ProprietorshipCategoryTypeID).get(getProprietorshipCategory1());
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
    public final LR_ValueID getProprietorshipCategory2ID() {
        return env.ValueReverseLookups.get(env.ProprietorshipCategoryTypeID).get(getProprietorshipCategory2());
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
    public final LR_ValueID getProprietorshipCategory3ID() {
        return env.ValueReverseLookups.get(env.ProprietorshipCategoryTypeID).get(getProprietorshipCategory3());
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
    public final LR_ValueID getProprietorshipCategory4ID() {
        return env.ValueReverseLookups.get(env.ProprietorshipCategoryTypeID).get(getProprietorshipCategory4());
    }

    /**
     * @return the CountryIncorporated1
     */
    public String getCountryIncorporated1() {
        return env.strings.S_United_Kingdom;
    }

    /**
     * @return the CountryIncorporated1ID
     */
    public LR_ValueID getCountryIncorporated1ID() {
        return env.ValueReverseLookups.get(env.CountryIncorporatedTypeID).get(getCountryIncorporated1());
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
     * @param s the PropertyAddressPAON to set
     */
    public final void setPropertyAddressPAON(String s) {
        this.PropertyAddressPAON = s;
    }

    /**
     * @param s the PropertyAddressSAON to set
     */
    public final void setPropertyAddressSAON(String s) {
        this.PropertyAddressSAON = s;
    }

    /**
     * @param s the PropertyAddressSTREET to set
     */
    public final void setPropertyAddressSTREET(String s) {
        this.PropertyAddressSTREET = s;
    }

    /**
     * @param s the PropertyAddressLOCALITY to set
     */
    public final void setPropertyAddressLOCALITY(String s) {
        this.PropertyAddressLOCALITY = s;
    }

    /**
     * @param s the PropertyAddressCITY to set
     */
    public final void setPropertyAddressCITY(String s) {
        this.PropertyAddressCITY = s;
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
     * If doUpdate then if s is blank then Postcode and PostcodeDistrict are set
     * to a unique number and a record is kept to look up this number from ID.
     * If !doUpdate then the unique number set previously is obtained from what
     * is stored.
     *
     * @TODO The way things currently are, the postcode is checked each time the
     * data is read in so as to set the Postcode District. This could be avoided
     * by storing formatted versions of records in collections that are loaded
     * back in.
     *
     * @param s Postcode
     * @param doUpdate IFF true then collections are updated otherwise ID is set
     * from data pulled from existing collections.
     */
    public final void initPostcodeAndPostcodeDistrict(String s, boolean doUpdate) {
        s = Generic_String.getUpperCase(s);
        setPostcode(s);
        LR_TypeID typeID;
        typeID = env.PostcodeDistrictTypeID;
        if (env.PostcodeHandler.isValidPostcodeForm(s)) {
            String[] split;
            split = this.Postcode.split(" ");
            // PostcodeDistrict
            String s0;
            s0 = split[0];
            setPostcodeDistrict(s0);
            if (doUpdate) {
                env.addValue(typeID, s0);
            }
        } else {
            setPostcodeDistrict(s);
            if (doUpdate) {
                updateNullCollection(typeID, s);
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
     * @param s the PricePaid to set
     */
    public final void setPricePaid(String s) {
        this.PricePaid = s;
    }

    /**
     * @param l the PricePaidValue to set
     */
    public final void setPricePaidValue(Long l) {
        this.PricePaidValue = l;
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
    }

    /**
     * @param s the AdditionalProprietorIndicator to set
     */
    public final void setAdditionalProprietorIndicator(String s) {
        this.AdditionalProprietorIndicator = s;
    }

    /**
     * @param s the CompanyRegistrationNo2 to set
     * @param doUpdate
     */
    public final void setCompanyRegistrationNo2(String s, boolean doUpdate) {
        s = getStringWithoutLeadingZeroes(s);
        this.CompanyRegistrationNo2 = s;
        if (doUpdate) {
            if (!s.isEmpty()) {
                env.addValue(env.CompanyRegistrationNoTypeID, s);
            }
        }
    }

    /**
     * @param s the CompanyRegistrationNo3 to set
     * @param doUpdate
     */
    public final void setCompanyRegistrationNo3(String s, boolean doUpdate) {
        s = getStringWithoutLeadingZeroes(s);
        this.CompanyRegistrationNo3 = s;
        if (doUpdate) {
            if (!s.isEmpty()) {
                env.addValue(env.CompanyRegistrationNoTypeID, s);
            }
        }
    }

    /**
     * @param s the CompanyRegistrationNo4 to set
     * @param doUpdate
     */
    public final void setCompanyRegistrationNo4(String s, boolean doUpdate) {
        s = getStringWithoutLeadingZeroes(s);
        this.CompanyRegistrationNo4 = s;
        if (doUpdate) {
            if (!s.isEmpty()) {
                env.addValue(env.CompanyRegistrationNoTypeID, s);
            }
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
     * @param doUpdate
     */
    public final void setProprietorName2(String s, boolean doUpdate) {
        this.ProprietorName2 = s;
        if (doUpdate) {
            if (!s.isEmpty()) {
                env.addValue(env.ProprietorNameTypeID, s);
            }
        }
    }

    /**
     * @param s the ProprietorName3 to set
     * @param doUpdate
     */
    public final void setProprietorName3(String s, boolean doUpdate) {
        this.ProprietorName3 = s;
        if (doUpdate) {
            if (!s.isEmpty()) {
                env.addValue(env.ProprietorNameTypeID, s);
            }
        }
    }

    /**
     * @param s the ProprietorName4 to set
     * @param doUpdate
     */
    public final void setProprietorName4(String s, boolean doUpdate) {
        this.ProprietorName4 = s;
        if (doUpdate) {
            if (!s.isEmpty()) {
                env.addValue(env.ProprietorNameTypeID, s);
            }
        }
    }

    /**
     * @param s the ProprietorshipCategory2 to set
     * @param doUpdate
     */
    public final void setProprietorshipCategory2(String s, boolean doUpdate) {
        this.ProprietorshipCategory2 = s;
        if (doUpdate) {
            if (!s.isEmpty()) {
                env.addValue(env.ProprietorshipCategoryTypeID, s);
            }
        }
    }

    /**
     * @param s the ProprietorshipCategory3 to set
     * @param doUpdate
     */
    public final void setProprietorshipCategory3(String s, boolean doUpdate) {
        this.ProprietorshipCategory3 = s;
        if (doUpdate) {
            if (!s.isEmpty()) {
                env.addValue(env.ProprietorshipCategoryTypeID, s);
            }
        }
    }

    /**
     * @param s the ProprietorshipCategory4 to set
     * @param doUpdate
     */
    public final void setProprietorshipCategory4(String s, boolean doUpdate) {
        this.ProprietorshipCategory4 = s;
        if (doUpdate) {
            if (!s.isEmpty()) {
                env.addValue(env.ProprietorshipCategoryTypeID, s);
            }
        }
    }
}
