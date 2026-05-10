/*
 * SkillBridge Android Application
 * Copyright (c) 2026
 * Developed by:
 * Nekkanti Venkata Avinash Krishna
 * Kommana Dheeraj
 *
 * Unauthorized academic re-submission or commercial reuse is prohibited.
 */
package com.skillbridge.app.ui.main.post;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.skillbridge.app.R;
import com.skillbridge.app.adapter.PostedProjectAdapter;
import com.skillbridge.app.databinding.FragmentPostBinding;
import com.skillbridge.app.model.Project;
import com.skillbridge.app.viewmodel.MainViewModel;
import com.skillbridge.app.viewmodel.PostViewModel;

import java.util.Objects;

public class PostFragment extends Fragment implements PostedProjectAdapter.OnDeleteClickListener {

    private FragmentPostBinding binding;
    private PostViewModel viewModel;
    private MainViewModel mainViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPostBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(PostViewModel.class);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        binding.rvMyProjects.setLayoutManager(new LinearLayoutManager(requireContext()));

        setupSpinner();
        setupPostButton();
        observeViewModel();
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<CharSequence>(requireContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.team_size_entries)) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(requireContext().getColor(R.color.text_primary));
                return v;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTextColor(requireContext().getColor(R.color.text_primary));
                v.setBackgroundColor(requireContext().getColor(R.color.card_bg));
                return v;
            }
        };
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spTeamSize.setAdapter(spinnerAdapter);
    }

    private void setupPostButton() {
        binding.btnPost.setOnClickListener(v -> {
            String title = Objects.requireNonNull(binding.etTitle.getText()).toString().trim();
            String desc = Objects.requireNonNull(binding.etDescription.getText()).toString().trim();
            String skills = Objects.requireNonNull(binding.etSkills.getText()).toString().trim();
            String location = Objects.requireNonNull(binding.etLocation.getText()).toString().trim();
            int teamSize = Integer.parseInt(binding.spTeamSize.getSelectedItem().toString());

            if (TextUtils.isEmpty(title)) {
                binding.etTitle.setError(getString(R.string.error_title_required));
                return;
            }
            if (TextUtils.isEmpty(desc)) {
                binding.etDescription.setError(getString(R.string.error_description_required));
                return;
            }

            viewModel.postProject(title, desc, skills, location, teamSize);
        });
    }

    private void observeViewModel() {
        viewModel.getMyProjectsLiveData().observe(getViewLifecycleOwner(), projects -> {
            PostedProjectAdapter adapter = new PostedProjectAdapter(projects, this);
            binding.rvMyProjects.setAdapter(adapter);
            binding.tvEmptyPosts.setVisibility(projects.isEmpty() ? View.VISIBLE : View.GONE);
            mainViewModel.triggerRefresh();
        });

        viewModel.getStatusMessage().observe(getViewLifecycleOwner(), message -> {
            Snackbar snackbar = Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT);
            View bottomNav = requireActivity().findViewById(R.id.bottom_nav);
            if (bottomNav != null) {
                snackbar.setAnchorView(bottomNav);
            }
            snackbar.show();

            if (message.contains(getString(R.string.status_success))) {
                clearFields();
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> 
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE)
        );

        mainViewModel.getProjectCount().observe(getViewLifecycleOwner(), count -> {
            String nth = getOrdinal(count + 1);
            binding.tvPostTitle.setText(getString(R.string.post_title_find_people, nth));
        });
    }

    private String getOrdinal(int i) {
        String[] suffixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + suffixes[i % 10];
        }
    }

    private void clearFields() {
        binding.etTitle.setText("");
        binding.etDescription.setText("");
        binding.etSkills.setText("");
        binding.etLocation.setText("");
        binding.spTeamSize.setSelection(0);
    }

    @Override
    public void onDeleteClick(Project project) {
        viewModel.deleteProject(project.getId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}