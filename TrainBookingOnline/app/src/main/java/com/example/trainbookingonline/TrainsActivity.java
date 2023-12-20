package com.example.trainbookingonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TrainsActivity extends AppCompatActivity {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    RecyclerView recyclerView;
    TrainTripAdapter trainTripAdapter;
    List<TrainTrip> dataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trains);
        Intent intent = getIntent();
        if (intent != null) {
            String text_gadi = intent.getStringExtra("spinner_gadi");
            String text_gaden = intent.getStringExtra("spinner_gaden");
            String text_luachon = intent.getStringExtra("luachon");
            String text_ngaydi = intent.getStringExtra("ngaydi");
            String text_ngayve="0/0/0";
            if (text_luachon.equals("Khứ hồi")){
               text_ngayve = intent.getStringExtra("ngayve");
            }

            recyclerView = findViewById(R.id.list_traintrip);
            dataList = new ArrayList<>();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("trips");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Lặp qua từng đối tượng
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        TrainTrip trainTrip = snapshot.getValue(TrainTrip.class);
                        Log.d("onDataChange: ", trainTrip+"");
                        if (trainTrip.getIdTrainTrip() != null && trainTrip.getGaden().equals(text_gaden) && trainTrip.getGadi().equals(text_gadi) && trainTrip.getNgaydi().contains(text_ngaydi)) {
                            dataList.add(trainTrip);
                        }
                    }

                    trainTripAdapter = new TrainTripAdapter(TrainsActivity.this, dataList);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(trainTripAdapter);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(TrainsActivity.this, "Lỗi xảy ra khi load chuyến tàu", Toast.LENGTH_SHORT).show();
                }
            });


            // Thêm đối tượng mới
//            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            DatabaseReference tripsRef = database.getReference("trips");
//            String idTrainTrip = tripsRef.push().getKey();
//            try {
//                Random random = new Random();
//
//                SimpleDateFormat sdfDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
//                SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//
//                Date ngaydi = sdfDate.parse(text_ngaydi); // Chuyển ngày đi thành kiểu Date
//                Toast.makeText(this, ngaydi.toString(), Toast.LENGTH_SHORT).show();
//
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(ngaydi);
//
//                calendar.add(Calendar.DATE, (random.nextInt(2)+1) ); // thời gian đến là 1 hoặc 2 ngày
//                Date ngayden = calendar.getTime();
//
//                // Sinh giờ, phút, giây ngẫu nhiên
//
//                calendar.setTime(ngaydi);
//                calendar.set(Calendar.HOUR_OF_DAY, random.nextInt(24)); // Giờ từ 0-23
//                calendar.set(Calendar.MINUTE, random.nextInt(60)); // Phút từ 0-59
//                calendar.set(Calendar.SECOND, random.nextInt(60)); // Giây từ 0-59
//
//                ngaydi = calendar.getTime();
//                String ngaydiString = sdfDateTime.format(ngaydi);
//
//                calendar.setTime(ngayden);
//                calendar.set(Calendar.HOUR_OF_DAY, random.nextInt(24));
//                calendar.set(Calendar.MINUTE, random.nextInt(60));
//                calendar.set(Calendar.SECOND, random.nextInt(60));
//
//                ngayden = calendar.getTime();
//                String ngaydenString = sdfDateTime.format(ngayden);
//
//                TrainTrip trainTrip = new TrainTrip(idTrainTrip,"SE20",text_gadi,text_gaden,ngaydiString,ngaydenString);
//                tripsRef.child(idTrainTrip).setValue(trainTrip);
//                Toast.makeText(this, "Thành công", Toast.LENGTH_SHORT).show();
//            } catch (Exception e){
//                e.printStackTrace();
//            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(trainTripAdapter!=null){
            trainTripAdapter.release();
        }
    }

    private class TrainTripAdapter extends RecyclerView.Adapter<TrainTripAdapter.TrainTripViewHolder>{
        private List<TrainTrip> dataList;
        private Context mContext;

        public TrainTripAdapter(Context context,List<TrainTrip> dataList) {
            this.mContext=context;
            this.dataList = dataList;
        }
        @NonNull
        @Override
        public TrainTripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_traintrip, parent, false);
            return new TrainTripViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull TrainTripViewHolder holder, int position) {
            TrainTrip data = dataList.get(position);
            holder.bind(data);
            holder.item_traintrip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickGoToSODO(data);
                }
            });
        }

        private void onClickGoToSODO(TrainTrip trainTrip) {
            Intent intent = new Intent(mContext,SodoTrainActivity.class);
            Bundle bundle= new Bundle();
            bundle.putSerializable("obj_traintrip",trainTrip);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        }
        public void release(){
            mContext = null;
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public class TrainTripViewHolder extends RecyclerView.ViewHolder {
            private TextView textViewIDTrain, textView_giodi, textView_gioden, textView_noidi, textView_noiden, textView_ngayden, textView_ngaydi, textView_thoigian, textView_cho;
            private LinearLayout item_traintrip;
            public TrainTripViewHolder(@NonNull View itemView) {
                super(itemView);
                item_traintrip= itemView.findViewById(R.id.item_traintrip);
                textViewIDTrain = itemView.findViewById(R.id.textView_toa_ghe);
                textView_giodi = itemView.findViewById(R.id.textView_thongtinve);
                textView_gioden = itemView.findViewById(R.id.textViewTong_ticket);
                textView_noidi = itemView.findViewById(R.id.textView_noidi);
                textView_noiden = itemView.findViewById(R.id.textView_noiden);
                textView_cho = itemView.findViewById(R.id.textView_cho);
                textView_thoigian = itemView.findViewById(R.id.textView_thoigian);
                textView_ngaydi = itemView.findViewById(R.id.textView_ngaydi);
                textView_ngayden = itemView.findViewById(R.id.textView_ngayden);
            }

            public void bind(TrainTrip data) {
                textViewIDTrain.setText(data.getIdTrain());
                textView_noidi.setText(data.getGadi());
                textView_noiden.setText(data.getGaden());
                String ngaydi_db= data.getNgaydi();
                String[] parts = ngaydi_db.split(" ");
                String ngaydi= parts[0];
                String giodi= parts[1];
                String ngayden_db= data.getNgayden();
                parts = ngayden_db.split(" ");
                String ngayden= parts[0];
                String gioden= parts[1];
                textView_ngaydi.setText(ngaydi);
                textView_giodi.setText(giodi);
                textView_ngayden.setText(ngayden);
                textView_gioden.setText(gioden);
                textView_cho.setText("Chỗ còn "+150);
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                    Date ngaydiDate = sdf.parse(ngaydi_db);
                    Date ngaydenDate = sdf.parse(ngayden_db);

                    Calendar calendar1 = Calendar.getInstance();
                    Calendar calendar2 = Calendar.getInstance();

                    calendar1.setTime(ngaydiDate);
                    calendar2.setTime(ngaydenDate);

                    long diffInMillis = Math.abs(calendar2.getTimeInMillis() - calendar1.getTimeInMillis());

                    long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis);
                    long days = diffInSeconds / (24 * 3600);
                    long remainingSeconds = diffInSeconds % (24 * 3600);
                    long hours = remainingSeconds / 3600;
                    long remainingMinutes = (remainingSeconds % 3600) / 60;

                    String result = days + "d " + hours + "h " + remainingMinutes + "m";
                    textView_thoigian.setText(result);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}