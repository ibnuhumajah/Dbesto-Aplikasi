package kaisar_pajar_oktavianus_entiman.tugasahir.listener;

import java.util.List;

import kaisar_pajar_oktavianus_entiman.tugasahir.model.CartModel;

public interface CartLoadListener {
    void onCartLoadSuccess(List<CartModel> cartModelList);
    void onCartLoadFailed(String message);
}
