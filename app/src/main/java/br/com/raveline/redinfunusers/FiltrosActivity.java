package br.com.raveline.redinfunusers;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import adapter.FiltrosAdapterThumbnails;
import helper.ConfiguracaoFirebase;
import helper.RecyclerItemClickListener;
import helper.UsuarioFirebase;
import model.FotoPostada;
import model.Usuario;

public class FiltrosActivity extends AppCompatActivity {
    private ImageView imagemSelecionadaFiltros;
    private Bitmap imagem;
    private Bitmap imagemFiltro;
    private List<ThumbnailItem> listaFiltros;
    private RecyclerView recyclerViewFiltros;
    private FiltrosAdapterThumbnails adapterFiltros;
    private TextInputEditText descricaoFiltros;
    private ProgressBar progressBarFiltros;
    private boolean dadosCarregadosUsuario;

    // firebase
    private DatabaseReference usuariosRef;
    private DatabaseReference usuarioLogadoRef;

    //usuarios
    private String idUsuarioLogado;
    private Usuario usuarioLogado;

    //bloco de inicialização de filtros
    static
    {
        System.loadLibrary("NativeImageProcessor");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);
        imagemSelecionadaFiltros = findViewById(R.id.imagem_foto_selecionada_filtro_activity);
        recyclerViewFiltros = findViewById(R.id.recycler_view_filtros_postar);
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();
        descricaoFiltros = findViewById(R.id.descricao_id_input_edittext_filtros);
        progressBarFiltros = findViewById(R.id.progressBar_id_filtros);


        //configurações iniciais
        listaFiltros = new ArrayList<>();
        usuariosRef = ConfiguracaoFirebase.getReferenciaDatabase().child("usuarios");
        usuarioLogado = UsuarioFirebase.getUsuarioLogado();

