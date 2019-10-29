package br.com.raveline.redinfunusers;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Usuario;

public class PerfilAcompanhante extends AppCompatActivity {

    private Usuario usuarioSelecionado;
    private Button botaoSeguirAcompanhante;
    private CircleImageView imagemPerfilAcompanhante;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_acompanhante);

        imagemPerfilAcompanhante = findViewById(R.id.perfil_foto_perfil_fragment);
        botaoSeguirAcompanhante = findViewById(R.id.botao_acao_perfil);
        botaoSeguirAcompanhante.setText("Seguir Acompanhante");

        //configurando toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_principal_main_activity);
        toolbar.setTitle("RedInFun");
        toolbar.setTitleTextColor(getColor(R.color.branco));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_voltar_back);

        //recuperar usuario selecionado
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            usuarioSelecionado = (Usuario) bundle.getSerializable("usuarioSelecionado");

            //configura nome da toolbar nome do usuario
            getSupportActionBar().setTitle(usuarioSelecionado.getNome());

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

        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
