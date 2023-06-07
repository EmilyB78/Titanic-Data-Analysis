package model;

/**
 * Countries class that includes countryID and countryName.
 * These values reflect needed country information. Getters and setters
 * are also included.
 */
public class Countries {
    private int countryID;
    private String countryName;

    public Countries(int countryID, String countryName){
        this.countryID = countryID;
        this.countryName = countryName;
    }

    /**
     *
     * @return countryID
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     *
     * @return countryName
     */

    public String getCountryName() {
        return countryName;
    }

    /**
     *
     * @return String countryName.
     */

    @Override
    public String toString (){
        return countryName;
    }

    }
