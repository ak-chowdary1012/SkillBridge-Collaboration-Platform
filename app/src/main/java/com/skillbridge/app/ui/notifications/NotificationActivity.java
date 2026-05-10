/*
 * SkillBridge Android Application
 * Copyright (c) 2026
 * Developed by:
 * Nekkanti Venkata Avinash Krishna
 * Kommana Dheeraj
 *
 * Unauthorized academic re-submission or commercial reuse is prohibited.
 */
package com.skillbridge.app.ui.notifications;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.skillbridge.app.R;
import com.skillbridge.app.adapter.NotificationAdapter;
import com.skillbridge.app.databinding.ActivityNotificationBinding;
import com.skillbridge.app.viewmodel.NotificationViewModel;

public class NotificationActivity extends AppCompatActivity {

    private ActivityNotificationBinding binding;
    private NotificationViewModel viewModel;
    private NotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        binding.rvNotifications.setLayoutManager(new LinearLayoutManager(this));

        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.getNotificationsLiveData().observe(this, notifications -> {
            adapter = new NotificationAdapter(notifications);
            binding.rvNotifications.setAdapter(adapter);
            binding.tvEmpty.setVisibility(notifications.isEmpty() ? View.VISIBLE : View.GONE);
        });

        viewModel.getUnreadCount().observe(this, count -> {
            if (count > 0) {
                getSupportActionBar().setTitle("Notifications (" + count + ")");
            } else {
                getSupportActionBar().setTitle("Notifications");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notification_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_mark_all_read) {
            viewModel.markAllRead();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}