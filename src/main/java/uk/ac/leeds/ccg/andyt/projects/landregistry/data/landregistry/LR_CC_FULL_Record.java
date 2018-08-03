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
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_ID2;

/**
 *
 * @author geoagdt
 */
public class LR_CC_FULL_Record extends LR_Record implements Serializable {

    public LR_CC_FULL_Record() {
    }

    public LR_CC_FULL_Record(String line) {
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
        setProprietor1Address1(ls[12]);
        setProprietor1Address2(ls[13]);
        setProprietor1Address3(ls[14]);
        setProprietorName2(ls[15]);
        setCompanyRegistrationNo2(ls[16]);
        setProprietorshipCategory2(ls[17]);
        setProprietor2Address1(ls[18]);
        setProprietor2Address2(ls[19]);
        setProprietor2Address3(ls[20]);
        setProprietorName3(ls[21]);
        setCompanyRegistrationNo3(ls[22]);
        setProprietorshipCategory3(ls[23]);
        setProprietor3Address1(ls[24]);
        setProprietor3Address2(ls[25]);
        setProprietor3Address3(ls[26]);
        setProprietorName4(ls[27]);
        setCompanyRegistrationNo4(ls[28]);
        setProprietorshipCategory4(ls[29]);
        setProprietor4Address1(ls[30]);
        setProprietor4Address2(ls[31]);
        setProprietor4Address3(ls[32]);
        setDateProprietorAdded(ls[33]);
        setAdditionalProprietorIndicator(ls[34]);
    }

    public LR_CC_FULL_Record(LR_Environment env, String line) {
        this(line);
        Env = env;
        updateIDs();
    }
    
    public LR_CC_FULL_Record(LR_Record r) {
        super(r);
    }

    protected final void updateIDs() {
        String s;
        s = getPropertyAddress();
        if (Env.PropertyAddressToID.containsKey(s)) {
            PropertyAddressID = Env.PropertyAddressToID.get(s);
        } else {
            PropertyAddressID = new LR_ID(Env.PropertyAddressToID.size());
            Env.PropertyAddressToID.put(s, PropertyAddressID);
            Env.IDToPropertyAddress.put(PropertyAddressID, s);
            Env.updatedPropertyAddressLookups = true;
        }
        s = getTitleNumber();
        if (Env.TitleNumberToID.containsKey(s)) {
            TitleNumberID = Env.TitleNumberToID.get(s);
        } else {
            TitleNumberID = new LR_ID(Env.TitleNumberToID.size());
            Env.TitleNumberToID.put(s, TitleNumberID);
            Env.IDToTitleNumber.put(TitleNumberID, s);
            Env.updatedTitleNumberLookups = true;
        }
        ID = new LR_ID2(TitleNumberID, PropertyAddressID);
        // ProprietorName
        s = getProprietorName1();
        updateProprietorNameLookups(1, s);
        s = getProprietorName2();
        updateProprietorNameLookups(2, s);
        s = getProprietorName3();
        updateProprietorNameLookups(3, s);
        s = getProprietorName4();
        updateProprietorNameLookups(4, s);
        // CompanyRegistrationNo
        s = getCompanyRegistrationNo1();
        updateCompanyRegistrationNoLookups(1, s);
        s = getCompanyRegistrationNo2();
        updateCompanyRegistrationNoLookups(2, s);
        s = getCompanyRegistrationNo3();
        updateCompanyRegistrationNoLookups(3, s);
        s = getCompanyRegistrationNo4();
        updateCompanyRegistrationNoLookups(4, s);
    }

    /**
     * @param i 1, 2, 3 or 4.
     * @param s A potentially new ProprietorName.
     */
    public final void updateProprietorNameLookups(int i, String s) {
        if (Env.ProprietorNameToID.containsKey(s)) {
            switch (i) {
                case 1:
                    ProprietorName1ID = Env.ProprietorNameToID.get(s);
                    break;
                case 2:
                    ProprietorName2ID = Env.ProprietorNameToID.get(s);
                    break;
                case 3:
                    ProprietorName3ID = Env.ProprietorNameToID.get(s);
                    break;
                default:
                    ProprietorName4ID = Env.ProprietorNameToID.get(s);
                    break;
            }
        } else {
            switch (i) {
                case 1:
                    ProprietorName1ID = new LR_ID(Env.ProprietorNameToID.size());
                    Env.ProprietorNameToID.put(s, ProprietorName1ID);
                    Env.IDToProprietorName.put(ProprietorName1ID, s);
                    break;
                case 2:
                    ProprietorName2ID = new LR_ID(Env.ProprietorNameToID.size());
                    Env.ProprietorNameToID.put(s, ProprietorName2ID);
                    Env.IDToProprietorName.put(ProprietorName2ID, s);
                    break;
                case 3:
                    ProprietorName3ID = new LR_ID(Env.ProprietorNameToID.size());
                    Env.ProprietorNameToID.put(s, ProprietorName3ID);
                    Env.IDToProprietorName.put(ProprietorName3ID, s);
                    break;
                default:
                    ProprietorName4ID = new LR_ID(Env.ProprietorNameToID.size());
                    Env.ProprietorNameToID.put(s, ProprietorName4ID);
                    Env.IDToProprietorName.put(ProprietorName4ID, s);
                    break;
            }
            Env.updatedProprietorNameLookups = true;
        }
    }

    /**
     * @param i 1, 2, 3 or 4.
     * @param s A potentially new CompanyRegistrationNo.
     */
    public final void updateCompanyRegistrationNoLookups(int i, String s) {
        if (Env.CompanyRegistrationNoToID.containsKey(s)) {
            switch (i) {
                case 1:
                    CompanyRegistrationNo1ID = Env.CompanyRegistrationNoToID.get(s);
                    break;
                case 2:
                    CompanyRegistrationNo2ID = Env.CompanyRegistrationNoToID.get(s);
                    break;
                case 3:
                    CompanyRegistrationNo3ID = Env.CompanyRegistrationNoToID.get(s);
                    break;
                default:
                    CompanyRegistrationNo4ID = Env.CompanyRegistrationNoToID.get(s);
                    break;
            }
        } else {
            switch (i) {
                case 1:
                    CompanyRegistrationNo1ID = new LR_ID(Env.CompanyRegistrationNoToID.size());
                    Env.CountryIncorporatedToID.put(s, CompanyRegistrationNo1ID);
                    Env.IDToCountryIncorporated.put(CompanyRegistrationNo1ID, s);
                    break;
                case 2:
                    CompanyRegistrationNo2ID = new LR_ID(Env.CompanyRegistrationNoToID.size());
                    Env.CountryIncorporatedToID.put(s, CompanyRegistrationNo2ID);
                    Env.IDToCountryIncorporated.put(CompanyRegistrationNo2ID, s);
                    break;
                case 3:
                    CompanyRegistrationNo3ID = new LR_ID(Env.CompanyRegistrationNoToID.size());
                    Env.CountryIncorporatedToID.put(s, CompanyRegistrationNo3ID);
                    Env.IDToCountryIncorporated.put(CompanyRegistrationNo3ID, s);
                    break;
                default:
                    CompanyRegistrationNo4ID = new LR_ID(Env.CompanyRegistrationNoToID.size());
                    Env.CountryIncorporatedToID.put(s, CompanyRegistrationNo4ID);
                    Env.IDToCountryIncorporated.put(CompanyRegistrationNo4ID, s);
                    break;
            }
            Env.updatedCompanyRegistrationNoLookups = true;
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

}
