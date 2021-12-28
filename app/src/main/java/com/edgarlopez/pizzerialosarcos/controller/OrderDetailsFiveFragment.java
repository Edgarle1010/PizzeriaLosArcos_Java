package com.edgarlopez.pizzerialosarcos.controller;

import static com.edgarlopez.pizzerialosarcos.util.Util.EGGS_INGREDIENTS_ID;
import static com.edgarlopez.pizzerialosarcos.util.Util.FOOD_ITEM;
import static com.edgarlopez.pizzerialosarcos.util.Util.FOOD_SIZE;
import static com.edgarlopez.pizzerialosarcos.util.Util.FOOD_TITLE;
import static com.edgarlopez.pizzerialosarcos.util.Util.FOOD_TYPE;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Vibrator;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.adapter.OnExtraIngredientClickListener;
import com.edgarlopez.pizzerialosarcos.model.ExtraIngredient;
import com.edgarlopez.pizzerialosarcos.model.ExtraIngredientViewModel;
import com.edgarlopez.pizzerialosarcos.model.Food;
import com.edgarlopez.pizzerialosarcos.model.FoodViewModel;
import com.edgarlopez.pizzerialosarcos.model.Item;
import com.edgarlopez.pizzerialosarcos.model.ItemViewModel;
import com.edgarlopez.pizzerialosarcos.ui.ExtraIngredientRecyclerViewAdapter;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsFiveFragment extends Fragment implements View.OnClickListener,
        OnExtraIngredientClickListener,
        NumberPicker.OnValueChangeListener {
    private FoodViewModel foodViewModel;
    private Food principalFood,
            ingredientFood;
    private ExtraIngredientViewModel extraIngredientViewModel;
    private TextView titleFoodTextView,
            descriptionTextView,
            completeOrderTextView,
            halfOrderTextView,
            styleTextView,
            smashedStyleTextView,
            scrambledStyleTextView,
            chilaquilesTextView,
            redChilaquilesTextView,
            greenChilaquilesTextView,
            fillingTextView,
            cheeseFillingTextView,
            hashFillingTextView,
            chickenFillingTextView,
            ingredientTextView,
            baconIngredientTextView,
            hamIngredientTextView,
            chorizoIngredientTextView,
            whiteBreadTextView,
            wholemealBreadTextView,
            baguetteBreadTextView,
            amountTextView,
            totalTextView;
    private ImageButton backButton;
    private EditText commentsEditText;
    private Button addOrderButton;
    private String foodType,
            foodTitle;
    private RelativeLayout extraIngredientLayout;
    private LinearLayout chilaquilesLayout,
            fillingLayout,
            ingredientLayout;

    private ItemViewModel itemViewModel;

    private String itemTitle;
    private boolean isCompleteItem;
    private String foodSize;
    private String foodStyle;
    private String foodBread;
    private String chilaquiles;
    private String filling;
    private String ingredient;
    private List<ExtraIngredient> extraIngredientList;
    private int itemAmount;
    private double price;

    private RecyclerView recyclerView;
    private ExtraIngredientRecyclerViewAdapter adapter;

    public OrderDetailsFiveFragment() {

    }

    public static OrderDetailsFiveFragment newInstance() {
        OrderDetailsFiveFragment fragment = new OrderDetailsFiveFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        extraIngredientList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_details_five, container, false);

        extraIngredientList.clear();

        MenuActivity ma = (MenuActivity) getActivity();
        assert ma != null;
        ma.currExtraIngredients = extraIngredientList;

        foodViewModel = new ViewModelProvider(requireActivity())
                .get(FoodViewModel.class);

        extraIngredientViewModel = new ViewModelProvider(requireActivity())
                .get(ExtraIngredientViewModel.class);

        itemViewModel = new ViewModelProvider.AndroidViewModelFactory(requireActivity()
                .getApplication())
                .create(ItemViewModel.class);

        assert getArguments() != null;
        foodType = getArguments().getString(FOOD_TYPE);
        foodTitle = getArguments().getString(FOOD_TITLE);

        recyclerView = view.findViewById(R.id.extraIngredientRecyclerView_five);

        backButton = view.findViewById(R.id.back_button_order_detail_five);
        titleFoodTextView = view.findViewById(R.id.food_title_order_five);
        descriptionTextView = view.findViewById(R.id.food_description_order_five);
        completeOrderTextView = view.findViewById(R.id.complete_text_view_five);
        halfOrderTextView = view.findViewById(R.id.half_text_view_five);
        styleTextView = view.findViewById(R.id.food_style_order_five);
        smashedStyleTextView = view.findViewById(R.id.style_smashed_text_view);
        scrambledStyleTextView = view.findViewById(R.id.style_scrambled_text_view);
        chilaquilesTextView = view.findViewById(R.id.food_chilaquiles_order_five);
        redChilaquilesTextView = view.findViewById(R.id.chilaquiles_red_text_view);
        greenChilaquilesTextView = view.findViewById(R.id.chilaquiles_green_text_view);
        fillingTextView = view.findViewById(R.id.food_filling_order_five);
        cheeseFillingTextView = view.findViewById(R.id.filling_cheese_text_view_five);
        hashFillingTextView = view.findViewById(R.id.filling_hash_text_view_five);
        chickenFillingTextView = view.findViewById(R.id.filling_chicken_text_view_five);
        ingredientTextView = view.findViewById(R.id.food_ingredient_order_five);
        baconIngredientTextView = view.findViewById(R.id.ingredient_bacon_text_view_five);
        hamIngredientTextView = view.findViewById(R.id.ingredient_ham_text_view_five);
        chorizoIngredientTextView = view.findViewById(R.id.ingredient_chorizo_text_view_five);
        whiteBreadTextView = view.findViewById(R.id.bread_white_text_view);
        wholemealBreadTextView = view.findViewById(R.id.bread_wholemeal_text_view);
        baguetteBreadTextView = view.findViewById(R.id.bread_baguette_text_view);
        commentsEditText = view.findViewById(R.id.order_comments_edit_text_five);
        amountTextView = view.findViewById(R.id.order_amount_text_view_five);
        addOrderButton = view.findViewById(R.id.order_add_button_five);
        extraIngredientLayout = view.findViewById(R.id.extra_ingredient_relative_layout_five);
        chilaquilesLayout = view.findViewById(R.id.chilaquiles_layout);
        fillingLayout = view.findViewById(R.id.filling_layout_five);
        ingredientLayout = view.findViewById(R.id.ingredient_layout_five);
        totalTextView = view.findViewById(R.id.order_total_text_view_five);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        DrawableCompat.setTint(completeOrderTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
        DrawableCompat.setTint(smashedStyleTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
        DrawableCompat.setTint(redChilaquilesTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
        DrawableCompat.setTint(cheeseFillingTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
        DrawableCompat.setTint(baconIngredientTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
        DrawableCompat.setTint(whiteBreadTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
        DrawableCompat.setTint(amountTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
        DrawableCompat.setTint(totalTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));

        backButton.setOnClickListener(this);
        completeOrderTextView.setOnClickListener(this);
        halfOrderTextView.setOnClickListener(this);
        smashedStyleTextView.setOnClickListener(this);
        scrambledStyleTextView.setOnClickListener(this);
        redChilaquilesTextView.setOnClickListener(this);
        greenChilaquilesTextView.setOnClickListener(this);
        cheeseFillingTextView.setOnClickListener(this);
        hashFillingTextView.setOnClickListener(this);
        chickenFillingTextView.setOnClickListener(this);
        baconIngredientTextView.setOnClickListener(this);
        hamIngredientTextView.setOnClickListener(this);
        chorizoIngredientTextView.setOnClickListener(this);
        whiteBreadTextView.setOnClickListener(this);
        wholemealBreadTextView.setOnClickListener(this);
        baguetteBreadTextView.setOnClickListener(this);
        extraIngredientLayout.setOnClickListener(this);
        amountTextView.setOnClickListener(this);
        addOrderButton.setOnClickListener(this);

        itemTitle = titleFoodTextView.getText().toString().trim();
        isCompleteItem = true;
        foodSize = "complete";
        foodStyle = "smashed";
        foodBread = "Pan blanco";
        itemAmount = 1;
        price = 0;

        principalFood = foodViewModel.getSelectedFood().getValue();
        if (principalFood != null) {
            titleFoodTextView.setText(principalFood.getTitle());
            descriptionTextView.setText(principalFood.getDescription());

            if (principalFood.getTitle().contains("divorciados") ||
                    principalFood.getTitle().contains("rancheros") ||
                    principalFood.getTitle().contains("mexicana") ||
                    principalFood.getTitle().contains("Montadas") ||
                    principalFood.getTitle().contains("Machaca") ||
                    principalFood.getTitle().contains("Omelette") ||
                    principalFood.getTitle().contains("cakes") ||
                    principalFood.getTitle().contains("Sandwich") ||
                    principalFood.getTitle().contains("Avena")) {
                styleTextView.setVisibility(View.GONE);
                smashedStyleTextView.setVisibility(View.GONE);
                scrambledStyleTextView.setVisibility(View.GONE);
                foodStyle = "default";
            }

            if (principalFood.getTitle().contains("chilaquiles") ||
                principalFood.getTitle().contains("Montadas")) {
                chilaquilesTextView.setVisibility(View.VISIBLE);
                chilaquilesLayout.setVisibility(View.VISIBLE);
                chilaquiles = "rojos";

                if (principalFood.getTitle().contains("Montadas")) {
                    chilaquilesTextView.setText("Enchiladas");
                    redChilaquilesTextView.setText("Rojas");
                    chilaquiles = "rojas";
                    fillingTextView.setVisibility(View.VISIBLE);
                    fillingLayout.setVisibility(View.VISIBLE);
                    filling = "queso";
                }
            }

            if (principalFood.getTitle().equals("Huevos")) {
                ingredientTextView.setVisibility(View.VISIBLE);
                ingredientLayout.setVisibility(View.VISIBLE);
                ingredient = "tocino";
            }
        }

        getTotal();

        commentsEditText.setOnKeyListener((v, keyCode, event) -> {
            // if enter is pressed start calculating
            if (keyCode == KeyEvent.KEYCODE_ENTER
                    && event.getAction() == KeyEvent.ACTION_UP) {

                // get EditText text
                String text = ((EditText) v).getText().toString();

                // find how many rows it contains
                int editTextRowCount = text.split("\\n").length;

                // user has input more than limited - lets do something
                // about that
                if (editTextRowCount >= 5) {

                    // find the last break
                    int lastBreakIndex = text.lastIndexOf("\n");

                    // compose new text
                    String newText = text.substring(0, lastBreakIndex);

                    // add new text - delete old one and append new one
                    // (append because I want the cursor to be at the end)
                    ((EditText) v).setText("");
                    ((EditText) v).append(newText);

                }
            }

            return false;
        });

        commentsEditText.setOnEditorActionListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // hide virtual keyboard
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(commentsEditText.getWindowToken(), 0);
                return true;
            }
            return false;
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_order_detail_five:
                getParentFragmentManager().popBackStack();
                break;
            case R.id.complete_text_view_five:
                DrawableCompat.setTint(completeOrderTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
                DrawableCompat.setTint(halfOrderTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));

                isCompleteItem = true;
                foodSize = "complete";
                getTotal();
                break;
            case R.id.half_text_view_five:
                DrawableCompat.setTint(halfOrderTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
                DrawableCompat.setTint(completeOrderTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));

                isCompleteItem = false;
                foodSize = "half";
                getTotal();
                break;
            case R.id.style_smashed_text_view:
                DrawableCompat.setTint(smashedStyleTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
                DrawableCompat.setTint(scrambledStyleTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));

                if (principalFood.getTitle().equals("Huevos")) {
                    ingredientTextView.setVisibility(View.VISIBLE);
                    ingredientLayout.setVisibility(View.VISIBLE);
                    baconIngredientTextView.callOnClick();
                }

                foodStyle = "smashed";
                getTotal();
                break;
            case R.id.style_scrambled_text_view:
                DrawableCompat.setTint(scrambledStyleTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
                DrawableCompat.setTint(smashedStyleTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));

                disableView();
                Fragment halfFoodFragment = new HalfFoodFragment();
                Bundle halfFoodBundle = new Bundle();
                halfFoodBundle.putString(FOOD_TYPE, EGGS_INGREDIENTS_ID);
                halfFoodFragment.setArguments(halfFoodBundle);
                halfFoodBundle.putSerializable(FOOD_ITEM, principalFood);

                FragmentTransaction HalfFoodFragmentTransaction = getParentFragmentManager().beginTransaction();
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
            case R.id.chilaquiles_red_text_view:
                DrawableCompat.setTint(redChilaquilesTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
                DrawableCompat.setTint(greenChilaquilesTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));

                if (principalFood.getTitle().contains("Montadas")) {
                    chilaquiles = "rojas";
                } else {
                    chilaquiles = "rojos";
                }
                getTotal();
                break;
            case R.id.chilaquiles_green_text_view:
                DrawableCompat.setTint(greenChilaquilesTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
                DrawableCompat.setTint(redChilaquilesTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));

                chilaquiles = "verdes";
                getTotal();
                break;
            case R.id.filling_cheese_text_view_five:
                DrawableCompat.setTint(cheeseFillingTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
                DrawableCompat.setTint(hashFillingTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));
                DrawableCompat.setTint(chickenFillingTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));

                foodBread = "queso";
                getTotal();
                break;
            case R.id.filling_hash_text_view_five:
                DrawableCompat.setTint(hashFillingTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
                DrawableCompat.setTint(cheeseFillingTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));
                DrawableCompat.setTint(chickenFillingTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));

                foodBread = "picadillo";
                getTotal();
                break;
            case R.id.filling_chicken_text_view_five:
                DrawableCompat.setTint(chickenFillingTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
                DrawableCompat.setTint(cheeseFillingTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));
                DrawableCompat.setTint(hashFillingTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));

                foodBread = "pollo";
                getTotal();
                break;
            case R.id.ingredient_bacon_text_view_five:
                DrawableCompat.setTint(baconIngredientTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
                DrawableCompat.setTint(hamIngredientTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));
                DrawableCompat.setTint(chorizoIngredientTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));

                foodBread = "tocino";
                getTotal();
                break;
            case R.id.ingredient_ham_text_view_five:
                DrawableCompat.setTint(hamIngredientTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
                DrawableCompat.setTint(baconIngredientTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));
                DrawableCompat.setTint(chorizoIngredientTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));

                foodBread = "jamón";
                getTotal();
                break;
            case R.id.ingredient_chorizo_text_view_five:
                DrawableCompat.setTint(chorizoIngredientTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
                DrawableCompat.setTint(baconIngredientTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));
                DrawableCompat.setTint(hamIngredientTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));

                foodBread = "chorizo";
                getTotal();
                break;
            case R.id.bread_white_text_view:
                DrawableCompat.setTint(whiteBreadTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
                DrawableCompat.setTint(wholemealBreadTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));
                DrawableCompat.setTint(baguetteBreadTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));

                foodBread = "Pan blanco";
                getTotal();
                break;
            case R.id.bread_wholemeal_text_view:
                DrawableCompat.setTint(wholemealBreadTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
                DrawableCompat.setTint(whiteBreadTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));
                DrawableCompat.setTint(baguetteBreadTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));

                foodBread = "Pan integral";
                getTotal();
                break;
            case R.id.bread_baguette_text_view:
                DrawableCompat.setTint(baguetteBreadTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
                DrawableCompat.setTint(wholemealBreadTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));
                DrawableCompat.setTint(whiteBreadTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));

                foodBread = "Pan baguette";
                getTotal();
                break;
            case R.id.extra_ingredient_relative_layout_five:
                disableView();

                Fragment fragment = new ExtraIngredientFragment();
                Bundle bundle = new Bundle();
                bundle.putString(FOOD_TYPE, foodType);
                bundle.putString(FOOD_SIZE, "big");
                fragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
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
            case R.id.order_amount_text_view_five:
                showNumberPicker(getView());
                break;
            case R.id.order_add_button_five:
                addItem();
                break;
        }
    }

    @Override
    public void onExtraIngredientClicked(ExtraIngredient extraIngredient) {
        if (extraIngredientList.size() <= 2) {
            extraIngredientLayout.setVisibility(View.VISIBLE);
        }else {
            extraIngredientLayout.setVisibility(View.GONE);
        }

        getTotal();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addExtraIngredientClicked() {
        extraIngredientList.add(extraIngredientViewModel.getSelectedExtraIngredient().getValue());

        MenuActivity ma = (MenuActivity) getActivity();
        assert ma != null;
        ma.currExtraIngredients = extraIngredientList;

        adapter.notifyDataSetChanged();

        if (extraIngredientList.size() > 2) {
            extraIngredientLayout.setVisibility(View.GONE);
        }else {
            extraIngredientLayout.setVisibility(View.VISIBLE);
        }

        enableView();
        getTotal();
    }

    public void cancelExtraIngredientClicked() {
        enableView();
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        itemAmount = picker.getValue();
        amountTextView.setText(String.valueOf(itemAmount));
        getTotal();
    }

    public void getTotal() {
        String sizeText;
        String styleText;
        String breadText = " | " + foodBread;
        float extraIngredientPrice;

        if (isCompleteItem) {
            price = principalFood.getbPrice();
            sizeText = "";
        } else {
            price = (principalFood.getbPrice() * .7);
            sizeText = " | Media orden";
        }

        if (foodStyle.equals("scrambled")) {
            price = price + ingredientFood.getbPrice();
            styleText = String.format("| Revueltos %s", ingredientFood.getTitle());
        } else if (foodStyle.equals("smashed")) {
            if (ingredient != null) {
                styleText = String.format("| Estrellados con %s", ingredient);
            } else {
                styleText = "| Estrellados";
            }
        } else{
            styleText = "";
        }

        if (chilaquiles != null) {
            if (filling != null) {
                itemTitle = String.format("%s %s con %s%s%s%s", titleFoodTextView.getText().toString().trim(), chilaquiles, filling, styleText, breadText, sizeText);
            } else {
                itemTitle = String.format("%s %s %s%s%s", titleFoodTextView.getText().toString().trim(), chilaquiles, styleText, breadText, sizeText);
            }
        } else {
            titleFoodTextView.setText(String.format("%s %s", principalFood.getTitle(), styleText));
            itemTitle = titleFoodTextView.getText().toString().trim() + breadText + sizeText;
        }

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.CENTER);
        layoutManager.setAlignItems(AlignItems.FLEX_START);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ExtraIngredientRecyclerViewAdapter(getContext(), extraIngredientList, foodSize, this);
        recyclerView.setAdapter(adapter);

        if (extraIngredientList != null) {
            float priceSummary = 0;
            for (ExtraIngredient extraIngredients : extraIngredientList) {
                extraIngredientPrice = extraIngredients.getbPrice();
                priceSummary = priceSummary + extraIngredientPrice;
            }

            extraIngredientPrice = priceSummary;

            price = price + extraIngredientPrice;
        }

        price = price * itemAmount;

        totalTextView.setText(String.format("Total: $%.2f", price));

    }

    public void disableView() {
        backButton.setClickable(false);
        completeOrderTextView.setClickable(false);
        halfOrderTextView.setClickable(false);
        smashedStyleTextView.setClickable(false);
        scrambledStyleTextView.setClickable(false);
        whiteBreadTextView.setClickable(false);
        wholemealBreadTextView.setClickable(false);
        baguetteBreadTextView.setClickable(false);
        extraIngredientLayout.setClickable(false);
        commentsEditText.setFocusable(false);
        amountTextView.setClickable(false);
        addOrderButton.setClickable(false);
        requireView().setAlpha((float) .3);
    }

    public void enableView() {
        backButton.setClickable(true);
        completeOrderTextView.setClickable(true);
        halfOrderTextView.setClickable(true);
        smashedStyleTextView.setClickable(true);
        scrambledStyleTextView.setClickable(true);
        whiteBreadTextView.setClickable(true);
        wholemealBreadTextView.setClickable(true);
        baguetteBreadTextView.setClickable(true);
        extraIngredientLayout.setClickable(true);
        commentsEditText.setFocusableInTouchMode(true);
        amountTextView.setClickable(true);
        addOrderButton.setClickable(true);
        requireView().setAlpha((float) 1);
    }

    public void addHalfFoodClicked() {
        enableView();

        ingredientFood = foodViewModel.getSelectedFood().getValue();

        foodStyle = "scrambled";

        if (principalFood.getTitle().equals("Huevos")) {
            ingredientTextView.setVisibility(View.GONE);
            ingredientLayout.setVisibility(View.GONE);
            ingredient = null;
        }

        getTotal();
    }

    public void cancelHalfFoodClicked() {
        enableView();
        smashedStyleTextView.callOnClick();
    }

    public void showNumberPicker(View view){
        AmountOrderFragment newFragment = new AmountOrderFragment();
        newFragment.setValueChangeListener(this);
        newFragment.show(requireActivity().getSupportFragmentManager(), "amount picker");
    }

    private void addItem() {
        Item item = new Item(itemTitle,
                isCompleteItem,
                extraIngredientList,
                "",
                itemAmount,
                commentsEditText.getText().toString().trim(),
                (float) price);

        ItemViewModel.insert(item);

        Toast.makeText(getActivity(),"Agregado al carrito", Toast.LENGTH_SHORT).show();

        Vibrator v = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(400);

        ProgressBar progressBar = requireActivity().findViewById(R.id.menu_activity_progress);

        requireView().animate()
                .translationY(requireView().getHeight())
                .alpha(0.0f)
                .setDuration(500)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        requireActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.menu_frame, MenuFragment.newInstance())
                                .commit();

                        FragmentManager fm = requireActivity().getSupportFragmentManager();
                        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                            fm.popBackStack();
                        }

                        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation);
                        bottomNavigationView.setVisibility(View.VISIBLE);

                        progressBar.setVisibility(View.GONE);
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