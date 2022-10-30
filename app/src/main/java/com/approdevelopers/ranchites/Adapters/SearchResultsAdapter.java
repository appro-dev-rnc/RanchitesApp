package com.approdevelopers.ranchites.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.approdevelopers.ranchites.Activities.LocationCompleteDetailsPage;
import com.approdevelopers.ranchites.Activities.UserCompleteDetailPage;
import com.approdevelopers.ranchites.ApplicationFiles.GlideApp;
import com.approdevelopers.ranchites.Models.SearchResultModel;
import com.approdevelopers.ranchites.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchResultsAdapter extends ListAdapter<SearchResultModel,SearchResultsAdapter.SearchResultsViewHolder> {

    public SearchResultsAdapter(@NonNull DiffUtil.ItemCallback<SearchResultModel> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public SearchResultsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_item,parent,false);
        return new SearchResultsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultsViewHolder holder, int position) {
        SearchResultModel model = getItem(position);
        GlideApp.with(holder.itemView.getContext()).load(model.getImageUrl())
                .thumbnail(GlideApp.with(holder.itemView.getContext()).load(model.getImageUrl()).override(30))
                .timeout(20000)
                .error(GlideApp.with(holder.itemView.getContext()).load(model.getImageUrl()).timeout(20000)
                        .thumbnail(GlideApp.with(holder.itemView.getContext()).load(model.getImageUrl()).override(30))
                        .into(holder.imgSearchResultsItem)).into(holder.imgSearchResultsItem);
        holder.txtSearchResultsTitle.setText(model.getTitle());
        holder.txtSearchResultsDesc.setText(model.getDesc());
        holder.txtSearchResultsCategory.setText(model.getCategory());
    }

     class SearchResultsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtSearchResultsTitle;
        TextView txtSearchResultsDesc;
        TextView txtSearchResultsCategory;
        CircleImageView imgSearchResultsItem;
        public SearchResultsViewHolder(View itemView){
            super(itemView);
            txtSearchResultsTitle = itemView.findViewById(R.id.txt_search_result_title);
            txtSearchResultsDesc = itemView.findViewById(R.id.txt_search_result_desc);
            txtSearchResultsCategory = itemView.findViewById(R.id.txt_search_result_category);
            imgSearchResultsItem = itemView.findViewById(R.id.img_search_result_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            SearchResultModel model = getItem(getAdapterPosition());


            if (model.getDocumentType()==1){
                Intent resultIntent = new Intent(view.getContext(), LocationCompleteDetailsPage.class);
                resultIntent.putExtra("documentId",model.getDocumentId());
                view.getContext().startActivity(resultIntent);
            }if (model.getDocumentType()==2){
                Intent resultIntent = new Intent(view.getContext(), UserCompleteDetailPage.class);
                resultIntent.putExtra("userId",model.getDocumentId());
                view.getContext().startActivity(resultIntent);
            }


        }
    }
}
