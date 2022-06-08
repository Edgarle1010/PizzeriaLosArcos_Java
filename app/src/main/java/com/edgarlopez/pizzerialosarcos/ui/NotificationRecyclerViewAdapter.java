package com.edgarlopez.pizzerialosarcos.ui;

import static com.edgarlopez.pizzerialosarcos.util.Util.NOTIFICATIONS_COLLECTION;
import static com.edgarlopez.pizzerialosarcos.util.Util.NOTIFICATIONS_DATE_SEND;
import static com.edgarlopez.pizzerialosarcos.util.Util.NOTIFICATIONS_VIEWED;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.adapter.DocumentIdCallBack;
import com.edgarlopez.pizzerialosarcos.adapter.FirestoreCallback;
import com.edgarlopez.pizzerialosarcos.adapter.FirestoreCallbackUserToken;
import com.edgarlopez.pizzerialosarcos.adapter.OnNotificationClickListener;
import com.edgarlopez.pizzerialosarcos.model.Notification;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Notification> notificationList;
    private final OnNotificationClickListener notificationClickListener;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public NotificationRecyclerViewAdapter(Context context, List<Notification> notificationList, OnNotificationClickListener notificationClickListener) {
        this.context = context;
        this.notificationList = notificationList;
        this.notificationClickListener = notificationClickListener;
    }

    @NonNull
    @Override
    public NotificationRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.notification_row, parent, false);

        return new NotificationRecyclerViewAdapter.ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationRecyclerViewAdapter.ViewHolder holder, int position) {
        Notification notification = notificationList.get(position);

        holder.folio.setText(notification.getFolio());
        holder.title.setText(notification.getTitle());
        holder.description.setText(notification.getDescription());

        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.date.setText(sfd.format(notification.getDateSend() * 1000));

        if (notification.viewed) {
            holder.viewedImage.setVisibility(View.GONE);
        }

        holder.buttonViewOption.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(context, holder.buttonViewOption);
            popup.inflate(R.menu.notification_menu);
            if (notification.viewed) {
                popup.getMenu().findItem(R.id.notification_menu_mark_read).setVisible(false);
            }
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.notification_menu_mark_read:
                        getDocumentID(notification, documentID -> {
                            db.collection(NOTIFICATIONS_COLLECTION)
                                    .document(documentID)
                                    .update(NOTIFICATIONS_VIEWED, true)
                                    .addOnCompleteListener(task1 -> {
                                        Log.d("NOTIFICATIONS_VIEWED", "DocumentSnapshot successfully updated!");
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.w("NOTIFICATIONS_VIEWED", "Error updating document", e);
                                    });
                        });
                        break;
                    case R.id.notification_menu_remove:
                        getDocumentID(notification, documentID -> {
                            db.collection(NOTIFICATIONS_COLLECTION).document(documentID)
                                    .delete()
                                    .addOnSuccessListener(aVoid -> Toast.makeText(context, "Notificación eliminada correctamente", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
                        });
                        break;
                }
                return false;
            });
            popup.show();

        });
    }

    private void getDocumentID(Notification notification, FirestoreCallback firestoreCallback) {
        db.collection(NOTIFICATIONS_COLLECTION)
                .whereEqualTo(NOTIFICATIONS_DATE_SEND, notification.getDateSend())
                .get()
                .addOnCompleteListener((task) -> {
                    if (task.isSuccessful()) {
                        String documentID = task.getResult().getDocuments().get(0).getId();
                        firestoreCallback.onCallback(documentID);
                    } else {
                        Toast.makeText(context, String.valueOf(task.getException()), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView folio;
        public ImageView image;
        public TextView title;
        public TextView description;
        public TextView date;
        public TextView buttonViewOption;
        public ImageView viewedImage;
        public ConstraintLayout viewedLayout;
        OnNotificationClickListener onNotificationClickListener;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            folio = itemView.findViewById(R.id.notification_row_folio_text_view);
            image = itemView.findViewById(R.id.notification_row_image);
            title = itemView.findViewById(R.id.notification_row_title_text_view);
            description = itemView.findViewById(R.id.notification_row_description_text_view);
            date = itemView.findViewById(R.id.notification_row_date_text_view);
            buttonViewOption = itemView.findViewById(R.id.notification_row_options_text_view);
            viewedImage = itemView.findViewById(R.id.notification_row_viewed_image);
            viewedLayout = itemView.findViewById(R.id.notification_row_viewed_layout);
            this.onNotificationClickListener = notificationClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Notification currNotification = notificationList.get(getAdapterPosition());
            onNotificationClickListener.onNotificationClicked(currNotification);
        }
    }
}
