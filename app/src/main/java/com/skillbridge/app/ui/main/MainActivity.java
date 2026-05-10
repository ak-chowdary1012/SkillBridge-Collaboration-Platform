/*
 * SkillBridge Android Application
 * Copyright (c) 2026
 * Developed by:
 * Nekkanti Venkata Avinash Krishna
 * Kommana Dheeraj
 *
 * Unauthorized academic re-submission or commercial reuse is prohibited.
 */
package com.skillbridge.app.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.skillbridge.app.R;
import com.skillbridge.app.databinding.ActivityMainBinding;
import com.skillbridge.app.receiver.NetworkChangeReceiver;
import com.skillbridge.app.service.DataSyncService;
import com.skillbridge.app.ui.main.connections.ConnectionsFragment;
import com.skillbridge.app.ui.main.home.HomeFragment;
import com.skillbridge.app.ui.main.post.PostFragment;
import com.skillbridge.app.ui.main.profile.ProfileFragment;
import com.skillbridge.app.ui.main.search.SearchFragment;
import com.skillbridge.app.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel mainViewModel;
    private final NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver();

    private final BroadcastReceiver syncCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (DataSyncService.ACTION_SYNC_COMPLETE.equals(intent.getAction())) {
                Toast.makeText(context, "Data Sync Complete", Toast.LENGTH_SHORT).show();
                mainViewModel.updateCounts();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setupBottomNav();

        if (savedInstanceState == null) {
            showFragment(new HomeFragment(), "home");
        }

        startService(new Intent(this, DataSyncService.class));
    }

    private void setupBottomNav() {
        binding.bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) showFragment(new HomeFragment(), "home");
            else if (id == R.id.nav_search) showFragment(new SearchFragment(), "search");
            else if (id == R.id.nav_post) showFragment(new PostFragment(), "post");
            else if (id == R.id.nav_connections) showFragment(new ConnectionsFragment(), "connections");
            else if (id == R.id.nav_profile) showFragment(new ProfileFragment(), "profile");
            return true;
        });
    }

    private void showFragment(Fragment fragment, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment, tag)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION), Context.RECEIVER_NOT_EXPORTED);
            registerReceiver(syncCompleteReceiver, new IntentFilter(DataSyncService.ACTION_SYNC_COMPLETE), Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            registerReceiver(syncCompleteReceiver, new IntentFilter(DataSyncService.ACTION_SYNC_COMPLETE));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkChangeReceiver);
        unregisterReceiver(syncCompleteReceiver);
    }
}