package com.example.profilui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.profilui.models.user;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class ActivityEditProfil extends AppCompatActivity {
    public DatabaseReference dbref= FirebaseDatabase.getInstance().getReference("/USER");
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;

    EditText nama, tempatlahir, tanggallahir, nohp, nik, nokk, email, gender;
    Button simpan, batal;
    ImageView fotoprofil;

    private Uri filePath;
    private String fotoUrl;

    private static final int IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        fotoprofil = findViewById(R.id.imageprofil);
        nama = findViewById(R.id.edit_nama);
        tempatlahir = findViewById(R.id.edit_tempatlahir);
        tanggallahir = findViewById(R.id.edit_tgllahir);
        nohp = findViewById(R.id.edit_nohp);
        nik = findViewById(R.id.edit_nik);
        nokk = findViewById(R.id.edit_nokk);
        email = findViewById(R.id.edit_email);
        simpan = findViewById(R.id.btn_simpan_profil);
        batal = findViewById(R.id.btn_batal_profil);

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        fotoprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ambilGambar();
            }

        });
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
                user user = new user();
                user.setNama(nama.getText().toString());
                user.setNohp(nohp.getText().toString());
                user.setTempatlahir(tempatlahir.getText().toString());
                user.setTgllahir(tanggallahir.getText().toString());
                user.setNik(nik.getText().toString());
                user.setNokk(nokk.getText().toString());
                user.setEmail(email.getText().toString());
                user.setGender(gender.getText().toString());
                user.setFoto(fotoUrl);

               dbref.child("USER").push().setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        nama.setText("");
                        Toast.makeText(ActivityEditProfil.this, "Data Berhasil Disimpan", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ActivityEditProfil.this, "Data Gagal Disimpan", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityEditProfil.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


    private void ambilGambar() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null &&
                data.getData() != null) {
            filePath = data.getData();
            Picasso.get().load(filePath).fit().centerInside().into(fotoprofil);
        } else {
            Toast.makeText(this, "Tidak ada gambar dipilih", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage() {
        if (filePath != null) {
            final StorageReference ref = storageReference.child(nama.getText().toString());
            UploadTask uploadTask = ref.putFile(filePath);

            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(task -> {
                Uri imagePath = task.getResult();
                fotoUrl = imagePath.toString();
            });
        }
    }
}
