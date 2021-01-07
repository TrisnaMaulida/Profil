# ERTEPINTER

## Cara Kontribusi

1.  Fork Repository Utama ke github masing-masing
2.  Setiap selesai commit dan push ke masing-masing fork
3.  Jika dirasa selesai buat pull request ke repo utama
4.  Format pull request
5.  Pastikan sebelum pull commit perubahan yang sudah dibuat
6.  Untuk message commit :

`[NEW]` = fitur baru misal = [NEW] buat class baru MainActivity.java

`[UPDATE]` = update fitur/class/kodingan misal = [UPDATE] edit onCreate di MainActivity.java

`[FIX]` = memperbaiki error misal = [FIX] eror pas login(force close)

`[MINOR]` = commit receh misal = [MINOR] tambahin koma

7.  Untuk Setiap method,variable,function usahakan beri komentar
    Contoh:

    ```java
    package com.example.profilui;
    /**
    * Author : Trian
    * Date : 07 Jan 2020
    * Time : 18.30
    **/
    import android.content.Intent;
    ...


    class MainActivity extends AppCompatActivity{
        //variabel untuk ...
        private String variable;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_edit_profil);


        }
        /***
        * Simpan data ke database
        * @param{ nama}
        * @return simpan nama ke child user
        */
        private void simpanData(String nama){

        }
    }

    ```

## Referensi

1. Untuk referensi variable,nama tabel/child ada di class:

```java
class Constant{
  private static String CHILD_USER="USER";
  private static String CHILD_PENGUMUMAN="PENGUMUMAN";
}
```

2. Struktur projek

```java
app
  |--manifest
  |     |--AndroidManifest.xml
  |--java
  |      |--com
  |      |     |--example
  |      |              |--ertepinter
  |      |              |           |--auth
  |      |              |           |       |--LoginActivity.java
  |      |              |           |       |--...
  |      |              |           |--main
  |      |              |           |       |--MainActivity.java
  |      |              |           |       |--PengumumanActivity.java
  |      |              |           |       |--...
  |      |              |           |--profil
  |      |              |           |       |--ProfilActivity.java
  |      |              |           |       |--EditProfilActivity.java
  |      |              |           |       |--...
  |      |              |           |--...
  |      |              |--models
  |      |              |--..
  |      |--com
  |      |      |--test
  |      |      |--...
  |      |--...
  |--res
  |     |--drawable
  |     |--layout
  |     |--mipmap
  |     |--...
  |--...

```

3. Usahakan setiap tabel ada class modelnya format nama classnya seperti `UserModel`
   Contoh:

```java
    class UserModel{
        private String nama;

        public void setNama(String nama){
            this.nama = nama;
        }
        public String getNama(){
            return this.nama;
        }
    }
```

## Struktur database

| USER     | PENGUMUMAN |
| -------- | :--------: |
| left foo | right foo  |
| left bar | right bar  |

Di Realtime database akan terlihat seperti ini:

```
    BASE
        |--USER
        |       |--id(random)
        |       |           |--nama
        |       |           |--email
        |       |
        |       |--id(random)
        |       |           |--nama
        |       |           |--email
        |       |--id...
        |
        |--PENGUMUMAN
        |       |--id(random)
        |       |            |--sender(idUSER)
        |       |            |--isi
        |       |--id(random)
        |       |            |--sender(idUSER)
        |       |            |--isi
        |       |--id...
        |
        |--...

```

Contoh koding

```java
DatabaseReference dbref = FirebaseDatabase.getIstance().getReference();
```

- Ambil data

```java
dbref
    .child(Constant.CHILD_USER)
    .child(/*Ini child where bisa berupa id user*/)
    .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            /*Ada data*/
                            user user = snapshot.getValue(user.class); // ambil value
                            assert user != null;
                            etnama.setText(user.getNama());
                        }else{
                            /*Tidak Ada / Tidak Ditemukan / Query Salah*/
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                       //Gagal
                    }
    });
```

- Simpan/Tulis Data

```java
String random = dbref.push().getKey(); // dapatkan id otomatis dari firebase
dbref
    .child(Constant.CHILD_USER)
    .child(random)
    .setValue(/* data yang disimpan /ubah*/)
    .addOnSuccessListener(aVoid -> {
            /*Berhasil*/
    }).addOnFailureListener(e -> {
            /*Gagal*/
    });
```
