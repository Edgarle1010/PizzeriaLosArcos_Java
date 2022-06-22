package com.edgarlopez.pizzerialosarcos.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.adapter.OnFoodClickListener;
import com.edgarlopez.pizzerialosarcos.model.Food;

import java.util.List;

public class FoodRecyclerViewAdapter extends RecyclerView.Adapter<FoodRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Food> foodList;
    private final OnFoodClickListener foodClickListener;

    public FoodRecyclerViewAdapter(Context context, List<Food> foodList, OnFoodClickListener foodClickListener) {
        this.context = context;
        this.foodList = foodList;
        this.foodClickListener = foodClickListener;
    }

    @NonNull
    @Override
    public FoodRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.food_row, viewGroup, false);

        int height = viewGroup.getMeasuredWidth() / 2;
        view.setMinimumHeight(height);
        DrawableCompat.setTint(view.getBackground(), ContextCompat.getColor(context, R.color.third_color));

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        Food food = foodList.get(position);

        viewHolder.title.setText(food.getTitle());

        String description = food.getDescription();
        if (description != null) {
            if (description.isEmpty()) {
                viewHolder.description.setVisibility(View.GONE);
            } else {
                viewHolder.description.setText(description);
            }
        } else {
            viewHolder.description.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout linearLayout;
        public TextView title;
        public TextView description;
        public String id;
        OnFoodClickListener onFoodClickListener;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            title = itemView.findViewById(R.id.food_title_list);
            description = itemView.findViewById(R.id.food_description_list);
            this.onFoodClickListener = foodClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Food currFood = foodList.get(getAdapterPosition());
            onFoodClickListener.onFoodClicked(currFood);
        }
    }
}
