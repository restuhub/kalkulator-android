
package com.swu.kalkulatorr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class ImageActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button buttonPilihGambar;

    private final ActivityResultLauncher<Intent> pilihGambarLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK
                                && result.getData() != null) {

                            Uri imageUri = result.getData().getData();

                            if (imageUri != null) {
                                imageView.setImageURI(imageUri);
                            } else {
                                Toast.makeText(
                                        this,
                                        "Gambar tidak ditemukan",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_image);

        imageView = findViewById(R.id.imageView);
        buttonPilihGambar = findViewById(R.id.buttonPilihGambar);

        buttonPilihGambar.setOnClickListener(v -> bukaPemilihGambar());
    }

    private void bukaPemilihGambar() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        pilihGambarLauncher.launch(intent);
    }
}
