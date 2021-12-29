package com.edgarlopez.pizzerialosarcos.controller;

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
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edgarlopez.pizzerialosarcos.R;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsDrinksFragment extends Fragment implements View.OnClickListener,
        NumberPicker.OnValueChangeListener {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FoodViewModel foodViewModel;
    private Food principalFood;
    private Food currDrink;
    private List<Food> foodList;
    private ProgressBar progressBar;
    private TextView titleFoodTextView,
            descriptionTextView,
            sodaTextView,
            sizeTextView,
            pitcherSizeTextView,
            glassSizeTextView,
            amountTextView,
            totalTextView;
    private ImageButton backButton;
    private EditText commentsEditText;
    private Button addOrderButton;
    private String foodType,
            foodTitle;
    private NumberPicker sodaNumberPicker;

    private LinearLayout sizeLayout;

    private ItemViewModel itemViewModel;

    private CollectionReference collectionReference = db.collection("Food");

    private String itemTitle;
    private String foodSize;
    private String foodSizeTitle;
    private boolean isCompleteItem;
    private int itemAmount;
    private float price;

    public OrderDetailsDrinksFragment() {

    }

    public static OrderDetailsDrinksFragment newInstance() {
        OrderDetailsDrinksFragment fragment = new OrderDetailsDrinksFragment();
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
        View view = inflater.inflate(R.layout.fragment_order_details_drinks, container, false);

        foodViewModel = new ViewModelProvider(requireActivity())
                .get(FoodViewModel.class);

        itemViewModel = new ViewModelProvider.AndroidViewModelFactory(requireActivity()
                .getApplication())
                .create(ItemViewModel.class);

        assert getArguments() != null;
        foodType = getArguments().getString(FOOD_TYPE);
        foodTitle = getArguments().getString(FOOD_TITLE);

        progressBar = requireActivity().findViewById(R.id.menu_activity_progress);
        backButton = view.findViewById(R.id.back_button_order_detail_drinks);
        titleFoodTextView = view.findViewById(R.id.food_title_order_drinks);
        descriptionTextView = view.findViewById(R.id.food_description_order_drinks);
        sodaTextView = view.findViewById(R.id.soda_text_view);
        sodaNumberPicker = view.findViewById(R.id.soda_number_picker);
        sizeTextView = view.findViewById(R.id.food_size_order_drinks);
        sizeLayout = view.findViewById(R.id.size_layout_drinks);
        pitcherSizeTextView = view.findViewById(R.id.size_pitcher_text_view);
        glassSizeTextView = view.findViewById(R.id.size_glass_text_view);
        commentsEditText = view.findViewById(R.id.order_comments_edit_text_drinks);
        amountTextView = view.findViewById(R.id.order_amount_text_view_drinks);
        addOrderButton = view.findViewById(R.id.order_add_button_drinks);
        totalTextView = view.findViewById(R.id.order_total_text_view_drinks);

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

        DrawableCompat.setTint(pitcherSizeTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
        DrawableCompat.setTint(amountTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
        DrawableCompat.setTint(totalTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));

        backButton.setOnClickListener(this);
        pitcherSizeTextView.setOnClickListener(this);
        glassSizeTextView.setOnClickListener(this);
        amountTextView.setOnClickListener(this);
        addOrderButton.setOnClickListener(this);

        if (principalFood.getTitle().contains("naranja") ||
            principalFood.getTitle().contains("Agua") ||
            principalFood.getTitle().contains("Limonada") ||
            principalFood.getTitle().contains("Café") ||
            principalFood.getTitle().contains("vainilla") ||
            principalFood.getTitle().contains("Chocolate") ||
            principalFood.getTitle().contains("Leche")) {
            sodaTextView.setVisibility(View.GONE);
            sodaNumberPicker.setVisibility(View.GONE);
            if (principalFood.getTitle().contains("naranja") ||
                principalFood.getTitle().contains("Limonada") ||
                principalFood.getTitle().contains("Chocolate")) {
                sizeTextView.setVisibility(View.VISIBLE);
                sizeLayout.setVisibility(View.VISIBLE);
                if (principalFood.getTitle().contains("Chocolate")) {
                    pitcherSizeTextView.setText("Caliente");
                    glassSizeTextView.setText("Frio");
                    foodSizeTitle = "Caliente";
                } else {
                    foodSizeTitle = "Jarra";
                }
            }
        } else {
            progressBar.setVisibility(View.VISIBLE);
            collectionReference
                    .orderBy("listPosition")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        progressBar.setVisibility(View.GONE);
                        foodList.clear();
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

                                String[] array = new String[foodList.size()];

                                for(int j = 0; j < foodList.size(); j++) {
                                    array[j] = foodList.get(j).getTitle();
                                }

                                if (foodList.size() > 1) {
                                    sodaNumberPicker.setMaxValue(foodList.size() - 1);
                                    sodaNumberPicker.setMinValue(0);
                                    sodaNumberPicker.setDisplayedValues(array);
                                    sodaNumberPicker.setWrapSelectorWheel(true);
                                    sodaNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                                    sodaNumberPicker.setOnValueChangedListener(this);
                                    foodViewModel.setSelectedFood(foodList.get(0));
                                    currDrink = foodList.get(0);
                                }
                            }

                        }else {
                            Toast.makeText(getContext(), "Lista vacía", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    });
        }

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
            case R.id.back_button_order_detail_drinks:
                getParentFragmentManager().popBackStack();
                break;
            case R.id.size_pitcher_text_view:
                DrawableCompat.setTint(pitcherSizeTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
                DrawableCompat.setTint(glassSizeTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));

                foodSize = "big";
                if (principalFood.getTitle().contains("Chocolate")) {
                    foodSizeTitle = "Caliente";
                } else {
                    foodSizeTitle = "Jarra";
                }
                getTotal();
                break;
            case R.id.size_glass_text_view:
                DrawableCompat.setTint(glassSizeTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.third_color));
                DrawableCompat.setTint(pitcherSizeTextView.getBackground(), ContextCompat.getColor(requireActivity(), R.color.quarter_color));

                if (principalFood.getTitle().contains("Chocolate")) {
                    foodSize = "big";
                    foodSizeTitle = "Frio";
                } else {
                    foodSize = "small";
                    foodSizeTitle = "Vaso";
                }
                getTotal();
                break;
            case R.id.order_amount_text_view_drinks:
                showNumberPicker(getView());
                break;
            case R.id.order_add_button_drinks:
                addItem();
                break;
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        if (picker.equals(sodaNumberPicker)) {
            currDrink = foodList.get(newVal);
            principalFood = currDrink;
        } else {
            itemAmount = picker.getValue();
            amountTextView.setText(String.valueOf(itemAmount));
        }

        getTotal();
    }

    public void getTotal() {
        float sizePrice;

        if (foodSize.equals("big")) {
            sizePrice = principalFood.getbPrice();
        }else if (foodSize.equals("medium")) {
            sizePrice = principalFood.getmPrice();
        }else {
            sizePrice = principalFood.getsPrice();
        }

        price = sizePrice * itemAmount;

        totalTextView.setText(String.format("Total: $%s", price));
    }

    public void showNumberPicker(View view){
        AmountOrderFragment newFragment = new AmountOrderFragment();
        newFragment.setValueChangeListener(this);
        newFragment.show(requireActivity().getSupportFragmentManager(), "amount picker");
    }

    private void addItem() {
        if (foodSizeTitle == null) {
            itemTitle = principalFood.getTitle();
        } else if (principalFood.getTitle().contains("naranja") ||
                principalFood.getTitle().contains("Limonada") ||
                principalFood.getTitle().contains("Chocolate")) {
            itemTitle = String.format("%s | %s", principalFood.getTitle(), foodSizeTitle);
        } else {
            itemTitle = String.format("%s | %s", principalFood.getTitle(), currDrink.getTitle());
        }

        Item item = new Item(itemTitle,
                isCompleteItem,
                null,
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
}