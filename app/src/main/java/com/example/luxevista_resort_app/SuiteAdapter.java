package com.example.luxevista_resort_app;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SuiteAdapter extends RecyclerView.Adapter<SuiteAdapter.SuiteViewHolder>{
    public interface OnSuiteClickListener {
        void onSuiteClick(Suite suite);
    }

    private final List<Suite> suiteList;
    private final OnSuiteClickListener listener;

    public SuiteAdapter(List<Suite> suiteList, OnSuiteClickListener listener) {
        this.suiteList = suiteList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SuiteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_suite, parent, false);
        return new SuiteViewHolder(view, suiteList, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull SuiteViewHolder holder, int position) {
        holder.bind(suiteList.get(position));
    }

    @Override
    public int getItemCount() {
        return suiteList.size();
    }

    static class SuiteViewHolder extends RecyclerView.ViewHolder {
        ImageView suiteImageView;

        public SuiteViewHolder(@NonNull View itemView, List<Suite> suiteList, OnSuiteClickListener listener) {
            super(itemView);
            suiteImageView = itemView.findViewById(R.id.suiteImageView);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onSuiteClick(suiteList.get(position));
                }
            });
        }

        void bind(Suite suite) {
            Bitmap bitmap = decodeSampledBitmapFromResource(
                    itemView.getResources(),
                    suite.getImageResId(),
                    1080,
                    1920
            );
            suiteImageView.setImageBitmap(bitmap);
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public List<Suite> getSuiteList() {
        return suiteList;
    }
    public void updateSuites(List<Suite> newSuiteList) {
        this.suiteList.clear();
        this.suiteList.addAll(newSuiteList);
        notifyDataSetChanged();
    }

}
