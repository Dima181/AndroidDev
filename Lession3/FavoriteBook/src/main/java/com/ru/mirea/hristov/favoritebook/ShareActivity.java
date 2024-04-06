package com.ru.mirea.hristov.favoritebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class ShareActivity extends AppCompatActivity {
    static final String EXTRA_USER_MESSAGE = "USER_MESSAGE";
    private EditText editTextBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        editTextBook = findViewById(R.id.editTextBook);
    }

    public void sendUserMessage(View view) {
        String userMessage = editTextBook.getText().toString();
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_USER_MESSAGE, userMessage);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}