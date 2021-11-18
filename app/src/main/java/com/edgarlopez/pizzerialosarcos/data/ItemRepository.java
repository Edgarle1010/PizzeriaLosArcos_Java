package com.edgarlopez.pizzerialosarcos.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.edgarlopez.pizzerialosarcos.model.Item;
import com.edgarlopez.pizzerialosarcos.ui.ItemRoomDatabase;

import java.util.List;

public class ItemRepository {
    private ItemDao itemDao;
    private LiveData<List<Item>> allItems;

    public ItemRepository(Application application) {
        ItemRoomDatabase db = ItemRoomDatabase.getDatabase(application);
        itemDao = db.itemDao();

        allItems = itemDao.getAllItems();
    }

    public LiveData<List<Item>> getAllData() { return allItems; }
    public void insert(Item item) {
        ItemRoomDatabase.databaseWriteExecutor.execute(() -> itemDao.insert(item));
    }
    public LiveData<Item> get(int id) {
        return itemDao.get(id);
    }
    public void update(Item item) {
        ItemRoomDatabase.databaseWriteExecutor.execute(() -> itemDao.update(item));
    }
    public void delete(Item item) {
        ItemRoomDatabase.databaseWriteExecutor.execute(() -> itemDao.delete(item));
    }
    public void deleteAll() {
        ItemRoomDatabase.databaseWriteExecutor.execute(() -> itemDao.deleteAll());
    }
}
