package com.example.trainbookingonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SodoTrainActivity extends AppCompatActivity {
    private Spinner btn_cabin;
    private Button btn_tieptuc;
    private ImageButton btn_shareTrainTrip;
    private TextView textViewIDTrain, textView_noidi_noiden, textView_thoigiandi, textView_toa;
    private SeatAdapter seatAdapter;
    private RecyclerView recyclerView;
    private List<Seat> dataList;
    private ArrayList<Seat> seats; //danh sách ghế đã chọn


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sodo_train);

        btn_cabin = findViewById(R.id.btn_cabin);
        btn_tieptuc= findViewById(R.id.btn_tieptuc);
        textViewIDTrain= findViewById(R.id.textView_toa_ghe);
        textView_thoigiandi= findViewById(R.id.textView_thoigiandi);
        textView_noidi_noiden= findViewById(R.id.textView_noidi_noiden);
        textView_toa= findViewById(R.id.textView_toa);
        recyclerView = findViewById(R.id.list_seat);
        btn_shareTrainTrip=findViewById(R.id.btn_shareTrainTrip);
        dataList = new ArrayList<>();
        seats= new ArrayList<>();
        // ATTENTION: This was auto-generated to handle app links.
//        Intent appLinkIntent = getIntent();
//        String appLinkAction = appLinkIntent.getAction();
//        Uri appLinkData = appLinkIntent.getData();
//        if (appLinkData != null) {
//            String idTrainTrip = appLinkData.getQueryParameter("idTrainTrip");
//            if (idTrainTrip != null) {
//                Log.d("idTrainTrip", idTrainTrip);
//            }
//        }

        Intent intent = getIntent();
        if (intent != null) {
            TrainTrip trainTrip = (TrainTrip) intent.getSerializableExtra("obj_traintrip");
            if (trainTrip != null) {
                //Thông tin chuyến tàu
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference trainsRef = database.getReference("trains");
                Query query = trainsRef.orderByChild("trainId").equalTo(trainTrip.getIdTrain());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            DataSnapshot trainSnapshot = snapshot.getChildren().iterator().next();
                            Train train = trainSnapshot.getValue(Train.class);
                            if (train != null) {
                                textViewIDTrain.setText(train.getTrainName());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                textView_noidi_noiden.setText(trainTrip.getStationDepartureStation()+" - "+trainTrip.getStationArrivalStation());
                try {
                    SimpleDateFormat sdfInput = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                    SimpleDateFormat sdfOutput = new SimpleDateFormat("HH:mm - dd/MM", Locale.getDefault());
                    String ngaydi= trainTrip.getDayhoursDeparture();
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
                                        return Integer.compare(Integer.parseInt(seat1.getSeatNumber()), Integer.parseInt(seat2.getSeatNumber()));
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
            btn_tieptuc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SodoTrainActivity.this,ThanhToanActivity.class);
                    Bundle bundle= new Bundle();
                    bundle.putSerializable("obj_seats",seats); //kiểu dữ liệu là ArrayList<Seat>
                    bundle.putSerializable("obj_traintrip", trainTrip);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            btn_shareTrainTrip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDynamicLinks dynamicLinks = FirebaseDynamicLinks.getInstance();
                    Uri.Builder dynamicLinkUri = Uri.parse("https://trainbookingonline.page.link/traintrip")
                            .buildUpon()
                            .appendQueryParameter("idTrainTrip", trainTrip.getIdTrip()+""); // Thay đổi giá trị idTrainTrip ở đây

                    Log.d("link", dynamicLinkUri.toString());
                    Task<ShortDynamicLink> shortLinkTask = dynamicLinks.createDynamicLink()
                            .setLink(dynamicLinkUri.build())
                            .setDomainUriPrefix("https://trainbookingonline.page.link")
                            .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                            .buildShortDynamicLink()
                            .addOnCompleteListener(SodoTrainActivity.this, new OnCompleteListener<ShortDynamicLink>() {
                                @Override
                                public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                                    if (task.isSuccessful() && task.getResult() != null) {
                                        Uri shortLink = task.getResult().getShortLink();
                                        Uri flowchartLink = task.getResult().getPreviewLink();

                                        Log.d("taolink: ", shortLink+"\n"+flowchartLink);
                                        // Chia sẻ dynamic link
                                        Intent intentShareTrainTrip = new Intent();
                                        intentShareTrainTrip.setAction(Intent.ACTION_SEND);
                                        intentShareTrainTrip.setType("text/plain");
                                        intentShareTrainTrip.putExtra(Intent.EXTRA_TEXT, shortLink != null ? shortLink.toString() : "No link available");
                                        startActivity(Intent.createChooser(intentShareTrainTrip, "Choose one"));
                                    } else {
                                        Log.d("failt", "sai rồi");
                                    }
                                }
                            });
                }
            });
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
            holder.item_seat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Kiểm tra xem view_status có background là seat_booked hay không
                    Drawable currentBackground = holder.view_status.getBackground();
                    Drawable bookedDrawable = ContextCompat.getDrawable(mContext, R.drawable.seat_booked);
                    Drawable choseDrawable = ContextCompat.getDrawable(mContext, R.drawable.seat_chose);
                    Drawable inchoseDrawable = ContextCompat.getDrawable(mContext, R.drawable.black_border);

                    // Nếu là booked thì không cho chọn
                    if (currentBackground == null || bookedDrawable == null || (currentBackground.getConstantState() == bookedDrawable.getConstantState())) {
                        return;
                    }

                    // Nếu đã chọn thì cho quay lại ban đầu
                    if (currentBackground.getConstantState() == choseDrawable.getConstantState()){
                        holder.view_status.setBackgroundResource(R.drawable.black_border);
                        holder.view_tmp.setBackgroundColor(Color.parseColor("#000000"));
                        holder.item_seat_linear.setBackgroundResource(R.drawable.black_border);
                        seats.remove(data);
                        return;
                    }

                    if (currentBackground.getConstantState() == inchoseDrawable.getConstantState()){
                        //Click chọn chỗ
                        holder.view_status.setBackgroundResource(R.drawable.seat_chose);
                        holder.view_tmp.setBackgroundColor(Color.parseColor("#00FFFF"));
                        holder.item_seat_linear.setBackgroundResource(R.drawable.blue_border);
                        seats.add(data);
                    }
                }
            });
        }
        public void release(){
            mContext = null;
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public class SeatViewHolder extends RecyclerView.ViewHolder {
            private TextView textView_soghe, textView_sotien;
            private View view_status, view_tmp;
            private CardView item_seat;
            private LinearLayout item_seat_linear;
            public SeatViewHolder(@NonNull View itemView) {
                super(itemView);
                item_seat= itemView.findViewById(R.id.item_seat);
                item_seat_linear=itemView.findViewById(R.id.item_seat_linear);
                view_status= itemView.findViewById(R.id.view_status);
                view_tmp= itemView.findViewById(R.id.view_tmp);
                textView_soghe= itemView.findViewById(R.id.textView_soghe);
                textView_sotien= itemView.findViewById(R.id.textView_sotien);
            }

            public void bind(Seat data) {
                textView_soghe.setText(data.getSeatNumber()+"");
                int price= data.getPrice();
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
                        view_tmp.setBackgroundColor(Color.parseColor("#CC0000"));
                        item_seat_linear.setBackgroundResource(R.drawable.red_border);
                        break;
                    case "available":
                        view_status.setBackgroundResource(R.drawable.black_border);
                        view_tmp.setBackgroundColor(Color.parseColor("#000000"));
                        item_seat_linear.setBackgroundResource(R.drawable.black_border);
                        break;
                    default:
                        break;
                }
                for (Seat seat: seats){
                    if(data.getSeatNumber() == seat.getSeatNumber() && data.getCabin().equals(seat.getCabin()) ){
                        view_status.setBackgroundResource(R.drawable.seat_chose);
                        view_tmp.setBackgroundColor(Color.parseColor("#00FFFF"));
                        item_seat_linear.setBackgroundResource(R.drawable.blue_border);
                    }
                }
            }
        }
    }
}