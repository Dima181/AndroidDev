package ru.mirea.hristov.firebaseauth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView userEmailTextView;
    private TextView userEmailVerifiedTextView;
    private TextView userUidTextView;
    private Button verifyEmailButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        // Инициализация TextViews и Button
        userEmailTextView = findViewById(R.id.userEmailTextView);
        userEmailVerifiedTextView = findViewById(R.id.userEmailVerifiedTextView);
        userUidTextView = findViewById(R.id.userUidTextView);
        verifyEmailButton = findViewById(R.id.verifyEmailButton);

        // Получение информации о текущем пользователе
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            userEmailTextView.setText(getString(R.string.email_template, user.getEmail()));
            userUidTextView.setText(getString(R.string.uid_template, user.getUid()));
            userEmailVerifiedTextView.setText(getString(R.string.verified_template, user.isEmailVerified() ? getString(R.string.yes) : getString(R.string.no)));

            if (!user.isEmailVerified()) {
                verifyEmailButton.setVisibility(View.VISIBLE);
            }
        }
    }

    public void signOut(View view) {
        mAuth.signOut();
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        finish();
    }

    public void sendVerificationEmail(View view) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && !user.isEmailVerified()) {
            user.sendEmailVerification().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(HomeActivity.this, getString(R.string.verification_email_sent), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HomeActivity.this, getString(R.string.failed_to_send_verification_email), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
