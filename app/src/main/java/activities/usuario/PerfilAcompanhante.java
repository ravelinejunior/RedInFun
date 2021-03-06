package activities.usuario;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import activities.view.VisualizarFotoPostada;
import adapter.AdapterGridFotosAcompanhante;
import br.com.raveline.redinfunusers.R;
import de.hdodenhof.circleimageview.CircleImageView;
import helper.ConfiguracaoFirebase;
import helper.UsuarioFirebase;
import model.FotoPostada;
import model.Usuario;

public class PerfilAcompanhante extends AppCompatActivity {


    private Button botaoSeguirAcompanhante;
    private CircleImageView imagemPerfilAcompanhante;
    private TextView fasPerfilAcompanhante;
    private TextView clientesPerfilAcompanhante;
    private TextView fotosPerfilAcompanhante;
    private GridView gridViewPerfilAcompanhante;
    private AdapterGridFotosAcompanhante adapterGridFotosAcompanhante;

    //Usuarios
    private Usuario usuarioSelecionado;
    private Usuario usuarioLogado;
    private String idUsuarioLogado;


    //firebase
    private DatabaseReference referenceFirebase;
    private DatabaseReference usuariosRef;
    private DatabaseReference usuarioAmigoRef;
    private DatabaseReference seguidoresRef;
    private DatabaseReference usuarioLogadoRef;
    private DatabaseReference fotoPostadaRef;
    private DatabaseReference firebaseRef;

    //para eventos
    private ValueEventListener valueEventListenerPerfilAcompanhante;

    //listas
    private List<FotoPostada> fotoPostadaList;

