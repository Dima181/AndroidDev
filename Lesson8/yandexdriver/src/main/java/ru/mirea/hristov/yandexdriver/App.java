package ru.mirea.hristov.yandexdriver;

import android.app.Application;

import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;

public class App extends Application {
    private final String MAPKIT_API_KEY = "cd1d39ce-aeac-4037-8672-f06c2075a020";
    @Override
    public void onCreate() {
        super.onCreate();
        // Set the api key before calling initialize on MapKitFactory.
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
    }
}
