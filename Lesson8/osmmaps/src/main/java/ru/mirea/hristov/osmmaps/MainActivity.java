package ru.mirea.hristov.osmmaps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import ru.mirea.hristov.osmmaps.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    static final private int REQUEST_CODE_PERMISSION = 200;
    private boolean is_permissions_granted = false;
    private ActivityMainBinding binding = null;
    GeoPoint point1 = new GeoPoint(55.794190, 37.701286); // Стромынка
    GeoPoint point2 = new GeoPoint(55.669690, 37.478508); // Вернадка


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.mapView.setZoomRounding(true);
        binding.mapView.setMultiTouchControls(true);
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        MyLocationNewOverlay locationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()), binding.mapView);
        locationNewOverlay.enableMyLocation();
        binding.mapView.getOverlays().add(locationNewOverlay);

        CompassOverlay compassOverlay = new CompassOverlay(getApplicationContext(), new
                InternalCompassOrientationProvider(getApplicationContext()), binding.mapView);
        compassOverlay.enableCompass();
        binding.mapView.getOverlays().add(compassOverlay);

        final Context context = this.getApplicationContext();
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(binding.mapView);
        scaleBarOverlay.setCentred(true);
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        binding.mapView.getOverlays().add(scaleBarOverlay);


        Marker marker = new Marker(binding.mapView);
        marker.setPosition(new GeoPoint(55.669690, 37.478508));
        marker.setOnMarkerClickListener((marker1, mapView) -> {
            Toast.makeText(getApplicationContext(),"Click", Toast.LENGTH_SHORT).show();
            return true;
        });
        binding.mapView.getOverlays().add(marker);
        marker.setIcon(ResourcesCompat.getDrawable(getResources(), org.osmdroid.library.R.drawable.osm_ic_follow_me_on, null));
        marker.setTitle("Title");


        IMapController mapController = binding.mapView.getController();
        mapController.setZoom(15.0);
        GeoPoint startPoint = new GeoPoint(55.794229, 37.700772);
        mapController.setCenter(startPoint);
        fetchAndDrawRoute(point1, point2);
        MakePermissionsRequest();
    }
    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        binding.mapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        Configuration.getInstance().save(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        binding.mapView.onPause();
    }


    private void MakePermissionsRequest() {
        is_permissions_granted =
                PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) &&
                        PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_NETWORK_STATE) &&
                        PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) &&
                        PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) &&
                        PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) &&
                        PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(!is_permissions_granted) {
            ActivityCompat.requestPermissions(this,
                    new	String[] { android.Manifest.permission.INTERNET,
                            android.Manifest.permission.ACCESS_NETWORK_STATE,
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },	REQUEST_CODE_PERMISSION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i("", "onRequestPermissionsResult: " + requestCode);
        if(requestCode == REQUEST_CODE_PERMISSION) {
            is_permissions_granted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
        } else {
            finish();
        }
    }

    private void fetchAndDrawRoute(GeoPoint start, GeoPoint end) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                JSONArray paths = getJsonArray(start, end);
                runOnUiThread(() -> {
                    try {
                        for (int i = 0; i < paths.length(); i++) {
                            JSONObject path = paths.getJSONObject(i);
                            String pointsEncoded = path.getString("points");  // Получаем закодированную строку полилинии
                            List<GeoPoint> geoPoints = decodePoly(pointsEncoded);  // Декодируем полилинию
                            Polyline line = new Polyline();  // Создаем полилинию для отображения маршрута
                            line.setPoints(geoPoints);
                            binding.mapView.getOverlays().add(line);
                        }
                        binding.mapView.invalidate();  // Обновляем карту
                    } catch (JSONException e) {
                        Log.e("MapError", "Failed to parse the route", e);
                    }
                });
            } catch (Exception e) {
                Log.e("HTTPError", "Failed to fetch data", e);
            }
        });
    }

    // Метод для декодирования полилинии
    public List<GeoPoint> decodePoly(String encoded) {
        List<GeoPoint> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            GeoPoint p = new GeoPoint(((double) lat / 1E5), ((double) lng / 1E5));
            poly.add(p);
        }
        return poly;
    }


    @NonNull
    private static JSONArray getJsonArray(GeoPoint start, GeoPoint end) throws IOException, JSONException {
        String graphHopperKey = "612d12d3-4093-4f55-8a24-72c181a9fdd0";
        String vehicle = "car";
        URL url = new URL("https://graphhopper.com/api/1/route?point=" + start.getLatitude() + "," + start.getLongitude() + "&point=" + end.getLatitude() + "," + end.getLongitude() + "&vehicle=" + vehicle + "&key=" + graphHopperKey);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            JSONObject jsonResponse = new JSONObject(result.toString());
            return jsonResponse.getJSONArray("paths");
        } finally {
            connection.disconnect();
        }
    }
}