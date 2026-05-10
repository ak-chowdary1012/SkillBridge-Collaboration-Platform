/*
 * SkillBridge Android Application
 * Copyright (c) 2026
 * Developed by:
 * Nekkanti Venkata Avinash Krishna
 * Kommana Dheeraj
 *
 * Unauthorized academic re-submission or commercial reuse is prohibited.
 */
package com.skillbridge.app.ui.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayoutMediator;
import com.skillbridge.app.R;
import com.skillbridge.app.databinding.DialogDynamicIslandBinding;
import com.skillbridge.app.databinding.ItemIslandCreatorsBinding;
import com.skillbridge.app.databinding.ItemIslandProfileBinding;
import com.skillbridge.app.utils.SharedPreferencesManager;

public class DynamicIslandDialog extends Dialog {

    private DialogDynamicIslandBinding binding;
    private final String userName;
    private final String userEmail;
    private final int projectCount;
    private final int skillsCount;
    
    private final int startX, startY, startWidth, startHeight;
    private static final int ANIM_DURATION = 500;

    // Fluid spring-like interpolator for Dynamic Island morphism
    private final Interpolator fluidInterpolator = new PathInterpolator(0.4f, 0.0f, 0.1f, 1.1f);

    public DynamicIslandDialog(@NonNull Context context, String userName, int projectCount, int connectionCount, 
                               int startX, int startY, int startWidth, int startHeight) {
        super(context, R.style.DynamicIslandDialogStyle);
        this.userName = userName;
        SharedPreferencesManager prefs = SharedPreferencesManager.getInstance(context);
        this.userEmail = prefs.getUserEmail();
        this.projectCount = projectCount;
        
        String skills = prefs.getUserSkills();
        if (skills != null && !skills.isEmpty()) {
            this.skillsCount = skills.split(",").length;
        } else {
            this.skillsCount = 0;
        }
        
        this.startX = startX;
        this.startY = startY;
        this.startWidth = startWidth;
        this.startHeight = startHeight;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DialogDynamicIslandBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setDimAmount(0.5f);
            
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
            
            // Adjust Y position: Dialog Y is relative to its window. 
            // Since it's a floating dialog, we might need a small adjustment for status bar if not handled by style.
            lp.y = startY - getStatusBarHeight(); 
            window.setAttributes(lp);
        }

        IslandAdapter adapter = new IslandAdapter();
        binding.viewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabDots, binding.viewPager, (tab, position) -> {
        }).attach();

        // Initial hidden state for content
        binding.viewPager.setAlpha(0f);
        binding.tabDots.setAlpha(0f);
        
        // Ensure title matches exactly
        binding.tvIslandTitle.setText(getContext().getString(R.string.app_name));
        
        binding.getRoot().post(this::startMorphicExpansion);
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void startMorphicExpansion() {
        final View root = binding.getRoot();
        
        // Measure target size
        root.measure(View.MeasureSpec.makeMeasureSpec(root.getWidth(), View.MeasureSpec.EXACTLY),
                     View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        final int finalHeight = root.getMeasuredHeight();
        final int finalWidth = root.getWidth();

        ValueAnimator morphAnimator = ValueAnimator.ofFloat(0f, 1f);
        morphAnimator.setDuration(ANIM_DURATION);
        morphAnimator.setInterpolator(fluidInterpolator);

        morphAnimator.addUpdateListener(animation -> {
            float fraction = (float) animation.getAnimatedValue();
            
            ViewGroup.LayoutParams lp = root.getLayoutParams();
            lp.width = (int) (startWidth + (finalWidth - startWidth) * fraction);
            lp.height = (int) (startHeight + (finalHeight - startHeight) * fraction);
            root.setLayoutParams(lp);

            // Fade in content
            if (fraction > 0.4f) {
                float contentAlpha = Math.min(1.0f, (fraction - 0.4f) * 2.5f);
                binding.viewPager.setAlpha(contentAlpha);
                binding.tabDots.setAlpha(contentAlpha);
                binding.viewPager.setTranslationY((1.0f - contentAlpha) * 30);
            }
        });

        morphAnimator.start();
    }

    @Override
    public void dismiss() {
        final View root = binding.getRoot();
        final int currentHeight = root.getHeight();
        final int currentWidth = root.getWidth();

        ValueAnimator reverseAnimator = ValueAnimator.ofFloat(1f, 0f);
        reverseAnimator.setDuration(300);
        reverseAnimator.setInterpolator(new PathInterpolator(0.4f, 0.0f, 0.2f, 1f));

        reverseAnimator.addUpdateListener(animation -> {
            float fraction = (float) animation.getAnimatedValue();
            
            ViewGroup.LayoutParams lp = root.getLayoutParams();
            lp.width = (int) (startWidth + (currentWidth - startWidth) * fraction);
            lp.height = (int) (startHeight + (currentHeight - startHeight) * fraction);
            root.setLayoutParams(lp);
            
            binding.viewPager.setAlpha(fraction);
            binding.tabDots.setAlpha(fraction);
        });

        reverseAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                DynamicIslandDialog.super.dismiss();
            }
        });
        reverseAnimator.start();
    }

    class IslandAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        @Override
        public int getItemViewType(int position) { return position; }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            if (viewType == 0) {
                return new ProfileViewHolder(ItemIslandProfileBinding.inflate(inflater, parent, false));
            } else {
                return new CreatorsViewHolder(ItemIslandCreatorsBinding.inflate(inflater, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ProfileViewHolder) {
                ((ProfileViewHolder) holder).bind();
            }
        }

        @Override
        public int getItemCount() { return 2; }

        class ProfileViewHolder extends RecyclerView.ViewHolder {
            private final ItemIslandProfileBinding b;
            public ProfileViewHolder(ItemIslandProfileBinding b) {
                super(b.getRoot());
                this.b = b;
            }
            void bind() {
                b.tvIslandName.setText(userName);
                b.tvIslandEmail.setText(userEmail);
                b.tvIslandProjectsCount.setText(String.valueOf(projectCount));
                b.tvIslandSkillsCount.setText(String.valueOf(skillsCount));
            }
        }

        class CreatorsViewHolder extends RecyclerView.ViewHolder {
            public CreatorsViewHolder(ItemIslandCreatorsBinding b) {
                super(b.getRoot());
            }
        }
    }
}
