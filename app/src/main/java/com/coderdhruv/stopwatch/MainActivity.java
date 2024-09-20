package com.coderdhruv.stopwatch;



import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;

import com.coderdhruv.stopwatch.databinding.ActivityMainBinding;





public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Handler handler;
    private long startTime, timeInMillis, holdTime = 0;
    private boolean isRunning = false;
    private boolean isPaused = false;

    private ValueAnimator blinkAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);
        handler = new Handler();
        setupBlinkAnimation();
        // Start/Pause Button
        binding.playPause.setOnClickListener(v -> {
            if (!isRunning) {
                // Start the stopwatch or resume after pause
                startTime = System.currentTimeMillis() - holdTime;
                handler.post(runnable);
                isRunning = true;
                isPaused = false;
                stopBlinking();
                binding.pImage.setImageResource(R.drawable.pause_icon); // Change icon to pause
            } else if (!isPaused) {
                // Pause the stopwatch
                handler.removeCallbacks(runnable);
                holdTime = System.currentTimeMillis() - startTime;  // Save elapsed time
                isPaused = true;
                isRunning = false;
                startBlinking();
                binding.pImage.setImageResource(R.drawable.play_ic); // Change icon to play
            }
        });

        // Reset Button
        binding.reset.setOnClickListener(v -> {
            if (isRunning || isPaused) {
                handler.removeCallbacks(runnable);
                holdTime = 0;
                isRunning = false;
                isPaused = false;
                binding.timer.setText("00:00:00");
                binding.millisecond.setText("00");
                binding.pImage.setImageResource(R.drawable.play_ic);
                stopBlinking();
            }
        });
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            timeInMillis = System.currentTimeMillis() - startTime;
            int seconds = (int) (timeInMillis / 1000);
            int minutes = seconds / 60;
            int hours = minutes / 60;
            seconds = seconds % 60;
            int millis = (int) (timeInMillis % 1000);

            binding.timer.setText(String.format("%02d:%02d:%02d", hours, minutes % 60, seconds));
            binding.millisecond.setText(String.format("%02d", millis / 10)); // Showing milliseconds divided by 10 for a more readable display
            handler.postDelayed(this, 10);
        }
    };


    private void setupBlinkAnimation() {
        blinkAnimator = ValueAnimator.ofFloat(1.0f, 0.0f);
        blinkAnimator.setDuration(500); // Set duration for one blink (500ms)
        blinkAnimator.setRepeatMode(ValueAnimator.REVERSE); // Reverse the animation (fade in/out)
        blinkAnimator.setRepeatCount(ValueAnimator.INFINITE); // Repeat indefinitely

        blinkAnimator.addUpdateListener(animation -> {
            float alpha = (float) animation.getAnimatedValue();
            binding.timer.setAlpha(alpha);
            binding.millisecond.setAlpha(alpha);
        });
    }

    // Start blinking effect
    private void startBlinking() {
        if (!blinkAnimator.isRunning()) {
            blinkAnimator.start();
        }
    }

    // Stop blinking effect
    private void stopBlinking() {
        if (blinkAnimator.isRunning()) {
            blinkAnimator.cancel();
            binding.timer.setAlpha(1.0f); // Ensure visibility when stopping
            binding.millisecond.setAlpha(1.0f);
        }
    }
}
