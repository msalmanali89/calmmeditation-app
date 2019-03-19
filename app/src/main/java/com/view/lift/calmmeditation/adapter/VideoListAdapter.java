package com.view.lift.calmmeditation.adapter;

import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import com.view.lift.calmmeditation.R;
import com.view.lift.calmmeditation.dto.Item;
import com.view.lift.calmmeditation.interfaces.ItemClick;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoListViewHolder> {



        private ItemClick itemClick;
        private List<Item> itemList;
        private Context mContext;
        RequestOptions requestOptions;
        int orientation;

        public VideoListAdapter(Context mContext , ItemClick itemClick, List<Item> itemList, int orientation) {

            this.orientation = orientation;
            this.itemClick = itemClick;
            this.itemList = itemList;
            this.mContext = mContext;
            requestOptions= new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        }
        public void setDataSet(List<Item> itemList){
            this.itemList = itemList;
        }
        public void setOrientation(int orientation){
            this.orientation = orientation;
        }

        @Override
        public VideoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = null;
            /*if(Configuration.ORIENTATION_LANDSCAPE ==  this.orientation) {
                view = inflater.inflate(R.layout.video_list_item_land, parent, false);
            }else*/{
                view = inflater.inflate(R.layout.video_list_item, parent, false);
            }
            return new VideoListViewHolder(view);

        }

        @Override
        public void onBindViewHolder(VideoListViewHolder holder, int position) {

            Glide.with(mContext)
                    .load(itemList.get(position).getMediaContent().getThumbnail().getUrl())
                    .apply(requestOptions)
                    .into(holder.thumbnailImage);
            holder.titleTextView.setText(itemList.get(position).getTitle());
            holder.durationTextView.setText(timeInMin(itemList.get(position).getMediaContent().getDuration()));
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }



        class VideoListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            // TextView movieName;
            ImageView thumbnailImage;
            TextView titleTextView;
            TextView durationTextView;

            public VideoListViewHolder(View itemView) {
                super(itemView);
                titleTextView = itemView.findViewById(R.id.titleTextView);

                durationTextView = itemView.findViewById(R.id.durationTextView
                );
                thumbnailImage = itemView.findViewById(R.id.thumbnailImage);
                itemView.setOnClickListener(this);
            }


            @Override
            public void onClick(View view) {
                itemClick.onItemClick(getAdapterPosition());
            }
        }



        public String timeInMin(String duration){

            try {
                if (duration != null && duration.length() > 0) {

                    String[] strArr = duration.split("\\.");
                    long seconds = Long.parseLong(strArr[0]);

                    long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
                    long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);



                    duration = String.format("%02d:%02d",minute,second);
                }

                return duration;
            }
            catch (Exception e){

            }

            return duration;
        }


}
