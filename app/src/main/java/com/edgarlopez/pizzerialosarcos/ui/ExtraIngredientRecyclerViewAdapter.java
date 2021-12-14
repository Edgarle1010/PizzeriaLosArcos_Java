package com.edgarlopez.pizzerialosarcos.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.adapter.OnExtraIngredientClickListener;
import com.edgarlopez.pizzerialosarcos.adapter.OnFoodClickListener;
import com.edgarlopez.pizzerialosarcos.model.ExtraIngredient;
import com.edgarlopez.pizzerialosarcos.model.Food;

import java.util.List;

public class ExtraIngredientRecyclerViewAdapter extends RecyclerView.Adapter<ExtraIngredientRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<ExtraIngredient> extraIngredientList;
    private String foodSize;
    private final OnExtraIngredientClickListener extraIngredientClickListener;

    public ExtraIngredientRecyclerViewAdapter(Context context, List<ExtraIngredient> extraIngredientList, String foodSize, OnExtraIngredientClickListener extraIngredientClickListener) {
        this.context = context;
        this.extraIngredientList = extraIngredientList;
        this.foodSize = foodSize;
        this.extraIngredientClickListener = extraIngredientClickListener;
    }

    @NonNull
    @Override
    public ExtraIngredientRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.extra_ingredient_row, viewGroup, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ExtraIngredientRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        ExtraIngredient extraIngredient = extraIngredientList.get(position);

        if (extraIngredient.getsPrice() == 0) {
            foodSize = "big";
        }

        int price;
        if (foodSize.equals("big")) {
            price = extraIngredient.getbPrice();
        }else if (foodSize.equals("medium")) {
            price = extraIngredient.getmPrice();
        }else {
            price = extraIngredient.getsPrice();
        }
        viewHolder.title.setText(String.format("%s $%d", extraIngredient.getTitle(), price));
    }

    @Override
    public int getItemCount() {
        return extraIngredientList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout linearLayout;
        public TextView title;
        public ImageView cancelImageView;
        OnExtraIngredientClickListener onExtraIngredientClickListener;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            title = itemView.findViewById(R.id.extra_ingredient_title_list);
            cancelImageView = itemView.findViewById(R.id.extra_ingredient_cancel_image_view);

            cancelImageView.setOnClickListener(this);
            this.onExtraIngredientClickListener = extraIngredientClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ExtraIngredient currExtraIngredient = extraIngredientList.get(getAdapterPosition());
            if (v.equals(cancelImageView)) {
                extraIngredientList.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                notifyItemRangeChanged(getItemCount(), extraIngredientList.size());
                onExtraIngredientClickListener.onExtraIngredientClicked(currExtraIngredient);
            }
        }
    }
}
