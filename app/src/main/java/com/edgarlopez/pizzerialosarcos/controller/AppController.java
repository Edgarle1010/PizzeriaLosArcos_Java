package com.edgarlopez.pizzerialosarcos.controller;

import android.app.Application;

public class AppController extends Application {
    private static AppController instance;

    public static synchronized AppController getInstance() {
        return instance;
    }

    private String userId;
    private String name;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String verificationId;
    private String resendToken;
    private boolean isRecoveryRequest;
    private int streaks;
    private boolean baned;

    public boolean isRecoveryRequest() {
        return isRecoveryRequest;
    }

    public void setRecoveryRequest(boolean recoveryRequest) {
        isRecoveryRequest = recoveryRequest;
    }

    public int getStreaks() {
        return streaks;
    }

    public void setStreaks(int streaks) {
        this.streaks = streaks;
    }

    public boolean isBaned() {
        return baned;
    }

    public void setBaned(boolean baned) {
        this.baned = baned;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(String verificationId) {
        this.verificationId = verificationId;
    }

    public String getResendToken() {
        return resendToken;
    }

    public void setResendToken(String resendToken) {
        this.resendToken = resendToken;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
