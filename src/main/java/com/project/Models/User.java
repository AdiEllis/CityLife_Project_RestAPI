package com.project.Models;

import com.project.Utils.PasswordAuthentication;

public class User extends BaseEntitie {
    private PasswordAuthentication passwordAuthentication = new PasswordAuthentication();

    private String uid;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String phone;
    private int permission;
    private int department;

    public User(String uid, String firstName, String lastName, String password, String email, String phone, int permission, int department, boolean deleted) {
        super(deleted);
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = passwordAuthentication.hash(password);
        this.email = email;
        this.phone = phone;
        this.permission = permission;
        this.department = department;

    }

    public User() {
        super();
    }

    public User(int id, String uid, String firstName, String lastName, String password, String email, String phone, int permission, int department, boolean deleted) {
        super(id, deleted);
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = passwordAuthentication.hash(password);
        this.email = email;
        this.phone = phone;
        this.permission = permission;
        this.department = department;
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
        this.password = passwordAuthentication.hash(password);
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
}
