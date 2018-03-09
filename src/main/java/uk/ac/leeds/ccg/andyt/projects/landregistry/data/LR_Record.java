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
package uk.ac.leeds.ccg.andyt.projects.landregistry.data;

/**
 *
 * @author geoagdt
 */
public abstract class LR_Record {

    protected long ID;
    protected String TitleNumber;
    protected String Tenure;
    protected String PropertyAddress;
    protected String District;
    protected String Region;
    protected String Postcode;
    protected String MultipleAddressIndicator;
    protected String PricePaid;
    protected String CompanyRegistrationNo1;
    protected String County;
    protected String ProprietorName1;
    protected String ProprietorshipCategory1;
    protected String AdditionalProprietorIndicator;
    protected String CompanyRegistrationNo2;
    protected String CompanyRegistrationNo3;
    protected String CompanyRegistrationNo4;
    protected String DateProprietorAdded;
    protected String Proprietor1Address1;
    protected String Proprietor1Address2;
    protected String Proprietor1Address3;
    protected String Proprietor2Address1;
    protected String Proprietor2Address2;
    protected String Proprietor2Address3;
    protected String Proprietor3Address1;
    protected String Proprietor3Address2;
    protected String Proprietor3Address3;
    protected String Proprietor4Address1;
    protected String Proprietor4Address2;
    protected String Proprietor4Address3;
    protected String ProprietorName2;
    protected String ProprietorName3;
    protected String ProprietorName4;
    protected String ProprietorshipCategory2;
    protected String ProprietorshipCategory3;
    protected String ProprietorshipCategory4;

    public LR_Record() {
    }

    public abstract String toCSV();

    /**
     * @return the ID
     */
    public long getID() {
        return ID;
    }

    /**
     * @return the TitleNumber
     */
    public String getTitleNumber() {
        return TitleNumber;
    }

    /**
     * @return the Tenure
     */
    public String getTenure() {
        return Tenure;
    }

    /**
     * @return the District
     */
    public String getDistrict() {
        return District;
    }

    /**
     * @return the Region
     */
    public String getRegion() {
        return Region;
    }

    /**
     * @return the Postcode
     */
    public String getPostcode() {
        return Postcode;
    }

    /**
     * @return the PricePaid
     */
    public String getPricePaid() {
        return PricePaid;
    }

    /**
     * @return the CompanyRegistrationNo1
     */
    public String getCompanyRegistrationNo1() {
        return CompanyRegistrationNo1;
    }

    /**
     * @return the County
     */
    public String getCounty() {
        return County;
    }

    /**
     * @return the ProprietorName1
     */
    public String getProprietorName1() {
        return ProprietorName1;
    }

    /**
     * @return the ProprietorshipCategory1
     */
    public String getProprietorshipCategory1() {
        return ProprietorshipCategory1;
    }

    /**
     * @return the AdditionalProprietorIndicator
     */
    public String getAdditionalProprietorIndicator() {
        return AdditionalProprietorIndicator;
    }

    /**
     * @return the CompanyRegistrationNo2
     */
    public String getCompanyRegistrationNo2() {
        return CompanyRegistrationNo2;
    }

    /**
     * @return the CompanyRegistrationNo3
     */
    public String getCompanyRegistrationNo3() {
        return CompanyRegistrationNo3;
    }

    /**
     * @return the CompanyRegistrationNo4
     */
    public String getCompanyRegistrationNo4() {
        return CompanyRegistrationNo4;
    }

    /**
     * @return the DateProprietorAdded
     */
    public String getDateProprietorAdded() {
        return DateProprietorAdded;
    }

    /**
     * @return the MultipleAddressIndicator
     */
    public String getMultipleAddressIndicator() {
        return MultipleAddressIndicator;
    }

    /**
     * @return the PropertyAddress
     */
    public String getPropertyAddress() {
        return PropertyAddress;
    }

    /**
     * @return the Proprietor1Address1
     */
    public String getProprietor1Address1() {
        return Proprietor1Address1;
    }

    /**
     * @return the Proprietor1Address2
     */
    public String getProprietor1Address2() {
        return Proprietor1Address2;
    }

    /**
     * @return the Proprietor1Address3
     */
    public String getProprietor1Address3() {
        return Proprietor1Address3;
    }

    /**
     * @return the Proprietor2Address1
     */
    public String getProprietor2Address1() {
        return Proprietor2Address1;
    }

    /**
     * @return the Proprietor2Address2
     */
    public String getProprietor2Address2() {
        return Proprietor2Address2;
    }

    /**
     * @return the Proprietor2Address3
     */
    public String getProprietor2Address3() {
        return Proprietor2Address3;
    }

    /**
     * @return the Proprietor3Address1
     */
    public String getProprietor3Address1() {
        return Proprietor3Address1;
    }

    /**
     * @return the Proprietor3Address2
     */
    public String getProprietor3Address2() {
        return Proprietor3Address2;
    }

    /**
     * @return the Proprietor3Address3
     */
    public String getProprietor3Address3() {
        return Proprietor3Address3;
    }

    /**
     * @return the Proprietor4Address1
     */
    public String getProprietor4Address1() {
        return Proprietor4Address1;
    }

    /**
     * @return the Proprietor4Address2
     */
    public String getProprietor4Address2() {
        return Proprietor4Address2;
    }

    /**
     * @return the Proprietor4Address3
     */
    public String getProprietor4Address3() {
        return Proprietor4Address3;
    }

    /**
     * @return the ProprietorName2
     */
    public String getProprietorName2() {
        return ProprietorName2;
    }

    /**
     * @return the ProprietorName3
     */
    public String getProprietorName3() {
        return ProprietorName3;
    }

    /**
     * @return the ProprietorName4
     */
    public String getProprietorName4() {
        return ProprietorName4;
    }

    /**
     * @return the ProprietorshipCategory2
     */
    public String getProprietorshipCategory2() {
        return ProprietorshipCategory2;
    }

    /**
     * @return the ProprietorshipCategory3
     */
    public String getProprietorshipCategory3() {
        return ProprietorshipCategory3;
    }

    /**
     * @return the ProprietorshipCategory4
     */
    public String getProprietorshipCategory4() {
        return ProprietorshipCategory4;
    }

    /**
     * @return the CountryIncorporated1
     */
    public String getCountryIncorporated1() {
        return "United Kingdom";
    }
}
