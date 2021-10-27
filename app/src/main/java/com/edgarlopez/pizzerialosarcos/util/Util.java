package com.edgarlopez.pizzerialosarcos.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    //*****************************************************************
    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public static final String FOOD_TYPE = "FOOD_TYPE";
    public static final String FOOD_TITLE = "FOOD_TITLE";
    public static final String FOOD_ITEM = "FOOD_ITEM";
    public static final String FOOD_SIZE = "FOOD_SIZE";

}
