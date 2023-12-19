package com.example.trainbookingonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SodoTrainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sodo_train);

        Intent intent = getIntent();
        if (intent != null) {
            TrainTrip trainTrip = (TrainTrip) intent.getSerializableExtra("obj_traintrip");
            Log.d("onCreate: ", trainTrip.getNgaydi()+", "+trainTrip.getNgayden());
        }
    }
}