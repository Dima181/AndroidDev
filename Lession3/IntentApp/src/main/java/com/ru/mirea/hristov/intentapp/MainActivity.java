package com.ru.mirea.hristov.intentapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ваш номер
        int yourNumber = 25;
        // Квадрат вашего номера
        int squareOfYourNumber = yourNumber * yourNumber;

        // Получаем текущее время
        long dateInMillis = System.currentTimeMillis();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.getDefault());
        final String dateString = dateFormat.format(new Date(dateInMillis));

        // Отображаем текущее время в TextView
        TextView textView = findViewById(R.id.textView);
        textView.setText(dateString);

        // Находим кнопку по идентификатору
        Button button = findViewById(R.id.button);
        // Устанавливаем обработчик нажатия на кнопку с помощью лямбда-выражения
        button.setOnClickListener(view -> {
            // Создаем Intent для запуска SecondActivity
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            // Формируем сообщение для передачи в SecondActivity
            String message = "КВАДРАТ ЗНАЧЕНИЯ МОЕГО НОМЕРА ПО СПИСКУ В ГРУППЕ СОСТАВЛЯЕТ " + squareOfYourNumber + ", а текущее время " + dateString;
            // Добавляем сообщение в Intent
            intent.putExtra("EXTRA_MESSAGE", message);
            // Запускаем SecondActivity
            startActivity(intent);
        });
    }
}