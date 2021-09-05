package com.edgarlopez.pizzerialosarcos.controller;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.model.ExtraIngredient;
import com.edgarlopez.pizzerialosarcos.model.ExtraIngredientViewModel;
import com.edgarlopez.pizzerialosarcos.model.FoodViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import static com.edgarlopez.pizzerialosarcos.util.Util.FOOD_TYPE;

public class OrderDetailsFragment extends Fragment implements View.OnClickListener {
    private FoodViewModel foodViewModel;
    private ExtraIngredientViewModel extraIngredientViewModel;
    private TextView titleFoodTextView,
            completeTextView,
            halfTextView,
            bigSizeTextView,
            mediumSizeTextView,
            smallSizeTextView,
            extraIngredientTextView;
    private String titleFood,
            foodType;
    private List<ExtraIngredient> extraIngredientList;
    private RelativeLayout relativeLayout;
    private BottomNavigationView navigationView;

    public OrderDetailsFragment() {
        // Required empty public constructor
    }

    public static OrderDetailsFragment newInstance() {
        OrderDetailsFragment fragment = new OrderDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        extraIngredientList = new ArrayList<>();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        completeTextView.setOnClickListener(this);
        halfTextView.setOnClickListener(this);
        bigSizeTextView.setOnClickListener(this);
        mediumSizeTextView.setOnClickListener(this);
        smallSizeTextView.setOnClickListener(this);
        relativeLayout.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);

        extraIngredientList.clear();

        foodViewModel = new ViewModelProvider(requireActivity())
                .get(FoodViewModel.class);

        extraIngredientViewModel = new ViewModelProvider(requireActivity())
                .get(ExtraIngredientViewModel.class);

        assert getArguments() != null;
        foodType = getArguments().getString(FOOD_TYPE);

        titleFoodTextView = view.findViewById(R.id.food_title_order);
        completeTextView = view.findViewById(R.id.complete_text_view);
        halfTextView = view.findViewById(R.id.half_text_view);
        bigSizeTextView = view.findViewById(R.id.size_big_text_view);
        mediumSizeTextView = view.findViewById(R.id.size_medium_text_view);
        smallSizeTextView = view.findViewById(R.id.size_small_text_view);
        extraIngredientTextView = view.findViewById(R.id.first_extra_ingredient);

        navigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        relativeLayout = view.findViewById(R.id.extra_ingredient_relative_layout);

        foodViewModel.getSelectedFood().observe(getViewLifecycleOwner(), food -> {
            titleFood = food.getTitle();
            titleFoodTextView.setText(titleFood);
        });

        return view;
    }

    public void disableView() {
        navigationView.setVisibility(View.GONE);
        completeTextView.setClickable(false);
        halfTextView.setClickable(false);
        bigSizeTextView.setClickable(false);
        mediumSizeTextView.setClickable(false);
        smallSizeTextView.setClickable(false);
        relativeLayout.setClickable(false);
        getView().setAlpha((float) .3);
    }

    public void enableView() {
        navigationView.setVisibility(View.VISIBLE);
        completeTextView.setClickable(true);
        halfTextView.setClickable(true);
        bigSizeTextView.setClickable(true);
        mediumSizeTextView.setClickable(true);
        smallSizeTextView.setClickable(true);
        relativeLayout.setClickable(true);
        getView().setAlpha((float) 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.complete_text_view:
                DrawableCompat.setTint(completeTextView.getBackground(), ContextCompat.getColor(getActivity(), R.color.third_color));
                DrawableCompat.setTint(halfTextView.getBackground(), ContextCompat.getColor(getActivity(), R.color.quarter_color));
                break;
            case R.id.half_text_view:
                DrawableCompat.setTint(halfTextView.getBackground(), ContextCompat.getColor(getActivity(), R.color.third_color));
                DrawableCompat.setTint(completeTextView.getBackground(), ContextCompat.getColor(getActivity(), R.color.quarter_color));
                break;
            case R.id.size_big_text_view:
                DrawableCompat.setTint(bigSizeTextView.getBackground(), ContextCompat.getColor(getActivity(), R.color.third_color));
                DrawableCompat.setTint(mediumSizeTextView.getBackground(), ContextCompat.getColor(getActivity(), R.color.quarter_color));
                DrawableCompat.setTint(smallSizeTextView.getBackground(), ContextCompat.getColor(getActivity(), R.color.quarter_color));
                break;
            case R.id.size_medium_text_view:
                DrawableCompat.setTint(mediumSizeTextView.getBackground(), ContextCompat.getColor(getActivity(), R.color.third_color));
                DrawableCompat.setTint(bigSizeTextView.getBackground(), ContextCompat.getColor(getActivity(), R.color.quarter_color));
                DrawableCompat.setTint(smallSizeTextView.getBackground(), ContextCompat.getColor(getActivity(), R.color.quarter_color));
                break;
            case R.id.size_small_text_view:
                DrawableCompat.setTint(smallSizeTextView.getBackground(), ContextCompat.getColor(getActivity(), R.color.third_color));
                DrawableCompat.setTint(bigSizeTextView.getBackground(), ContextCompat.getColor(getActivity(), R.color.quarter_color));
                DrawableCompat.setTint(mediumSizeTextView.getBackground(), ContextCompat.getColor(getActivity(), R.color.quarter_color));
                break;
            case R.id.extra_ingredient_relative_layout:
                disableView();
                Fragment fragment = new ExtraIngredientFragment();
                Bundle bundle = new Bundle();
                bundle.putString(FOOD_TYPE, foodType);
                fragment.setArguments(bundle);

                assert getFragmentManager() != null;
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction
                        .setReorderingAllowed(true)
                        .setCustomAnimations(
                                R.anim.slide_in_up,
                                R.anim.fade_out
                        )
                        .replace(R.id.extra_ingredient_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

        }
    }

    public void addButtonClicked() {
        extraIngredientList.add(extraIngredientViewModel.getSelectedExtraIngredient().getValue());

        ArrayList<String> list = new ArrayList<String>();
        for (ExtraIngredient extraIngredient : extraIngredientList) {
            list.add(extraIngredient.getTitle());
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (String s : list) {
            stringBuilder.append(s);
            stringBuilder.append("\n");
        }

        extraIngredientTextView.setVisibility(View.VISIBLE);
        extraIngredientTextView.setText(stringBuilder.toString());

        //extraIngredientViewModel.setSelectedExtraIngredients(extraIngredientList);
        enableView();
    }

    public void cancelButtonClicked() {
        enableView();
    }

}