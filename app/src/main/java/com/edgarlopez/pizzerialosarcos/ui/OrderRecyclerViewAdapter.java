package com.edgarlopez.pizzerialosarcos.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.adapter.OnOrderClickListener;
import com.edgarlopez.pizzerialosarcos.model.Order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Order> orderList;
    private final OnOrderClickListener orderClickListener;

    public OrderRecyclerViewAdapter(Context context, List<Order> orderList, OnOrderClickListener orderClickListener) {
        this.context = context;
        this.orderList = orderList;
        this.orderClickListener = orderClickListener;
    }

    @NonNull
    @Override
    public OrderRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.order_process_row, viewGroup, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        Order order = orderList.get(position);

        viewHolder.folio.setText(order.getFolio());

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");

        viewHolder.dateRequest.setText(dateFormat.format(order.getDateRequest() * 1000));

        if (order.getDateProcessed() != 0.0) {
            viewHolder.dateProcessed.setText(dateFormat.format(order.getDateProcessed() * 1000));
        } else {
            viewHolder.dateProcessed.setText("");
        }

        if (order.getDateFinished() != 0.0) {
            viewHolder.dateFinished.setText(dateFormat.format(order.getDateFinished() * 1000));
        } else {
            viewHolder.dateFinished.setText("");
        }

        if (order.getDateDelivered() != 0.0) {
            viewHolder.dateDelivery.setText(dateFormat.format(order.getDateDelivered() * 1000));
        } else {
            viewHolder.dateDelivery.setText("");
        }

        viewHolder.process.setProgress(order.getStatus(order.getStatus()));

        viewHolder.dateEstimatedDelivery.setText(dateFormat.format(order.getDateEstimatedDelivery() * 1000));

        viewHolder.price.setText("$" + String.format("%.2f", order.getTotalPrice()));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView folio;
        public TextView price;
        public TextView dateRequest;
        public TextView dateProcessed;
        public TextView dateFinished;
        public TextView dateDelivery;
        public ProgressBar process;
        public TextView dateEstimatedDelivery;
        OnOrderClickListener onOrderClickListener;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            folio = itemView.findViewById(R.id.orders_in_process_row_folio_text_view);
            price = itemView.findViewById(R.id.orders_in_process_row_price_text_view);
            dateRequest = itemView.findViewById(R.id.orders_in_process_row_date_request);
            dateProcessed = itemView.findViewById(R.id.orders_in_process_row_date_processed);
            dateFinished = itemView.findViewById(R.id.orders_in_process_row_date_finished);
            dateDelivery = itemView.findViewById(R.id.orders_in_process_row_date_delivered);
            process = itemView.findViewById(R.id.orders_in_process_row_progress_bar);
            dateEstimatedDelivery = itemView.findViewById(R.id.orders_in_process_row_date_estimated_delivery);
            this.onOrderClickListener = orderClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Order currOrder = orderList.get(getAdapterPosition());
            onOrderClickListener.onOrderClicked(currOrder);
        }
    }
}
