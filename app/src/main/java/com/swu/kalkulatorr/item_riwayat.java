
package com.swu.kalkulatorr;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class item_riwayat extends AppCompatActivity {

    TextView textRiwayat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_item_riwayat);

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

        textRiwayat = findViewById(R.id.textRiwayat);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String riwayat = getSharedPreferences(
                "KalkulatorPrefs", MODE_PRIVATE
        ).getString("riwayat", "");

        if (riwayat.isEmpty()) {
            textRiwayat.setText(R.string.belum_ada_riwayat_perhitungan);
        } else {
            textRiwayat.setText(riwayat);
        }
    }
}
