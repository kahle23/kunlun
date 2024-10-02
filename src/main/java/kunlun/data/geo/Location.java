/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.geo;

import java.io.Serializable;

/**
 * The location information.
 * @see <a href="https://en.wikipedia.org/wiki/Geocode">Geocode</a>
 * @author Kahle
 */
public class Location implements Serializable {
    /**
     * The country.
     */
    private String country;
    /**
     * The country code.
     */
    private String countryCode;
    /**
     * The province or state.
     */
    private String region;
    /**
     * The region code.
     */
    private String regionCode;
    /**
     * The city.
     */
    private String city;
    /**
     * The city code.
     */
    private String cityCode;
    /**
     * The county, town or district.
     */
    private String district;
    /**
     * The district code.
     */
    private String districtCode;
    /**
     * The street.
     */
    private String street;
    /**
     * The street code.
     */
    private String streetCode;
    /**
     * The detailed address.
     */
    private String address;

    public String getCountry() {

        return country;
    }

    public void setCountry(String country) {

        this.country = country;
    }

    public String getCountryCode() {

        return countryCode;
    }

    public void setCountryCode(String countryCode) {

        this.countryCode = countryCode;
    }

    public String getRegion() {

        return region;
    }

    public void setRegion(String region) {

        this.region = region;
    }

    public String getRegionCode() {

        return regionCode;
    }

    public void setRegionCode(String regionCode) {

        this.regionCode = regionCode;
    }

    public String getCity() {

        return city;
    }

    public void setCity(String city) {

        this.city = city;
    }

    public String getCityCode() {

        return cityCode;
    }

    public void setCityCode(String cityCode) {

        this.cityCode = cityCode;
    }

    public String getDistrict() {

        return district;
    }

    public void setDistrict(String district) {

        this.district = district;
    }

    public String getDistrictCode() {

        return districtCode;
    }

    public void setDistrictCode(String districtCode) {

        this.districtCode = districtCode;
    }

    public String getStreet() {

        return street;
    }

    public void setStreet(String street) {

        this.street = street;
    }

    public String getStreetCode() {

        return streetCode;
    }

    public void setStreetCode(String streetCode) {

        this.streetCode = streetCode;
    }

    public String getAddress() {

        return address;
    }

    public void setAddress(String address) {

        this.address = address;
    }

}
