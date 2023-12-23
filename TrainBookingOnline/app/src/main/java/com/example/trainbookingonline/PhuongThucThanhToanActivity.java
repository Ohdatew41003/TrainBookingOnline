package com.example.trainbookingonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class PhuongThucThanhToanActivity extends AppCompatActivity {

    private int tongtien=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phuong_thuc_thanh_toan);

        Intent intent=getIntent();
        if(intent!=null) {
            ArrayList<Seat> seats = (ArrayList<Seat>) intent.getSerializableExtra("obj_seats");
            ArrayList<String> lstseats = (ArrayList<String>) intent.getSerializableExtra("obj_lstseats");
            TrainTrip trainTrip = (TrainTrip) intent.getSerializableExtra("obj_traintrip");
            User user = (User) intent.getSerializableExtra("obj_user");
            if (seats != null && trainTrip != null && user !=null && lstseats != null) {
                for (Seat seat:seats){
                    tongtien+=seat.getPrice();
                }
            }
            Log.d("onCreate: ", seats.size()+", "+lstseats.size()+", "+trainTrip.getIdTrip()+", "+user.getEmail()+", "+tongtien);
        }
    }
}