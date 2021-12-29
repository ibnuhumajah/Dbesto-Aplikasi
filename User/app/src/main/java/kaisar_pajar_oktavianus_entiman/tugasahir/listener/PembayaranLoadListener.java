package kaisar_pajar_oktavianus_entiman.tugasahir.listener;

import java.util.List;

import kaisar_pajar_oktavianus_entiman.tugasahir.model.PembayaranModel;

public interface PembayaranLoadListener {
    void onPembayaranLoadSuccess(List<PembayaranModel> pembayaranModelList);
    void onPembayaranFailed(String message);
}
