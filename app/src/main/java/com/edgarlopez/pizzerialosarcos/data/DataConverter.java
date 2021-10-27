package com.edgarlopez.pizzerialosarcos.data;

import androidx.room.TypeConverter;

import com.edgarlopez.pizzerialosarcos.model.ExtraIngredient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class DataConverter {

    static Gson gson = new Gson();

    @TypeConverter
    public static List<ExtraIngredient> stringToExtraIngredientList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<ExtraIngredient>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someExtraIngredientListToString(List<ExtraIngredient> extraIngredients) {
        return gson.toJson(extraIngredients);
    }
}
