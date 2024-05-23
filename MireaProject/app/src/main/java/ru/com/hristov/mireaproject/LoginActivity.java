package ru.com.hristov.mireaproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
    }

    public void signUp(View view) {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Email and password cannot be empty.", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Уведомляем пользователя о отправке верификационного письма
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            user.sendEmailVerification()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Toast.makeText(LoginActivity.this, "Verification email sent. Please check your email.", Toast.LENGTH_LONG).show();
                                        } else {
                                            // Логируем ошибку отправки письма
                                            Log.e("EmailVerification", "sendEmailVerification", task1.getException());
                                            Toast.makeText(LoginActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        // Логируем ошибку регистрации
                        Log.e("SignUp", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Registration failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void signIn(View view) {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Email and password cannot be empty.", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            if (user.isEmailVerified()) {
                                // Переход на главный экран приложения
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            } else {
                                // Показать сообщение о необходимости верификации
                                Toast.makeText(LoginActivity.this, "Email is not verified. Check your inbox or verify your email.", Toast.LENGTH_LONG).show();
                                // Переход на экран с информацией о пользователе
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                            finish();
                        }
                    } else {
                        // Ошибка аутентификации
                        Toast.makeText(LoginActivity.this, "Authentication failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
