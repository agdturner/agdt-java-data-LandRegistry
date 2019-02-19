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
package uk.ac.leeds.ccg.andyt.projects.landregistry.data.landregistry.pricepaid;


import java.io.Serializable;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Environment;
import uk.ac.leeds.ccg.andyt.projects.landregistry.core.LR_Object;

/**
 *
 * @author geoagdt
 */
public class LR_PricePaid_Record extends LR_Object implements Serializable {

    /**
     * Transaction unique identifier: A reference number which is generated
     * automatically recording each published sale. The number is unique and
     * will change each time a sale is recorded.
     */
    String ID;

    /**
     * Price Sale price stated on the transfer deed.
     */
    String pricePaid;

    /**
     * Date of Transfer: Date when the sale was completed, as stated on the
     * transfer deed.
     */
    String DateOfTransfer;

    /**
     * Postcode: This is the postcode used at the time of the original
     * transaction. Note that postcodes can be reallocated and these changes are
     * not reflected in the Price Paid Dataset.
     */
    String Postcode;

    /**
     * Property Type: D = Detached, S = Semi - Detached, T = Terraced, F = Flats
     * / Maisonettes, O = Other (Note that: - we only record the above
     * categories to describe property type, we do not separately identify
     * bungalows. - end-of-terrace properties are included in the Terraced
     * category above. - ‘Other’ is only valid where the transaction relates to
     * a property type that is not covered by existing values.
     */
    String PropertyType;

    /**
     * Old/New: Indicates the age of the property and applies to all price paid
     * transactions, residential and non -residential. Y = a newly built
     * property , N = an established residential building.
     *
     */
    String New;

    /**
     * Duration: Relates to the tenure: F = Freehold, L = Leasehold etc. Note
     * that HM Land Registry does not record leases of 7 years or less in the
     * Price Paid Dataset.
     *
     */
    String Duration;
    
    /**
     * PAON (Primary Addressable Object Name). Typically the house number or
     * name.
     */
    public String PAON;

    /**
     * SAON (Secondary Addressable Object Name). Where a property has been
     * divided into separate units(for example, flats), the PAON (above) will
     * identify the building and a SAON will be specified that identifies the
     * separate unit/flat.
     */
    public String SAON;

    /**
     * Street
     */
    public String Street;

    /**
     * Locality
     */
    public String Locality;

    /**
     * Town/City
     */
    public String TownOrCity;

    /**
     * District
     */
    public String District;

    /**
     * County
     */
    public String County;

    /**
     * PPD Category Type: Indicates the type of Price Paid transaction. A =
     * Standard Price Paid entry, includes single residential property sold for
     * full market value. B = Additional Price Paid entry including transfers
     * under a power of sale/repossessions, buy-to-lets (where they can be
     * identified by a Mortgage) and transfers to non-private individuals. Note
     * that category B does not separately identify the transaction types
     * stated. HM Land Registry has been collecting information on Category A
     * transactions from January 1995. Category B transactions were identified
     * from October 2013.
     */
    public String PPDCategoryType;
    
    /**
     * Record Status - monthly file only Indicates additions , changes and
     * deletions to the records. A - Added records: records added into the price
     * paid dataset in the monthly refresh due to new sales transactions C -
     * Changed records: records changed in the price paid dataset in the monthly
     * refresh. You should replace or update records in any stored data using
     * the unique identifier to recognise them D - Deleted records: records
     * deleted from the price paid dataset in the monthly refresh. You should
     * delete records from any stored data using the unique identifier to
     * recognise them. When a transaction changes category type due to
     * misallocation it will be deleted from the original category type and
     * added to the correct category with a new transaction unique identifier.
     * Note that where a transaction changes category type due to misallocation
     * (as above) it will be deleted from the original category type and added
     * to the correct category with a new transaction unique identifier.
     */
    String RecordStatus;
    
    /**
     *
     * @param env
     * @param line
     * @throws Exception
     */
    public LR_PricePaid_Record(LR_Environment env, String line) throws Exception {
        super(env);
        String[] split;
        split = line.split("\",\"");
        //System.out.println(line);
        if (split.length != 16) {
            throw new Exception("Unexpected record length!");
        }
        ID = split[0].substring(1);
        pricePaid = split[1];
        DateOfTransfer = split[2];
        Postcode = split[3];
        PropertyType = split[4];
        New = split[5];
        Duration = split[6];
        PAON = split[7];
        SAON = split[8];
        Street = split[9];
        Locality = split[10];
        TownOrCity = split[11];
        District = split[12];
        County = split[13];
        PPDCategoryType = split[14];
        RecordStatus = split[15].substring(0, split[15].length() - 1);
        //System.out.println(toString());
    }

    @Override
    public String toString() {
        return toCSV();
    }

    public String toCSV() {
        return "\"" + ID + "\",\"" + pricePaid + "\",\"" + DateOfTransfer 
                + "\",\"" + Postcode  + "\",\"" + PropertyType + "\",\"" + New 
                + "\",\"" + Duration + "\",\"" + PAON + "\",\"" + SAON 
                + "\",\"" + Street + "\",\"" + Locality + "\",\"" + TownOrCity
                + "\",\"" + District + "\",\"" + County + "\",\""
                + PPDCategoryType + "\",\"" + RecordStatus + "\"";
    }

    public static String header() {
        return "ID, pricePaid, DateOfTransfer, Postcode, PropertyType, New, "
                + "Duration, PAON, SAON, Street, Locality, TownOrCity, "
                + "District, County, PPDCategoryType, RecordStatus";
    }

}
