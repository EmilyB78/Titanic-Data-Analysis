package model;

/**
 * StateProvince class that contains divisionID, divisionName, and country_ID.
 * These values reflect needed State/Province information. Getters and
 * setters are included.
 */
public class StateProvince {
    private int divisionID;
    private String divisionName;
    public int country_ID;
    public StateProvince(int divisionID, String divisionName, int country_ID){
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.country_ID = country_ID;
    }

    /**
     *
     * @return divisionID
     */

    public int getDivisionID() {
        int divisionID = this.divisionID;
        return divisionID;
    }

    /**
     *
     * @return divisionName
     */

    public String getDivisionName(){
        return divisionName;
    }

    /**
     *
     * @return country_ID
     */

    public int getCountry_ID() {
        return country_ID;
    }

    /**
     *
     * @return String divisionName.
     */

    @Override
    public String toString (){
        return divisionName;
    }


}
