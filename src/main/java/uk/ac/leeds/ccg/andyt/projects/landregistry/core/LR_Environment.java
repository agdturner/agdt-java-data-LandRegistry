package uk.ac.leeds.ccg.andyt.projects.landregistry.core;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.projects.landregistry.io.LR_Files;

/**
 *
 * @author geoagdt
 */
public class LR_Environment extends LR_OutOfMemoryErrorHandler
        implements Serializable {

    public LR_Strings Strings;
    public LR_Files Files;
    
    public LR_ID TypeIDTenure;
    public LR_ID TypeIDCompanyRegistrationNo1;
    public LR_ID TypeIDProprietorshipCategory1;
    public LR_ID TypeIDProprietorName1;
    public LR_ID TypeIDCountryIncorporated1;
    public LR_ID TypeIDPostcodeDistrict;

    //public HashMap<LR_ID, HashMap<LR_ID, String>> IDMaps;
    
    //protected <K> Map<K, String> getLookup(LR_ID typeID) {
    //    Map<K, String> result;
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
    
    // PropertyAddress, TitleNumber, ProprietorName, CompanyRegistrationNo, CountryIncorporated Lookups
    public HashMap<LR_ID, String> IDToPropertyAddress;
    public HashMap<String, LR_ID> PropertyAddressToID;
    public HashMap<LR_ID, String> IDToTitleNumber;
    public HashMap<String, LR_ID> TitleNumberToID;
    public HashMap<LR_ID, String> IDToTenure;
    public HashMap<String, LR_ID> TenureToID;
    
    public HashMap<LR_ID, String> IDToPostcodeDistrict;
    public HashMap<String, LR_ID> PostcodeDistrictToID;
    
    public HashMap<LR_ID, String> IDToProprietorName;
    public HashMap<String, LR_ID> ProprietorNameToID;
    public HashMap<LR_ID, String> IDToCompanyRegistrationNo;
    public HashMap<String, LR_ID> CompanyRegistrationNoToID;
    public HashMap<LR_ID, String> IDToCountryIncorporated;
    public HashMap<String, LR_ID> CountryIncorporatedToID;
    public HashMap<LR_ID, String> IDToProprietorshipCategory;
    public HashMap<String, LR_ID> ProprietorshipCategoryToID;
    

    public boolean updatedPropertyAddressLookups;
    public boolean updatedTitleNumberLookups;
    public boolean updatedTenureLookups;
    public boolean updatedPostcodeDistrictLookups;
    public boolean updatedProprietorNameLookups;
    public boolean updatedCompanyRegistrationNoLookups;
    public boolean updatedCountryIncorporatedLookups;
    public boolean updatedProprietorshipCategoryLookups;

    public LR_Environment() {
        Strings = new LR_Strings();
        Files = new LR_Files(Strings);
        updatedPropertyAddressLookups = false;
        updatedTitleNumberLookups = false;
        updatedTenureLookups = false;
        updatedPostcodeDistrictLookups = false;
        updatedProprietorNameLookups = false;
        updatedCompanyRegistrationNoLookups = false;
        updatedCountryIncorporatedLookups = false;
        updatedProprietorshipCategoryLookups = false;
    }

    public void writePropertyAddressLookups() {
        if (updatedPropertyAddressLookups) {
            File f;
            f = new File(Files.getGeneratedDataDir(Strings), "IDToPropertyAddress.dat");
            Generic_StaticIO.writeObject(IDToPropertyAddress, f);
            f = new File(Files.getGeneratedDataDir(Strings), "PropertyAddressToID.dat");
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
        if (updatedTitleNumberLookups) {
            File f;
            f = new File(Files.getGeneratedDataDir(Strings), "IDToTitleNumber.dat");
            Generic_StaticIO.writeObject(IDToTitleNumber, f);
            f = new File(Files.getGeneratedDataDir(Strings), "TitleNumberToID.dat");
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
        if (updatedTenureLookups) {
            File f;
            f = new File(Files.getGeneratedDataDir(Strings), "IDToTenure.dat");
            Generic_StaticIO.writeObject(IDToTenure, f);
            f = new File(Files.getGeneratedDataDir(Strings), "TenureToID.dat");
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
        if (updatedPostcodeDistrictLookups) {
            File f;
            f = new File(Files.getGeneratedDataDir(Strings), "IDToPostcodeDistrict.dat");
            Generic_StaticIO.writeObject(IDToPostcodeDistrict, f);
            f = new File(Files.getGeneratedDataDir(Strings), "PostcodeDistrictToID.dat");
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
        if (updatedProprietorNameLookups) {
            File f;
            f = new File(Files.getGeneratedDataDir(Strings), "IDToProprietorName.dat");
            Generic_StaticIO.writeObject(IDToProprietorName, f);
            f = new File(Files.getGeneratedDataDir(Strings), "ProprietorNameToID.dat");
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
        if (updatedCompanyRegistrationNoLookups) {
            File f;
            f = new File(Files.getGeneratedDataDir(Strings), "IDToCompanyRegistrationNo.dat");
            Generic_StaticIO.writeObject(IDToCompanyRegistrationNo, f);
            f = new File(Files.getGeneratedDataDir(Strings), "CompanyRegistrationNoToID.dat");
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
        if (updatedCountryIncorporatedLookups) {
            File f;
            f = new File(Files.getGeneratedDataDir(Strings), "IDToCountryIncorporated.dat");
            Generic_StaticIO.writeObject(IDToCountryIncorporated, f);
            f = new File(Files.getGeneratedDataDir(Strings), "CountryIncorporatedToID.dat");
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
        if (updatedProprietorshipCategoryLookups) {
            File f;
            f = new File(Files.getGeneratedDataDir(Strings), "IDToProprietorshipCategory.dat");
            Generic_StaticIO.writeObject(IDToProprietorshipCategory, f);
            f = new File(Files.getGeneratedDataDir(Strings), "ProprietorshipCategoryToID.dat");
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
