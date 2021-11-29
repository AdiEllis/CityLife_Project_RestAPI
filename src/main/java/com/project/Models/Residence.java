package com.project.Models;

public class Residence extends BaseEntitie {
    private String firstName;
    private String lastName;
    private String birthDate;
    private int age;
    private String phone;
    private String id;
    private int colonyID;
    private String colonyName;
    private int streetID;
    private String streetName;
    private String houseNumber;
    private int apartmentOwner;
    private boolean livesInHousingUnit;

    public Residence() {
    }

    public Residence(int oid, boolean deleted, String firstName, String lastName,
                     String birthDate, String phone, String id, int colonyID,
                     int streetID, String houseNumber, int apartmentOwner, boolean livesInHousingUnit) {
        super(oid, deleted);
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.phone = phone;
        this.id = id;
        this.colonyID = colonyID;
        this.streetID = streetID;
        this.houseNumber = houseNumber;
        this.apartmentOwner = apartmentOwner;
        this.livesInHousingUnit = livesInHousingUnit;
    }

    public Residence(String firstName, String lastName, String birthDate,
                     String phone, String id, int colonyID, int streetID,
                     String houseNumber,int apartmentOwner,boolean livesInHousingUnit, boolean deleted) {
        super(deleted);
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.phone = phone;
        this.id = id;
        this.colonyID = colonyID;
        this.streetID = streetID;
        this.houseNumber = houseNumber;
        this.apartmentOwner = apartmentOwner;
        this.livesInHousingUnit = livesInHousingUnit;
    }

    public boolean objectIsEmpty() {
        //TODO make id > 0
        if (isEmpty(this.firstName) || isEmpty(this.lastName) || isEmpty(this.birthDate) || isEmpty(this.phone) || isEmpty(this.id)) {
            return true;
        }
        return false;
    }

    public boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getColonyID() {
        return colonyID;
    }

    public void setColonyID(int colonyID) {
        this.colonyID = colonyID;
    }

    public int getStreetID() {
        return streetID;
    }

    public void setStreetID(int streetID) {
        this.streetID = streetID;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColonyName() {
        return colonyName;
    }

    public void setColonyName(String colonyName) {
        this.colonyName = colonyName;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public int getApartmentOwner() {
        return apartmentOwner;
    }

    public void setApartmentOwner(int apartmentOwner) {
        this.apartmentOwner = apartmentOwner;
    }

    public boolean isLivesInHousingUnit() {
        return livesInHousingUnit;
    }

    public void setLivesInHousingUnit(boolean livesInHousingUnit) {
        this.livesInHousingUnit = livesInHousingUnit;
    }
}
