
package com.edgarlopez.pizzerialosarcos.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.adapter.OnAddExtraIngredientClickListener;
import com.edgarlopez.pizzerialosarcos.adapter.OnAddHalfFoodClickListener;
import com.edgarlopez.pizzerialosarcos.adapter.OnCancelExtraIngredientClickListener;
import com.edgarlopez.pizzerialosarcos.adapter.OnCancelHalfFoodClickListener;
import com.edgarlopez.pizzerialosarcos.model.ExtraIngredient;
import com.edgarlopez.pizzerialosarcos.model.Item;
import com.edgarlopez.pizzerialosarcos.model.ItemViewModel;
import com.edgarlopez.pizzerialosarcos.model.User;
import com.edgarlopez.pizzerialosarcos.model.UserViewModel;
import com.edgarlopez.pizzerialosarcos.ui.ItemRecyclerViewAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class MenuActivity extends AppCompatActivity implements OnAddExtraIngredientClickListener,
        OnCancelExtraIngredientClickListener,
        OnAddHalfFoodClickListener,
        OnCancelHalfFoodClickListener {

    private ProgressBar progressBar;
    public List<ExtraIngredient> currExtraIngredients;

    private ItemViewModel itemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, MenuFragment.newInstance())
                .commit();

        progressBar = findViewById(R.id.menu_activity_progress);

        BottomNavigationView bottomNavigationView =
                findViewById(R.id.bottom_navigation);

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

            int id = item.getItemId();
            if (id == R.id.menu_nav_button) {
                //show the menu view
                selectedFragment = MenuFragment.newInstance();

            }else if (id == R.id.more_nav_button) {
                selectedFragment = MoreFragment.newInstance();

            }else if (id == R.id.cart_nav_button) {
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
        }
    }

    @Override
    public void onAddHalfFoodClicked() {
        OrderDetailsFragment frag = (OrderDetailsFragment)
                getSupportFragmentManager().findFragmentById(R.id.menu_frame);
        frag.addHalfFoodClicked();
    }

    @Override
    public void onCancelHalfFoodClicked() {
        OrderDetailsFragment frag = (OrderDetailsFragment)
                getSupportFragmentManager().findFragmentById(R.id.menu_frame);
        frag.cancelHalfFoodClicked();
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
}