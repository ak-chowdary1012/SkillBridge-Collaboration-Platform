/*
 * SkillBridge Android Application
 * Copyright (c) 2026
 * Developed by:
 * Nekkanti Venkata Avinash Krishna
 * Kommana Dheeraj
 *
 * Unauthorized academic re-submission or commercial reuse is prohibited.
 */
package com.skillbridge.app.ui.main.home;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayoutMediator;
import com.skillbridge.app.R;
import com.skillbridge.app.adapter.ProjectAdapter;
import com.skillbridge.app.databinding.FragmentHomeBinding;
import com.skillbridge.app.model.Project;
import com.skillbridge.app.ui.notifications.NotificationActivity;
import com.skillbridge.app.utils.ApiHelper;
import com.skillbridge.app.utils.SensorHelper;
import com.skillbridge.app.utils.SharedPreferencesManager;
import com.skillbridge.app.viewmodel.HomeViewModel;
import com.skillbridge.app.viewmodel.MainViewModel;

public class HomeFragment extends Fragment implements ProjectAdapter.OnProjectClickListener {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private MainViewModel mainViewModel;
    private SensorHelper sensorHelper;
    private SharedPreferencesManager prefs;
    
    private boolean isExpanded = false;
    private boolean isDragging = false;

    private final Handler autoCloseHandler = new Handler(Looper.getMainLooper());
    private final Runnable autoCloseRunnable = this::collapseIsland;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        prefs = SharedPreferencesManager.getInstance(requireContext());
        
        setupRecyclerView();
        setupToolbar();
        setupTrendingDev();
        setupGreeting();
        setupSensor();
        setupIslandMotion();

