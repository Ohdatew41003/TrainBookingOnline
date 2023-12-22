package com.example.trainbookingonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ShareActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference tripsRef = database.getReference("trips");
    TrainTrip trainTrip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        Intent intent = getIntent();
        Uri appLinkData = intent.getData();

        if (appLinkData != null) {
            String idTrainTrip = appLinkData.getQueryParameter("idTrainTrip");
            if (idTrainTrip != null) {
                Query query = tripsRef.orderByChild("idTrainTrip").equalTo(idTrainTrip);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                TrainTrip trip = dataSnapshot.getValue(TrainTrip.class);
                                if (trip != null) {
                                    trainTrip= new TrainTrip(trip);
                                    Intent intent = new Intent(ShareActivity.this,SodoTrainActivity.class);
                                    Bundle bundle= new Bundle();
                                    bundle.putSerializable("obj_traintrip",trainTrip);
                                    intent.putExtras(bundle);
                                    ShareActivity.this.startActivity(intent);
                                    finish();
                                }
                            }
                        } else {
                            // Xử lý khi không tìm thấy dữ liệu phù hợp với idTrainTrip
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
    }
}