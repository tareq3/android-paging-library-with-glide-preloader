package com.paging.com.mysample.homescreen;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.paging.com.mysample.R;
import com.paging.com.mysample.pojo.Image;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;


public class ItemAdapter extends PagedListAdapter<Image, ItemAdapter.ItemViewHolder> {

    private static DiffUtil.ItemCallback<Image> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Image>() {
                @Override
                public boolean areItemsTheSame(Image oldItem, Image newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(Image oldItem, Image newItem) {
                    return oldItem.equals(newItem);
                }
            };
    private Context mCtx;

    public ItemAdapter(Context mCtx) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.image_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        final Image image = getItem(position);
        holder.userName.setText(image.getUser().toString());
        holder.like.setText(image.getLikes().toString());
        holder.comments.setText(image.getComments().toString());

        Glide.with(mCtx)
                .load(image.getWebformatURL())
                .placeholder(R.drawable.shimmer_background)
                .thumbnail(0.1f)

                .into(holder.image);



        if (!image.getUserImageURL().isEmpty()) {
            Glide.with(mCtx)
                    .load(image.getUserImageURL())
                    .centerCrop()
                    .transform(new CircleCrop())
                    .into(holder.userImage);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView userName, like, comments;
        ImageView image, userImage;

        public ItemViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image);
            userImage = view.findViewById(R.id.user_image);
            userName = view.findViewById(R.id.user);
            like = view.findViewById(R.id.like);
            comments = view.findViewById(R.id.comments);
        }
    }
}
