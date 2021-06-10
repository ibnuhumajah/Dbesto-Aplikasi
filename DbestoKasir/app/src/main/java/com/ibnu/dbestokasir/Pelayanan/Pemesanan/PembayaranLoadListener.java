package com.ibnu.dbestokasir.Pelayanan.Pemesanan;

import java.util.List;

public interface PembayaranLoadListener {
    void onPembayaranLoadSuccess(List<PembayaranModel> pembayaranModelList);
    void onPembayaranFailed(String message);
}
