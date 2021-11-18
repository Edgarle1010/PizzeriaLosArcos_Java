package com.edgarlopez.pizzerialosarcos.controller;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.model.ItemViewModel;
import com.edgarlopez.pizzerialosarcos.model.User;
import com.edgarlopez.pizzerialosarcos.model.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MoreFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    private UserViewModel userViewModel;
    private ItemViewModel itemViewModel;
    private FirebaseUser user;

    private Button logoutButton;

    public MoreFragment() {

    }

    public static MoreFragment newInstance() {
        MoreFragment fragment = new MoreFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        userViewModel = new ViewModelProvider.AndroidViewModelFactory(requireActivity()
                .getApplication())
                .create(UserViewModel.class);

        itemViewModel = new ViewModelProvider.AndroidViewModelFactory(requireActivity()
                .getApplication())
                .create(ItemViewModel.class);

        logoutButton = view.findViewById(R.id.logout_button);

        logoutButton.setOnClickListener(v -> {
            if (user != null && firebaseAuth != null) {
                firebaseAuth.signOut();

                ItemViewModel.deleteAll();
                UserViewModel.deleteAll();

                Intent intent = new Intent(getActivity(),
                        WelcomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}