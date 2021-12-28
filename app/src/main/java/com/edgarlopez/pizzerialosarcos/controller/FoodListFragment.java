package com.edgarlopez.pizzerialosarcos.controller;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.adapter.OnFoodClickListener;
import com.edgarlopez.pizzerialosarcos.model.Food;
import com.edgarlopez.pizzerialosarcos.model.FoodViewModel;
import com.edgarlopez.pizzerialosarcos.ui.FoodRecyclerViewAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.edgarlopez.pizzerialosarcos.util.Util.BREAKFAST;
import static com.edgarlopez.pizzerialosarcos.util.Util.BURGER;
import static com.edgarlopez.pizzerialosarcos.util.Util.DRINKS;
import static com.edgarlopez.pizzerialosarcos.util.Util.ENCHILADAS_FOOD_TYPE;
import static com.edgarlopez.pizzerialosarcos.util.Util.ENCHILADAS_ID;
import static com.edgarlopez.pizzerialosarcos.util.Util.FISH_STEAK_FOOD_TYPE;
import static com.edgarlopez.pizzerialosarcos.util.Util.FISH_STEAK_ID;
import static com.edgarlopez.pizzerialosarcos.util.Util.FISH_STEAK_TITLE;
import static com.edgarlopez.pizzerialosarcos.util.Util.FOOD_TITLE;
import static com.edgarlopez.pizzerialosarcos.util.Util.FOOD_TYPE;
import static com.edgarlopez.pizzerialosarcos.util.Util.HOTDOG_FOOD_TYPE;
import static com.edgarlopez.pizzerialosarcos.util.Util.HOTDOG_ID;
import static com.edgarlopez.pizzerialosarcos.util.Util.PIZZA;
import static com.edgarlopez.pizzerialosarcos.util.Util.PLATILLO;
import static com.edgarlopez.pizzerialosarcos.util.Util.RANCH_SHRIMP_FOOD_TYPE;
import static com.edgarlopez.pizzerialosarcos.util.Util.RANCH_SHRIMP_ID;
import static com.edgarlopez.pizzerialosarcos.util.Util.SALAD;
import static com.edgarlopez.pizzerialosarcos.util.Util.SEA_FOOD;
import static com.edgarlopez.pizzerialosarcos.util.Util.SEA_FOOD_TITLE;
import static com.edgarlopez.pizzerialosarcos.util.Util.SHRIMP_FOOD_TYPE;
import static com.edgarlopez.pizzerialosarcos.util.Util.SHRIMP_ID;
import static com.edgarlopez.pizzerialosarcos.util.Util.SODA_FOOD_TYPE;
import static com.edgarlopez.pizzerialosarcos.util.Util.SODA_ID;
import static com.edgarlopez.pizzerialosarcos.util.Util.SPAGHETTI_FOOD_TYPE;
import static com.edgarlopez.pizzerialosarcos.util.Util.SPAGHETTI_ID;
import static com.edgarlopez.pizzerialosarcos.util.Util.TORTILLA_SOUP_FOOD_TYPE;
import static com.edgarlopez.pizzerialosarcos.util.Util.TORTILLA_SOUP_ID;

public class FoodListFragment extends Fragment implements OnFoodClickListener {
    private ImageButton backButton;
    private TextView titleTextView;
    private String foodType;
    private String foodTitle;
    private ProgressBar progressBar;
    private List<Food> foodList;
    private RecyclerView recyclerView;
    private FoodRecyclerViewAdapter foodRecyclerAdapter;
    private FoodViewModel foodViewModel;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Food");

    public FoodListFragment() {
    }

    public static FoodListFragment newInstance() {
        FoodListFragment fragment = new FoodListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        foodList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_list, container, false);

        backButton = view.findViewById(R.id.back_button_food_list);
        titleTextView = view.findViewById(R.id.title_food);
        recyclerView = view.findViewById(R.id.foodRecyclerView);
        progressBar = requireActivity().findViewById(R.id.menu_activity_progress);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assert getArguments() != null;
        foodType = getArguments().getString(FOOD_TYPE);
        foodTitle = getArguments().getString(FOOD_TITLE);

        foodViewModel = new ViewModelProvider(requireActivity())
                .get(FoodViewModel.class);

        titleTextView.setText(foodTitle);

