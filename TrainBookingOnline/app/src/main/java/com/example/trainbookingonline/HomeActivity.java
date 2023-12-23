package com.example.trainbookingonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    Button btn_ngaydi, btn_ngayve, btn_timchuyen, btn_swap;
    RadioButton btn_motchieu, btn_khuhoi;
    Spinner spinner_gadi, spinner_gaden;
    RadioGroup radiogroup_luachon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SCHEDULE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SCHEDULE_EXACT_ALARM}, 101);
        }

        Locale vietnamLocale = new Locale("vi", "VN");
        Locale.setDefault(vietnamLocale);

        fetchProvincesFromFirebase();

        btn_swap= findViewById(R.id.btn_swap);
        btn_ngaydi=findViewById(R.id.btn_ngaydi);
        btn_ngayve=findViewById(R.id.btn_ngayve);
        btn_timchuyen=findViewById(R.id.btn_timchuyen);
        btn_motchieu=findViewById(R.id.btn_motchieu);
        btn_khuhoi=findViewById(R.id.btn_khuhoi);
        radiogroup_luachon= findViewById(R.id.radiogroup_luachon);
        spinner_gadi=findViewById(R.id.spinner_gadi);
        spinner_gaden=findViewById(R.id.spinner_gaden);

        btn_swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedIndexGadi = spinner_gadi.getSelectedItemPosition();
                int selectedIndexGaden = spinner_gaden.getSelectedItemPosition();

                spinner_gadi.setSelection(selectedIndexGaden);
                spinner_gaden.setSelection(selectedIndexGadi);
            }
        });
        radiogroup_luachon.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);

                if (radioButton != null) {
                    String selectedValue = radioButton.getText().toString();
                    if (selectedValue.equals("Một chiều")){
                        btn_ngayve.setEnabled(false);
                        String colorHex = "#A5A5A5";
                        int color = Color.parseColor(colorHex);
                        btn_ngayve.setTextColor(color);
                    }else {
                        btn_ngayve.setEnabled(true);
                        String colorHex = "#000000";
                        int color = Color.parseColor(colorHex);
                        btn_ngayve.setTextColor(color);
                    }
                }
            }
        });
        btn_ngaydi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(btn_ngaydi);
            }
        });
        btn_ngayve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(btn_ngayve);
            }
        });

        btn_timchuyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gadi = spinner_gadi.getSelectedItem().toString();
                String gaden = spinner_gaden.getSelectedItem().toString();
                RadioButton luachon = findViewById(radiogroup_luachon.getCheckedRadioButtonId());
                String selectedOption = luachon.getText().toString();
                String ngaydi = btn_ngaydi.getText().toString();
                String ngayve = btn_ngayve.getText().toString();

                boolean isRoundTrip = selectedOption.equals("Khứ hồi");
                boolean isNgayDiChosen = !ngaydi.equals("Chọn ngày đi");
                boolean isNgayVeChosen = !ngayve.equals("Chọn ngày về");

                if (isRoundTrip && (!isNgayDiChosen || !isNgayVeChosen)) {
                    Toast.makeText(HomeActivity.this, "Vui lòng chọn đầy đủ ngày đi và ngày về", Toast.LENGTH_SHORT).show();
                } else if (!isRoundTrip && !isNgayDiChosen) {
                    Toast.makeText(HomeActivity.this, "Vui lòng chọn ngày đi", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(HomeActivity.this, TrainsActivity.class);
                    intent.putExtra("spinner_gadi", gadi);
                    intent.putExtra("spinner_gaden", gaden);
                    intent.putExtra("luachon", selectedOption);
                    intent.putExtra("ngaydi", ngaydi);

                    if (isRoundTrip) {
                        intent.putExtra("ngayve", ngayve);
                    }

                    startActivity(intent);
                }
            }
        });

    }
    private void fetchProvincesFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("provinces");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> provinceList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String provinceName = snapshot.child("province").getValue(String.class);
                    provinceList.add(provinceName);
                }

                setupSpinner(provinceList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình truy xuất dữ liệu từ Firebase
            }
        });
    }
    private void setupSpinner(List<String> provinceList) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinceList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gadi.setAdapter(spinnerAdapter);
        spinner_gaden.setAdapter(spinnerAdapter);
    }

    private void showDatePickerDialog(Button button) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                button.setText(selectedDate);
            }
        }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

}
