package com.edgarlopez.pizzerialosarcos.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.edgarlopez.pizzerialosarcos.data.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    public static UserRepository repository;
    public final LiveData<List<User>> allUsers;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        allUsers = repository.getAllData();
    }

    public LiveData<List<User>> getAllUsers() { return allUsers; }
    public static void insert(User user) { repository.insert(user); }

    public LiveData<User> get(String id) { return repository.get(id); }
    public static void update(User user) { repository.update(user); }
    public static void delete(User user) { repository.delete(user); }
    public static void deleteAll() { repository.deleteAll(); }
}
