package uk.ac.leeds.ccg.andyt.projects.landregistry.core;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
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

    // PropertyAddress, TitleNumber, ProprietorName, CompanyRegistrationNo, CountryIncorporated Lookups
    public HashMap<LR_ID, String> IDToPropertyAddress;
    public HashMap<String, LR_ID> PropertyAddressToID;
    public HashMap<LR_ID, String> IDToTitleNumber;
    public HashMap<String, LR_ID> TitleNumberToID;
    public HashMap<LR_ID, String> IDToProprietorName;
    public HashMap<String, LR_ID> ProprietorNameToID;
    public HashMap<LR_ID, String> IDToCompanyRegistrationNo;
    public HashMap<String, LR_ID> CompanyRegistrationNoToID;
    public HashMap<LR_ID, String> IDToCountryIncorporated;
    public HashMap<String, LR_ID> CountryIncorporatedToID;

    public boolean updatedPropertyAddressLookups;
    public boolean updatedTitleNumberLookups;
    public boolean updatedProprietorNameLookups;
    public boolean updatedCompanyRegistrationNoLookups;
    public boolean updatedCountryIncorporatedLookups;

    public LR_Environment() {
        Files = new LR_Files();
        Strings = new LR_Strings();
        updatedPropertyAddressLookups = false;
        updatedTitleNumberLookups = false;
        updatedProprietorNameLookups = false;
        updatedCompanyRegistrationNoLookups = false;
        updatedCountryIncorporatedLookups = false;
    }

    public void writeIDToPropertyAddress() {
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

    public void writeIDToTitleNumber() {
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

    public void writeIDToProprietorName() {
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

    public void writeIDToCompanyRegistrationNo() {
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

    public void writeIDToCountryIncorporated() {
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
        } else {
            IDToCountryIncorporated = (HashMap<LR_ID, String>) Generic_StaticIO.readObject(f);
        }
    }

    public void loadCountryIncorporatedToID() {
        File f;
        f = new File(Files.getGeneratedDataDir(Strings), "CountryIncorporatedToID.dat");
        if (!f.exists()) {
            CountryIncorporatedToID = new HashMap<>();
        } else {
            CountryIncorporatedToID = (HashMap<String, LR_ID>) Generic_StaticIO.readObject(f);
        }
    }

}
