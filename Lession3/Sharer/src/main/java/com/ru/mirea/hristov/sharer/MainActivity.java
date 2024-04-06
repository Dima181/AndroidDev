package com.ru.mirea.hristov.sharer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Находим кнопку по идентификатору
        Button buttonSend = findViewById(R.id.button_send);
        // Устанавливаем обработчик нажатия на кнопку
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создаем Intent для отправки сообщения
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                // Устанавливаем тип данных
                intent.setType("*/*");
                // Добавляем текст сообщения
                intent.putExtra(Intent.EXTRA_TEXT, "Mirea");
                // Создаем диалоговое окно выбора приложения и принудительно отображаем его
                Intent chooser = Intent.createChooser(intent, "Выберите приложение");
                // Запускаем диалоговое окно выбора приложения для отправки сообщения
                startActivity(chooser);
            }
        });
    }
}
