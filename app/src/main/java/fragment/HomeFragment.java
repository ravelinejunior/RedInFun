package fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import adapter.AdapterHome;
import br.com.raveline.redinfunusers.R;
import helper.ConfiguracaoFirebase;
import helper.UsuarioFirebase;
import model.HomeFeed;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private RecyclerView recyclerViewHome;
    private AdapterHome adapterHomeFeed;
    private final List<HomeFeed> listaHomeFeed = new ArrayList<>();
    private ValueEventListener valueEventListenerHomeFeed;
    private DatabaseReference homeFeedRef;
    private String idUsuarioLogado;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Configurações Iniciais
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();
        //recuperando o feed para o usuario que está logado
        homeFeedRef = ConfiguracaoFirebase.getReferenciaDatabase()
                .child("feed")
                .child(idUsuarioLogado);

        //inicializar componentes
        recyclerViewHome = view.findViewById(R.id.recyclerView_home_id);

        //configurar RecyclerView
        adapterHomeFeed = new AdapterHome(listaHomeFeed,getActivity());
        recyclerViewHome.setHasFixedSize(true);
        recyclerViewHome.setLayoutManager(new LinearLayoutManager(getActivity()));

        //setando adapter
        recyclerViewHome.setAdapter(adapterHomeFeed);

        return view;
    }

    private void listarHomeFeed(){
            valueEventListenerHomeFeed = homeFeedRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        //recuperar listagem de home feed
                        listaHomeFeed.add(ds.getValue(HomeFeed.class));
                    }
                    //revertendo a lista para pegar sempre a ultima postagem de cada usuario
                    Collections.reverse(listaHomeFeed);
                    adapterHomeFeed.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }

    @Override
    public void onStart() {
        super.onStart();
        listarHomeFeed();
    }

    @Override
    public void onStop() {
        super.onStop();
        homeFeedRef.removeEventListener(valueEventListenerHomeFeed);

    }
}
