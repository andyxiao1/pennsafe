package edu.upenn.cis350.cis350project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

public class FlashlightActivity extends AppCompatActivity {

    private float alpha;
    private boolean isFlashOn;
    private View flashlight;
    private static int SHORT_FLASH = 500;
    private static int LONG_FLASH = 1000;

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
        Animation flash1 = getFlashAnimation(FlashlightActivity.SHORT_FLASH);
        final Animation flash2 = getFlashAnimation(FlashlightActivity.LONG_FLASH);
        final Animation flash3 = getFlashAnimation(FlashlightActivity.SHORT_FLASH);

        flash1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                flashlight.startAnimation(flash2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        flash2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                flashlight.startAnimation(flash3);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        flashlight.startAnimation(flash1);
    }

    public void onThreeFlashes(View view) {
        Animation flash = getFlashAnimation(FlashlightActivity.SHORT_FLASH);
        flashlight.startAnimation(flash);
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

    public Animation getFlashAnimation(int duration) {
        Animation animation = new AlphaAnimation(0, 1);
        animation.setDuration(duration);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(2);
        return animation;
    }
}

