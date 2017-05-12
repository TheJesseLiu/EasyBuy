package com.example.ee4901.project2;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import static android.R.attr.animation;

public class WelcomeActivity extends AppCompatActivity {
    ImageView logo_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        logo_image = (ImageView) findViewById(R.id.logo);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        RelativeLayout r_layout=(RelativeLayout) findViewById(R.id.welcome);
        r_layout.clearAnimation();
        r_layout.startAnimation(anim);

        Animation logo_ani = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.welcome_anim);
        logo_image.setAnimation(logo_ani);

        logo_ani.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
