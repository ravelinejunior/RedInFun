package fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import adapter.AdapterGridFotosAcompanhante;
import br.com.raveline.redinfunusers.AlterarDados;
import br.com.raveline.redinfunusers.R;
import de.hdodenhof.circleimageview.CircleImageView;
import helper.ConfiguracaoFirebase;
import helper.UsuarioFirebase;
import model.FotoPostada;
import model.Usuario;

import static java.util.Objects.*;

/**
* A simple {@link Fragment} subclass.
*/
public class PerfilFragment extends Fragment {
private Button botaoEditarPerfil;
private GridView gridViewPerfil;
private TextView fotosPostadasPerfil;
private TextView clientesPerfil;
private TextView fasPerfil;
private CircleImageView fotoPerfil;

//Firebse
private DatabaseReference usuariosRef;
private DatabaseReference usuarioLogadoRef;
private DatabaseReference firebaseRef;

//Usuario
private Usuario usuarioLogado;
private AdapterGridFotosAcompanhante adapterGridFotosPerfil;

//events
private ValueEventListener valueEventListenerPerfilUsuario;
private DatabaseReference fotoPostadaRef;

public PerfilFragment() {
    // Required empty public constructor
}



@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {

    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_perfil, container, false);

    //deixando apenas como portrait (nao permitir tela virar)
    requireNonNull(getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

    //CONFIGURAÇÕES INICIAIS
    usuarioLogado = UsuarioFirebase.getUsuarioLogado();
    firebaseRef = ConfiguracaoFirebase.getReferenciaDatabase();
    usuariosRef = firebaseRef.child("usuarios");

    // recuperando dados do usuario selecionado para visualizar suas postagens
    fotoPostadaRef = ConfiguracaoFirebase.getReferenciaDatabase()
            .child("fotosPostadas")
            .child(usuarioLogado.getId());

    //inicializando componentes
    inicializarComponentes(view);

    //Recuperando foto usuario logado

    recuperarFotoUsuario();

    //recuperar usuario logado
        botaoEditarPerfil.setOnClickListener(v -> startActivity(new Intent(getActivity(), AlterarDados.class)));

    //iniciaizar ImageLoader
    inicializarImageLoader();

    //carregar fotos do usuario
    carregarFotosPostadas();

        return view;
    }



private void carregarFotosPostadas(){
    // recuperando dados do usuario selecionado para visualizar suas postagens
    fotoPostadaRef = ConfiguracaoFirebase.getReferenciaDatabase()
            .child("fotosPostadas")
            .child(usuarioLogado.getId());

    //Recupera fotos postadas
    //Usar metodo para carregar as fotos uma unica vez para reduzir gastos de memoria
    fotoPostadaRef.addListenerForSingleValueEvent(new ValueEventListener() {


        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            try {
                //configurando tamanho do gridView
                int tamanhoGridView = getResources().getDisplayMetrics().widthPixels;
                //dividido por 3 por coloquei o numero de colunas = 3
                int tamanhoImagemGrid = tamanhoGridView / 3;
                gridViewPerfil.setColumnWidth(tamanhoImagemGrid);

                List<String> urlFotos = new ArrayList<>();

                //percorrer objetos para verificar dados existentes
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    FotoPostada fotoPostada = ds.getValue(FotoPostada.class);
                    //carregando lista de urls
                    urlFotos.add(requireNonNull(fotoPostada).getCaminhoFotoPostada());

                }
               // int quantidadeFotos = urlFotos.size();
                // fotosPostadasPerfil.setText(String.valueOf(quantidadeFotos));

                //Configurar Adapter
                adapterGridFotosPerfil = new AdapterGridFotosAcompanhante(requireNonNull(getActivity()), R.layout.grid_fotos_acompanhante, urlFotos);
                gridViewPerfil.setAdapter(adapterGridFotosPerfil);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });



}

private void inicializarImageLoader(){

    //carregando o ImageLoader,
    File cacheDir = StorageUtils.getCacheDirectory(getActivity());
    ImageLoaderConfiguration configuration = new ImageLoaderConfiguration
            .Builder(getActivity())
            .memoryCache(new LruMemoryCache(2*1024*1024))
            .memoryCacheSize(2*1024*1024)
            .diskCache(new UnlimitedDiskCache(requireNonNull(cacheDir)))
            .diskCacheSize(50*1024*1024)
            .diskCacheFileCount(100)
            .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
            .memoryCacheSizePercentage(99)
            .memoryCacheSize(99)
            .build();
    ImageLoader.getInstance().init(configuration);

}

private void recuperarFotoUsuario(){
    usuarioLogado = UsuarioFirebase.getUsuarioLogado();
    String caminhoFoto = usuarioLogado.getCaminhoFoto();
    if( caminhoFoto != null){
        Uri url = Uri.parse(caminhoFoto);
        Glide.with(requireNonNull(getActivity())).load(url)
                .circleCrop()
                .centerInside()
                .into(fotoPerfil);

    } else{
        Toast.makeText(getActivity(), "Erro ao recuperar imagem.", Toast.LENGTH_SHORT).show();
    }
}

private void inicializarComponentes(View view){
        botaoEditarPerfil = view.findViewById(R.id.botao_acao_perfil);
        gridViewPerfil = view.findViewById(R.id.grid_perfil_layout_fragment);
        fasPerfil = view.findViewById(R.id.fas_perfil_fragment);
        fotosPostadasPerfil = view.findViewById(R.id.fotos_perfil_fragment);
        clientesPerfil = view.findViewById(R.id.clientes_perfil_fragment);
        fotoPerfil = view.findViewById(R.id.perfil_foto_perfil_fragment);

    }

private void recuperarDadosUsuarioLogado(){
    //recuperando dados do usuario mencionado por id
    usuarioLogadoRef = usuariosRef.child(usuarioLogado.getId());

    //adicionando evento
    valueEventListenerPerfilUsuario = usuarioLogadoRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            //recuperar e exibir dados do usuario
            Usuario usuario = dataSnapshot.getValue(Usuario.class);

            //configurar valores da activity

            try {
                String fasAcompanhante = String.valueOf(requireNonNull(usuario).getFas());
                String clientesAcompanhantes = String.valueOf(usuario.getClientes());
                String fotosPostadasAcompanhante = String.valueOf(Objects.requireNonNull(usuario).getFotos());
                //configurar caixa de texto

            fasPerfil.setText(fasAcompanhante);
            clientesPerfil.setText(clientesAcompanhantes);
            fotosPostadasPerfil.setText(fotosPostadasAcompanhante);

            }catch (Exception e){
                e.getMessage();
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

}

@Override
public void onStop() {
    super.onStop();
    usuarioLogadoRef.removeEventListener(valueEventListenerPerfilUsuario);
}

@Override
public void onStart() {
    super.onStart();
    recuperarFotoUsuario();
    //recuperando dados do usuario logado
    recuperarDadosUsuarioLogado();

}
}

