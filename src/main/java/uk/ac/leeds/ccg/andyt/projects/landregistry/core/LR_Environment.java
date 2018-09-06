package uk.ac.leeds.ccg.andyt.projects.landregistry.core;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import uk.ac.leeds.ccg.andyt.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.projects.landregistry.io.LR_Files;

/**
 *
 * @author geoagdt
 */
public class LR_Environment extends LR_OutOfMemoryErrorHandler
        implements Serializable {
    
    public Generic_Environment ge;
    
    public LR_Strings Strings;
    public LR_Files Files;
    
    public LR_ID TypeIDTenure;
    public LR_ID TypeIDCompanyRegistrationNo1;
    public LR_ID TypeIDProprietorshipCategory1;
    public LR_ID TypeIDProprietorName1;
    public LR_ID TypeIDCountryIncorporated1;
    public LR_ID TypeIDPostcodeDistrict;

    /**
     * For returning the lookup related to the typeID.
     *
     * @param typeID
     * @return
     */
    public HashMap<LR_ID, String> getLookup(LR_ID typeID) {
        if (typeID.equals(TypeIDTenure)) {
            return IDToTenure;
        } else if (typeID.equals(TypeIDCompanyRegistrationNo1)) {
            return IDToCompanyRegistrationNo;
        } else if (typeID.equals(TypeIDProprietorshipCategory1)) {
            return IDToProprietorshipCategory;
        } else if (typeID.equals(TypeIDProprietorName1)) {
            return IDToProprietorName;
        } else if (typeID.equals(TypeIDCountryIncorporated1)) {
            return IDToCountryIncorporated;
        } else if (typeID.equals(TypeIDPostcodeDistrict)) {
            return IDToPostcodeDistrict;
        } else {
            return null;
        }
    }

    /**
     * PropertyAddressID to PropertyAddress Lookup
     */
    public HashMap<LR_ID, String> IDToPropertyAddress;
    /**
     * PropertyAddress to PropertyAddressID Lookup
     */
    public HashMap<String, LR_ID> PropertyAddressToID;
    /**
     * TitleNumberID to TitleNumber Lookup
     */
    public HashMap<LR_ID, String> IDToTitleNumber;
    /**
     * TitleNumber to TitleNumberID Lookup
     */
    public HashMap<String, LR_ID> TitleNumberToID;
    /**
     * TenureID to Tenure Lookup
     */
    public HashMap<LR_ID, String> IDToTenure;
    /**
     * Tenure to TenureID Lookup
     */
    public HashMap<String, LR_ID> TenureToID;
    /**
     * PostcodeDistrictID to PostcodeDistrict Lookup
     */
    public HashMap<LR_ID, String> IDToPostcodeDistrict;
    /**
     * PostcodeDistrict to PostcodeDistrictID Lookup
     */
    public HashMap<String, LR_ID> PostcodeDistrictToID;
    /**
     * ProprietorNameID to ProprietorName Lookup
     */
    public HashMap<LR_ID, String> IDToProprietorName;
    /**
     * ProprietorName to ProprietorNameID Lookup
     */
    public HashMap<String, LR_ID> ProprietorNameToID;
    /**
     * CompanyRegistrationNoID to CompanyRegistrationNo Lookup
     */
    public HashMap<LR_ID, String> IDToCompanyRegistrationNo;
    /**
     * CompanyRegistrationNo to CompanyRegistrationNoID Lookup
     */
    public HashMap<String, LR_ID> CompanyRegistrationNoToID;
    /**
     * CountryIncorporatedID to CountryIncorporated Lookup
     */
    public HashMap<LR_ID, String> IDToCountryIncorporated;
    /**
     * CountryIncorporated to CountryIncorporatedID Lookup
     */
    public HashMap<String, LR_ID> CountryIncorporatedToID;
    /**
     * ProprietorshipCategoryID to ProprietorshipCategory Lookup
     */
    public HashMap<LR_ID, String> IDToProprietorshipCategory;
    /**
     * ProprietorshipCategory to ProprietorshipCategoryID Lookup
     */
    public HashMap<String, LR_ID> ProprietorshipCategoryToID;

    /**
     * Set to true if a new PropertyAddress is added.
     */
    public boolean UpdatedPropertyAddressLookups;
    /**
     * Set to true if a new TitleNumber is added.
     */
    public boolean UpdatedTitleNumberLookups;
    /**
     * Set to true if a new Tenure is added.
     */
    public boolean UpdatedTenureLookups;
    /**
     * Set to true if a new PostcodeDistrict is added.
     */
    public boolean UpdatedPostcodeDistrictLookups;
    /**
     * Set to true if a new ProprietorName is added.
     */
    public boolean UpdatedProprietorNameLookups;
    /**
     * Set to true if a new CompanyRegistrationNo is added.
     */
    public boolean UpdatedCompanyRegistrationNoLookups;
    /**
     * Set to true if a new CountryIncorporated is added.
     */
    public boolean UpdatedCountryIncorporatedLookups;
    /**
     * Set to true if a new ProprietorshipCategory is added.
     */
    public boolean UpdatedProprietorshipCategoryLookups;

    public LR_Environment() {
        Strings = new LR_Strings();
        Files = new LR_Files(Strings);
        ge = new Generic_Environment(Files, Strings);
        UpdatedPropertyAddressLookups = false;
        UpdatedTitleNumberLookups = false;
        UpdatedTenureLookups = false;
        UpdatedPostcodeDistrictLookups = false;
        UpdatedProprietorNameLookups = false;
        UpdatedCompanyRegistrationNoLookups = false;
        UpdatedCountryIncorporatedLookups = false;
        UpdatedProprietorshipCategoryLookups = false;
    }
    
    public void writePropertyAddressLookups() {
        if (UpdatedPropertyAddressLookups) {
            File dir;
            dir = Files.getGeneratedDataDir(Strings);
            File f;
            f = new File(dir, "IDToPropertyAddress.dat");
            Generic_StaticIO.writeObject(IDToPropertyAddress, f);
            f = new File(dir, "PropertyAddressToID.dat");
            Generic_StaticIO.writeObject(PropertyAddressToID, f);
        }
    }
    
    public void loadIDToPropertyAddress() {
        File f;
        f = new File(Files.getGeneratedDataDir(Strings), "IDToPropertyAddress.dat");
        if (!f.exists()) {
            IDToPropertyAddress = new HashMap<>();
        } else {
            IDToPropertyAddress = (HashMap<LR_ID, String>) Generic_StaticIO.readObject(f);
        }
    }
    
    public void loadPropertyAddressToID() {
        File f;
        f = new File(Files.getGeneratedDataDir(Strings), "PropertyAddressToID.dat");
        if (!f.exists()) {
            PropertyAddressToID = new HashMap<>();
        } else {
            PropertyAddressToID = (HashMap<String, LR_ID>) Generic_StaticIO.readObject(f);
        }
    }
    
    public void writeTitleNumberLookups() {
        if (UpdatedTitleNumberLookups) {
            File dir;
            dir = Files.getGeneratedDataDir(Strings);
            File f;
            f = new File(dir, "IDToTitleNumber.dat");
            Generic_StaticIO.writeObject(IDToTitleNumber, f);
            f = new File(dir, "TitleNumberToID.dat");
            Generic_StaticIO.writeObject(TitleNumberToID, f);
        }
    }
    
    public void loadIDToTitleNumber() {
        File f;
        f = new File(Files.getGeneratedDataDir(Strings), "IDToTitleNumber.dat");
        if (!f.exists()) {
            IDToTitleNumber = new HashMap<>();
        } else {
            IDToTitleNumber = (HashMap<LR_ID, String>) Generic_StaticIO.readObject(f);
        }
    }
    
    public void loadTitleNumberToID() {
        File f;
        f = new File(Files.getGeneratedDataDir(Strings), "TitleNumberToID.dat");
        if (!f.exists()) {
            TitleNumberToID = new HashMap<>();
        } else {
            TitleNumberToID = (HashMap<String, LR_ID>) Generic_StaticIO.readObject(f);
        }
    }
    
    public void writeTenureLookups() {
        if (UpdatedTenureLookups) {
            File dir;
            dir = Files.getGeneratedDataDir(Strings);
            File f;
            f = new File(dir, "IDToTenure.dat");
            Generic_StaticIO.writeObject(IDToTenure, f);
            f = new File(dir, "TenureToID.dat");
            Generic_StaticIO.writeObject(TenureToID, f);
        }
    }
    
    public void loadIDToTenure() {
        File f;
        f = new File(Files.getGeneratedDataDir(Strings), "IDToTenure.dat");
        if (!f.exists()) {
            IDToTenure = new HashMap<>();
        } else {
            IDToTenure = (HashMap<LR_ID, String>) Generic_StaticIO.readObject(f);
        }
    }
    
    public void loadTenureToID() {
        File f;
        f = new File(Files.getGeneratedDataDir(Strings), "TenureToID.dat");
        if (!f.exists()) {
            TenureToID = new HashMap<>();
        } else {
            TenureToID = (HashMap<String, LR_ID>) Generic_StaticIO.readObject(f);
        }
    }
    
    public void writePostcodeDistrictLookups() {
        if (UpdatedPostcodeDistrictLookups) {
            File dir;
            dir = Files.getGeneratedDataDir(Strings);
            File f;
            f = new File(dir, "IDToPostcodeDistrict.dat");
            Generic_StaticIO.writeObject(IDToPostcodeDistrict, f);
            f = new File(dir, "PostcodeDistrictToID.dat");
            Generic_StaticIO.writeObject(PostcodeDistrictToID, f);
        }
    }
    
    public void loadIDToPostcodeDistrict() {
        File f;
        f = new File(Files.getGeneratedDataDir(Strings), "IDToPostcodeDistrict.dat");
        if (!f.exists()) {
            IDToPostcodeDistrict = new HashMap<>();
        } else {
            IDToPostcodeDistrict = (HashMap<LR_ID, String>) Generic_StaticIO.readObject(f);
        }
    }
    
    public void loadPostcodeDistrictToID() {
        File f;
        f = new File(Files.getGeneratedDataDir(Strings), "PostcodeDistrictToID.dat");
        if (!f.exists()) {
            PostcodeDistrictToID = new HashMap<>();
        } else {
            PostcodeDistrictToID = (HashMap<String, LR_ID>) Generic_StaticIO.readObject(f);
        }
    }
    
    public void writeProprietorNameLookups() {
        if (UpdatedProprietorNameLookups) {
            File dir;
            dir = Files.getGeneratedDataDir(Strings);
            File f;
            f = new File(dir, "IDToProprietorName.dat");
            Generic_StaticIO.writeObject(IDToProprietorName, f);
            f = new File(dir, "ProprietorNameToID.dat");
            Generic_StaticIO.writeObject(ProprietorNameToID, f);
        }
    }
    
    public void loadIDToProprietorName() {
        File f;
        f = new File(Files.getGeneratedDataDir(Strings), "IDToProprietorName.dat");
        if (!f.exists()) {
            IDToProprietorName = new HashMap<>();
        } else {
            IDToProprietorName = (HashMap<LR_ID, String>) Generic_StaticIO.readObject(f);
        }
    }
    
    public void loadProprietorNameToID() {
        File f;
        f = new File(Files.getGeneratedDataDir(Strings), "ProprietorNameToID.dat");
        if (!f.exists()) {
            ProprietorNameToID = new HashMap<>();
        } else {
            ProprietorNameToID = (HashMap<String, LR_ID>) Generic_StaticIO.readObject(f);
        }
    }
    
    public void writeCompanyRegistrationNoLookups() {
        if (UpdatedCompanyRegistrationNoLookups) {
            File dir;
            dir = Files.getGeneratedDataDir(Strings);
            File f;
            f = new File(dir, "IDToCompanyRegistrationNo.dat");
            Generic_StaticIO.writeObject(IDToCompanyRegistrationNo, f);
            f = new File(dir, "CompanyRegistrationNoToID.dat");
            Generic_StaticIO.writeObject(CompanyRegistrationNoToID, f);
        }
    }
    
    public void loadIDToCompanyRegistrationNo() {
        File f;
        f = new File(Files.getGeneratedDataDir(Strings), "IDToCompanyRegistrationNo.dat");
        if (!f.exists()) {
            IDToCompanyRegistrationNo = new HashMap<>();
        } else {
            IDToCompanyRegistrationNo = (HashMap<LR_ID, String>) Generic_StaticIO.readObject(f);
        }
    }
    
    public void loadCompanyRegistrationNoToID() {
        File f;
        f = new File(Files.getGeneratedDataDir(Strings), "CompanyRegistrationNoToID.dat");
        if (!f.exists()) {
            CompanyRegistrationNoToID = new HashMap<>();
        } else {
            CompanyRegistrationNoToID = (HashMap<String, LR_ID>) Generic_StaticIO.readObject(f);
        }
    }
    
    public void writeCountryIncorporatedLookups() {
        if (UpdatedCountryIncorporatedLookups) {
            File dir;
            dir = Files.getGeneratedDataDir(Strings);
            File f;
            f = new File(dir, "IDToCountryIncorporated.dat");
            Generic_StaticIO.writeObject(IDToCountryIncorporated, f);
            f = new File(dir, "CountryIncorporatedToID.dat");
            Generic_StaticIO.writeObject(CountryIncorporatedToID, f);
        }
    }
    
    public void loadIDToCountryIncorporated() {
        File f;
        f = new File(Files.getGeneratedDataDir(Strings), "IDToCountryIncorporated.dat");
        if (!f.exists()) {
            IDToCountryIncorporated = new HashMap<>();
            IDToCountryIncorporated.put(new LR_ID(0), Strings.S_United_Kingdom);
        } else {
            IDToCountryIncorporated = (HashMap<LR_ID, String>) Generic_StaticIO.readObject(f);
        }
    }
    
    public void loadCountryIncorporatedToID() {
        File f;
        f = new File(Files.getGeneratedDataDir(Strings), "CountryIncorporatedToID.dat");
        if (!f.exists()) {
            CountryIncorporatedToID = new HashMap<>();
            CountryIncorporatedToID.put(Strings.S_United_Kingdom, new LR_ID(0));
        } else {
            CountryIncorporatedToID = (HashMap<String, LR_ID>) Generic_StaticIO.readObject(f);
        }
    }
    
    public void writeProprietorshipCategoryLookups() {
        if (UpdatedProprietorshipCategoryLookups) {
            File dir;
            dir = Files.getGeneratedDataDir(Strings);
            File f;
            f = new File(dir, "IDToProprietorshipCategory.dat");
            Generic_StaticIO.writeObject(IDToProprietorshipCategory, f);
            f = new File(dir, "ProprietorshipCategoryToID.dat");
            Generic_StaticIO.writeObject(ProprietorshipCategoryToID, f);
        }
    }
    
    public void loadIDToProprietorshipCategory() {
        File f;
        f = new File(Files.getGeneratedDataDir(Strings), "IDToProprietorshipCategory.dat");
        if (!f.exists()) {
            IDToProprietorshipCategory = new HashMap<>();
        } else {
            IDToProprietorshipCategory = (HashMap<LR_ID, String>) Generic_StaticIO.readObject(f);
        }
    }
    
    public void loadProprietorshipCategoryToID() {
        File f;
        f = new File(Files.getGeneratedDataDir(Strings), "ProprietorshipCategoryToID.dat");
        if (!f.exists()) {
            ProprietorshipCategoryToID = new HashMap<>();
        } else {
            ProprietorshipCategoryToID = (HashMap<String, LR_ID>) Generic_StaticIO.readObject(f);
        }
    }
}
