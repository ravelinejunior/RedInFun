package fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import activities.usuario.PerfilAcompanhante;
import adapter.AdapterPesquisar;
import br.com.raveline.redinfunusers.R;
import helper.ConfiguracaoFirebase;
import helper.RecyclerItemClickListener;
import helper.UsuarioFirebase;
import model.Usuario;

/**
 * A simple {@link Fragment} subclass.
 */
public class PesquisarFragment extends Fragment {

    private RecyclerView recyclerViewPesquisarFragment;
    private SearchView searchViewPesquisarFragment;

    //lista de usuarios
    private List<Usuario> listaUsuarios;
    private String idUsuarioLogado;

    //atributo de referencia para usuarios
    private DatabaseReference usuariosReferencia;

    //adapter pesquisa
    private AdapterPesquisar adapterPesquisar;

    public PesquisarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_pesquisar, container, false);
        recyclerViewPesquisarFragment = view.findViewById(R.id.recyclerview_id_pesquisar_fragment);
        searchViewPesquisarFragment = view.findViewById(R.id.searchview_id_pesquisar_fragment);
        //configurar usuario logado
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();

        //configurar lista
        listaUsuarios = new ArrayList<>();
        usuariosReferencia = ConfiguracaoFirebase.getReferenciaDatabase().child("usuarios");

        //configurar Recycler view
        recyclerViewPesquisarFragment.setHasFixedSize(true);
        recyclerViewPesquisarFragment.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapterPesquisar = new AdapterPesquisar(listaUsuarios,getActivity());
        recyclerViewPesquisarFragment.setAdapter(adapterPesquisar);

        //configurando evento de clique
        recyclerViewPesquisarFragment.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(),
                recyclerViewPesquisarFragment,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        //recuperando valores dos usuarios
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

        //configurar searchview
        //usar metodo para começar pesquisa quando usuario começa a pesquisar na search view
        searchViewPesquisarFragment.setQueryHint("Buscar Acompanhante");
        //capturando o que o usuario digitou
        searchViewPesquisarFragment.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //metodo para começar a pesquisar quando usuario pressionar o submit
                //rodar em modo debug para testar / mudar para false
                String textoDigitado = query.toUpperCase();
                //realizar pesquisa
                pesquisarUsuario(textoDigitado);

                Log.d("onQueryTextSubmit","texto digitado = "+query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //esse metodo é para quando usuario começa a digitar
                 Log.d("onQueryTextChange","texto digitado = "+newText);
                //rodar em modo debug para testar
                //recuperando os valores
                String textoDigitado = newText.toUpperCase();
                //realizar pesquisa
                pesquisarUsuario(textoDigitado);

            return true;
            }
        });

        //realizar modificações de layout
        return view;
    }



    private void pesquisarUsuario(String textoInformado) {
        //limpar pesquisa
        listaUsuarios.clear();

        //criar uma lista para recuperar valores digitados
        if (textoInformado.length() >= 0){
            Query queryPesquisa = usuariosReferencia.orderByChild("nomeUsuarioPesquisa").
                    startAt(textoInformado).
                    endAt(textoInformado + "\uf8ff");

            queryPesquisa.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    //para nao fazer pesquisas desnecessarias no banco de dados, limpar a pesquisa
                    listaUsuarios.clear();

                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        //recuperar usuarios do firebase
                        Usuario usuario = ds.getValue(Usuario.class);
                        //verificar se usuario pesquisado sou eu mesmo logado.
                        if (idUsuarioLogado.equals(Objects.requireNonNull(usuario).getId()))
                            continue;

                        //adicionar usuario no firebase
                        listaUsuarios.add(usuario);
                        //Log.d("Usuario", Objects.requireNonNull(ds.getValue(Usuario.class)).toString());
                    }
                    //notificando o adapter para exibir os dados
                    adapterPesquisar.notifyDataSetChanged();

                    /*exibir os itens
                    int para recuperar a quantidade de valores reconhecidos no banco de dados
                    int total = listaUsuarios.size();
                    */


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


    }

}
