package activities.usuario;


import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
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

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import activities.view.MainActivity;
import br.com.raveline.redinfunusers.R;
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
    private StorageReference storageRef;

    //lista de permissoes
    private final String[] listaPermissoesNecessarias = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    //classe Usuario
    private Usuario usuarioLogado;

    //dados camera
    private static final int CODIGO_GALERIA_FOTO = 100;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_dados);

        //requisitar permissoes
        Permissao.validarPermissoes(listaPermissoesNecessarias,this,1);

        //configurações iniciais do usuario
        usuarioLogado = UsuarioFirebase.getUsuarioLogado();
        storageRef = ConfiguracaoFirebase.getStorageReference();
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();

        //carregar elementos
        carregarElementos();

        //carregando usuario do firebase
        FirebaseUser firebaseUser = UsuarioFirebase.getUsuarioAtual();

        //usuario do firebase ja foi carregado no metodo carregarElementos()
        try {
            firebaseUser = UsuarioFirebase.getUsuarioAtual();
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
        if (url != null){
            Glide.with(AlterarDados.this).load(url).into(fotoPerfilAlterarDados);
        } else{
            fotoPerfilAlterarDados.setImageResource(R.drawable.ic_pessoa_usuario);
        }

        fotoPerfilAlterarDados.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (intent.resolveActivity(getPackageManager())!= null){
                startActivityForResult(intent,CODIGO_GALERIA_FOTO);
            }

        });

        botaoAlterarDados.setOnClickListener(v -> {
            //recuperar nome atualizado
            String nomeAtualizado = editarNomeAlterarDados.getText().toString();
            UsuarioFirebase.atualizarNomeUsuario(nomeAtualizado);

            //atualizar o nome no Firebase
            usuarioLogado.setNome(nomeAtualizado);
            usuarioLogado.setNomeUsuarioPesquisa(nomeAtualizado);
            usuarioLogado.atualizarDados();
            Toast.makeText(AlterarDados.this, "Nome alterado", Toast.LENGTH_SHORT).show();

        });

        //alterar foto do usuario
        editarFotoAlterarDados.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (intent.resolveActivity(getPackageManager())!=null){
                startActivityForResult(intent,CODIGO_GALERIA_FOTO);
            }

        });

        imagemBotaoVoltar.setOnClickListener(v -> {
            startActivity(new Intent(AlterarDados.this,(MainActivity.class)));
            finish();
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
                        Uri localImagemSelecionada = Objects.requireNonNull(data).getData();
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
                    uploadTask.addOnFailureListener(e -> {
                        Toast.makeText(AlterarDados.this, "Falha ao executar o comando para fazer upload da imagem.", Toast.LENGTH_SHORT).show();
                        progressBarAlterarDados.setVisibility(View.GONE);
                    }).addOnProgressListener(taskSnapshot -> progressBarAlterarDados.setVisibility(View.VISIBLE)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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

    private void carregarElementos(){
        editarEmailAlterarDados = findViewById(R.id.email_alterar_perfil);
        editarNomeAlterarDados = findViewById(R.id.nome_alterar_perfil);
        editarFotoAlterarDados = findViewById(R.id.alterar_foto_perfil);
        fotoPerfilAlterarDados = findViewById(R.id.imagem_alterar_perfil);
        botaoAlterarDados = findViewById(R.id.botao_alterar_perfil);
        progressBarAlterarDados = findViewById(R.id.progressBar_alterar_dados);
        imagemBotaoVoltar = findViewById(R.id.voltar_tela_topo_alterar_dados);
        editarEmailAlterarDados.setFocusable(false);

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
