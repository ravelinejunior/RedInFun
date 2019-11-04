package br.com.raveline.redinfunusers;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class FiltrosActivity extends AppCompatActivity {
    private ImageView imagemSelecionadaFiltros;
    private Bitmap imagem;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);
        imagemSelecionadaFiltros = findViewById(R.id.imagem_foto_selecionada_filtro_activity);

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
