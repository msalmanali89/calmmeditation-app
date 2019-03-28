package com.view.lift.calmmeditation.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.view.lift.calmmeditation.R;
import com.view.lift.calmmeditation.dto.Item;
import com.view.lift.calmmeditation.interfaces.ItemClick;

import java.util.ArrayList;
import java.util.List;

public class VideoListMainAdapter extends RecyclerView.Adapter<VideoListMainAdapter.VideoListMainViewHolder>{



    private List<Object> nestedItemList;
    private Context mContext;
    RequestOptions requestOptions;
    int orientation;
    ItemClick itemClick;

    public VideoListMainAdapter(Context mContext, ItemClick itemClick, List<Object> nestedItemList, int orientation){

        this.orientation=orientation;
        this.itemClick=itemClick;
        this.nestedItemList=nestedItemList;
        this.mContext=mContext;
        requestOptions=new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    public void setDataSet(List<Object> nestedItemList){
        this.nestedItemList=nestedItemList;
    }

    public void setOrientation(int orientation){
        this.orientation=orientation;
    }

    @Override
    public VideoListMainViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=null;
            /*if(Configuration.ORIENTATION_LANDSCAPE ==  this.orientation) {
                view = inflater.inflate(R.layout.video_list_item_land, parent, false);
            }else*/{
            view=inflater.inflate(R.layout.video_list_main_item,parent,false);
        }
        return new VideoListMainAdapter.VideoListMainViewHolder(view);

    }

    @Override
    public void onBindViewHolder(VideoListMainViewHolder holder,int position){

        try {
            String categoryName = ((Item) ((List<Item>) nestedItemList.get(position)).get(0)).getCategoryList().get(1);
            holder.categoryNameTextView.setText(categoryName);
            holder.categoryNameTextView.setVisibility(View.VISIBLE);
        }catch (Exception e){
            holder.categoryNameTextView.setVisibility(View.GONE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false);
        holder.itemRecyclerView.setLayoutManager(linearLayoutManager);
        VideoListAdapter videoListAdapter = new VideoListAdapter(mContext, this.itemClick, (List<Item>)nestedItemList.get(position), orientation);


        holder.itemRecyclerView.setAdapter(videoListAdapter);
    }

    @Override
    public int getItemCount(){
        return nestedItemList.size();
    }

    class VideoListMainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

         TextView categoryNameTextView;
        RecyclerView itemRecyclerView;


        public VideoListMainViewHolder(View itemView) {
            super(itemView);
            itemRecyclerView     = itemView.findViewById(R.id.itemRecyclerView);
            categoryNameTextView = itemView.findViewById(R.id.categoryNameTextView);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            itemClick.onItemClick(getAdapterPosition());
        }
    }


}