
package com.edgarlopez.pizzerialosarcos.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.ProgressBar;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.adapter.OnAddExtraIngredientClickListener;
import com.edgarlopez.pizzerialosarcos.adapter.OnCancelExtraIngredientClickListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuActivity extends AppCompatActivity implements OnAddExtraIngredientClickListener, OnCancelExtraIngredientClickListener {
    private ProgressBar progressBar;

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
    public void onAddClicked() {
        OrderDetailsFragment frag = (OrderDetailsFragment)
                getSupportFragmentManager().findFragmentById(R.id.menu_frame);
        frag.addButtonClicked();
    }

    @Override
    public void onCancelClicked() {
        OrderDetailsFragment frag = (OrderDetailsFragment)
                getSupportFragmentManager().findFragmentById(R.id.menu_frame);
        frag.cancelButtonClicked();
    }

    @Override
    public void onBackPressed() {
        ExtraIngredientFragment myFragment = (ExtraIngredientFragment) getSupportFragmentManager().findFragmentById(R.id.extra_ingredient_frame);
        if (myFragment != null && myFragment.isVisible()) {
            myFragment.cancelButtonPressed();
        }else {
            super.onBackPressed();
        }

    }
}