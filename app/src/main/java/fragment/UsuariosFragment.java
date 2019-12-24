package fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import adapter.AdapterUsuarios;
import br.com.raveline.redinfunusers.PerfilAcompanhante;
import br.com.raveline.redinfunusers.R;
import helper.ConfiguracaoFirebase;
import helper.RecyclerItemClickListener;
import helper.UsuarioFirebase;
import model.Usuario;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsuariosFragment extends Fragment {
    private RecyclerView recyclerViewUsuarios;
    private List<Usuario> listaUsuarios;
    private String idUsuarioLogado;

    //atributo de referencia para usuarios
    private DatabaseReference usuariosReferencia;

    //event listenener
    private ValueEventListener valueEventListenerUsuariosFragment;

    //adapter
    private AdapterUsuarios adapterUsuarios;



    public UsuariosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_usuarios, container, false);


        //inicializando componentes
        recyclerViewUsuarios = view.findViewById(R.id.recyclerView_usuarios_id);
        listaUsuarios = new ArrayList<>();
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();
        usuariosReferencia = ConfiguracaoFirebase.getReferenciaDatabase().child("usuarios");

        recyclerViewUsuarios.setHasFixedSize(true);
        recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterUsuarios = new AdapterUsuarios(listaUsuarios,getActivity());
        recyclerViewUsuarios.setAdapter(adapterUsuarios);

        //configurar evento de clique
        recyclerViewUsuarios.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                recyclerViewUsuarios,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        //recuperar valores selecionados
                        Usuario usuarioSelecionado = listaUsuarios.get(position);
                        Intent i = new Intent(getActivity(), PerfilAcompanhante.class);
                        i.putExtra("usuarioSelecionado",usuarioSelecionado);
                        startActivity(i);
                    }

                    @Override
                    public void onLongItemClick() {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }

        ));

        exibirUsuarios();


        return view;
    }

    public void exibirUsuarios(){


        valueEventListenerUsuariosFragment = usuariosReferencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //limpar lista de usuarios
                listaUsuarios.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    //recuperando usuarios
                    Usuario usuario = ds.getValue(Usuario.class);
                    //verificar se usuario pesquisado sou eu mesmo logado.
                    if (idUsuarioLogado.equals(Objects.requireNonNull(usuario).getId()))
                        continue;

                    //adicionando usuario no Firebase
                    listaUsuarios.add(usuario);
                    listaUsuarios.lastIndexOf(usuario);


                }
                adapterUsuarios.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

}











