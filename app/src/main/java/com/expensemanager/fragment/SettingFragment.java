package com.expensemanager.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.expensemanager.R;
import com.expensemanager.activity.ContainerActivity;
import com.expensemanager.activity.EntryActivity;
import com.expensemanager.databinding.FragmentSettingBinding;
import com.expensemanager.databse.IncomeDatabase;
import com.expensemanager.databse.UserDatabase;
import com.expensemanager.model.Income;
import com.expensemanager.model.User;
import com.expensemanager.utils.PreferenceHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SettingFragment extends Fragment {

    FragmentSettingBinding binding;
    Executor executor = Executors.newSingleThreadExecutor();
    private Uri profile_uri;
    private String imagePath;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(getLayoutInflater(), container, false);

        ShowData();

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

        binding.backBtn.setOnClickListener(view -> {
            getActivity().finish();
        });

        binding.profileImgAddBtn.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
            } else {
                openImageSelection();
            }
        });

        binding.saveBtn.setOnClickListener(view -> {
            String imageUrl = imagePath;
            String name = binding.profileName.getText().toString().trim();
            String email = binding.profileEmail.getText().toString().trim();
            String phone = binding.profilePhone.getText().toString().trim();
            String profession = binding.profileProfession.getText().toString().trim();
            String password = binding.profilePassword.getText().toString().trim();
            String question = binding.securityQuestion.getText().toString().trim();
            String answer = binding.securityAnswer.getText().toString().trim();

            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

            if (name.equals("")) {
                binding.profileName.setError("Required");
            } else if (email.equals("")) {
                binding.profileEmail.setError("Required");
            } else if (!email.matches(emailRegex)) {
                binding.profileEmail.setError("Invalid email address");
            } else if (phone.equals("")) {
                binding.profilePhone.setError("Required");
            } else if (profession.equals("")) {
                binding.profileProfession.setError("Required");
            } else if (password.equals("")) {
                binding.profilePassword.setError("Required");
            } else {
                executor.execute(() -> {
                    UserDatabase userDatabase = UserDatabase.getInstance(getActivity());
                    User userdata = userDatabase.userDao().getUser();

                    User user = new User();
                    user.setId(userdata.getId());
                    user.setUser_name(name);
                    user.setUser_email(email);
                    user.setUser_phone(phone);
                    user.setUser_profession(profession);
                    user.setUser_password(password);

                    if (question.equals("")){
                        user.setSecurity_question(userdata.getSecurity_question());
                    }else {
                        user.setSecurity_question(question);
                    }
                    if (answer.equals("")){
                        user.setSecurity_answer(userdata.getSecurity_answer());
                    }else {
                        user.setSecurity_answer(answer);
                    }
                    if (imageUrl == null && userdata.getUser_profile_img().equals("")) {
                        user.setUser_profile_img("");
                    }else if (imageUrl == null && !userdata.getUser_profile_img().equals("")){
                        user.setUser_profile_img(userdata.getUser_profile_img());
                    }else {
                        user.setUser_profile_img(imageUrl);
                    }

                    userDatabase.userDao().updateUser(user);
                    saveCredentials(email, password);
                    Intent intent = new Intent(getActivity(), ContainerActivity.class);
                    intent.putExtra("profile", "profile");
                    startActivity(intent);
                    getActivity().finish();
                });
            }
        });

        return binding.getRoot();
    }

    private void ShowData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                UserDatabase userDatabase = UserDatabase.getInstance(getActivity());
                User userData = userDatabase.userDao().getUser();
                if (userData != null){
                    binding.profileName.setText(userData.getUser_name());
                    binding.profileEmail.setText(userData.getUser_email());
                    binding.profileProfession.setText(userData.getUser_profession());
                    binding.profilePassword.setText(userData.getUser_password());
                    binding.profilePhone.setText(userData.getUser_phone());
                    if (!userData.getUser_profile_img().equals("")){
                        binding.profileImgSet.setImageURI(Uri.parse(userData.getUser_profile_img()));
                    }
                }
            }
        });
    }

    private void openImageSelection() {
        Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
        intent1.setType("image/*");
        startActivityForResult(intent1, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with image selection
                openImageSelection();
            } else {
                // Permission denied, handle it (e.g., show a message to the user)
                Toast.makeText(requireContext(), "Permission denied. You can't select an image.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    profile_uri = data.getData();
                    binding.profileImgSet.setImageURI(profile_uri);
                    imagePath = saveImageToInternalStorage(profile_uri);
                }
            }
        }
    }

    private String saveImageToInternalStorage(Uri imageUri) {
        try {
            String fileName = "profile_image.jpg";
            File imageFile = new File(requireContext().getFilesDir(), fileName);

            InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri);
            FileOutputStream outputStream = new FileOutputStream(imageFile);

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveCredentials(String email, String password) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("savedEmail", email);
        editor.putString("savedPassword", password);
        editor.apply();
    }
}
