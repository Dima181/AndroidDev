package ru.mirea.hristov.employeedb;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppDatabase db = App.getInstance().getDatabase();
        EmployeeDao employeeDao = db.employeeDao();

        // Создание нового супергероя
        Employee superhero = new Employee();
        superhero.name = "Robert Dauni Junior";
        superhero.alias = "Iron Man";
        superhero.superPower = "Iron suit";
        employeeDao.insert(superhero);

        // Получение и вывод всех супергероев
        List<Employee> superheroes = employeeDao.getAll();
        for (Employee hero : superheroes) {
            Log.d(TAG, "Superhero: " + hero.name + ", Alias: " + hero.alias + ", Powers: " + hero.superPower);
        }
        TextView superheroInfoTextView = findViewById(R.id.superheroInfoTextView);
        String info = "Superhero: Robert Dauni Junior, Alias: Iron Man, Powers: Iron suit";
        superheroInfoTextView.setText(info);
    }
}