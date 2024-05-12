package com.example.onlineszinhazjegyvasarlas;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineszinhazjegyvasarlas.Model.PlayItem;

import java.util.ArrayList;

public class PlayItemAdapter extends RecyclerView.Adapter<PlayItemAdapter.ViewHolder> implements Filterable {
    private ArrayList<PlayItem> playItemsData;
    private ArrayList<PlayItem> playItemsDataAll;
    private Context context;
    private int lastPosition = -1;

    PlayItemAdapter(Context context, ArrayList<PlayItem> itemsData) {
        this.playItemsData = itemsData;
        this.playItemsDataAll = itemsData;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlayItemAdapter.ViewHolder holder, int position) {
        PlayItem currentPlay = playItemsData.get(position);
        holder.bindTo(currentPlay);
        if(holder.getAdapterPosition()>lastPosition){
            Animation animation= AnimationUtils.loadAnimation(context,R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition=holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return playItemsData.size();
    }

    @Override
    public Filter getFilter() {
        return playFilter;
    }
    private Filter playFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<PlayItem> filteredList=new ArrayList<>();
            FilterResults results=new FilterResults();

            if(charSequence==null||charSequence.length()==0){
                results.count = playItemsDataAll.size();
                results.values = playItemsDataAll;
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(PlayItem item : playItemsDataAll) {
                    if(item.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            playItemsData = (ArrayList)filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleText;
        private TextView infoText;
        private TextView priceText;
        private ImageView itemImage;
        private RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleText = itemView.findViewById(R.id.itemTitle);
            infoText = itemView.findViewById(R.id.subTitle);
            priceText = itemView.findViewById(R.id.price);
            itemImage = itemView.findViewById(R.id.itemImage);
            ratingBar = itemView.findViewById(R.id.ratingBar);

            itemView.findViewById(R.id.add_to_view).setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Log.d("Activity", "Add to cart button clicked!");
                }
            });
        }

        public void bindTo(PlayItem currentPlay) {
            titleText.setText(currentPlay.getName());
            infoText.setText(currentPlay.getInfo());
            priceText.setText(currentPlay.getPrice());
            ratingBar.setRating(currentPlay.getRatedInfo());

            Glide.with(context).load(currentPlay.getImageResource()).into(itemImage);

        }
    }
}


