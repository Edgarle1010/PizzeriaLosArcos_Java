package com.edgarlopez.pizzerialosarcos.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.model.ItemViewModel;
import com.edgarlopez.pizzerialosarcos.model.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MoreFragment extends Fragment implements View.OnClickListener {
    private CardView userInformationCardView,
            ordersHistoryCardView,
            howToGetCardView,
            callCardView,
            commentsCardView;
    private TextView userNameTextView,
            phoneNumberTextView;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    private static final int REQUEST = 112;

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

        userInformationCardView = view.findViewById(R.id.user_information_cardview);
        ordersHistoryCardView = view.findViewById(R.id.orders_history_cardview);
        howToGetCardView = view.findViewById(R.id.how_to_get_cardview);
        callCardView = view.findViewById(R.id.call_cardview);
        commentsCardView = view.findViewById(R.id.comments_cardview);
        userNameTextView = view.findViewById(R.id.username_textview);
        phoneNumberTextView = view.findViewById(R.id.phone_number_textview);

        userInformationCardView.setOnClickListener(this);
        ordersHistoryCardView.setOnClickListener(this);
        howToGetCardView.setOnClickListener(this);
        callCardView.setOnClickListener(this);
        commentsCardView.setOnClickListener(this);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_information_cardview:
                startActivity(new Intent(requireActivity(),
                        UserInformationActivity.class));
                break;
            case R.id.orders_history_cardview:
                startActivity(new Intent(requireActivity(),
                        OrdersHistoryActivity.class));
                break;
            case R.id.how_to_get_cardview:
                Intent intentMaps = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://goo.gl/maps/rVtzSXdvUaxMSc1M9"));
                startActivity(intentMaps);
                break;
            case R.id.call_cardview:
                if (Build.VERSION.SDK_INT >= 23) {
                    String[] PERMISSIONS = {android.Manifest.permission.CALL_PHONE};
                    if (!hasPermissions(requireActivity(), PERMISSIONS)) {
                        ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, REQUEST );
                    } else {
                        makeCall();
                    }
                } else {
                    makeCall();
                }
                break;
            case R.id.comments_cardview:
                Intent selectorIntent = new Intent(Intent.ACTION_SENDTO);
                selectorIntent.setData(Uri.parse("mailto:"));

                final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"pizzeriagmapp@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Pizzería Los Arcos App");
                emailIntent.setSelector( selectorIntent );

                requireActivity().startActivity(Intent.createChooser(emailIntent, "Enviar correo por..."));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        String currentUserId = user.getUid();

        collectionReference
                .whereEqualTo("userId", currentUserId)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {

                    if (e != null) {
                    }
                    assert queryDocumentSnapshots != null;
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            String name = snapshot.getString("name");
                            String lastName = snapshot.getString("lastName");
                            String phoneNumber = snapshot.getString("phoneNumber");

                            userNameTextView.setText(name + " " + lastName);
                            phoneNumberTextView.setText(phoneNumber);
                        }

                    }

                });
    }

    public void makeCall()
    {
        String number="+526255834400";
        Intent intent4=new Intent(Intent.ACTION_CALL);
        intent4.setData(Uri.parse("tel:"+number));
        startActivity(intent4);
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeCall();
                } else {
                    Toast.makeText(requireActivity(), "The app was not allowed to call.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}