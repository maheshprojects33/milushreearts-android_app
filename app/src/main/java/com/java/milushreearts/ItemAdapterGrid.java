package com.java.milushreearts;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.makeramen.roundedimageview.RoundedImageView;

public class ItemAdapterGrid extends FirebaseRecyclerAdapter<ItemHolderGrid, ItemAdapterGrid.ItemGridViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ItemAdapterGrid(@NonNull FirebaseRecyclerOptions<ItemHolderGrid> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ItemGridViewHolder holder, int position, @NonNull final ItemHolderGrid model) {
        holder.nameG.setText(model.getName());

        Glide.with(holder.imageG.getContext()).load(model.image).into(holder.imageG);

        holder.imageG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.imageG.getContext(), EnlargeItemActivity.class);
                intent.putExtra("Extra_Name", model.getName());
                intent.putExtra("Extra_Image", model.getImage());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.imageG.getContext().startActivity(intent);



            }
        });

    }

    @NonNull
    @Override
    public ItemGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_grid, parent, false);
        return new ItemGridViewHolder(view);
    }

    public class ItemGridViewHolder extends RecyclerView.ViewHolder {

        private TextView nameG;
        private RoundedImageView imageG;
        private ToggleButton ivLike;
        private ImageView ivShare;

        public ItemGridViewHolder(@NonNull View itemView) {
            super(itemView);

            nameG = itemView.findViewById(R.id.tvStatueNameG);
            imageG = itemView.findViewById(R.id.ivThumbnailG);
            ivLike = itemView.findViewById(R.id.ivLikeG);
            ivShare = itemView.findViewById(R.id.ivShareG);


            ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(ivLike.isChecked()){
                        Toast.makeText(ivLike.getContext(), "Liked", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(ivLike.getContext(), "UnLiked", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            ivShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    String shareBody = "Your Body goes here";
                    String shareSubject = "Your Subject goes here";

                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);

                    ivShare.getContext().startActivity(Intent.createChooser(shareIntent, "Share Using"));

                }
            });

        }


    }
}
