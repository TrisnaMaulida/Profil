package com.example.profilui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
    TextView txtnama, txtstatus;
    EditText etnama, ettempatlahir, ettgllahir, etnohp, etnik, etnokk, etemail, etjk;
    Button ubahpass, keluar, editprofil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtnama = findViewById(R.id.name);
        txtstatus = findViewById(R.id.status);
        etnama = findViewById(R.id.et_nama);
        ettempatlahir = findViewById(R.id.et_tempatlahir);
        ettgllahir = findViewById(R.id.et_tgllahir);
        etnohp = findViewById(R.id.et_nohp);
        etnik = findViewById(R.id.et_nik);
        etnokk = findViewById(R.id.et_email);
        etjk = findViewById(R.id.et_gender);
        ubahpass = findViewById(R.id.btn_ubah_pass);
        keluar = findViewById(R.id.btn_keluar);
        editprofil = findViewById(R.id.btn_edit_profil);

        ubahpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( MainActivity.this, UbahPassword.class);
                startActivity(intent);
            }
        });
        editprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( MainActivity.this, ActivityEditProfil.class);
                startActivity(intent);
            }
        });

    }
}
