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

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.skillbridge.app.R;
import com.skillbridge.app.databinding.ItemConnectionBinding;
import com.skillbridge.app.model.User;

import java.util.List;

public class ConnectionsAdapter extends RecyclerView.Adapter<ConnectionsAdapter.ConnectionsViewHolder> {

    private final List<User> users;
    private final OnConnectionClickListener listener;

    public interface OnConnectionClickListener {
        void onConnectClick(User user, int position);
        void onMessageClick(User user);
    }

    public ConnectionsAdapter(List<User> users, OnConnectionClickListener listener) {
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ConnectionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemConnectionBinding binding = ItemConnectionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ConnectionsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ConnectionsViewHolder holder, int position) {
        holder.bind(users.get(position), position, listener);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class ConnectionsViewHolder extends RecyclerView.ViewHolder {
        private final ItemConnectionBinding binding;

        public ConnectionsViewHolder(ItemConnectionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(User user, int position, OnConnectionClickListener listener) {
            binding.tvName.setText(user.getName());
            binding.tvSkills.setText(user.getSkills());
            binding.tvBio.setText(user.getBio());

            // Show Creator Tag for authors
            if (isAuthor(user.getName())) {
                binding.tvCreatorTag.setVisibility(View.VISIBLE);
            } else {
                binding.tvCreatorTag.setVisibility(View.GONE);
            }

            int[] colors = {0xFF1A73E8, 0xFF34A853, 0xFFD4A017, 0xFFE53935, 0xFF9C27B0, 0xFF00BCD4};
            binding.ivAvatar.setBorderColor(colors[position % 6]);
            binding.ivAvatar.setBorderWidth(4);

            Glide.with(binding.getRoot().getContext())
                    .load(user.getProfileImageUrl())
                    .placeholder(R.drawable.ic_skillbridge_logo)
                    .circleCrop()
                    .into(binding.ivAvatar);

            updateConnectButton(user);

            binding.btnConnect.setOnClickListener(v -> listener.onConnectClick(user, position));
            binding.btnMessage.setOnClickListener(v -> listener.onMessageClick(user));
        }

        private boolean isAuthor(String userName) {
            return "Avinash Krishna Nekkanti".equals(userName) || "K. Dheeraj".equals(userName);
        }

        private void updateConnectButton(User user) {
            if (user.isConnected()) {
                binding.btnConnect.setText("Connected ✓");
                binding.btnConnect.setTextColor(Color.WHITE);
                binding.btnConnect.setBackgroundResource(R.drawable.btn_apply_gradient);
            } else {
                binding.btnConnect.setText("Connect");
                binding.btnConnect.setTextColor(binding.getRoot().getContext().getColor(R.color.connect_btn_text));
                binding.btnConnect.setBackgroundResource(R.drawable.btn_connect);
            }
        }
    }
}