    //usuarios


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_acompanhante);
        inicializarComponentes();

        //Configurações iniciais Firebase
        //recuperando o no usuarios
        referenceFirebase = ConfiguracaoFirebase.getReferenciaDatabase();
        usuariosRef = referenceFirebase.child("usuarios");
        seguidoresRef = referenceFirebase.child("seguidores");
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();
        firebaseRef = ConfiguracaoFirebase.getReferenciaDatabase();
        //
        //recuperar usuario selecionado
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            usuarioSelecionado = (Usuario) bundle.getSerializable("usuarioSelecionado");

            //configura nome da toolbar nome do usuario
            Objects.requireNonNull(getSupportActionBar()).setTitle(usuarioSelecionado.getNome());

            //recuperar foto do usuario
            String caminhoFoto = usuarioSelecionado.getCaminhoFoto();
            if( caminhoFoto != null){
                Uri url = Uri.parse(caminhoFoto);
                Glide.with(PerfilAcompanhante.this).load(url)
                        .circleCrop()
                        .centerInside()
                        .into(imagemPerfilAcompanhante);

            } else{
                Toast.makeText(this, "Erro ao recuperar imagem.", Toast.LENGTH_SHORT).show();
            }

            //inicializa imagemLoader Universal para carregar fotos dos usuarios
            inicializarImageLoader();
            try {
                //carregar fotos dos usuarios selecionados
                carregarFotosPostadas();

            } catch (Exception e){
                e.printStackTrace();
            }

            //abrir foto clicada
            gridViewPerfilAcompanhante.setOnItemClickListener((parent, view, position, id) -> {
                    FotoPostada fotoPostada = fotoPostadaList.get(position);
                Intent intent = new Intent(getApplicationContext(), VisualizarFotoPostada.class);

                    //passando dados para outra activity
                    intent.putExtra("fotoPostadaAcompanhante",fotoPostada);
                    intent.putExtra("usuario",usuarioSelecionado);
                    startActivity(intent);
            });

        }

        botaoSeguirAcompanhante.setOnClickListener(v -> {
            //salvando seguidores
            salvarSeguidor(usuarioLogado,usuarioSelecionado);
        });

    }

    private void verificarSeSegueAcompanhante(){
        DatabaseReference seguidorRef = seguidoresRef.
                child(usuarioSelecionado.getId()).
                child(idUsuarioLogado)
                ;
        seguidorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //verificar se existem dados dentro do DataSnapshot
                if (dataSnapshot.exists()){
                    //Já segue usuario
                    habilitarBotaoSeguir(true,usuarioSelecionado);
                    botaoSeguirAcompanhante.setOnClickListener(null);
                }
                else{
                    //Ainda não segue usuario
                    habilitarBotaoSeguir(false,usuarioSelecionado);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void carregarFotosPostadas(){

        //inicializando um arrayList
        fotoPostadaList = new ArrayList<>();

       // recuperando dados do usuario selecionado para visualizar suas postagens
        fotoPostadaRef = ConfiguracaoFirebase.getReferenciaDatabase()
                .child("fotosPostadas")
                .child(usuarioSelecionado.getId());

        //Recupera fotos postadas
        //Usar metodo para carregar as fotos uma unica vez para reduzir gastos de memoria
        fotoPostadaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //configurando tamanho do gridView
                int tamanhoGridView = getResources().getDisplayMetrics().widthPixels;
                //dividido por 3 por coloquei o numero de colunas = 3
                int tamanhoImagemGrid = tamanhoGridView/3;
                gridViewPerfilAcompanhante.setColumnWidth(tamanhoImagemGrid);

                List<String> urlFotos = new ArrayList<>();

                //percorrer objetos para verificar dados existentes
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    FotoPostada fotoPostada = ds.getValue(FotoPostada.class);
                    //carregando lista de urls
                    fotoPostadaList.add(fotoPostada);
                    urlFotos.add(Objects.requireNonNull(fotoPostada).getCaminhoFotoPostada());

                }
                //Configurar Adapter
                adapterGridFotosAcompanhante = new AdapterGridFotosAcompanhante(getApplicationContext(),R.layout.grid_fotos_acompanhante,urlFotos);
                gridViewPerfilAcompanhante.setAdapter(adapterGridFotosAcompanhante);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void inicializarImageLoader(){

        //carregando o ImageLoader,
        File cacheDir = StorageUtils.getCacheDirectory(this);
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration
                .Builder(this)
                .memoryCache(new LruMemoryCache(2*1024*1024))
                .memoryCacheSize(2*1024*1024)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheSize(50*1024*1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();
        ImageLoader.getInstance().init(configuration);

    }

    private void recuperarDadosUsuarioLogado(){
        usuarioLogadoRef = usuariosRef.child(idUsuarioLogado);
        usuarioLogadoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuarioLogado = dataSnapshot.getValue(Usuario.class);

                //verificar se usuario ja segue acompanhante selecionada
                verificarSeSegueAcompanhante();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void habilitarBotaoSeguir(boolean segueUsuario,Usuario usuAcompanhante){


        if (segueUsuario){
            botaoSeguirAcompanhante.setText("Curtindo " + usuAcompanhante.getNome());


        }
        else{
            botaoSeguirAcompanhante.setText("Curtir Acompanhante");
            botaoSeguirAcompanhante.setOnClickListener(v ->
                    salvarSeguidor(usuarioLogado,usuarioSelecionado));


        }

    }

    private void salvarSeguidor(Usuario usuLogado, Usuario usuAcompanhante){
        //modelo estrutura de dados seguidores
        /*
            cliente
            id_usuario_seguido (acompanhante)
                id_seguindo de quem está  seguindo (usuario logado) seguidor
                       dados de quem está sendo seguido (usuario logado) seguidor

         */
        //recuperar valores no firebase para selecionar quais os valores eu quero
        HashMap<String,Object> dadosUsuarioLogado = new HashMap<>();
        dadosUsuarioLogado.put("nome",usuLogado.getNome());
        dadosUsuarioLogado.put("caminhoFoto",usuLogado.getCaminhoFoto());

        DatabaseReference seguidorRef = seguidoresRef.
                child(usuAcompanhante.getId()).
                child(usuLogado.getId());
        seguidorRef.setValue(dadosUsuarioLogado);

        botaoSeguirAcompanhante.setText("Curtindo " + usuAcompanhante.getNome());
        //desabilitar evento onClick
        //botaoSeguirAcompanhante.setOnClickListener(null);

        //incrementar seguidores das acompanhantes
        int seguidoresFas = usuAcompanhante.getFas()+1;
        HashMap<String,Object> novoFa = new HashMap<>();
        novoFa.put("fas",seguidoresFas);
        DatabaseReference fasRef = usuariosRef.child(usuAcompanhante.getId());
        fasRef.updateChildren(novoFa);

        //incrementar seguindo do usuario logado
        int seguindoClientes = usuLogado.getClientes() + 1;
        HashMap<String,Object> novoCliente = new HashMap<>();
        novoCliente.put("clientes",seguindoClientes);
        //Acessando os valores do nó usuarios
        DatabaseReference clientesRef = usuariosRef.child(usuLogado.getId());
        //alterando os dados
        clientesRef.updateChildren(novoCliente);

    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarDadosAcompanhante();

        //recupera sempre os dados do usuario logado
        recuperarDadosUsuarioLogado();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //remove evento para nao pesar activity
        usuarioAmigoRef.removeEventListener(valueEventListenerPerfilAcompanhante);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void inicializarComponentes(){
        imagemPerfilAcompanhante = findViewById(R.id.perfil_foto_perfil_fragment);
        botaoSeguirAcompanhante = findViewById(R.id.botao_acao_perfil);
        fasPerfilAcompanhante = findViewById(R.id.fas_perfil_fragment);
        clientesPerfilAcompanhante = findViewById(R.id.clientes_perfil_fragment);
        fotosPerfilAcompanhante = findViewById(R.id.fotos_perfil_fragment);
        gridViewPerfilAcompanhante = findViewById(R.id.grid_perfil_layout_fragment);
        botaoSeguirAcompanhante.setText("Carregando");


        //configurando toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_principal_main_activity);
        toolbar.setTitle("RedInFun");
        toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(),R.color.branco));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_voltar_back);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    private void recuperarDadosAcompanhante(){
        //recuperando dados do usuario mencionado por id
        usuarioAmigoRef = usuariosRef.child(usuarioSelecionado.getId());

        //adicionando evento
        valueEventListenerPerfilAcompanhante = usuarioAmigoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //recuperar e exibir dados do usuario
                Usuario usuario = dataSnapshot.getValue(Usuario.class);

                //configurar valores da activity
                String fotosPostadasAcompanhante = String.valueOf(Objects.requireNonNull(usuario).getFotos());
                String fasAcompanhante = String.valueOf(usuario.getFas());
                String clientesAcompanhantes = String.valueOf(usuario.getClientes());

                //configurar caixa de texto
                fotosPerfilAcompanhante.setText(fotosPostadasAcompanhante);
                fasPerfilAcompanhante.setText(fasAcompanhante);
                clientesPerfilAcompanhante.setText(clientesAcompanhantes);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
