/*
 * SkillBridge Android Application
 * Copyright (c) 2026
 * Developed by:
 * Nekkanti Venkata Avinash Krishna
 * Kommana Dheeraj
 *
 * Unauthorized academic re-submission or commercial reuse is prohibited.
 */
package com.skillbridge.app.ui.auth;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.skillbridge.app.databinding.ActivitySplashBinding;
import com.skillbridge.app.ui.main.MainActivity;
import com.skillbridge.app.utils.SharedPreferencesManager;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initial state
        binding.ivLogo.setAlpha(0f);
        binding.ivLogo.setScaleX(0.5f);
        binding.ivLogo.setScaleY(0.5f);
        binding.tvAppName.setAlpha(0f);
        binding.tvTagline.setAlpha(0f);

        // Logo Animation
        binding.ivLogo.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(1000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // Fade in text
                        binding.tvAppName.animate().alpha(1f).setDuration(600).start();
                        binding.tvTagline.animate().alpha(1f).setDuration(600).setStartDelay(200).start();
                    }
                }).start();

        // Glow circle animation
        ObjectAnimator glow = ObjectAnimator.ofFloat(binding.glowCircle, View.ALPHA, 0.1f, 0.3f);
        glow.setDuration(1500);
        glow.setRepeatCount(ObjectAnimator.INFINITE);
        glow.setRepeatMode(ObjectAnimator.REVERSE);
        glow.start();

        handler.postDelayed(() -> {
            if (SharedPreferencesManager.getInstance(this).isLoggedIn()) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        binding = null;
    }
}