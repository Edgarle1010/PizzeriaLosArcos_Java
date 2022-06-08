package com.edgarlopez.pizzerialosarcos.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
                .inflate(R.layout.order_row, viewGroup, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        Order order = orderList.get(position);

        viewHolder.folio.setText(String.valueOf(order.getFolio()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString  = dateFormat.format(order.getDateRequest());
        viewHolder.date.setText(dateString);

        viewHolder.status.setText(order.getStatus());
        viewHolder.price.setText(String.format("$%s", order.getTotalPrice()));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout linearLayout;
        public TextView folio;
        public TextView date;
        public TextView status;
        public TextView price;
        OnOrderClickListener onOrderClickListener;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            folio = itemView.findViewById(R.id.order_title_textview);
            date = itemView.findViewById(R.id.order_date_object_textview);
            status = itemView.findViewById(R.id.order_status_object_textview);
            price = itemView.findViewById(R.id.order_price_textview);
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
