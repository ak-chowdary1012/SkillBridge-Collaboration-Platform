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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skillbridge.app.databinding.ItemPostedProjectBinding;
import com.skillbridge.app.model.Project;

import java.util.List;

public class PostedProjectAdapter extends RecyclerView.Adapter<PostedProjectAdapter.PostedProjectViewHolder> {

    private final List<Project> projects;
    private final OnDeleteClickListener listener;

    public interface OnDeleteClickListener {
        void onDeleteClick(Project project);
    }

    public PostedProjectAdapter(List<Project> projects, OnDeleteClickListener listener) {
        this.projects = projects;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostedProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPostedProjectBinding binding = ItemPostedProjectBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PostedProjectViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostedProjectViewHolder holder, int position) {
        holder.bind(projects.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    static class PostedProjectViewHolder extends RecyclerView.ViewHolder {
        private final ItemPostedProjectBinding binding;

        public PostedProjectViewHolder(ItemPostedProjectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Project project, OnDeleteClickListener listener) {
            binding.tvTitle.setText(project.getTitle());
            binding.tvDescription.setText(project.getDescription());
            binding.tvSkills.setText(project.getSkillsRequired());
            binding.tvLocation.setText("📍 " + project.getLocation());
            binding.tvTime.setText(project.getTimeAgo());

            binding.btnDelete.setOnClickListener(v -> listener.onDeleteClick(project));
        }
    }
}