
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

import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_ID;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_ID2;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Object;

/**
 *
 * @author geoagdt
 */
public abstract class LR_Record extends LR_Object {

    protected LR_ID2 ID;
    protected LR_ID TitleNumberID;
    protected LR_ID TenureID;
    protected LR_ID PropertyAddressID;
    protected LR_ID ProprietorName1ID;
    protected LR_ID ProprietorName2ID;
    protected LR_ID ProprietorName3ID;
    protected LR_ID ProprietorName4ID;
    protected LR_ID CompanyRegistrationNo1ID;
    protected LR_ID CompanyRegistrationNo2ID;
    protected LR_ID CompanyRegistrationNo3ID;
    protected LR_ID CompanyRegistrationNo4ID;
    protected LR_ID ProprietorshipCategory1ID;
    protected LR_ID ProprietorshipCategory2ID;
    protected LR_ID ProprietorshipCategory3ID;
    protected LR_ID ProprietorshipCategory4ID;
    private String TitleNumber;
    private String Tenure;
    private String PropertyAddress;
    private String District;
    private String Region;
    private String Postcode;
    private String MultipleAddressIndicator;
    private String PricePaid;
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

    public LR_Record() {
    }

    public LR_Record(LR_Record r) {
        Env = r.Env;
        ID = r.ID;
        setTitleNumber(r.getTitleNumber());
        setTenure(r.getTenure());
        setPropertyAddress(r.getPropertyAddress());
        setDistrict(r.getDistrict());
        setCounty(r.getCounty());
        setRegion(r.getRegion());
        setPostcode(r.getPostcode());
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

    public abstract String toCSV();

    /**
     * @return the ID
     */
    public final LR_ID2 getID() {
        return ID;
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
        return PropertyAddressID;
    }

    /**
     * @return the ProprietorName1ID
     */
    public final LR_ID getProprietorName1ID() {
        return ProprietorName1ID;
    }

    /**
     * @return the ProprietorName2ID
     */
    public final LR_ID getProprietorName2ID() {
        return ProprietorName2ID;
    }

    /**
     * @return the ProprietorName3ID
     */
    public final LR_ID getProprietorName3ID() {
        return ProprietorName3ID;
    }

    /**
     * @return the ProprietorName4ID
     */
    public final LR_ID getProprietorName4ID() {
        return ProprietorName4ID;
    }

    /**
     * @return the CompanyRegistrationNo1ID
     */
    public final LR_ID getCompanyRegistrationNo1ID() {
        return CompanyRegistrationNo1ID;
    }

    /**
     * @return the CompanyRegistrationNo2ID
     */
    public final LR_ID getCompanyRegistrationNo2ID() {
        return CompanyRegistrationNo2ID;
    }

    /**
     * @return the CompanyRegistrationNo3ID
     */
    public final LR_ID getCompanyRegistrationNo3ID() {
        return CompanyRegistrationNo3ID;
    }

    /**
     * @return the CompanyRegistrationNo4ID
     */
    public final LR_ID getCompanyRegistrationNo4ID() {
        return CompanyRegistrationNo4ID;
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
        return TenureID;
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
     * @return the PricePaid
     */
    public final String getPricePaid() {
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
        return ProprietorshipCategory1ID;
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
        return ProprietorshipCategory2ID;
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
        return ProprietorshipCategory3ID;
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
        return ProprietorshipCategory4ID;
    }

    /**
     * @return the CountryIncorporated1
     */
    public String getCountryIncorporated1() {
        return "United Kingdom";
    }
    
    /**
     * @return the CountryIncorporated1ID
     */
    public LR_ID getCountryIncorporated1ID() {
        return Env.CountryIncorporatedToID.get(getCountryIncorporated1());
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
     * @param PropertyAddress the PropertyAddress to set
     */
    public final void setPropertyAddress(String PropertyAddress) {
        this.PropertyAddress = PropertyAddress;
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
    public final void setPostcode(String Postcode) {
        this.Postcode = Postcode;
    }

    /**
     * @param MultipleAddressIndicator the MultipleAddressIndicator to set
     */
    public final void setMultipleAddressIndicator(String MultipleAddressIndicator) {
        this.MultipleAddressIndicator = MultipleAddressIndicator;
    }

    /**
     * @param PricePaid the PricePaid to set
     */
    public final void setPricePaid(String PricePaid) {
        this.PricePaid = PricePaid;
    }

    /**
     * @param CompanyRegistrationNo1 the CompanyRegistrationNo1 to set
     */
    public final void setCompanyRegistrationNo1(String CompanyRegistrationNo1) {
        if (CompanyRegistrationNo1.startsWith("0")) {
            do {
                CompanyRegistrationNo1 = CompanyRegistrationNo1.substring(1);
            } while (CompanyRegistrationNo1.startsWith("0"));
        }
        this.CompanyRegistrationNo1 = CompanyRegistrationNo1;
    }

    /**
     * @param County the County to set
     */
    public final void setCounty(String County) {
        this.County = County;
    }

    /**
     * @param ProprietorName1 the ProprietorName1 to set
     */
    public final void setProprietorName1(String ProprietorName1) {
        this.ProprietorName1 = ProprietorName1;
    }

    /**
     * @param ProprietorshipCategory1 the ProprietorshipCategory1 to set
     */
    public final void setProprietorshipCategory1(String ProprietorshipCategory1) {
        this.ProprietorshipCategory1 = ProprietorshipCategory1;
    }

    /**
     * @param AdditionalProprietorIndicator the AdditionalProprietorIndicator to
     * set
     */
    public final void setAdditionalProprietorIndicator(String AdditionalProprietorIndicator) {
        this.AdditionalProprietorIndicator = AdditionalProprietorIndicator;
    }

    /**
     * @param CompanyRegistrationNo2 the CompanyRegistrationNo2 to set
     */
    public final void setCompanyRegistrationNo2(String CompanyRegistrationNo2) {
        this.CompanyRegistrationNo2 = CompanyRegistrationNo2;
    }

    /**
     * @param CompanyRegistrationNo3 the CompanyRegistrationNo3 to set
     */
    public final void setCompanyRegistrationNo3(String CompanyRegistrationNo3) {
        this.CompanyRegistrationNo3 = CompanyRegistrationNo3;
    }

    /**
     * @param CompanyRegistrationNo4 the CompanyRegistrationNo4 to set
     */
    public final void setCompanyRegistrationNo4(String CompanyRegistrationNo4) {
        this.CompanyRegistrationNo4 = CompanyRegistrationNo4;
    }

    /**
     * @param DateProprietorAdded the DateProprietorAdded to set
     */
    public final void setDateProprietorAdded(String DateProprietorAdded) {
        this.DateProprietorAdded = DateProprietorAdded;
    }

    /**
     * @param Proprietor1Address1 the Proprietor1Address1 to set
     */
    public final void setProprietor1Address1(String Proprietor1Address1) {
        this.Proprietor1Address1 = Proprietor1Address1;
    }

    /**
     * @param Proprietor1Address2 the Proprietor1Address2 to set
     */
    public final void setProprietor1Address2(String Proprietor1Address2) {
        this.Proprietor1Address2 = Proprietor1Address2;
    }

    /**
     * @param Proprietor1Address3 the Proprietor1Address3 to set
     */
    public final void setProprietor1Address3(String Proprietor1Address3) {
        this.Proprietor1Address3 = Proprietor1Address3;
    }

    /**
     * @param Proprietor2Address1 the Proprietor2Address1 to set
     */
    public final void setProprietor2Address1(String Proprietor2Address1) {
        this.Proprietor2Address1 = Proprietor2Address1;
    }

    /**
     * @param Proprietor2Address2 the Proprietor2Address2 to set
     */
    public final void setProprietor2Address2(String Proprietor2Address2) {
        this.Proprietor2Address2 = Proprietor2Address2;
    }

    /**
     * @param Proprietor2Address3 the Proprietor2Address3 to set
     */
    public final void setProprietor2Address3(String Proprietor2Address3) {
        this.Proprietor2Address3 = Proprietor2Address3;
    }

    /**
     * @param Proprietor3Address1 the Proprietor3Address1 to set
     */
    public final void setProprietor3Address1(String Proprietor3Address1) {
        this.Proprietor3Address1 = Proprietor3Address1;
    }

    /**
     * @param Proprietor3Address2 the Proprietor3Address2 to set
     */
    public final void setProprietor3Address2(String Proprietor3Address2) {
        this.Proprietor3Address2 = Proprietor3Address2;
    }

    /**
     * @param Proprietor3Address3 the Proprietor3Address3 to set
     */
    public final void setProprietor3Address3(String Proprietor3Address3) {
        this.Proprietor3Address3 = Proprietor3Address3;
    }

    /**
     * @param Proprietor4Address1 the Proprietor4Address1 to set
     */
    public final void setProprietor4Address1(String Proprietor4Address1) {
        this.Proprietor4Address1 = Proprietor4Address1;
    }

    /**
     * @param Proprietor4Address2 the Proprietor4Address2 to set
     */
    public final void setProprietor4Address2(String Proprietor4Address2) {
        this.Proprietor4Address2 = Proprietor4Address2;
    }

    /**
     * @param Proprietor4Address3 the Proprietor4Address3 to set
     */
    public final void setProprietor4Address3(String Proprietor4Address3) {
        this.Proprietor4Address3 = Proprietor4Address3;
    }

    /**
     * @param ProprietorName2 the ProprietorName2 to set
     */
    public final void setProprietorName2(String ProprietorName2) {
        this.ProprietorName2 = ProprietorName2;
    }

    /**
     * @param ProprietorName3 the ProprietorName3 to set
     */
    public final void setProprietorName3(String ProprietorName3) {
        this.ProprietorName3 = ProprietorName3;
    }

    /**
     * @param ProprietorName4 the ProprietorName4 to set
     */
    public final void setProprietorName4(String ProprietorName4) {
        this.ProprietorName4 = ProprietorName4;
    }

    /**
     * @param ProprietorshipCategory2 the ProprietorshipCategory2 to set
     */
    public final void setProprietorshipCategory2(String ProprietorshipCategory2) {
        this.ProprietorshipCategory2 = ProprietorshipCategory2;
    }

    /**
     * @param ProprietorshipCategory3 the ProprietorshipCategory3 to set
     */
    public final void setProprietorshipCategory3(String ProprietorshipCategory3) {
        this.ProprietorshipCategory3 = ProprietorshipCategory3;
    }

    /**
     * @param ProprietorshipCategory4 the ProprietorshipCategory4 to set
     */
    public final void setProprietorshipCategory4(String ProprietorshipCategory4) {
        this.ProprietorshipCategory4 = ProprietorshipCategory4;
    }
}
