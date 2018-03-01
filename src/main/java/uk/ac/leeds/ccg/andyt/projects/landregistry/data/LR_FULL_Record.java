package uk.ac.leeds.ccg.andyt.projects.landregistry.data;

import java.io.Serializable;

/**
 *
 * @author geoagdt
 */
public class LR_FULL_Record implements Serializable {

    private final long ID;
    private final String TitleNumber;
    private final String Tenure;
    private final String PropertyAddress;
    private final String District;
    private final String County;
    private final String Region;
    private final String Postcode;
    private final String MultipleAddressIndicator;
    private final String PricePaid;
    private final String ProprietorName1;
    private final String CompanyRegistrationNo1;
    private final String ProprietorshipCategory1;
    private final String CountryIncorporated1;
    private final String Proprietor1Address1;
    private final String Proprietor1Address2;
    private final String Proprietor1Address3;
    private final String ProprietorName2;
    private final String CompanyRegistrationNo2;
    private final String ProprietorshipCategory2;
    private final String CountryIncorporated2;
    private final String Proprietor2Address1;
    private final String Proprietor2Address2;
    private final String Proprietor2Address3;
    private final String ProprietorName3;
    private final String CompanyRegistrationNo3;
    private final String ProprietorshipCategory3;
    private final String CountryIncorporated3;
    private final String Proprietor3Address1;
    private final String Proprietor3Address2;
    private final String Proprietor3Address3;
    private final String ProprietorName4;
    private final String CompanyRegistrationNo4;
    private final String ProprietorshipCategory4;
    private final String CountryIncorporated4;
    private final String Proprietor4Address1;
    private final String Proprietor4Address2;
    private final String Proprietor4Address3;
    private final String DateProprietorAdded;
    private final String AdditionalProprietorIndicator;

    public LR_FULL_Record(long ID, String line) {
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

    public String toCSV() {
        return getTitleNumber()
                + "," + getTenure()
                + "," + getPropertyAddress()
                + "," + getDistrict()
                + "," + getCounty()
                + "," + getRegion()
                + "," + getPostcode()
                + "," + getMultipleAddressIndicator()
                + "," + getPricePaid()
                + "," + getProprietorName1()
                + "," + getCompanyRegistrationNo1()
                + "," + getProprietorshipCategory1()
                + "," + getCountryIncorporated1()
                + "," + getProprietor1Address1()
                + "," + getProprietor1Address2()
                + "," + getProprietor1Address3()
                + "," + getProprietorName2()
                + "," + getCompanyRegistrationNo2()
                + "," + getProprietorshipCategory2()
                + "," + getCountryIncorporated2()
                + "," + getProprietor2Address1()
                + "," + getProprietor2Address2()
                + "," + getProprietor2Address3()
                + "," + getProprietorName3()
                + "," + getCompanyRegistrationNo3()
                + "," + getProprietorshipCategory3()
                + "," + getCountryIncorporated3()
                + "," + getProprietor3Address1()
                + "," + getProprietor3Address2()
                + "," + getProprietor3Address3()
                + "," + getProprietorName4()
                + "," + getCompanyRegistrationNo4()
                + "," + getProprietorshipCategory4()
                + "," + getCountryIncorporated4()
                + "," + getProprietor4Address1()
                + "," + getProprietor4Address2()
                + "," + getProprietor4Address3()
                + "," + getDateProprietorAdded()
                + "," + getAdditionalProprietorIndicator();
    }
    
    public static String header() {
        return "TitleNumber,Tenure,PropertyAddress,District,County,Region,"
                + "Postcode,MultipleAddressIndicator,PricePaid,ProprietorName1"
                + ",CompanyRegistrationNo1,ProprietorshipCategory"
                + ",CountryIncorporated1,Proprietor1Address1"
                + ",Proprietor1Address2,Proprietor1Address3,ProprietorName2"
                + ",CompanyRegistrationNo2,ProprietorshipCategory2"
                + ",CountryIncorporated2,Proprietor2Address1"
                + ",Proprietor2Address2,Proprietor2Address3,ProprietorName3"
                + ",CompanyRegistrationNo3,ProprietorshipCategory3"
                + ",CountryIncorporated3,Proprietor3Address1"
                + ",Proprietor3Address2,Proprietor3Address3,ProprietorName4"
                + ",CompanyRegistrationNo4,ProprietorshipCategory4"
                + ",CountryIncorporated4,Proprietor4Address1"
                + ",Proprietor4Address2,Proprietor4Address3,DateProprietorAdded"
                + ",AdditionalProprietorIndicator";
    }
    
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
     * @return the PropertyAddress
     */
    public String getPropertyAddress() {
        return PropertyAddress;
    }

    /**
     * @return the District
     */
    public String getDistrict() {
        return District;
    }

    /**
     * @return the County
     */
    public String getCounty() {
        return County;
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
     * @return the MultipleAddressIndicator
     */
    public String getMultipleAddressIndicator() {
        return MultipleAddressIndicator;
    }

    /**
     * @return the PricePaid
     */
    public String getPricePaid() {
        return PricePaid;
    }

    /**
     * @return the ProprietorName1
     */
    public String getProprietorName1() {
        return ProprietorName1;
    }

    /**
     * @return the CompanyRegistrationNo1
     */
    public String getCompanyRegistrationNo1() {
        return CompanyRegistrationNo1;
    }

    /**
     * @return the ProprietorshipCategory1
     */
    public String getProprietorshipCategory1() {
        return ProprietorshipCategory1;
    }

    /**
     * @return the CountryIncorporated1
     */
    public String getCountryIncorporated1() {
        return CountryIncorporated1;
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
     * @return the ProprietorName2
     */
    public String getProprietorName2() {
        return ProprietorName2;
    }

    /**
     * @return the CompanyRegistrationNo2
     */
    public String getCompanyRegistrationNo2() {
        return CompanyRegistrationNo2;
    }

    /**
     * @return the ProprietorshipCategory2
     */
    public String getProprietorshipCategory2() {
        return ProprietorshipCategory2;
    }

    /**
     * @return the CountryIncorporated2
     */
    public String getCountryIncorporated2() {
        return CountryIncorporated2;
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
     * @return the ProprietorName3
     */
    public String getProprietorName3() {
        return ProprietorName3;
    }

    /**
     * @return the CompanyRegistrationNo3
     */
    public String getCompanyRegistrationNo3() {
        return CompanyRegistrationNo3;
    }

    /**
     * @return the ProprietorshipCategory3
     */
    public String getProprietorshipCategory3() {
        return ProprietorshipCategory3;
    }

    /**
     * @return the CountryIncorporated3
     */
    public String getCountryIncorporated3() {
        return CountryIncorporated3;
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
     * @return the ProprietorName4
     */
    public String getProprietorName4() {
        return ProprietorName4;
    }

    /**
     * @return the CompanyRegistrationNo4
     */
    public String getCompanyRegistrationNo4() {
        return CompanyRegistrationNo4;
    }

    /**
     * @return the ProprietorshipCategory4
     */
    public String getProprietorshipCategory4() {
        return ProprietorshipCategory4;
    }

    /**
     * @return the CountryIncorporated4
     */
    public String getCountryIncorporated4() {
        return CountryIncorporated4;
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
     * @return the DateProprietorAdded
     */
    public String getDateProprietorAdded() {
        return DateProprietorAdded;
    }

    /**
     * @return the AdditionalProprietorIndicator
     */
    public String getAdditionalProprietorIndicator() {
        return AdditionalProprietorIndicator;
    }

    
}
