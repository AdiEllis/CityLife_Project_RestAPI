package com.project.Models;


public class User extends BaseEntitie {

    private String uid;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String phone;
    private String token;
    private int permission;
    private int department;

    public User(String uid, String firstName, String lastName, String password, String email, String phone, String token, int permission, int department, boolean deleted) {
        super(deleted);
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.token = token;
        this.permission = permission;
        this.department = department;

    }

    public User() {
        super();
    }

    public User(int id, String uid, String firstName, String lastName, String password, String email, String phone, String token, int permission, int department, boolean deleted) {
        super(id, deleted);
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.token = token;
        this.permission = permission;
        this.department = department;
    }

    public boolean objectIsEmpty() {
        if (isEmpty(this.uid) || isEmpty(this.firstName) || isEmpty(this.lastName) || isEmpty(this.password) || isEmpty(this.phone) || this.permission <= 0 || this.department <= 0) {
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public int getDepartment() {
        return department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
