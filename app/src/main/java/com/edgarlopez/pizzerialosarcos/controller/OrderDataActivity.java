package com.edgarlopez.pizzerialosarcos.controller;

import static com.edgarlopez.pizzerialosarcos.util.Util.ORDERS_COLLECTIONS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.model.ItemViewModel;
import com.edgarlopez.pizzerialosarcos.model.Order;
import com.edgarlopez.pizzerialosarcos.model.OrderViewModel;
import com.edgarlopez.pizzerialosarcos.ui.ItemRecyclerViewAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.text.SimpleDateFormat;

public class OrderDataActivity extends AppCompatActivity {
    private ImageButton backImageButton;
    private TextView folioTextView;
    private TextView dateTextView;
    private TextView dateRequestTextView;
    private TextView dateProcessedTextView;
    private TextView dateFinishedTextView;
    private TextView dateDeliveredTitleTextView;
    private TextView dateDeliveredTextView;
    private TextView statusTextView;
    private TextView totalTextView;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection(ORDERS_COLLECTIONS);
    private ListenerRegistration registration;

    private ItemViewModel itemViewModel;
    private ItemRecyclerViewAdapter itemRecyclerViewAdapter;

    private Order order;
    private String folio;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
    SimpleDateFormat shortDateFormat = new SimpleDateFormat("hh:mm aa");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_data);

        backImageButton = findViewById(R.id.back_button_order_data);
        folioTextView = findViewById(R.id.folio_order_data);
        dateTextView = findViewById(R.id.date_order_data_text_view);
        dateRequestTextView = findViewById(R.id.date_request_order_data_text_view);
        dateProcessedTextView = findViewById(R.id.date_processed_order_data_text_view);
        dateFinishedTextView = findViewById(R.id.date_finished_order_data_text_view);
        dateDeliveredTitleTextView = findViewById(R.id.date_delivered_title_order_data_text_view);
        dateDeliveredTextView = findViewById(R.id.date_delivered_order_data_text_view);
        statusTextView = findViewById(R.id.status_order_data_text_view);
        totalTextView = findViewById(R.id.total_order_data_text_view);
        recyclerView = findViewById(R.id.order_data_recycler_view);
        progressBar = findViewById(R.id.order_data_activity_progress);

        folio = getIntent().getStringExtra("folio");

        itemViewModel = new ViewModelProvider(this)
                .get(ItemViewModel.class);

        backImageButton.setOnClickListener(view -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (folio != null) {
            progressBar.setVisibility(View.VISIBLE);
            registration = collectionReference
                    .document(folio)
                    .addSnapshotListener((value, error) -> {
                        if (error != null) {
                            Toast.makeText(this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }

                        if (value != null && value.exists()) {
                            order = value.toObject(Order.class);

                            setView(order);
                        }
                    });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        registration.remove();
    }

    public void setView(Order order) {
        folioTextView.setText(order.getFolio());
        dateTextView.setText(dateFormat.format(order.getDateRequest() * 1000));
        dateRequestTextView.setText(shortDateFormat.format(order.getDateRequest() * 1000));

        double dateProcessed = order.getDateProcessed();
        if (dateProcessed != 0.0) {
            dateProcessedTextView.setText(shortDateFormat.format(dateProcessed * 1000));
        }

        double dateFinished = order.getDateFinished();
        if (dateFinished != 0.0) {
            dateFinishedTextView.setText(shortDateFormat.format(dateFinished * 1000));
        }

        double dateDelivered = order.getDateDelivered();
        if (dateDelivered != 0.0) {
            dateDeliveredTextView.setText(shortDateFormat.format(dateDelivered * 1000));
        }

        double dateCanceled = order.getDateCanceled();
        if (dateCanceled != 0.0) {
            dateDeliveredTitleTextView.setText("Hora de cancelación");
            dateDeliveredTextView.setText(shortDateFormat.format(dateCanceled * 1000));
        }

        statusTextView.setText(order.getStatus());
        totalTextView.setText("Total: $" + String.format("%.2f", order.getTotalPrice()));

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemRecyclerViewAdapter = new ItemRecyclerViewAdapter(order.getItemList(), this);
        recyclerView.setAdapter(itemRecyclerViewAdapter);
    }
}