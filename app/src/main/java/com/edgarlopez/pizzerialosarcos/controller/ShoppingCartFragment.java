package com.edgarlopez.pizzerialosarcos.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.adapter.BanedCallBack;
import com.edgarlopez.pizzerialosarcos.adapter.DocumentIdCallBack;
import com.edgarlopez.pizzerialosarcos.adapter.FirestoreCallback;
import com.edgarlopez.pizzerialosarcos.adapter.RecyclerItemTouchHelper;
import com.edgarlopez.pizzerialosarcos.adapter.ServiceCallBack;
import com.edgarlopez.pizzerialosarcos.model.Item;
import com.edgarlopez.pizzerialosarcos.model.ItemViewModel;
import com.edgarlopez.pizzerialosarcos.model.Order;
import com.edgarlopez.pizzerialosarcos.model.UserViewModel;
import com.edgarlopez.pizzerialosarcos.ui.ItemRecyclerViewAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class ShoppingCartFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final int ALL_PERMISSIONS_RESULT = 1111;
    private GoogleApiClient client;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissions = new ArrayList<>();
    private ArrayList<String> permissionsRejected = new ArrayList();
    private LocationRequest locationRequest;

    public static final long UPDATE_INTERVAL = 5000;
    public static final long FASTEST_INTERVAL = 5000;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private UserViewModel userViewModel;
    private ItemViewModel itemViewModel;

    private Button sendButton;
    private TextView totalTextView;
    private RecyclerView recyclerView;
    private ItemRecyclerViewAdapter itemRecyclerViewAdapter;
    private ProgressBar progressBar;

    private float price = 0;
    private String waitTime = "";
    private String currentLocation;
    private boolean isBaned = false;
    private String messageStatus = "";
    private boolean activeService = true;

    public ShoppingCartFragment() {

    }

    public static ShoppingCartFragment newInstance() {
        ShoppingCartFragment fragment = new ShoppingCartFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionsToRequest = permissionsToRequest(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(
                        new String[permissionsToRequest.size()]),
                        ALL_PERMISSIONS_RESULT
                );
            }
        }

        client = new GoogleApiClient.Builder(requireActivity())
                .addApi(LocationServices.API)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .build();

        userViewModel = new ViewModelProvider.AndroidViewModelFactory(requireActivity()
                .getApplication())
                .create(UserViewModel.class);

        itemViewModel = new ViewModelProvider.AndroidViewModelFactory(requireActivity()
                .getApplication())
                .create(ItemViewModel.class);

        itemViewModel.getAllItems().observe(getViewLifecycleOwner(), items -> {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            itemRecyclerViewAdapter = new ItemRecyclerViewAdapter(items, requireActivity());
            recyclerView.setAdapter(itemRecyclerViewAdapter);

            price = 0;
            for (Item itemList : items) {
                price = price + itemList.getPrice();
            }

            totalTextView.setText("Total: $" + price);
        });

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        sendButton.setOnClickListener(view1 -> {
            if (Objects.requireNonNull(itemViewModel.getAllItems().getValue()).size() != 0) {
                readData(waitTime -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                    builder.setTitle("¿Seguro que quieres realizar el pedido?")
                            .setMessage("Tiempo de espera promedio: " + waitTime + " minutos");
                    builder.setPositiveButton("Sí", (dialogInterface, i) -> {
                        if (user.getPhoneNumber() != null && user.getDisplayName() != null) {
                            checkBaned(baned -> {
                                if (!baned) {
                                    checkService(activeService -> {
                                        Order order = new Order();
                                        if (activeService) {
                                            String name = userViewModel.getOne(user.getUid()).getName();
                                            String lastName = userViewModel.getOne(user.getUid()).getLastName();

                                            order.setClient(user.getPhoneNumber());
                                            order.setClientName(name + " " + lastName);
                                            order.setDate(System.currentTimeMillis());
                                            if (currentLocation != null) {
                                                order.setLocation(currentLocation);
                                            } else {
                                                order.setLocation("Ubicación no proporcionada.");
                                            }
                                            order.setComplete(false);
                                            order.setItems(itemViewModel.getAllItems().getValue().size());
                                            order.setItemList(itemViewModel.getAllItems().getValue());
                                            order.setTotalPrice(price);

                                            checkOrderId(id -> {
                                                String fol = "F" + id;
                                                db.collection("orders")
                                                        .document(fol)
                                                        .set(order).addOnSuccessListener(documentReference -> {
                                                    AlertDialog.Builder aD = new AlertDialog.Builder(requireActivity());
                                                    aD.setTitle(R.string.order_sent_successfully_title);
                                                    aD.setMessage(String.format(getString(R.string.order_successfully_message), waitTime, fol));
                                                    aD.setPositiveButton(R.string.acept, (dialog, which) -> {
                                                        ItemViewModel.deleteAll();
                                                    });
                                                    aD.setCancelable(false);

                                                    AlertDialog dialog = aD.create();
                                                    dialog.show();
                                                });
                                            });

                                        } else {
                                            AlertDialog.Builder aD = new AlertDialog.Builder(requireActivity());
                                            aD.setTitle(R.string.notice);
                                            aD.setMessage(messageStatus);
                                            aD.setPositiveButton(R.string.acept, null);

                                            AlertDialog dialog = aD.create();
                                            dialog.show();
                                        }
                                    });
                                } else {
                                    AlertDialog.Builder aD = new AlertDialog.Builder(requireActivity());
                                    aD.setTitle(R.string.problem_occurred);
                                    aD.setMessage(R.string.baned_dialog);
                                    aD.setPositiveButton(R.string.acept, null);

                                    AlertDialog dialog = aD.create();
                                    dialog.show();
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("No", (dialogInterface, i) -> {
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                });
            } else {
                Toast.makeText(requireActivity(), "La lista está vacía", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String perm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(requireActivity(), perm) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_shopping_cart, container, false);

        recyclerView = view.findViewById(R.id.orderRecyclerView);
        sendButton = view.findViewById(R.id.send_order_button);
        totalTextView = view.findViewById(R.id.shopping_total_text_view);
        progressBar = requireActivity().findViewById(R.id.menu_activity_progress);

        return view;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ItemRecyclerViewAdapter.ViewHolder) {

            // remove the item from recycler view
            ItemViewModel.delete(Objects.requireNonNull(itemViewModel.getAllItems().getValue()).get(position));

            itemRecyclerViewAdapter.notifyItemRemoved(position);

        }
    }

    private void checkOrderId(DocumentIdCallBack documentIdCallBack) {
        progressBar.setVisibility(View.VISIBLE);

        db.collection("orders")
                .get().addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                documentIdCallBack.onDocumentIdChecked(task.getResult().size());
            } else {
                Toast.makeText(requireActivity(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkService(ServiceCallBack serviceCallBack) {
        progressBar.setVisibility(View.VISIBLE);

        db.collection("messages")
                .document("current")
                .get().addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();

                assert doc != null;
                if (doc.exists()) {
                    messageStatus = doc.getString("message");
                    activeService = doc.getBoolean("status");
                    serviceCallBack.onServiceChecked(activeService);
                }
            } else {
                Toast.makeText(requireActivity(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkBaned(BanedCallBack banedCallBack) {
        progressBar.setVisibility(View.VISIBLE);

        db.collection("Users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    progressBar.setVisibility(View.GONE);
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot users : queryDocumentSnapshots) {

                            if (Objects.equals(users.getString("phoneNumber"), user.getPhoneNumber())) {
                                isBaned = users.getBoolean("baned");
                                banedCallBack.onCheckBaned(isBaned);
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                });
    }

    private void readData(FirestoreCallback firestoreCallback) {
        progressBar.setVisibility(View.VISIBLE);

        db.collection("waitTime")
                .document("time")
                .get().addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();

                assert doc != null;
                if (doc.exists()) {
                    waitTime = doc.getString("time");
                    firestoreCallback.onCallback(waitTime);
                }
            } else {
                Toast.makeText(requireActivity(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (client != null) {
            client.connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (client != null && client.isConnected()) {
            LocationServices.getFusedLocationProviderClient(requireActivity())
                    .removeLocationUpdates(new LocationCallback() {
                    });
            client.disconnect();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        /*if (location != null && isAdded()) {
            Toast.makeText(requireActivity(), "Lat: " + location.getLatitude()
                    + " Lon: " +location.getLongitude(), Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (isAdded()) {
            if (ActivityCompat.checkSelfPermission(requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), location -> {
                        //Get last know location. But it could be null
                        if (location != null && isAdded()) {
                            currentLocation = location.getLatitude() + "," + location.getLongitude();
                        }
                    });
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(requireActivity(),
                    "You need to enable permission to display location!",
                    Toast.LENGTH_SHORT).show();
        }

        LocationServices.getFusedLocationProviderClient(requireActivity())
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);

                        if (locationResult != null && isAdded()) {
                            Location location = locationResult.getLastLocation();
                            /*Toast.makeText(requireActivity(), "Lat: " + location.getLatitude()
                                    + " Lon: " + location.getLongitude(), Toast.LENGTH_SHORT).show();*/
                        }
                    }

                    @Override
                    public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                        super.onLocationAvailability(locationAvailability);
                    }
                }, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perm : permissionsToRequest) {
                    if (!hasPermission(perm)) {
                        permissionsRejected.add(perm);
                    }
                }
                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            new AlertDialog.Builder(requireActivity())
                                    .setMessage("These permissions are mandatory to get location")
                                    .setPositiveButton("OK", (dialogInterface, i) -> {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(permissionsRejected.toArray(
                                                    new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                        }
                                    }).setNegativeButton("Cancel", null).create().show();
                        }
                    }
                } else {
                    if (client != null) {
                        client.connect();
                    }
                }
                break;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}