/*
 * SkillBridge Android Application
 * Copyright (c) 2026
 * Developed by:
 * Nekkanti Venkata Avinash Krishna
 * Kommana Dheeraj
 *
 * Unauthorized academic re-submission or commercial reuse is prohibited.
 */
package com.skillbridge.app.ui.main.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.skillbridge.app.R;
import com.skillbridge.app.adapter.ConnectionsAdapter;
import com.skillbridge.app.adapter.ProjectAdapter;
import com.skillbridge.app.databinding.FragmentSearchBinding;
import com.skillbridge.app.model.Project;
import com.skillbridge.app.model.User;
import com.skillbridge.app.ui.main.home.ProjectDetailActivity;
import com.skillbridge.app.viewmodel.MainViewModel;
import com.skillbridge.app.viewmodel.SearchViewModel;

import java.util.List;

public class SearchFragment extends Fragment implements ProjectAdapter.OnProjectClickListener, ConnectionsAdapter.OnConnectionClickListener {

    private FragmentSearchBinding binding;
    private SearchViewModel viewModel;
    private MainViewModel mainViewModel;
    private ProjectAdapter projectAdapter;
    private ConnectionsAdapter connectionsAdapter;
    private boolean isProjectsTab = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        
        binding.rvResults.setLayoutManager(new LinearLayoutManager(requireContext()));

        setupTabs();
        setupSearchView();
        observeViewModel();
    }

    private void setupTabs() {
        binding.tabProjects.setOnClickListener(v -> setActiveTab(true));
        binding.tabPeople.setOnClickListener(v -> setActiveTab(false));
    }

    private void setActiveTab(boolean projects) {
        isProjectsTab = projects;
        if (projects) {
            binding.tabProjects.setBackgroundResource(R.drawable.tab_active);
            binding.tabProjects.setTextColor(0xFFFFFFFF);
            binding.tabPeople.setBackgroundResource(android.R.color.transparent);
            binding.tabPeople.setTextColor(requireContext().getColor(R.color.text_secondary));
            if (viewModel.getProjectsLiveData().getValue() != null) {
                updateProjectList(viewModel.getProjectsLiveData().getValue());
            }
        } else {
            binding.tabPeople.setBackgroundResource(R.drawable.tab_active);
            binding.tabPeople.setTextColor(0xFFFFFFFF);
            binding.tabProjects.setBackgroundResource(android.R.color.transparent);
            binding.tabProjects.setTextColor(requireContext().getColor(R.color.text_secondary));
            if (viewModel.getUsersLiveData().getValue() != null) {
                updateUserList(viewModel.getUsersLiveData().getValue());
            }
        }
    }

    private void setupSearchView() {
        binding.svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.search(newText);
                return true;
            }
        });
    }

    private void observeViewModel() {
        viewModel.getProjectsLiveData().observe(getViewLifecycleOwner(), projects -> {
            if (isProjectsTab) updateProjectList(projects);
        });

        viewModel.getUsersLiveData().observe(getViewLifecycleOwner(), users -> {
            if (!isProjectsTab) updateUserList(users);
        });
    }

    private void updateProjectList(List<Project> projects) {
        projectAdapter = new ProjectAdapter(projects, this);
        binding.rvResults.setAdapter(projectAdapter);
        binding.tvEmpty.setVisibility(projects.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void updateUserList(List<User> users) {
        connectionsAdapter = new ConnectionsAdapter(users, this);
        binding.rvResults.setAdapter(connectionsAdapter);
        binding.tvEmpty.setVisibility(users.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onProjectClick(Project project) {
        Intent intent = new Intent(requireContext(), ProjectDetailActivity.class);
        intent.putExtra("project", project);
        startActivity(intent);
    }

    @Override
    public void onApplyClick(Project project) {
        // Toggled in adapter
    }

    @Override
    public void onConnectClick(User user, int position) {
        boolean wasConnected = user.isConnected();
        viewModel.toggleConnection(user);
        
        // Notify global count change
        if (!wasConnected && user.isConnected()) {
            mainViewModel.incrementConnectionCount();
        }
    }

    @Override
    public void onMessageClick(User user) {
        // Handle message click
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when returning to search to ensure sync with other pages
        viewModel.search(binding.svSearch.getQuery().toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}