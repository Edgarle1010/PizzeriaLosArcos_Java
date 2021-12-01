package com.edgarlopez.pizzerialosarcos.controller;

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
import com.edgarlopez.pizzerialosarcos.adapter.OnAddExtraIngredientClickListener;
import com.edgarlopez.pizzerialosarcos.adapter.OnCancelExtraIngredientClickListener;
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

public class OrderDetailsSecondaryFragment extends Fragment implements View.OnClickListener,
        OnExtraIngredientClickListener,
        NumberPicker.OnValueChangeListener {
    private ItemViewModel itemViewModel;
    private ExtraIngredientViewModel extraIngredientViewModel;
    private FoodViewModel foodViewModel;
    private Food principalFood;

    private RecyclerView recyclerView;
    private ExtraIngredientRecyclerViewAdapter adapter;

    private String foodType;
    private String foodTitle;
    private String itemTitle;
    private String foodSize;
    private boolean isCompleteItem;
    private int itemAmount;
    private float price;
    private List<ExtraIngredient> extraIngredientList;

    private TextView titleFoodTextView,
            descriptionTextView,
            amountTextView,
            totalTextView;
    private EditText commentsEditText;
    private ImageButton backButton;
    private Button addOrderButton;
    private RelativeLayout extraIngredientLayout;

    public OrderDetailsSecondaryFragment() {

    }

    public static OrderDetailsSecondaryFragment newInstance() {
        OrderDetailsSecondaryFragment fragment = new OrderDetailsSecondaryFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_details_secundary, container, false);

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

        recyclerView = view.findViewById(R.id.extraIngredientRecyclerView_secondary);
        backButton = view.findViewById(R.id.back_button_order_detail_secondary);
        titleFoodTextView = view.findViewById(R.id.food_title_order_secondary);
        descriptionTextView = view.findViewById(R.id.food_description_order_secondary);
        commentsEditText = view.findViewById(R.id.order_secondary_comments_edit_text);
        amountTextView = view.findViewById(R.id.order_secondary_amount_text_view);
        addOrderButton = view.findViewById(R.id.order_secondary_add_button);
        extraIngredientLayout = view.findViewById(R.id.extra_ingredient_relative_layout_secondary);
        totalTextView = view.findViewById(R.id.order_secondary_total_text_view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        principalFood = foodViewModel.getSelectedFood().getValue();
        if (principalFood != null) {
            titleFoodTextView.setText(String.format("%s %s", foodTitle, principalFood.getTitle()));
            descriptionTextView.setText(principalFood.getDescription());
        }

        backButton.setOnClickListener(this);
        extraIngredientLayout.setOnClickListener(this);
        amountTextView.setOnClickListener(this);
        addOrderButton.setOnClickListener(this);

        itemTitle = titleFoodTextView.getText().toString().trim();
        isCompleteItem = true;
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
            case R.id.back_button_order_detail_secondary:
                getParentFragmentManager().popBackStack();
                break;
            case R.id.extra_ingredient_relative_layout_secondary:
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
            case R.id.order_secondary_amount_text_view:
                showNumberPicker(getView());
                break;
            case R.id.order_secondary_add_button:
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
                if (foodSize.equals("big")) {
                    extraIngredientPrice = extraIngredients.getbPrice();
                }else if (foodSize.equals("medium")) {
                    extraIngredientPrice = extraIngredients.getmPrice();
                }else {
                    extraIngredientPrice = extraIngredients.getsPrice();
                }
                priceSummary = priceSummary + extraIngredientPrice;
            }
            extraIngredientPrice = priceSummary;
            price = price + extraIngredientPrice;
        }
        price = price * itemAmount;

        totalTextView.setText(String.format("Total: $%s", price));
    }

    public void enableView() {
        extraIngredientLayout.setClickable(true);
        commentsEditText.setFocusableInTouchMode(true);
        amountTextView.setClickable(true);
        addOrderButton.setClickable(true);
        requireView().setAlpha((float) 1);
    }

    public void disableView() {
        extraIngredientLayout.setClickable(false);
        commentsEditText.setFocusable(false);
        amountTextView.setClickable(false);
        addOrderButton.setClickable(false);
        requireView().setAlpha((float) .3);
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

}