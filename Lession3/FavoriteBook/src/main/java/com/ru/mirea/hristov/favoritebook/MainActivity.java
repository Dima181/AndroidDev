package com.ru.mirea.hristov.favoritebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> activityResultLauncher;
    static final String KEY = "book_name";
    static final String USER_MESSAGE = "MESSAGE";
    private TextView textViewUserBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewUserBook = findViewById(R.id.textViewBook);

        // Находим кнопку по ее id
        Button buttonSend = findViewById(R.id.buttonSend);

        // Устанавливаем обработчик нажатия на кнопку
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Вызываем метод getInfoAboutBook, который запускает ShareActivity
                getInfoAboutBook(v);
            }
        });

        ActivityResultCallback<ActivityResult> callback = new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.hasExtra(ShareActivity.EXTRA_USER_MESSAGE)) {
                        String userMessage = data.getStringExtra(ShareActivity.EXTRA_USER_MESSAGE);
                        // Добавляем префикс к полученной строке
                        userMessage = "Название Вашей любимой книги: " + userMessage;
                        textViewUserBook.setText(userMessage);
                    }
                }
            }
        };

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                callback);
    }

    public void getInfoAboutBook(View view) {
        Intent intent = new Intent(this, ShareActivity.class);
        intent.putExtra(KEY, "Анабасис");
        activityResultLauncher.launch(intent);
    }
}