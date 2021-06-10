package com.ibnu.dbestokasir.Pelayanan.Pemesanan;
import java.util.List;


public interface CartLoadListener {
    void onCartLoadSuccess(List<CartModel> cartModelList);
    void onCartLoadFailed(String message);
}
