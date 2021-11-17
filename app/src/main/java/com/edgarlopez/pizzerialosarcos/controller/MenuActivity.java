
package com.edgarlopez.pizzerialosarcos.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.adapter.OnAddExtraIngredientClickListener;
import com.edgarlopez.pizzerialosarcos.adapter.OnAddHalfFoodClickListener;
import com.edgarlopez.pizzerialosarcos.adapter.OnCancelExtraIngredientClickListener;
import com.edgarlopez.pizzerialosarcos.adapter.OnCancelHalfFoodClickListener;
import com.edgarlopez.pizzerialosarcos.model.ExtraIngredient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MenuActivity extends AppCompatActivity implements OnAddExtraIngredientClickListener,
        OnCancelExtraIngredientClickListener,
        OnAddHalfFoodClickListener,
        OnCancelHalfFoodClickListener {

    private ProgressBar progressBar;
    public List<ExtraIngredient> currExtraIngredients;

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

    }

    @Override
    public void onAddExtraIngredientClicked() {
        OrderDetailsFragment frag = (OrderDetailsFragment)
                getSupportFragmentManager().findFragmentById(R.id.menu_frame);
        frag.addExtraIngredientClicked();
    }

    @Override
    public void onCancelExtraIngredientClicked() {
        OrderDetailsFragment frag = (OrderDetailsFragment)
                getSupportFragmentManager().findFragmentById(R.id.menu_frame);
        frag.cancelExtraIngredientClicked();
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
            foodListFragment.getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
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
            //Toast.makeText(MenuActivity.this, "All is good", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        checkPlayServices();

    }

}