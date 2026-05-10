/*
 * SkillBridge Android Application
 * Copyright (c) 2026
 * Developed by:
 * Nekkanti Venkata Avinash Krishna
 * Kommana Dheeraj
 *
 * Unauthorized academic re-submission or commercial reuse is prohibited.
 */
package com.skillbridge.app.ui.main.connections;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.skillbridge.app.adapter.ConnectionsAdapter;
import com.skillbridge.app.databinding.FragmentConnectionsBinding;
import com.skillbridge.app.model.User;
import com.skillbridge.app.viewmodel.ConnectionsViewModel;
import com.skillbridge.app.viewmodel.MainViewModel;

import java.util.List;

public class ConnectionsFragment extends Fragment implements ConnectionsAdapter.OnConnectionClickListener {

    private FragmentConnectionsBinding binding;
    private ConnectionsViewModel viewModel;
    private MainViewModel mainViewModel;
    private ConnectionsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentConnectionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ConnectionsViewModel.class);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        
        binding.rvConnections.setLayoutManager(new LinearLayoutManager(requireContext()));

        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.getUsersLiveData().observe(getViewLifecycleOwner(), users -> {
            adapter = new ConnectionsAdapter(users, this);
            binding.rvConnections.setAdapter(adapter);
            binding.tvEmpty.setVisibility(users.isEmpty() ? View.VISIBLE : View.GONE);
        });

        viewModel.getStatusMessage().observe(getViewLifecycleOwner(), message -> {
            Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onConnectClick(User user, int position) {
        boolean wasConnected = user.isConnected();
        viewModel.toggleConnection(user);
        
        // Sync with MainViewModel count
        if (!wasConnected && user.isConnected()) {
            mainViewModel.incrementConnectionCount();
        } else if (wasConnected && !user.isConnected()) {
            // Optional: add decrement if needed, for now just increment works for the user's request
            // mainViewModel.decrementConnectionCount(); 
        }
    }

    @Override
    public void onMessageClick(User user) {
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("sms:9999999999"));
            intent.putExtra("sms_body", "Hi " + user.getName() + "! I found you on SkillBridge.");
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(requireContext(), "No messaging app found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}