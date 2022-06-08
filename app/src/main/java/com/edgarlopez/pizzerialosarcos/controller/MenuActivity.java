
package com.edgarlopez.pizzerialosarcos.controller;

import static com.edgarlopez.pizzerialosarcos.util.Util.NOTIFICATIONS_COLLECTION;
import static com.edgarlopez.pizzerialosarcos.util.Util.USERS_COLLECTION;
import static com.edgarlopez.pizzerialosarcos.util.Util.USER_TOKEN;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.adapter.OnAddExtraIngredientClickListener;
import com.edgarlopez.pizzerialosarcos.adapter.OnAddHalfFoodClickListener;
import com.edgarlopez.pizzerialosarcos.adapter.OnCancelExtraIngredientClickListener;
import com.edgarlopez.pizzerialosarcos.adapter.OnCancelHalfFoodClickListener;
import com.edgarlopez.pizzerialosarcos.model.ExtraIngredient;
import com.edgarlopez.pizzerialosarcos.model.ItemViewModel;
import com.edgarlopez.pizzerialosarcos.model.Notification;
import com.edgarlopez.pizzerialosarcos.model.NotificationViewModel;
import com.edgarlopez.pizzerialosarcos.ui.NotificationRecyclerViewAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity implements OnAddExtraIngredientClickListener,
        OnCancelExtraIngredientClickListener,
        OnAddHalfFoodClickListener,
        OnCancelHalfFoodClickListener, View.OnClickListener {
    private ProgressBar progressBar;
    public List<ExtraIngredient> currExtraIngredients;
    private Button notificationsButton,
            ordersHistoryImageButton;
    private TextView navBarTitleTextView;
    private LinearLayout navBarLayout;
    private BottomNavigationView bottomNavigationView;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private Query query = db.collection(NOTIFICATIONS_COLLECTION);
    private ListenerRegistration registration;

    private ItemViewModel itemViewModel;

    private List<Notification> notificationList;
    private BadgeDrawable badgeDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, MenuFragment.newInstance())
                .commit();

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        progressBar = findViewById(R.id.menu_activity_progress);
        navBarLayout = findViewById(R.id.nav_bar_layout);
        notificationsButton = findViewById(R.id.notifications_icon);
        ordersHistoryImageButton = findViewById(R.id.orders_in_process_icon);
        navBarTitleTextView = findViewById(R.id.nav_bar_title_text_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        notificationsButton.setOnClickListener(this);
        ordersHistoryImageButton.setOnClickListener(this);

        notificationList = new ArrayList<>();
        badgeDrawable =  BadgeDrawable.create(this);

        itemViewModel = new ViewModelProvider.AndroidViewModelFactory(MenuActivity.this
                .getApplication())
                .create(ItemViewModel.class);

        itemViewModel.getAllItems().observe(MenuActivity.this, items -> {
            if (items.size() == 0) {
                bottomNavigationView.removeBadge(R.id.cart_nav_button);
            } else {
                BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.cart_nav_button);
                badge.setNumber(items.size());
            }
        });


        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            navBarTitleTextView.setText(item.getTitle());
            ordersHistoryImageButton.setVisibility(View.GONE);

            int id = item.getItemId();
            if (id == R.id.menu_nav_button) {
                selectedFragment = MenuFragment.newInstance();

            } else if (id == R.id.more_nav_button) {
                selectedFragment = MoreFragment.newInstance();

            } else if (id == R.id.cart_nav_button) {
                ordersHistoryImageButton.setVisibility(View.VISIBLE);
                selectedFragment = ShoppingCartFragment.newInstance();
            }
            assert selectedFragment != null;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.menu_frame, selectedFragment)
                    .commit();

            return true;
        });

        bottomNavigationView.setOnItemReselectedListener(item -> {

        });
    }

    @SuppressLint("UnsafeOptInUsageError")
    @Override
    protected void onResume() {
        super.onResume();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("NOTIFICATION_TAG", "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    String token = task.getResult();

                    db.collection(USERS_COLLECTION).document(user.getUid()).update(USER_TOKEN, token)
                            .addOnCompleteListener(updateTask -> {
                                Log.d("USER_TOKEN", "DocumentSnapshot successfully updated!");
                            })
                            .addOnFailureListener(e -> {
                                Log.w("USER_TOKEN", "Error updating document", e);
                            });

                    progressBar.setVisibility(View.VISIBLE);
                    registration = query.addSnapshotListener((value, error) -> {
                        progressBar.setVisibility(View.GONE);
                        if (error != null) {
                            Toast.makeText(this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }

                        notificationList.clear();
                        if (value != null && !value.isEmpty()) {
                            for (QueryDocumentSnapshot notifications : value) {
                                Notification notification = notifications.toObject(Notification.class);

                                if (notification.userToken.equals(token) && !notification.viewed) {
                                    notificationList.add(notification);
                                }
                            }
                        }

                        badgeDrawable.setVisible(true);
                        badgeDrawable.setVerticalOffset(10);
                        badgeDrawable.setHorizontalOffset(10);

                        if (notificationList.size() == 0) {
                            badgeDrawable.setNumber(notificationList.size());
                            BadgeUtils.detachBadgeDrawable(badgeDrawable, notificationsButton);
                        } else {
                            badgeDrawable.setNumber(notificationList.size());
                            BadgeUtils.attachBadgeDrawable(badgeDrawable, notificationsButton);
                        }
                    });
                });
    }

    @Override
    public void onAddExtraIngredientClicked() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.menu_frame);

        if (fragment instanceof OrderDetailsFragment) {
            ((OrderDetailsFragment) fragment).addExtraIngredientClicked();
        } else if (fragment instanceof OrderDetailsSecondaryFragment) {
            ((OrderDetailsSecondaryFragment) fragment).addExtraIngredientClicked();
        } else if (fragment instanceof OrderDetailsTertiaryFragment) {
            ((OrderDetailsTertiaryFragment) fragment).addExtraIngredientClicked();
        } else if (fragment instanceof OrderDetailsFourFragment) {
            ((OrderDetailsFourFragment) fragment).addExtraIngredientClicked();
        } else if (fragment instanceof OrderDetailsFiveFragment) {
            ((OrderDetailsFiveFragment) fragment).addExtraIngredientClicked();
        }
    }

    @Override
    public void onCancelExtraIngredientClicked() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.menu_frame);

        if (fragment instanceof OrderDetailsFragment) {
            ((OrderDetailsFragment) fragment).cancelExtraIngredientClicked();
        } else if (fragment instanceof OrderDetailsSecondaryFragment) {
            ((OrderDetailsSecondaryFragment) fragment).cancelExtraIngredientClicked();
        } else if (fragment instanceof OrderDetailsTertiaryFragment) {
            ((OrderDetailsTertiaryFragment) fragment).cancelExtraIngredientClicked();
        } else if (fragment instanceof OrderDetailsFourFragment) {
            ((OrderDetailsFourFragment) fragment).cancelExtraIngredientClicked();
        } else if (fragment instanceof OrderDetailsFiveFragment) {
            ((OrderDetailsFiveFragment) fragment).cancelExtraIngredientClicked();
        }
    }

    @Override
    public void onAddHalfFoodClicked() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.menu_frame);

        if (fragment instanceof OrderDetailsFragment) {
            ((OrderDetailsFragment) fragment).addHalfFoodClicked();
        } else if (fragment instanceof OrderDetailsFiveFragment) {
            ((OrderDetailsFiveFragment) fragment).addHalfFoodClicked();
        } else if (fragment instanceof OrderDetailsDrinksFragment) {
            ((OrderDetailsDrinksFragment) fragment).addHalfFoodClicked();
        }
    }

    @Override
    public void onCancelHalfFoodClicked() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.menu_frame);

        if (fragment instanceof OrderDetailsFragment) {
            ((OrderDetailsFragment) fragment).cancelHalfFoodClicked();
        } else if (fragment instanceof OrderDetailsFiveFragment) {
            ((OrderDetailsFiveFragment) fragment).cancelHalfFoodClicked();
        } else if (fragment instanceof OrderDetailsDrinksFragment) {
            ((OrderDetailsDrinksFragment) fragment).cancelHalfFoodClicked();
        }
    }

    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.extras_frame);
        Fragment menuFragment = getSupportFragmentManager().findFragmentById(R.id.menu_frame);

        if (fragment instanceof ExtraIngredientFragment) {
            ExtraIngredientFragment extraIngredientFragment = (ExtraIngredientFragment) getSupportFragmentManager().findFragmentById(R.id.extras_frame);
            extraIngredientFragment.cancelButtonPressed();
        }else if (fragment instanceof HalfFoodFragment) {
            HalfFoodFragment halfFoodFragment = (HalfFoodFragment) getSupportFragmentManager().findFragmentById(R.id.extras_frame);
            halfFoodFragment.cancelButtonPressed();
        }else if (menuFragment instanceof FoodListFragment) {
            FoodListFragment foodListFragment = (FoodListFragment) getSupportFragmentManager().findFragmentById(R.id.menu_frame);
            foodListFragment.requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
            foodListFragment.requireActivity().findViewById(R.id.nav_bar_layout).setVisibility(View.VISIBLE);
            super.onBackPressed();
        }else if (menuFragment instanceof OrderDetailsFragment) {
            OrderDetailsFragment orderDetailsFragment = (OrderDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.menu_frame);
            getSupportFragmentManager().beginTransaction().remove(orderDetailsFragment).commit();
            super.onBackPressed();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int[] sourceCoordinates = new int[2];
            v.getLocationOnScreen(sourceCoordinates);
            float x = ev.getRawX() + v.getLeft() - sourceCoordinates[0];
            float y = ev.getRawY() + v.getTop() - sourceCoordinates[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) {
                ((EditText) v).setCursorVisible(false);
                hideKeyboard(this);
            }

        }
        return super.dispatchTouchEvent(ev);
    }

    private void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null) {
            activity.getWindow().getDecorView();
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
            }
        }
    }

    private void checkPlayServices() {
        int errorCode = GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(this);

        if (errorCode != ConnectionResult.SUCCESS) {
            Dialog errorDialog = GoogleApiAvailability.getInstance()
                    .getErrorDialog(this, errorCode, errorCode, dialogInterface -> {
                        Toast.makeText(MenuActivity.this, "No services", Toast.LENGTH_SHORT).show();
                        finish();
                    });
            errorDialog.show();
        }else {
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        checkPlayServices();

    }

    @Override
    protected void onPause() {
        super.onPause();

        registration.remove();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.notifications_icon:
                startActivity(new Intent(MenuActivity.this,
                        NotificationsActivity.class));
                break;
            case R.id.orders_in_process_icon:
                break;
        }
    }
}