package ru.mirea.hristov.internalfilestorage;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private static final String FILE_NAME = "mirea.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editText = findViewById(R.id.editText);
        Button buttonSave = findViewById(R.id.buttonSave);
        TextView textView = findViewById(R.id.textView); // Добавляем ссылку на TextView

        buttonSave.setOnClickListener(view -> {
            String data = editText.getText().toString();
            if (!data.isEmpty()) {
                saveToFile(data);
                editText.getText().clear(); // Очищаем поле ввода после сохранения данных
                displayFileContents(textView); // Обновляем содержимое TextView после сохранения
            } else {
                Toast.makeText(MainActivity.this, "Введите данные", Toast.LENGTH_SHORT).show();
            }
        });

        displayFileContents(textView); // Отображаем содержимое файла при запуске активности
    }

    private void saveToFile(String data) {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE_NAME, Context.MODE_APPEND);
            String formattedData = data + ";" + System.getProperty("line.separator"); // Добавляем ; и новую строку в конце каждой записи
            fos.write(formattedData.getBytes());
            Toast.makeText(this, "Данные успешно сохранены", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка при сохранении данных", Toast.LENGTH_SHORT).show();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void displayFileContents(TextView textView) {
        try {
            FileInputStream fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            br.close();
            isr.close();
            fis.close();
            textView.setText(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка при чтении данных из файла", Toast.LENGTH_SHORT).show();
        }
    }
}