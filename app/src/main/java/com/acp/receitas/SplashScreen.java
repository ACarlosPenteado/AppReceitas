package com.acp.receitas;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterViewFlipper;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.acp.receitas.Login.Login;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.acp.receitas.Logic.FlipperAdapterI;

public class SplashScreen extends AppCompatActivity {

    private AdapterViewFlipper adapterViewFlipper;
    private static final int[] novaImg = { R.drawable.doces, R.drawable.salgadas };
    TextView txt1R, txt1E, txt1C, txt1e, txt1i, txt1t, txt1a, txt1s;
    TextView txt2d, txt2a, txt2s, txt2a2, txt2n, txt2d2, txt2r, txt2a3, text_view_progress;
    RelativeLayout relativeLayout;
    private ProgressBar progressBar;
    Animation animation_in, animation_out, alpha, scale, slide_up, slide_down;
    Animation fade_in, fade_out;
    private static int SPLASH_TIME = 2000;
    private int progr = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 50;

        adapterViewFlipper = findViewById(R.id.viewFlipperS);
        FlipperAdapterI flipperAdapter = new FlipperAdapterI(this, novaImg/*, novoTxt*/ );

        adapterViewFlipper.setAdapter(flipperAdapter);
        adapterViewFlipper.setAutoStart(true);
        adapterViewFlipper.setFlipInterval(2000);

        txt1R = findViewById(R.id.txt1R);
        txt1E = findViewById(R.id.txt1E);
        txt1C = findViewById(R.id.txt1C);
        txt1e = findViewById(R.id.txt1e);
        txt1i = findViewById(R.id.txt1i);
        txt1t = findViewById(R.id.txt1t);
        txt1a = findViewById(R.id.txt1a);
        txt1s = findViewById(R.id.txt1s);
        txt2d = findViewById(R.id.txt2d);
        txt2a = findViewById(R.id.txt2a);
        txt2s = findViewById(R.id.txt2s);
        txt2a2 = findViewById(R.id.txt2a2);
        txt2n = findViewById(R.id.txt2n);
        txt2d2 = findViewById(R.id.txt2d2);
        txt2r = findViewById(R.id.txt2r);
        txt2a3 = findViewById(R.id.txt2a3);

        relativeLayout = findViewById(R.id.relativeLayoutS);
        progressBar = findViewById(R.id.progress_bar);
        text_view_progress = findViewById(R.id.text_view_progress);

        ShimmerFrameLayout container1 = findViewById(R.id.shimmer_view_container1);
        ShimmerFrameLayout container2 = findViewById(R.id.shimmer_view_container2);
        ShimmerFrameLayout container3 = findViewById(R.id.shimmer_view_container3);
        ShimmerFrameLayout container4 = findViewById(R.id.shimmer_view_container4);
        ShimmerFrameLayout container5 = findViewById(R.id.shimmer_view_container5);
        ShimmerFrameLayout container6 = findViewById(R.id.shimmer_view_container6);
        ShimmerFrameLayout container7 = findViewById(R.id.shimmer_view_container7);
        ShimmerFrameLayout container8 = findViewById(R.id.shimmer_view_container8);
        ShimmerFrameLayout container9 = findViewById(R.id.shimmer_view_container9);
        ShimmerFrameLayout container10 = findViewById(R.id.shimmer_view_container10);
        ShimmerFrameLayout container11 = findViewById(R.id.shimmer_view_container11);
        ShimmerFrameLayout container12 = findViewById(R.id.shimmer_view_container12);
        ShimmerFrameLayout container13 = findViewById(R.id.shimmer_view_container13);
        ShimmerFrameLayout container14 = findViewById(R.id.shimmer_view_container14);
        ShimmerFrameLayout container15 = findViewById(R.id.shimmer_view_container15);
        ShimmerFrameLayout container16 = findViewById(R.id.shimmer_view_container16);

        container1.startShimmer();
        container2.startShimmer();
        container3.startShimmer();
        container4.startShimmer();
        container5.startShimmer();
        container6.startShimmer();
        container7.startShimmer();
        container8.startShimmer();
        container9.startShimmer();
        container10.startShimmer();
        container11.startShimmer();
        container12.startShimmer();
        container13.startShimmer();
        container14.startShimmer();
        container15.startShimmer();
        container16.startShimmer();

