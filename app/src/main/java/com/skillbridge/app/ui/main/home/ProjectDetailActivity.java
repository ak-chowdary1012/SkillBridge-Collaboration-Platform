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

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.skillbridge.app.R;
import com.skillbridge.app.databinding.ActivityProjectDetailBinding;
import com.skillbridge.app.model.Project;

public class ProjectDetailActivity extends AppCompatActivity {

    private ActivityProjectDetailBinding binding;
    private Project project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProjectDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        project = (Project) getIntent().getSerializableExtra("project");
        if (project == null) {
            finish();
            return;
        }

        setupToolbar();
        populateData();
        setupClickListeners();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(project.getTitle());
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void populateData() {
        binding.tvTitle.setText(project.getTitle());
        binding.tvDescription.setText(project.getDescription());
        binding.tvPostedBy.setText(project.getPostedBy());
        binding.tvTeamSize.setText(String.valueOf(project.getTeamSize()));
        binding.tvLocation.setText(getString(R.string.location_format, project.getLocation()));

        binding.chipsContainer.removeAllViews();
        String skillsRequired = project.getSkillsRequired();
        if (skillsRequired != null && !skillsRequired.isEmpty()) {
            String[] skills = skillsRequired.split(",");
            for (String skill : skills) {
                TextView chip = (TextView) LayoutInflater.from(this).inflate(R.layout.item_chip, binding.chipsContainer, false);
                chip.setText(skill.trim());
                binding.chipsContainer.addView(chip);
            }
        }

        if (project.isApplied()) {
            binding.btnApply.setText(getString(R.string.applied));
            binding.btnApply.setBackgroundResource(android.R.drawable.btn_default);
        } else {
            binding.btnApply.setText(getString(R.string.apply_now));
            binding.btnApply.setBackgroundResource(R.drawable.btn_apply_gradient);
        }
    }

    private void setupClickListeners() {
        binding.btnApply.setOnClickListener(v -> {
            project.setApplied(!project.isApplied());
            if (project.isApplied()) {
                binding.btnApply.setText(getString(R.string.applied));
                binding.btnApply.setBackgroundResource(android.R.drawable.btn_default);
                Snackbar.make(binding.getRoot(), getString(R.string.application_sent), Snackbar.LENGTH_SHORT).show();
            } else {
                binding.btnApply.setText(getString(R.string.apply_now));
                binding.btnApply.setBackgroundResource(R.drawable.btn_apply_gradient);
            }
        });

        binding.btnMap.setOnClickListener(v -> {
            Intent intent = new Intent(ProjectDetailActivity.this, MapActivity.class);
            intent.putExtra("title", project.getTitle());
            intent.putExtra("lat", project.getLatitude());
            intent.putExtra("lng", project.getLongitude());
            intent.putExtra("location", project.getLocation());
            startActivity(intent);
        });

        binding.btnShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out: " + project.getTitle());
            shareIntent.putExtra(Intent.EXTRA_TEXT, project.getTitle() + "\n\n" + project.getDescription() + "\n\nSkills: " + project.getSkillsRequired() + "\n\nPosted on SkillBridge");
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)));
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}