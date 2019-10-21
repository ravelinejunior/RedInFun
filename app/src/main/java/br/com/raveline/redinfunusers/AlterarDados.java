package br.com.raveline.redinfunusers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import fragment.PerfilFragment;
import helper.ConfiguracaoFirebase;
import helper.UsuarioFirebase;
import model.Usuario;

public class AlterarDados extends AppCompatActivity {
    private EditText editarNomeAlterarDados;
    private EditText editarEmailAlterarDados;
    private Button botaoAlterarDados;
    private TextView editarFotoAlterarDados;
    private ImageView fotoPerfilAlterarDados;
    private ImageButton imagemBotaoVoltar;
    private ProgressBar progressBarAlterarDados;

    //Firebase
    private FirebaseUser firebaseUser;
    private FirebaseAuth autenticacao;

    //classe Usuario
    Usuario usuarioLogado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_dados);
        //carregar elementos
        carregarElementos();

        //configurações iniciais do usuario
        usuarioLogado = UsuarioFirebase.getUsuarioLogado();

        //atualizando nome do usuario no banco de dados
        usuarioLogado.salvarDados();
        //usuario do firebase ja foi carregado no metodo carregarElementos()
            try {

                editarNomeAlterarDados.setText(Objects.requireNonNull(firebaseUser.getDisplayName()));
                String nomeExibido = editarNomeAlterarDados.getText().toString();
                if (nomeExibido.length() > 30){
                    editarEmailAlterarDados.setText("");
                }
                editarEmailAlterarDados.setText(firebaseUser.getEmail().toString());
            }catch (Exception e){
                e.getStackTrace();
            }

        botaoAlterarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recuperar nome atualizado
                String nomeAtualizado = editarNomeAlterarDados.getText().toString();
                UsuarioFirebase.atualizarNomeUsuario(nomeAtualizado);

                //atualizar o nome no Firebase
                usuarioLogado.setNome(nomeAtualizado);
                usuarioLogado.atualizarDados();
                Toast.makeText(AlterarDados.this, "Nome alterado", Toast.LENGTH_SHORT).show();


            }
        });

            imagemBotaoVoltar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(AlterarDados.this,(MainActivity.class)));
                    finish();
                }
            });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    public void carregarElementos(){
        editarEmailAlterarDados = findViewById(R.id.email_alterar_perfil);
        editarNomeAlterarDados = findViewById(R.id.nome_alterar_perfil);
        editarFotoAlterarDados = findViewById(R.id.alterar_foto_perfil);
        fotoPerfilAlterarDados = findViewById(R.id.imagem_alterar_perfil);
        botaoAlterarDados = findViewById(R.id.botao_alterar_perfil);
        progressBarAlterarDados = findViewById(R.id.progressBar_alterar_dados);
        imagemBotaoVoltar = findViewById(R.id.voltar_tela_topo_alterar_dados);
        editarEmailAlterarDados.setFocusable(false);

        //carregando usuario do firebase
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        firebaseUser = UsuarioFirebase.getUsuarioAtual();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
