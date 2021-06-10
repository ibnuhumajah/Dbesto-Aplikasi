package com.ibnu.dbestokasir.Presensi;

public class Model {

    String nama;
    String nik;
    String key;

    public Model(){

    }

    public Model(String nama, String nik) {
        this.nama = nama;
        this.nik = nik;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Model{" +
                "nama='" + nama + '\'' +
                ", nik='" + nik + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
