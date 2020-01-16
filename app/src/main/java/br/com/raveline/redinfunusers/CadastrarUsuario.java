package br.com.raveline.redinfunusers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;

import java.util.Objects;

import helper.ConfiguracaoFirebase;
import helper.UsuarioFirebase;
import model.Usuario;

public class CadastrarUsuario extends AppCompatActivity {

    private Button botaoCadastroUsuario;
    private EditText emailCadastroUsuario;
    private EditText idadeCadastroUsuario;
    private EditText nomeCadastroUsuario;
    private EditText senhaCadastroUsuario;
    private EditText alturaCadastroUsuario;
    private EditText pesoCadastroUsuario;
    private EditText localCadastroUsuario;
    private EditText descricaoCadastroUsuario;
    private EditText telefoneCadastroUsuario;
    private EditText confirmarSenhaCadastroUsuario;
    private ProgressBar progressBarCadastroUsuario;

    //classe de Usuario
    private Usuario usuario;

    //atributos Firebase
    private FirebaseAuth autenticacao;
    private DatabaseReference firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);
        //carrega componentes R
        carregarComponentes();

        //carrega a referencia de Firebase
        firebaseRef = ConfiguracaoFirebase.getReferenciaDatabase();

        progressBarCadastroUsuario.setVisibility(View.GONE);
        botaoCadastroUsuario.setOnClickListener(view -> {
            //exibir progressbar
            String nomeUsuarioDigitado = nomeCadastroUsuario.getText().toString();
            String emailUsuarioDigitado = emailCadastroUsuario.getText().toString();
            String idadeUsuarioDigitado = idadeCadastroUsuario.getText().toString();
            String senhaUsuarioDigitado = senhaCadastroUsuario.getText().toString();
            String telefoneUsuarioDigitado = telefoneCadastroUsuario.getText().toString();
            String alturaUsuarioDigitado = alturaCadastroUsuario.getText().toString();
            String pesoUsuarioDigitado = pesoCadastroUsuario.getText().toString();
            String descricaoUsuarioDigitado = descricaoCadastroUsuario.getText().toString();
            String localUsuarioDigitado = localCadastroUsuario.getText().toString();

            //verificar condição dos campos
            if(!nomeUsuarioDigitado.isEmpty()){
                if(!emailUsuarioDigitado.isEmpty()){
                    if(!idadeUsuarioDigitado.isEmpty()){
                        if(!senhaUsuarioDigitado.isEmpty()){
                            usuario = new Usuario();
                            usuario.setNome(nomeUsuarioDigitado);
                            usuario.setEmail(emailUsuarioDigitado);
                            usuario.setSenha(senhaUsuarioDigitado);
                            usuario.setIdade(idadeUsuarioDigitado);
                            usuario.setTelefoneUsuario(telefoneUsuarioDigitado);
                            usuario.setLocalUsuario(localUsuarioDigitado);
                            usuario.setDescricaoUsuario(descricaoUsuarioDigitado);
                            usuario.setPesoUsuario(pesoUsuarioDigitado);
                            usuario.setAlturaUsuario(alturaUsuarioDigitado);
                            //metodo Cadastrar
                            cadastrarUsuario(usuario);

                        }else{
                            Toast.makeText(CadastrarUsuario.this, "Campo SENHA está vazio.\nFavor digitar seu nome.", Toast.LENGTH_SHORT).show();
                            progressBarCadastroUsuario.setVisibility(View.GONE);
                        }
                    }else{
                        Toast.makeText(CadastrarUsuario.this, "Campo IDADE está vazio.\nFavor digitar sua idade.", Toast.LENGTH_SHORT).show();
                        progressBarCadastroUsuario.setVisibility(View.GONE);
                    }
                }else{
                    Toast.makeText(CadastrarUsuario.this, "Campo EMAIL está vazio.\nFavor digitar seu email.", Toast.LENGTH_SHORT).show();
                    progressBarCadastroUsuario.setVisibility(View.GONE);
                }
            }else{
                Toast.makeText(CadastrarUsuario.this, "Campo NOME está vazio.\nFavor digitar seu nome.", Toast.LENGTH_SHORT).show();
                progressBarCadastroUsuario.setVisibility(View.GONE);
            }
        });

    }

    private void carregarComponentes(){
        botaoCadastroUsuario = findViewById(R.id.botao_cadastro_cadastrar);
        emailCadastroUsuario = findViewById(R.id.email_id_cadastrar);
        idadeCadastroUsuario = findViewById(R.id.idade_id_cadastrar);
        nomeCadastroUsuario = findViewById(R.id.nome_id_cadastrar);
        senhaCadastroUsuario = findViewById(R.id.senha_id_cadastrar);
        progressBarCadastroUsuario = findViewById(R.id.progressBar_cadastro);
        alturaCadastroUsuario = findViewById(R.id.altura_id_cadastrar);
        pesoCadastroUsuario = findViewById(R.id.peso_id_cadastrar);
        descricaoCadastroUsuario = findViewById(R.id.descricao_id_cadastrar);
        localCadastroUsuario = findViewById(R.id.local_id_cadastrar);
        confirmarSenhaCadastroUsuario = findViewById(R.id.confirmar_senha_id_cadastrar);
        telefoneCadastroUsuario = findViewById(R.id.telefone_id_cadastrar);

    }

    private void cadastrarUsuario(final Usuario usuario) {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String confirmaSenha = confirmarSenhaCadastroUsuario.getText().toString();
        if (confirmaSenha.equals(usuario.getSenha())) {


            try {
                progressBarCadastroUsuario.setVisibility(View.VISIBLE);

                //criando autenticação via email e senha
                autenticacao.createUserWithEmailAndPassword(
                        usuario.getEmail(), usuario.getSenha()
                ).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(CadastrarUsuario.this, "Usuario " + usuario.getNome().toUpperCase() + " cadastrado.", Toast.LENGTH_SHORT).show();
                        //caso usuario tenha sido cadastrado , sucesso.
                        progressBarCadastroUsuario.setVisibility(View.GONE);

                        //Cadastrando e configurando os dados na Firebase
                        String idUsuario = Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getUid();
                        usuario.setId(idUsuario);
                        usuario.salvarDados();

                        //Salvar os dados no cadastro no profile do Firebase
                        UsuarioFirebase.atualizarNomeUsuario(usuario.getNome());
                        Intent intent = new Intent(CadastrarUsuario.this, MainActivity.class);
                        startActivity(intent);
                        finish();


                    } else {
                        progressBarCadastroUsuario.setVisibility(View.GONE);
                        //tratando os erros
                        String erro = "";
                        try {
                            throw Objects.requireNonNull(task.getException());

                        } catch (FirebaseAuthEmailException e) {
                            erro = "Autenticação falhou.";

                        } catch (FirebaseAuthWeakPasswordException e) {
                            erro = "Senha fraca.";

                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            erro = "Digite um email válido.";

                        } catch (FirebaseAuthUserCollisionException e) {
                            erro = "Usuario já cadastrado.";
                        } catch (Exception e) {
                            erro = "Erro de autenticação: " + e.getMessage();
                            e.printStackTrace();
                        }

                        Toast.makeText(CadastrarUsuario.this, "Erro: " + erro, Toast.LENGTH_LONG).show();

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(this, "Senhas divergentes, favor digitar senhas iguais.", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
