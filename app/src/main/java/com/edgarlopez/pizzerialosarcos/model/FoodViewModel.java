package com.edgarlopez.pizzerialosarcos.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class FoodViewModel extends ViewModel {
    private final MutableLiveData<Food> selectedFood = new MutableLiveData<>();
    private final MutableLiveData<List<Food>> selectedFoods = new MutableLiveData<>();

    public void setSelectedFood(Food food) {
        selectedFood.setValue(food);
    }

    public LiveData<Food> getSelectedFood() {
        return selectedFood;
    }

    public void setSelectedFoods(List<Food> foods) {
        selectedFoods.setValue(foods);
    }

    public LiveData<List<Food>> getFoods() {
        return selectedFoods;
    }
}
