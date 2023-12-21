package com.example.trainbookingonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.AlarmManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Calendar;
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
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);

        createNotificationChannel();

        dataList=new ArrayList<>();
        recyclerView=findViewById(R.id.list_ve);
        editTextText_hoten=findViewById(R.id.editTextText_hoten);
        editTextText_cccd=findViewById(R.id.editTextText_cccd);
        editTextText_sdt=findViewById(R.id.editTextText_sdt);
        editTextText_email=findViewById(R.id.editTextText_email);
        textView_tongsove=findViewById(R.id.textView_tongsove);
        textView_tongtien=findViewById(R.id.textView_tongtien);
        btn_tieptucthanhtoan=findViewById(R.id.btn_tieptucthanhtoan);

        // Vô hiệu hóa EditText
        editTextText_hoten.setEnabled(false);
        editTextText_cccd.setEnabled(false);
        editTextText_sdt.setEnabled(false);
        editTextText_email.setEnabled(false);

        Intent intent=getIntent();
        if(intent!=null){
            ArrayList<Seat> seats= (ArrayList<Seat>) intent.getSerializableExtra("obj_seats");
            trainTrip= (TrainTrip) intent.getSerializableExtra("obj_traintrip");
            if(seats!=null && trainTrip!=null){
                SharedPreferences sharedPreferences= getSharedPreferences("User_Storage", Context.MODE_PRIVATE);
                String email = sharedPreferences.getString("email","False Email");
                if(!email.equals("False Email")){
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

                    btn_tieptucthanhtoan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            for (Seat seat:dataList){
//                                String numSeat="seat";
//                                if (seat.getSeatNumber()<10){
//                                    numSeat=numSeat+"0"+seat.getSeatNumber();
//                                }else {
//                                    numSeat=numSeat+seat.getSeatNumber();
//                                }
//                                DatabaseReference seatRef = FirebaseDatabase.getInstance().getReference()
//                                        .child("trips")
//                                        .child(trainTrip.getIdTrainTrip()) // ID trip
//                                        .child("cabins")
//                                        .child(seat.getCabin()) // Tên Cabin
//                                        .child("seats")
//                                        .child(numSeat); // Tên Seat
//                                seatRef.child("status").setValue("booked");
//                            }

                            Intent intent_Thanhtoan = new Intent(ThanhToanActivity.this,PhuongThucThanhToanActivity.class);
                            Bundle bundle= new Bundle();
                            bundle.putSerializable("obj_seats",new ArrayList<>(dataList)); //kiểu dữ liệu là ArrayList<Seat> (Danh sách ghế đã chọn)
                            bundle.putSerializable("obj_traintrip", trainTrip); //kiểu dữ liệu TrainTrip (Chuyến tàu)
                            bundle.putSerializable("obj_user",user); //kiểu dữ liệu user
                            intent_Thanhtoan.putExtras(bundle);
                            startActivity(intent_Thanhtoan);

                            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            Intent intentThongBao = new Intent(ThanhToanActivity.this,AlarmReceiver.class);
                            pendingIntent= PendingIntent.getBroadcast(ThanhToanActivity.this,0,intentThongBao,PendingIntent.FLAG_MUTABLE);

                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            Calendar calendar = Calendar.getInstance();
//                            calendar.add(Calendar.SECOND, 5);

                            try {
//                                Log.d("TAG", trainTrip.getNgaydi());
                                Date date = dateFormat.parse(trainTrip.getNgaydi());
                                calendar.setTime(date);
                                calendar.add(Calendar.DATE, -1); //Thông báo trước 1 ngày
//                                calendar.add(Calendar.MONTH,1);
                                Log.d("TAG", calendar.toString());
                                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                                Toast.makeText(ThanhToanActivity.this, "Cài đặt nhắc nhở thành công", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }
    }
    private void createNotificationChannel(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name="trainBookingReminderChannel";
            String desc = "Channel for Notification about TrainTrip";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel=new NotificationChannel("trainBooking",name,importance);
            channel.setDescription(desc);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
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
            holder.bind(data, trainTrip);

            holder.btn_delete_ticket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteConfirmationDialog(data);
                }
            });
        }

        // Hàm để hiển thị AlertDialog xác nhận
        private void showDeleteConfirmationDialog(Seat data) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ThanhToanActivity.this);
            builder.setTitle("Xác nhận xóa");
            builder.setMessage("Bạn có chắc chắn muốn xóa không?");

            // Nút Xác nhận
            builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dataList.remove(data);
                    seatAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });

            // Nút Hủy
            builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            // Hiển thị AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
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