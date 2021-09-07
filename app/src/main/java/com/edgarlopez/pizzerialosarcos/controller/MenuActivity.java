
package com.edgarlopez.pizzerialosarcos.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.adapter.OnAddExtraIngredientClickListener;
import com.edgarlopez.pizzerialosarcos.adapter.OnAddHalfFoodClickListener;
import com.edgarlopez.pizzerialosarcos.adapter.OnCancelExtraIngredientClickListener;
import com.edgarlopez.pizzerialosarcos.adapter.OnCancelHalfFoodClickListener;
import com.edgarlopez.pizzerialosarcos.model.ExtraIngredient;
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

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int id = item.getItemId();
            if (id == R.id.menu_nav_button) {
                //show the menu view
                selectedFragment = MenuFragment.newInstance();

            }else if (id == R.id.more_nav_button) {
                selectedFragment = MoreFragment.newInstance();

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

        if (fragment instanceof ExtraIngredientFragment) {
            ExtraIngredientFragment extraIngredientFragment = (ExtraIngredientFragment) getSupportFragmentManager().findFragmentById(R.id.extras_frame);
            extraIngredientFragment.cancelButtonPressed();
        }else if (fragment instanceof HalfFoodFragment) {
            HalfFoodFragment halfFoodFragment = (HalfFoodFragment) getSupportFragmentManager().findFragmentById(R.id.extras_frame);
            halfFoodFragment.cancelButtonPressed();
        }else {
            super.onBackPressed();
        }
    }
}