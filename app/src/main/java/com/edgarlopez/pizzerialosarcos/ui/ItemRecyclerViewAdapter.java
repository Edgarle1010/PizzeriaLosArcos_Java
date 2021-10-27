package com.edgarlopez.pizzerialosarcos.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.adapter.OnItemClickListener;
import com.edgarlopez.pizzerialosarcos.model.ExtraIngredient;
import com.edgarlopez.pizzerialosarcos.model.Item;

import java.util.List;
import java.util.Objects;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder> {
    private OnItemClickListener onItemClickListener;
    private List<Item> itemList;
    private Context context;

    public ItemRecyclerViewAdapter(List<Item> itemList, Context context, OnItemClickListener onItemClickListener) {
        this.itemList = itemList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);

        return new ViewHolder(view, onItemClickListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = Objects.requireNonNull(itemList.get(position));

        if (item.size.equals("big")) {
            item.size = "Grande";
        }else if (item.size.equals("medium")) {
            item.size = "Mediana";
        }else {
            item.size = "Chica";
        }

        holder.title.setText(item.getTitle() + " | " + item.getSize());
        for (ExtraIngredient extraIngredients : item.extraIngredientList) {
            holder.extraIngredient.append("- " + extraIngredients.getTitle() + "\n");
        }
        holder.comments.setText(item.getComments());
        holder.amount.setText(String.valueOf(item.getAmount()));
        holder.price.setText(String.valueOf("$" + item.getPrice()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnItemClickListener onItemClickListener;
        public TextView title;
        public TextView extraIngredient;
        public TextView comments;
        public TextView amount;
        public TextView price;

        public ViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title_textView);
            extraIngredient = itemView.findViewById(R.id.item_extra_ingredient_object_textview);
            comments = itemView.findViewById(R.id.item_comments_object_textview);
            amount = itemView.findViewById(R.id.item_amount_object_textview);
            price = itemView.findViewById(R.id.item_price_textview);
            this.onItemClickListener = onItemClickListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onOrderClicked(getAdapterPosition());
        }

    }

}