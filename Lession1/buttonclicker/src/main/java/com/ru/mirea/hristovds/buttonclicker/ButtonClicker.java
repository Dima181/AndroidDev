package com.ru.mirea.hristovds.buttonclicker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class ButtonClicker extends AppCompatActivity {

    private TextView tvOut;
    private Button btnWhoAmI;
    private Button btnItIsNotMe;
    private CheckBox Me;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_clicker);
        tvOut = findViewById(R.id.tvOut);
        btnWhoAmI = findViewById(R.id.btnWhoAmI);
        btnItIsNotMe = findViewById(R.id.btnItIsNotMe);
        Me = findViewById(R.id.Me);
        View.OnClickListener oclBtnWhoAmI = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvOut.setText("Мой номер по списку №25");
                Me.setChecked(true);

            }
        };
        btnWhoAmI.setOnClickListener(oclBtnWhoAmI);
    }
    public void onMyButtonClick(View view) {
        Toast.makeText(this, "Another way", Toast.LENGTH_SHORT).show();
        tvOut.setText("Это не я сделал");
        Me.setChecked(false);
    }
}