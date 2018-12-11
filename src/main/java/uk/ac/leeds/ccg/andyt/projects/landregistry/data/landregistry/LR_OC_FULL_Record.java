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

import java.io.Serializable;
import uk.ac.leeds.ccg.andyt.generic.time.Generic_YearMonth;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Environment;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_TypeID;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_ValueID;

/**
 *
 * @author Andy Turner
 */
public class LR_OC_FULL_Record extends LR_CC_FULL_Record implements Serializable {

    protected LR_ValueID CountryIncorporated1ID;
    protected LR_ValueID CountryIncorporated2ID;
    protected LR_ValueID CountryIncorporated3ID;
    protected LR_ValueID CountryIncorporated4ID;
    protected String CountryIncorporated1;
    protected String CountryIncorporated2;
    protected String CountryIncorporated3;
    protected String CountryIncorporated4;

    protected LR_OC_FULL_Record() {
    }

    public LR_OC_FULL_Record(LR_Environment env, Generic_YearMonth YM,
            String line, boolean doUpdate) throws Exception {
        this.Env = env;
        this.YM = YM;
        String[] ls;
        ls = getSplitAndTrim(line);
        initTitleNumber(ls[0].substring(1), doUpdate);
        initTenure(ls[1], doUpdate);
        initPropertyAddressAndID(ls[2], doUpdate);
        initDistrict(ls[3], doUpdate);
        initCounty(ls[4], doUpdate);
        initRegion(ls[5], doUpdate);
        initPostcodeAndPostcodeDistrict(ls[6], doUpdate);
        setMultipleAddressIndicator(ls[7]);
        initPricePaid(ls[8], doUpdate);
        initProprietorName1(ls[9], doUpdate);
        initCompanyRegistrationNo1(ls[10], doUpdate);
        initProprietorshipCategory1(ls[11], doUpdate);
        initCountryIncorporated1(ls[12], doUpdate);
        setProprietor1Address1(ls[13]);
        setProprietor1Address2(ls[14]);
        setProprietor1Address3(ls[15]);
        setProprietorName2(ls[16], doUpdate);
        setCompanyRegistrationNo2(ls[17], doUpdate);
        setProprietorshipCategory2(ls[18], doUpdate);
        setCountryIncorporated2(ls[19], doUpdate);
        setProprietor2Address1(ls[20]);
        setProprietor2Address2(ls[21]);
        setProprietor2Address3(ls[22]);
        setProprietorName3(ls[23], doUpdate);
        setCompanyRegistrationNo3(ls[24], doUpdate);
        setProprietorshipCategory3(ls[25], doUpdate);
        setCountryIncorporated3(ls[26], doUpdate);
        setProprietor3Address1(ls[27]);
        setProprietor3Address2(ls[28]);
        setProprietor3Address3(ls[29]);
        setProprietorName4(ls[30], doUpdate);
        setCompanyRegistrationNo4(ls[31], doUpdate);
        setProprietorshipCategory4(ls[32], doUpdate);
        setCountryIncorporated4(ls[33], doUpdate);
        setProprietor4Address1(ls[34]);
        setProprietor4Address2(ls[35]);
        setProprietor4Address3(ls[36]);
        setDateProprietorAdded(ls[37]);
        setAdditionalProprietorIndicator(ls[38]);
    }

    /**
     * Creates a simple copy of r without changing any collections.
     *
     * @param r
     * @param doUpdate
     */
    public LR_OC_FULL_Record(LR_OC_FULL_Record r, boolean doUpdate) {
        super(r, doUpdate);
        setCountryIncorporated1(r.getCountryIncorporated1());
        setCountryIncorporated2(r.getCountryIncorporated2(), doUpdate);
        setCountryIncorporated3(r.getCountryIncorporated3(), doUpdate);
        setCountryIncorporated4(r.getCountryIncorporated4(), doUpdate);
    }

