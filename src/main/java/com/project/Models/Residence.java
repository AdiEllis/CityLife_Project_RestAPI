package com.project.Models;

public class Residence extends BaseEntitie {
    private String firstName;
    private String lastName;
    private String birthDate;
    private String phone;
    private String id;
    private String colonyID;
    private String streetID;
    private String houseNumber;

    public Residence() {}
    public Residence(int oid, boolean deleted, String firstName, String lastName, String birthDate, String phone, String id, String colonyID, String streetID, String houseNumber) {
        super(oid, deleted);
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.phone = phone;
        this.id = id;
        this.colonyID = colonyID;
        this.streetID = streetID;
        this.houseNumber = houseNumber;
    }

    public Residence(String firstName, String lastName, String birthDate, String phone, String id,String colonyID, String streetID, String houseNumber, boolean deleted) {
        super(deleted);
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.phone = phone;
        this.id = id;
        this.colonyID = colonyID;
        this.streetID = streetID;
        this.houseNumber = houseNumber;
    }
    public boolean objectIsEmpty() {
        if (isEmpty(this.firstName) ||  isEmpty(this.lastName) || isEmpty(this.birthDate) || isEmpty(this.phone) || isEmpty(this.id)) {
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

    public String getColonyID() {
        return colonyID;
    }

    public void setColonyID(String colonyID) {
        this.colonyID = colonyID;
    }

    public String getStreetID() {
        return streetID;
    }

    public void setStreetID(String streetID) {
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
}
