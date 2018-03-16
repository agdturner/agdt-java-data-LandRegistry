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

/**
 *
 * @author geoagdt
 */
public class LR_OC_FULL_Record extends LR_CC_FULL_Record implements Serializable {

    private String CountryIncorporated1;
    private String CountryIncorporated2;
    private String CountryIncorporated3;
    private String CountryIncorporated4;

    public LR_OC_FULL_Record() {
    }

    public LR_OC_FULL_Record(long ID, String line) {
        this.ID = ID;
        String[] ls;
        ls = line.split("\",\"");
        TitleNumber = ls[0].substring(1);
        Tenure = ls[1];
        PropertyAddress = ls[2];
        District = ls[3];
        County = ls[4];
        Region = ls[5];
        Postcode = ls[6];
        MultipleAddressIndicator = ls[7];
        PricePaid = ls[8];
        ProprietorName1 = ls[9];
        CompanyRegistrationNo1 = ls[10];
        ProprietorshipCategory1 = ls[11];
        CountryIncorporated1 = ls[12];
        Proprietor1Address1 = ls[13];
        Proprietor1Address2 = ls[14];
        Proprietor1Address3 = ls[15];
        ProprietorName2 = ls[16];
        CompanyRegistrationNo2 = ls[17];
        ProprietorshipCategory2 = ls[18];
        CountryIncorporated2 = ls[19];
        Proprietor2Address1 = ls[20];
        Proprietor2Address2 = ls[21];
        Proprietor2Address3 = ls[22];
        ProprietorName3 = ls[23];
        CompanyRegistrationNo3 = ls[24];
        ProprietorshipCategory3 = ls[25];
        CountryIncorporated3 = ls[26];
        Proprietor3Address1 = ls[27];
        Proprietor3Address2 = ls[28];
        Proprietor3Address3 = ls[29];
        ProprietorName4 = ls[30];
        CompanyRegistrationNo4 = ls[31];
        ProprietorshipCategory4 = ls[32];
        CountryIncorporated4 = ls[33];
        Proprietor4Address1 = ls[34];
        Proprietor4Address2 = ls[35];
        Proprietor4Address3 = ls[36];
        DateProprietorAdded = ls[37];
        AdditionalProprietorIndicator = ls[38];
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
    public String getCountryIncorporated1() {
        return CountryIncorporated1;
    }

    /**
     * @return the CountryIncorporated2
     */
    public String getCountryIncorporated2() {
        return CountryIncorporated2;
    }

    /**
     * @return the CountryIncorporated3
     */
    public String getCountryIncorporated3() {
        return CountryIncorporated3;
    }

    /**
     * @return the CountryIncorporated4
     */
    public String getCountryIncorporated4() {
        return CountryIncorporated4;
    }
}
