package kaisar_pajar_oktavianus_entiman.tugasahir.listener;

import java.util.List;

import kaisar_pajar_oktavianus_entiman.tugasahir.model.MenuModel;

public interface MenuLoadListener {
    void onMenuLoadSuccess(List<MenuModel> menuModelList);
    void onMenuLoadSuccessBurger(List<MenuModel> menuModelList);
    void onMenuLoadSuccessCemilan(List<MenuModel> menuModelList);
    void onMenuLoadSuccessMinuman(List<MenuModel> menuModelList);
    void onMenuLoadSuccessPaket(List<MenuModel> menuModelList);
    void onMenuLoadFailed(String message);
}
