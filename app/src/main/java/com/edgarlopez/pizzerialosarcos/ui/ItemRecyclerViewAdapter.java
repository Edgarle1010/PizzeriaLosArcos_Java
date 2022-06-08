package com.edgarlopez.pizzerialosarcos.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.adapter.OnItemClickListener;
import com.edgarlopez.pizzerialosarcos.model.ExtraIngredient;
import com.edgarlopez.pizzerialosarcos.model.Item;

import java.util.List;
import java.util.Objects;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder> {
    //private OnItemClickListener onItemClickListener;
    private List<Item> itemList;
    private Context context;

    //public ItemRecyclerViewAdapter(List<Item> itemList, Context context, OnItemClickListener onItemClickListener) {
    public ItemRecyclerViewAdapter(List<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
        //this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);

        return new ViewHolder(view);
        //return new ViewHolder(view, onItemClickListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = Objects.requireNonNull(itemList.get(position));

        if (item.size.equals("big")) {
            item.size = "Grande";
        } else if (item.size.equals("medium")) {
            item.size = "Mediana";
        } else if (item.size.equals("small")) {
            if (item.getTitle().contains("Coctel") || item.getTitle().contains("Filete")) {
                item.size = "Chico";
            } else {
                item.size = "Chica";
            }
        }

        if (item.size.isEmpty()) {
            holder.title.setText(item.getTitle());
        } else {
            holder.title.setText(item.getTitle() + " | " + item.getSize());
        }

        if (item.extraIngredientList != null) {
            if (!item.extraIngredientList.isEmpty()) {
                for (ExtraIngredient extraIngredients : item.extraIngredientList) {
                    holder.extraIngredient.append("- " + extraIngredients.getTitle() + "\n");
                }
            } else {
                holder.extraIngredientSubtitle.setVisibility(View.GONE);
                holder.extraIngredient.setVisibility(View.GONE);
            }
        } else {
            holder.extraIngredientSubtitle.setVisibility(View.GONE);
            holder.extraIngredient.setVisibility(View.GONE);
        }

        String comments = item.getComments();
        if (!comments.isEmpty()) {
            holder.comments.setText(comments);
        } else {
            holder.commentsSubtitle.setVisibility(View.GONE);
            holder.comments.setVisibility(View.GONE);
        }

        holder.amount.setText(String.valueOf(item.getAmount()));
        holder.price.setText("$" + String.format("%.02f", item.getPrice()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    //public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public class ViewHolder extends RecyclerView.ViewHolder {
        //OnItemClickListener onItemClickListener;
        public TextView title,
                extraIngredientSubtitle,
                extraIngredient,
                commentsSubtitle,
                comments,
                amount,
                price;
        public ConstraintLayout viewForeground;
        public RelativeLayout viewBackground;

        //public ViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title_textView);
            extraIngredientSubtitle = itemView.findViewById(R.id.item_extra_ingredient_textview);
            extraIngredient = itemView.findViewById(R.id.item_extra_ingredient_object_textview);
            commentsSubtitle = itemView.findViewById(R.id.item_comments_textview);
            comments = itemView.findViewById(R.id.item_comments_object_textview);
            amount = itemView.findViewById(R.id.item_amount_object_textview);
            price = itemView.findViewById(R.id.item_price_textview);
            viewForeground = itemView.findViewById(R.id.view_foreground);
            viewBackground = itemView.findViewById(R.id.view_background);
            //this.onItemClickListener = onItemClickListener;

            //itemView.setOnClickListener(this);

        }

        /*@Override
        public void onClick(View v) {
            onItemClickListener.onOrderClicked(getAdapterPosition());
        }*/

    }

}
