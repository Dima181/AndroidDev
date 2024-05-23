package ru.mirea.hristov.yandexmaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import ru.mirea.hristov.yandexmaps.databinding.ActivityMainBinding;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CompositeIcon;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.RotationType;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;

public class MainActivity extends AppCompatActivity implements UserLocationObjectListener {
    static final private int REQUEST_CODE_PERMISSION = 200;
    private boolean is_permissions_granted = false;
    private UserLocationLayer userLocationLayer = null;
    private ActivityMainBinding binding = null;
    static final private int REQUEST_CODE_PERMISSION_BACKGROUND_LOCATION = 201;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MapKitFactory.initialize(this);
        binding.mapview.getMap().move(
                new CameraPosition(new Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 1),
                null
        );
    }
    @Override
    protected void onStart() {
        super.onStart();
        binding.mapview.onStart();
        MapKitFactory.getInstance().onStart();
        MakePermissionsRequest();
    }
    @Override
    protected void onStop() {
        super.onStop();
        binding.mapview.onStop();
        MapKitFactory.getInstance().onStop();
    }

    @Override
    public void onObjectAdded(@NonNull UserLocationView userLocationView) {
        Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show();
        userLocationLayer.setAnchor(
                new PointF((float)(binding.mapview.getWidth() * 0.5),
                        (float)(binding.mapview.getHeight() * 0.5)),
                new PointF((float)(binding.mapview.getWidth() * 0.5),
                        (float)(binding.mapview.getHeight() * 0.83)));
        // При определении направления движения устанавливается следующая иконка
        userLocationView.getArrow().setIcon(ImageProvider.fromResource(
                this, android.R.drawable.arrow_up_float));

        // При получении координат местоположения устанавливается следующая иконка
        CompositeIcon pinIcon = userLocationView.getPin().useCompositeIcon();
        pinIcon.setIcon("pin",
                ImageProvider.fromResource(this, R.drawable.ic_launcher_foreground),
                new IconStyle().setAnchor(new PointF(0.5f, 0.5f))
                        .setRotationType(RotationType.ROTATE)
                        .setZIndex(1f)
                        .setScale(0.5f)
        );

        userLocationView.getAccuracyCircle().setFillColor(Color.BLUE & 0x99ffffff);
    }
    @Override
    public void onObjectRemoved(@NonNull UserLocationView userLocationView) {

    }
    @Override
    public void onObjectUpdated(@NonNull UserLocationView userLocationView, @NonNull ObjectEvent objectEvent) {

    }

    private void loadUserLocationLayer(){
        MapKit mapKit = MapKitFactory.getInstance();
        mapKit.resetLocationManagerToDefault();

        userLocationLayer = mapKit.createUserLocationLayer(binding.mapview.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(true);
        userLocationLayer.setObjectListener(this);
    }



    private void MakePermissionsRequest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            final boolean fineLocationGranted =
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

            final boolean backgroundLocationGranted =
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;

            if (!fineLocationGranted) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_PERMISSION);
            } else if (!backgroundLocationGranted) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                        REQUEST_CODE_PERMISSION_BACKGROUND_LOCATION);
            } else {
                is_permissions_granted = true;
                loadUserLocationLayer();
            }
        } else {
            // Для Android 10 и ниже, где фоновое разрешение не требуется отдельно
            final boolean permissionsGranted =
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

            if (!permissionsGranted) {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_CODE_PERMISSION);
            } else {
                is_permissions_granted = true;
                loadUserLocationLayer();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // После получения основного разрешения местоположения, проверяем и запрашиваем фоновое разрешение
                MakePermissionsRequest();
            } else {
                Toast.makeText(this, "Разрешение на местоположение не предоставлено", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CODE_PERMISSION_BACKGROUND_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                is_permissions_granted = true;
                loadUserLocationLayer();
            } else {
                Toast.makeText(this, "Фоновое разрешение на местоположение не предоставлено", Toast.LENGTH_SHORT).show();
            }
        }
    }

}