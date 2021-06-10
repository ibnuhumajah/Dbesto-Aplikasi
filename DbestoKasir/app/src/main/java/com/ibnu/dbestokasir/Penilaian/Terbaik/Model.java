package com.ibnu.dbestokasir.Penilaian.Terbaik;

public class Model {
    String nama;
    String bulan;
    String key;

    public Model(){

    }

    public Model(String nama, String bulan) {
        this.nama = nama;
        this.bulan = bulan;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getBulan() {
        return bulan;
    }

    public void setBulan(String bulan) {
        this.bulan = bulan;
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
                ", bulan='" + bulan + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}