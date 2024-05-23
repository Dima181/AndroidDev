package ru.com.hristov.mireaproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

public class PlacesFragment extends Fragment {

    private MapView mapView;
    private EditText searchField;
    List<Place> places = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue("ru.com.hristov.mireaproject");

        // Добавление адреса в каждый объект Place
        places.add(new Place("Рту МИРЭА", "Главный корпус", "г. Москва, ул. Вернадского, 78", new GeoPoint(55.669690, 37.478508)));
        places.add(new Place("Рту МИРЭА", "Филиал ВУЗа", "г. Москва, ул. Стромынка, 20", new GeoPoint(55.794190, 37.701286)));
        // Добавьте другие заведения по аналогии
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places, container, false);
        mapView = view.findViewById(R.id.map);
        searchField = view.findViewById(R.id.search_field);

        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        IMapController mapController = mapView.getController();
        mapController.setZoom(9.5);
        GeoPoint startPoint = new GeoPoint(55.7558, 37.6173); // Примерно центр Москвы
        mapController.setCenter(startPoint);

        // Добавляем маркеры на карту
        addMarkersToMap();

        // Обработчик для поиска заведений
        searchField.setOnEditorActionListener((v, actionId, event) -> {
            String searchText = v.getText().toString();
            searchPlaces(searchText);
            return true;
        });

        return view;
    }

    private void addMarkersToMap() {
        for (Place place : places) {
            addMarker(place.getLocation(), place.getName(), place.getDescription(), place.getAddress());
        }
    }


    private void addMarker(GeoPoint point, String title, String description, String address) {
        Marker marker = new Marker(mapView);
        marker.setPosition(point);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle(title + "\n" + description + "\nАдрес: " + address); // Добавление адреса в заголовок маркера
        mapView.getOverlays().add(marker);
    }


    private void searchPlaces(String query) {
        mapView.getOverlays().clear(); // Очищаем все маркеры на карте

        if (query.isEmpty()) {
            // Если строка поиска пуста, добавляем все маркеры обратно
            addMarkersToMap();
        } else {
            // Фильтруем список заведений по запросу и отображаем только соответствующие
            for (Place place : places) {
                if (place.getName().toLowerCase().contains(query.toLowerCase())) {
                    addMarker(place.getLocation(), place.getName(), place.getDescription(), place.getAddress());
                }
            }
        }

        mapView.invalidate(); // Обновляем карту для отображения изменений
    }



    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
}