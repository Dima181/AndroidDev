package ru.mirea.hristov.firebaseauth;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Проверка, вошел ли пользователь
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Пользователь вошел, переходим на главный экран
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
        } else {
            // Пользователь не вошел, переходим на экран логина
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        finish();
    }
}