        animation_in = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        animation_out = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
        alpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        scale = AnimationUtils.loadAnimation(this, R.anim.scale);
        slide_up = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        slide_down = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        fade_in = AnimationUtils.loadAnimation(this, R.anim.fadein);
        fade_out = AnimationUtils.loadAnimation(this, R.anim.fadeout);

        //txtAnimation();
        txt1R.startAnimation(alpha);
        txt1E.startAnimation(alpha);
        txt1C.startAnimation(alpha);
        txt1e.startAnimation(alpha);
        txt1i.startAnimation(alpha);
        txt1t.startAnimation(alpha);
        txt1a.startAnimation(alpha);
        txt1s.startAnimation(alpha);
        txt2d.startAnimation(alpha);
        txt2a.startAnimation(alpha);
        txt2s.startAnimation(alpha);
        txt2a2.startAnimation(alpha);
        txt2n.startAnimation(alpha);
        txt2d2.startAnimation(alpha);
        txt2r.startAnimation(alpha);
        txt2a3.startAnimation(alpha);


        getSupportActionBar().hide();

        final Handler handler = new Handler();
        handler.postDelayed( new Runnable()
        {
            @Override
            public void run() {
                relativeLayout.setVisibility(View.GONE);
                relativeLayout.setAnimation(fade_in);
                if(progr <= 100){
                    text_view_progress.setText("" + progr + "%");
                    progressBar.setProgress(progr);
                    progr = progr + 20;
                    handler.postDelayed(this, 100);
                } else {
                    handler.removeCallbacks(this);
                    Intent i = new Intent(SplashScreen.this, Login.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    finish();
                }

            }
        }, SPLASH_TIME);

    }

    private void txtAnimation() {
        Animation txt = new AlphaAnimation(0.00f, 1.00f);
        txt.setDuration(SPLASH_TIME);
        txt.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                txt1R.setVisibility(View.INVISIBLE);
                txt1E.setVisibility(View.INVISIBLE);
                txt1C.setVisibility(View.INVISIBLE);
                txt1e.setVisibility(View.INVISIBLE);
                txt1i.setVisibility(View.INVISIBLE);
                txt1t.setVisibility(View.INVISIBLE);
                txt1a.setVisibility(View.INVISIBLE);
                txt1s.setVisibility(View.INVISIBLE);
                txt2d.setVisibility(View.INVISIBLE);
                txt2a.setVisibility(View.INVISIBLE);
                txt2s.setVisibility(View.INVISIBLE);
                txt2a2.setVisibility(View.INVISIBLE);
                txt2n.setVisibility(View.INVISIBLE);
                txt2d2.setVisibility(View.INVISIBLE);
                txt2r.setVisibility(View.INVISIBLE);
                txt2a3.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                txt1R.setVisibility(View.VISIBLE);
                txt1E.setVisibility(View.VISIBLE);
                txt1C.setVisibility(View.VISIBLE);
                txt1e.setVisibility(View.VISIBLE);
                txt1i.setVisibility(View.VISIBLE);
                txt1t.setVisibility(View.VISIBLE);
                txt1a.setVisibility(View.VISIBLE);
                txt1s.setVisibility(View.VISIBLE);
                txt2d.setVisibility(View.VISIBLE);
                txt2a.setVisibility(View.VISIBLE);
                txt2s.setVisibility(View.VISIBLE);
                txt2a2.setVisibility(View.VISIBLE);
                txt2n.setVisibility(View.VISIBLE);
                txt2d2.setVisibility(View.VISIBLE);
                txt2r.setVisibility(View.VISIBLE);
                txt2a3.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                txt1R.startAnimation(txt);
                txt1E.startAnimation(txt);
                txt1C.startAnimation(txt);
                txt1e.startAnimation(txt);
                txt1i.startAnimation(txt);
                txt1t.startAnimation(txt);
                txt1a.startAnimation(txt);
                txt1s.startAnimation(txt);
                txt2d.startAnimation(txt);
                txt2a.startAnimation(txt);
                txt2s.startAnimation(txt);
                txt2a2.startAnimation(txt);
                txt2n.startAnimation(txt);
                txt2d2.startAnimation(txt);
                txt2r.startAnimation(txt);
                txt2a3.startAnimation(txt);
            }
        });


    }

}