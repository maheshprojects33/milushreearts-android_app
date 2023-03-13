package com.java.milushreearts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class EnlargeItemActivity extends AppCompatActivity {

    private TextView fullImageName;
    private ImageView fullImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlarge_item);

        fullImageName = findViewById(R.id.fullImageName);
        fullImage = findViewById(R.id.fullImage);

        String extraName = getIntent().getStringExtra("Extra_Name");
        String extraImage = getIntent().getStringExtra("Extra_Image");

        fullImageName.setText(extraName);
        Glide.with(fullImageName).load(extraImage).into(fullImage);


    }
}