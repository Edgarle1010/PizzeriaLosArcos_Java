package com.edgarlopez.pizzerialosarcos.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.edgarlopez.pizzerialosarcos.data.DataConverter;
import com.edgarlopez.pizzerialosarcos.data.DataConverterString;

import java.util.List;

@Entity(tableName = "user_table")
public class User {

    @PrimaryKey()
    @NonNull
    public String userId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "lastname")
    public String lastName;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "phone_number")
    public String phoneNumber;

    @ColumnInfo(name = "streak")
    @TypeConverters(DataConverterString.class)
    public List<String> streaks;

    @ColumnInfo(name = "is_baned")
    public boolean isBaned;

    @ColumnInfo(name = "fcm_token")
    public String fcmToken;

    public User(String userId, String name, String lastName, String email, String phoneNumber, List<String> streaks, boolean isBaned, String fcmToken) {
        this.userId = userId;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.streaks = streaks;
        this.isBaned = isBaned;
        this.fcmToken = fcmToken;
    }

    @Ignore
    public User() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<String> getStreaks() {
        return streaks;
    }

    public void setStreaks(List<String> streaks) {
        this.streaks = streaks;
    }

    public boolean isBaned() {
        return isBaned;
    }

    public void setBaned(boolean baned) {
        isBaned = baned;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
