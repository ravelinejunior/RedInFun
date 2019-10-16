package br.com.raveline.redinfunusers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import helper.ConfiguracaoFirebase;
import model.Usuario;

public class LoginActivity extends AppCompatActivity {
    private Button botaoLogarLogin;
    private TextView cadastrarTextoLogin;
    private EditText emailLogarLogin;
    private EditText senhaLogarLogin;
    private ProgressBar progressBarLogin;

    //Firebase
    private FirebaseAuth autenticacao;

    //Usuario
    public Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        verificarUsuarioLogado();
        carregarElementos();
        progressBarLogin.setVisibility(View.GONE);
        botaoLogarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailDigitado = emailLogarLogin.getText().toString();
                String senhaDigitada = senhaLogarLogin.getText().toString();
                if (!emailDigitado.isEmpty()){
                    if (!senhaDigitada.isEmpty()){
                        usuario = new Usuario();
                        usuario.setEmail(emailDigitado);
                        usuario.setSenha(senhaDigitada);
                        validarUsuario(usuario);
                    }else{
                        Toast.makeText(LoginActivity.this, "Campo SENHA está vazio.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "Campo EMAIL está vazio.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //verificar se usuario está logado
    public void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if(autenticacao.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
    }

    public void validarUsuario(Usuario usuario) {
        progressBarLogin.setVisibility(View.VISIBLE);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        //Logando com usuario digitado
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(),usuario.getSenha()).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressBarLogin.setVisibility(View.GONE);
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBarLogin.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Erro "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void abrirCadastro(View v) {
        Intent intent = new Intent(LoginActivity.this, CadastrarUsuario.class);
        startActivity(intent);

    }

    public void carregarElementos(){
        botaoLogarLogin = findViewById(R.id.botao_logar_login);
        cadastrarTextoLogin = findViewById(R.id.cadastrar_text_login);
        emailLogarLogin = findViewById(R.id.email_id_login);
        senhaLogarLogin = findViewById(R.id.senha_id_login);
        progressBarLogin = findViewById(R.id.progressBar_login);
    }

}
