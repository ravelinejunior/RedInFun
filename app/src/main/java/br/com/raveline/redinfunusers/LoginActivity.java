package br.com.raveline.redinfunusers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

import helper.ConfiguracaoFirebase;
import helper.UsuarioFirebase;
import model.Usuario;

import static android.view.View.GONE;

public class LoginActivity extends AppCompatActivity {
    private Button botaoLogarLogin;
    private SignInButton botaoGoogleLogin;
    private TextView cadastrarTextoLogin;
    private EditText emailLogarLogin;
    private EditText senhaLogarLogin;
    private ProgressBar progressBarLogin;

    //login google
    private static final int RC_SIGN_IN = 100;
    GoogleSignInClient mGoogleSignInClient;
    private TextView esqueceuSenhaLogin;

    //Firebase
    private FirebaseAuth autenticacao;
    private DatabaseReference databaseReference;

    //Usuario
    public Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        verificarUsuarioLogado();
        carregarElementos();
        progressBarLogin.setVisibility(GONE);
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

        //configurando botao para reenviar recuperação de senha
        esqueceuSenhaLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esqueceuSenha();
            }
        });


        // Configure Google Sign In
        //Tem que ser antes do Firebase Autentication
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        //cadastrando usuario atraves da conta Google
        botaoGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBarLogin.setVisibility(View.VISIBLE);
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });


    }

    //METODO PARA Abrir o linear layout de recuperação de senha
    private void esqueceuSenha(){
        //Alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recuperar a senha");

        //setar layout linear
        LinearLayout linearLayout = new LinearLayout(this);

        //views do alertdialog
        final EditText emailDigitadoNovo = new EditText(this);
        emailDigitadoNovo.setHint("Digite seu Email");
        emailDigitadoNovo.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailDigitadoNovo.setMinEms(20);
        linearLayout.addView(emailDigitadoNovo);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);

        //botoes para recuperar
        builder.setPositiveButton("Recuperar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String emailRecuperado = emailDigitadoNovo.getText().toString();
                progressBarLogin.setVisibility(View.VISIBLE);
                recuperarSenha(emailRecuperado);

            }
        });

        //botao para cancelar
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        builder.create().show();
    }

    private void recuperarSenha(String email){

        autenticacao.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressBarLogin.setVisibility(GONE);
                    Toast.makeText(LoginActivity.this, "Email enviado.", Toast.LENGTH_SHORT).show();
                }
                else{
                    progressBarLogin.setVisibility(GONE);
                    Toast.makeText(LoginActivity.this, "Falha ao enviar.", Toast.LENGTH_SHORT).show();
                }
            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBarLogin.setVisibility(GONE);
                //mostrar o erro
                Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
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

    // O código do cliente de tipo Aplicativo da Web é o código do cliente OAuth 2.0 do servidor de back-end
    // Você precisa enviar o ID do cliente do servidor para o método requestIdToken
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }


    //Depois que um usuário se conectar, receba um token de código do objeto GoogleSignInAccount, troque-o por uma credencial do Firebase e faça a autenticação

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        autenticacao.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                           progressBarLogin.setVisibility(View.GONE);
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = autenticacao.getCurrentUser();

                            //se o usuario logar primeiro antes das informações carregarem
                            if (task.getResult().getAdditionalUserInfo().isNewUser()) {

                                //recuperando valores e ids do usuario
                                String emailUser = user.getEmail();
                                 String uidUser = user.getUid();

                            }


                            Toast.makeText(LoginActivity.this, "Usuario: "+user.getDisplayName()+" Logado.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();

                            //updateUI(user);
                        } else {
                            progressBarLogin.setVisibility(GONE);
                            // If sign in fails, display a message to the user.
                            // updateUI(null);


                            FirebaseAuth.getInstance().signOut();
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBarLogin.setVisibility(GONE);
                autenticacao.signOut();
                Toast.makeText(LoginActivity.this, "Erro: "+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


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
                    progressBarLogin.setVisibility(GONE);
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBarLogin.setVisibility(GONE);
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
        botaoGoogleLogin = findViewById(R.id.botao_logar_google_login);
        esqueceuSenhaLogin = findViewById(R.id.esqueceu_senha_login);
        databaseReference = ConfiguracaoFirebase.getReferenciaDatabase();
    }

}
