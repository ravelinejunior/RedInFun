package br.com.raveline.redinfunusers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import model.Usuario;

public class CadastrarUsuario extends AppCompatActivity {

    private Button botaoCadastroUsuario;
    private EditText emailCadastroUsuario;
    private EditText idadeCadastroUsuario;
    private EditText nomeCadastroUsuario;
    private EditText senhaCadastroUsuario;
    private ProgressBar progressBarCadastroUsuario;

    //classe de Usuario
    public Usuario usuario;

    //atributos Firebase
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);
        //carrega componentes R
        carregarComponentes();

        progressBarCadastroUsuario.setVisibility(View.GONE);
        botaoCadastroUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //exibir progressbar
                progressBarCadastroUsuario.setVisibility(View.VISIBLE);
                String nomeUsuarioDigitado = nomeCadastroUsuario.getText().toString();
                String emailUsuarioDigitado = emailCadastroUsuario.getText().toString();
                String idadeUsuarioDigitado = idadeCadastroUsuario.getText().toString();
                String senhaUsuarioDigitado = senhaCadastroUsuario.getText().toString();

                //verificar condição dos campos
                if(!nomeUsuarioDigitado.isEmpty()){
                    if(!emailUsuarioDigitado.isEmpty()){
                        if(!idadeUsuarioDigitado.isEmpty()){
                            if(!senhaUsuarioDigitado.isEmpty()){
                                usuario = new Usuario();
                                usuario.setNome(nomeUsuarioDigitado);
                                usuario.setEmail(emailUsuarioDigitado);
                                usuario.setSenha(senhaUsuarioDigitado);
                                //metodo Cadastrar
                                cadastrarUsuario(usuario);

                            }else{
                                Toast.makeText(CadastrarUsuario.this, "Campo senha está vazio.\nFavor digitar seu nome.", Toast.LENGTH_SHORT).show();
                                progressBarCadastroUsuario.setVisibility(View.GONE);
                            }
                        }else{
                            Toast.makeText(CadastrarUsuario.this, "Campo idade está vazio.\nFavor digitar seu nome.", Toast.LENGTH_SHORT).show();
                            progressBarCadastroUsuario.setVisibility(View.GONE);
                        }
                    }else{
                        Toast.makeText(CadastrarUsuario.this, "Campo email está vazio.\nFavor digitar seu nome.", Toast.LENGTH_SHORT).show();
                        progressBarCadastroUsuario.setVisibility(View.GONE);
                    }
                }else{
                    Toast.makeText(CadastrarUsuario.this, "Campo nome está vazio.\nFavor digitar seu nome.", Toast.LENGTH_SHORT).show();
                    progressBarCadastroUsuario.setVisibility(View.GONE);
                }
            }


        });




    }

    public void carregarComponentes(){
        botaoCadastroUsuario = findViewById(R.id.botao_cadastrar_cadastro);
        emailCadastroUsuario = findViewById(R.id.email_id_cadastrar);
        idadeCadastroUsuario = findViewById(R.id.idade_id_cadastrar);
        nomeCadastroUsuario = findViewById(R.id.nome_id_cadastrar);
        senhaCadastroUsuario = findViewById(R.id.senha_id_cadastrar);
        progressBarCadastroUsuario = findViewById(R.id.progressBar_cadastro);

    }

    public void cadastrarUsuario(Usuario usuario) {


    }
}
