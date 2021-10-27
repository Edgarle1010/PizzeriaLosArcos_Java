package com.edgarlopez.pizzerialosarcos.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.edgarlopez.pizzerialosarcos.data.ItemRepository;

import java.util.List;

public class ItemViewModel extends AndroidViewModel {

    public static ItemRepository repository;
    public final LiveData<List<Item>> allItems;

    public ItemViewModel(@NonNull Application application) {
        super(application);
        repository = new ItemRepository(application);
        allItems = repository.getAllData();
    }

    public LiveData<List<Item>> getAllItems() { return allItems; }
    public static void insert(Item item) { repository.insert(item); }

    public LiveData<Item> get(int id) { return repository.get(id); }
    public static void update(Item item) { repository.update(item); }
    public static void delete(Item item) { repository.delete(item); }
}
