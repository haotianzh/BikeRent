package com.bike.haotian.bikerent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by HAOTIAN on 2016/8/8.
 */
public class ShowImageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showimage);
        Intent intent = getIntent();
        String uri = intent.getStringExtra("image");
        Picasso.with(this).load(uri).into((ImageView) findViewById(R.id.show_imageview));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return super.onTouchEvent(event);

    }
}
