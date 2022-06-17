package com.edgarlopez.pizzerialosarcos.controller;

import static com.edgarlopez.pizzerialosarcos.util.Util.NOTIFICATIONS_COLLECTION;
import static com.edgarlopez.pizzerialosarcos.util.Util.NOTIFICATIONS_DATE_SEND;
import static com.edgarlopez.pizzerialosarcos.util.Util.NOTIFICATIONS_VIEWED;
import static com.edgarlopez.pizzerialosarcos.util.Util.USERS_COLLECTION;
import static com.edgarlopez.pizzerialosarcos.util.Util.USER_TOKEN;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.adapter.FirestoreCallbackUserToken;
import com.edgarlopez.pizzerialosarcos.adapter.OnNotificationClickListener;
import com.edgarlopez.pizzerialosarcos.model.Notification;
import com.edgarlopez.pizzerialosarcos.model.NotificationViewModel;
import com.edgarlopez.pizzerialosarcos.ui.NotificationRecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotificationsActivity extends AppCompatActivity implements OnNotificationClickListener {
    private ImageButton backImageButton;
    private RecyclerView recyclerView;
    private TextView emptyListTextView;
    private ProgressBar progressBar;

    private NotificationRecyclerViewAdapter notificationRecyclerAdapter;
    private NotificationViewModel notificationViewModel;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private Query query = db.collection(NOTIFICATIONS_COLLECTION);
    private ListenerRegistration registration;

    private String userToken;
    private List<Notification> notificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        backImageButton = findViewById(R.id.back_button_notifications);
        recyclerView = findViewById(R.id.notifications_recycler_view);
        emptyListTextView = findViewById(R.id.empty_list_notifications);
        progressBar = findViewById(R.id.notifications_progress);

        notificationViewModel = new ViewModelProvider(this)
                .get(NotificationViewModel.class);

        notificationList = new ArrayList<>();

        backImageButton.setOnClickListener(view -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();

        getUserToken(userToken -> {
            progressBar.setVisibility(View.VISIBLE);
            registration = query
                    .orderBy(NOTIFICATIONS_DATE_SEND, Query.Direction.DESCENDING)
                    .addSnapshotListener((value, error) -> {
                progressBar.setVisibility(View.GONE);
                if (error != null) {
                    Toast.makeText(this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

                if (value != null && !value.isEmpty()) {
                    notificationList.clear();
                    for (QueryDocumentSnapshot notifications : value) {
                        Notification notification = notifications.toObject(Notification.class);

                        if (notification.phoneNumber.equals(user.getPhoneNumber())) {
                            notificationList.add(notification);
                        }
                    }

                    notificationViewModel.setSelectedNotifications(notificationList);

                    if (notificationViewModel.getNotifications().getValue() != null) {
                        notificationList = notificationViewModel.getNotifications().getValue();

                        notificationRecyclerAdapter = new NotificationRecyclerViewAdapter(this,
                                notificationList,
                                this);

                        recyclerView.setHasFixedSize(true);
                        recyclerView.setAdapter(notificationRecyclerAdapter);
                    }
                }

                if (notificationList.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyListTextView.setVisibility(View.VISIBLE);
                } else {
                    emptyListTextView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            });
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        registration.remove();
    }

    private void getUserToken(FirestoreCallbackUserToken firestoreCallbackUserToken) {
        progressBar.setVisibility(View.VISIBLE);
        db.collection(USERS_COLLECTION).document(user.getUid()).get().addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                if (doc.exists()) {
                    userToken = doc.getString(USER_TOKEN);
                    firestoreCallbackUserToken.onCallbackUserToken(userToken);
                }
            } else {
                Toast.makeText(this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onNotificationClicked(Notification notification) {

    }
}