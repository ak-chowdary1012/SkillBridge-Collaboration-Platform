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
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skillbridge.app.R;
import com.skillbridge.app.databinding.ItemNotificationBinding;
import com.skillbridge.app.model.Notification;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private final List<Notification> notifications;

    public NotificationAdapter(List<Notification> notifications) {
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationBinding binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NotificationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.bind(notifications.get(position));
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final ItemNotificationBinding binding;

        public NotificationViewHolder(ItemNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Notification notification) {
            binding.tvMessage.setText(notification.getMessage());
            binding.tvTime.setText(notification.getTimeAgo());

            switch (notification.getType()) {
                case "connection":
                    binding.ivIcon.setText("🤝");
                    binding.ivIcon.setBackgroundResource(R.drawable.notif_icon_blue);
                    break;
                case "project":
                    binding.ivIcon.setText("💼");
                    binding.ivIcon.setBackgroundResource(R.drawable.notif_icon_green);
                    break;
                case "message":
                    binding.ivIcon.setText("💬");
                    binding.ivIcon.setBackgroundResource(R.drawable.notif_icon_purple);
                    break;
                case "profile":
                    binding.ivIcon.setText("👁");
                    binding.ivIcon.setBackgroundResource(R.drawable.notif_icon_gold);
                    break;
            }

            if (notification.isRead()) {
                binding.notificationRoot.setBackgroundResource(R.drawable.bg_notification_read);
                binding.unreadDot.setVisibility(View.GONE);
            } else {
                binding.notificationRoot.setBackgroundResource(R.drawable.bg_notification_unread);
                binding.unreadDot.setVisibility(View.VISIBLE);
            }
        }
    }
}