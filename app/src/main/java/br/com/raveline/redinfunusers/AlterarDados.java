package br.com.raveline.redinfunusers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class AlterarDados extends AppCompatActivity {
    private EditText editarNomeAlterarDados;
    private EditText editarEmailAlterarDados;
    private Button botaoAlterarDados;
    private TextView editarFotoAlterarDados;
    private ImageView fotoPerfilAlterarDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_dados);
        //carregar elementos
        carregarElementos();


    }

    public void carregarElementos(){
        editarEmailAlterarDados = findViewById(R.id.email_alterar_perfil);
        editarNomeAlterarDados = findViewById(R.id.nome_alterar_perfil);
        editarFotoAlterarDados = findViewById(R.id.alterar_foto_perfil);
        fotoPerfilAlterarDados = findViewById(R.id.imagem_alterar_perfil);
        botaoAlterarDados = findViewById(R.id.botao_alterar_perfil);
        editarNomeAlterarDados.requestFocus();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