        backButton.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
            requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
        });

        progressBar.setVisibility(View.VISIBLE);
        collectionReference
                .orderBy("listPosition")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    progressBar.setVisibility(View.GONE);
                    if (!queryDocumentSnapshots.isEmpty()) {
                        foodList.clear();
                        for (QueryDocumentSnapshot foods : queryDocumentSnapshots) {
                            Food food = foods.toObject(Food.class);

                            if (food.getId().contains(foodType)) {
                                foodList.add(food);
                            }
                        }

                        foodViewModel.setSelectedFoods(foodList);

                        if (foodViewModel.getFoods().getValue() != null) {
                            foodList = foodViewModel.getFoods().getValue();
                            foodRecyclerAdapter = new FoodRecyclerViewAdapter(getContext(), foodList, this);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                            recyclerView.setAdapter(foodRecyclerAdapter);
                        }

                    }else {
                        Toast.makeText(getContext(), "Lista vacía", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                });
    }

    @Override
    public void onFoodClicked(Food food) {
        foodViewModel.setSelectedFood(food);

        Bundle bundle = new Bundle();
        bundle.putString(FOOD_TYPE, foodType);
        bundle.putString(FOOD_TITLE, foodTitle);

        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                .setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                )
                .setReorderingAllowed(true);
        fragmentTransaction.addToBackStack(null);

        if (foodType.equals(PIZZA)) {
            Fragment orderFragment = new OrderDetailsFragment();
            orderFragment.setArguments(bundle);
            fragmentTransaction
                    .replace(R.id.menu_frame, orderFragment);
            fragmentTransaction.commit();
        } else if (foodType.equals(PLATILLO) && food.getId().contains(ENCHILADAS_ID)) {
            bundle.putString(FOOD_TYPE, ENCHILADAS_FOOD_TYPE);
            Fragment orderTertiary = new OrderDetailsTertiaryFragment();
            orderTertiary.setArguments(bundle);
            fragmentTransaction
                    .replace(R.id.menu_frame, orderTertiary);
            fragmentTransaction.commit();
        } else if (foodType.equals(SEA_FOOD) && (food.getId().contains(SHRIMP_ID) || food.getId().contains(FISH_STEAK_ID))) {
            if (food.getId().equals(SHRIMP_ID)) {
                bundle.putString(FOOD_TYPE, SHRIMP_FOOD_TYPE);
            } else if (food.getId().equals(FISH_STEAK_ID)) {
                bundle.putString(FOOD_TYPE, FISH_STEAK_FOOD_TYPE);
            }
            Fragment orderFour = new OrderDetailsFourFragment();
            orderFour.setArguments(bundle);
            fragmentTransaction
                    .replace(R.id.menu_frame, orderFour);
            fragmentTransaction.commit();
        } else if (foodType.equals(BURGER) || foodType.equals(SALAD) || foodType.equals(PLATILLO) || foodType.equals(SEA_FOOD)) {
            if (food.getId().equals(HOTDOG_ID)) {
                bundle.putString(FOOD_TYPE, HOTDOG_FOOD_TYPE);
            } else if (food.getId().equals(SPAGHETTI_ID)) {
                bundle.putString(FOOD_TYPE, SPAGHETTI_FOOD_TYPE);
            } else if (food.getId().equals(TORTILLA_SOUP_ID)) {
                bundle.putString(FOOD_TYPE, TORTILLA_SOUP_FOOD_TYPE);
            } else if (food.getId().contains(FISH_STEAK_TITLE)) {
                bundle.putString(FOOD_TYPE, FISH_STEAK_FOOD_TYPE);
            } else if (food.getId().equals(RANCH_SHRIMP_ID)) {
                bundle.putString(FOOD_TYPE, RANCH_SHRIMP_FOOD_TYPE);
            }
            Fragment orderSecondary = new OrderDetailsSecondaryFragment();
            orderSecondary.setArguments(bundle);
            fragmentTransaction
                    .replace(R.id.menu_frame, orderSecondary);
            fragmentTransaction.commit();
        } else if (foodType.equals(BREAKFAST)) {
            Fragment orderFive = new OrderDetailsFiveFragment();
            orderFive.setArguments(bundle);
            fragmentTransaction
                    .replace(R.id.menu_frame, orderFive);
            fragmentTransaction.commit();
        } else if (foodType.equals(DRINKS)) {
            Fragment orderDrinks = new OrderDetailsDrinksFragment();
            if (food.getId().equals(SODA_ID)) {
                bundle.putString(FOOD_TYPE, SODA_FOOD_TYPE);
            }
            orderDrinks.setArguments(bundle);
            fragmentTransaction
                    .replace(R.id.menu_frame, orderDrinks);
            fragmentTransaction.commit();
        }

    }
}