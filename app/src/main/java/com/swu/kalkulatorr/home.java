
package com.swu.kalkulatorr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {
                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars());

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom);

                    return insets;
                });

        // Tombol Kalkulator
        Button btnKalkulator = findViewById(R.id.button1);

        // tombol tentang
        Button btnAbout = findViewById(R.id.button2);

        // Tombol Riwayat
        Button btnRiwayat = findViewById(R.id.buttonRiwayat);

        // Pindah ke halaman kalkulator
        btnKalkulator.setOnClickListener(v -> {
            Intent intent = new Intent(home.this, MainActivity.class);
            startActivity(intent);
        });

        //Pindah halaman about
        btnAbout.setOnClickListener(v -> {
            Intent intent = new Intent(home.this, about.class);
            startActivity(intent);
        });

        // Pindah ke halaman Riwayat
        btnRiwayat.setOnClickListener(v -> {
            Intent intent = new Intent(home.this, item_riwayat.class);
            startActivity(intent);
        });
    }
}
