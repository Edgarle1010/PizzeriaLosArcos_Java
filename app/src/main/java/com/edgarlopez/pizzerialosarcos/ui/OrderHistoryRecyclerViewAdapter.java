package com.edgarlopez.pizzerialosarcos.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.adapter.OnOrderClickListener;
import com.edgarlopez.pizzerialosarcos.model.Order;

import java.text.SimpleDateFormat;
import java.util.List;

public class OrderHistoryRecyclerViewAdapter extends RecyclerView.Adapter<OrderHistoryRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Order> orderList;
    private final OnOrderClickListener orderClickListener;

    public OrderHistoryRecyclerViewAdapter(Context context, List<Order> orderList, OnOrderClickListener orderClickListener) {
        this.context = context;
        this.orderList = orderList;
        this.orderClickListener = orderClickListener;
    }

    @NonNull
    @Override
    public OrderHistoryRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.order_row, viewGroup, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        Order order = orderList.get(position);

        viewHolder.folio.setText(order.getFolio());
        viewHolder.price.setText("$" + String.format("%.2f", order.getTotalPrice()));
        viewHolder.status.setText(order.getStatus());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");

        viewHolder.dateRequest.setText(dateFormat.format(order.getDateRequest() * 1000));

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView folio;
        public TextView price;
        public TextView status;
        public TextView dateRequest;
        OnOrderClickListener onOrderClickListener;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            folio = itemView.findViewById(R.id.orders_history_row_folio_text_view);
            price = itemView.findViewById(R.id.orders_history_row_price_text_view);
            status = itemView.findViewById(R.id.orders_history_row_status);
            dateRequest = itemView.findViewById(R.id.orders_history_row_date_request);
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
