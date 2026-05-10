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

import com.bumptech.glide.Glide;
import com.skillbridge.app.R;
import com.skillbridge.app.databinding.ItemUserBinding;
import com.skillbridge.app.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<User> users;
    private final OnUserClickListener listener;

    public interface OnUserClickListener {
        void onConnectClick(User user, int position);
    }

    public UserAdapter(List<User> users, OnUserClickListener listener) {
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserBinding binding = ItemUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bind(users.get(position), position, listener);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        private final ItemUserBinding binding;

        public UserViewHolder(ItemUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(User user, int position, OnUserClickListener listener) {
            binding.tvName.setText(user.getName());
            binding.tvSkills.setText(user.getSkills());
            binding.tvBio.setText(user.getBio());

            int[] colors = {0xFF1A73E8, 0xFF34A853, 0xFFD4A017, 0xFFE53935, 0xFF9C27B0, 0xFF00BCD4};
            binding.ivAvatar.setBorderColor(colors[position % 6]);
            binding.ivAvatar.setBorderWidth(3);

            Glide.with(binding.getRoot().getContext())
                    .load(user.getProfileImageUrl())
                    .placeholder(R.drawable.ic_skillbridge_logo)
                    .circleCrop()
                    .into(binding.ivAvatar);

            updateConnectButton(user);

            binding.btnConnect.setOnClickListener(v -> listener.onConnectClick(user, position));
        }

        private void updateConnectButton(User user) {
            if (user.isConnected()) {
                binding.btnConnect.setText("Connected ✓");
                binding.btnConnect.setBackgroundResource(R.drawable.btn_apply_gradient);
            } else {
                binding.btnConnect.setText("Connect");
                binding.btnConnect.setBackgroundResource(R.drawable.btn_connect);
            }
        }
    }
}