package com.example.roommvvm;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import android.content.ClipData;
import android.content.ClipboardManager;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;



import android.widget.Button;
import android.widget.ImageView;

import android.widget.Toast;
import java.time.LocalTime;

import com.bumptech.glide.Glide;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {
    private TextView addButton;
    private TextView itemCount;

    ImageView imageView;
    TextView urlTv;

    ImageView copyBtn;

    String imageUrl;

    public AddFragment() {

    }

    public static AddFragment newInstance() {
        AddFragment fragment = new AddFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);




    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addButton = view.findViewById(R.id.add_item);
        itemCount = view.findViewById(R.id.item_count);

        LocalTime Time = LocalTime.now();
        int currentHour = Time.getHour();

        imageView = view.findViewById(R.id.imageView);
        urlTv = view.findViewById(R.id.urlTv);
        copyBtn = view.findViewById(R.id.copyBtn); // Added this line to initialize copyBtn

        imageUrl = urlTv.getText().toString();

        UrlViewModel urlViewModel = new ViewModelProvider(getActivity()).get(UrlViewModel.class);
        addButton.setOnClickListener(v -> urlViewModel.addUrl(imageUrl, currentHour));

        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imageUrl = urlTv.getText().toString();

                ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);

                ClipData clip = ClipData.newPlainText("Image URL", imageUrl);

                clipboard.setPrimaryClip(clip);

                Toast.makeText(requireActivity(), "Image URL copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        urlViewModel.getUrls().observe(getActivity(), urls ->  {
            Log.i("UWC", "Urls live data in fragment: ");
            itemCount.setText(String.valueOf(urls.size()));
        });
        loadImg(getActivity().getIntent());
    }




    private void loadImg(Intent intent){
        String action = intent.getAction();
        if((action.equals(Intent.ACTION_SEND))){
            Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
            if (imageUri != null){

                imageView.setBackgroundColor(Color.TRANSPARENT);

                imageUrl = imageUri.toString();
                urlTv.setText(imageUrl);

                Glide.with(this)
                        .load(imageUri)
                        .into(imageView);
                // the code that adds the url and creation date to database here
            }
        }else{
            Log.i("action", "action: " + action);
        }
    }
}