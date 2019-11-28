package br.com.raveline.redinfunusers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import model.FotoPostada;
import model.Usuario;

public class VisualizarFotoPostada extends AppCompatActivity {
    private CircleImageView fotoUsuarioPostagem;
    private ImageView imagemPostadaPostagem;
    private TextView nomeUsuarioSelecionadoPostagem;
    private TextView numeroCurtidasPostagem;
    private TextView descricaoPostagem;
    //private TextView comentariosPostagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_foto_postada);
        inicializarComponentes();

        //recuperar dados da activity
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            //recuperar dados
            FotoPostada postagem = (FotoPostada) bundle.getSerializable("fotoPostadaAcompanhante");
            Usuario usuario = (Usuario) bundle.getSerializable("usuario");

            //exibir dados do usuario selecionado
            Uri uri = Uri.parse(Objects.requireNonNull(usuario).getCaminhoFoto());
            Glide.with(getApplicationContext()).load(uri).into(fotoUsuarioPostagem);
            nomeUsuarioSelecionadoPostagem.setText(usuario.getNome());

            //exibir postagem
            Uri uriPostagem = Uri.parse(Objects.requireNonNull(postagem).getCaminhoFotoPostada());
            Glide.with(VisualizarFotoPostada.this).load(uriPostagem).into(imagemPostadaPostagem);
            //numeroCurtidasPostagem.setText(usuario.getFotos());
            if (postagem.getDescricaoFotoPostada() != null) {
                descricaoPostagem.setText(postagem.getDescricaoFotoPostada());
            }else {
                descricaoPostagem.setText("Foto sem descrição.");
            }
            //configurar comentarios

        }


    }


    public void inicializarComponentes(){

        fotoUsuarioPostagem = findViewById(R.id.foto_perfil_usuario_visualizar_foto);
        imagemPostadaPostagem = findViewById(R.id.imagem_selecionada_visualizar_postagem);
        nomeUsuarioSelecionadoPostagem = findViewById(R.id.nome_usuario_visualizar_postagem);
        numeroCurtidasPostagem = findViewById(R.id.numero_curtidas_visualizar_postagem);
        descricaoPostagem = findViewById(R.id.descricao_foto_visualizar_postagem);
        //comentariosPostagem = findViewById(R.id.comentarios_visualizar_postagem);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
