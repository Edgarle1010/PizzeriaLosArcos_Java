package com.edgarlopez.pizzerialosarcos.controller;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.adapter.OnExtraIngredientClickListener;
import com.edgarlopez.pizzerialosarcos.adapter.OnFoodClickListener;
import com.edgarlopez.pizzerialosarcos.model.ExtraIngredient;
import com.edgarlopez.pizzerialosarcos.model.ExtraIngredientViewModel;
import com.edgarlopez.pizzerialosarcos.model.Food;
import com.edgarlopez.pizzerialosarcos.model.FoodViewModel;
import com.edgarlopez.pizzerialosarcos.ui.ExtraIngredientRecyclerViewAdapter;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.edgarlopez.pizzerialosarcos.util.Util.FOOD_ITEM;
import static com.edgarlopez.pizzerialosarcos.util.Util.FOOD_TYPE;

public class OrderDetailsFragment extends Fragment implements View.OnClickListener, OnExtraIngredientClickListener {
    private FoodViewModel foodViewModel;
    private Food principalFood;
    private ExtraIngredientViewModel extraIngredientViewModel;
    private TextView titleFoodTextView,
            descriptionTextView,
            completeTextView,
            halfTextView,
            bigSizeTextView,
            mediumSizeTextView,
            smallSizeTextView;
    private String foodType;
    private RelativeLayout relativeLayout;
    private BottomNavigationView navigationView;

    private RecyclerView recyclerView;
    private List<ExtraIngredient> extraIngredientList;
    private ExtraIngredientRecyclerViewAdapter adapter;

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

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.CENTER);
        layoutManager.setAlignItems(AlignItems.FLEX_START);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ExtraIngredientRecyclerViewAdapter(getContext(), extraIngredientList, this);
        recyclerView.setAdapter(adapter);

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

        MenuActivity ma = (MenuActivity) getActivity();
        assert ma != null;
        ma.currExtraIngredients = extraIngredientList;

        foodViewModel = new ViewModelProvider(requireActivity())
                .get(FoodViewModel.class);

        extraIngredientViewModel = new ViewModelProvider(requireActivity())
                .get(ExtraIngredientViewModel.class);

        assert getArguments() != null;
        foodType = getArguments().getString(FOOD_TYPE);

        recyclerView = view.findViewById(R.id.extraIngredientRecyclerView);

        titleFoodTextView = view.findViewById(R.id.food_title_order);
        descriptionTextView = view.findViewById(R.id.food_description_order);
        completeTextView = view.findViewById(R.id.complete_text_view);
        halfTextView = view.findViewById(R.id.half_text_view);
        bigSizeTextView = view.findViewById(R.id.size_big_text_view);
        mediumSizeTextView = view.findViewById(R.id.size_medium_text_view);
        smallSizeTextView = view.findViewById(R.id.size_small_text_view);

        navigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        relativeLayout = view.findViewById(R.id.extra_ingredient_relative_layout);

        titleFoodTextView.setText(foodViewModel.getSelectedFood().getValue().getTitle());
        descriptionTextView.setText(foodViewModel.getSelectedFood().getValue().getDescription());

        principalFood = foodViewModel.getSelectedFood().getValue();

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
                titleFoodTextView.setText(principalFood.getTitle());
                descriptionTextView.setText(principalFood.getDescription());
                break;
            case R.id.half_text_view:
                DrawableCompat.setTint(halfTextView.getBackground(), ContextCompat.getColor(getActivity(), R.color.third_color));
                DrawableCompat.setTint(completeTextView.getBackground(), ContextCompat.getColor(getActivity(), R.color.quarter_color));
                disableView();

                Fragment halfFoodFragment = new HalfFoodFragment();
                Bundle halfFoodBundle = new Bundle();
                halfFoodBundle.putString(FOOD_TYPE, foodType);
                halfFoodBundle.putSerializable(FOOD_ITEM, principalFood);
                halfFoodFragment.setArguments(halfFoodBundle);

                assert getFragmentManager() != null;
                FragmentTransaction HalfFoodFragmentTransaction = getFragmentManager().beginTransaction();
                HalfFoodFragmentTransaction
                        .setReorderingAllowed(true)
                        .setCustomAnimations(
                                R.anim.slide_in_up,
                                R.anim.fade_out
                        )
                        .replace(R.id.extras_frame, halfFoodFragment);
                HalfFoodFragmentTransaction.addToBackStack(null);
                HalfFoodFragmentTransaction.commit();

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
                        .replace(R.id.extras_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

        }
    }

    public void addExtraIngredientClicked() {
        extraIngredientList.add(extraIngredientViewModel.getSelectedExtraIngredient().getValue());

        MenuActivity ma = (MenuActivity) getActivity();
        assert ma != null;
        ma.currExtraIngredients = extraIngredientList;

        adapter.notifyDataSetChanged();

        if (extraIngredientList.size() > 2) {
            relativeLayout.setVisibility(View.GONE);
        }else {
            relativeLayout.setVisibility(View.VISIBLE);
        }

        enableView();
    }

    public void cancelExtraIngredientClicked() {
        enableView();
    }

    public void addHalfFoodClicked() {
        enableView();

        Food halfFood = foodViewModel.getSelectedFood().getValue();
        titleFoodTextView.setText(principalFood.getTitle() + " / " + halfFood.getTitle());

        String pfDescription = principalFood.getDescription();
        if (pfDescription == null) {
            pfDescription = principalFood.getTitle();
        }

        String hfDescription = halfFood.getDescription();
        if (hfDescription == null) {
            hfDescription = halfFood.getTitle();
        }

        descriptionTextView.setText(pfDescription + " / " + hfDescription);
    }

    public void cancelHalfFoodClicked() {
        enableView();
        completeTextView.callOnClick();
    }

    @Override
    public void onExtraIngredientClicked(ExtraIngredient extraIngredient) {

        if (extraIngredientList.size() <= 2) {
            relativeLayout.setVisibility(View.VISIBLE);
        }else {
            relativeLayout.setVisibility(View.GONE);
        }
    }

}