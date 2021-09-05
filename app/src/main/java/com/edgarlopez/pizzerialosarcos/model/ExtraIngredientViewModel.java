package com.edgarlopez.pizzerialosarcos.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class ExtraIngredientViewModel extends ViewModel {
    private final MutableLiveData<ExtraIngredient> selectedExtraIngredient = new MutableLiveData<>();
    private final MutableLiveData<List<ExtraIngredient>> selectedExtraIngredients = new MutableLiveData<>();

    public void setSelectedExtraIngredient(ExtraIngredient extraIngredient) {
        selectedExtraIngredient.setValue(extraIngredient);
    }

    public LiveData<ExtraIngredient> getSelectedExtraIngredient() {
        return selectedExtraIngredient;
    }

    public void setSelectedExtraIngredients(List<ExtraIngredient> extraIngredients) {
        selectedExtraIngredients.setValue(extraIngredients);
    }

    public LiveData<List<ExtraIngredient>> getExtraIngredients() {
        return selectedExtraIngredients;
    }
}
