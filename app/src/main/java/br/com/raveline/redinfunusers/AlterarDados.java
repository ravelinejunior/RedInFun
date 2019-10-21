package br.com.raveline.redinfunusers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
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
    private StorageReference storageRef;

    //classe Usuario
    Usuario usuarioLogado;

    //dados camera
    public static final int CODIGO_GALERIA_FOTO = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_dados);
        //carregar elementos
        carregarElementos();

        //configurações iniciais do usuario
        usuarioLogado = UsuarioFirebase.getUsuarioLogado();
        storageRef = ConfiguracaoFirebase.getStorageReference();

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

            //alterar foto do usuario
            editarFotoAlterarDados.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    if (intent.resolveActivity(getPackageManager())!=null){
                        startActivityForResult(intent,CODIGO_GALERIA_FOTO);
                    }

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


    //on result para recuperar dados para setar imagem no perfil
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            //preenchendo valores Bitmap
            Bitmap bitmapImagem = null;

            try {
                //selecionar apenas da galeria
                switch (requestCode){
                    case CODIGO_GALERIA_FOTO:
                        Uri localImagemSelecionada = data.getData();
                        bitmapImagem = MediaStore.Images.Media.getBitmap(getContentResolver(),localImagemSelecionada);
                    break;
                }

                //configurando caso usuario tenha selecionado uma imagem
                if (bitmapImagem != null){
                    progressBarAlterarDados.setVisibility(View.VISIBLE);
                    //configurando imagem de perfil
                    fotoPerfilAlterarDados.setImageBitmap(bitmapImagem);

                    //recuperar dados da imagem para setar no firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmapImagem.compress(Bitmap.CompressFormat.JPEG,75,baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //salvando dados da imagem no firebase
                    StorageReference imagemRef = storageRef.
                            child("imagens").
                            child("perfil").
                            child("<id-usuario>.jpeg");

                    //passar um array de bytes no putbytes da imagem
                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AlterarDados.this, "Falha ao executar o comando para fazer upload da imagem.", Toast.LENGTH_SHORT).show();
                            progressBarAlterarDados.setVisibility(View.GONE);
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            progressBarAlterarDados.setVisibility(View.VISIBLE);
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(AlterarDados.this, "Realizado com sucesso.", Toast.LENGTH_SHORT).show();
                            progressBarAlterarDados.setVisibility(View.GONE);
                        }
                    });

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
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
