package com.edgarlopez.pizzerialosarcos.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.edgarlopez.pizzerialosarcos.model.Item;
import com.edgarlopez.pizzerialosarcos.model.User;
import com.edgarlopez.pizzerialosarcos.ui.ItemRoomDatabase;

import java.util.List;

public class UserRepository {
    private UserDao userDao;
    private LiveData<List<User>> allUsers;

    public UserRepository(Application application) {
        ItemRoomDatabase db = ItemRoomDatabase.getDatabase(application);
        userDao = db.userDao();
        allUsers = userDao.getAllUsers();
    }
    public LiveData<List<User>> getAllData() { return allUsers; }
    public void insert(User user) {
        ItemRoomDatabase.databaseWriteExecutor.execute(() -> {
            userDao.insert(user);
        });
    }
    public LiveData<User> get(String id) { return userDao.get(id); }
    public User getOne(String id) { return userDao.getOne(id); }

    public void update(User user) {
        ItemRoomDatabase.databaseWriteExecutor.execute(() -> userDao.update(user));
    }
    public void delete(User user) {
        ItemRoomDatabase.databaseWriteExecutor.execute(() -> userDao.delete(user));
    }
    public void deleteAll() {
        ItemRoomDatabase.databaseWriteExecutor.execute(() -> userDao.deleteAll());
    }
}
