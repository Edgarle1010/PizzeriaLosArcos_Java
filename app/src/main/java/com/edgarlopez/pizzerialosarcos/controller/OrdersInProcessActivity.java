package com.edgarlopez.pizzerialosarcos.controller;

import static com.edgarlopez.pizzerialosarcos.util.Util.ORDERS_CLIENT;
import static com.edgarlopez.pizzerialosarcos.util.Util.ORDERS_COLLECTIONS;
import static com.edgarlopez.pizzerialosarcos.util.Util.ORDERS_DATE_REQUEST;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.adapter.OnOrderClickListener;
import com.edgarlopez.pizzerialosarcos.model.Notification;
import com.edgarlopez.pizzerialosarcos.model.Order;
import com.edgarlopez.pizzerialosarcos.model.OrderViewModel;
import com.edgarlopez.pizzerialosarcos.ui.NotificationRecyclerViewAdapter;
import com.edgarlopez.pizzerialosarcos.ui.OrderRecyclerViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrdersInProcessActivity extends AppCompatActivity implements OnOrderClickListener {
    private ImageButton backImageButton;
    private RecyclerView recyclerView;
    private TextView emptyListTextView;
    private ProgressBar progressBar;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private OrderRecyclerViewAdapter orderRecyclerAdapter;
    private OrderViewModel orderViewModel;

    private Query query = db.collection(ORDERS_COLLECTIONS);
    private ListenerRegistration registration;

    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_in_process);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        backImageButton = findViewById(R.id.back_button_orders_in_process);
        recyclerView = findViewById(R.id.orders_in_process_recycler_view);
        emptyListTextView = findViewById(R.id.empty_list_orders_in_process);
        progressBar = findViewById(R.id.orders_in_process_progress);

        orderViewModel = new ViewModelProvider(this)
                .get(OrderViewModel.class);
        orderList = new ArrayList<>();

        backImageButton.setOnClickListener(view -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();

        progressBar.setVisibility(View.VISIBLE);
        registration = query
                .orderBy(ORDERS_DATE_REQUEST)
                .addSnapshotListener((value, error) -> {
                    progressBar.setVisibility(View.GONE);
                    if (error != null) {
                        Toast.makeText(this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                    if (value != null && !value.isEmpty()) {
                        orderList.clear();
                        for (QueryDocumentSnapshot orders : value) {
                            Order order = orders.toObject(Order.class);

                            if (!order.isComplete() && order.getClient().equals(user.getPhoneNumber())) {
                                orderList.add(order);
                            }
                        }

                        orderViewModel.setSelectedOrders(orderList);

                        if (orderViewModel.getOrders().getValue() != null) {
                            orderList = orderViewModel.getOrders().getValue();

                            orderRecyclerAdapter = new OrderRecyclerViewAdapter(this,
                                    orderList,
                                    this);

                            recyclerView.setHasFixedSize(true);
                            recyclerView.setAdapter(orderRecyclerAdapter);
                        }
                    }

                    if (orderList.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        emptyListTextView.setVisibility(View.VISIBLE);
                    } else {
                        emptyListTextView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();

        registration.remove();
    }

    @Override
    public void onOrderClicked(Order order) {

    }
}