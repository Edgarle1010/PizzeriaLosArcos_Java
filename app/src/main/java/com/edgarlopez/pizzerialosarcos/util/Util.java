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

    public static final String PIZZA = "pizza";
    public static final String PIZZA_TITLE = "Pizzas";
    public static final String BURGER = "burger";
    public static final String BURGER_TITLE = "Hamburguesas";
    public static final String SALAD = "salad";
    public static final String SALAD_TITLE = "Ensaladas";
    public static final String PLATILLO = "platillo";
    public static final String PLATILLO_TITLE = "Platillos";
    public static final String SEA_FOOD = "seaFood";
    public static final String SEA_FOOD_TITLE = "Mariscos";
    public static final String BREAKFAST = "breakfast";
    public static final String BREAKFAST_TITLE = "Desayunos";
    public static final String DRINKS = "drinks";
    public static final String DRINKS_TITLE = "Bebidas";

    public static final String HOTDOG_ID = "burgerHotDog";
    public static final String HOTDOG_FOOD_TYPE = "hotdog";
    public static final String FRENCH_FRIES_ID = "burgerPapasFrancesa";
    public static final String SPAGHETTI_ID = "platilloSpaghetti";
    public static final String SPAGHETTI_FOOD_TYPE = "spaghetti";
    public static final String TORTILLA_SOUP_ID = "platilloSopaTortilla";
    public static final String TORTILLA_SOUP_FOOD_TYPE = "tortillaSoup";
    public static final String ENCHILADAS_ID = "Enchiladas";
    public static final String ENCHILADAS_FOOD_TYPE = "enchiladas";
    public static final String SHRIMP_ID = "seaFoodCoctelCamarones";
    public static final String SHRIMP_FOOD_TYPE = "seaFood";
    public static final String FISH_STEAK_ID = "seaFoodFileteEmpanizado";
    public static final String FISH_STEAK_TITLE = "Filete";
    public static final String FISH_STEAK_FOOD_TYPE = "fishSteak";
    public static final String RANCH_SHRIMP_ID = "seaFoodCamaronesRancheros";
    public static final String RANCH_SHRIMP_FOOD_TYPE = "ranchShrimp";
    public static final String EGGS_INGREDIENTS_ID = "ingredientB";
    public static final String SODA_ID = "drinksRefresco";
    public static final String SODA_FOOD_TYPE = "soda";
}
