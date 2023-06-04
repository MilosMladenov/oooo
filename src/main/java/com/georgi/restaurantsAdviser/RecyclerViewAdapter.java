package com.georgi.restaurantsAdviser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.georgi.restaurantsAdviser.Model.Restaurants;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private final List<Restaurants> restaurantsList;

    public RecyclerViewAdapter(Context context, List<Restaurants> restaurantsList) {
        this.context = context;
        this.restaurantsList = restaurantsList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_list_row, parent,
                false);

        return new ViewHolder(view, context);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewAdapter.ViewHolder holder, int position) {

        Restaurants restaurants = restaurantsList.get(position);

        holder.restaurantName.setText(restaurants.getmName());
        holder.restaurantAddress.setText(String.format("Address: %s", restaurants.getmAddress()));
        holder.restaurantRating.setText(restaurants.getmRating() + "/5");

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.cardView.getHeight() != ViewGroup.LayoutParams.WRAP_CONTENT) {
                    holder.cardView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                } else {
                    holder.cardView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurantsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView restaurantName;
        public TextView restaurantAddress;
        public TextView restaurantRating;
        public ImageView star;
        public CardView cardView;
        public RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            restaurantName = itemView.findViewById(R.id.restaurantName);
            restaurantAddress = itemView.findViewById(R.id.restaurantAddress);
            restaurantRating = itemView.findViewById(R.id.restaurantRating);
            star = itemView.findViewById(R.id.imageView);
            cardView = itemView.findViewById(R.id.cardView);
            relativeLayout = itemView.findViewById(R.id.rel_lay);
        }
    }
}