    @Override
    public String toString() {
        return "TitleNumber " + getTitleNumber()
                + ",Tenure " + getTenure()
                + ",PropertyAddress " + getPropertyAddress()
                + ",District " + getDistrict()
                + ",County " + getCounty()
                + ",Region " + getRegion()
                + ",Postcode " + getPostcode()
                + ",MultipleAddressIndicator " + getMultipleAddressIndicator()
                + ",PricePaid " + getPricePaid()
                + ",ProprietorName1 " + getProprietorName1()
                + ",CompanyRegistrationNo1 " + getCompanyRegistrationNo1()
                + ",ProprietorshipCategory1 " + getProprietorshipCategory1()
                + ",CountryIncorporated1 " + getCountryIncorporated1()
                + ",Proprietor1Address1 " + getProprietor1Address1()
                + ",Proprietor1Address2 " + getProprietor1Address2()
                + ",Proprietor1Address3 " + getProprietor1Address3()
                + ",ProprietorName2 " + getProprietorName2()
                + ",CompanyRegistrationNo2 " + getCompanyRegistrationNo2()
                + ",ProprietorshipCategory2 " + getProprietorshipCategory2()
                + ",CountryIncorporated2 " + getCountryIncorporated2()
                + ",Proprietor2Address1 " + getProprietor2Address1()
                + ",Proprietor2Address2 " + getProprietor2Address2()
                + ",Proprietor2Address3 " + getProprietor2Address3()
                + ",ProprietorName3 " + getProprietorName3()
                + ",CompanyRegistrationNo3 " + getCompanyRegistrationNo3()
                + ",ProprietorshipCategory3 " + getProprietorshipCategory3()
                + ",CountryIncorporated3 " + getCountryIncorporated3()
                + ",Proprietor3Address1 " + getProprietor3Address1()
                + ",Proprietor3Address2 " + getProprietor3Address2()
                + ",Proprietor3Address3 " + getProprietor3Address3()
                + ",ProprietorName4 " + getProprietorName4()
                + ",CompanyRegistrationNo4 " + getCompanyRegistrationNo4()
                + ",ProprietorshipCategory4 " + getProprietorshipCategory4()
                + ",CountryIncorporated4 " + getCountryIncorporated4()
                + ",Proprietor4Address1 " + getProprietor4Address1()
                + ",Proprietor4Address2 " + getProprietor4Address2()
                + ",Proprietor4Address3 " + getProprietor4Address3()
                + ",DateProprietorAdded " + getDateProprietorAdded()
                + ",AdditionalProprietorIndicator " + getAdditionalProprietorIndicator();
    }

    @Override
    public String toCSV() {
        return "\"" + getTitleNumber()
                + "\",\"" + getTenure()
                + "\",\"" + getPropertyAddress()
                + "\",\"" + getDistrict()
                + "\",\"" + getCounty()
                + "\",\"" + getRegion()
                + "\",\"" + getPostcode()
                + "\",\"" + getMultipleAddressIndicator()
                + "\",\"" + getPricePaid()
                + "\",\"" + getProprietorName1()
                + "\",\"" + getCompanyRegistrationNo1()
                + "\",\"" + getProprietorshipCategory1()
                + "\",\"" + getCountryIncorporated1()
                + "\",\"" + getProprietor1Address1()
                + "\",\"" + getProprietor1Address2()
                + "\",\"" + getProprietor1Address3()
                + "\",\"" + getProprietorName2()
                + "\",\"" + getCompanyRegistrationNo2()
                + "\",\"" + getProprietorshipCategory2()
                + "\",\"" + getCountryIncorporated2()
                + "\",\"" + getProprietor2Address1()
                + "\",\"" + getProprietor2Address2()
                + "\",\"" + getProprietor2Address3()
                + "\",\"" + getProprietorName3()
                + "\",\"" + getCompanyRegistrationNo3()
                + "\",\"" + getProprietorshipCategory3()
                + "\",\"" + getCountryIncorporated3()
                + "\",\"" + getProprietor3Address1()
                + "\",\"" + getProprietor3Address2()
                + "\",\"" + getProprietor3Address3()
                + "\",\"" + getProprietorName4()
                + "\",\"" + getCompanyRegistrationNo4()
                + "\",\"" + getProprietorshipCategory4()
                + "\",\"" + getCountryIncorporated4()
                + "\",\"" + getProprietor4Address1()
                + "\",\"" + getProprietor4Address2()
                + "\",\"" + getProprietor4Address3()
                + "\",\"" + getDateProprietorAdded()
                + "\",\"" + getAdditionalProprietorIndicator();
    }

