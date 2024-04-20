package ru.com.hristov.lession4;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import ru.com.hristov.lession4.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding; // Объявление переменной для привязки макета

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Создание объекта ActivityMainBinding и привязка макета activity_main.xml к коду активности
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        // Установка корневого элемента макета как содержимого активности
        setContentView(binding.getRoot());

        // Пример использования binding для доступа к элементам макета
        binding.editTextMirea.setText("Мой номер по списку №25");
        binding.buttonMirea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(MainActivity.class.getSimpleName(),"onClickListener");
            }
        });
    }
}