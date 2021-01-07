package com.example.profilui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityEditProfil extends AppCompatActivity {
    public DatabaseReference dbref= FirebaseDatabase.getInstance().getReference("/USER");
    private StorageReference storageReference;

    private EditText nama, tempatlahir, tanggallahir, nohp, nik, nokk, email, gender;
    private Button simpan, batal;
    private CircleImageView fotoprofil;

    private Uri imageUri;
    private String myUri = "";


    private Uri filePath;
    private String fotoUrl;
    private boolean uploadSuccess;


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

        storageReference = FirebaseStorage.getInstance().getReference();

        fotoprofil.setOnClickListener(view -> ambilGambar());
        simpan.setOnClickListener(view -> {
            //cek
            if(checkEditText()){
                uploadImage();


            }else {
                //jika kosong
                Toast.makeText(ActivityEditProfil.this,"Tidak boleh kososng!",Toast.LENGTH_LONG).show();

                user user = new user();
                user.setNama(nama.getText().toString());
                user.setNohp(nohp.getText().toString());
                user.setTempatlahir(tempatlahir.getText().toString());
                user.setTgllahir(tanggallahir.getText().toString());
                user.setNik(nik.getText().toString());
                user.setNokk(nokk.getText().toString());
                user.setEmail(email.getText().toString());
                user.setFoto(myUri);

                if (nama.equals("")) {
                    nama.setError("Silahkan masukan nama");
                    nama.requestFocus();
                } else if (tempatlahir.equals("")) {
                    tempatlahir.setError("Silahkan masukan tempat lahir");
                    tempatlahir.requestFocus();
                } else if (tanggallahir.equals("")) {
                    tanggallahir.setError("Silahkan masukan tanggal lahir");
                    tanggallahir.requestFocus();
                } else if (nohp.equals("")) {
                    nohp.setError("Silahkan masukan nomor handphone");
                    nohp.requestFocus();
                } else if (nik.equals("")) {
                    nik.setError("Silahkan masukan NIK");
                    nik.requestFocus();
                } else if (nokk.equals("")) {
                    nokk.setError("Silahkan masukan No KK");
                    nokk.requestFocus();
                } else if (email.equals("")) {
                    email.setError("Silahkan masukan email");
                    email.requestFocus();
                }


                dbref.child("USER").push().setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void Void) {
                        nama.setText("");
                        tempatlahir.setText("");
                        tanggallahir.setText("");
                        nohp.setText("");
                        nik.setText("");
                        nokk.setText("");
                        email.setText("");
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
        batal.setOnClickListener(view -> {
            Intent intent = new Intent(ActivityEditProfil.this, MainActivity.class);
            startActivity(intent);
        });
    }
/**
 * Cek apakah sudah diisi atau belum
 * @param {}
 * @return true/false (harusnya semua tidak boleh kosong == true)
 * */
    private boolean checkEditText(){
        return !TextUtils.isEmpty(nama.getText().toString()) &&
                !TextUtils.isEmpty(nohp.getText().toString()) &&
                !TextUtils.isEmpty(tempatlahir.getText().toString()) &&
                !TextUtils.isEmpty(tanggallahir.getText().toString()) &&
                !TextUtils.isEmpty(nik.getText().toString()) &&
                !TextUtils.isEmpty(nokk.getText().toString()) &&
                !TextUtils.isEmpty(email.getText().toString()) &&
                !TextUtils.isEmpty(gender.getText().toString()) &&
                !TextUtils.isEmpty(fotoUrl);
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
            imageUri = data.getData();
            Picasso.get().load(imageUri).fit().centerInside().into(fotoprofil);
        } else {
            Toast.makeText(this, "Tidak ada gambar dipilih", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage() {
        if (imageUri != null) {
            final StorageReference ref = storageReference.child("user"+firebaseFirestore.toString());
            UploadTask uploadTask = ref.putFile(imageUri);


            Task<Uri> uriTask = uploadTask.continueWithTask(task -> ref.getDownloadUrl()).addOnCompleteListener(task -> {

            }).addOnSuccessListener(uri -> uploadDataUser(uri.toString()));
        }else {
            Toast.makeText(ActivityEditProfil.this,"Pilih Gambar dulu",Toast.LENGTH_LONG).show();

            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(task -> {
                Uri imagePath = task.getResult();
                myUri = imagePath.toString();
            });

        }
    }

/**
 * upload to database
 * @param imagepath
 *
 *
 * */
    private void uploadDataUser(String imagepath){
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

        dbref.child("USER").push().setValue(user).addOnSuccessListener(aVoid -> {
            nama.setText("");
            Toast.makeText(ActivityEditProfil.this, "Data Berhasil Disimpan", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ActivityEditProfil.this, "Data Gagal Disimpan", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
