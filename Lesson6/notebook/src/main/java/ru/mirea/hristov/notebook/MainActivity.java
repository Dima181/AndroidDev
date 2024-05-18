package ru.mirea.hristov.notebook;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private EditText editTextFileName;
    private EditText editTextQuote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextFileName = findViewById(R.id.editTextFileName);
        editTextQuote = findViewById(R.id.editTextQuote);

        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(v -> saveToFile());

        Button buttonLoad = findViewById(R.id.buttonLoad);
        buttonLoad.setOnClickListener(v -> loadFromFile());
    }

    private void saveToFile() {
        String fileName = editTextFileName.getText().toString().trim();
        String quote = editTextQuote.getText().toString().trim();

        if (!fileName.isEmpty() && !quote.isEmpty()) {
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File file = new File(path, fileName + ".txt");

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(quote.getBytes());
                fileOutputStream.close();
                Log.d("MainActivity", "File saved successfully");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("MainActivity", "Error saving file: " + e.getMessage());
            }
        } else {
            Log.e("MainActivity", "File name or quote is empty");
        }
    }

    private void loadFromFile() {
        String fileName = editTextFileName.getText().toString().trim();

        if (!fileName.isEmpty()) {
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File file = new File(path, fileName + ".txt");

            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                fileInputStream.close();
                editTextQuote.setText(stringBuilder.toString().trim());
                Log.d("MainActivity", "File loaded successfully");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("MainActivity", "Error loading file: " + e.getMessage());
            }
        } else {
            Log.e("MainActivity", "File name is empty");
        }
    }
}