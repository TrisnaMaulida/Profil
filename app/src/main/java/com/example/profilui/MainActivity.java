package com.example.profilui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.profilui.models.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends Activity {

    public DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();

    TextView txtnama, txtstatus;
    EditText etnama, ettempatlahir, ettgllahir, etnohp, etnik, etnokk, etemail, etjk;
    Button ubahpass, keluar, editprofil;
    CircleImageView fotoprofile;
    private String imageUri;
    private static final int IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fotoprofile = findViewById(R.id.image_profil);
        txtnama = findViewById(R.id.name);
        txtstatus = findViewById(R.id.status);
        etnama = findViewById(R.id.et_nama);
        ettempatlahir = findViewById(R.id.et_tempatlahir);
        ettgllahir = findViewById(R.id.et_tgllahir);
        etnohp = findViewById(R.id.et_nohp);
        etnik = findViewById(R.id.et_nik);
        etnokk = findViewById(R.id.et_nokk);
        etemail = findViewById(R.id.et_email);
        etjk = findViewById(R.id.et_gender);
        ubahpass = findViewById(R.id.btn_ubah_pass);
        keluar = findViewById(R.id.btn_keluar);
        editprofil = findViewById(R.id.btn_edit_profil);
        readData();

        ubahpass.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, UbahPassword.class);
            startActivity(intent);
        });

        editprofil.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ActivityEditProfil.class);
            startActivity(intent);
        });
    }

    /**
     * Ubah Firestore ke realtime database
     *
     * @param {}
     * @return show data
     */
    private void readData() {
        dbref.child("USER")
                .child(Constant.idUser)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            user user = snapshot.getValue(user.class);
                            assert user != null;
                            etnama.setText(user.getNama());
                            ettempatlahir.setText(user.getTempatlahir());
                            ettgllahir.setText(user.getTgllahir());
                            etnohp.setText(user.getNohp());
                            etnik.setText(user.getNik());
                            etnokk.setText(user.getNokk());
                            etemail.setText(user.getEmail());
                            imageUri = user.getFoto();
                            etjk.setText(user.getGender());
                            if (imageUri != "") {
                                Picasso.get().load(imageUri).fit().into(fotoprofile);
                            } else {
                                Picasso.get().load(R.drawable.profile_image).fit().into(fotoprofile);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this, "Gagal mengambil data", Toast.LENGTH_LONG).show();
                        ;
                    }
                });

    }
}



