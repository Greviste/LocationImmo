package com.example.locationimmo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.SearchViewHolder> {
    private ArrayList<RentalAd> rental_ads_list;

    public RecyclerAdapter(ArrayList<RentalAd> list){
        rental_ads_list = list;
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder{
        ImageView image_view;
        TextView tv_city, tv_title, tv_price;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            image_view = itemView.findViewById(R.id.imageView);
            tv_city = itemView.findViewById(R.id.textViewCity);
            tv_title = itemView.findViewById(R.id.textViewTitle);
            tv_price = itemView.findViewById(R.id.textViewPrice);
        }
    }
    @NonNull
    @Override
    public RecyclerAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_rental_item, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.SearchViewHolder holder, int position) {
        String city = rental_ads_list.get(position).address;
        String title = rental_ads_list.get(position).title;
        String price = rental_ads_list.get(position).price.toString();

        holder.tv_city.setText(city);
        holder.tv_title.setText(title);
        holder.tv_price.setText(price);
    }

    @Override
    public int getItemCount() {
        return rental_ads_list.size();
    }
}
