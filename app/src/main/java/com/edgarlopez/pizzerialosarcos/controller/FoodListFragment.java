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

import static com.edgarlopez.pizzerialosarcos.util.Util.FOOD_TITLE;
import static com.edgarlopez.pizzerialosarcos.util.Util.FOOD_TYPE;

public class FoodListFragment extends Fragment implements OnFoodClickListener {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView titleTextView;
    private String foodType;
    private String foodTitle;
    private ProgressBar progressBar;
    private List<Food> foodList;
    private RecyclerView recyclerView;
    private FoodRecyclerViewAdapter foodRecyclerAdapter;
    private FoodViewModel foodViewModel;

    private CollectionReference collectionReference = db.collection("Food");

    public FoodListFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_list, container, false);

        foodList.clear();

        assert getArguments() != null;
        foodType = getArguments().getString(FOOD_TYPE);
        foodTitle = getArguments().getString(FOOD_TITLE);

        titleTextView = view.findViewById(R.id.title_food);
        recyclerView = view.findViewById(R.id.foodRecyclerView);
        progressBar = requireActivity().findViewById(R.id.menu_activity_progress);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        foodViewModel = new ViewModelProvider(requireActivity())
                .get(FoodViewModel.class);

        progressBar.setVisibility(View.VISIBLE);
        titleTextView.setText(foodTitle);

        collectionReference
                .orderBy("listPosition")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    progressBar.setVisibility(View.GONE);
                    if (!queryDocumentSnapshots.isEmpty()) {
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
        Log.d("Food", "onFoodClicked: " + food.getTitle());
        foodViewModel.setSelectedFood(food);

        Fragment fragment = new OrderDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FOOD_TYPE, foodType);
        bundle.putString(FOOD_TITLE, foodTitle);
        fragment.setArguments(bundle);

        assert getActivity().getSupportFragmentManager() != null;
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
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