package com.project.Models;


public class User extends BaseEntitie {
    private String email;
    private String password;
    private String phone;
    private String token;
    private int colonyID;
    private String colonyName;

    public User( String email, String password, String phone, String token, boolean deleted, int colonyID) {
        super(deleted);
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.token = token;
        this.colonyID = colonyID;

    }

    public User() {
        super();
    }

    public User(int id, String email, String password, String phone, String token,int colonyID, boolean deleted) {
        super(id, deleted);
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.token = token;
        this.colonyID = colonyID;

    }
    //TODO check if colonyID is > 0
    public boolean objectIsEmpty() {
        if (/*isEmpty(this.colonyID) ||*/ isEmpty(this.password) || isEmpty(this.phone)) {
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


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getColonyID() {
        return colonyID;
    }

    public void setColonyID(int colonyID) {
        this.colonyID = colonyID;
    }

    public String getColonyName() {
        return colonyName;
    }

    public void setColonyName(String colonyName) {
        this.colonyName = colonyName;
    }
}
