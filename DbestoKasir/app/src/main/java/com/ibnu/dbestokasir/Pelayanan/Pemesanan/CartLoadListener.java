package com.ibnu.dbestokasir.Pelayanan.Pemesanan;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.List;


public interface CartLoadListener {
    void onCartLoadSuccess(List<CartModel> cartModelList);
    void onCartLoadFailed(String message);
    void onPembayaranLoad(boolean notificationBadge);
}