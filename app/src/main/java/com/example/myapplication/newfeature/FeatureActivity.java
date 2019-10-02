package com.example.myapplication.newfeature;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.servicelayer.DataManager;
import com.example.myapplication.servicelayer.FeatureManager;

import javax.inject.Inject;

public class FeatureActivity extends AppCompatActivity {

    @Inject
    DataManager dataManager;
    @Inject
    FeatureManager featureManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FeatureComponentFactory.getFeatureComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView viewById = findViewById(R.id.textview);
        viewById.setText(String.format("%s - %s",
                dataManager.getData(),
                featureManager.getFeatureData()));
    }
}
