package com.example.profilui.models;

public class user {
    private String nama, tempatlahir, tgllahir, nohp, nik, nokk, email, gender, foto;
    public user () {
    }
    public user (String nama, String tempatlahir, String tgllahir, String nohp, String nik, String nokk, String email, String gender, String foto) {
        this.nama = nama;
        this.tempatlahir = tempatlahir;
        this.tgllahir = tgllahir;
        this.nohp = nohp;
        this.nik = nik;
        this.nokk = nokk;
        this.email = email;
        this.gender = gender;
        this.foto = foto;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTempatlahir() {
        return tempatlahir;
    }

    public void setTempatlahir(String tempatlahir) {
        this.tempatlahir = tempatlahir;
    }

    public String getTgllahir() {
        return tgllahir;
    }

    public void setTgllahir(String tgllahir) {
        this.tgllahir = tgllahir;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNokk() {
        return nokk;
    }

    public void setNokk(String nokk) {
        this.nokk = nokk;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
