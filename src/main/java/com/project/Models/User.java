package com.project.Models;


public class User extends BaseEntitie {
    private String email;
    private String password;
    private String phone;
    private String token;
    private String colonyID;

    public User( String email, String password, String phone, String token, boolean deleted, String colonyID) {
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

    public User(int id, String email, String password, String phone, String token,String colonyID, boolean deleted) {
        super(id, deleted);
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.token = token;
        this.colonyID = colonyID;

    }

    public boolean objectIsEmpty() {
        if (isEmpty(this.colonyID) || isEmpty(this.password) || isEmpty(this.phone)) {
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

    public String getColonyID() {
        return colonyID;
    }

    public void setColonyID(String colonyID) {
        this.colonyID = colonyID;
    }
}
