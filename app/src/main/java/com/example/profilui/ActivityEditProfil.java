package com.example.profilui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.profilui.models.user;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityEditProfil extends AppCompatActivity {
    public DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference;

    private EditText nama, tempatlahir, tanggallahir, nohp, nik, nokk, email;
    private RadioGroup gender;
    private Button simpan, batal;
    private CircleImageView fotoprofil;

    private Uri imageUri;


    private static final int IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        storageReference = FirebaseStorage.getInstance().getReference();


        initId();
        initListener();


    }

    /**
     * Registrasi view
     *
     * @param {}
     * @reurn semua id ter findview
     */
    private void initId() {
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
        gender = findViewById(R.id.rg_gender);
    }

    /**
     * Semua onclick listener disini
     *
     * @param {}
     * @return semua onclicklsitener cek disini
     */
    private void initListener() {
        fotoprofil.setOnClickListener(view -> {
            ambilGambar();
        });
        simpan.setOnClickListener(view -> {
            if (checkEditText()) {
                uploadImage();
            } else {
                Toast.makeText(ActivityEditProfil.this, "Tidak boleh kosong!", Toast.LENGTH_LONG).show();
            }
        });
        batal.setOnClickListener(view -> {
            if (checkEditText()) {
                dialogInfo();
            } else {
                startActivity(new Intent(ActivityEditProfil.this, MainActivity.class));
                finish();
            }

        });
    }

    /**
     * Kasih konfirmasi ke user jika edittext masih ada isinya
     *
     * @param {}
     * @return user menghapus perubahan atau lanjut ?
     */
    private void dialogInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityEditProfil.this);
        builder.create();
        builder.setTitle("Hai");
        builder.setMessage("Perubahan belum disimpan nih");
        builder.setCancelable(true);
        builder.setPositiveButton("Buang", (dialog, which) -> {

            startActivity(new Intent(ActivityEditProfil.this, MainActivity.class));
            finish();

        });
        builder.setNegativeButton("Batal", (dialog, which) -> dialog.dismiss());
    }

    /**
     * Cek apakah sudah diisi atau belum
     *
     * @param {}
     * @return true/false (harusnya semua tidak boleh kosong == true)
     */
    private boolean checkEditText() {

        return !TextUtils.isEmpty(nama.getText().toString()) &&
                !TextUtils.isEmpty(nohp.getText().toString()) &&
                !TextUtils.isEmpty(tempatlahir.getText().toString()) &&
                !TextUtils.isEmpty(tanggallahir.getText().toString()) &&
                !TextUtils.isEmpty(nik.getText().toString()) &&
                !TextUtils.isEmpty(nokk.getText().toString()) &&
                !TextUtils.isEmpty(email.getText().toString()) &&
                !TextUtils.isEmpty(getJenisKelamin()) &&
                !TextUtils.isEmpty(imageUri.toString());
    }

    /**
     * Ambil RadioButton terpilih
     *
     * @param {}
     * @return Jenis Kelamin yang dipilih
     */
    private String getJenisKelamin() {
        RadioButton radioButton = findViewById(gender.getCheckedRadioButtonId());
        return radioButton.getText().toString();
    }


    /**
     * upload to database
     *
     * @param imagepath
     */
    private void uploadDataUser(String imagepath) {
        user user = new user();
        RadioButton radioButton = findViewById(gender.getCheckedRadioButtonId());
        user.setNama(nama.getText().toString());
        user.setNohp(nohp.getText().toString());
        user.setTempatlahir(tempatlahir.getText().toString());
        user.setTgllahir(tanggallahir.getText().toString());
        user.setNik(nik.getText().toString());
        user.setNokk(nokk.getText().toString());
        user.setEmail(email.getText().toString());
        user.setGender(getJenisKelamin());
        user.setFoto(imagepath);

        dbref.child("USER").child(Constant.idUser).setValue(user).addOnSuccessListener(aVoid -> {
            Toast.makeText(ActivityEditProfil.this, "Data Berhasil Disimpan", Toast.LENGTH_SHORT).show();
            clear();
        }).addOnFailureListener(e -> {
            Toast.makeText(ActivityEditProfil.this, "Data Gagal Disimpan", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Kosongkan editetxt
     *
     * @param {}
     * @return editetxt kembali ke ""
     */
    private void clear() {
        nama.setText("");
        nohp.setText("");
        tempatlahir.setText("");
        tanggallahir.setText("");
        nik.setText("");
        nokk.setText("");
        email.setText("");

    }

    private void ambilGambar() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), IMAGE_REQUEST);
    }

    @Override
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
            final StorageReference ref = storageReference.child("USER/" + Constant.idUser + ".jpg");
            UploadTask uploadTask = ref.putFile(imageUri);

            uploadTask.continueWithTask(task -> ref.getDownloadUrl())
                    .addOnFailureListener(e -> {
                        Toast.makeText(ActivityEditProfil.this, "Gagal", Toast.LENGTH_LONG).show();
                    }).addOnSuccessListener(uri -> {
                uploadDataUser(uri.toString());
            });
        } else {
            Toast.makeText(ActivityEditProfil.this, "Pilih Gambar dulu", Toast.LENGTH_LONG).show();
        }
    }
}
