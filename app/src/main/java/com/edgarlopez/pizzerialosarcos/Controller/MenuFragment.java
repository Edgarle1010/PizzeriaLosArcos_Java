package com.edgarlopez.pizzerialosarcos.Controller;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.edgarlopez.pizzerialosarcos.R;
import com.squareup.picasso.Picasso;

public class MenuFragment extends Fragment {
    private ImageView pizzasMenu;
    private ImageView burgersMenu;
    private ImageView saladsMenu;
    private ImageView foodDishesMenu;
    private ImageView seaFoodMenu;
    private ImageView breakfastsMenu;
    private ImageView drinksMenu;
    private ImageView dessertsMenu;
    private ImageView snowMilkshakesMenu;
    private ImageView kidsMenu;

    public MenuFragment() {
        // Required empty public constructor
    }

    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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

}