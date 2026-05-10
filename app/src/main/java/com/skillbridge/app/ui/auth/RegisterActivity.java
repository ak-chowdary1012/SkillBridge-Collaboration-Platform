/*
 * SkillBridge Android Application
 * Copyright (c) 2026
 * Developed by:
 * Nekkanti Venkata Avinash Krishna
 * Kommana Dheeraj
 *
 * Unauthorized academic re-submission or commercial reuse is prohibited.
 */
package com.skillbridge.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.skillbridge.app.databinding.ActivityRegisterBinding;
import com.skillbridge.app.utils.SharedPreferencesManager;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private SharedPreferencesManager prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefs = SharedPreferencesManager.getInstance(this);

        binding.btnRegister.setOnClickListener(v -> handleRegister());
        binding.tvLogin.setOnClickListener(v -> finish());
    }

    private void handleRegister() {
        String name = Objects.requireNonNull(binding.etFullName.getText()).toString().trim();
        String email = Objects.requireNonNull(binding.etEmail.getText()).toString().trim();
        String skills = Objects.requireNonNull(binding.etSkills.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.etPassword.getText()).toString().trim();
        String confirmPassword = Objects.requireNonNull(binding.etConfirmPassword.getText()).toString().trim();

        if (TextUtils.isEmpty(name)) {
            binding.etFullName.setError("Name is required");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            binding.etEmail.setError("Email is required");
            return;
        }
        if (TextUtils.isEmpty(skills)) {
            binding.etSkills.setError("Skills are required");
            return;
        }
        if (password.length() < 6) {
            binding.etPassword.setError("Password must be at least 6 characters");
            return;
        }
        if (!password.equals(confirmPassword)) {
            binding.etConfirmPassword.setError("Passwords do not match");
            return;
        }

        prefs.saveUserName(name);
        prefs.saveUserEmail(email);
        prefs.saveUserSkills(skills);
        prefs.saveUserPassword(password);

        Toast.makeText(this, "Registered Successfully! Please login.", Toast.LENGTH_SHORT).show();
        
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}