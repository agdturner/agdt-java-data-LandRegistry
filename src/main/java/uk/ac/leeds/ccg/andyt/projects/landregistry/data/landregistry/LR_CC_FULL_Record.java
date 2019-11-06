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

import uk.ac.leeds.ccg.andyt.generic.time.Generic_YearMonth;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Environment;
import uk.ac.leeds.ccg.andyt.projects.landregistry.data.id.LR_RecordID;

/**
 *
 * @author geoagdt
 */
public class LR_CC_FULL_Record extends LR_Record {

    public LR_CC_FULL_Record(LR_Environment e, LR_RecordID i){
        super(e, i);
    }
    
    /**
     *
     * @param e
     * @param i
     * @param YM
     * @param line
     * @param doUpdate
     * @throws Exception
     */
    public LR_CC_FULL_Record(LR_Environment e, LR_RecordID i, 
            Generic_YearMonth YM, String line, boolean doUpdate) 
            throws Exception {
        super(e, i);
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
        setProprietor1Address1(ls[12]);
        setProprietor1Address2(ls[13]);
        setProprietor1Address3(ls[14]);
        setProprietorName2(ls[15], doUpdate);
        setCompanyRegistrationNo2(ls[16], doUpdate);
        setProprietorshipCategory2(ls[17], doUpdate);
        setProprietor2Address1(ls[18]);
        setProprietor2Address2(ls[19]);
        setProprietor2Address3(ls[20]);
        setProprietorName3(ls[21], doUpdate);
        setCompanyRegistrationNo3(ls[22], doUpdate);
        setProprietorshipCategory3(ls[23], doUpdate);
        setProprietor3Address1(ls[24]);
        setProprietor3Address2(ls[25]);
        setProprietor3Address3(ls[26]);
        setProprietorName4(ls[27], doUpdate);
        setCompanyRegistrationNo4(ls[28], doUpdate);
        setProprietorshipCategory4(ls[29], doUpdate);
        setProprietor4Address1(ls[30]);
        setProprietor4Address2(ls[31]);
        setProprietor4Address3(ls[32]);
        setDateProprietorAdded(ls[33]);
        setAdditionalProprietorIndicator(ls[34]);
    }

    /**
     * Creates a simple copy of r without changing any collections.
     *
     * @param r
     * @param doUpdate
     */
    public LR_CC_FULL_Record(LR_Record r, boolean doUpdate) {
        super(r, doUpdate);
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
                + ",Proprietor1Address1 " + getProprietor1Address1()
                + ",Proprietor1Address2 " + getProprietor1Address2()
                + ",Proprietor1Address3 " + getProprietor1Address3()
                + ",ProprietorName2 " + getProprietorName2()
                + ",CompanyRegistrationNo2 " + getCompanyRegistrationNo2()
                + ",ProprietorshipCategory2 " + getProprietorshipCategory2()
                + ",Proprietor2Address1 " + getProprietor2Address1()
                + ",Proprietor2Address2 " + getProprietor2Address2()
                + ",Proprietor2Address3 " + getProprietor2Address3()
                + ",ProprietorName3 " + getProprietorName3()
                + ",CompanyRegistrationNo3 " + getCompanyRegistrationNo3()
                + ",ProprietorshipCategory3 " + getProprietorshipCategory3()
                + ",Proprietor3Address1 " + getProprietor3Address1()
                + ",Proprietor3Address2 " + getProprietor3Address2()
                + ",Proprietor3Address3 " + getProprietor3Address3()
                + ",ProprietorName4 " + getProprietorName4()
                + ",CompanyRegistrationNo4 " + getCompanyRegistrationNo4()
                + ",ProprietorshipCategory4 " + getProprietorshipCategory4()
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
                + "\",\"" + getProprietor1Address1()
                + "\",\"" + getProprietor1Address2()
                + "\",\"" + getProprietor1Address3()
                + "\",\"" + getProprietorName2()
                + "\",\"" + getCompanyRegistrationNo2()
                + "\",\"" + getProprietorshipCategory2()
                + "\",\"" + getProprietor2Address1()
                + "\",\"" + getProprietor2Address2()
                + "\",\"" + getProprietor2Address3()
                + "\",\"" + getProprietorName3()
                + "\",\"" + getCompanyRegistrationNo3()
                + "\",\"" + getProprietorshipCategory3()
                + "\",\"" + getProprietor3Address1()
                + "\",\"" + getProprietor3Address2()
                + "\",\"" + getProprietor3Address3()
                + "\",\"" + getProprietorName4()
                + "\",\"" + getCompanyRegistrationNo4()
                + "\",\"" + getProprietorshipCategory4()
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
                + ",ProprietorshipCategory1"
                + ",Proprietor1Address1"
                + ",Proprietor1Address2"
                + ",Proprietor1Address3"
                + ",ProprietorName2"
                + ",CompanyRegistrationNo2"
                + ",ProprietorshipCategory2"
                + ",Proprietor2Address1"
                + ",Proprietor2Address2"
                + ",Proprietor2Address3"
                + ",ProprietorName3"
                + ",CompanyRegistrationNo3"
                + ",ProprietorshipCategory3"
                + ",Proprietor3Address1"
                + ",Proprietor3Address2"
                + ",Proprietor3Address3"
                + ",ProprietorName4"
                + ",CompanyRegistrationNo4"
                + ",ProprietorshipCategory4"
                + ",Proprietor4Address1"
                + ",Proprietor4Address2"
                + ",Proprietor4Address3"
                + ",DateProprietorAdded"
                + ",AdditionalProprietorIndicator";
    }

    @Override
    public LR_RecordID getID() {
        return (LR_RecordID) ID;
    }

}
