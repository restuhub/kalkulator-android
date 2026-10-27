
package com.swu.kalkulatorr;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText editNum1, editNum2;
    private Button buttonPlus, buttonMin, buttonMulti, buttonDiv;
    private Button buttonCalc, buttonClear;
    private TextView textResult;

    private String operatorTerpilih = "";
    private double hasilTerakhir = 0;
    private boolean sudahMenghitung = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        View mainView = findViewById(R.id.main);

        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(
                    mainView, (v, insets) -> {
                        Insets systemBars = insets.getInsets(
                                WindowInsetsCompat.Type.systemBars()
                        );

                        v.setPadding(
                                systemBars.left,
                                systemBars.top,
                                systemBars.right,
                                systemBars.bottom
                        );

                        return insets;
                    }
            );
        }

        editNum1 = findViewById(R.id.editNum1);
        editNum2 = findViewById(R.id.editNum2);

        buttonPlus = findViewById(R.id.buttonPlus);
        buttonMin = findViewById(R.id.buttonMin);
        buttonMulti = findViewById(R.id.buttonMulti);
        buttonDiv = findViewById(R.id.buttonDiv);

        buttonCalc = findViewById(R.id.buttonCalc);
        buttonClear = findViewById(R.id.buttonClear);

        textResult = findViewById(R.id.textResult);

        buttonPlus.setOnClickListener(v -> pilihOperator("+"));
        buttonMin.setOnClickListener(v -> pilihOperator("-"));
        buttonMulti.setOnClickListener(v -> pilihOperator("x"));
        buttonDiv.setOnClickListener(v -> pilihOperator(":"));

        buttonCalc.setOnClickListener(v -> hitungHasil());
        buttonClear.setOnClickListener(v -> clear());
    }

    private void pilihOperator(String operator) {
        operatorTerpilih = operator;
        sudahMenghitung = false;

        Toast.makeText(
                this,
                "Operator " + operator + " dipilih",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void hitungHasil() {
        String input1 = editNum1.getText().toString().trim();
        String input2 = editNum2.getText().toString().trim();

        if (input1.isEmpty() || input2.isEmpty()) {
            Toast.makeText(
                    this,
                    "Mohon isi kedua angka terlebih dahulu",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (operatorTerpilih.isEmpty()) {
            Toast.makeText(
                    this,
                    "Mohon pilih operator terlebih dahulu",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        double angka1;
        double angka2;

        try {
            angka1 = Double.parseDouble(input1.replace(',', '.'));
            angka2 = Double.parseDouble(input2.replace(',', '.'));
        } catch (NumberFormatException e) {
            Toast.makeText(
                    this,
                    "Input harus berupa angka yang valid",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (!Double.isFinite(angka1) || !Double.isFinite(angka2)) {
            Toast.makeText(
                    this,
                    "Angka yang dimasukkan tidak valid",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        double hasil;

        switch (operatorTerpilih) {
            case "+":
                hasil = angka1 + angka2;
                break;

            case "-":
                hasil = angka1 - angka2;
                break;

            case "x":
                hasil = angka1 * angka2;
                break;

            case ":":
                if (angka2 == 0) {
                    Toast.makeText(
                            this,
                            "Tidak dapat membagi dengan angka nol!",
                            Toast.LENGTH_SHORT
                    ).show();

                    textResult.setText("Error");
                    return;
                }

                hasil = angka1 / angka2;
                break;

            default:
                return;
        }

        if (!Double.isFinite(hasil)) {
            Toast.makeText(
                    this,
                    "Hasil perhitungan tidak valid",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        hasilTerakhir = hasil;
        sudahMenghitung = true;

        String hasilTeks = formatHasil(hasil);
        textResult.setText("Hasil = " + hasilTeks);

        // Simpan riwayat perhitungan
        String catatan = input1 + " "
                + operatorTerpilih + " "
                + input2 + " = " + hasilTeks;

        String riwayatLama = getSharedPreferences(
                "KalkulatorPrefs",
                MODE_PRIVATE
        ).getString("riwayat", "");

        String riwayatBaru = catatan;

        if (!riwayatLama.isEmpty()) {
            riwayatBaru += "\n" + riwayatLama;
        }

        getSharedPreferences(
                "KalkulatorPrefs",
                MODE_PRIVATE
        ).edit()
                .putString("riwayat", riwayatBaru)
                .apply();

        // Jalankan fitur rahasia setelah hasil dihitung
        jalankanFiturTersembunyi(hasil);
    }

    // =========================================
    // FITUR RAHASIA SECRET CALCULATOR
    // Pemicu: 0, 3, 19, 29, 39
    // =========================================

    private void jalankanFiturTersembunyi(double hasil) {
        if (hasil == 0.0) {

            // Hasil 0: membuka kamera
            Intent intentKamera = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE
            );

            bukaIntent(
                    intentKamera,
                    "Kamera tidak tersedia"
            );

        } else if (hasil == 3.0) {

            // Hasil 3: memutar audio
            putarAudio();

        } else if (hasil == 19.0) {

            // Hasil 19: menampilkan gambar
            bukaActivity(
                    ImageActivity.class,
                    "ImageActivity belum tersedia"
            );

        } else if (hasil == 29.0) {

            // Hasil 29: membuka GPS / Google Maps
            tampilkanGPS();

        } else if (hasil == 39.0) {

            // Hasil 39: accelerometer dan gyroscope
            bukaActivity(
                    SensorActivity.class,
                    "SensorActivity belum tersedia"
            );
        }
    }

    // Membuka Activity
    private void bukaActivity(
            Class<?> activityClass,
            String pesanError
    ) {
        Intent intent = new Intent(
                MainActivity.this,
                activityClass
        );

        bukaIntent(intent, pesanError);
    }

    // Membuka Intent dengan penanganan error
    private void bukaIntent(
            Intent intent,
            String pesanError
    ) {
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(
                    this,
                    pesanError,
                    Toast.LENGTH_SHORT
            ).show();
        } catch (SecurityException e) {
            Toast.makeText(
                    this,
                    "Izin tidak tersedia untuk fitur ini",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    // Memutar audio dari res/raw/audio.mp3
    private void putarAudio() {
        MediaPlayer mediaPlayer = MediaPlayer.create(
                this,
                R.raw.audio
        );

        if (mediaPlayer == null) {
            Toast.makeText(
                    this,
                    "Audio tidak ditemukan. Periksa res/raw/audio.mp3",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        mediaPlayer.setOnCompletionListener(
                MediaPlayer::release
        );

        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
            mp.release();

            Toast.makeText(
                    MainActivity.this,
                    "Gagal memutar audio",
                    Toast.LENGTH_SHORT
            ).show();

            return true;
        });

        mediaPlayer.start();

        Toast.makeText(
                this,
                "Memutar audio",
                Toast.LENGTH_SHORT
        ).show();
    }

    // Membuka Google Maps
    private void tampilkanGPS() {
        Uri uriMaps = Uri.parse(
                "https://maps.google.com/?q=lokasi+saya"
        );

        Intent intentMaps = new Intent(
                Intent.ACTION_VIEW,
                uriMaps
        );

        try {
            startActivity(intentMaps);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(
                    this,
                    "Tidak ada aplikasi untuk membuka peta",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    // Format hasil agar bilangan bulat tidak memiliki desimal
    private String formatHasil(double hasil) {
        if (hasil == Math.rint(hasil)
                && Math.abs(hasil) < 1.0E15) {
            return String.valueOf((long) hasil);
        }

        DecimalFormatSymbols simbol =
                DecimalFormatSymbols.getInstance(Locale.US);

        DecimalFormat format =
                new DecimalFormat("0.##########", simbol);

        return format.format(hasil);
    }

    // Membersihkan kalkulator
    private void clear() {
        editNum1.setText("");
        editNum2.setText("");
        textResult.setText("");

        operatorTerpilih = "";
        hasilTerakhir = 0;
        sudahMenghitung = false;

        editNum1.requestFocus();
    }
}
