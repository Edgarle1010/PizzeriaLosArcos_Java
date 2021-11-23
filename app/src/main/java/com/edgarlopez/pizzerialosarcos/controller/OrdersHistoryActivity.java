package com.edgarlopez.pizzerialosarcos.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.adapter.OnOrderClickListener;
import com.edgarlopez.pizzerialosarcos.model.Food;
import com.edgarlopez.pizzerialosarcos.model.FoodViewModel;
import com.edgarlopez.pizzerialosarcos.model.Order;
import com.edgarlopez.pizzerialosarcos.model.OrderViewModel;
import com.edgarlopez.pizzerialosarcos.ui.FoodRecyclerViewAdapter;
import com.edgarlopez.pizzerialosarcos.ui.OrderRecyclerViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrdersHistoryActivity extends AppCompatActivity implements View.OnClickListener, OnOrderClickListener {
    private ImageButton backImageButton;
    private ProgressBar progressBar;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private OrderRecyclerViewAdapter orderRecyclerAdapter;
    private RecyclerView activeOrdersRecyclerView;
    private RecyclerView completeOrdersRecyclerView;
    private List<Order> orderList;
    private List<Order> completeOrderList;

    private CollectionReference collectionReference = db.collection("orders");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_history);

        backImageButton = findViewById(R.id.back_button_orders_history);
        progressBar = findViewById(R.id.orders_history_activity_progress);
        activeOrdersRecyclerView = findViewById(R.id.active_orders_recycler_view);
        completeOrdersRecyclerView = findViewById(R.id.complete_orders_recycler_view);

        backImageButton.setOnClickListener(this);

        orderList = new ArrayList<>();
        completeOrderList = new ArrayList<>();

        activeOrdersRecyclerView.setHasFixedSize(true);
        activeOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(OrdersHistoryActivity.this));
        completeOrdersRecyclerView.setHasFixedSize(true);
        completeOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(OrdersHistoryActivity.this));

        loadOrdersList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_orders_history:
                finish();
                break;
        }
    }

    private void loadOrdersList() {
        progressBar.setVisibility(View.VISIBLE);

        collectionReference
                .orderBy("date")
                .addSnapshotListener((value, error) -> {
                    progressBar.setVisibility(View.GONE);
                    if (error != null) {
                        System.err.println("Listen failed: " + error);
                        return;
                    }

                    if (value != null) {
                        orderList.clear();
                        completeOrderList.clear();
                        for (QueryDocumentSnapshot orders : value) {
                            Order order = orders.toObject(Order.class);

                            if (!order.isComplete()) {
                                orderList.add(order);
                            } else {
                                completeOrderList.add(order);
                            }
                        }

                        orderRecyclerAdapter = new OrderRecyclerViewAdapter(OrdersHistoryActivity.this, orderList, this);
                        activeOrdersRecyclerView.setAdapter(orderRecyclerAdapter);

                        orderRecyclerAdapter = new OrderRecyclerViewAdapter(OrdersHistoryActivity.this, completeOrderList, this);
                        completeOrdersRecyclerView.setAdapter(orderRecyclerAdapter);

                    } else {
                        System.out.print("Current data: null");
                    }
                });
    }

    @Override
    public void onOrderClicked(Order order) {
        Log.d("Order", "onOrderClicked: " + order.getFolio());
    }
}