        viewModel.getProjectsLiveData().observe(getViewLifecycleOwner(), projects -> {
            ProjectAdapter adapter = new ProjectAdapter(projects, this);
            binding.rvProjects.setAdapter(adapter);
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        mainViewModel.getRefreshTrigger().observe(getViewLifecycleOwner(), trigger -> {
            if (trigger) viewModel.refreshFeed();
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupIslandMotion() {
        // 🧩 ViewPager Setup
        binding.islandPager.setAdapter(new ProfilePagerAdapter(this));
        binding.islandPager.setOffscreenPageLimit(2);
        binding.islandPager.setPageTransformer((page, position) -> {
            float abs = Math.abs(position);
            page.setAlpha(1 - abs);
            page.setScaleX(0.9f + (1 - abs) * 0.1f);
            page.setScaleY(0.9f + (1 - abs) * 0.1f);
        });

        // 🧩 TabLayout Dots Setup
        new TabLayoutMediator(binding.islandIndicator, binding.islandPager, (tab, position) -> {
            // No text, just dots
        }).attach();

        // 🧩 Click Listener
        binding.islandView.setOnClickListener(v -> {
            if (!isExpanded) expandIsland();
            else collapseIsland();
        });

        // 🧩 Close on touch outside
        binding.islandDim.setOnClickListener(v -> {
            if (isExpanded) collapseIsland();
        });

        // 🚀 DRAG + PHYSICS
        binding.islandView.setOnTouchListener(new View.OnTouchListener() {
            float startY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                resetAutoCloseTimer(); // Interaction detected, reset timer
                
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startY = event.getRawY();
                        isDragging = false;
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        float dy = event.getRawY() - startY;
                        if (Math.abs(dy) > dp(8)) isDragging = true;
                        if (dy > 0) binding.islandView.setTranslationY(dy);
                        return true;

                    case MotionEvent.ACTION_UP:
                        if (!isDragging) {
                            v.performClick();
                        } else {
                            if (binding.islandView.getTranslationY() > dp(80)) {
                                collapseIsland();
                            }
                            // bounce back
                            binding.islandView.animate()
                                    .translationY(0)
                                    .setInterpolator(new OvershootInterpolator())
                                    .setDuration(400)
                                    .start();
                        }
                        return true;
                }
                return false;
            }
        });
    }

    private void resetAutoCloseTimer() {
        autoCloseHandler.removeCallbacks(autoCloseRunnable);
        if (isExpanded) {
            autoCloseHandler.postDelayed(autoCloseRunnable, 5000);
        }
    }

    private void expandIsland() {
        if (isExpanded) return;
        isExpanded = true;

        // Pop Animation
        binding.islandView.animate()
                .scaleX(1.05f)
                .scaleY(1.05f)
                .setDuration(150)
                .withEndAction(() -> {
                    binding.islandView.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(150)
                            .start();
                    binding.islandView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                });

        ValueAnimator widthAnim = ValueAnimator.ofInt(binding.islandView.getWidth(), getScreenWidth());
        ValueAnimator heightAnim = ValueAnimator.ofInt(binding.islandView.getHeight(), dp(260));

        widthAnim.addUpdateListener(animation -> {
            binding.islandView.getLayoutParams().width = (int) animation.getAnimatedValue();
            binding.islandView.requestLayout();
        });

        heightAnim.addUpdateListener(animation -> {
            binding.islandView.getLayoutParams().height = (int) animation.getAnimatedValue();
            binding.islandView.requestLayout();
        });

        AnimatorSet set = new AnimatorSet();
        set.playTogether(widthAnim, heightAnim);
        set.setDuration(350);
        set.setInterpolator(new DecelerateInterpolator());
        set.start();

        // Fade Content
        binding.islandTitle.animate().alpha(0).setDuration(150).start();
        binding.islandExpandedContent.setVisibility(View.VISIBLE);
        binding.islandExpandedContent.animate().alpha(1).setDuration(250).start();

        // Dim Background
        binding.islandDim.setVisibility(View.VISIBLE);
        binding.islandDim.animate().alpha(1).setDuration(300).start();

        resetAutoCloseTimer();
    }

    private void collapseIsland() {
        if (!isExpanded) return;
        isExpanded = false;
        
        autoCloseHandler.removeCallbacks(autoCloseRunnable);

        ValueAnimator widthAnim = ValueAnimator.ofInt(binding.islandView.getWidth(), dp(160));
        ValueAnimator heightAnim = ValueAnimator.ofInt(binding.islandView.getHeight(), dp(50));

        widthAnim.addUpdateListener(animation -> {
            binding.islandView.getLayoutParams().width = (int) animation.getAnimatedValue();
            binding.islandView.requestLayout();
        });

        heightAnim.addUpdateListener(animation -> {
            binding.islandView.getLayoutParams().height = (int) animation.getAnimatedValue();
            binding.islandView.requestLayout();
        });

        AnimatorSet set = new AnimatorSet();
        set.playTogether(widthAnim, heightAnim);
        set.setDuration(300);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.start();

        binding.islandTitle.animate().alpha(1).setDuration(200).start();
        binding.islandExpandedContent.animate().alpha(0).setDuration(150).withEndAction(() -> {
            binding.islandExpandedContent.setVisibility(View.INVISIBLE);
        }).start();

        // Hide Dim
        binding.islandDim.animate().alpha(0).setDuration(250).withEndAction(() -> {
            binding.islandDim.setVisibility(View.GONE);
        }).start();
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }

    private int getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels - dp(32);
    }

    private void setupGreeting() {
        String name = prefs.getUserName();
        binding.tvGreeting.setText(getString(R.string.hello_name, name));
    }

    private void setupRecyclerView() {
        binding.rvProjects.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void setupToolbar() {
        binding.ivNotification.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), NotificationActivity.class));
        });
    }

    private void setupTrendingDev() {
        binding.pbApi.setVisibility(View.VISIBLE);
        ApiHelper.getInstance(requireContext()).fetchTrendingDeveloper(new ApiHelper.ApiCallback() {
            @Override
            public void onSuccess(String name, String company) {
                if (isAdded()) {
                    binding.pbApi.setVisibility(View.GONE);
                    binding.tvDevName.setText(name);
                    binding.tvDevCompany.setText(company);
                }
            }

            @Override
            public void onError(String message) {
                if (isAdded()) {
                    binding.pbApi.setVisibility(View.GONE);
                    binding.tvDevName.setText(getString(R.string.could_not_load));
                }
            }
        });
    }

    private void setupSensor() {
        sensorHelper = new SensorHelper(requireContext());
        sensorHelper.setOnShakeListener(count -> {
            viewModel.refreshFeed();
            mainViewModel.updateCounts();
            
            Snackbar snackbar = Snackbar.make(binding.getRoot(), getString(R.string.feed_refreshed), Snackbar.LENGTH_SHORT);
            View bottomNav = requireActivity().findViewById(R.id.bottom_nav);
            if (bottomNav != null) {
                snackbar.setAnchorView(bottomNav);
            }
            snackbar.show();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorHelper.register();
        mainViewModel.updateCounts();
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorHelper.unregister();
        autoCloseHandler.removeCallbacks(autoCloseRunnable);
    }

    @Override
    public void onProjectClick(Project project) {
        Intent intent = new Intent(requireContext(), ProjectDetailActivity.class);
        intent.putExtra("project", project);
        startActivity(intent);
    }

    @Override
    public void onApplyClick(Project project) {
        // Handled via ViewModel
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        autoCloseHandler.removeCallbacks(autoCloseRunnable);
        binding = null;
    }
}