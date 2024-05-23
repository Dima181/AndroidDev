package ru.mirea.hristov.timeservice;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import ru.mirea.hristov.timeservice.databinding.ActivityMainBinding;
import java.io.BufferedReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private final String host = "time.nist.gov";
    private final int port = 13;
    private static final String TAG = "TimeServiceMainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetTimeTask().execute();
            }
        });
    }

    private class GetTimeTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String timeResult = "";
            try {
                SocketUtils socketUtils = new SocketUtils(host, port);
                try (BufferedReader reader = socketUtils.getReader()) {
                    reader.readLine(); // игнорируем первую строку
                    timeResult = reader.readLine(); // считываем вторую строку
                    Log.d(TAG, timeResult);
                } finally {
                    socketUtils.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error in getting time", e);
            }
            return timeResult;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            binding.textView.setText(result);
        }
    }
}