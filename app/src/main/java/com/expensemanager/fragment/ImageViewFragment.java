package com.expensemanager.fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.expensemanager.R;
import com.expensemanager.databinding.FragmentImageViewBinding;

public class ImageViewFragment extends Fragment {

    FragmentImageViewBinding binding;
    String imageUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentImageViewBinding.inflate(getLayoutInflater(), container, false);

        imageUrl = getActivity().getIntent().getStringExtra("profileImg");

        if (imageUrl != null){
            binding.imageView.setImageURI(Uri.parse(imageUrl));
        }

        binding.backBtn.setOnClickListener(view -> {
            getActivity().finish();
        });

        return binding.getRoot();
    }
}