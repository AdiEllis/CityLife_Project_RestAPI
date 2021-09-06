package com.project.Objects.Entities;

public class AuthUser {
    private int authUserId;
    private String authUserEmail;
    private Integer authUserError;

    public AuthUser(int authUserId, String authUserEmail) {
        this.authUserId = authUserId;
        this.authUserEmail = authUserEmail;
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

    @Override
    public String toString() {
        return "AuthUser{" +
                "authUserId=" + authUserId +
                ", authUserEmail='" + authUserEmail + '\'' +
                ", authUserError=" + authUserError +
                '}';
    }
}
