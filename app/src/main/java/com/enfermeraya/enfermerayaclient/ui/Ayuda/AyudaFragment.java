package com.enfermeraya.enfermerayaclient.ui.Ayuda;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.enfermeraya.enfermerayaclient.R;
import com.google.firebase.database.annotations.Nullable;


public class AyudaFragment extends Fragment {

    private com.enfermeraya.enfermerayaclient.ui.Ayuda.AyudaViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(AyudaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_ayuda, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