        //configurando toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_principal_main_activity);
        toolbar.setTitle("Filtros");
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(),R.color.branco));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_fechar);

        //recuperando dados do usuario logado
        recuperarDadosUsuarioLogado();

        //recuperando imagem da tela de fragment
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            byte[] dadosFoto = bundle.getByteArray("fotoSelecionada");
            //permitir editar byte array
            if (dadosFoto != null) {
                imagem = BitmapFactory.decodeByteArray(dadosFoto,0,dadosFoto.length);
            } else {
                Toast.makeText(this, "Dados foto vazio ou muito grande.", Toast.LENGTH_SHORT).show();
            }
            imagemSelecionadaFiltros.setImageBitmap(imagem);
            imagemFiltro = imagem.copy(imagem.getConfig(),true);

           //configurando o RecyclerView
            adapterFiltros = new FiltrosAdapterThumbnails(listaFiltros,this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
            recyclerViewFiltros.setLayoutManager(layoutManager);
            recyclerViewFiltros.setAdapter(adapterFiltros);


            //adicionando evento de click ao recycler view
            recyclerViewFiltros.addOnItemTouchListener(new RecyclerItemClickListener(
                    this,
                    recyclerViewFiltros,
                    new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                        //configuração de imagem de filtro
                        //recuperando item selecionado
                          ThumbnailItem item = listaFiltros.get(position);

                         imagemFiltro = imagem.copy(imagem.getConfig(),true);
                         Filter filtro = item.filter;
                         imagemSelecionadaFiltros.setImageBitmap(filtro.processFilter(imagemFiltro));

                        }

                        @Override
                        public void onLongItemClick(View view, int position) {

                        }

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        }
                    }

            ));

           //recuperando filtros
           recuperarFiltros();
        }
    }

    //verificando se dados do usuario foram carregados
    private void carregadoUsuario(boolean carregado){
        if (carregado){
            dadosCarregadosUsuario = true;
            progressBarFiltros.setVisibility(View.VISIBLE);
        }else{
            dadosCarregadosUsuario = false;
            progressBarFiltros.setVisibility(View.GONE);
        }
    }

    private void recuperarDadosUsuarioLogado(){
        usuarioLogadoRef = usuariosRef.child(idUsuarioLogado);
        //assim que função é acionada, setar valor como true
        carregadoUsuario(true);
        usuarioLogadoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //recuperando valores de usuarios
                usuarioLogado = dataSnapshot.getValue(Usuario.class);
                //depois de recuperar dados do usuario logado
                carregadoUsuario(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void recuperarFiltros(){
        //limpando itens
        ThumbnailsManager.clearThumbs();
        listaFiltros.clear();

        //configurando filtro normal
        ThumbnailItem item = new ThumbnailItem();
        item.image = imagem;
        item.filterName = "Padrão";
        ThumbnailsManager.addThumb(item);

        //listando todos os filtros
        List<Filter> filtros = FilterPack.getFilterPack(this);
        for (Filter filtro:filtros){

            ThumbnailItem itemFiltro = new ThumbnailItem();
            itemFiltro.image = imagem;
            itemFiltro.filter = filtro;
            itemFiltro.filterName = filtro.getName();

            //setando configuração dos filtros gerais
            ThumbnailsManager.addThumb(itemFiltro);
        }

        //ThumbNailsManager processThumbs processa todas as miniaturas de filtros para layout que será criado
        listaFiltros.addAll(ThumbnailsManager.processThumbs(this));
        adapterFiltros.notifyDataSetChanged();
    }

    private void postarFoto(){

        //verificando se dados ja foram carregados antes de postar foto
        if (dadosCarregadosUsuario){
            Toast.makeText(this, "Carregando dados, aguarde.", Toast.LENGTH_SHORT).show();
        } else{
            final FotoPostada fotoPostada = new FotoPostada();
            fotoPostada.setIdUsuario(idUsuarioLogado);
            fotoPostada.setDescricaoFotoPostada(Objects.requireNonNull(descricaoFiltros.getText()).toString());

            //recuperar dados da imagem para salvar no firebaseStorage para depois salvar no firebase
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imagemFiltro.compress(Bitmap.CompressFormat.JPEG,100,baos);
            byte[] dadosImagemPostada = baos.toByteArray();

            //salvando no storage
            StorageReference storageReference = ConfiguracaoFirebase.getStorageReference();

            StorageReference imagemRef = storageReference.child("imagens")
                    .child("fotoPostada")
                    .child(fotoPostada.getIdFotoPostada()+".jpeg");

            //passar um array de bytes no putbytes da imagem
            UploadTask uploadTask = imagemRef.putBytes(dadosImagemPostada);
            uploadTask.addOnFailureListener(e -> Toast.makeText(FiltrosActivity.this, "Falha ao executar o comando para fazer upload da imagem.", Toast.LENGTH_SHORT).show()).addOnProgressListener(taskSnapshot -> progressBarFiltros.setVisibility(View.VISIBLE)).addOnSuccessListener(taskSnapshot -> {
                //recuperar local da foto
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                    //recuperando local da foto postada
                    fotoPostada.setCaminhoFotoPostada(uri.toString());

                    //salvando a foto no banco de dados
                    if (fotoPostada.salvarFotoPostada()){
                        //caso foto tenha sido postada com sucesso, atualizar numero de fotos postada
                        try {

                            int quantidadeFotosPostadas = usuarioLogado.getFotos() + 1;
                            usuarioLogado.setFotos(quantidadeFotosPostadas);
                            usuarioLogado.atualizarFotosPostadas();

                            Toast.makeText(FiltrosActivity.this, "Foto postada com sucesso!", Toast.LENGTH_SHORT).show();
                            progressBarFiltros.setVisibility(View.GONE);
                            finish();
                        }catch (Exception e){
                            finish();
                            Toast.makeText(this, "Foto postada com sucesso!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    } else{
                        Toast.makeText(FiltrosActivity.this, "Erro ao postar foto. Verifique sua internet.", Toast.LENGTH_SHORT).show();
                    }
                });

            });
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filtros_postar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //definindo itens que foram selecionados

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.ic_salvar_postagem_menu_postar:
                postarFoto();
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
