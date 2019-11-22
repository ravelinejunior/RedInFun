package br.com.raveline.redinfunusers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
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
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.util.Objects;
import helper.ConfiguracaoFirebase;
import helper.Permissao;
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
    private String identificadorUsuario;

    //Firebase
    private FirebaseUser firebaseUser;
    private FirebaseAuth autenticacao;
    private StorageReference storageRef;

    //lista de permissoes
    private String[] listaPermissoesNecessarias = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final int CODIGO_REQUISICAO_GALERIA = 200;

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

        //requisitar permissoes
        Permissao.validarPermissoes(listaPermissoesNecessarias,this,1);

        //configurações iniciais do usuario
        usuarioLogado = UsuarioFirebase.getUsuarioLogado();
        storageRef = ConfiguracaoFirebase.getStorageReference();
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();

        //atualizando nome do usuario no banco de dados
        usuarioLogado.salvarDados();
        //usuario do firebase ja foi carregado no metodo carregarElementos()
            try {

                editarNomeAlterarDados.setText(Objects.requireNonNull(firebaseUser.getDisplayName()));
                String nomeExibido = editarNomeAlterarDados.getText().toString();
                if (nomeExibido.length() > 30){
                    editarEmailAlterarDados.setText("");
                }
                editarEmailAlterarDados.setText(firebaseUser.getEmail());
            }catch (Exception e){
                e.getStackTrace();
            }

            Uri url = firebaseUser.getPhotoUrl();
            if (url !=null){
                Glide.with(AlterarDados.this).load(url).fitCenter().into(fotoPerfilAlterarDados);
            } else{
                fotoPerfilAlterarDados.setImageResource(R.drawable.ic_pessoa_usuario);
            }

        botaoAlterarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recuperar nome atualizado
                String nomeAtualizado = editarNomeAlterarDados.getText().toString();
                UsuarioFirebase.atualizarNomeUsuario(nomeAtualizado);

                //atualizar o nome no Firebase
                usuarioLogado.setNome(nomeAtualizado);
                usuarioLogado.setNomeUsuarioPesquisa(nomeAtualizado);
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
                            child(identificadorUsuario+".jpeg");

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
                            //recuperar local da foto
                                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    atualizarFoto(uri);
                                }
                            });

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

    private void atualizarFoto(Uri uri) {
            //atualizar foto no perfil
                UsuarioFirebase.atualizarFotoUsuario(uri);

        //atualizar foto no firebase
        usuarioLogado.setCaminhoFoto(uri.toString());
        usuarioLogado.atualizarDados();
        Toast.makeText(this, "Foto atualizada", Toast.LENGTH_SHORT).show();
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
