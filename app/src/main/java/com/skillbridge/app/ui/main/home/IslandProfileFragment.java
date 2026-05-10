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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.skillbridge.app.databinding.ItemIslandProfileBinding;
import com.skillbridge.app.utils.SharedPreferencesManager;
import com.skillbridge.app.viewmodel.MainViewModel;

public class IslandProfileFragment extends Fragment {

    private ItemIslandProfileBinding binding;
    private MainViewModel mainViewModel;
    private SharedPreferencesManager prefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ItemIslandProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        prefs = SharedPreferencesManager.getInstance(requireContext());
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        updateProfileInfo();
        observeViewModel();
    }

    private void updateProfileInfo() {
        binding.tvIslandName.setText(prefs.getUserName());
        binding.tvIslandEmail.setText(prefs.getUserEmail());
        
        // Calculate skills count
        String skills = prefs.getUserSkills();
        int skillCount = 0;
        if (skills != null && !skills.isEmpty()) {
            skillCount = skills.split(",").length;
        }
        binding.tvIslandSkillsCount.setText(String.valueOf(skillCount));
    }

    private void observeViewModel() {
        mainViewModel.getProjectCount().observe(getViewLifecycleOwner(), count -> {
            binding.tvIslandProjectsCount.setText(String.valueOf(count));
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh counts and profile data when visible
        updateProfileInfo();
        mainViewModel.updateCounts();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
