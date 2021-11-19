package com.edgarlopez.pizzerialosarcos.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.edgarlopez.pizzerialosarcos.model.Item;
import com.edgarlopez.pizzerialosarcos.model.User;

import java.util.List;

@Dao
public interface UserDao {
    //CRUD
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(User user);

    @Query("DELETE FROM user_table")
    void deleteAll();

    @Query("SELECT * FROM user_table ORDER BY name ASC")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM user_table WHERE user_table.userId == :id")
    LiveData<User> get(String id);

    @Query("SELECT * FROM user_table WHERE user_table.userId == :id")
    User getOne(String id);

    @Update
    void update(User user);

    @Delete
    void delete(User user);
}
