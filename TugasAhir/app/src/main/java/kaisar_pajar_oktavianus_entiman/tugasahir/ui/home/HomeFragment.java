package kaisar_pajar_oktavianus_entiman.tugasahir.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nex3z.notificationbadge.NotificationBadge;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kaisar_pajar_oktavianus_entiman.tugasahir.R;
import kaisar_pajar_oktavianus_entiman.tugasahir.adapter.AdaperMenu;
import kaisar_pajar_oktavianus_entiman.tugasahir.listener.CartLoadListener;
import kaisar_pajar_oktavianus_entiman.tugasahir.listener.MenuLoadListener;
import kaisar_pajar_oktavianus_entiman.tugasahir.model.MenuModel;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    AdaperMenu adapterMenu;
    DatabaseReference databaseReference;
    StorageReference reference = FirebaseStorage.getInstance().getReference();
    ArrayList<MenuModel> list;

//    @BindView(R.id.badge)
//    NotificationBadge badge;
//    @BindView(R.id.btnCart)
//    FrameLayout btnCart;
//    @BindView(R.id.mainLayout)
//    RelativeLayout mainLayout;
//
//    MenuLoadListener menuLoadListener;
//    CartLoadListener cartLoadListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseReference = FirebaseDatabase.getInstance("https://tugasahir-4ae0b-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("menuMakanan");

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();
        adapterMenu = new AdaperMenu(getActivity(), list);
        recyclerView.setAdapter(adapterMenu);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MenuModel menuModel = dataSnapshot.getValue(MenuModel.class);
                    list.add(menuModel);
                }
                adapterMenu.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(getActivity(), "Gagal menghubungkan", Toast.LENGTH_SHORT).show();
            }
        });

        init();
    }

    private void init() {
        ButterKnife.bind(getActivity());

//        menuLoadListener = (MenuLoadListener) getActivity();
//        cartLoadListener = (CartLoadListener) getActivity();
        }

    void Gambar() {
        databaseReference = FirebaseDatabase.getInstance("https://tugasahir-4ae0b-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("gambar");

    }

    @Override
    public void onRefresh() {

    }
}