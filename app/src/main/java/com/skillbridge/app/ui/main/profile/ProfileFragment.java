/*
 * SkillBridge Android Application
 * Copyright (c) 2026
 * Developed by:
 * Nekkanti Venkata Avinash Krishna
 * Kommana Dheeraj
 *
 * Unauthorized academic re-submission or commercial reuse is prohibited.
 */
package com.skillbridge.app.ui.main.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.skillbridge.app.R;
import com.skillbridge.app.databinding.DialogEditProfileBinding;
import com.skillbridge.app.databinding.FragmentProfileBinding;
import com.skillbridge.app.ui.auth.LoginActivity;
import com.skillbridge.app.utils.SharedPreferencesManager;
import com.skillbridge.app.viewmodel.MainViewModel;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private MainViewModel mainViewModel;
    private SharedPreferencesManager prefs;
    private static final int PICK_IMAGE_REQUEST = 100;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prefs = SharedPreferencesManager.getInstance(requireContext());
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        loadProfileData();
        setupClickListeners();
        observeViewModel();
    }

    private void observeViewModel() {
        mainViewModel.getProjectCount().observe(getViewLifecycleOwner(), count -> {
            binding.tvProjectsCount.setText(String.valueOf(count));
        });

        mainViewModel.getConnectionCount().observe(getViewLifecycleOwner(), count -> {
            binding.tvConnectionsCount.setText(String.valueOf(count));
        });
    }

    private void loadProfileData() {
        binding.tvName.setText(prefs.getUserName());
        binding.tvBio.setText(prefs.getUserBio());
        binding.tvSkills.setText(prefs.getUserSkills());

        String profileUri = prefs.getProfileImageUri();
        if (profileUri != null) {
            Glide.with(this).load(Uri.parse(profileUri)).circleCrop().into(binding.ivProfile);
        } else {
            binding.ivProfile.setImageResource(R.drawable.ic_skillbridge_logo);
        }

        setupSkillsChips();
    }

    private void setupSkillsChips() {
        binding.skillsContainer.removeAllViews();
        String skills = prefs.getUserSkills();
        if (skills != null && !skills.isEmpty()) {
            String[] skillArray = skills.split(",");
            for (String skill : skillArray) {
                View chipView = LayoutInflater.from(requireContext()).inflate(R.layout.item_chip, binding.skillsContainer, false);
                android.widget.TextView chip = chipView.findViewById(R.id.chip_text);
                if (chip == null && chipView instanceof android.widget.TextView) {
                    chip = (android.widget.TextView) chipView;
                }
                if (chip != null) {
                    chip.setText(skill.trim());
                    binding.skillsContainer.addView(chipView);
                }
            }
        }
    }

    private void setupClickListeners() {
        binding.ivProfile.setOnClickListener(v -> pickImage());
        binding.tvCameraBadge.setOnClickListener(v -> pickImage());

        binding.tvEditProfile.setOnClickListener(v -> showEditDialog());

        binding.btnLogout.setOnClickListener(v -> {
            prefs.logout();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                prefs.saveProfileImageUri(imageUri.toString());
                Glide.with(this).load(imageUri).circleCrop().into(binding.ivProfile);
                Snackbar.make(binding.getRoot(), "Photo updated!", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void showEditDialog() {
        DialogEditProfileBinding dialogBinding = DialogEditProfileBinding.inflate(getLayoutInflater());
        
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogBinding.getRoot())
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialogBinding.etName.setText(prefs.getUserName());
        dialogBinding.etBio.setText(prefs.getUserBio());
        dialogBinding.etSkills.setText(prefs.getUserSkills());

        dialogBinding.btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialogBinding.btnSave.setOnClickListener(v -> {
            String name = dialogBinding.etName.getText().toString().trim();
            String bio = dialogBinding.etBio.getText().toString().trim();
            String skills = dialogBinding.etSkills.getText().toString().trim();

            if (name.isEmpty()) {
                dialogBinding.tilName.setError("Name cannot be empty");
                return;
            }

            prefs.saveUserName(name);
            prefs.saveUserBio(bio);
            prefs.saveUserSkills(skills);
            
            loadProfileData();
            dialog.dismiss();
            Snackbar.make(binding.getRoot(), "Profile updated!", Snackbar.LENGTH_SHORT).show();
        });

        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mainViewModel.updateCounts();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
