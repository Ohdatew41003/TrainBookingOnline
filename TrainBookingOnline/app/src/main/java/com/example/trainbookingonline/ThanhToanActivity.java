package com.example.trainbookingonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ThanhToanActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText editTextText_hoten, editTextText_cccd, editTextText_sdt, editTextText_email;
    private TextView textView_tongsove, textView_tongtien;
    private Button btn_tieptucthanhtoan;
    private List<Seat> dataList;
    private User user;
    private int tongtien;
    private ThanhToanActivity.SeatAdapter seatAdapter;
    private TrainTrip trainTrip;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersRef = database.getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);

        dataList=new ArrayList<>();
        recyclerView=findViewById(R.id.list_ve);
        editTextText_hoten=findViewById(R.id.editTextText_hoten);
        editTextText_cccd=findViewById(R.id.editTextText_cccd);
        editTextText_sdt=findViewById(R.id.editTextText_sdt);
        editTextText_email=findViewById(R.id.editTextText_email);
        textView_tongsove=findViewById(R.id.textView_tongsove);
        textView_tongtien=findViewById(R.id.textView_tongtien);
        btn_tieptucthanhtoan=findViewById(R.id.btn_tieptucthanhtoan);

        Intent intent=getIntent();
        if(intent!=null){
            ArrayList<Seat> seats= (ArrayList<Seat>) intent.getSerializableExtra("obj_seats");
            trainTrip= (TrainTrip) intent.getSerializableExtra("obj_traintrip");
            if(seats!=null && trainTrip!=null){
                SharedPreferences sharedPreferences= getSharedPreferences("User_Storage", Context.MODE_PRIVATE);
                String email = sharedPreferences.getString("email","False Email");
                if(!email.equals("False Email")){
                    Log.d("email: ", email);
                    Query query = usersRef.orderByChild("email").equalTo(email);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                // Lấy thông tin của người dùng từ snapshot đầu tiên
                                DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                                user = userSnapshot.getValue(User.class);
                                editTextText_hoten.setText(user.getFullName());
                                editTextText_cccd.setText(user.getCccd());
                                editTextText_sdt.setText(user.getPhoneNumber());
                                editTextText_email.setText(user.getEmail());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Xử lý lỗi nếu có
                        }
                    });
                    textView_tongsove.setText("Tổng "+seats.size()+" vé");
                    tongtien=0;
                    for (Seat seat: seats){
                        tongtien+=seat.getPrice();
                    }
                    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                    String formattedNumber = formatter.format(tongtien);
                    textView_tongtien.setText(formattedNumber);

                    dataList=new ArrayList<>(seats);
                    seatAdapter = new ThanhToanActivity.SeatAdapter(ThanhToanActivity.this, dataList);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(seatAdapter);
                }
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

    private class SeatAdapter extends RecyclerView.Adapter<ThanhToanActivity.SeatAdapter.SeatViewHolder>{
        private List<Seat> dataList;
        private Context mContext;

        public SeatAdapter(Context context,List<Seat> dataList) {
            this.mContext=context;
            this.dataList = dataList;
        }
        @NonNull
        @Override
        public ThanhToanActivity.SeatAdapter.SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_ticket, parent, false);
            return new ThanhToanActivity.SeatAdapter.SeatViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull ThanhToanActivity.SeatAdapter.SeatViewHolder holder, int position) {
            Seat data = dataList.get(position);
            holder.bind(data,trainTrip);
//            holder.item_seat.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // Kiểm tra xem view_status có background là seat_booked hay không
//                    Drawable currentBackground = holder.view_status.getBackground();
//                    Drawable bookedDrawable = ContextCompat.getDrawable(mContext, R.drawable.seat_booked);
//                    Drawable choseDrawable = ContextCompat.getDrawable(mContext, R.drawable.seat_chose);
//
//                    // Nếu là booked thì không cho chọn
//                    if (currentBackground == null || bookedDrawable == null || (currentBackground.getConstantState() == bookedDrawable.getConstantState())) {
//                        return;
//                    }
//
//                    // Nếu đã chọn thì cho quay lại ban đầu
//                    if (currentBackground.getConstantState() == choseDrawable.getConstantState()){
//                        holder.view_status.setBackgroundResource(R.drawable.black_border);
//                        holder.view_tmp.setBackgroundColor(Color.parseColor("#000000"));
//                        holder.item_seat_linear.setBackgroundResource(R.drawable.black_border);
//                        seats.remove(data);
//                        return;
//                    }
//
//                    //Click chọn chỗ
//                    holder.view_status.setBackgroundResource(R.drawable.seat_chose);
//                    holder.view_tmp.setBackgroundColor(Color.parseColor("#00FFFF"));
//                    holder.item_seat_linear.setBackgroundResource(R.drawable.blue_border);
//                    seats.add(data);
//                }
//            });
        }
        public void release(){
            mContext = null;
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public class SeatViewHolder extends RecyclerView.ViewHolder {
            private TextView textView_toa_ghe, textView_thongtinve, textView_noidi_noiden_ticket, textView_tongtien_ticket;
            private ImageButton btn_delete_ticket;
            public SeatViewHolder(@NonNull View itemView) {
                super(itemView);
                textView_toa_ghe=itemView.findViewById(R.id.textView_toa_ghe);
                textView_thongtinve=itemView.findViewById(R.id.textView_thongtinve);
                textView_noidi_noiden_ticket=itemView.findViewById(R.id.textView_noidi_noiden_ticket);
                textView_tongtien_ticket=itemView.findViewById(R.id.textView_tongtien_ticket);
                btn_delete_ticket=itemView.findViewById(R.id.btn_delete_ticket);
            }

            public void bind(Seat seat, TrainTrip trainTrip) {
                textView_toa_ghe.setText("Toa "+seat.getCabin().substring(5)+" - "+"Ghế "+seat.getSeatNumber());
                textView_noidi_noiden_ticket.setText(trainTrip.getGadi()+" - "+trainTrip.getGaden());
                NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                String formattedNumber = formatter.format(seat.getPrice());
                textView_tongtien_ticket.setText(formattedNumber);
                try {
                    SimpleDateFormat sdfInput = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                    SimpleDateFormat sdfOutput = new SimpleDateFormat("HH:mm dd/MM", Locale.getDefault());
                    String ngaydi= trainTrip.getNgaydi();
                    Date date = sdfInput.parse(ngaydi);
                    String formattedDate = sdfOutput.format(date);
                    textView_thongtinve.setText("Đi "+trainTrip.getIdTrain()+" "+formattedDate);
                }catch (ParseException e){
                    e.printStackTrace();
                }
            }
        }
    }
}