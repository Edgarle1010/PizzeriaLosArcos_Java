package com.edgarlopez.pizzerialosarcos.controller;

import static com.edgarlopez.pizzerialosarcos.util.Util.EGGS_INGREDIENTS_ID;
import static com.edgarlopez.pizzerialosarcos.util.Util.FOOD_ITEM;
import static com.edgarlopez.pizzerialosarcos.util.Util.FOOD_SIZE;
import static com.edgarlopez.pizzerialosarcos.util.Util.FOOD_TITLE;
import static com.edgarlopez.pizzerialosarcos.util.Util.FOOD_TYPE;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.adapter.OnExtraIngredientClickListener;
import com.edgarlopez.pizzerialosarcos.model.ExtraIngredient;
import com.edgarlopez.pizzerialosarcos.model.ExtraIngredientViewModel;
import com.edgarlopez.pizzerialosarcos.model.Food;
import com.edgarlopez.pizzerialosarcos.model.FoodViewModel;
import com.edgarlopez.pizzerialosarcos.model.ItemViewModel;
import com.edgarlopez.pizzerialosarcos.ui.ExtraIngredientRecyclerViewAdapter;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

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
            smashedStyleTextView,
            scrambledStyleTextView,
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

    private ItemViewModel itemViewModel;

    private String itemTitle;
    private boolean isCompleteItem;
    private String foodSize;
    private String foodStyle;
    private String foodBread;
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
        smashedStyleTextView = view.findViewById(R.id.style_smashed_text_view);
        scrambledStyleTextView = view.findViewById(R.id.style_scrambled_text_view);
        whiteBreadTextView = view.findViewById(R.id.bread_white_text_view);
        wholemealBreadTextView = view.findViewById(R.id.bread_wholemeal_text_view);
        baguetteBreadTextView = view.findViewById(R.id.bread_baguette_text_view);
        commentsEditText = view.findViewById(R.id.order_comments_edit_text_five);
        amountTextView = view.findViewById(R.id.order_amount_text_view_five);
        addOrderButton = view.findViewById(R.id.order_add_button_five);
        extraIngredientLayout = view.findViewById(R.id.extra_ingredient_relative_layout_five);
        totalTextView = view.findViewById(R.id.order_total_text_view_five);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        principalFood = foodViewModel.getSelectedFood().getValue();
        if (principalFood != null) {
            titleFoodTextView.setText(principalFood.getTitle());
            descriptionTextView.setText(principalFood.getDescription());
        }

        DrawableCompat.setTint(completeOrderTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
        DrawableCompat.setTint(smashedStyleTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
        DrawableCompat.setTint(whiteBreadTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
        DrawableCompat.setTint(amountTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
        DrawableCompat.setTint(totalTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));

        backButton.setOnClickListener(this);
        completeOrderTextView.setOnClickListener(this);
        halfOrderTextView.setOnClickListener(this);
        smashedStyleTextView.setOnClickListener(this);
        scrambledStyleTextView.setOnClickListener(this);
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
        foodBread = "white";
        itemAmount = 1;
        price = 0;

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_order_detail_five:
                getParentFragmentManager().popBackStack();
                break;
            case R.id.complete_text_view_five:
                DrawableCompat.setTint(completeOrderTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
                DrawableCompat.setTint(halfOrderTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));
                titleFoodTextView.setText(principalFood.getTitle());
                descriptionTextView.setText(principalFood.getDescription());
                itemTitle = titleFoodTextView.getText().toString().trim();

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
            /*case R.id.size_small_text_view:
                DrawableCompat.setTint(smallSizeTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
                DrawableCompat.setTint(bigSizeTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));
                DrawableCompat.setTint(mediumSizeTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));

                foodSize = "small";
                getTotal();
                break;
            case R.id.extra_ingredient_relative_layout:
                disableView();

                Fragment fragment = new ExtraIngredientFragment();
                Bundle bundle = new Bundle();
                bundle.putString(FOOD_TYPE, foodType);
                bundle.putString(FOOD_SIZE, foodSize);
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
            case R.id.order_amount_text_view:
                showNumberPicker(getView());
                break;
            case R.id.order_add_button:
                addItem();
                break;*/
        }
    }

    @Override
    public void onExtraIngredientClicked(ExtraIngredient extraIngredient) {

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }

    public void getTotal() {
        float extraIngredientPrice;

        if (foodSize.equals("complete")) {
            price = principalFood.getbPrice();
        }else {
            price = (principalFood.getbPrice() * .7);
        }

        if (foodStyle.equals("scrambled")) {
            price = price + ingredientFood.getbPrice();
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

        totalTextView.setText(String.format("Total: $%s", price));

    }

    public void disableView() {
        backButton.setClickable(false);
        completeOrderTextView.setClickable(false);
        halfOrderTextView.setClickable(false);
        smashedStyleTextView.setClickable(false);
        scrambledStyleTextView.setClickable(false);
        whiteBreadTextView.setClickable(false);
        whiteBreadTextView.setClickable(false);
        baguetteBreadTextView.setClickable(false);
        extraIngredientLayout.setClickable(false);
        commentsEditText.setFocusable(false);
        amountTextView.setClickable(false);
        addOrderButton.setClickable(false);
        requireView().setAlpha((float) .3);
    }
}