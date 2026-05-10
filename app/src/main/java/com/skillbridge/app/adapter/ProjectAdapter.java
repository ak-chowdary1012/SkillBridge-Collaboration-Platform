/*
 * SkillBridge Android Application
 * Copyright (c) 2026
 * Developed by:
 * Nekkanti Venkata Avinash Krishna
 * Kommana Dheeraj
 *
 * Unauthorized academic re-submission or commercial reuse is prohibited.
 */
package com.skillbridge.app.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skillbridge.app.R;
import com.skillbridge.app.databinding.ItemProjectBinding;
import com.skillbridge.app.model.Project;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    private final List<Project> projects;
    private final OnProjectClickListener listener;

    public interface OnProjectClickListener {
        void onProjectClick(Project project);
        void onApplyClick(Project project);
    }

    public ProjectAdapter(List<Project> projects, OnProjectClickListener listener) {
        this.projects = projects;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProjectBinding binding = ItemProjectBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProjectViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Project project = projects.get(position);
        holder.bind(project, listener);
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    static class ProjectViewHolder extends RecyclerView.ViewHolder {
        private final ItemProjectBinding binding;

        public ProjectViewHolder(ItemProjectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Project project, OnProjectClickListener listener) {
            binding.tvTitle.setText(project.getTitle());
            binding.tvDescription.setText(project.getDescription());
            binding.tvPostedBy.setText(project.getPostedBy());
            binding.tvTimeAgo.setText(project.getTimeAgo());
            binding.tvTeam.setText(binding.getRoot().getContext().getString(R.string.team_size_format, project.getTeamSize()));

            // Clear and add chips
            binding.chipsContainer.removeAllViews();
            String skillsRequired = project.getSkillsRequired();
            if (skillsRequired != null && !skillsRequired.isEmpty()) {
                String[] skills = skillsRequired.split(",");
                for (String skill : skills) {
                    TextView chip = (TextView) LayoutInflater.from(binding.getRoot().getContext()).inflate(R.layout.item_chip, binding.chipsContainer, false);
                    chip.setText(skill.trim());
                    binding.chipsContainer.addView(chip);
                }
            }

            updateApplyButton(project);

            binding.getRoot().setOnClickListener(v -> listener.onProjectClick(project));
            binding.btnApply.setOnClickListener(v -> {
                project.setApplied(!project.isApplied());
                updateApplyButton(project);
                listener.onApplyClick(project);
            });
        }

        private void updateApplyButton(Project project) {
            if (project.isApplied()) {
                binding.btnApply.setText(binding.getRoot().getContext().getString(R.string.applied));
                binding.btnApply.setBackgroundResource(R.drawable.btn_applied_gray);
                binding.btnApply.setBackgroundTintList(null);
            } else {
                binding.btnApply.setText(binding.getRoot().getContext().getString(R.string.apply_now));
                binding.btnApply.setBackgroundResource(R.drawable.btn_apply_gradient);
                binding.btnApply.setBackgroundTintList(null);
            }
        }
    }
}