    public static String header() {
        return "TitleNumber,Tenure,PropertyAddress,District,County,Region,"
                + "Postcode,MultipleAddressIndicator,PricePaid"
                + ",ProprietorName1"
                + ",CompanyRegistrationNo1"
                + ",ProprietorshipCategory"
                + ",CountryIncorporated1"
                + ",Proprietor1Address1"
                + ",Proprietor1Address2"
                + ",Proprietor1Address3"
                + ",ProprietorName2"
                + ",CompanyRegistrationNo2"
                + ",ProprietorshipCategory2"
                + ",CountryIncorporated2"
                + ",Proprietor2Address1"
                + ",Proprietor2Address2"
                + ",Proprietor2Address3"
                + ",ProprietorName3"
                + ",CompanyRegistrationNo3"
                + ",ProprietorshipCategory3"
                + ",CountryIncorporated3"
                + ",Proprietor3Address1"
                + ",Proprietor3Address2"
                + ",Proprietor3Address3"
                + ",ProprietorName4"
                + ",CompanyRegistrationNo4"
                + ",ProprietorshipCategory4"
                + ",CountryIncorporated4"
                + ",Proprietor4Address1"
                + ",Proprietor4Address2"
                + ",Proprietor4Address3"
                + ",DateProprietorAdded"
                + ",AdditionalProprietorIndicator";
    }

    /**
     * @return the CountryIncorporated1
     */
    @Override
    public final String getCountryIncorporated1() {
        return CountryIncorporated1;
    }

    /**
     * @return the CountryIncorporated2
     */
    public final String getCountryIncorporated2() {
        return CountryIncorporated2;
    }

    /**
     * @return the CountryIncorporated3
     */
    public final String getCountryIncorporated3() {
        return CountryIncorporated3;
    }

    /**
     * @return the CountryIncorporated4
     */
    public final String getCountryIncorporated4() {
        return CountryIncorporated4;
    }

    /**
     * If doUpdate then if s is blank then CountryIncorporated1 is set to a
     * unique number and a record is kept to look up this number from ID. If
     * !doUpdate then the unique number set previously is obtained from what is
     * stored.
     *
     * @param s CountryIncorporated1
     * @param doUpdate IFF true then collections are updated otherwise ID is set
     * from data pulled from existing collections.
     */
    public final void initCountryIncorporated1(String s, boolean doUpdate) {
        LR_TypeID typeID;
        typeID = Env.CountryIncorporatedTypeID;
        setCountryIncorporated1(s);
        update(typeID, s, doUpdate);
    }

    /**
     * @param s what CountryIncorporated1 to set
     */
    public final void setCountryIncorporated1(String s) {
        this.CountryIncorporated1 = s;
    }

    /**
     * @param s the CountryIncorporated2 to set
     * @param doUpdate
     */
    public final void setCountryIncorporated2(String s, boolean doUpdate) {
        this.CountryIncorporated2 = s;
        if (doUpdate) {
            if (!s.isEmpty()) {
                Env.addValue(Env.CountryIncorporatedTypeID, s);
            }
        }
    }

    /**
     * @param s the CountryIncorporated3 to set
     * @param doUpdate
     */
    public final void setCountryIncorporated3(String s, boolean doUpdate) {
        this.CountryIncorporated3 = s;
        if (doUpdate) {
            if (!s.isEmpty()) {
                Env.addValue(Env.CountryIncorporatedTypeID, s);
            }
        }
    }

    /**
     * @param s the CountryIncorporated4 to set
     * @param doUpdate
     */
    public final void setCountryIncorporated4(String s, boolean doUpdate) {
        this.CountryIncorporated4 = s;
        if (doUpdate) {
            if (!s.isEmpty()) {
                Env.addValue(Env.CountryIncorporatedTypeID, s);
            }
        }
    }

    /**
     * @return the CountryIncorporated1ID
     */
    @Override
    public final LR_ValueID getCountryIncorporated1ID() {
        return Env.ValueReverseLookups.get(Env.CountryIncorporatedTypeID).get(getCountryIncorporated1());
    }

}
