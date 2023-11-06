package com.example.trainbookingonline;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editTextUsername;
    EditText editTextPassword;
    Button loginButton;
    Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsername =findViewById(R.id.editTextUsername);
        editTextPassword =findViewById(R.id.editTextPassword);
        loginButton =findViewById(R.id.loginButton);
        registerButton =findViewById(R.id.registerButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username= editTextUsername.getText().toString();
                String password= editTextPassword.getText().toString();

                if(username.equals("admin") && password.equals("admin")){
                    Toast.makeText(MainActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Saỉ tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Đăng ký tài khoản!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}