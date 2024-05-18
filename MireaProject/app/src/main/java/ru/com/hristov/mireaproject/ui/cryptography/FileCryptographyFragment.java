package ru.com.hristov.mireaproject.ui.cryptography;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import ru.com.hristov.mireaproject.R;

public class FileCryptographyFragment extends Fragment {

    private static final int REQUEST_CODE_CHOOSE_FILE = 100;
    private Uri fileUri;
    private FileCryptographyViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FileCryptographyViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_cryptography, container, false);

        Button chooseFileButton = view.findViewById(R.id.buttonChooseFile);
        EditText encryptionKeyEditText = view.findViewById(R.id.editTextEncryptionKey);
        Button encryptButton = view.findViewById(R.id.buttonEncrypt);
        Button decryptButton = view.findViewById(R.id.buttonDecrypt);
        FloatingActionButton fabCreateRecord = view.findViewById(R.id.fab_create_record);

        chooseFileButton.setOnClickListener(v -> chooseFile());
        encryptButton.setOnClickListener(v -> {
            if (fileUri != null && encryptionKeyEditText.getText().length() > 0) {
                viewModel.encryptFile(getContext(), fileUri, encryptionKeyEditText.getText().toString());
            } else {
                Toast.makeText(getContext(), "Please choose a file and enter an encryption key.", Toast.LENGTH_LONG).show();
            }
        });

        decryptButton.setOnClickListener(v -> {
            if (fileUri != null && encryptionKeyEditText.getText().length() > 0) {
                viewModel.decryptFile(getContext(), fileUri, encryptionKeyEditText.getText().toString());
            } else {
                Toast.makeText(getContext(), "Please choose a file and enter a decryption key.", Toast.LENGTH_LONG).show();
            }
        });

        fabCreateRecord.setOnClickListener(v -> Toast.makeText(getContext(), "FAB clicked. Implement action here.", Toast.LENGTH_SHORT).show());

        return view;
    }

    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_CODE_CHOOSE_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE_FILE && resultCode == Activity.RESULT_OK && data != null) {
            fileUri = data.getData();
            Toast.makeText(getContext(), "File selected: " + fileUri.getPath(), Toast.LENGTH_LONG).show();
        }
    }
}