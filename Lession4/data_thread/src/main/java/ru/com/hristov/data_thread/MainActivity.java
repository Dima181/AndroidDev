package ru.com.hristov.data_thread;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Handler handler = new Handler();

        final Runnable runn1 = new Runnable() {
            @Override
            public void run() {
                // Изменение текста на экране
                // Здесь можно обновить UI элементы вашего макета
                // Пример: binding.tvInfo.setText("runn1");
            }
        };

        final Runnable runn2 = new Runnable() {
            @Override
            public void run() {
                // Изменение текста на экране
                // Пример: binding.tvInfo.setText("runn2");
            }
        };

        final Runnable runn3 = new Runnable() {
            @Override
            public void run() {
                // Изменение текста на экране
                // Пример: binding.tvInfo.setText("runn3");
            }
        };

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(2); // Задержка на 2 секунды
                    handler.post(runn1);
                    TimeUnit.SECONDS.sleep(1); // Задержка на 1 секунду
                    handler.post(runn2);
                    TimeUnit.SECONDS.sleep(2); // Задержка на 2 секунды
                    handler.post(runn3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
}