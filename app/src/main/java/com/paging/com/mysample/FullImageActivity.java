package com.paging.com.mysample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


public class FullImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        Intent intent = getIntent();
        String url = intent.getStringExtra("imageUrl");
        if (!url.isEmpty()) {
            ImageView imageView = findViewById(R.id.image);
            Glide.with(imageView)
                    .load(url)
                    .placeholder(R.drawable.shimmer_background)
                    .thumbnail(0.1f)
                    .into(imageView);
        }
    }
}
