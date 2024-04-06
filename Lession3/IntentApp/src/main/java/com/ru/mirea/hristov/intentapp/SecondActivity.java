package com.ru.mirea.hristov.intentapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Получаем Intent, который запустил эту активность
        Intent intent = getIntent();
        // Получаем сообщение из Intent
        String message = intent.getStringExtra("EXTRA_MESSAGE");

        // Находим TextView для отображения сообщения
        TextView textView = findViewById(R.id.textView);
        // Отображаем сообщение в TextView
        textView.setText(message);
    }
}