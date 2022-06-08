package com.edgarlopez.pizzerialosarcos.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class NotificationViewModel extends ViewModel {
    private final MutableLiveData<Notification> selectedNotification = new MutableLiveData<>();
    private final MutableLiveData<List<Notification>> selectedNotifications = new MutableLiveData<>();

    public void setSelectedNotification(Notification notification) {
        selectedNotification.setValue(notification);
    }

    public LiveData<Notification> getSelectedNotification() {
        return selectedNotification;
    }

    public void setSelectedNotifications(List<Notification> notifications) {
        selectedNotifications.setValue(notifications);
    }

    public LiveData<List<Notification>> getNotifications() {
        return selectedNotifications;
    }
}
