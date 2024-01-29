package com.expensemanager.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.expensemanager.R;
import com.expensemanager.activity.EntryActivity;
import com.expensemanager.activity.MainActivity;
import com.expensemanager.adapter.IncomeAdapter;
import com.expensemanager.databinding.FragmentLogInBinding;
import com.expensemanager.databse.ExpenseDatabase;
import com.expensemanager.databse.IncomeDatabase;
import com.expensemanager.databse.UserDatabase;
import com.expensemanager.model.Income;
import com.expensemanager.model.User;

import java.security.acl.Owner;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LogInFragment extends Fragment {

    FragmentLogInBinding binding;
    Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLogInBinding.inflate(getLayoutInflater(), container, false);

        loadSavedCredentials();

        binding.signInBtn.setOnClickListener(view -> {
            String email = binding.emailInput.getText().toString().trim();
            String password = binding.passwordInput.getText().toString().trim();

            if (email.equals("")) {
                binding.emailInput.setError("Required");
            } else if (password.equals("")) {
                binding.passwordInput.setError("Required");
            } else {
                executor.execute(() -> {
                    LiveData<User> backgroundResult = UserDatabase.getInstance(getActivity()).userDao().loginUser(email, password);
                    getActivity().runOnUiThread(() -> {
                        backgroundResult.observe(getViewLifecycleOwner(), user -> {
                            if (user != null) {
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean("isLoggedIn", true);
                                editor.apply();

                                saveCredentials(email, password);

                                startActivity(new Intent(getActivity(), MainActivity.class));
                                getActivity().finish();
                                Toast.makeText(getActivity(), "Log In Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "User not exist or password incorrect", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                });
            }
        });

        binding.forgotPassword.setOnClickListener(view -> {
            ShowDialog();
        });

        binding.registerBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), EntryActivity.class);
            intent.putExtra("register", "register");
            startActivity(intent);
            getActivity().finish();
        });

        return binding.getRoot();
    }

    private void ShowDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        View dialog = LayoutInflater.from(getActivity()).inflate(R.layout.forgot_password_layout, null);
        alert.setView(dialog);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCancelable(true);

        AutoCompleteTextView securityQuestion = dialog.findViewById(R.id.securityQuestion);
        EditText securityAnswer = dialog.findViewById(R.id.securityAnswer);
        TextView passwordText = dialog.findViewById(R.id.passwordText);
        TextView userPassword = dialog.findViewById(R.id.userPassword);
        AppCompatButton verifyBtn = dialog.findViewById(R.id.verifyBtn);
        AppCompatButton okBtn = dialog.findViewById(R.id.okBtn);
        ProgressBar progressBar = dialog.findViewById(R.id.progressBar);

        String[] questionType = new String[]{"What is your mother's maiden name?", "What is your first childhood nickname?", "What is your favorite movie or book?", "What is the birthplace of your father?"};

        ArrayAdapter adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.account_type_list,
                questionType);

        securityQuestion.setAdapter(adapter);

        verifyBtn.setOnClickListener(view -> {
            String question = securityQuestion.getText().toString();
            String answer = securityAnswer.getText().toString();

            if (question.equals("")){
                securityQuestion.setError("Required");
            }else if (answer.equals("")){
                securityAnswer.setError("Required");
            }else {
                verifyBtn.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                Thread thread = new Thread(){
                    public void run(){
                        try {
                            sleep(4000);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        finally {
                            executor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    UserDatabase userDatabase = UserDatabase.getInstance(getActivity());
                                    User user = userDatabase.userDao().getUserForRetrievePassword(question, answer);

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (user != null) {
                                                userPassword.setText(user.getUser_password());
                                                passwordText.setVisibility(View.VISIBLE);
                                                userPassword.setVisibility(View.VISIBLE);
                                                okBtn.setVisibility(View.VISIBLE);
                                                progressBar.setVisibility(View.GONE);
                                            } else {
                                                Toast.makeText(getActivity(), "Your provided information not matched", Toast.LENGTH_SHORT).show();
                                                alertDialog.dismiss();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }
                };thread.start();
            }
        });

        okBtn.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void saveCredentials(String email, String password) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("savedEmail", email);
        editor.putString("savedPassword", password);
        editor.apply();
    }

    private void loadSavedCredentials() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String savedEmail = preferences.getString("savedEmail", "");
        String savedPassword = preferences.getString("savedPassword", "");
        binding.emailInput.setText(savedEmail);
        binding.passwordInput.setText(savedPassword);
    }
}