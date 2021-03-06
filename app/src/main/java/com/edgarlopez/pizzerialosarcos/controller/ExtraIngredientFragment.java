package com.edgarlopez.pizzerialosarcos.controller;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.adapter.OnAddExtraIngredientClickListener;
import com.edgarlopez.pizzerialosarcos.adapter.OnCancelExtraIngredientClickListener;
import com.edgarlopez.pizzerialosarcos.model.ExtraIngredient;
import com.edgarlopez.pizzerialosarcos.model.ExtraIngredientViewModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.edgarlopez.pizzerialosarcos.util.Util.FOOD_SIZE;
import static com.edgarlopez.pizzerialosarcos.util.Util.FOOD_TYPE;

public class ExtraIngredientFragment extends Fragment implements View.OnClickListener, NumberPicker.OnValueChangeListener {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ImageView cancelImageView;
    private ImageView addImageView;
    private String foodType,
            foodSize;
    private List<ExtraIngredient> extraIngredientList;
    private ExtraIngredient currExtraIngredient;
    private NumberPicker extraIngredientPicker;
    private ExtraIngredientViewModel extraIngredientViewModel;

    private CollectionReference collectionReference = db.collection("ExtraIngredients");

    private OnAddExtraIngredientClickListener addCallback;
    private OnCancelExtraIngredientClickListener cancelCallback;

    public ExtraIngredientFragment() {
    }

    public static ExtraIngredientFragment newInstance() {
        ExtraIngredientFragment fragment = new ExtraIngredientFragment();
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
        View view = inflater.inflate(R.layout.fragment_extra_ingredient, container, false);

        extraIngredientList.clear();

        assert getArguments() != null;
        foodType = getArguments().getString(FOOD_TYPE);
        foodSize = getArguments().getString(FOOD_SIZE);

        cancelImageView = view.findViewById(R.id.cancel_extra_ingredient_image_view);
        addImageView = view.findViewById(R.id.add_extra_ingredient_image_view);
        extraIngredientPicker = view.findViewById(R.id.extra_ingredient_picker);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        extraIngredientViewModel = new ViewModelProvider(requireActivity())
                .get(ExtraIngredientViewModel.class);

        cancelImageView.setOnClickListener(this);
        addImageView.setOnClickListener(this);

        collectionReference
                .orderBy("listPosition")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot extraIngredients : queryDocumentSnapshots) {
                            ExtraIngredient extraIngredient = extraIngredients.toObject(ExtraIngredient.class);

                           if (extraIngredient.getFood().contains(foodType)) {
                                extraIngredientList.add(extraIngredient);
                            }
                        }

                        extraIngredientViewModel.setSelectedExtraIngredients(extraIngredientList);

                        if (extraIngredientViewModel.getExtraIngredients().getValue() != null) {
                            extraIngredientList = extraIngredientViewModel.getExtraIngredients().getValue();

                            String[] array = new String[extraIngredientList.size()];

                            for(int j = 0; j < extraIngredientList.size(); j++) {
                                int price;

                                if (extraIngredientList.get(j).getbPrice() == null) {
                                    foodSize = "big";
                                }
//                                if (extraIngredientList.get(j).getsPrice() == 0) {
//                                    foodSize = "big";
//                                }

                                if (foodSize.equals("big")) {
                                    price = extraIngredientList.get(j).getbPrice();
                                }else if (foodSize.equals("medium")) {
                                    price = extraIngredientList.get(j).getmPrice();
                                }else {
                                    price = extraIngredientList.get(j).getsPrice();
                                }
                                array[j] = extraIngredientList.get(j).getTitle()
                                        + " $"
                                        + price;

                            }

                            if (extraIngredientList.size() > 1) {
                                extraIngredientPicker.setMaxValue(extraIngredientList.size() - 1);
                                extraIngredientPicker.setMinValue(0);
                                extraIngredientPicker.setDisplayedValues(array);
                                extraIngredientPicker.setWrapSelectorWheel(true);
                                extraIngredientPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                                extraIngredientPicker.setOnValueChangedListener(this);
                                extraIngredientViewModel.setSelectedExtraIngredient(extraIngredientList.get(0));
                                currExtraIngredient = extraIngredientList.get(0);
                            }
                        }

                    }else {
                        Toast.makeText(getContext(), "Lista vac??a", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_extra_ingredient_image_view:
                cancelButtonPressed();
                break;
            case R.id.add_extra_ingredient_image_view:
                addButtonPressed();
                break;
        }

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        extraIngredientViewModel.setSelectedExtraIngredient(extraIngredientList.get(newVal));
        currExtraIngredient = extraIngredientList.get(newVal);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnAddExtraIngredientClickListener
            && context instanceof OnCancelExtraIngredientClickListener) {
            addCallback = (OnAddExtraIngredientClickListener) context;
            cancelCallback = (OnCancelExtraIngredientClickListener) context;
        }else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCancelExtraIngredientClickListener");
        }
    }

    @Override
    public void onDetach() {
        addCallback = null;
        cancelCallback = null;
        super.onDetach();
    }

    public void addButtonPressed() {
        boolean alreadyExist = false;
        MenuActivity ma = (MenuActivity) getActivity();
        assert ma != null;
        if (ma.currExtraIngredients != null) {
            for (ExtraIngredient extraIngredient : ma.currExtraIngredients) {
                if (extraIngredient.getId().equals(currExtraIngredient.getId())) {
                    alreadyExist = true;
                }
            }
        }

        if (alreadyExist) {
            Toast.makeText(getActivity(), "El ingrediente extra seleccionado ya ha sido agregado anteriormente", Toast.LENGTH_SHORT).show();
        }else {
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
                            getParentFragmentManager().popBackStack();
                            addCallback.onAddExtraIngredientClicked();
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
                        getParentFragmentManager().popBackStack();
                        cancelCallback.onCancelExtraIngredientClicked();
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