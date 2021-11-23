package com.edgarlopez.pizzerialosarcos.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class OrderViewModel extends ViewModel {
    private final MutableLiveData<Order> selectedOrder = new MutableLiveData<>();
    private final MutableLiveData<List<Order>> selectedOrders = new MutableLiveData<>();

    public void setSelectedOrder(Order order) {
        selectedOrder.setValue(order);
    }

    public LiveData<Order> getSelectedOrder() {
        return selectedOrder;
    }

    public void setSelectedOrders(List<Order> orders) {
        selectedOrders.setValue(orders);
    }

    public LiveData<List<Order>> getOrders() {
        return selectedOrders;
    }
}
