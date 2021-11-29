package com.project.Objects.Entities;

public class AuthUser {
    private int authUserId;
    private String authUserEmail;
    private Integer authUserError;
    private String authUserToken;
    private int authUserColonyID;
    private boolean authUserIsAdmin;

    public AuthUser(int authUserId, String authUserToken,
                    String authUserEmail, int colonyID, boolean isAdmin) {
        this.authUserId = authUserId;
        this.authUserToken = authUserToken;
        this.authUserEmail = authUserEmail;
        this.authUserColonyID = colonyID;
        this.authUserIsAdmin = isAdmin;
    }

    public boolean getAuthUserIsAdmin() {
        return authUserIsAdmin;
    }

    public void setAuthUserIsAdmin(boolean admin) {
        authUserIsAdmin = admin;
    }

    public AuthUser(Integer authUserError) {
        this.authUserError = authUserError;
    }

    public int getAuthUserId() {
        return authUserId;
    }

    public void setAuthUserId(int authUserId) {
        this.authUserId = authUserId;
    }

    public String getAuthUserEmail() {
        return authUserEmail;
    }

    public void setAuthUserEmail(String authUserEmail) {
        this.authUserEmail = authUserEmail;
    }

    public Integer getAuthUserError() {
        return authUserError;
    }

    public void setAuthUserError(Integer authUserError) {
        this.authUserError = authUserError;
    }

    public String getAuthUserToken() {
        return authUserToken;
    }

    public void setAuthUserToken(String authUserToken) {
        this.authUserToken = authUserToken;
    }

    public int getAuthUserColonyID() {
        return authUserColonyID;
    }

    public void setAuthUserColonyID(int colonyID) {
        colonyID = colonyID;
    }

    @Override
    public String toString() {
        return "AuthUser{" +
                "authUserId=" + authUserId +
                ", authUserEmail='" + authUserEmail + '\'' +
                ", authUserError=" + authUserError +
                ", authUserToken='" + authUserToken + '\'' +
                ", colonyID=" + authUserColonyID +
                ", isAdmin=" + authUserIsAdmin +
                '}';
    }
}
