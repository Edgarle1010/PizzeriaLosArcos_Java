package com.edgarlopez.pizzerialosarcos.data;

import androidx.room.TypeConverter;

import com.edgarlopez.pizzerialosarcos.model.ExtraIngredient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class DataConverterString {

    static Gson gson = new Gson();

    @TypeConverter
    public static List<String> stringToStringList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<String>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someStringToString(List<String> string) {
        return gson.toJson(string);
    }
}
