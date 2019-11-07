package br.com.raveline.redinfunusers;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.util.ArrayList;
import java.util.List;

import adapter.FiltrosAdapterThumbnails;
import helper.RecyclerItemClickListener;

public class FiltrosActivity extends AppCompatActivity {
    private ImageView imagemSelecionadaFiltros;
    private Bitmap imagem;
    private Bitmap imagemFiltro;
    private List<ThumbnailItem> listaFiltros;
    private RecyclerView recyclerViewFiltros;
    private FiltrosAdapterThumbnails adapterFiltros;

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


        //configurações iniciais
        listaFiltros = new ArrayList<>();


        //configurando toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_principal_main_activity);
        toolbar.setTitle("Filtros");
        toolbar.setTitleTextColor(getColor(R.color.branco));
        toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_fechar);

        //recuperando imagem da tela de fragment
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            byte[] dadosFoto = bundle.getByteArray("fotoSelecionada");
            //permitir editar byte array
            imagem = BitmapFactory.decodeByteArray(dadosFoto,0,dadosFoto.length);
            imagemSelecionadaFiltros.setImageBitmap(imagem);



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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filtros_postar,menu);
        menu.addSubMenu("Sair").setHeaderTitle("Loucura");
        return super.onCreateOptionsMenu(menu);
    }

    //definindo itens que foram selecionados

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.ic_salvar_postagem_menu_postar:
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
