package com.edgarlopez.pizzerialosarcos.controller;

import static com.edgarlopez.pizzerialosarcos.util.Util.FOOD_ITEM;
import static com.edgarlopez.pizzerialosarcos.util.Util.FOOD_SIZE;
import static com.edgarlopez.pizzerialosarcos.util.Util.FOOD_TITLE;
import static com.edgarlopez.pizzerialosarcos.util.Util.FOOD_TYPE;
import static com.edgarlopez.pizzerialosarcos.util.Util.FRENCH_FRIES_ID;

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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.Objects;

public class OrderDetailsTertiaryFragment extends Fragment implements View.OnClickListener,
        OnExtraIngredientClickListener,
        NumberPicker.OnValueChangeListener {
    private FoodViewModel foodViewModel;
    private Food principalFood;
    private ExtraIngredientViewModel extraIngredientViewModel;
    private TextView titleFoodTextView,
            descriptionTextView,
            hashFillingTextView,
            chickenFillingTextView,
            cheeseFillingTextView,
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
    private String foodSize;
    private boolean isCompleteItem;
    private String foodFilling;
    private List<ExtraIngredient> extraIngredientList;
    private int itemAmount;
    private float price;

    private RecyclerView recyclerView;
    private ExtraIngredientRecyclerViewAdapter adapter;

    public OrderDetailsTertiaryFragment() {

    }

    public static OrderDetailsTertiaryFragment newInstance() {
        OrderDetailsTertiaryFragment fragment = new OrderDetailsTertiaryFragment();
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
        View view = inflater.inflate(R.layout.fragment_order_details_tertiary, container, false);

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

        recyclerView = view.findViewById(R.id.extraIngredientRecyclerView_tertiary);

        backButton = view.findViewById(R.id.back_button_order_detail_tertiary);
        titleFoodTextView = view.findViewById(R.id.food_title_order_tertiary);
        descriptionTextView = view.findViewById(R.id.food_description_order_tertiary);
        hashFillingTextView = view.findViewById(R.id.filling_hash_text_view);
        chickenFillingTextView = view.findViewById(R.id.filling_chicken_text_view);
        cheeseFillingTextView = view.findViewById(R.id.filling_cheese_text_view);
        commentsEditText = view.findViewById(R.id.order_comments_edit_text_tertiary);
        amountTextView = view.findViewById(R.id.order_amount_text_view_tertiary);
        addOrderButton = view.findViewById(R.id.order_add_button_tertiary);
        extraIngredientLayout = view.findViewById(R.id.extra_ingredient_relative_layout_tertiary);
        totalTextView = view.findViewById(R.id.order_total_text_view_tertiary);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        principalFood = foodViewModel.getSelectedFood().getValue();
        if (principalFood != null) {
            titleFoodTextView.setText(principalFood.getTitle());

            String description = principalFood.getDescription();
            if (description != null) {
                if (!description.isEmpty()) {
                    descriptionTextView.setText(description);
                } else {
                    descriptionTextView.setVisibility(View.GONE);
                }
            } else {
                descriptionTextView.setVisibility(View.GONE);
            }
        }

        DrawableCompat.setTint(hashFillingTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
        DrawableCompat.setTint(amountTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
        DrawableCompat.setTint(totalTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));

        backButton.setOnClickListener(this);
        hashFillingTextView.setOnClickListener(this);
        chickenFillingTextView.setOnClickListener(this);
        cheeseFillingTextView.setOnClickListener(this);
        extraIngredientLayout.setOnClickListener(this);
        amountTextView.setOnClickListener(this);
        addOrderButton.setOnClickListener(this);

        itemTitle = titleFoodTextView.getText().toString().trim();
        isCompleteItem = true;
        foodFilling = "Picadillo";
        foodSize = "big";
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_order_detail_tertiary:
                getParentFragmentManager().popBackStack();
                break;
            case R.id.filling_hash_text_view:
                DrawableCompat.setTint(hashFillingTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
                DrawableCompat.setTint(chickenFillingTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));
                DrawableCompat.setTint(cheeseFillingTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));

                foodFilling = "Picadillo";
                getTotal();
                break;
            case R.id.filling_chicken_text_view:
                DrawableCompat.setTint(chickenFillingTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
                DrawableCompat.setTint(hashFillingTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));
                DrawableCompat.setTint(cheeseFillingTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));

                foodFilling = "Pollo";
                getTotal();
                break;
            case R.id.filling_cheese_text_view:
                DrawableCompat.setTint(cheeseFillingTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
                DrawableCompat.setTint(hashFillingTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));
                DrawableCompat.setTint(chickenFillingTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));

                foodFilling = "Queso";
                getTotal();
                break;
            case R.id.extra_ingredient_relative_layout_tertiary:
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
            case R.id.order_amount_text_view_tertiary:
                showNumberPicker(getView());
                break;
            case R.id.order_add_button_tertiary:
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

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        itemAmount = picker.getValue();
        amountTextView.setText(String.valueOf(itemAmount));
        getTotal();
    }

    public void disableView() {
        backButton.setClickable(false);
        hashFillingTextView.setClickable(false);
        chickenFillingTextView.setClickable(false);
        cheeseFillingTextView.setClickable(false);
        extraIngredientLayout.setClickable(false);
        commentsEditText.setFocusable(false);
        amountTextView.setClickable(false);
        addOrderButton.setClickable(false);
        requireView().setAlpha((float) .3);
    }

    public void enableView() {
        backButton.setClickable(true);
        hashFillingTextView.setClickable(true);
        chickenFillingTextView.setClickable(true);
        cheeseFillingTextView.setClickable(true);
        extraIngredientLayout.setClickable(true);
        commentsEditText.setFocusableInTouchMode(true);
        amountTextView.setClickable(true);
        addOrderButton.setClickable(true);
        requireView().setAlpha((float) 1);
    }

    public void showNumberPicker(View view){
        AmountOrderFragment newFragment = new AmountOrderFragment();
        newFragment.setValueChangeListener(this);
        newFragment.show(requireActivity().getSupportFragmentManager(), "amount picker");
    }

    public void getTotal() {
        float sizePrice;
        float extraIngredientPrice;

        sizePrice = principalFood.getbPrice();

        price = sizePrice;

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(requireActivity());
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

    private void addItem() {
        Item item = new Item(principalFood.getId(),
                itemTitle,
                isCompleteItem,
                extraIngredientList,
                foodFilling,
                itemAmount,
                commentsEditText.getText().toString().trim(),
                price);

        ItemViewModel.insert(item);

        Toast.makeText(getActivity(),"Agregado al carrito", Toast.LENGTH_SHORT).show();

        Vibrator v = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(400);

        ProgressBar progressBar = requireActivity().findViewById(R.id.menu_activity_progress);

        requireView().animate()
                .translationY(getView().getHeight())
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