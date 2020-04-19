package edu.upenn.cis350.cis350project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

public class FlashlightActivity extends AppCompatActivity {

    private float alpha;
    private boolean isFlashOn;
    private View flashlight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashlight);
        flashlight = findViewById(R.id.flashlight);
        alpha = 1f;
        isFlashOn = true;
        updateFlashlight();
    }

    public void onFlashlightOnOff(View view) {
        isFlashOn = !isFlashOn;
        updateFlashlight();
    }

    public void onSOS(View view) {
    }

    public void onThreeFlashes(View view) {

    }

    public void onIncreaseBrightness(View view) {
        if (isFlashOn) {
            alpha = Float.min(alpha + .1f, 1f);
        }
        updateFlashlight();
    }

    public void onDecreaseBrightness(View view) {
        if (isFlashOn) {
            alpha = Float.max(alpha - .1f, 0f);
            Log.v("dec", alpha + "");
        }
        updateFlashlight();
    }

    public void updateFlashlight() {
        if (isFlashOn) {
            flashlight.setAlpha(alpha);
        } else {
            flashlight.setAlpha(0f);
        }
    }

    public void flash() {
        Animation animation = new AlphaAnimation(1, 0); // Change alpha
        // from fully
        // visible to
        // invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter
        // animation
        // rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation
        // infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at

        // the
        // end so the layout will
        // fade back in
        ((LinearLayout) findViewById(R.id.container)).startAnimation(animation);
    }
}

