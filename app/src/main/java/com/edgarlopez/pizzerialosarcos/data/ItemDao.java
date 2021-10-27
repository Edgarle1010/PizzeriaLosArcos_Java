package com.edgarlopez.pizzerialosarcos.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.edgarlopez.pizzerialosarcos.model.Item;

import java.util.List;

@Dao
public interface ItemDao {
    // CRUD
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Item item);

    @Query("DELETE FROM item_table")
    void deleteAll();

    @Query("SELECT * FROM item_table ORDER BY title ASC")
    LiveData<List<Item>> getAllItems();

    @Query("SELECT * FROM item_table WHERE item_table.id == :id")
    LiveData<Item> get(int id);

    @Update
    void update(Item item);

    @Delete
    void delete(Item item);
}
