package com.approdevelopers.ranchites.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.approdevelopers.ranchites.Interfaces.RecentSearchItemClickInterface;
import com.approdevelopers.ranchites.Interfaces.RecentSearchItemOnLongClickInterface;
import com.approdevelopers.ranchites.R;

import java.util.List;


public class RecentSearchesKeywordAdapter extends RecyclerView.Adapter<RecentSearchesKeywordAdapter.RecentSearchesKeywordViewHolder> {

    RecentSearchItemClickInterface recentSearchItemClickInterface;
    RecentSearchItemOnLongClickInterface recentSearchItemOnLongClickInterface;
    List<String> searchKeywords;

    public RecentSearchesKeywordAdapter(List<String> searchKeywords, RecentSearchItemClickInterface recentSearchItemClickInterface,RecentSearchItemOnLongClickInterface recentSearchItemOnLongClickInterface) {
        this.searchKeywords = searchKeywords;
        this.recentSearchItemClickInterface = recentSearchItemClickInterface;
        this.recentSearchItemOnLongClickInterface = recentSearchItemOnLongClickInterface;
    }

    @NonNull
    @Override
    public RecentSearchesKeywordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_searches_item, parent, false);
        return new RecentSearchesKeywordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentSearchesKeywordViewHolder holder, int position) {
        holder.txtRecentSearchKey.setText(searchKeywords.get(position));
    }

    @Override
    public int getItemCount() {
        return searchKeywords.size();
    }

    class RecentSearchesKeywordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView txtRecentSearchKey;

        RecentSearchesKeywordViewHolder(View itemView) {
            super(itemView);
            txtRecentSearchKey = itemView.findViewById(R.id.txt_recent_searches_key);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String clickedKey = searchKeywords.get(getAdapterPosition());
            if (recentSearchItemClickInterface!=null){
                recentSearchItemClickInterface.onRecentSearchClick(clickedKey);
            }

        }

        @Override
        public boolean onLongClick(View view) {
            String clickedKey = searchKeywords.get(getAdapterPosition());
            if (recentSearchItemOnLongClickInterface!=null){
                recentSearchItemOnLongClickInterface.onRecentSearchLongClick(clickedKey);
            }
            return false;
        }
    }
}
