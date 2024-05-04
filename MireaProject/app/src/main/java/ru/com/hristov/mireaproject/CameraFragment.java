package ru.com.hristov.mireaproject;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraFragment extends Fragment {

    private ImageView imageViewPreview1, imageViewPreview2;
    private Button buttonTakePhoto, buttonCreateCollage;
    private ActivityResultLauncher<Intent> takePictureLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private File photoFile1 = null, photoFile2 = null;
    private boolean isFirstPhoto = true; // Флаг для отслеживания, первое это фото или второе

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        takePhoto();
                    } else {
                        Toast.makeText(getContext(), "Camera permission is required to take photos.", Toast.LENGTH_LONG).show();
                    }
                });

        takePictureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Bitmap bitmap = isFirstPhoto ? BitmapFactory.decodeFile(photoFile1.getAbsolutePath()) : BitmapFactory.decodeFile(photoFile2.getAbsolutePath());
                if (isFirstPhoto) {
                    imageViewPreview1.setImageBitmap(bitmap);
                    isFirstPhoto = false;
                } else {
                    imageViewPreview2.setImageBitmap(bitmap);
                }
            } else {
                Toast.makeText(getContext(), "Failed to take photo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        imageViewPreview1 = view.findViewById(R.id.imageView_preview1);
        imageViewPreview2 = view.findViewById(R.id.imageView_preview2);
        buttonTakePhoto = view.findViewById(R.id.button_takePhoto);
        buttonCreateCollage = view.findViewById(R.id.button_create_collage);

        buttonTakePhoto.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA);
            }
        });

        buttonCreateCollage.setOnClickListener(v -> createCollage());

        return view;
    }

    private void takePhoto() {
        try {
            File photoFile = createImageFile();
            if (isFirstPhoto) {
                photoFile1 = photoFile;
            } else {
                photoFile2 = photoFile;
            }
            Uri photoURI = FileProvider.getUriForFile(requireContext(), "ru.mirea.hristov.mireaproject.fileprovider", photoFile);
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            takePictureLauncher.launch(takePictureIntent);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error while creating image file.", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private void createCollage() {
        if (photoFile1 == null || photoFile2 == null) {
            Toast.makeText(getContext(), "Please take two photos first", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            Bitmap firstImage = BitmapFactory.decodeFile(photoFile1.getAbsolutePath());
            Bitmap secondImage =             BitmapFactory.decodeFile(photoFile2.getAbsolutePath());

            // Определение размеров для нового изображения
            int width = firstImage.getWidth() + secondImage.getWidth();
            int height = Math.max(firstImage.getHeight(), secondImage.getHeight());

            // Создание пустого изображения для коллажа
            Bitmap collageBitmap = Bitmap.createBitmap(width, height, firstImage.getConfig());
            Canvas canvas = new Canvas(collageBitmap);

            // Размещение изображений рядом друг с другом
            canvas.drawBitmap(firstImage, 0f, 0f, null);
            canvas.drawBitmap(secondImage, firstImage.getWidth(), 0f, null);

            // Показываем созданный коллаж в первом ImageView
            imageViewPreview1.setImageBitmap(collageBitmap);

            // Сохраняем коллаж в галерее
            saveImageToGallery(collageBitmap);

            Toast.makeText(getContext(), "Collage created and saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed to create collage", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImageToGallery(Bitmap bitmap) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "collage_" + System.currentTimeMillis() + ".jpg");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try (OutputStream out = getContext().getContentResolver().openOutputStream(uri)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            Toast.makeText(getContext(), "Collage saved to Gallery", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed to save collage", Toast.LENGTH_SHORT).show();
        }
    }
}
