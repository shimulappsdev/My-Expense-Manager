package com.expensemanager.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.expensemanager.R;
import com.expensemanager.activity.EntryActivity;
import com.expensemanager.databinding.FragmentRegisterBinding;
import com.expensemanager.databse.UserDatabase;
import com.expensemanager.model.User;
import com.expensemanager.utils.PreferenceHelper;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RegisterFragment extends Fragment {

    FragmentRegisterBinding binding;
    Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(getLayoutInflater(), container, false);

        String[] questionType = new String[]{"What is your mother's maiden name?", "What is your first childhood nickname?", "What is your favorite movie or book?", "What is the birthplace of your father?"};

        ArrayAdapter adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.account_type_list,
                questionType);

        binding.securityQuestion.setAdapter(adapter);

        binding.securityQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!questionType[i].equals("")){
                    binding.securityAnswer.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.registerBtn.setOnClickListener(view -> {
            String name = binding.nameInput.getText().toString().trim();
            String email = binding.emailInput.getText().toString().trim();
            String profession = binding.professionInput.getText().toString().trim();
            String password = binding.passwordInput.getText().toString().trim();
            String re_password = binding.rePasswordInput.getText().toString().trim();
            String question = binding.securityQuestion.getText().toString().trim();
            String answer = binding.securityAnswer.getText().toString().trim();

            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

            if (name.equals("")) {
                binding.nameInput.setError("Required");
            } else if (email.equals("")) {
                binding.emailInput.setError("Required");
            } else if (!email.matches(emailRegex)) {
                binding.emailInput.setError("Invalid email address");
            } else if (profession.equals("")) {
                binding.professionInput.setError("Required");
            } else if (password.equals("")) {
                binding.passwordInput.setError("Required");
            } else if (!re_password.equals(password)) {
                binding.rePasswordInput.setError("Not Match");
            }else if (question.equals("")) {
                binding.securityQuestion.setError("Required");
            }else if (answer.equals("")) {
                binding.securityAnswer.setError("Required");
            } else {
                if (PreferenceHelper.isRegistered(getActivity())) {
                    Toast.makeText(getActivity(), "Already Registered.!" +"\n"+ "You can register only once", Toast.LENGTH_SHORT).show();
                } else {
                    User user = new User();
                    user.setUser_name(name);
                    user.setUser_email(email);
                    user.setUser_phone("N/A");
                    user.setUser_profession(profession);
                    user.setUser_password(password);
                    user.setSecurity_question(question);
                    user.setSecurity_answer(answer);
                    user.setUser_profile_img("");

                    executor.execute(() -> {
                        UserDatabase.getInstance(requireActivity()).userDao().registerUser(user);
                        PreferenceHelper.setRegistered(getActivity(), true);
                        Intent intent = new Intent(getActivity(), EntryActivity.class);
                        intent.putExtra("login", "login");
                        startActivity(intent);
                        getActivity().finish();
                        saveCredentials(email, password);
                    });
                    Toast.makeText(getActivity(), "Registration Successful", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.logInBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), EntryActivity.class);
            intent.putExtra("login", "login");
            startActivity(intent);
            getActivity().finish();
        });

        return binding.getRoot();
    }
    private void saveCredentials(String email, String password) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("savedEmail", email);
        editor.putString("savedPassword", password);
        editor.apply();
    }
}