package com.ray.project.commons;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.photo.select.ImageLoader;
import com.ray.project.R;

public class GlideLoader implements ImageLoader {

	private static final long serialVersionUID = 1L;

	@Override
    public void displayImage(Context context, String path, ImageView imageView) {
        RequestOptions options = new RequestOptions();
        options.centerCrop()
                .placeholder(R.drawable.global_img_default)
                .centerCrop();
        Glide.with(context)
                .load(path)
                .apply(options)
                .into(imageView);
    }

}
