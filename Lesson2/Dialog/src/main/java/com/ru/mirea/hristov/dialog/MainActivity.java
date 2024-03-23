package com.ru.mirea.hristov.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements AlertDialogFragment.DialogClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Метод для вызова диалогового окна AlertDialog
    public void onClickShowDialog(View view) {
        AlertDialogFragment alertDialog = new AlertDialogFragment();
        alertDialog.show(getSupportFragmentManager(), "dialog");
    }

    // Метод для вызова диалогового окна TimePickerDialog
    public void onClickShowTimeDialog(View view) {
        MyTimeDialogFragment timeDialog = new MyTimeDialogFragment();
        timeDialog.show(getSupportFragmentManager(), "time_dialog");
    }

    // Метод для вызова диалогового окна DatePickerDialog
    public void onClickShowDateDialog(View view) {
        MyDateDialogFragment dateDialog = new MyDateDialogFragment();
        dateDialog.show(getSupportFragmentManager(), "date_dialog");
    }

    // Метод для вызова диалогового окна ProgressDialog
    public void onClickShowProgressDialog(View view) {
        MyProgressDialogFragment progressDialog = new MyProgressDialogFragment();
        progressDialog.show(getSupportFragmentManager(), "progress_dialog");
    }

    @Override
    public void onOkClicked() {
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку \"Иду дальше\"!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCancelClicked() {
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку \"Нет\"!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNeutralClicked() {
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку \"На паузе\"!", Toast.LENGTH_LONG).show();
    }
}
