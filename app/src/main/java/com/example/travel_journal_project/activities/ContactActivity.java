package com.example.travel_journal_project.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.travel_journal_project.MainActivity;
import com.example.travel_journal_project.R;

public class ContactActivity extends AppCompatActivity {
    private ImageView callButton;
    private ImageView emailButton;

    private ImageView backButton;
    private static final String PHONE_NUMBER = "07657899766";
    private static final String EMAIL_ADDRESS = "journeystoremember@gmail.com";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);


        Toolbar toolbar = findViewById(R.id.toolbarContact);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        callButton = findViewById(R.id.dialButton);
        emailButton = findViewById(R.id.emailButton);
        backButton = findViewById(R.id.back_button);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + PHONE_NUMBER));
                startActivity(intent);
            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + EMAIL_ADDRESS));
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}