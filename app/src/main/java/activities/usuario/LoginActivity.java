package activities.usuario;

import android.app.AlertDialog;
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

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;

import java.util.Objects;

import activities.view.MainActivity;
import br.com.raveline.redinfunusers.R;
import helper.ConfiguracaoFirebase;
import model.Usuario;

import static android.view.View.GONE;

public class LoginActivity extends AppCompatActivity {
    private Button botaoLogarLogin;
    private SignInButton botaoGoogleLogin;
    private TextInputEditText emailLogarLogin;
    private TextInputEditText senhaLogarLogin;
    private ProgressBar progressBarLogin;

    //login google
    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient mGoogleSignInClient;
    private TextView esqueceuSenhaLogin;

    //Firebase
    private FirebaseAuth autenticacao;
    private FirebaseUser user;
    private DatabaseReference databaseReference;


    //Usuario
    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        verificarUsuarioLogado();
        carregarElementos();
        progressBarLogin.setVisibility(GONE);

        botaoLogarLogin.setOnClickListener(view -> {
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
        });



        //configurando botao para reenviar recuperação de senha
        esqueceuSenhaLogin.setOnClickListener(v -> esqueceuSenha());


        // Configure Google Sign In
        //Tem que ser antes do Firebase Autentication
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        //cadastrando usuario atraves da conta Google
        botaoGoogleLogin.setOnClickListener(view -> {

            progressBarLogin.setVisibility(View.VISIBLE);
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);

        });


    }

    //METODO PARA Abrir o linear layout de recuperação de senha
    private void esqueceuSenha() {
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
        linearLayout.setPadding(10, 10, 10, 10);
        builder.setView(linearLayout);

        //botoes para recuperar
        builder.setPositiveButton("Recuperar", (dialogInterface, i) -> {
            if (emailDigitadoNovo == null) {
                Toast.makeText(this, "Não deixe o campo em branco!", Toast.LENGTH_SHORT).show();
            } else {
                String emailRecuperado = emailDigitadoNovo.getText().toString();
                progressBarLogin.setVisibility(View.VISIBLE);
                recuperarSenha(emailRecuperado);
            }


        });

        //botao para cancelar
        builder.setNegativeButton("Cancelar", (dialogInterface, i) -> {
        });

        builder.create().show();
    }


    private void recuperarSenha(String email) {
        try {


            autenticacao.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    progressBarLogin.setVisibility(GONE);
                    Toast.makeText(LoginActivity.this, "Email enviado.", Toast.LENGTH_SHORT).show();
                } else {
                    progressBarLogin.setVisibility(GONE);
                    Toast.makeText(LoginActivity.this, "Falha ao enviar.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                progressBarLogin.setVisibility(GONE);
                //mostrar o erro
                Toast.makeText(LoginActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Verifique se o campo email está digitado corretamente.", Toast.LENGTH_SHORT).show();
            progressBarLogin.setVisibility(GONE);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    //verificar se usuario está logado
    private void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if(autenticacao.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
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
                firebaseAuthWithGoogle(Objects.requireNonNull(account));
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
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                       progressBarLogin.setVisibility(View.GONE);
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = autenticacao.getCurrentUser();

                        //se o usuario logar primeiro antes das informações carregarem
                        if (Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getAdditionalUserInfo()).isNewUser()) {

                            //recuperando valores e ids do usuario
                            String emailUser = Objects.requireNonNull(user).getEmail();
                             String uidUser = user.getUid();

                        }


                        Toast.makeText(LoginActivity.this, "Usuario: "+ Objects.requireNonNull(user).getDisplayName()+" Logado.", Toast.LENGTH_SHORT).show();
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
                }).addOnFailureListener(e -> {
                    progressBarLogin.setVisibility(GONE);
                    autenticacao.signOut();
                    Toast.makeText(LoginActivity.this, "Erro: "+e.getMessage(), Toast.LENGTH_LONG).show();
                });


    }

    private void validarUsuario(Usuario usuario) {
        progressBarLogin.setVisibility(View.VISIBLE);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        //Logando com usuario digitado
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(),usuario.getSenha()).
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        progressBarLogin.setVisibility(GONE);
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(e -> {
                    progressBarLogin.setVisibility(GONE);
                    Toast.makeText(LoginActivity.this, "Erro "+e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void abrirCadastro(View v) {
        Intent intent = new Intent(LoginActivity.this, CadastrarUsuario.class);
        startActivity(intent);

    }

    private void carregarElementos(){
        botaoLogarLogin = findViewById(R.id.botao_logar_login);
//        cadastrarTextoLogin = findViewById(R.id.cadastrar_text_login);
        emailLogarLogin = findViewById(R.id.email_id_login);
        senhaLogarLogin = findViewById(R.id.senha_id_login);
        progressBarLogin = findViewById(R.id.progressBar_login);
        botaoGoogleLogin = findViewById(R.id.botao_logar_google_login);
        esqueceuSenhaLogin = findViewById(R.id.esqueceu_senha_login);
        databaseReference = ConfiguracaoFirebase.getReferenciaDatabase();
        //   botaoLogarAnonimo = findViewById(R.id.botao_logar_anonimamente_id);
    }

}
