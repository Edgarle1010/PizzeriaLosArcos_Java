package com.edgarlopez.pizzerialosarcos.controller;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.adapter.OnAddHalfFoodClickListener;
import com.edgarlopez.pizzerialosarcos.adapter.OnCancelExtraIngredientClickListener;
import com.edgarlopez.pizzerialosarcos.adapter.OnCancelHalfFoodClickListener;
import com.edgarlopez.pizzerialosarcos.adapter.OnFoodClickListener;
import com.edgarlopez.pizzerialosarcos.model.Food;
import com.edgarlopez.pizzerialosarcos.model.FoodViewModel;
import com.edgarlopez.pizzerialosarcos.ui.FoodRecyclerViewAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.edgarlopez.pizzerialosarcos.util.Util.EGGS_INGREDIENTS_ID;
import static com.edgarlopez.pizzerialosarcos.util.Util.FOOD_ITEM;
import static com.edgarlopez.pizzerialosarcos.util.Util.FOOD_TYPE;
import static com.edgarlopez.pizzerialosarcos.util.Util.ICECREAM_ID;

public class HalfFoodFragment extends Fragment implements OnFoodClickListener {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String foodType;
    private Food principalFood;
    private ProgressBar progressBar;
    private TextView halfTitleTextView;
    private ImageView cancelImageView;
    private List<Food> foodList;
    private RecyclerView recyclerView;
    private FoodRecyclerViewAdapter foodRecyclerAdapter;
    private FoodViewModel foodViewModel;

    private CollectionReference collectionReference = db.collection("Food");

    private OnAddHalfFoodClickListener addCallback;
    private OnCancelHalfFoodClickListener cancelCallback;

    public HalfFoodFragment() {

    }

    public static HalfFoodFragment newInstance() {
        HalfFoodFragment fragment = new HalfFoodFragment();
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

        View view = inflater.inflate(R.layout.fragment_half_food, container, false);

        assert getArguments() != null;
        foodType = getArguments().getString(FOOD_TYPE);
        principalFood = (Food) getArguments().getSerializable(FOOD_ITEM);

        cancelImageView = view.findViewById(R.id.cancel_half_food_image_view);
        recyclerView = view.findViewById(R.id.halfFoodRecyclerView);
        progressBar = requireActivity().findViewById(R.id.menu_activity_progress);
        halfTitleTextView = view.findViewById(R.id.half_fragment_title);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnCancelHalfFoodClickListener
            && context instanceof OnAddHalfFoodClickListener) {
            addCallback = (OnAddHalfFoodClickListener) context;
            cancelCallback = (OnCancelHalfFoodClickListener) context;
        }else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCancelHalfFoodClickListener");
        }
    }

    @Override
    public void onDetach() {
        cancelCallback = null;
        addCallback = null;
        super.onDetach();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        foodViewModel = new ViewModelProvider(requireActivity())
                .get(FoodViewModel.class);

        if (foodType.equals(EGGS_INGREDIENTS_ID)) {
            halfTitleTextView.setText("Seleccione el ingrediente");
        } else if (foodType.equals((ICECREAM_ID))) {
            halfTitleTextView.setText("Seleccione el sabor");
        }

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

                            if (food.getId().contains(foodType) && !food.getTitle().contains(principalFood.getTitle())) {
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

        cancelImageView.setOnClickListener(v -> {
            cancelButtonPressed();
        });
    }

    public void cancelButtonPressed() {
        getView().animate()
                .translationY(getView().getHeight())
                .alpha(0.0f)
                .setDuration(500)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        getFragmentManager().popBackStack();
                        cancelCallback.onCancelHalfFoodClicked();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

    @Override
    public void onFoodClicked(Food food) {
        Log.d("Food", "onFoodClicked: " + food.getTitle());
        foodViewModel.setSelectedFood(food);

        getView().animate()
                .translationY(getView().getHeight())
                .alpha(0.0f)
                .setDuration(500)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        getFragmentManager().popBackStack();
                        addCallback.onAddHalfFoodClicked();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }
}