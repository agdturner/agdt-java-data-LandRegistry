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
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Environment;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_ID;

/**
 *
 * @author Andy Turner
 */
public class LR_OC_FULL_Record extends LR_CC_FULL_Record implements Serializable {

    protected LR_ID CountryIncorporated1ID;
    protected LR_ID CountryIncorporated2ID;
    protected LR_ID CountryIncorporated3ID;
    protected LR_ID CountryIncorporated4ID;
    private String CountryIncorporated1;
    private String CountryIncorporated2;
    private String CountryIncorporated3;
    private String CountryIncorporated4;

    public LR_OC_FULL_Record() {
    }

    public LR_OC_FULL_Record(String line) {
        String[] ls;
        ls = line.split("\",\"");
        setTitleNumber(ls[0].substring(1));
        setTenure(ls[1]);
        setPropertyAddress(ls[2]);
        setDistrict(ls[3]);
        setCounty(ls[4]);
        setRegion(ls[5]);
        setPostcode(ls[6]);
        setMultipleAddressIndicator(ls[7]);
        setPricePaid(ls[8]);
        setProprietorName1(ls[9]);
        setCompanyRegistrationNo1(ls[10]);
        setProprietorshipCategory1(ls[11]);
        setCountryIncorporated1(ls[12]);
        setProprietor1Address1(ls[13]);
        setProprietor1Address2(ls[14]);
        setProprietor1Address3(ls[15]);
        setProprietorName2(ls[16]);
        setCompanyRegistrationNo2(ls[17]);
        setProprietorshipCategory2(ls[18]);
        setCountryIncorporated2(ls[19]);
        setProprietor2Address1(ls[20]);
        setProprietor2Address2(ls[21]);
        setProprietor2Address3(ls[22]);
        setProprietorName3(ls[23]);
        setCompanyRegistrationNo3(ls[24]);
        setProprietorshipCategory3(ls[25]);
        setCountryIncorporated3(ls[26]);
        setProprietor3Address1(ls[27]);
        setProprietor3Address2(ls[28]);
        setProprietor3Address3(ls[29]);
        setProprietorName4(ls[30]);
        setCompanyRegistrationNo4(ls[31]);
        setProprietorshipCategory4(ls[32]);
        setCountryIncorporated4(ls[33]);
        setProprietor4Address1(ls[34]);
        setProprietor4Address2(ls[35]);
        setProprietor4Address3(ls[36]);
        setDateProprietorAdded(ls[37]);
        setAdditionalProprietorIndicator(ls[38]);
    }

    public LR_OC_FULL_Record(LR_Environment env, String line) {
        this(line);
        Env = env;
        updateIDs();
        String s;
        s = getCountryIncorporated1();
        updateCountryIncorporatedLookups(1, s);
        s = getCountryIncorporated2();
        updateCountryIncorporatedLookups(2, s);
        s = getCountryIncorporated3();
        updateCountryIncorporatedLookups(3, s);
        s = getCountryIncorporated4();
        updateCountryIncorporatedLookups(4, s);
    }
    
    public LR_OC_FULL_Record(LR_OC_FULL_Record r) {
        super(r);
        setCountryIncorporated1(r.getCountryIncorporated1());
        setCountryIncorporated2(r.getCountryIncorporated2());
        setCountryIncorporated3(r.getCountryIncorporated3());
        setCountryIncorporated4(r.getCountryIncorporated4());
    }

    /**
     *
     * @param i Either 1, 2, 3, or 4.
     * @param s A potentially new ProprietorName.
     */
    public final void updateCountryIncorporatedLookups(int i, String s) {
        if (Env.CountryIncorporatedToID.containsKey(s)) {
            switch (i) {
                case 1:
                    CountryIncorporated1ID = Env.CountryIncorporatedToID.get(s);
                    break;
                case 2:
                    CountryIncorporated2ID = Env.CountryIncorporatedToID.get(s);
                    break;
                case 3:
                    CountryIncorporated3ID = Env.CountryIncorporatedToID.get(s);
                    break;
                default:
                    CountryIncorporated4ID = Env.CountryIncorporatedToID.get(s);
                    break;
            }
        } else {
            switch (i) {
                case 1:
                    CountryIncorporated1ID = new LR_ID(Env.CountryIncorporatedToID.size());
                    Env.CountryIncorporatedToID.put(s, CountryIncorporated1ID);
                    Env.IDToCountryIncorporated.put(CountryIncorporated1ID, s);
                    break;
                case 2:
                    CountryIncorporated2ID = new LR_ID(Env.CountryIncorporatedToID.size());
                    Env.CountryIncorporatedToID.put(s, CountryIncorporated2ID);
                    Env.IDToCountryIncorporated.put(CountryIncorporated2ID, s);
                    break;
                case 3:
                    CountryIncorporated3ID = new LR_ID(Env.CountryIncorporatedToID.size());
                    Env.CountryIncorporatedToID.put(s, CountryIncorporated3ID);
                    Env.IDToCountryIncorporated.put(CountryIncorporated3ID, s);
                    break;
                default:
                    CountryIncorporated4ID = new LR_ID(Env.CountryIncorporatedToID.size());
                    Env.CountryIncorporatedToID.put(s, CountryIncorporated4ID);
                    Env.IDToCountryIncorporated.put(CountryIncorporated4ID, s);
                    break;
            }
        }
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
     * @param CountryIncorporated1 the CountryIncorporated1 to set
     */
    public final void setCountryIncorporated1(String CountryIncorporated1) {
        this.CountryIncorporated1 = CountryIncorporated1;
    }

    /**
     * @param CountryIncorporated2 the CountryIncorporated2 to set
     */
    public final void setCountryIncorporated2(String CountryIncorporated2) {
        this.CountryIncorporated2 = CountryIncorporated2;
    }

    /**
     * @param CountryIncorporated3 the CountryIncorporated3 to set
     */
    public final void setCountryIncorporated3(String CountryIncorporated3) {
        this.CountryIncorporated3 = CountryIncorporated3;
    }

    /**
     * @param CountryIncorporated4 the CountryIncorporated4 to set
     */
    public final void setCountryIncorporated4(String CountryIncorporated4) {
        this.CountryIncorporated4 = CountryIncorporated4;
    }

    /**
     * @return the CountryIncorporated1ID
     */
    public final LR_ID getCountryIncorporated1ID() {
        return CountryIncorporated1ID;
    }

}
