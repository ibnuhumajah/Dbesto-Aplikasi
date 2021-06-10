package com.ibnu.dbestokasir.admin.absen;

public class Model {
    String nama;
    String nik;
    String qr;
    String masuk;
    String keluar;
    String key;

    public Model (){

    }

    public Model(String nama, String nik, String qr, String masuk, String keluar, String key) {
        this.nama = nama;
        this.nik = nik;
        this.qr = qr;
        this.masuk = masuk;
        this.keluar = keluar;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNik() { return nik; }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public String getMasuk() {
        return masuk;
    }

    public void setMasuk(String masuk) {
        this.masuk = masuk;
    }

    public String getKeluar() {
        return keluar;
    }

    public void setKeluar(String keluar) {
        this.keluar = keluar;
    }

    @Override
    public String toString() {
        return "Model{" +
                "nama='" + nama + '\'' +
                ", nik='" + nik + '\'' +
                ", qr='" + qr + '\'' +
                ", masuk='" + masuk + '\'' +
                ", keluar='" + keluar + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}

