package com.company.flickrgallery.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.company.flickrgallery.PhotoDisplay;
import com.company.flickrgallery.R;
import com.company.flickrgallery.models.Photo;
import com.company.flickrgallery.utils.MyUtils;
import com.squareup.picasso.Picasso;
import java.io.Serializable;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder>{

    private Context context;
    private List<Photo> photos;

    public PhotoAdapter(Context context, List<Photo> photos) {
        this.context = context;
        this.photos = photos;
    }

    class PhotoHolder extends RecyclerView.ViewHolder{
        AppCompatImageView photo;
        AppCompatTextView photo_title;

        PhotoHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.img_photo);
            photo_title = itemView.findViewById(R.id.txt_photo_title);
        }
    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhotoHolder(LayoutInflater.from(context).inflate(R.layout.photo_grid,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, final int position) {
        final Photo photo = photos.get(position);
        Picasso.get().load(photo.getImage_url()).into(holder.photo);
        holder.photo_title.setText(photo.getPhoto_title());
        Log.i("Photo URL: ", photo.getImage_url());
        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(context, PhotoDisplay.class);
                mIntent.putExtra(MyUtils.photos,photo);
                mIntent.putExtra(MyUtils.photoID,position);
                mIntent.putExtra(MyUtils.photosList, (Serializable) photos);
                context.startActivity(mIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }
}
