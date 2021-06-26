package com.ibnu.dbestokasir.Pelayanan.Pemesanan;

public class PembayaranModel {
        String key, nama, harga, gambar;
        int quantity, totalPrice;

public PembayaranModel() {
        }

public PembayaranModel(String key, String nama, String harga, String gambar, int quantity, int totalPrice) {
        this.key = key;
        this.nama = nama;
        this.harga = harga;
        this.gambar = gambar;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
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

public String getHarga() {
        return harga;
        }

public void setHarga(String harga) {
        this.harga = harga;
        }

public String getGambar() {
        return gambar;
        }

public void setGambar(String gambar) {
        this.gambar = gambar;
        }

public int getQuantity() {
        return quantity;
        }

public void setQuantity(int quantity) {
        this.quantity = quantity;
        }

public int getTotalPrice() {
        return totalPrice;
        }

public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
        }
 }
