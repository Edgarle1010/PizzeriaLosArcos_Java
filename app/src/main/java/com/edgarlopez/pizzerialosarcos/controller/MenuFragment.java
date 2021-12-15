package com.edgarlopez.pizzerialosarcos.controller;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.edgarlopez.pizzerialosarcos.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import static com.edgarlopez.pizzerialosarcos.util.Util.BREAKFAST;
import static com.edgarlopez.pizzerialosarcos.util.Util.BREAKFAST_TITLE;
import static com.edgarlopez.pizzerialosarcos.util.Util.BURGER;
import static com.edgarlopez.pizzerialosarcos.util.Util.BURGER_TITLE;
import static com.edgarlopez.pizzerialosarcos.util.Util.FOOD_TITLE;
import static com.edgarlopez.pizzerialosarcos.util.Util.FOOD_TYPE;
import static com.edgarlopez.pizzerialosarcos.util.Util.PIZZA;
import static com.edgarlopez.pizzerialosarcos.util.Util.PIZZA_TITLE;
import static com.edgarlopez.pizzerialosarcos.util.Util.PLATILLO;
import static com.edgarlopez.pizzerialosarcos.util.Util.PLATILLO_TITLE;
import static com.edgarlopez.pizzerialosarcos.util.Util.SALAD;
import static com.edgarlopez.pizzerialosarcos.util.Util.SALAD_TITLE;
import static com.edgarlopez.pizzerialosarcos.util.Util.SEA_FOOD;
import static com.edgarlopez.pizzerialosarcos.util.Util.SEA_FOOD_TITLE;

public class MenuFragment extends Fragment implements View.OnClickListener {
    private ImageView pizzasMenu,
            burgersMenu,
            saladsMenu,
            foodDishesMenu,
            seaFoodMenu,
            breakfastsMenu,
            drinksMenu,
            dessertsMenu,
            snowMilkshakesMenu,
            kidsMenu;

    public MenuFragment() {
    }

    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        pizzasMenu = view.findViewById(R.id.pizzas_image_menu);
        burgersMenu = view.findViewById(R.id.burgers_image_menu);
        saladsMenu = view.findViewById(R.id.salads_image_menu);
        foodDishesMenu = view.findViewById(R.id.food_dishes_image_menu);
        seaFoodMenu = view.findViewById(R.id.seafood_image_menu);
        breakfastsMenu = view.findViewById(R.id.breakfasts_image_menu);
        drinksMenu = view.findViewById(R.id.drinks_image_menu);
        dessertsMenu = view.findViewById(R.id.desserts_image_menu);
        snowMilkshakesMenu = view.findViewById(R.id.snow_milkshakes_image_menu);
        kidsMenu = view.findViewById(R.id.kids_image_menu);

        loadImageMenu();

        pizzasMenu.setOnClickListener(this);
        burgersMenu.setOnClickListener(this);
        saladsMenu.setOnClickListener(this);
        foodDishesMenu.setOnClickListener(this);
        seaFoodMenu.setOnClickListener(this);
        breakfastsMenu.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void loadImageMenu() {
        Picasso.get()
                .load(R.drawable.pizzas_menu)
                .resize(800,300)
                .into(pizzasMenu);

        Picasso.get()
                .load(R.drawable.hamburguesas_menu)
                .resize(400,300)
                .into(burgersMenu);

        Picasso.get()
                .load(R.drawable.ensaladas_menu)
                .resize(400,300)
                .into(saladsMenu);

        Picasso.get()
                .load(R.drawable.platillos_menu)
                .resize(400,300)
                .into(foodDishesMenu);

        Picasso.get()
                .load(R.drawable.mariscos_menu)
                .resize(400,300)
                .into(seaFoodMenu);

        Picasso.get()
                .load(R.drawable.desayunos_menu)
                .resize(400,300)
                .into(breakfastsMenu);

        Picasso.get()
                .load(R.drawable.bebidas_menu)
                .resize(400,300)
                .into(drinksMenu);

        Picasso.get()
                .load(R.drawable.postres_menu)
                .resize(400,300)
                .into(dessertsMenu);

        Picasso.get()
                .load(R.drawable.nieves_malteadas_menu)
                .resize(400,300)
                .into(snowMilkshakesMenu);

        Picasso.get()
                .load(R.drawable.kids_menu)
                .resize(800,300)
                .into(kidsMenu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Fragment fragment = new FoodListFragment();
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.pizzas_image_menu:
                bundle.putString(FOOD_TYPE, PIZZA);
                bundle.putString(FOOD_TITLE, PIZZA_TITLE);
                fragment.setArguments(bundle);
                break;
            case R.id.burgers_image_menu:
                bundle.putString(FOOD_TYPE, BURGER);
                bundle.putString(FOOD_TITLE, BURGER_TITLE);
                fragment.setArguments(bundle);
                break;
            case R.id.salads_image_menu:
                bundle.putString(FOOD_TYPE, SALAD);
                bundle.putString(FOOD_TITLE, SALAD_TITLE);
                fragment.setArguments(bundle);
                break;
            case R.id.food_dishes_image_menu:
                bundle.putString(FOOD_TYPE, PLATILLO);
                bundle.putString(FOOD_TITLE, PLATILLO_TITLE);
                fragment.setArguments(bundle);
                break;
            case R.id.seafood_image_menu:
                bundle.putString(FOOD_TYPE, SEA_FOOD);
                bundle.putString(FOOD_TITLE, SEA_FOOD_TITLE);
                fragment.setArguments(bundle);
                break;
            case R.id.breakfasts_image_menu:
                bundle.putString(FOOD_TYPE, BREAKFAST);
                bundle.putString(FOOD_TITLE, BREAKFAST_TITLE);
                fragment.setArguments(bundle);
                break;
        }

        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);

        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction
                .setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                )
                .setReorderingAllowed(true)
                .replace(R.id.menu_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}