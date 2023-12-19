package com.example.trainbookingonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SodoTrainActivity extends AppCompatActivity {
    private Spinner btn_cabin;
    private TextView textViewIDTrain, textView_noidi_noiden, textView_thoigiandi, textView_toa;
    private SeatAdapter seatAdapter;
    private RecyclerView recyclerView;
    private List<Seat> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sodo_train);

        btn_cabin = findViewById(R.id.btn_cabin);
        textViewIDTrain= findViewById(R.id.textViewIDTrain);
        textView_thoigiandi= findViewById(R.id.textView_thoigiandi);
        textView_noidi_noiden= findViewById(R.id.textView_noidi_noiden);
        textView_toa= findViewById(R.id.textView_toa);
        recyclerView = findViewById(R.id.list_seat);
        dataList = new ArrayList<>();

        Intent intent = getIntent();
        if (intent != null) {
            TrainTrip trainTrip = (TrainTrip) intent.getSerializableExtra("obj_traintrip");
            if (trainTrip != null) {
                //Thông tin chuyến tàu
                textViewIDTrain.setText(trainTrip.getIdTrain());
                textView_noidi_noiden.setText(trainTrip.getGadi()+" - "+trainTrip.getGaden());
                try {
                    SimpleDateFormat sdfInput = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                    SimpleDateFormat sdfOutput = new SimpleDateFormat("HH:mm - dd/MM", Locale.getDefault());
                    String ngaydi= trainTrip.getNgaydi();
                    Date date = sdfInput.parse(ngaydi);
                    String formattedDate = sdfOutput.format(date);
                    textView_thoigiandi.setText(formattedDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //Xử lý cabin
                btn_cabin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                        String selectedCabin = adapterView.getItemAtPosition(position).toString();
                        String[] parts = selectedCabin.split("-");
                        String cabinName = parts[0];
                        String cabinNumber = parts[1];
                        textView_toa.setText("Toa số "+cabinNumber);
                        Cabin cabin = trainTrip.getCabins().get(selectedCabin);

                        //Clear datalist
                        dataList.clear();

                        if (cabin != null) {
                            Map<String, Seat> seatsMap = cabin.getSeats();
                            if (seatsMap != null) {
                                ArrayList<Seat> seatsList = new ArrayList<>(seatsMap.values());
                                // Sắp xếp danh sách ghế theo số ghế tăng dần
                                Collections.sort(seatsList, new Comparator<Seat>() {
                                    @Override
                                    public int compare(Seat seat1, Seat seat2) {
                                        // So sánh theo số ghế
                                        return Integer.compare(seat1.getSeatNumber(), seat2.getSeatNumber());
                                    }
                                });
                                // Duyệt từng ghế ngồi
                                for (Seat seat : seatsList) {
                                    dataList.add(seat);
                                }
                                seatAdapter = new SodoTrainActivity.SeatAdapter(SodoTrainActivity.this, dataList);
                                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),4);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(seatAdapter);
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        // Xử lý khi không có item nào được chọn trong Spinner (nếu cần)
                    }
                });
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(seatAdapter!=null){
            seatAdapter.release();
        }
    }

    private class SeatAdapter extends RecyclerView.Adapter<SodoTrainActivity.SeatAdapter.SeatViewHolder>{
        private List<Seat> dataList;
        private Context mContext;

        public SeatAdapter(Context context,List<Seat> dataList) {
            this.mContext=context;
            this.dataList = dataList;
        }
        @NonNull
        @Override
        public SodoTrainActivity.SeatAdapter.SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_seat, parent, false);
            return new SodoTrainActivity.SeatAdapter.SeatViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull SodoTrainActivity.SeatAdapter.SeatViewHolder holder, int position) {
            Seat data = dataList.get(position);
            holder.bind(data);
//            holder.item_traintrip.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onClickGoToSODO(data);
//                }
//            });
        }

//        private void onClickGoToSODO(TrainTrip trainTrip) {
//            Intent intent = new Intent(mContext,SodoTrainActivity.class);
//            Bundle bundle= new Bundle();
//            bundle.putSerializable("obj_traintrip",trainTrip);
//            intent.putExtras(bundle);
//            mContext.startActivity(intent);
//        }
        public void release(){
            mContext = null;
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public class SeatViewHolder extends RecyclerView.ViewHolder {
            private TextView textView_soghe, textView_sotien;
            private View view_status;
            private CardView item_seat;
            public SeatViewHolder(@NonNull View itemView) {
                super(itemView);
                item_seat= itemView.findViewById(R.id.item_seat);
                view_status= itemView.findViewById(R.id.view_status);
                textView_soghe= itemView.findViewById(R.id.textView_soghe);
                textView_sotien= itemView.findViewById(R.id.textView_sotien);
            }

            public void bind(Seat data) {
                textView_soghe.setText(data.getSeatNumber()+"");
                int price= data.getPrice();
                Log.d("bind: ", price+"");
                if (price >= 1000) {
                    double result = price / 1000.0;
                    textView_sotien.setText(String.format("%.1fK", result));
                } else {
                    textView_sotien.setText(price+"đ");
                }
                String seat_status= data.getStatus();
                switch (seat_status){
                    case "booked":
                        view_status.setBackgroundResource(R.drawable.seat_booked);
                        break;
                    case "available":
                        view_status.setBackgroundResource(R.drawable.black_border);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}