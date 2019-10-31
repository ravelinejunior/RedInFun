package br.com.raveline.redinfunusers;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import model.Usuario;

public class PerfilAcompanhante extends AppCompatActivity {

    private Usuario usuarioSelecionado;
    private Button botaoSeguirAcompanhante;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_acompanhante);

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

        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
