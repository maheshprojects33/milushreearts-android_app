package com.java.milushreearts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.makeramen.roundedimageview.RoundedImageView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class ItemAdapter extends FirebaseRecyclerAdapter<ItemHolder, ItemAdapter.Item_viewHolder> {



    public ItemAdapter(@NonNull FirebaseRecyclerOptions<ItemHolder> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final Item_viewHolder holder, final int position, @NonNull final ItemHolder model) {
        holder.name.setText(model.getName());
        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDescription());

        Glide.with(holder.thumbnail.getContext()).load(model.getImage()).into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.thumbnail.getContext(), EnlargeItemActivity.class);
                intent.putExtra("Extra_Name", model.getTitle());
                intent.putExtra("Extra_Image", model.getImage());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.thumbnail.getContext().startActivity(intent);
            }
        });

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.name.getContext())
                        .setContentHolder(new ViewHolder(R.layout.dialog_box))
                        .setExpanded(true)
                        .setContentBackgroundResource(R.color.black)
                        .create();

                View myView = dialogPlus.getHolderView();
                RoundedImageView rImage = myView.findViewById(R.id.etThumbnail);
                final EditText sName = myView.findViewById(R.id.etStatueName);
                final EditText sTitle = myView.findViewById(R.id.etTitle);
                final EditText sDesc = myView.findViewById(R.id.etDesc);
                Button sUpload = myView.findViewById(R.id.etUpload);

                sName.setText(model.getName());
                sTitle.setText(model.getTitle());
                sDesc.setText(model.getDescription());
                Glide.with(rImage.getContext()).load(model.getImage()).into(rImage);

                dialogPlus.show();

                sUpload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String , Object> map = new HashMap<>();
                        map.put("name", sName.getText().toString());
                        map.put("title", sTitle.getText().toString());
                        map.put("description", sDesc.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("dataStorage")
                                .child(getRef(position).getKey())
                                .updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(holder.name.getContext(), "Successfully Edited", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.name.getContext(), "ERROR Loading: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }
        });
        

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.name.getContext());
                builder.setTitle("Delete Warning")
                        .setMessage("Are You Sure To Delete This Content")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference().child("dataStorage")
                                        .child(getRef(position).getKey()).removeValue();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
    }

    @NonNull
    @Override
    public Item_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item, parent, false);
        return new Item_viewHolder(view);
    }

    public class Item_viewHolder extends RecyclerView.ViewHolder {

        private TextView name, title, description;
        private RoundedImageView thumbnail;
        private ToggleButton ivLike;
        private ImageView ivShare, ivEdit, ivDelete;

        public Item_viewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.etStatueName);
            title = itemView.findViewById(R.id.etTitle);
            description = itemView.findViewById(R.id.etDesc);
            thumbnail = itemView.findViewById(R.id.etThumbnail);
            ivLike = itemView.findViewById(R.id.ivLike);
            ivShare = itemView.findViewById(R.id.ivShare);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivDelete = itemView.findViewById(R.id.ivDelete);

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
